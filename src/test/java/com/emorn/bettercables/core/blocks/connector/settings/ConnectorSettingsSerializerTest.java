package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.blocks.connector.IDataList;
import com.emorn.bettercables.contract.gui.ExtractType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ConnectorSettingsSerializerTest
{
    private final String testKey = "testKey";
    private ConnectorSettingsSerializer serializer;
    private IData mockNbt;
    private IDataList mockNbtList;
    private ConnectorSettings mockSettings;
    private ConnectorSettingsDefaultFilter mockDefaultInsertFilter;
    private ConnectorSettingsDefaultFilter mockDefaultExtractFilter;
    private ConnectorSettingsFilter mockFilter1;
    private ConnectorSettingsFilter mockFilter2;

    @Before
    public void setUp()
    {
        serializer = new ConnectorSettingsSerializer();
        mockNbt = mock(IData.class);
        mockNbtList = mock(IDataList.class);
        mockSettings = mock(ConnectorSettings.class);
        mockDefaultInsertFilter = mock(ConnectorSettingsDefaultFilter.class);
        mockDefaultExtractFilter = mock(ConnectorSettingsDefaultFilter.class);
        mockFilter1 = mock(ConnectorSettingsFilter.class);
        mockFilter2 = mock(ConnectorSettingsFilter.class);

        when(mockSettings.defaultInsertFilter()).thenReturn(mockDefaultInsertFilter);
        when(mockSettings.defaultExtractFilter()).thenReturn(mockDefaultExtractFilter);
    }

    @Test
    public void serialize_savesAllFields()
    {
        when(mockSettings.isInsertEnabled()).thenReturn(true);
        when(mockSettings.isExtractEnabled()).thenReturn(false);
        when(mockSettings.inventorySlotCount()).thenReturn(27);
        when(mockSettings.extractChannelId()).thenReturn(1);
        when(mockSettings.tickRate()).thenReturn(20);
        when(mockSettings.isDynamicTickRateEnabled()).thenReturn(true);
        when(mockSettings.dynamicTickRateMinimum()).thenReturn(5);
        when(mockSettings.dynamicTickRateMaximum()).thenReturn(100);
        when(mockSettings.extractType()).thenReturn(ExtractType.PRIORITY);
        when(mockSettings.itemsPerExtract()).thenReturn(64);
        when(mockSettings.insertChannelId()).thenReturn(2);
        when(mockSettings.priority()).thenReturn(3);

        Map<Integer, ConnectorSettingsFilter> insertFilters = new HashMap<>();
        insertFilters.put(1, mockFilter1);
        when(mockSettings.insertFilters()).thenReturn(insertFilters);

        Map<Integer, ConnectorSettingsFilter> extractFilters = new HashMap<>();
        extractFilters.put(2, mockFilter2);
        when(mockSettings.extractFilters()).thenReturn(extractFilters);

        IData mockDefaultInsertNbt = mock(IData.class);
        IData mockDefaultExtractNbt = mock(IData.class);
        IData mockFilter1Data = mock(IData.class);
        IData mockFilter2Data = mock(IData.class);
        when(mockNbt.newData()).thenReturn(mockDefaultInsertNbt, mockDefaultExtractNbt);
        when(mockNbtList.newData()).thenReturn(mockFilter1Data, mockFilter2Data);


        when(mockNbt.newList()).thenReturn(mockNbtList);

        when(mockDefaultInsertFilter.serializeNBT(mockDefaultInsertNbt)).thenReturn(mockDefaultInsertNbt);
        when(mockDefaultExtractFilter.serializeNBT(mockDefaultExtractNbt)).thenReturn(mockDefaultExtractNbt);

        serializer.serialize(mockNbt, testKey, mockSettings);

        InOrder inOrder = inOrder(mockNbt);

        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.IS_INSERT_ENABLED, true);
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.IS_EXTRACT_ENABLED, false);
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.INVENTORY_SLOT_COUNT, 27);
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.EXTRACT_CHANNEL_ID, 1);
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.TICK_RATE, 20);
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.IS_DYNAMIC_TICK_RATE_ENABLED, true);
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.DYNAMIC_TICK_RATE_MINIMUM, 5);
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.DYNAMIC_TICK_RATE_MAXIMUM, 100);
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.EXTRACT_TYPE, "PRIORITY");
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.ITEMS_PER_EXTRACT, 64);
        inOrder.verify(mockNbt)
            .save(eq(testKey + "-" + ConnectorDataConstants.DEFAULT_INSERT_FILTER), any(IData.class));
        inOrder.verify(mockNbt)
            .save(eq(testKey + "-" + ConnectorDataConstants.DEFAULT_EXTRACT_FILTER), any(IData.class));
        inOrder.verify(mockNbt).save(eq(testKey + "-" + ConnectorDataConstants.INSERT_FILTERS), any(IDataList.class));
        inOrder.verify(mockNbt).save(eq(testKey + "-" + ConnectorDataConstants.EXTRACT_FILTERS), any(IDataList.class));
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.INSERT_CHANNEL_ID, 2);
        inOrder.verify(mockNbt).save(testKey + "-" + ConnectorDataConstants.PRIORITY, 3);

        verify(mockDefaultInsertFilter).serializeNBT(mockDefaultInsertNbt);
        verify(mockDefaultExtractFilter).serializeNBT(mockDefaultExtractNbt);

        verify(mockNbtList).add(mockFilter1Data);
        verify(mockNbtList).add(mockFilter2Data);

        verify(mockFilter1Data).save("id", 1);
        verify(mockFilter2Data).save("id", 2);
        verify(mockFilter1).serializeNBT(mockFilter1Data);
        verify(mockFilter2).serializeNBT(mockFilter2Data);
    }
}