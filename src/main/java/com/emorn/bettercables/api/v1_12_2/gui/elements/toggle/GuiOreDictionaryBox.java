package com.emorn.bettercables.api.v1_12_2.gui.elements.toggle;

import com.emorn.bettercables.api.v1_12_2.gui.elements.GuiTooltipData;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiOreDictionaryBox extends GuiToggle
{
    public GuiOreDictionaryBox(
        int buttonId,
        int x,
        int y,
        boolean isChecked,
        boolean disabled,
        Consumer<GuiTooltipData> callback
    )
    {
        super(
            buttonId,
            x,
            y,
            "",
            new String[]{
                "Enables or disables ore dictionary matching for this filter.",
                "When enabled, items matching the same ore dictionary entry will be treated as equivalent.",
            },
            isChecked,
            new ToggleImagePosition(x, y, 197, 18, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18, 18, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18 + 18, 18, 18, 18),
            disabled,
            callback
        );
    }
}
