package com.emorn.bettercables.objects.gateway.blocks;

import com.emorn.bettercables.common.gui.ExtractType;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTBase;
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
    private int insertSlotCount = 27;
    private int extractSlotCount = 27;

    // extract
    private int extractChannelId = 0;
    private int tickRate = 20;
    private boolean isDynamicTickRateEnabled = false;
    private int dynamicTickRateMinimum = 1;
    private int dynamicTickRateMaximum = 999;
    private ExtractType extractType = ExtractType.ROUND_ROBIN;
    private int itemsPerExtract = 1;
    private int itemCount = 1; // todo to filter
    private int minSlotRange = -1; // todo to filter
    private int maxSlotRange = -1; // todo to filter

    // filters
    private ConnectorSettingsDefaultFilter defaultInsertFilter = new ConnectorSettingsDefaultFilter();
    private ConnectorSettingsDefaultFilter defaultExtractFilter = new ConnectorSettingsDefaultFilter();

    private Map<Integer, ConnectorSettingsFilter> insertFilters = new HashMap<>();
    private Map<Integer, ConnectorSettingsFilter> extractFilters = new HashMap<>();

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

    public int insertSlotCount()
    {
        return insertSlotCount;
    }

    public void changeInsertSlotCount(int insertSlotCount)
    {
        this.insertSlotCount = insertSlotCount;
    }

    public int extractSlotCount()
    {
        return extractSlotCount;
    }

    public void changeExtractSlotCount(int extractSlotCount)
    {
        this.extractSlotCount = extractSlotCount;
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

    // todo not sure if needed
    public void changeDefaultInsertFilter(ConnectorSettingsDefaultFilter defaultInsertFilter)
    {
        this.defaultInsertFilter = defaultInsertFilter;
    }

    public ConnectorSettingsDefaultFilter defaultExtractFilter()
    {
        return defaultExtractFilter;
    }

    // todo not sure if needed
    public void changeDefaultExtractFilter(ConnectorSettingsDefaultFilter defaultExtractFilter)
    {
        this.defaultExtractFilter = defaultExtractFilter;
    }

    public ConnectorSettingsFilter insertFilter(Integer id)
    {
        insertFilters.putIfAbsent(id, new ConnectorSettingsFilter());
        return insertFilters.get(id);
    }

    public void changeInsertFilters(
        Integer id,
        ConnectorSettingsFilter insertFilter
    )
    {
        this.insertFilters.put(id, insertFilter);
    }

    public ConnectorSettingsFilter extractFilter(Integer id)
    {
        extractFilters.putIfAbsent(id, new ConnectorSettingsFilter());
        return extractFilters.get(id);
    }

    public void changeExtractFilter(
        Integer id,
        ConnectorSettingsFilter extractFilter
    )
    {
        this.extractFilters.put(id, extractFilter);
    }

    public int priority()
    {
        return priority;
    }

    public void changePriority(int priority)
    {
        this.priority = priority;
    }

    public int minSlotRange()
    {
        return minSlotRange;
    }

    public void changeMinSlotRange(int minSlotRangeInput)
    {
        this.minSlotRange = minSlotRangeInput;
    }

    public int maxSlotRange()
    {
        return maxSlotRange;
    }

    public void changeMaxSlotRange(int maxSlotRangeInput)
    {
        this.maxSlotRange = maxSlotRangeInput;
    }

    public int itemCount()
    {
        return itemCount;
    }

    public void changeItemCount(int itemCount)
    {
        this.itemCount = itemCount;
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
        nbt.setInteger(key + "-" + "insertSlotCount", insertSlotCount);
        nbt.setInteger(key + "-" + "extractSlotCount", extractSlotCount);

        // extract
        nbt.setInteger(key + "-" + "extractChannelId", extractChannelId);
        nbt.setInteger(key + "-" + "tickRate", tickRate);
        nbt.setBoolean(key + "-" + "isDynamicTickRateEnabled", isDynamicTickRateEnabled);
        nbt.setInteger(key + "-" + "dynamicTickRateMinimum", dynamicTickRateMinimum);
        nbt.setInteger(key + "-" + "dynamicTickRateMaximum", dynamicTickRateMaximum);
        nbt.setString(key + "-" + "extractType", extractType.name());
        nbt.setInteger(key + "-" + "itemsPerExtract", itemsPerExtract);
        nbt.setInteger(key + "-" + "itemCount", itemCount);
        nbt.setInteger(key + "-" + "minSlotRange", minSlotRange);
        nbt.setInteger(key + "-" + "maxSlotRange", maxSlotRange);

        // filters
        nbt.setTag(key + "-" + "defaultInsertFilter", defaultInsertFilter.serializeNBT(key));
        nbt.setTag(key + "-" + "defaultExtractFilter", defaultExtractFilter.serializeNBT(key));
        nbt.setTag(key + "-" + "insertFilters", serializeFilters(insertFilters, key));
        nbt.setTag(key + "-" + "extractFilters", serializeFilters(extractFilters, key));

        // insert
        nbt.setInteger(key + "-" + "insertChannelId", insertChannelId);
        nbt.setInteger(key + "-" + "priority", priority);

        return nbt;
    }

    private NBTBase serializeFilters(
        Map<Integer, ConnectorSettingsFilter> insertFilters,
        String key
    )
    { // todo might be broken
        NBTTagCompound nbt = new NBTTagCompound();
        // loop over filters and call .serializeNBT
        for (Map.Entry<Integer, ConnectorSettingsFilter> entry : insertFilters.entrySet()) {
            nbt.setTag(key + "-" + "filter_" + entry.getKey(), entry.getValue().serializeNBT(key));
        }

        return nbt;
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
        if (nbt.hasKey(key + "-" + "insertSlotCount", Constants.NBT.TAG_INT)) {
            insertSlotCount = nbt.getInteger(key + "-" + "insertSlotCount");
        }
        if (nbt.hasKey(key + "-" + "extractSlotCount", Constants.NBT.TAG_INT)) {
            extractSlotCount = nbt.getInteger(key + "-" + "extractSlotCount");
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
        if (nbt.hasKey(key + "-" + "itemCount", Constants.NBT.TAG_INT)) {
            itemCount = nbt.getInteger(key + "-" + "itemCount");
        }
        if (nbt.hasKey(key + "-" + "minSlotRange", Constants.NBT.TAG_INT)) {
            minSlotRange = nbt.getInteger(key + "-" + "minSlotRange");
        }
        if (nbt.hasKey(key + "-" + "maxSlotRange", Constants.NBT.TAG_INT)) {
            maxSlotRange = nbt.getInteger(key + "-" + "maxSlotRange");
        }

        // filters
        if (nbt.hasKey(key + "-" + "defaultInsertFilter", Constants.NBT.TAG_COMPOUND)) {
            defaultInsertFilter.deserializeNBT(nbt.getCompoundTag(key + "-" + "defaultInsertFilter"));
        }
        if (nbt.hasKey(key + "-" + "defaultExtractFilter", Constants.NBT.TAG_COMPOUND)) {
            defaultExtractFilter.deserializeNBT(nbt.getCompoundTag(key + "-" + "defaultExtractFilter"));
        }

        deserializeFilters(nbt.getTagList(key + "-" + "insertFilters", Constants.NBT.TAG_COMPOUND), insertFilters);
        deserializeFilters(nbt.getTagList(key + "-" + "extractFilters", Constants.NBT.TAG_COMPOUND), extractFilters);

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
        for (int i = 0; i < nbtFilters.tagCount(); i++) {
            NBTTagCompound filterTag = nbtFilters.getCompoundTagAt(i);
            int id = filterTag.getInteger("id");
            ConnectorSettingsFilter filter = new ConnectorSettingsFilter();
            filter.deserializeNBT(filterTag);
            filters.put(id, filter);
        }
    }

}
