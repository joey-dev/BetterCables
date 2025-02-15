package com.emorn.bettercables.core.blocks.connector.settings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectorSideTest
{
    private ConnectorSide side;
    private ConnectorSettings mockSettings;

    @Before
    public void setUp()
    {
        mockSettings = mock(ConnectorSettings.class);
        side = new ConnectorSide();

        setInternalState(side, "connectorSettings", mockSettings);
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
    public void tick_incrementsAndWrapsCurrentTick()
    {
        assertEquals((Integer) 0, getInternalState(side, "currentTick"));
        side.tick();
        assertEquals((Integer) 1, getInternalState(side, "currentTick"));
        for (int i = 2; i < 20; i++) {
            side.tick();
        }
        assertEquals((Integer) 19, getInternalState(side, "currentTick"));
        side.tick();
        assertEquals((Integer) 0, getInternalState(side, "currentTick"));
        side.tick();
        assertEquals((Integer) 1, getInternalState(side, "currentTick"));
    }

    private <T> T getInternalState(
        Object target,
        String fieldName
    )
    {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get internal state", e);
        }
    }

    @Test
    public void canExport_tickRateZero_returnsFalse()
    {
        when(mockSettings.tickRate()).thenReturn(0);
        when(mockSettings.isExtractEnabled()).thenReturn(true);

        assertFalse(side.canExport());
    }

    @Test
    public void canExport_notTimeToExport_returnsFalse()
    {
        when(mockSettings.tickRate()).thenReturn(5);
        when(mockSettings.isExtractEnabled()).thenReturn(true);

        assertTrue(side.canExport());
        side.tick();
        assertFalse(side.canExport());
        side.tick();
        assertFalse(side.canExport());

    }

    @Test
    public void canExport_extractDisabled_returnsFalse()
    {
        when(mockSettings.tickRate()).thenReturn(5);
        when(mockSettings.isExtractEnabled()).thenReturn(false);

        setInternalState(side, "currentTick", 15);

        assertFalse(side.canExport());
    }

    @Test
    public void canExport_allConditionsMet_returnsTrue()
    {
        when(mockSettings.tickRate()).thenReturn(5);
        when(mockSettings.isExtractEnabled()).thenReturn(true);

        setInternalState(side, "currentTick", 10);

        assertTrue(side.canExport());
    }

    @Test
    public void canExport_allConditionsMet_returnsTrueAfterTick()
    {
        when(mockSettings.tickRate()).thenReturn(5);
        when(mockSettings.isExtractEnabled()).thenReturn(true);

        assertTrue(side.canExport());
        side.tick();
        assertFalse(side.canExport());
        side.tick();
        assertFalse(side.canExport());
        side.tick();
        assertFalse(side.canExport());
        side.tick();
        assertFalse(side.canExport());
        side.tick();
        assertTrue(side.canExport());

    }

    @Test
    public void connectorSettings_returnsSettings()
    {
        Mockito.reset(mockSettings);
        side = new ConnectorSide();

        assertNotNull(side.connectorSettings());
        assertTrue(side.connectorSettings() instanceof ConnectorSettings);
    }
}