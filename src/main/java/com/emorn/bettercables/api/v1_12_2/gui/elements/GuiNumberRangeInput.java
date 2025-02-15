package com.emorn.bettercables.api.v1_12_2.gui.elements;

import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiNumberRangeInput extends GuiButton implements AbleToChangeDisabledState
{
    private static final int WIDTH_OF_IMAGE = 27;
    private static final int HEIGHT_OF_IMAGE = 13;
    private final GuiNumberInput minInput;
    private final GuiNumberInput maxInput;

    public GuiNumberRangeInput(
        int buttonId,
        int buttonIdMin,
        int buttonIdMax,
        int x,
        int y,
        int initialValueMin,
        int initialValueMax,
        String text,
        int minimumValue,
        boolean disabled
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, text);

        this.minInput = new GuiNumberInput(
            buttonIdMin,
            x,
            y + 9,
            initialValueMin,
            TextPosition.RIGHT,
            "Min",
            minimumValue,
            disabled
        );

        this.maxInput = new GuiNumberInput(
            buttonIdMax,
            x,
            y + 15 + 9,
            initialValueMax,
            TextPosition.RIGHT,
            "Max",
            minimumValue,
            disabled
        );
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
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        mc.fontRenderer.drawString(
            this.displayString,
            this.x,
            this.y,
            Reference.TEXT_COLOR
        );
    }

    public GuiNumberInput minInput()
    {
        return this.minInput;
    }

    public GuiNumberInput maxInput()
    {
        return this.maxInput;
    }

    public void changeDisabledState(boolean disable)
    {
        this.minInput.changeDisabledState(disable);
        this.maxInput.changeDisabledState(disable);
    }
}