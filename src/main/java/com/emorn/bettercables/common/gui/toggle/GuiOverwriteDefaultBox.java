package com.emorn.bettercables.common.gui.toggle;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiOverwriteDefaultBox extends GuiCheckbox
{
    public GuiOverwriteDefaultBox(
        int buttonId,
        int x,
        int y,
        boolean isChecked
    )
    {
        super(
            buttonId,
            x,
            y,
            "Overwrite",
            isChecked,
            false
        );
    }
}
