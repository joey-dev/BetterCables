package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PossibleSlotCalculator
{
    // 1: extract settings, 2: insert settings, 3: insertIndex, 4: extractIndex
    private final Map<ConnectorSettings, Map<ConnectorSettings, List<List<Integer>>>> possibleSlotsToExtract = new HashMap<>();

    public List<List<Integer>> getPossibleSlots(
        ConnectorSettings exportSettings,
        ConnectorSettings importSettings
    )
    {
        this.possibleSlotsToExtract.computeIfAbsent(exportSettings, k -> new HashMap<>());
        this.possibleSlotsToExtract.get(exportSettings).computeIfAbsent(importSettings, k -> new ArrayList<>());

        return this.possibleSlotsToExtract.get(exportSettings).get(importSettings);
    }

    public void addInsert(
        ConnectorSettings settings,
        Map<ConnectorSettings, IPositionInWorld> extractConnectorSettings
    )
    {
        this.recalculateInsertsFromPossibleSlots(
            settings,
            extractConnectorSettings
        );
    }

    private void recalculateInsertsFromPossibleSlots(
        ConnectorSettings insertSettings,
        Map<ConnectorSettings, IPositionInWorld> extractConnectorSettings
    )
    {
        extractConnectorSettings.forEach((k, v) -> {
            this.possibleSlotsToExtract.putIfAbsent(k, new HashMap<>());

            List<List<Integer>> possibleSlots = this.calculate(k, insertSettings);
            this.possibleSlotsToExtract.get(k).put(insertSettings, possibleSlots);
        });
    }

    // 0 = insertIndex, 1 = extractIndex
    public List<List<Integer>> calculate(
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

    public void addExtract(
        ConnectorSettings settings,
        Map<ConnectorSettings, IPositionInWorld> insertConnectorSettings
    )
    {
        this.recalculateExtractsFromPossibleSlots(
            settings,
            insertConnectorSettings
        );
    }

    private void recalculateExtractsFromPossibleSlots(
        ConnectorSettings extractSettings,
        Map<ConnectorSettings, IPositionInWorld> insertConnectorSettings
    )
    {
        new HashMap<>(insertConnectorSettings).forEach((k, v) -> {
            this.possibleSlotsToExtract.putIfAbsent(extractSettings, new HashMap<>());

            List<List<Integer>> possibleSlots = this.calculate(extractSettings, k);
            this.possibleSlotsToExtract.get(extractSettings).put(k, possibleSlots);
        });
    }

    public void reCalculateAllPossibleSlots(
        Map<ConnectorSettings, IPositionInWorld> insertConnectorSettings,
        Map<ConnectorSettings, IPositionInWorld> extractConnectorSettings
    )
    {
        this.possibleSlotsToExtract.clear();

        for (Map.Entry<ConnectorSettings, IPositionInWorld> insertEntry : insertConnectorSettings.entrySet()) {
            for (Map.Entry<ConnectorSettings, IPositionInWorld> extractEntry : extractConnectorSettings.entrySet()) {
                // 1: extract, 2: insert, 3: insertIndex, 4: extractIndex
                this.possibleSlotsToExtract.putIfAbsent(extractEntry.getKey(), new HashMap<>());
                this.possibleSlotsToExtract.get(extractEntry.getKey())
                    .putIfAbsent(insertEntry.getKey(), new ArrayList<>());

                this.possibleSlotsToExtract.get(extractEntry.getKey())
                    .put(insertEntry.getKey(), this.calculate(extractEntry.getKey(), insertEntry.getKey()));
            }
        }
    }

    public void removeInsert(
        ConnectorSettings insertSettings
    )
    {
        this.possibleSlotsToExtract.forEach((k, v)
            -> v.remove(insertSettings));
    }

    public void removeExtract(
        ConnectorSettings extractSettings
    )
    {
        this.possibleSlotsToExtract.remove(extractSettings);
    }

    public void updateSlotCount(
        ConnectorSettings connector,
        Map<ConnectorSettings, IPositionInWorld> insertConnectorSettings,
        Map<ConnectorSettings, IPositionInWorld> extractConnectorSettings
    )
    {
        if (connector.isInsertEnabled()) {
            this.recalculateInsertsFromPossibleSlots(
                connector,
                extractConnectorSettings
            );
        }

        if (connector.isExtractEnabled()) {
            this.recalculateExtractsFromPossibleSlots(
                connector,
                insertConnectorSettings
            );
        }
    }
}
