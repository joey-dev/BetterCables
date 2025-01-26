package com.emorn.bettercables.common.gui;

import net.minecraft.client.gui.GuiButton;

public class GuiDurability extends GuiButton implements AbleToChangeDisabledState
{
    private static final int WIDTH_OF_IMAGE = 27;
    private static final int HEIGHT_OF_IMAGE = 13;
    private final GuiComparisonOperatorButton button;

    public GuiDurability(
        int buttonId,
        int inputId,
        int x,
        int y,
        ComparisonOperator comparisonOperator,
        int initialValue,
        boolean disable
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, "Durability");

        this.button = new GuiComparisonOperatorButton(
            inputId,
            x,
            y,
            "Durability%",
            comparisonOperator,
            initialValue,
            disable
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
        // not really a button, so it shouldn't draw one
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

    @Override
    public void changeDisabledState(boolean disabled)
    {
        this.button.changeDisabledState(disabled);
    }
}