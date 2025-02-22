package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;

import java.util.*;

public class SlotCache
{
    public static final String DEFAULT = "default";
    private final Map<String, List<ExtractSlot>> slotsPerItemName = new HashMap<>();

    public List<ExtractSlot> find(
        String itemName
    )
    {
        if (!slotsPerItemName.containsKey(itemName))
        {
            return new ArrayList<>();
        }

        return slotsPerItemName.get(itemName);
    }

    public void addInsert(
        String itemName,
        ConnectorSettings insertSettings,
        List<InsertSlot> insertSlots,
        List<Integer> extractSlotIndices
    )
    {
        this.slotsPerItemName.putIfAbsent(itemName, new ArrayList<>());
        List<ExtractSlot> extractSlots = this.slotsPerItemName.get(itemName);

        Map<Integer, ExtractSlot> extractSlotMap = new HashMap<>();
        for (ExtractSlot extractSlot : extractSlots)
        {
            extractSlotMap.put(extractSlot.extractIndex(), extractSlot);
        }

        for (Integer extractSlotIndex : extractSlotIndices)
        {
            if (!extractSlotMap.containsKey(extractSlotIndex))
            {
                ExtractSlot extractSlot = new ExtractSlot(extractSlotIndex);
                extractSlotMap.put(extractSlotIndex, extractSlot);
                extractSlots.add(extractSlot);
            }

            extractSlotMap.get(extractSlotIndex).addInsert(insertSettings, insertSlots);
        }
    }

    public void removeInsert(
        String itemName,
        ConnectorSettings insertSettings
    )
    {
        List<ExtractSlot> extractSlots = this.slotsPerItemName.get(itemName);
        for (ExtractSlot extractSlot : extractSlots)
        {
            extractSlot.removeInsert(insertSettings);
        }
    }
}
