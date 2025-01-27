package com.emorn.bettercables.objects.gateway.blocks;

import com.emorn.bettercables.common.gui.ExtractType;
import mcp.MethodsReturnNonnullByDefault;

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
    private int itemCount = 1;
    private int minSlotRange = -1;
    private int maxSlotRange = -1;

    // filters
    private ConnectorSettingsDefaultFilter defaultInsertFilter = new ConnectorSettingsDefaultFilter();
    private ConnectorSettingsDefaultFilter defaultExtractFilter = new ConnectorSettingsDefaultFilter();

    private Map<Integer, ConnectorSettingsFilter> insertFilters = new HashMap<>();
    private Map<Integer, ConnectorSettingsFilter> extractFilters = new HashMap<>();

    // insert
    private int insertChannelId = 0;
    private int priority = 0;

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

    public void changeInsertFilters(Integer id, ConnectorSettingsFilter insertFilter)
    {
        this.insertFilters.put(id, insertFilter);
    }

    public ConnectorSettingsFilter extractFilter(Integer id)
    {
        extractFilters.putIfAbsent(id, new ConnectorSettingsFilter());
        return extractFilters.get(id);
    }

    public void changeExtractFilter(Integer id, ConnectorSettingsFilter extractFilter)
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
}
