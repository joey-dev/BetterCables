package com.emorn.bettercables.contract.blocks.connector;

import javax.annotation.Nullable;

public interface IDataList
{
    public int size();
    @Nullable
    public IData findByIndex(int index);
    public void add(IData data);
    public IData newData();
}
