package com.emorn.bettercables.api.v1_12_2.gui.elements;

import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiNumberInput extends GuiButton implements AbleToChangeDisabledState
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );
    private static final int WIDTH_OF_IMAGE = 27;
    private static final int HEIGHT_OF_IMAGE = 13;
    private static final int PLUS_BUTTON_WIDTH = 5;
    private static final int PLUS_BUTTON_HEIGHT = 5;
    private static final int MINUS_BUTTON_WIDTH = 5;
    private static final int MINUS_BUTTON_HEIGHT = 5;
    private static final int BACK_SPACE_CODE = 14;
    private final int minimumValue;
    private final int maximumValue;
    private static final int MAXIMUM_CHARACTER_LENGTH = 3;
    private int value;
    private String valueString;
    private final Consumer<GuiTooltipData> callback;
    private boolean isFocused;
    private int plusButtonY = 0;
    private int minusButtonY = 0;
    private final String[] description;
    private boolean disabled = false;
    private final TextPosition textPosition;
    private int extraY = 0;
    private boolean wasCursorOverInputBox;

    public GuiNumberInput(
        int buttonId,
        int x,
        int y,
        int initialValue,
        TextPosition textPosition,
        String text,
        String[] description,
        int minimumValue,
        int maximumValue,
        boolean disabled,
        Consumer<GuiTooltipData> callback
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, text);
        this.value = initialValue;
        this.description = description;
        this.disabled = disabled;
        this.textPosition = textPosition;

        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.valueString = Integer.toString(initialValue);
        this.callback = callback;
        this.plusButtonY = this.y + 1;
        this.minusButtonY = this.y + 6;
    }

    private int minusButtonX()
    {
        return this.x + WIDTH_OF_IMAGE - PLUS_BUTTON_WIDTH - 1;
    }

    private int plusButtonX()
    {
        return this.x + WIDTH_OF_IMAGE - PLUS_BUTTON_WIDTH - 1;
    }

    @Override
    public void drawButton(
        Minecraft mc,
        int mouseX,
        int mouseY,
        float partialTicks
    )
    {
        if (!this.visible) {
            return;
        }
        int stringWidth = mc.fontRenderer.getStringWidth(valueString);
        mc.getTextureManager().bindTexture(TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (this.textPosition.equals(TextPosition.TOP)) {
            extraY = mc.fontRenderer.FONT_HEIGHT;
        }


        if (this.disabled) {
            drawTexturedModalRect(this.x, this.y + extraY, 61, 13, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE); // input box
        } else {
            drawTexturedModalRect(this.x, this.y + extraY, 61, 0, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE); // input box
        }

        drawTexturedModalRect(this.plusButtonX(), this.plusButtonY + extraY, 55, 0, 5, 5); // plus
        drawTexturedModalRect(this.minusButtonX(), this.minusButtonY + extraY, 55, 6, 5, 5); // minus

        mc.fontRenderer.drawString(
            valueString,
            this.x + (WIDTH_OF_IMAGE - stringWidth) - 6,
            (this.y + (this.height - mc.fontRenderer.FONT_HEIGHT) / 2) + extraY,
            Reference.TEXT_COLOR
        );

        if (this.textPosition.equals(TextPosition.RIGHT)) {
            mc.fontRenderer.drawString(
                displayString,
                this.x + this.width + 5,
                this.y + (this.height - mc.fontRenderer.FONT_HEIGHT) / 2,
                Reference.TEXT_COLOR
            );
        } else if (this.textPosition.equals(TextPosition.TOP)) {
            mc.fontRenderer.drawString(
                displayString,
                this.x,
                this.y,
                Reference.TEXT_COLOR
            );
        }

        boolean isCursorOverInputBox = this.isMouseOverInputBox(mouseX, mouseY);

        GuiTooltipData guiTooltipData = new GuiTooltipData(
            this.x,
            this.y + 1000,
            description,
            this.minimumValue,
            true,
            this.maximumValue,
            true,
            false
        );

        if (isCursorOverInputBox) {
            this.callback.accept(
                guiTooltipData
            );
        } else if (this.wasCursorOverInputBox) {
            guiTooltipData.setDisabled();
            this.callback.accept(
                guiTooltipData
            );
        }

        this.wasCursorOverInputBox = isCursorOverInputBox;
    }

    public void changeDisabledState(boolean disable)
    {
        this.disabled = disable;
    }

    @Override
    public boolean mousePressed(
        Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        if (!this.visible || !this.enabled || this.disabled) {
            this.isFocused = false;
            return false;
        }

        if (this.isPlusButtonPressed(mouseX, mouseY)) {
            this.value = Math.min(this.value + 1, this.maximumValue);
            this.updateValue();
            this.isFocused = false;
            return true;
        }

        if (this.isMinusButtonPressed(mouseX, mouseY)) {
            this.value = Math.max(this.value - 1, minimumValue);
            this.updateValue();
            this.isFocused = false;
            return true;
        }

        this.isFocused =
            (mouseX >= this.x && mouseX < this.x + this.width) &&
                (mouseY >= this.y + extraY && mouseY < this.y + this.height + extraY);

        return false;
    }

    private boolean isMouseOverInputBox(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.x && mouseX < this.x + WIDTH_OF_IMAGE) &&
                (mouseY > this.y + extraY && mouseY < this.y + HEIGHT_OF_IMAGE + extraY)
            ;
    }

    private boolean isPlusButtonPressed(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.plusButtonX() && mouseX < this.plusButtonX() + PLUS_BUTTON_WIDTH) &&
                (mouseY > this.plusButtonY + extraY && mouseY < this.plusButtonY + PLUS_BUTTON_HEIGHT + extraY)
            ;
    }

    private void updateValue()
    {
        this.valueString = Integer.toString(this.value);
    }

    private boolean isMinusButtonPressed(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.minusButtonX() && mouseX < this.minusButtonX() + MINUS_BUTTON_WIDTH) &&
                (mouseY > this.minusButtonY + extraY && mouseY < this.minusButtonY + MINUS_BUTTON_HEIGHT + extraY)
            ;
    }

    public boolean keyTyped(
        char typedChar,
        int keyCode
    )
    {
        if (!this.visible || !this.enabled || !this.isFocused || this.disabled) {
            return false;
        }

        if (keyCode == BACK_SPACE_CODE && !this.valueString.isEmpty()) {
            this.valueString = this.valueString.substring(0, this.valueString.length() - 1);
            if (this.valueString.equals("-")) {
                this.valueString = "";
            }
            this.value = this.valueString.isEmpty() ? 0 : Integer.parseInt(this.valueString);
            return true;
        }

        if (!Character.isDigit(typedChar)) {
            return false;
        }

        if (this.valueString.length() < MAXIMUM_CHARACTER_LENGTH) {
            this.valueString += typedChar;
            int newValue = Math.max(
                Math.min(Integer.parseInt(this.valueString), this.maximumValue),
                this.minimumValue
            );
            this.valueString = Integer.toString(newValue);
            this.value = newValue;

            return true;
        }

        return false;
    }

    public int value()
    {
        return this.value;
    }
}