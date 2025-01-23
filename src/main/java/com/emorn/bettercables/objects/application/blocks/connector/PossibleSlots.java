package com.emorn.bettercables.objects.application.blocks.connector;

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
    public static List<List<Integer>> calculate() // todo actually calculate
    {
        int slotCount = 27;

        List<List<Integer>> indexes = new ArrayList<>();
        for (int insertIndex = 0; insertIndex < slotCount; insertIndex++) {
            for (int extractIndex = 0; extractIndex < slotCount; extractIndex++) {
                List<Integer> combination = new ArrayList<>();
                combination.add(insertIndex);
                combination.add(extractIndex);

                indexes.add(combination);
            }
        }

        return indexes;
    }
}
