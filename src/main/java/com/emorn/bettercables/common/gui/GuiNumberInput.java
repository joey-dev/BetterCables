package com.emorn.bettercables.common.gui;

import com.emorn.bettercables.utils.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiNumberInput extends GuiButton
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
    private static final int MINIMUM_VALUE = 0;
    private static final int MAXIMUM_VALUE = 999;
    private static final int MAXIMUM_CHARACTER_LENGTH = 3;
    private int value;
    private String valueString;
    private boolean isFocused;
    private int plusButtonX = 0;
    private int plusButtonY = 0;
    private int minusButtonX = 0;
    private int minusButtonY = 0;

    public GuiNumberInput(
        int buttonId,
        int x,
        int y,
        int initialValue,
        String text
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, text);
        this.value = initialValue;

        this.valueString = Integer.toString(initialValue);
        this.plusButtonX = this.x + WIDTH_OF_IMAGE - PLUS_BUTTON_WIDTH - 1;
        this.plusButtonY = this.y + 1;
        this.minusButtonX = this.x + WIDTH_OF_IMAGE - PLUS_BUTTON_WIDTH - 1;
        this.minusButtonY = this.y + 6;
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
        drawTexturedModalRect(this.x, this.y, 61, 0, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE); // input box
        drawTexturedModalRect(this.plusButtonX, this.plusButtonY, 55, 0, 5, 5); // plus
        drawTexturedModalRect(this.minusButtonX, this.minusButtonY, 55, 6, 5, 5); // minus

        mc.fontRenderer.drawString(
            valueString,
            this.x + (WIDTH_OF_IMAGE - stringWidth) - 6,
            this.y + (this.height - mc.fontRenderer.FONT_HEIGHT) / 2,
            Reference.TEXT_COLOR
        );

        mc.fontRenderer.drawString(
            displayString,
            this.x + this.width + 5,
            this.y + (this.height - mc.fontRenderer.FONT_HEIGHT) / 2,
            Reference.TEXT_COLOR
        );
    }

    @Override
    public boolean mousePressed(
        Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        if (!this.visible || !this.enabled) {
            this.isFocused = false;
            return false;
        }

        if (this.isPlusButtonPressed(mouseX, mouseY)) {
            this.value = Math.min(this.value + 1, MAXIMUM_VALUE);
            this.updateValue();
            this.isFocused = false;
            return true;
        }

        if (this.isMinusButtonPressed(mouseX, mouseY)) {
            this.value = Math.max(this.value - 1, MINIMUM_VALUE);
            this.updateValue();
            this.isFocused = false;
            return true;
        }

        this.isFocused =
            (mouseX >= this.x && mouseX < this.x + this.width) &&
                (mouseY >= this.y && mouseY < this.y + this.height);

        return false;
    }

    private boolean isPlusButtonPressed(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.plusButtonX && mouseX < this.plusButtonX + PLUS_BUTTON_WIDTH) &&
                (mouseY > this.plusButtonY && mouseY < this.plusButtonY + PLUS_BUTTON_HEIGHT)
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
            (mouseX > this.minusButtonX && mouseX < this.minusButtonX + MINUS_BUTTON_WIDTH) &&
                (mouseY > this.minusButtonY && mouseY < this.minusButtonY + MINUS_BUTTON_HEIGHT)
            ;
    }

    public void keyTyped(
        char typedChar,
        int keyCode
    )
    {
        if (!this.visible || !this.enabled || !this.isFocused) {
            return;
        }

        if (keyCode == BACK_SPACE_CODE && !this.valueString.isEmpty()) {
            this.valueString = this.valueString.substring(0, this.valueString.length() - 1);
            this.value = this.valueString.isEmpty() ? 0 : Integer.parseInt(this.valueString);
            return;
        }

        if (!Character.isDigit(typedChar)) {
            return;
        }

        if (this.valueString.length() < MAXIMUM_CHARACTER_LENGTH) {
            this.valueString += typedChar;
            int newValue = Math.min(Integer.parseInt(this.valueString), MAXIMUM_VALUE);
            this.valueString = Integer.toString(newValue);
            this.value = newValue;
        }
    }

    public int getValue()
    {
        return this.value;
    }
}