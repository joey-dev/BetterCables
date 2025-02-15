package com.emorn.bettercables.core.blocks.connector.item_transfer;

import com.emorn.bettercables.contract.common.IInventory;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.contract.common.ITileEntity;
import com.emorn.bettercables.contract.common.IWorld;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.common.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ConnectorInventoryLocatorTest
{
    private ConnectorInventoryLocator locator;
    private IWorld mockWorld;
    private ConnectorNetwork mockNetwork;
    private IPositionInWorld mockPosition;
    private ITileEntity mockTileEntity;
    private IInventory mockInventory;

    @Before
    public void setUp()
    {
        mockWorld = mock(IWorld.class);
        mockNetwork = mock(ConnectorNetwork.class);
        mockPosition = mock(IPositionInWorld.class);
        mockTileEntity = mock(ITileEntity.class);
        mockInventory = mock(IInventory.class);

        locator = new ConnectorInventoryLocator();
    }

    @Test
    public void findImportInventory_validInventory_returnsInventory()
    {
        when(mockNetwork.findInventoryPositionBy(5)).thenReturn(mockPosition);
        when(mockWorld.getTileEntity(mockPosition)).thenReturn(mockTileEntity);
        when(mockTileEntity.isInventory()).thenReturn(true);
        when(mockTileEntity.getInventory()).thenReturn(mockInventory);

        IInventory result = locator.findImportInventory(Direction.NORTH, mockNetwork, 5, mockWorld);

        assertEquals(mockInventory, result);
    }

    @Test
    public void findImportInventory_noPositionFound_returnsNull()
    {
        when(mockNetwork.findInventoryPositionBy(5)).thenReturn(null);

        IInventory result = locator.findImportInventory(Direction.NORTH, mockNetwork, 5, mockWorld);

        assertNull(result);
        verify(mockNetwork).findInventoryPositionBy(5);
        verifyNoInteractions(mockWorld);
    }

    @Test
    public void findImportInventory_noTileEntity_returnsNull()
    {
        when(mockNetwork.findInventoryPositionBy(5)).thenReturn(mockPosition);
        when(mockWorld.getTileEntity(mockPosition)).thenReturn(mockTileEntity);
        when(mockTileEntity.isInventory()).thenReturn(false);

        IInventory result = locator.findImportInventory(Direction.NORTH, mockNetwork, 5, mockWorld);

        assertNull(result);
        verify(mockTileEntity, never()).getInventory();
    }

    @Test
    public void findImportInventory_tileEntityGetInventoryReturnsNull()
    {
        when(mockNetwork.findInventoryPositionBy(5)).thenReturn(mockPosition);
        when(mockWorld.getTileEntity(mockPosition)).thenReturn(mockTileEntity);
        when(mockTileEntity.isInventory()).thenReturn(true);
        when(mockTileEntity.getInventory()).thenReturn(null);

        IInventory result = locator.findImportInventory(Direction.NORTH, mockNetwork, 5, mockWorld);

        assertNull(result);
    }

    @Test
    public void findExportInventory_validInventory_returnsInventory()
    {
        IPositionInWorld mockOffsetPosition = mock(IPositionInWorld.class);
        when(mockPosition.offset(Direction.SOUTH)).thenReturn(mockOffsetPosition);
        when(mockWorld.getTileEntity(mockOffsetPosition)).thenReturn(mockTileEntity);
        when(mockTileEntity.isInventory()).thenReturn(true);
        when(mockTileEntity.getInventory()).thenReturn(mockInventory);

        IInventory result = locator.findExportInventory(Direction.SOUTH, mockPosition, mockWorld);

        assertEquals(mockInventory, result);
    }

    @Test
    public void findExportInventory_noTileEntity_returnsNull()
    {
        IPositionInWorld mockOffsetPosition = mock(IPositionInWorld.class);
        when(mockPosition.offset(Direction.SOUTH)).thenReturn(mockOffsetPosition);
        when(mockWorld.getTileEntity(mockOffsetPosition)).thenReturn(mockTileEntity);
        when(mockTileEntity.isInventory()).thenReturn(false);

        IInventory result = locator.findExportInventory(Direction.SOUTH, mockPosition, mockWorld);

        assertNull(result);
        verify(mockTileEntity, never()).getInventory();
    }

    @Test
    public void findExportInventory_tileEntityGetInventoryReturnsNull()
    {
        IPositionInWorld mockOffsetPosition = mock(IPositionInWorld.class);
        when(mockPosition.offset(Direction.SOUTH)).thenReturn(mockOffsetPosition);
        when(mockWorld.getTileEntity(mockOffsetPosition)).thenReturn(mockTileEntity);
        when(mockTileEntity.isInventory()).thenReturn(true);
        when(mockTileEntity.getInventory()).thenReturn(null);

        IInventory result = locator.findExportInventory(Direction.SOUTH, mockPosition, mockWorld);

        assertNull(result);
    }
}