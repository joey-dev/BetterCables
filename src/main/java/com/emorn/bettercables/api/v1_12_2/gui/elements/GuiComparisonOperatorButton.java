package com.emorn.bettercables.api.v1_12_2.gui.elements;

import com.emorn.bettercables.contract.gui.ComparisonOperator;
import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiComparisonOperatorButton extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );

    private static final int WIDTH_OF_IMAGE = 18;
    private static final int HEIGHT_OF_IMAGE = 18;

    private ComparisonOperator comparisonOperator;
    private final GuiNumberInput numberInput;
    private int stringWidth = 0;
    private boolean disabled;
    private final Consumer<GuiTooltipData> callback;
    private boolean wasCursorOverInputBox = false;

    public GuiComparisonOperatorButton(
        int buttonId,
        int x,
        int y,
        String buttonText,
        String[] description,
        ComparisonOperator comparisonOperator,
        int initialValue,
        int maximumValue,
        boolean disable,
        Consumer<GuiTooltipData> callback
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, buttonText);
        this.comparisonOperator = comparisonOperator;
        this.disabled = disable;
        this.callback = callback;

        this.numberInput = new GuiNumberInput(
            buttonId,
            x,
            y + 15,
            initialValue,
            TextPosition.RIGHT,
            "",
            description,
            -1,
            maximumValue,
            disable,
            callback
        );
    }

    @Override
    public void drawButton(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY,
        float partialTicks
    )
    {
        if (!this.visible) {
            return;
        }
        setupRenderState(
            mc,
            mouseX,
            mouseY
        );
    }

    private void setupRenderState(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        stringWidth = mc.fontRenderer.getStringWidth(this.displayString);
        mc.getTextureManager().bindTexture(TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (this.comparisonOperator.equals(ComparisonOperator.EQUALS)) {
            drawTexturedModalRect(
                this.x + stringWidth,
                this.y + 2,
                89,
                2,
                9,
                9
            );
        } else if (this.comparisonOperator.equals(ComparisonOperator.GREATER_THAN)) {
            drawTexturedModalRect(
                this.x + stringWidth,
                this.y + 2,
                97,
                2,
                9,
                9
            );
        } else if (this.comparisonOperator.equals(ComparisonOperator.LESS_THAN)) {
            drawTexturedModalRect(
                this.x + stringWidth,
                this.y + 2,
                104,
                2,
                9,
                9
            );
        }

        mc.fontRenderer.drawString(
            displayString,
            this.x,
            this.y + ((this.height - 3) - mc.fontRenderer.FONT_HEIGHT) / 2,
            Reference.TEXT_COLOR
        );

        boolean isCursorOverInputBox = this.isMouseOverInputBox(mouseX, mouseY, mc);

        GuiTooltipData guiTooltipData = new GuiTooltipData(
            this.x,
            this.y + 1000,
            new String[]{
                "Click to change the operator",
                "=: if the value is equal to the input",
                ">: if the value is greater than the input",
                "<: if the value is less than the input"
            },
            0,
            false,
            0,
            false,
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

    private boolean isMouseOverInputBox(
        int mouseX,
        int mouseY,
        net.minecraft.client.Minecraft mc
    )
    {
        return
            (mouseX > this.x && mouseX < this.x + (WIDTH_OF_IMAGE / 2) + mc.fontRenderer.getStringWidth(this.displayString)) &&
                (mouseY > this.y && mouseY < this.y + HEIGHT_OF_IMAGE)
            ;
    }

    @Override
    public boolean mousePressed(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        if (disabled || !this.isButtonPressed(mouseX, mouseY)) {
            return false;
        }

        if (this.comparisonOperator.equals(ComparisonOperator.EQUALS)) {
            this.comparisonOperator = ComparisonOperator.GREATER_THAN;
        } else if (this.comparisonOperator.equals(ComparisonOperator.GREATER_THAN)) {
            this.comparisonOperator = ComparisonOperator.LESS_THAN;
        } else if (this.comparisonOperator.equals(ComparisonOperator.LESS_THAN)) {
            this.comparisonOperator = ComparisonOperator.EQUALS;
        }

        return true;
    }


    private boolean isButtonPressed(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.x + this.stringWidth && mouseX < this.x + this.stringWidth + 9) &&
                (mouseY > this.y + 2 && mouseY < this.y + 2 + 9)
            ;
    }

    public ComparisonOperator comparisonOperator()
    {
        return comparisonOperator;
    }

    public GuiNumberInput numberInput()
    {
        return this.numberInput;
    }

    public void changeDisabledState(boolean disabled)
    {
        this.disabled = disabled;
        this.numberInput.changeDisabledState(disabled);
    }
}
