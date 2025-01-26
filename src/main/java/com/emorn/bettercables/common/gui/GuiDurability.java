package com.emorn.bettercables.common.gui;

import com.emorn.bettercables.utils.Reference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiDurability extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );

    private static final int WIDTH_OF_IMAGE = 27;
    private static final int HEIGHT_OF_IMAGE = 13;
    private final GuiComparisonOperatorButton button;

    public GuiDurability(
        int buttonId,
        int inputId,
        int x,
        int y,
        ComparisonOperator comparisonOperator,
        int initialValue
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, "Durability");

        this.button = new GuiComparisonOperatorButton(
            inputId,
            x,
            y,
            "Durability%",
            comparisonOperator,
            initialValue
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
    }

    public ComparisonOperator comparisonOperator()
    {
        return this.button.comparisonOperator();
    }

    public GuiComparisonOperatorButton operatorInput()
    {
        return this.button;
    }

    public GuiNumberInput numberInput()
    {
        return this.button.numberInput();
    }
}