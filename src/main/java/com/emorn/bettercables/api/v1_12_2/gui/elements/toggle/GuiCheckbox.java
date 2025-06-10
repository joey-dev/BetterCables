package com.emorn.bettercables.api.v1_12_2.gui.elements.toggle;

import com.emorn.bettercables.api.v1_12_2.gui.elements.GuiTooltipData;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiCheckbox extends GuiToggle
{
    public GuiCheckbox(
        int buttonId,
        int x,
        int y,
        String buttonText,
        String[] description,
        boolean isChecked,
        boolean disabled,
        Consumer<GuiTooltipData> callbackTooltip
    )
    {
        super(
            buttonId,
            x,
            y,
            buttonText,
            description,
            isChecked,
            new ToggleImagePosition(x, y, 0, 0, 18, 18),
            new ToggleImagePosition(x, y, 18, 0, 18, 18),
        new ToggleImagePosition(x, y, 18, 0, 18, 18), // no disabled state
            disabled,
            callbackTooltip
        );
    }
}
