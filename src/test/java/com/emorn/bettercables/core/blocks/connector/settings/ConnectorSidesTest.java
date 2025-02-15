package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.core.common.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConnectorSidesTest
{
    private ConnectorSides sides;
    private ConnectorSide mockNorth;
    private ConnectorSide mockEast;
    private ConnectorSide mockSouth;
    private ConnectorSide mockWest;
    private ConnectorSide mockUp;
    private ConnectorSide mockDown;
    private ConnectorSettings mockSettings;

    @Before
    public void setUp()
    {
        sides = new ConnectorSides();
        mockNorth = mock(ConnectorSide.class);
        mockEast = mock(ConnectorSide.class);
        mockSouth = mock(ConnectorSide.class);
        mockWest = mock(ConnectorSide.class);
        mockUp = mock(ConnectorSide.class);
        mockDown = mock(ConnectorSide.class);
        mockSettings = mock(ConnectorSettings.class);

        setInternalState(sides, "north", mockNorth);
        setInternalState(sides, "east", mockEast);
        setInternalState(sides, "south", mockSouth);
        setInternalState(sides, "west", mockWest);
        setInternalState(sides, "up", mockUp);
        setInternalState(sides, "down", mockDown);

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
    public void findConnectorByDirection_returnsCorrectSide()
    {
        assertSame(mockNorth, sides.findConnectorByDirection(Direction.NORTH));
        assertSame(mockEast, sides.findConnectorByDirection(Direction.EAST));
        assertSame(mockSouth, sides.findConnectorByDirection(Direction.SOUTH));
        assertSame(mockWest, sides.findConnectorByDirection(Direction.WEST));
        assertSame(mockUp, sides.findConnectorByDirection(Direction.UP));
        assertSame(mockDown, sides.findConnectorByDirection(Direction.DOWN));
    }

    @Test
    public void canExport_delegatesToCorrectSide()
    {
        when(mockNorth.canExport()).thenReturn(true);
        when(mockEast.canExport()).thenReturn(false);

        assertTrue(sides.canNorthExport());
        assertFalse(sides.canEastExport());

        verify(mockNorth).canExport();
        verify(mockEast).canExport();
    }

    @Test
    public void connectorSettings_returnsCorrectSettings()
    {
        sides = new ConnectorSides();
        assertEquals(
            sides.findConnectorByDirection(Direction.NORTH).connectorSettings(),
            sides.connectorSettings(Direction.NORTH)
        );
        assertEquals(
            sides.findConnectorByDirection(Direction.EAST).connectorSettings(),
            sides.connectorSettings(Direction.EAST)
        );
        assertEquals(
            sides.findConnectorByDirection(Direction.SOUTH).connectorSettings(),
            sides.connectorSettings(Direction.SOUTH)
        );
        assertEquals(
            sides.findConnectorByDirection(Direction.WEST).connectorSettings(),
            sides.connectorSettings(Direction.WEST)
        );
        assertEquals(
            sides.findConnectorByDirection(Direction.UP).connectorSettings(),
            sides.connectorSettings(Direction.UP)
        );
        assertEquals(
            sides.findConnectorByDirection(Direction.DOWN).connectorSettings(),
            sides.connectorSettings(Direction.DOWN)
        );
    }

    @Test
    public void tick_ticksAllSides()
    {
        sides.tick();

        verify(mockNorth).tick();
        verify(mockEast).tick();
        verify(mockSouth).tick();
        verify(mockWest).tick();
        verify(mockUp).tick();
        verify(mockDown).tick();
    }

    @Test
    public void isInsertEnabled_validDirection_returnsCorrectValue()
    {
        when(mockNorth.connectorSettings()).thenReturn(mockSettings);
        when(mockSettings.isInsertEnabled()).thenReturn(true);

        assertTrue(sides.isInsertEnabled(Direction.NORTH));
        verify(mockNorth).connectorSettings();
        verify(mockSettings).isInsertEnabled();
    }

    @Test
    public void isExtractEnabled_validDirection_returnsCorrectValue()
    {
        when(mockSouth.connectorSettings()).thenReturn(mockSettings);
        when(mockSettings.isExtractEnabled()).thenReturn(false);

        assertFalse(sides.isExtractEnabled(Direction.SOUTH));
        verify(mockSouth).connectorSettings();
        verify(mockSettings).isExtractEnabled();
    }
}