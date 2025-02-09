package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.common.gui.ComparisonOperator;
import com.emorn.bettercables.contract.blocks.connector.DataType;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IItemStack;
import com.emorn.bettercables.core.common.EmptyItemStack;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSettingsFilter
{
    private boolean isOverwriteEnabled = false;
    private boolean isOreDictEnabled = false;
    private boolean isNbtDataEnabled = false;
    private boolean isBlackListEnabled = false;
    private int itemCount = -1;
    private ComparisonOperator durabilityType = ComparisonOperator.EQUALS;
    private int durabilityPercentage = -1;
    private int minSlotRange = -1;
    private int maxSlotRange = -1;
    private IItemStack itemStack = new EmptyItemStack();

    public boolean isOverwriteEnabled()
    {
        return isOverwriteEnabled;
    }

    public void changeOverwriteEnabled(boolean overwriteEnabled)
    {
        isOverwriteEnabled = overwriteEnabled;
    }

    public boolean isOreDictEnabled()
    {
        return isOreDictEnabled;
    }

    public void changeOreDictEnabled(boolean oreDictEnabled)
    {
        isOreDictEnabled = oreDictEnabled;
    }

    public boolean isNbtDataEnabled()
    {
        return isNbtDataEnabled;
    }

    public void changeNbtDataEnabled(boolean nbtDataEnabled)
    {
        isNbtDataEnabled = nbtDataEnabled;
    }

    public boolean isBlackListEnabled()
    {
        return isBlackListEnabled;
    }

    public void changeBlackListEnabled(boolean blackListEnabled)
    {
        isBlackListEnabled = blackListEnabled;
    }

    public int itemCount()
    {
        return itemCount;
    }

    public void changeItemCount(int itemCount)
    {
        this.itemCount = itemCount;
    }

    public ComparisonOperator durabilityType()
    {
        return durabilityType;
    }

    public void changeDurabilityType(ComparisonOperator durabilityType)
    {
        this.durabilityType = durabilityType;
    }

    public int durabilityPercentage()
    {
        return durabilityPercentage;
    }

    public void changeDurabilityPercentage(int durabilityPercentage)
    {
        this.durabilityPercentage = durabilityPercentage;
    }

    public int minSlotRange()
    {
        return this.minSlotRange;
    }

    public void changeMinSlotRange(int minSlotRange)
    {
        this.minSlotRange = minSlotRange;
    }

    public int maxSlotRange()
    {
        return this.maxSlotRange;
    }

    public void changeMaxSlotRange(int maxSlotRange)
    {
        this.maxSlotRange = maxSlotRange;
    }

    public IData serializeNBT(IData nbt)
    {
        nbt.save(ConnectorDataConstants.IS_OVERWRITE_ENABLED, isOverwriteEnabled);
        nbt.save(ConnectorDataConstants.MIN_SLOT_RANGE, minSlotRange);
        nbt.save(ConnectorDataConstants.MAX_SLOT_RANGE, maxSlotRange);
        nbt.save(ConnectorDataConstants.IS_ORE_DICT_ENABLED, isOreDictEnabled);
        nbt.save(ConnectorDataConstants.IS_NBT_DATA_ENABLED, isNbtDataEnabled);
        nbt.save(ConnectorDataConstants.IS_BLACK_LIST_ENABLED, isBlackListEnabled);
        nbt.save(ConnectorDataConstants.ITEM_COUNT, itemCount);
        nbt.save(ConnectorDataConstants.DURABILITY_TYPE, durabilityType.toString());
        nbt.save(ConnectorDataConstants.DURABILITY_PERCENTAGE, durabilityPercentage);
        IData itemStackData = itemStack.serialize();
        if (itemStackData != null) {
            nbt.save(ConnectorDataConstants.ITEM_STACK, itemStackData);
        }

        return nbt;
    }

    public void deserializeNBT(IData filterNbt)
    {
        if (filterNbt.hasKey(ConnectorDataConstants.IS_OVERWRITE_ENABLED, DataType.BOOLEAN)) {
            isOverwriteEnabled = filterNbt.loadBoolean(ConnectorDataConstants.IS_OVERWRITE_ENABLED);
        }
        if (filterNbt.hasKey(ConnectorDataConstants.MIN_SLOT_RANGE, DataType.INTEGER)) {
            minSlotRange = filterNbt.loadInteger(ConnectorDataConstants.MIN_SLOT_RANGE);
        }
        if (filterNbt.hasKey(ConnectorDataConstants.MAX_SLOT_RANGE, DataType.INTEGER)) {
            maxSlotRange  = filterNbt.loadInteger(ConnectorDataConstants.MAX_SLOT_RANGE);
        }
        if (filterNbt.hasKey(ConnectorDataConstants.IS_ORE_DICT_ENABLED, DataType.BOOLEAN)) {
            isOreDictEnabled = filterNbt.loadBoolean(ConnectorDataConstants.IS_ORE_DICT_ENABLED);
        }
        if (filterNbt.hasKey(ConnectorDataConstants.IS_NBT_DATA_ENABLED, DataType.BOOLEAN)) {
            isNbtDataEnabled = filterNbt.loadBoolean(ConnectorDataConstants.IS_NBT_DATA_ENABLED);
        }
        if (filterNbt.hasKey(ConnectorDataConstants.IS_BLACK_LIST_ENABLED, DataType.BOOLEAN)) {
            isBlackListEnabled = filterNbt.loadBoolean(ConnectorDataConstants.IS_BLACK_LIST_ENABLED);
        }
        if (filterNbt.hasKey(ConnectorDataConstants.ITEM_COUNT, DataType.INTEGER)) {
            itemCount = filterNbt.loadInteger(ConnectorDataConstants.ITEM_COUNT);
        }
        if (filterNbt.hasKey(ConnectorDataConstants.DURABILITY_TYPE, DataType.STRING)) {
            durabilityType = ComparisonOperator.valueOf(filterNbt.loadString(ConnectorDataConstants.DURABILITY_TYPE));
        }
        if (filterNbt.hasKey(ConnectorDataConstants.DURABILITY_PERCENTAGE, DataType.INTEGER)) {
            durabilityPercentage = filterNbt.loadInteger(ConnectorDataConstants.DURABILITY_PERCENTAGE);
        }

        this.itemStack = new EmptyItemStack();
        if (filterNbt.hasKey("itemStack", DataType.COMPOUND)) {
            this.itemStack = filterNbt.loadItemStack("itemStack");
        }
    }

    public void changeItemStack(IItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    public IItemStack itemStack()
    {
        return itemStack;
    }
}