package com.emorn.bettercables.api.v1_12_2.blocks.connector;

import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.blocks.connector.IDataList;
import com.emorn.bettercables.core.common.Logger;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DataList implements IDataList
{
    private final NBTTagList tag;

    public DataList(NBTTagList tag)
    {
        this.tag = tag;
    }

    public NBTTagList tag()
    {
        return this.tag;
    }

    @Override
    public int size()
    {
        return this.tag.tagCount();
    }

    @Override
    @Nullable
    public IData findByIndex(int index)
    {
        return this.tag.getCompoundTagAt(index).hasKey("id")
            ? new Data(this.tag.getCompoundTagAt(index))
            : null;
    }

    @Override
    public void add(IData iData)
    {
        if (!(iData instanceof Data)) {
            Logger.error("DataList.add: data is not instance of Data");
            return;
        }
        Data data = (Data) iData;

        this.tag.appendTag(data.tag());
    }

    @Override
    public IData newData()
    {
        return new Data(new NBTTagCompound());
    }
}
