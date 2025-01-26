package com.emorn.bettercables.common.gui.toggle;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiCheckbox extends GuiToggle
{
    public GuiCheckbox(
        int buttonId,
        int x,
        int y,
        String buttonText,
        boolean isChecked,
        boolean disabled
    )
    {
        super(
            buttonId,
            x,
            y,
            buttonText,
            isChecked,
            new ToggleImagePosition(x, y, 0, 0, 18, 18),
            new ToggleImagePosition(x, y, 18, 0, 18, 18),
        new ToggleImagePosition(x, y, 18, 0, 18, 18), // no disabled state
            disabled
        );
    }
}
