package com.emorn.bettercables.core.blocks.connector.settings;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorDataConstants
{
    private ConnectorDataConstants()
    {
    }

    public static final String IS_INSERT_ENABLED = "isInsertEnabled";
    public static final String IS_EXTRACT_ENABLED = "isExtractEnabled";
    public static final String INVENTORY_SLOT_COUNT = "inventorySlotCount";

    // extract
    public static final String EXTRACT_CHANNEL_ID = "extractChannelId";
    public static final String TICK_RATE = "tickRate";
    public static final String IS_DYNAMIC_TICK_RATE_ENABLED = "isDynamicTickRateEnabled";
    public static final String DYNAMIC_TICK_RATE_MINIMUM = "dynamicTickRateMinimum";
    public static final String DYNAMIC_TICK_RATE_MAXIMUM = "dynamicTickRateMaximum";
    public static final String EXTRACT_TYPE = "extractType";
    public static final String ITEMS_PER_EXTRACT = "itemsPerExtract";
    public static final String POWER_SAVING = "powerSavings";

    // filters
    public static final String DEFAULT_INSERT_FILTER = "defaultInsertFilter";
    public static final String DEFAULT_EXTRACT_FILTER = "defaultExtractFilter";
    public static final String INSERT_FILTERS = "insertFilters";
    public static final String EXTRACT_FILTERS = "extractFilters";

    // insert
    public static final String INSERT_CHANNEL_ID = "insertChannelId";
    public static final String PRIORITY = "priority";

    // within filters
    public static final String IS_OVERWRITE_ENABLED = "isOverwriteEnabled";
    public static final String MIN_SLOT_RANGE = "minSlotRange";
    public static final String MAX_SLOT_RANGE = "maxSlotRange";
    public static final String IS_ORE_DICT_ENABLED = "isOreDictEnabled";
    public static final String IS_NBT_DATA_ENABLED = "isNbtDataEnabled";
    public static final String IS_BLACK_LIST_ENABLED = "isBlackListEnabled";
    public static final String ITEM_COUNT = "itemCount";
    public static final String DURABILITY_TYPE = "durabilityType";
    public static final String DURABILITY_PERCENTAGE = "durabilityPercentage";
    public static final String ITEM_STACK = "itemStack";
}
