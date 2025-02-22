package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PossibleSlotCalculator
{
    private final Map<ConnectorSettings, SlotCache> slotsPerExtractSetting = new HashMap<>();

    public List<ExtractSlot> getPossibleSlots(
        ConnectorSettings exportSettings
    )
    {
        this.slotsPerExtractSetting.computeIfAbsent(exportSettings, k -> new SlotCache());

        return this.slotsPerExtractSetting.get(exportSettings).find(SlotCache.DEFAULT);
    }

    public void addInsert(
        ConnectorSettings settings,
        List<ConnectorSettings> extractConnectorSettings
    )
    {
        this.recalculateInsertsFromPossibleSlots(
            settings,
            extractConnectorSettings
        );
    }

    private void recalculateInsertsFromPossibleSlots(
        ConnectorSettings insertSettings,
        List<ConnectorSettings> extractConnectorSettings
    )
    {
        for (ConnectorSettings extractSettings : extractConnectorSettings) {
            this.slotsPerExtractSetting.putIfAbsent(extractSettings, new SlotCache());

            int insertSlotCount = insertSettings.inventorySlotCount();
            int extractSlotCount = extractSettings.inventorySlotCount();

            List<InsertSlot> insertSlots = new ArrayList<>();
            List<Integer> extractSlotIndices = new ArrayList<>();

            for (int extractIndex = 0; extractIndex < extractSlotCount; extractIndex++) {
                extractSlotIndices.add(extractIndex);
            }
            for (int insertIndex = 0; insertIndex < insertSlotCount; insertIndex++) {
                insertSlots.add(new InsertSlot(insertIndex));
            }

            this.slotsPerExtractSetting.get(extractSettings).addInsert(
                SlotCache.DEFAULT,
                insertSettings,
                insertSlots,
                extractSlotIndices
            );
        };
    }

    public void addExtract(
        ConnectorSettings settings,
        List<ConnectorSettings> insertConnectorSettings
    )
    {
        this.recalculateExtractsFromPossibleSlots(
            settings,
            insertConnectorSettings
        );
    }

    private void recalculateExtractsFromPossibleSlots(
        ConnectorSettings extractSettings,
        List<ConnectorSettings> insertConnectorSettings
    )
    {
        this.slotsPerExtractSetting.putIfAbsent(extractSettings, new SlotCache());

        int extractSlotCount = extractSettings.inventorySlotCount();

        SlotCache extractSlotCache = this.slotsPerExtractSetting.get(extractSettings);

        for (ConnectorSettings insertSettings : insertConnectorSettings) {
            int insertSlotCount = insertSettings.inventorySlotCount();

            List<InsertSlot> insertSlots = new ArrayList<>();
            List<Integer> extractSlotIndices = new ArrayList<>();

            for (int extractIndex = 0; extractIndex < extractSlotCount; extractIndex++) {
                extractSlotIndices.add(extractIndex);
            }

            for (int insertIndex = 0; insertIndex < insertSlotCount; insertIndex++) {
                insertSlots.add(new InsertSlot(insertIndex));
            }

            extractSlotCache.addInsert(
                SlotCache.DEFAULT,
                insertSettings,
                insertSlots,
                extractSlotIndices
            );
        }
    }

    public void reCalculateAllPossibleSlots(
        List<ConnectorSettings> insertConnectorSettings,
        List<ConnectorSettings> extractConnectorSettings
    )
    {
        this.slotsPerExtractSetting.clear();

        for (ConnectorSettings extractSettings : extractConnectorSettings) {
            this.recalculateExtractsFromPossibleSlots(
                extractSettings,
                insertConnectorSettings
            );
        }
    }

    public void removeInsert(
        ConnectorSettings insertSettings
    )
    {
        for (SlotCache slotCache : this.slotsPerExtractSetting.values()) {
            slotCache.removeInsert(SlotCache.DEFAULT, insertSettings);
        }
    }

    public void removeExtract(
        ConnectorSettings extractSettings
    )
    {
        this.slotsPerExtractSetting.remove(extractSettings);
    }

    public void updateSlotCount(
        ConnectorSettings connector,
        List<ConnectorSettings> insertConnectorSettings,
        List<ConnectorSettings> extractConnectorSettings
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
