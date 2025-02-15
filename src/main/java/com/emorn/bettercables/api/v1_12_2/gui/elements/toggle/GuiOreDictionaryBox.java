package com.emorn.bettercables.api.v1_12_2.gui.elements.toggle;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiOreDictionaryBox extends GuiToggle
{
    public GuiOreDictionaryBox(
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
            new ToggleImagePosition(x, y, 197, 18, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18, 18, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18 + 18, 18, 18, 18),
            disabled
        );
    }
}
