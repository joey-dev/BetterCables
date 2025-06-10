package com.emorn.bettercables.api.v1_12_2.gui.elements.toggle;

import com.emorn.bettercables.api.v1_12_2.gui.elements.GuiTooltipData;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiOverwriteDefaultBox extends GuiCheckbox
{
    public GuiOverwriteDefaultBox(
        int buttonId,
        int x,
        int y,
        boolean isChecked,
        Consumer<GuiTooltipData> callbackTooltip
    )
    {
        super(
            buttonId,
            x,
            y,
            "Overwrite",
            new String[]{
                "When disables, it will follow the settings of the connector itself",
                "When enabled, it will overwrite all settings with the values set here",
            },
            isChecked,
            false,
            callbackTooltip
        );
    }
}
