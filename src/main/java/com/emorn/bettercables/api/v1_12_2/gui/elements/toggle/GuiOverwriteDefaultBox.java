package com.emorn.bettercables.api.v1_12_2.gui.elements.toggle;

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
