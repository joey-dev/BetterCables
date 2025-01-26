package com.emorn.bettercables.common.gui;

import com.emorn.bettercables.utils.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiNumberRangeInput extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );
    private static final int WIDTH_OF_IMAGE = 27;
    private static final int HEIGHT_OF_IMAGE = 13;
    private GuiNumberInput minInput;
    private GuiNumberInput maxInput;

    public GuiNumberRangeInput(
        int buttonId,
        int buttonIdMin,
        int buttonIdMax,
        int x,
        int y,
        int initialValueMin,
        int initialValueMax,
        String text,
        boolean minus1Allowed
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, text);

        this.minInput = new GuiNumberInput(
            buttonIdMin,
            x,
            y + 9,
            initialValueMin,
            "Min",
            minus1Allowed
        );

        this.maxInput = new GuiNumberInput(
            buttonIdMax,
            x,
            y + 15 + 9,
            initialValueMax,
            "Max",
            minus1Allowed
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

    public int minValue()
    {
        return this.minInput.value();
    }

    public int maxValue()
    {
        return this.maxInput.value();
    }
}