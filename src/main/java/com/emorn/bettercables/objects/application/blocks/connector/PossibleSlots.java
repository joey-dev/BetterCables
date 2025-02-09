package com.emorn.bettercables.objects.application.blocks.connector;

import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PossibleSlots
{
    private PossibleSlots()
    {
    }

    // 0 = insertIndex, 1 = extractIndex
    public static List<List<Integer>> calculate(
        ConnectorSettings extractSettings,
        ConnectorSettings insertSettings
    )
    {
        int insertSlotCount = insertSettings.inventorySlotCount();
        int extractSlotCount = extractSettings.inventorySlotCount();

        List<List<Integer>> indexes = new ArrayList<>();
        for (int insertIndex = 0; insertIndex < insertSlotCount; insertIndex++) {
            for (int extractIndex = 0; extractIndex < extractSlotCount; extractIndex++) {
                List<Integer> combination = new ArrayList<>();
                combination.add(insertIndex);
                combination.add(extractIndex);

                indexes.add(combination);
            }
        }

        return indexes;
    }
}
