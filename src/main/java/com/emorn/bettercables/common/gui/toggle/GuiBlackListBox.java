package com.emorn.bettercables.common.gui.toggle;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiBlackListBox extends GuiToggle
{
    public GuiBlackListBox(
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
            new ToggleImagePosition(x, y, 197, 0, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18, 0, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18 + 18, 0, 18, 18),
            disabled
        );
    }
}
