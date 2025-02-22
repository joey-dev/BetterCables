package com.emorn.bettercables.core.blocks.connector.network;

public class InsertSlot
{
    private int insertIndex;
    private int enabledAt;

    public InsertSlot(int insertIndex)
    {
        this.insertIndex = insertIndex;
    }

    public int insertIndex()
    {
        return insertIndex;
    }
}
