package com.emorn.bettercables.objects.gateway.blocks;

import com.emorn.bettercables.common.gui.ComparisonOperator;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSettingsFilter
{
    private boolean isOverwriteEnabled = false;
    private int slotMinimumRange = -1;
    private int slotMaximumRange = -1;
    private boolean isOreDictEnabled = false;
    private boolean isNbtDataEnabled = false;
    private boolean isBlackListEnabled = false;
    private int itemCount = -1;
    private ComparisonOperator durabilityType = ComparisonOperator.EQUALS;
    private int durabilityPercentage = -1;

    public boolean isOverwriteEnabled()
    {
        return isOverwriteEnabled;
    }

    public void changeOverwriteEnabled(boolean overwriteEnabled)
    {
        isOverwriteEnabled = overwriteEnabled;
    }

    public int slotMinimumRange()
    {
        return slotMinimumRange;
    }

    public void changeSlotMinimumRange(int slotMinimumRange)
    {
        this.slotMinimumRange = slotMinimumRange;
    }

    public int slotMaximumRange()
    {
        return slotMaximumRange;
    }

    public void changeSlotMaximumRange(int slotMaximumRange)
    {
        this.slotMaximumRange = slotMaximumRange;
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


    public NBTTagCompound serializeNBT(String key)
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean(key + "-" + "isOverwriteEnabled", isOverwriteEnabled);
        nbt.setInteger(key + "-" + "slotMinimumRange", slotMinimumRange);
        nbt.setInteger(key + "-" + "slotMaximumRange", slotMaximumRange);
        nbt.setBoolean(key + "-" + "isOreDictEnabled", isOreDictEnabled);
        nbt.setBoolean(key + "-" + "isNbtDataEnabled", isNbtDataEnabled);
        nbt.setBoolean(key + "-" + "isBlackListEnabled", isBlackListEnabled);
        nbt.setInteger(key + "-" + "itemCount", itemCount);
        nbt.setString(key + "-" + "durabilityType", durabilityType.toString());
        nbt.setInteger(key + "-" + "durabilityPercentage", durabilityPercentage);

        return nbt;
    }

    public void deserializeNBT(NBTTagCompound defaultInsertFilter)
    {
        if (defaultInsertFilter.hasKey("isOverwriteEnabled", Constants.NBT.TAG_BYTE)) {
            isOverwriteEnabled = defaultInsertFilter.getBoolean("isOverwriteEnabled");
        }
        if (defaultInsertFilter.hasKey("slotMinimumRange", Constants.NBT.TAG_INT)) {
            slotMinimumRange = defaultInsertFilter.getInteger("slotMinimumRange");
        }
        if (defaultInsertFilter.hasKey("slotMaximumRange", Constants.NBT.TAG_INT)) {
            slotMaximumRange = defaultInsertFilter.getInteger("slotMaximumRange");
        }
        if (defaultInsertFilter.hasKey("isOreDictEnabled", Constants.NBT.TAG_BYTE)) {
            isOreDictEnabled = defaultInsertFilter.getBoolean("isOreDictEnabled");
        }
        if (defaultInsertFilter.hasKey("isNbtDataEnabled", Constants.NBT.TAG_BYTE)) {
            isNbtDataEnabled = defaultInsertFilter.getBoolean("isNbtDataEnabled");
        }
        if (defaultInsertFilter.hasKey("isBlackListEnabled", Constants.NBT.TAG_BYTE)) {
            isBlackListEnabled = defaultInsertFilter.getBoolean("isBlackListEnabled");
        }
        if (defaultInsertFilter.hasKey("itemCount", Constants.NBT.TAG_INT)) {
            itemCount = defaultInsertFilter.getInteger("itemCount");
        }
        if (defaultInsertFilter.hasKey("durabilityType", Constants.NBT.TAG_STRING)) {
            durabilityType = ComparisonOperator.valueOf(defaultInsertFilter.getString("durabilityType"));
        }
        if (defaultInsertFilter.hasKey("durabilityPercentage", Constants.NBT.TAG_INT)) {
            durabilityPercentage = defaultInsertFilter.getInteger("durabilityPercentage");
        }
    }
}