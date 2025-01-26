package com.emorn.bettercables.common.gui.toggle;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiNbtDataBox extends GuiToggle
{
    public GuiNbtDataBox(
        int buttonId,
        int x,
        int y,
        boolean isChecked,
        boolean disabled
    )
    {
        super(
            buttonId,
            x,
            y,
            "",
            isChecked,
            new ToggleImagePosition(x, y, 197, 36, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18, 36, 18, 18),
        new ToggleImagePosition(x, y, 197 + 18 + 18, 36, 18, 18),
        disabled
        );
    }
}
