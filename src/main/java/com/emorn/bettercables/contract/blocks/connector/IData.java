package com.emorn.bettercables.contract.blocks.connector;

import com.emorn.bettercables.contract.common.IItemStack;

import javax.annotation.Nullable;

public interface IData
{
    public void save(String key, String value);
    public void save(String key, boolean value);
    public void save(String key, int value);
    public void save(String key, IData value);
    public void save(String key, IDataList value);

    public IDataList newList();
    public IData newData();

    public String loadString(String key);
    public boolean loadBoolean(String key);
    public int loadInteger(String key);
    @Nullable
    public IData loadCompound(String key);
    public IDataList loadList(String key, DataType type);
    public IItemStack loadItemStack(String key);

    public boolean hasKey(String key, DataType type);
}
