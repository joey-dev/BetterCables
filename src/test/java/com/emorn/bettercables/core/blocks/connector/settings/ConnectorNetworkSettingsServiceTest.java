package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.common.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectorNetworkSettingsServiceTest
{
    private ConnectorNetworkSettingsService service;
    private ConnectorSides mockConnectorSides;
    private ConnectorNetwork mockNetwork;
    private ConnectorSettings mockSettings;
    private ConnectorSide mockConnectorSide;

    @Before
    public void setUp()
    {
        mockConnectorSides = mock(ConnectorSides.class);
        mockNetwork = mock(ConnectorNetwork.class);
        mockSettings = mock(ConnectorSettings.class);
        mockConnectorSide = mock(ConnectorSide.class);

        service = new ConnectorNetworkSettingsService(mockConnectorSides);
    }

    @Test
    public void findImportSettings_nullSettingsFromNetwork_returnsNull()
    {
        when(mockNetwork.findInsertSettingsBy(5)).thenReturn(null);

        ConnectorSettings result = service.findImportSettings(Direction.NORTH, mockNetwork, 5);

        assertNull(result);
    }

    @Test
    public void findImportSettings_validSettings_returnsSettings()
    {
        when(mockNetwork.findInsertSettingsBy(3)).thenReturn(mockSettings);

        ConnectorSettings result = service.findImportSettings(Direction.SOUTH, mockNetwork, 3);

        assertEquals(mockSettings, result);
    }

    @Test
    public void findImportSettings_networkFindInsertSettingsReturnsNull()
    {
        when(mockNetwork.findInsertSettingsBy(3)).thenReturn(null);

        ConnectorSettings result = service.findImportSettings(Direction.SOUTH, mockNetwork, 3);

        assertNull(result);
    }

    @Test
    public void settings_validDirection_returnsSettings()
    {

        when(mockConnectorSides.findConnectorByDirection(Direction.WEST)).thenReturn(mockConnectorSide);
        when(mockConnectorSide.connectorSettings()).thenReturn(mockSettings);

        ConnectorSettings result = service.settings(Direction.WEST);
        assertEquals(mockSettings, result);
    }

    @Test
    public void settings_returnsNullWhenConnectorSideIsNull()
    {
        when(mockConnectorSides.findConnectorByDirection(Direction.WEST)).thenReturn(null);

        ConnectorSettings result = service.settings(Direction.WEST);

        assertNull(result);
    }

    @Test
    public void settings_returnsNullWhenConnectorSettingsIsNull()
    {
        when(mockConnectorSides.findConnectorByDirection(Direction.WEST)).thenReturn(mockConnectorSide);
        when(mockConnectorSide.connectorSettings()).thenReturn(null);

        ConnectorSettings result = service.settings(Direction.WEST);

        assertNull(result);
    }
}