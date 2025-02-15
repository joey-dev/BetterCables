package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IItemStack;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectorSettingsTest
{
    private ConnectorSettings settings;
    private ConnectorSettingsSerializer mockSerializer;
    private IData mockNbt;

    @Before
    public void setUp()
    {
        settings = new ConnectorSettings();
        mockSerializer = mock(ConnectorSettingsSerializer.class);
        mockNbt = mock(IData.class);

        setInternalState(settings, "connectorSettingsSerializer", mockSerializer);
    }

    private void setInternalState(
        Object target,
        String fieldName,
        Object value
    )
    {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set internal state", e);
        }
    }

    @Test
    public void serializeNBT_delegatesToSerializer()
    {
        settings.serializeNBT(mockNbt, "testKey");
        verify(mockSerializer).serialize(mockNbt, "testKey", settings);
    }

    @Test
    public void deserializeNBT_delegatesToSerializer()
    {
        settings.deserializeNBT(mockNbt, "testKey");
        verify(mockSerializer).deserialize(mockNbt, "testKey", settings);
    }

    @Test
    public void insertFilter_createsNewFilterIfAbsent()
    {
        ConnectorSettingsFilter filter = settings.insertFilter(5);
        assertNotNull(filter);
        assertSame(filter, settings.insertFilter(5));
    }

    @Test
    public void extractFilter_createsNewFilterIfAbsent()
    {
        ConnectorSettingsFilter filter = settings.extractFilter(10);
        assertNotNull(filter);
        assertSame(filter, settings.extractFilter(10));
    }

    @Test
    public void changeFilterItem_insert_updatesCorrectFilter()
    {
        IItemStack mockItemStack = mock(IItemStack.class);

        settings.changeFilterItem(2, true, mockItemStack);

        Map<Integer, ConnectorSettingsFilter> insertFilters = settings.insertFilters();
        assertTrue(insertFilters.containsKey(2));
    }

    @Test
    public void changeFilterItem_extract_updatesCorrectFilter()
    {
        IItemStack mockItemStack = mock(IItemStack.class);

        settings.changeFilterItem(7, false, mockItemStack);

        Map<Integer, ConnectorSettingsFilter> extractFilters = settings.extractFilters();
        assertTrue(extractFilters.containsKey(7));
    }

    @Test
    public void changeFilterItem_insert_createsFilterIfAbsent()
    {
        IItemStack mockItemStack = mock(IItemStack.class);

        settings.changeFilterItem(3, true, mockItemStack);

        Map<Integer, ConnectorSettingsFilter> insertFilters = settings.insertFilters();
        assertTrue(insertFilters.containsKey(3));
    }

    @Test
    public void changeFilterItem_extract_createsFilterIfAbsent()
    {
        IItemStack mockItemStack = mock(IItemStack.class);

        settings.changeFilterItem(8, false, mockItemStack);

        Map<Integer, ConnectorSettingsFilter> extractFilters = settings.extractFilters();
        assertTrue(extractFilters.containsKey(8));
    }

    @Test
    public void defaultInsertFilter_returnsFilter()
    {
        ConnectorSettingsDefaultFilter filter = settings.defaultInsertFilter();
        assertNotNull(filter);
    }

    @Test
    public void defaultExtractFilter_returnsFilter()
    {
        ConnectorSettingsDefaultFilter filter = settings.defaultExtractFilter();
        assertNotNull(filter);
    }

    @Test
    public void insertFilters_returnsFilter()
    {
        Map<Integer, ConnectorSettingsFilter> filter = settings.insertFilters();
        assertNotNull(filter);
    }

    @Test
    public void extractFilters_returnsFilter()
    {
        Map<Integer, ConnectorSettingsFilter> filter = settings.extractFilters();
        assertNotNull(filter);
    }
}