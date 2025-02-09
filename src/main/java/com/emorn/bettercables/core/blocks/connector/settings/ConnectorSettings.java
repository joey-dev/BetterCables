package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.common.gui.ExtractType;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IItemStack;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSettings
{
    private final ConnectorSettingsSerializer connectorSettingsSerializer;
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

    public ConnectorSettings()
    {
        this.connectorSettingsSerializer = new ConnectorSettingsSerializer();
    }

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

    public Map<Integer, ConnectorSettingsFilter> insertFilters()
    {
        return insertFilters;
    }

    public Map<Integer, ConnectorSettingsFilter> extractFilters()
    {
        return extractFilters;
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

    public void serializeNBT(IData nbt, String key)
    {
        this.connectorSettingsSerializer.serialize(nbt, key, this);
    }

    public void deserializeNBT(
        IData nbt,
        String key
    )
    {
        this.connectorSettingsSerializer.deserialize(nbt, key, this);
    }

    public void changeFilterItem(
        int index,
        boolean isInsert,
        IItemStack itemStack
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
