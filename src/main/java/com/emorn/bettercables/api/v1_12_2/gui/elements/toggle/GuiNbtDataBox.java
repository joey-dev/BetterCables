package com.emorn.bettercables.api.v1_12_2.gui.elements.toggle;

import com.emorn.bettercables.api.v1_12_2.gui.elements.GuiTooltipData;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiNbtDataBox extends GuiToggle
{
    public GuiNbtDataBox(
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
                "Nbt Data Box",
                "When enabled, the filters will not only check the name of the item",
                "but also the NBT data of the item.",
            },
            isChecked,
            new ToggleImagePosition(x, y, 197, 36, 18, 18),
            new ToggleImagePosition(x, y, 197 + 18, 36, 18, 18),
        new ToggleImagePosition(x, y, 197 + 18 + 18, 36, 18, 18),
        disabled,
            callback
        );
    }
}
