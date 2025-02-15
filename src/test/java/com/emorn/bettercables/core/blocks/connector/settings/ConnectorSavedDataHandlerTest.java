package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.ConnectorNetworkHandler;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.common.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ConnectorSavedDataHandlerTest
{
    private ConnectorSavedDataHandler handler;
    private ConnectorSides mockConnectorSides;
    private ConnectorNetworkHandler mockNetworkHandler;
    private ConnectorNetworkSavedDataHandler mockNetworkSavedDataHandler; // Mock this
    private IData mockCompound;
    private IPositionInWorld mockPosition;
    private ConnectorSettings mockSettingsNorth;
    private ConnectorSettings mockSettingsEast;
    private ConnectorSettings mockSettingsSouth;
    private ConnectorSettings mockSettingsWest;
    private ConnectorSettings mockSettingsUp;
    private ConnectorSettings mockSettingsDown;
    private ConnectorNetwork mockNetwork;

    @Before
    public void setUp()
    {
        mockConnectorSides = mock(ConnectorSides.class);
        mockNetworkHandler = mock(ConnectorNetworkHandler.class);
        mockCompound = mock(IData.class);
        mockPosition = mock(IPositionInWorld.class);
        mockNetwork = mock(ConnectorNetwork.class);

        mockSettingsNorth = mock(ConnectorSettings.class);
        mockSettingsEast = mock(ConnectorSettings.class);
        mockSettingsSouth = mock(ConnectorSettings.class);
        mockSettingsWest = mock(ConnectorSettings.class);
        mockSettingsUp = mock(ConnectorSettings.class);
        mockSettingsDown = mock(ConnectorSettings.class);

        when(mockConnectorSides.connectorSettings(Direction.NORTH)).thenReturn(mockSettingsNorth);
        when(mockConnectorSides.connectorSettings(Direction.EAST)).thenReturn(mockSettingsEast);
        when(mockConnectorSides.connectorSettings(Direction.SOUTH)).thenReturn(mockSettingsSouth);
        when(mockConnectorSides.connectorSettings(Direction.WEST)).thenReturn(mockSettingsWest);
        when(mockConnectorSides.connectorSettings(Direction.UP)).thenReturn(mockSettingsUp);
        when(mockConnectorSides.connectorSettings(Direction.DOWN)).thenReturn(mockSettingsDown);

        handler = new ConnectorSavedDataHandler(mockConnectorSides, mockNetworkHandler);

        mockNetworkSavedDataHandler = mock(ConnectorNetworkSavedDataHandler.class);
        setInternalState(handler, "connectorNetworkSavedDataHandler", mockNetworkSavedDataHandler);
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
    public void readFromNBT_deserializesSettingsAndRetrievesNetwork()
    {
        when(mockNetworkSavedDataHandler.retrieveNetworkFromNBT(mockCompound, mockPosition))
            .thenReturn(mockNetwork);

        handler.readFromNBT(mockCompound, mockPosition);

        verify(mockSettingsNorth).deserializeNBT(mockCompound, "north");
        verify(mockSettingsEast).deserializeNBT(mockCompound, "east");
        verify(mockSettingsSouth).deserializeNBT(mockCompound, "south");
        verify(mockSettingsWest).deserializeNBT(mockCompound, "west");
        verify(mockSettingsUp).deserializeNBT(mockCompound, "up");
        verify(mockSettingsDown).deserializeNBT(mockCompound, "down");

        verify(mockNetworkSavedDataHandler).retrieveNetworkFromNBT(mockCompound, mockPosition);
        verify(mockNetworkHandler).setNetwork(mockNetwork);
    }

    @Test
    public void readFromNBT_deserializesSettingsAndRetrievesNullNetwork()
    {
        when(mockNetworkSavedDataHandler.retrieveNetworkFromNBT(mockCompound, mockPosition))
            .thenReturn(null);

        handler.readFromNBT(mockCompound, mockPosition);

        verify(mockSettingsNorth).deserializeNBT(mockCompound, "north");
        verify(mockSettingsEast).deserializeNBT(mockCompound, "east");
        verify(mockSettingsSouth).deserializeNBT(mockCompound, "south");
        verify(mockSettingsWest).deserializeNBT(mockCompound, "west");
        verify(mockSettingsUp).deserializeNBT(mockCompound, "up");
        verify(mockSettingsDown).deserializeNBT(mockCompound, "down");

        verify(mockNetworkSavedDataHandler).retrieveNetworkFromNBT(mockCompound, mockPosition);
        verify(mockNetworkHandler).setNetwork(null);
    }

    @Test
    public void writeToNBT_serializesSettingsAndStoresNetwork()
    {
        when(mockNetworkHandler.getNetworkOrNull()).thenReturn(mockNetwork);

        handler.writeToNBT(mockCompound);

        verify(mockSettingsNorth).serializeNBT(mockCompound, "north");
        verify(mockSettingsEast).serializeNBT(mockCompound, "east");
        verify(mockSettingsSouth).serializeNBT(mockCompound, "south");
        verify(mockSettingsWest).serializeNBT(mockCompound, "west");
        verify(mockSettingsUp).serializeNBT(mockCompound, "up");
        verify(mockSettingsDown).serializeNBT(mockCompound, "down");

        verify(mockNetworkSavedDataHandler).storeNetworkFromNBT(mockCompound, mockNetwork);
    }

    @Test
    public void writeToNBT_serializesSettingsAndStoresNullNetwork()
    {
        when(mockNetworkHandler.getNetworkOrNull()).thenReturn(null);

        handler.writeToNBT(mockCompound);

        verify(mockSettingsNorth).serializeNBT(mockCompound, "north");
        verify(mockSettingsEast).serializeNBT(mockCompound, "east");
        verify(mockSettingsSouth).serializeNBT(mockCompound, "south");
        verify(mockSettingsWest).serializeNBT(mockCompound, "west");
        verify(mockSettingsUp).serializeNBT(mockCompound, "up");
        verify(mockSettingsDown).serializeNBT(mockCompound, "down");

        verify(mockNetworkSavedDataHandler).storeNetworkFromNBT(mockCompound, null);
    }
}