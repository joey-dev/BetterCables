package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.contract.gui.ExtractType;
import com.emorn.bettercables.contract.blocks.connector.DataType;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.blocks.connector.IDataList;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSettingsSerializer
{
    public void serialize(
        IData nbt,
        String key,
        ConnectorSettings settings
    )
    {
        nbt.save(key + "-" + ConnectorDataConstants.IS_INSERT_ENABLED, settings.isInsertEnabled());
        nbt.save(key + "-" + ConnectorDataConstants.IS_EXTRACT_ENABLED, settings.isExtractEnabled());
        nbt.save(key + "-" + ConnectorDataConstants.INVENTORY_SLOT_COUNT, settings.inventorySlotCount());

        serializeExtract(nbt, key, settings);
        serializeFilters(nbt, key, settings);
        serializeInsert(nbt, key, settings);
    }

    private void serializeExtract(
        IData nbt,
        String key,
        ConnectorSettings settings
    )
    {
        nbt.save(key + "-" + ConnectorDataConstants.EXTRACT_CHANNEL_ID, settings.extractChannelId());
        nbt.save(key + "-" + ConnectorDataConstants.TICK_RATE, settings.tickRate());
        nbt.save(key + "-" + ConnectorDataConstants.IS_DYNAMIC_TICK_RATE_ENABLED, settings.isDynamicTickRateEnabled());
        nbt.save(key + "-" + ConnectorDataConstants.DYNAMIC_TICK_RATE_MINIMUM, settings.dynamicTickRateMinimum());
        nbt.save(key + "-" + ConnectorDataConstants.DYNAMIC_TICK_RATE_MAXIMUM, settings.dynamicTickRateMaximum());
        nbt.save(key + "-" + ConnectorDataConstants.EXTRACT_TYPE, settings.extractType().name());
        nbt.save(key + "-" + ConnectorDataConstants.ITEMS_PER_EXTRACT, settings.itemsPerExtract());
        nbt.save(key + "-" + ConnectorDataConstants.POWER_SAVING, settings.powerSavingsSlotDisableTicks());
    }

    private void serializeFilters(
        IData nbt,
        String key,
        ConnectorSettings settings
    )
    {
        nbt.save(
            key + "-" + ConnectorDataConstants.DEFAULT_INSERT_FILTER,
            settings.defaultInsertFilter().serializeNBT(nbt.newData())
        );
        nbt.save(
            key + "-" + ConnectorDataConstants.DEFAULT_EXTRACT_FILTER,
            settings.defaultExtractFilter().serializeNBT(nbt.newData())
        );
        nbt.save(
            key + "-" + ConnectorDataConstants.INSERT_FILTERS,
            serializeFilters(nbt.newList(), settings.insertFilters())
        );
        nbt.save(
            key + "-" + ConnectorDataConstants.EXTRACT_FILTERS,
            serializeFilters(nbt.newList(), settings.extractFilters())
        );
    }

    private void serializeInsert(
        IData nbt,
        String key,
        ConnectorSettings settings
    )
    {
        nbt.save(key + "-" + ConnectorDataConstants.INSERT_CHANNEL_ID, settings.insertChannelId());
        nbt.save(key + "-" + ConnectorDataConstants.PRIORITY, settings.priority());
    }

    private IDataList serializeFilters(
        IDataList list,
        Map<Integer, ConnectorSettingsFilter> filters
    )
    {
        for (Map.Entry<Integer, ConnectorSettingsFilter> entry : filters.entrySet()) {
            IData filterTag = list.newData();
            filterTag.save("id", entry.getKey());
            entry.getValue().serializeNBT(filterTag);
            list.add(filterTag);
        }
        return list;
    }

    public void deserialize(
        IData nbt,
        String key,
        ConnectorSettings settings
    )
    {
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.IS_INSERT_ENABLED, DataType.BOOLEAN)) {
            settings.changeInsertEnabled(nbt.loadBoolean(key + "-" + ConnectorDataConstants.IS_INSERT_ENABLED));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.IS_EXTRACT_ENABLED, DataType.BOOLEAN)) {
            settings.changeExtractEnabled(nbt.loadBoolean(key + "-" + ConnectorDataConstants.IS_EXTRACT_ENABLED));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.INVENTORY_SLOT_COUNT, DataType.INTEGER)) {
            settings.changeInventorySlotCount(nbt.loadInteger(key + "-" + ConnectorDataConstants.INVENTORY_SLOT_COUNT));
        }

        deserializeExtract(nbt, key, settings);
        deserializeFilters(nbt, key, settings);
        deserializeInsert(nbt, key, settings);
    }

    private void deserializeExtract(
        IData nbt,
        String key,
        ConnectorSettings settings
    )
    {
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.EXTRACT_CHANNEL_ID, DataType.INTEGER)) {
            settings.changeExtractChannelId(nbt.loadInteger(key + "-" + ConnectorDataConstants.EXTRACT_CHANNEL_ID));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.TICK_RATE, DataType.INTEGER)) {
            settings.changeTickRate(nbt.loadInteger(key + "-" + ConnectorDataConstants.TICK_RATE));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.IS_DYNAMIC_TICK_RATE_ENABLED, DataType.BOOLEAN)) {
            settings.changeDynamicTickRateEnabled(nbt.loadBoolean(key +
                "-" +
                ConnectorDataConstants.IS_DYNAMIC_TICK_RATE_ENABLED));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.DYNAMIC_TICK_RATE_MINIMUM, DataType.INTEGER)) {
            settings.changeDynamicTickRateMinimum(nbt.loadInteger(key +
                "-" +
                ConnectorDataConstants.DYNAMIC_TICK_RATE_MINIMUM));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.DYNAMIC_TICK_RATE_MAXIMUM, DataType.INTEGER)) {
            settings.changeDynamicTickRateMaximum(nbt.loadInteger(key +
                "-" +
                ConnectorDataConstants.DYNAMIC_TICK_RATE_MAXIMUM));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.EXTRACT_TYPE, DataType.STRING)) {
            settings.changeExtractType(ExtractType.valueOf(nbt.loadString(key +
                "-" +
                ConnectorDataConstants.EXTRACT_TYPE)));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.ITEMS_PER_EXTRACT, DataType.INTEGER)) {
            settings.changeItemsPerExtract(nbt.loadInteger(key + "-" + ConnectorDataConstants.ITEMS_PER_EXTRACT));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.POWER_SAVING, DataType.INTEGER)) {
            settings.changePowerSavingsSlotDisableTicks(nbt.loadInteger(key + "-" + ConnectorDataConstants.POWER_SAVING));
        }
    }

    private void deserializeFilters(
        IData nbt,
        String key,
        ConnectorSettings settings
    )
    {
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.DEFAULT_INSERT_FILTER, DataType.COMPOUND)) {
            settings.defaultInsertFilter()
                .deserializeNBT(nbt.loadCompound(key + "-" + ConnectorDataConstants.DEFAULT_INSERT_FILTER));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.DEFAULT_EXTRACT_FILTER, DataType.COMPOUND)) {
            settings.defaultExtractFilter()
                .deserializeNBT(nbt.loadCompound(key + "-" + ConnectorDataConstants.DEFAULT_EXTRACT_FILTER));
        }

        if (nbt.hasKey(key + "-" + ConnectorDataConstants.INSERT_FILTERS, DataType.LIST)) {
            deserializeFilters(
                nbt.loadList(key + "-" + ConnectorDataConstants.INSERT_FILTERS, DataType.COMPOUND),
                settings.insertFilters()
            );
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.EXTRACT_FILTERS, DataType.LIST)) {
            deserializeFilters(
                nbt.loadList(key + "-" + ConnectorDataConstants.EXTRACT_FILTERS, DataType.COMPOUND),
                settings.extractFilters()
            );
        }
    }

    private void deserializeInsert(
        IData nbt,
        String key,
        ConnectorSettings settings
    )
    {
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.INSERT_CHANNEL_ID, DataType.INTEGER)) {
            settings.changeInsertChannelId(nbt.loadInteger(key + "-" + ConnectorDataConstants.INSERT_CHANNEL_ID));
        }
        if (nbt.hasKey(key + "-" + ConnectorDataConstants.PRIORITY, DataType.INTEGER)) {
            settings.changePriority(nbt.loadInteger(key + "-" + ConnectorDataConstants.PRIORITY));
        }
    }

    private void deserializeFilters(
        IDataList nbtFilters,
        Map<Integer, ConnectorSettingsFilter> filters
    )
    {
        filters.clear();
        for (int i = 0; i < nbtFilters.size(); i++) {
            IData filterTag = nbtFilters.findByIndex(i);
            int id = filterTag.loadInteger("id");
            ConnectorSettingsFilter filter = new ConnectorSettingsFilter();
            filter.deserializeNBT(filterTag);
            filters.put(id, filter);
        }
    }
}
