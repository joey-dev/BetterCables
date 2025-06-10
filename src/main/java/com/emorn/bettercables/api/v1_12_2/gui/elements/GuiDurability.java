package com.emorn.bettercables.api.v1_12_2.gui.elements;

import com.emorn.bettercables.contract.gui.ComparisonOperator;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
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
        boolean disable,
        Consumer<GuiTooltipData> callback
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, "Durability");

        this.button = new GuiComparisonOperatorButton(
            inputId,
            x,
            y,
            "Durability%",
            new String[]{
                "Durability percentage of the item",
            },
            comparisonOperator,
            initialValue,
            100,
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
        // not really a button, so it shouldn't draw one
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