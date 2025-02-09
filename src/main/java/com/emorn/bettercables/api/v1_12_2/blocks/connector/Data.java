package com.emorn.bettercables.api.v1_12_2.blocks.connector;

import com.emorn.bettercables.api.v1_12_2.common.ItemStack;
import com.emorn.bettercables.contract.blocks.connector.DataType;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.blocks.connector.IDataList;
import com.emorn.bettercables.contract.common.IItemStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Data implements IData
{
    private final NBTTagCompound tag;

    public Data(NBTTagCompound tag)
    {
        this.tag = tag;
    }

    public NBTTagCompound tag()
    {
        return this.tag;
    }


    @Override
    public void save(
        String key,
        String value
    )
    {
        this.tag.setString(key, value);
    }

    @Override
    public void save(
        String key,
        boolean value
    )
    {
        this.tag.setBoolean(key, value);
    }

    @Override
    public void save(
        String key,
        int value
    )
    {
        this.tag.setInteger(key, value);
    }

    @Override
    public void save(
        String key,
        IData value
    )
    {
        this.tag.setTag(key, ((Data) value).tag());
    }

    @Override
    public void save(
        String key,
        IDataList value
    )
    {
        this.tag.setTag(key, ((DataList) value).tag());
    }

    @Override
    public IDataList newList()
    {
        return new DataList(new NBTTagList());
    }

    @Override
    public IData newData()
    {
        return new Data(new NBTTagCompound());
    }

    @Override
    public String loadString(String key)
    {
        return this.tag.getString(key);
    }

    @Override
    public boolean loadBoolean(String key)
    {
        return this.tag.getBoolean(key);
    }

    @Override
    public int loadInteger(String key)
    {
        return this.tag.getInteger(key);
    }

    @Override
    @Nullable
    public IData loadCompound(String key)
    {
        return this.tag.hasKey(key) ? new Data(this.tag.getCompoundTag(key)) : null;
    }

    public IItemStack loadItemStack(String key)
    {
        return new ItemStack(
            new net.minecraft.item.ItemStack(
                this.tag.getCompoundTag(key)
            )
        );
    }

    @Override
    public IDataList loadList(
        String key,
        DataType type
    )
    {
        return this.tag.hasKey(key)
            ? new DataList(this.tag.getTagList(key, this.findNBTType(type)))
            : new DataList(new NBTTagList());
    }

    @Override
    public boolean hasKey(
        String key,
        DataType type
    )
    {
        return this.tag.hasKey(key, this.findNBTType(type));
    }

    private int findNBTType(DataType type)
    {
        switch (type)
        {
            case BOOLEAN:
                return Constants.NBT.TAG_BYTE;
            case INTEGER:
                return Constants.NBT.TAG_INT;
            case STRING:
                return Constants.NBT.TAG_STRING;
            case COMPOUND:
                return Constants.NBT.TAG_COMPOUND;
            case LIST:
                return Constants.NBT.TAG_LIST;
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }
}
