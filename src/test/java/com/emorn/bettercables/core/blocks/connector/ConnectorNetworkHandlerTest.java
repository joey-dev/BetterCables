package com.emorn.bettercables.core.blocks.connector;

import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConnectorNetworkHandlerTest
{
    private ConnectorNetworkHandler handler;
    private ConnectorNetwork mockNetwork;
    private IPositionInWorld mockPosition;

    @Before
    public void setUp()
    {
        handler = new ConnectorNetworkHandler();
        mockNetwork = mock(ConnectorNetwork.class);
        mockPosition = mock(IPositionInWorld.class);
    }

    @Test
    public void tick_isClientFalse_returnsEarly()
    {
        handler.tick(false, mockPosition);

        verifyNoInteractions(mockPosition);
    }

    @Test
    public void tick_networkNull_returnsEarly()
    {
        handler.tick(true, mockPosition);

        verifyNoInteractions(mockPosition);
    }

    @Test
    public void tick_networkDisabled_returnsEarly()
    {
        handler.setNetwork(mockNetwork);
        when(mockNetwork.isDisabled()).thenReturn(true);

        handler.tick(true, mockPosition);

        verify(mockNetwork).isDisabled();
        verifyNoMoreInteractions(mockNetwork);
    }

    @Test
    public void tick_networkNotRemoved_doesNotMerge()
    {
        handler.setNetwork(mockNetwork);
        when(mockNetwork.isDisabled()).thenReturn(false);
        when(mockNetwork.isRemoved()).thenReturn(false);

        handler.tick(true, mockPosition);

        verify(mockNetwork).isDisabled();
        verify(mockNetwork).isRemoved();
        verifyNoMoreInteractions(mockNetwork);
    }

    @Test
    public void tick_networkRemoved_mergesNetwork()
    {
        ConnectorNetwork mockMergedNetwork = mock(ConnectorNetwork.class);
        handler.setNetwork(mockNetwork);
        when(mockNetwork.isDisabled()).thenReturn(false);
        when(mockNetwork.isRemoved()).thenReturn(true);
        when(mockNetwork.mergeToNetwork(mockPosition)).thenReturn(mockMergedNetwork);

        handler.tick(true, mockPosition);

        verify(mockNetwork).isDisabled();
        verify(mockNetwork).isRemoved();
        verify(mockNetwork).mergeToNetwork(mockPosition);

        assertSame(mockMergedNetwork, handler.getNetworkOrNull());
    }

    @Test
    public void isNetworkDisabled_nullNetwork_returnsTrue()
    {
        assertTrue(handler.isNetworkDisabled());
    }

    @Test
    public void isNetworkDisabled_networkDisabled_returnsTrue()
    {
        handler.setNetwork(mockNetwork);
        when(mockNetwork.isDisabled()).thenReturn(true);
        assertTrue(handler.isNetworkDisabled());
    }

    @Test
    public void isNetworkDisabled_networkEnabled_returnsFalse()
    {
        handler.setNetwork(mockNetwork);
        when(mockNetwork.isDisabled()).thenReturn(false);
        assertFalse(handler.isNetworkDisabled());
    }

    @Test
    public void getNetwork_returnsNetwork()
    {
        handler.setNetwork(mockNetwork);
        assertSame(mockNetwork, handler.getNetwork());
    }

    @Test(expected = IllegalStateException.class)
    public void getNetwork_nullNetwork_throwsException()
    {
        handler.getNetwork();
    }

    @Test
    public void getNetworkOrNull_returnsNetwork()
    {
        handler.setNetwork(mockNetwork);
        assertSame(mockNetwork, handler.getNetworkOrNull());
    }

    @Test
    public void getNetworkOrNull_nullNetwork_returnsNull()
    {
        assertNull(handler.getNetworkOrNull());
    }

    @Test
    public void setNetwork_setsNetwork()
    {
        handler.setNetwork(mockNetwork);
        assertSame(mockNetwork, handler.getNetworkOrNull());

        handler.setNetwork(null);
        assertNull(handler.getNetworkOrNull());
    }
}