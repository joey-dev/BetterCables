package com.emorn.bettercables.core.blocks.connector.network;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InsertSlot that = (InsertSlot) obj;
        return insertIndex == that.insertIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(insertIndex);
    }
}
