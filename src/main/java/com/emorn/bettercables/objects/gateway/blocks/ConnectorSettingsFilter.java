package com.emorn.bettercables.objects.gateway.blocks;

import com.emorn.bettercables.common.gui.ComparisonOperator;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

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
    private ItemStack itemStack = ItemStack.EMPTY;

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

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("isOverwriteEnabled", isOverwriteEnabled);
        nbt.setInteger("minSlotRange", minSlotRange);
        nbt.setInteger("maxSlotRange", maxSlotRange);
        nbt.setBoolean("isOreDictEnabled", isOreDictEnabled);
        nbt.setBoolean("isNbtDataEnabled", isNbtDataEnabled);
        nbt.setBoolean("isBlackListEnabled", isBlackListEnabled);
        nbt.setInteger("itemCount", itemCount);
        nbt.setString("durabilityType", durabilityType.toString());
        nbt.setInteger("durabilityPercentage", durabilityPercentage);
        nbt.setTag("itemStack", itemStack.serializeNBT());

        return nbt;
    }

    public void deserializeNBT(NBTTagCompound filterNbt)
    {
        if (filterNbt.hasKey("isOverwriteEnabled", Constants.NBT.TAG_BYTE)) {
            isOverwriteEnabled = filterNbt.getBoolean("isOverwriteEnabled");
        }
        if (filterNbt.hasKey("minSlotRange", Constants.NBT.TAG_INT)) {
            minSlotRange = filterNbt.getInteger("minSlotRange");
        }
        if (filterNbt.hasKey("maxSlotRange", Constants.NBT.TAG_INT)) {
            maxSlotRange  = filterNbt.getInteger("maxSlotRange");
        }
        if (filterNbt.hasKey("isOreDictEnabled", Constants.NBT.TAG_BYTE)) {
            isOreDictEnabled = filterNbt.getBoolean("isOreDictEnabled");
        }
        if (filterNbt.hasKey("isNbtDataEnabled", Constants.NBT.TAG_BYTE)) {
            isNbtDataEnabled = filterNbt.getBoolean("isNbtDataEnabled");
        }
        if (filterNbt.hasKey("isBlackListEnabled", Constants.NBT.TAG_BYTE)) {
            isBlackListEnabled = filterNbt.getBoolean("isBlackListEnabled");
        }
        if (filterNbt.hasKey("itemCount", Constants.NBT.TAG_INT)) {
            itemCount = filterNbt.getInteger("itemCount");
        }
        if (filterNbt.hasKey("durabilityType", Constants.NBT.TAG_STRING)) {
            durabilityType = ComparisonOperator.valueOf(filterNbt.getString("durabilityType"));
        }
        if (filterNbt.hasKey("durabilityPercentage", Constants.NBT.TAG_INT)) {
            durabilityPercentage = filterNbt.getInteger("durabilityPercentage");
        }

        if (filterNbt.hasKey("itemStack", Constants.NBT.TAG_COMPOUND)) {
            this.itemStack = new ItemStack(filterNbt.getCompoundTag("itemStack"));
        } else {
            this.itemStack = ItemStack.EMPTY;
        }
    }

    public void changeItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    public ItemStack itemStack()
    {
        return itemStack;
    }
}