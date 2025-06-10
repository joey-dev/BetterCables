package com.emorn.bettercables.api.v1_12_2.gui.elements.toggle;

import com.emorn.bettercables.api.v1_12_2.gui.elements.GuiTooltipData;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiBlackListBox extends GuiToggle
{
    public GuiBlackListBox(
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
                "BlackListBox",
                "By default the item filters are in whitelist mode,",
                "by enabling this option, the item filters will be in blacklist mode.",
            },
            isChecked,
            new ToggleImagePosition(x, y, 197, 0, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18, 0, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18 + 18, 0, 18, 18),
            disabled,
            callback
        );
    }
}
