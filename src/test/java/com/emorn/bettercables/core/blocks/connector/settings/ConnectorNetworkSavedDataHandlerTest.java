package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ConnectorNetworkSavedDataHandlerTest {

    private ConnectorNetworkSavedDataHandler handler;
    private ConnectorSides mockConnectorSides;
    private IData mockCompound;
    private IPositionInWorld mockPosition;
    private ConnectorNetwork mockNetwork;

    @Before
    public void setUp() {
        mockConnectorSides = mock(ConnectorSides.class);
        mockCompound = mock(IData.class);
        mockPosition = mock(IPositionInWorld.class);
        mockNetwork = mock(ConnectorNetwork.class);

        handler = new ConnectorNetworkSavedDataHandler(mockConnectorSides);
    }

    @Test
    public void retrieveNetworkFromNBT_noNetworkId_returnsNull() {
        when(mockCompound.loadInteger(ConnectorNetworkSavedDataHandler.NETWORK_ID)).thenReturn(0);

        ConnectorNetwork result = handler.retrieveNetworkFromNBT(mockCompound, mockPosition);

        assertNull(result);
    }

    @Test
    public void storeNetworkFromNBT_nullNetwork_savesZero() {
        handler.storeNetworkFromNBT(mockCompound, null);
        verify(mockCompound).save(ConnectorNetworkSavedDataHandler.NETWORK_ID, 0);
    }

    @Test
    public void storeNetworkFromNBT_validNetwork_savesNetworkId() {
        when(mockNetwork.id()).thenReturn(123);
        handler.storeNetworkFromNBT(mockCompound, mockNetwork);
        verify(mockCompound).save(ConnectorNetworkSavedDataHandler.NETWORK_ID, 123);
    }
}