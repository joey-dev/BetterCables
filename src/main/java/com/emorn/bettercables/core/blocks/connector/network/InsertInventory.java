package com.emorn.bettercables.core.blocks.connector.network;

import java.util.ArrayList;
import java.util.List;

public class InsertInventory
{
    private final List<InsertSlot> insertSlots = new ArrayList<>();

    public InsertInventory(List<InsertSlot> insertSlots)
    {
        this.insertSlots.addAll(insertSlots);
    }

    public List<InsertSlot> insertSlots()
    {
        return insertSlots;
    }
}
