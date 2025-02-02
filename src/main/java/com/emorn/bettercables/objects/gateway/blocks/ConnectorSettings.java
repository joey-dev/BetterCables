package com.emorn.bettercables.objects.gateway.blocks;

import com.emorn.bettercables.common.gui.ExtractType;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSettings
{
    private boolean isExtractEnabled = false;
    private boolean isInsertEnabled = false;
    private int inventorySlotCount = 27;

    // extract
    private int extractChannelId = 0;
    private int tickRate = 20;
    private boolean isDynamicTickRateEnabled = false;
    private int dynamicTickRateMinimum = 1;
    private int dynamicTickRateMaximum = 999;
    private ExtractType extractType = ExtractType.ROUND_ROBIN;
    private int itemsPerExtract = 1;

    // filters
    private final ConnectorSettingsDefaultFilter defaultInsertFilter = new ConnectorSettingsDefaultFilter();
    private final ConnectorSettingsDefaultFilter defaultExtractFilter = new ConnectorSettingsDefaultFilter();

    private final Map<Integer, ConnectorSettingsFilter> insertFilters = new HashMap<>();
    private final Map<Integer, ConnectorSettingsFilter> extractFilters = new HashMap<>();

    // insert
    private int insertChannelId = 0;
    private int priority = 1;

    public boolean isExtractEnabled()
    {
        return isExtractEnabled;
    }

    public void changeExtractEnabled(boolean extractEnabled)
    {
        isExtractEnabled = extractEnabled;
    }

    public boolean isInsertEnabled()
    {
        return isInsertEnabled;
    }

    public void changeInsertEnabled(boolean insertEnabled)
    {
        isInsertEnabled = insertEnabled;
    }

    public int inventorySlotCount()
    {
        return inventorySlotCount;
    }

    public void changeInventorySlotCount(int inventorySlotCount)
    {
        this.inventorySlotCount = inventorySlotCount;
    }

    public int insertChannelId()
    {
        return insertChannelId;
    }

    public void changeInsertChannelId(int channelId)
    {
        this.insertChannelId = channelId;
    }

    public int extractChannelId()
    {
        return extractChannelId;
    }

    public void changeExtractChannelId(int channelId)
    {
        this.extractChannelId = channelId;
    }

    public int tickRate()
    {
        return tickRate;
    }

    public void changeTickRate(int tickRate)
    {
        this.tickRate = tickRate;
    }

    public boolean isDynamicTickRateEnabled()
    {
        return isDynamicTickRateEnabled;
    }

    public void changeDynamicTickRateEnabled(boolean dynamicTickRateEnabled)
    {
        isDynamicTickRateEnabled = dynamicTickRateEnabled;
    }

    public int dynamicTickRateMinimum()
    {
        return dynamicTickRateMinimum;
    }

    public void changeDynamicTickRateMinimum(int dynamicTickRateMinimum)
    {
        this.dynamicTickRateMinimum = dynamicTickRateMinimum;
    }

    public int dynamicTickRateMaximum()
    {
        return dynamicTickRateMaximum;
    }

    public void changeDynamicTickRateMaximum(int dynamicTickRateMaximum)
    {
        this.dynamicTickRateMaximum = dynamicTickRateMaximum;
    }

    public ExtractType extractType()
    {
        return extractType;
    }

    public void changeExtractType(ExtractType extractType)
    {
        this.extractType = extractType;
    }

    public int itemsPerExtract()
    {
        return itemsPerExtract;
    }

    public void changeItemsPerExtract(int itemsPerExtract)
    {
        this.itemsPerExtract = itemsPerExtract;
    }

    public ConnectorSettingsDefaultFilter defaultInsertFilter()
    {
        return defaultInsertFilter;
    }

    public ConnectorSettingsDefaultFilter defaultExtractFilter()
    {
        return defaultExtractFilter;
    }

    public ConnectorSettingsFilter insertFilter(Integer id)
    {
        insertFilters.putIfAbsent(id, new ConnectorSettingsFilter());
        return insertFilters.get(id);
    }

    public ConnectorSettingsFilter extractFilter(Integer id)
    {
        extractFilters.putIfAbsent(id, new ConnectorSettingsFilter());
        return extractFilters.get(id);
    }

    public int priority()
    {
        return priority;
    }

    public void changePriority(int priority)
    {
        this.priority = priority;
    }

    public NBTTagCompound serializeNBT()
    {
        return this.serializeNBT("");
    }

    public NBTTagCompound serializeNBT(String key)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean(key + "-" + "isInsertEnabled", isInsertEnabled);
        nbt.setBoolean(key + "-" + "isExtractEnabled", isExtractEnabled);
        nbt.setInteger(key + "-" + "inventorySlotCount", inventorySlotCount);

        // extract
        nbt.setInteger(key + "-" + "extractChannelId", extractChannelId);
        nbt.setInteger(key + "-" + "tickRate", tickRate);
        nbt.setBoolean(key + "-" + "isDynamicTickRateEnabled", isDynamicTickRateEnabled);
        nbt.setInteger(key + "-" + "dynamicTickRateMinimum", dynamicTickRateMinimum);
        nbt.setInteger(key + "-" + "dynamicTickRateMaximum", dynamicTickRateMaximum);
        nbt.setString(key + "-" + "extractType", extractType.name());
        nbt.setInteger(key + "-" + "itemsPerExtract", itemsPerExtract);

        // filters
        nbt.setTag(key + "-" + "defaultInsertFilter", defaultInsertFilter.serializeNBT());
        nbt.setTag(key + "-" + "defaultExtractFilter", defaultExtractFilter.serializeNBT());
        nbt.setTag(key + "-" + "insertFilters", serializeFilters(insertFilters));
        nbt.setTag(key + "-" + "extractFilters", serializeFilters(extractFilters));

        // insert
        nbt.setInteger(key + "-" + "insertChannelId", insertChannelId);
        nbt.setInteger(key + "-" + "priority", priority);

        return nbt;
    }

    private NBTTagList serializeFilters(
        Map<Integer, ConnectorSettingsFilter> filters
    )
    {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<Integer, ConnectorSettingsFilter> entry : filters.entrySet()) {
            NBTTagCompound filterTag = new NBTTagCompound();
            filterTag.setInteger("id", entry.getKey()); // Store filter ID
            filterTag.setTag("data", entry.getValue().serializeNBT()); // Store filter data
            list.appendTag(filterTag);
        }
        return list;
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        deserializeNBT(nbt, "");
    }

    public void deserializeNBT(
        NBTTagCompound nbt,
        String key
    )
    {
        if (nbt.hasKey(key + "-" + "isInsertEnabled", Constants.NBT.TAG_BYTE)) {
            isInsertEnabled = nbt.getBoolean(key + "-" + "isInsertEnabled");
        }
        if (nbt.hasKey(key + "-" + "isExtractEnabled", Constants.NBT.TAG_BYTE)) {
            isExtractEnabled = nbt.getBoolean(key + "-" + "isExtractEnabled");
        }
        if (nbt.hasKey(key + "-" + "inventorySlotCount", Constants.NBT.TAG_INT)) {
            inventorySlotCount = nbt.getInteger(key + "-" + "inventorySlotCount");
        }

        // extract
        if (nbt.hasKey(key + "-" + "extractChannelId", Constants.NBT.TAG_INT)) {
            extractChannelId = nbt.getInteger(key + "-" + "extractChannelId");
        }
        if (nbt.hasKey(key + "-" + "tickRate", Constants.NBT.TAG_INT)) {
            tickRate = nbt.getInteger(key + "-" + "tickRate");
        }
        if (nbt.hasKey(key + "-" + "isDynamicTickRateEnabled", Constants.NBT.TAG_BYTE)) {
            isDynamicTickRateEnabled = nbt.getBoolean(key + "-" + "isDynamicTickRateEnabled");
        }
        if (nbt.hasKey(key + "-" + "dynamicTickRateMinimum", Constants.NBT.TAG_INT)) {
            dynamicTickRateMinimum = nbt.getInteger(key + "-" + "dynamicTickRateMinimum");
        }
        if (nbt.hasKey(key + "-" + "dynamicTickRateMaximum", Constants.NBT.TAG_INT)) {
            dynamicTickRateMaximum = nbt.getInteger(key + "-" + "dynamicTickRateMaximum");
        }
        if (nbt.hasKey(key + "-" + "extractType", Constants.NBT.TAG_STRING)) {
            extractType = ExtractType.valueOf(nbt.getString(key + "-" + "extractType"));
        }
        if (nbt.hasKey(key + "-" + "itemsPerExtract", Constants.NBT.TAG_INT)) {
            itemsPerExtract = nbt.getInteger(key + "-" + "itemsPerExtract");
        }

        // filters
        if (nbt.hasKey(key + "-" + "defaultInsertFilter", Constants.NBT.TAG_COMPOUND)) {
            defaultInsertFilter.deserializeNBT(nbt.getCompoundTag(key + "-" + "defaultInsertFilter"));
        }
        if (nbt.hasKey(key + "-" + "defaultExtractFilter", Constants.NBT.TAG_COMPOUND)) {
            defaultExtractFilter.deserializeNBT(nbt.getCompoundTag(key + "-" + "defaultExtractFilter"));
        }

        if (nbt.hasKey(key + "-" + "insertFilters", Constants.NBT.TAG_LIST)) {
            deserializeFilters(nbt.getTagList(key + "-" + "insertFilters", Constants.NBT.TAG_COMPOUND), insertFilters);
        }
        if (nbt.hasKey(key + "-" + "extractFilters", Constants.NBT.TAG_LIST)) {
            deserializeFilters(nbt.getTagList(key + "-" + "extractFilters", Constants.NBT.TAG_COMPOUND), extractFilters);
        }

        // insert
        if (nbt.hasKey(key + "-" + "insertChannelId", Constants.NBT.TAG_INT)) {
            insertChannelId = nbt.getInteger(key + "-" + "insertChannelId");
        }
        if (nbt.hasKey(key + "-" + "priority", Constants.NBT.TAG_INT)) {
            priority = nbt.getInteger(key + "-" + "priority");
        }
    }

    private void deserializeFilters(
        NBTTagList nbtFilters,
        Map<Integer, ConnectorSettingsFilter> filters
    )
    {
        filters.clear();
        for (int i = 0; i < nbtFilters.tagCount(); i++) {
            NBTTagCompound filterTag = nbtFilters.getCompoundTagAt(i);
            int id = filterTag.getInteger("id");
            ConnectorSettingsFilter filter = new ConnectorSettingsFilter();
            filter.deserializeNBT(filterTag.getCompoundTag("data"));
            filters.put(id, filter);
        }
    }

    public void changeFilterItem(
        int index,
        boolean isInsert,
        ItemStack itemStack
    )
    {
        if (isInsert) {
            this.insertFilters.putIfAbsent(index, new ConnectorSettingsFilter());
            this.insertFilters.get(index).changeItemStack(itemStack);
            return;
        }

        this.extractFilters.putIfAbsent(index, new ConnectorSettingsFilter());
        this.extractFilters.get(index).changeItemStack(itemStack);
    }
}
