package com.emorn.bettercables.core.blocks.connector.item_transfer;

import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.contract.common.IWorld;
import com.emorn.bettercables.core.blocks.connector.IConnectorNetworkService;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSides;
import com.emorn.bettercables.core.common.Direction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ConnectorUpdateHandlerTest
{
    private ConnectorUpdateHandler handler;
    private IConnectorNetworkService mockNetworkService;
    private ConnectorExportItemHandler mockExportItemHandler;
    private IPositionInWorld mockPosition;
    private IWorld mockWorld;
    private ConnectorSides spyConnectorSides;


    @Before
    public void setUp()
    {
        mockNetworkService = mock(IConnectorNetworkService.class);
        mockExportItemHandler = mock(ConnectorExportItemHandler.class);
        mockPosition = mock(IPositionInWorld.class);
        mockWorld = mock(IWorld.class);

        handler = new ConnectorUpdateHandler(mockNetworkService);

        spyConnectorSides = Mockito.spy(new ConnectorSides());

        setInternalState(handler, "connectorSides", spyConnectorSides);
        setInternalState(handler, "exportItemHandler", mockExportItemHandler);

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
    public void invoke_isClient_doesNothing()
    {
        handler.invoke(false, mockPosition, mockWorld);

        verifyNoInteractions(mockNetworkService, mockExportItemHandler, spyConnectorSides);
    }

    @Test
    public void invoke_networkDisabled_doesNothing()
    {
        when(mockNetworkService.isNetworkDisabled()).thenReturn(true);

        handler.invoke(true, mockPosition, mockWorld);

        verify(mockNetworkService).isNetworkDisabled();
        verifyNoMoreInteractions(mockNetworkService, mockExportItemHandler, spyConnectorSides);
    }

    @Test
    public void invoke_allSidesDisabled_noExports()
    {
        when(mockNetworkService.isNetworkDisabled()).thenReturn(false);
        when(spyConnectorSides.canNorthExport()).thenReturn(false);
        when(spyConnectorSides.canEastExport()).thenReturn(false);
        when(spyConnectorSides.canSouthExport()).thenReturn(false);
        when(spyConnectorSides.canWestExport()).thenReturn(false);
        when(spyConnectorSides.canUpExport()).thenReturn(false);
        when(spyConnectorSides.canDownExport()).thenReturn(false);

        handler.invoke(true, mockPosition, mockWorld);

        verify(spyConnectorSides).tick();
        verifyNoInteractions(mockExportItemHandler);
    }

    @Test
    public void invoke_northEnabled_exportsNorth()
    {
        when(mockNetworkService.isNetworkDisabled()).thenReturn(false);
        when(spyConnectorSides.canNorthExport()).thenReturn(true);

        handler.invoke(true, mockPosition, mockWorld);

        verify(spyConnectorSides).tick();
        verify(mockExportItemHandler).invoke(Direction.NORTH, mockPosition, mockWorld);
        verifyNoMoreInteractions(mockExportItemHandler);
    }

    @Test
    public void invoke_multipleSidesEnabled_exportsCorrectly()
    {
        when(mockNetworkService.isNetworkDisabled()).thenReturn(false);

        when(spyConnectorSides.canNorthExport()).thenReturn(true);
        when(spyConnectorSides.canWestExport()).thenReturn(true);

        handler.invoke(true, mockPosition, mockWorld);

        verify(spyConnectorSides).tick();
        verify(mockExportItemHandler).invoke(Direction.NORTH, mockPosition, mockWorld);
        verify(mockExportItemHandler).invoke(Direction.WEST, mockPosition, mockWorld);
        verifyNoMoreInteractions(mockExportItemHandler);
    }

    @Test
    public void invoke_allSidesEnabled_exportsAllDirections()
    {
        when(mockNetworkService.isNetworkDisabled()).thenReturn(false);

        when(spyConnectorSides.canNorthExport()).thenReturn(true);
        when(spyConnectorSides.canEastExport()).thenReturn(true);
        when(spyConnectorSides.canSouthExport()).thenReturn(true);
        when(spyConnectorSides.canWestExport()).thenReturn(true);
        when(spyConnectorSides.canUpExport()).thenReturn(true);
        when(spyConnectorSides.canDownExport()).thenReturn(true);

        handler.invoke(true, mockPosition, mockWorld);

        verify(spyConnectorSides).tick();

        verify(mockExportItemHandler).invoke(Direction.NORTH, mockPosition, mockWorld);
        verify(mockExportItemHandler).invoke(Direction.EAST, mockPosition, mockWorld);
        verify(mockExportItemHandler).invoke(Direction.SOUTH, mockPosition, mockWorld);
        verify(mockExportItemHandler).invoke(Direction.WEST, mockPosition, mockWorld);
        verify(mockExportItemHandler).invoke(Direction.UP, mockPosition, mockWorld);
        verify(mockExportItemHandler).invoke(Direction.DOWN, mockPosition, mockWorld);
    }

    @Test
    public void getConnectorSides_returnsCorrectInstance()
    {
        ConnectorSides connectorSides = handler.getConnectorSides();
        assertNotNull(connectorSides);
    }
}