package com.emorn.bettercables.objects.api.forge.blocks.connector;

import com.emorn.bettercables.objects.api.forge.common.Direction;
import com.emorn.bettercables.objects.application.blocks.connector.ConnectorSide;
import com.emorn.bettercables.objects.gateway.blocks.ConnectorSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TileEntityConnector.class, TileEntity.class, ConnectorNetwork.class, ConnectorSide.class, World.class, BlockPos.class, TileEntityChest.class, IItemHandler.class, ConnectorSettings.class, ItemStack.class})
@PowerMockIgnore({"javax.script.*", "org.apache.log4j.*", "javax.management.*"})
public class TileEntityConnectorTest
{
    private TileEntityConnector tileEntityConnectorSpy;
    private ConnectorNetwork connectorNetworkMock;
    private World worldMock;

    @Before
    public void setUp() throws Exception
    {
        tileEntityConnectorSpy = PowerMockito.spy(new TileEntityConnector());

        connectorNetworkMock = mock(ConnectorNetwork.class);
        worldMock = mock(World.class);

        tileEntityConnectorSpy.setWorld(worldMock);
        PowerMockito.whenNew(World.class).withAnyArguments().thenReturn(worldMock);
        PowerMockito.whenNew(ConnectorNetwork.class).withAnyArguments().thenReturn(connectorNetworkMock);
    }

    @Test
    public void update_shouldNotExportItem_whenNetworkIsNull() throws Exception
    {
        this.tileEntityConnectorSpy.update();

        verifyPrivate(this.tileEntityConnectorSpy, times(0)).invoke("exportItem", any());
    }

    @Test
    public void update_shouldNotExportItem_whenNetworkIsDisabled() throws Exception
    {
        when(this.connectorNetworkMock.isDisabled()).thenReturn(true);
        this.tileEntityConnectorSpy.setNetwork(this.connectorNetworkMock);

        this.tileEntityConnectorSpy.update();

        verifyPrivate(this.tileEntityConnectorSpy, times(0)).invoke("exportItem", any());
    }

    @Test
    public void update_shouldMergeNetwork_whenNetworkIsRemoved()
    {
        when(this.connectorNetworkMock.isRemoved()).thenReturn(true);
        this.tileEntityConnectorSpy.setNetwork(this.connectorNetworkMock);

        BlockPos blockPos = mock(BlockPos.class);
        when(this.tileEntityConnectorSpy.getPos()).thenReturn(blockPos);

        this.tileEntityConnectorSpy.update();

        verify(this.connectorNetworkMock, times(1)).mergeToNetwork(blockPos);
    }

    @Test
    public void update_shouldExportItem_whenNetworkIsAvailable() throws Exception
    {
        when(this.connectorNetworkMock.isDisabled()).thenReturn(false);
        when(this.connectorNetworkMock.findNextIndex(anyInt())).thenReturn(0);

        BlockPos chestPos = mock(BlockPos.class);
        when(this.connectorNetworkMock.findInventoryPositionBy(anyInt())).thenReturn(chestPos);

        TileEntityChest tileEntityChestMock = mock(TileEntityChest.class);
        when(this.worldMock.getTileEntity(any())).thenReturn(tileEntityChestMock);

        List<Integer> possibleIndex = new ArrayList<>();
        possibleIndex.add(0);
        possibleIndex.add(1);

        List<List<Integer>> possibleIndexes = new ArrayList<>();
        possibleIndexes.add(possibleIndex);

        IItemHandler itemHandlerMock = mock(IItemHandler.class);
        ItemStack mockItemStack = PowerMockito.mock(ItemStack.class);
        when(mockItemStack.isEmpty())
            .thenReturn(true);
        when(mockItemStack.getCount())
            .thenReturn(0);

        PowerMockito.when(itemHandlerMock.extractItem(0, 1, false)).thenReturn(mockItemStack);
        PowerMockito.when(itemHandlerMock.extractItem(1, 1, false)).thenReturn(mockItemStack);

        when(tileEntityChestMock.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).thenReturn(itemHandlerMock);


        // Mock ConnectorSide for each direction
        ConnectorSide northMock = mock(ConnectorSide.class);
        when(northMock.canExport()).thenReturn(true);
        ConnectorSide eastMock = mock(ConnectorSide.class);
        when(eastMock.canExport()).thenReturn(true);
        ConnectorSide southMock = mock(ConnectorSide.class);
        when(southMock.canExport()).thenReturn(true);
        ConnectorSide westMock = mock(ConnectorSide.class);
        when(westMock.canExport()).thenReturn(true);
        ConnectorSide upMock = mock(ConnectorSide.class);
        when(upMock.canExport()).thenReturn(true);
        ConnectorSide downMock = mock(ConnectorSide.class);
        when(downMock.canExport()).thenReturn(true);

        when(this.connectorNetworkMock.getPossibleSlots(any())).thenReturn(possibleIndexes);


        doNothing().when(this.tileEntityConnectorSpy).markDirty();
        this.tileEntityConnectorSpy.setExtractEnabled(true, Direction.NORTH);
        this.tileEntityConnectorSpy.setExtractEnabled(true, Direction.EAST);
        this.tileEntityConnectorSpy.setExtractEnabled(true, Direction.SOUTH);
        this.tileEntityConnectorSpy.setExtractEnabled(true, Direction.WEST);
        this.tileEntityConnectorSpy.setExtractEnabled(true, Direction.UP);
        this.tileEntityConnectorSpy.setExtractEnabled(true, Direction.DOWN);

        // Set up the spy to return the correct mocked instances
        PowerMockito.doReturn(northMock).when( this.tileEntityConnectorSpy, "findConnectorByDirection", Direction.NORTH);
        PowerMockito.doReturn(eastMock).when( this.tileEntityConnectorSpy, "findConnectorByDirection", Direction.EAST);
        PowerMockito.doReturn(southMock).when( this.tileEntityConnectorSpy, "findConnectorByDirection", Direction.SOUTH);
        PowerMockito.doReturn(westMock).when( this.tileEntityConnectorSpy, "findConnectorByDirection", Direction.WEST);
        PowerMockito.doReturn(upMock).when( this.tileEntityConnectorSpy, "findConnectorByDirection", Direction.UP);
        PowerMockito.doReturn(downMock).when( this.tileEntityConnectorSpy, "findConnectorByDirection", Direction.DOWN);

        this.tileEntityConnectorSpy.setNetwork(this.connectorNetworkMock);
        this.tileEntityConnectorSpy.update();
        this.tileEntityConnectorSpy.update();

        verifyPrivate(this.tileEntityConnectorSpy, times(1)).invoke("exportItem", Direction.NORTH);
        verifyPrivate(this.tileEntityConnectorSpy, times(1)).invoke("exportItem", Direction.EAST);
        verifyPrivate(this.tileEntityConnectorSpy, times(1)).invoke("exportItem", Direction.SOUTH);
        verifyPrivate(this.tileEntityConnectorSpy, times(1)).invoke("exportItem", Direction.WEST);
        verifyPrivate(this.tileEntityConnectorSpy, times(1)).invoke("exportItem", Direction.UP);
        verifyPrivate(this.tileEntityConnectorSpy, times(1)).invoke("exportItem", Direction.DOWN);
    }

    @Test
    public void exportItem_shouldNotThrowError_whenChestIsNotFound()
    {
        when(this.connectorNetworkMock.isDisabled()).thenReturn(false);
        when(this.connectorNetworkMock.findNextIndex(anyInt())).thenReturn(0);
        when(this.connectorNetworkMock.findInventoryPositionBy(anyInt())).thenReturn(null);
        this.tileEntityConnectorSpy.setNetwork(this.connectorNetworkMock);

        this.tileEntityConnectorSpy.exportItem(Direction.NORTH);

        verify(this.worldMock, times(0)).getTileEntity(any());
    }

    @Test
    public void exportItem_shouldNotThrowError_whenExportChestIsNotFound()
    {
        when(this.connectorNetworkMock.isDisabled()).thenReturn(false);
        when(this.connectorNetworkMock.findNextIndex(anyInt())).thenReturn(0);

        BlockPos chestPos = mock(BlockPos.class);
        when(this.connectorNetworkMock.findInventoryPositionBy(anyInt())).thenReturn(chestPos);

        this.tileEntityConnectorSpy.setNetwork(this.connectorNetworkMock);

        this.tileEntityConnectorSpy.exportItem(Direction.NORTH);

        verify(this.worldMock, times(1)).getTileEntity(any());
    }

    @Test
    public void exportItem_shouldNotThrowError_whenImportChestIsNotFound()
    {
        when(this.connectorNetworkMock.isDisabled()).thenReturn(false);
        when(this.connectorNetworkMock.findNextIndex(anyInt())).thenReturn(0);

        BlockPos chestPos = mock(BlockPos.class);
        when(this.connectorNetworkMock.findInventoryPositionBy(anyInt())).thenReturn(chestPos);

        TileEntity tileEntityMock = mock(TileEntity.class);
        when(this.worldMock.getTileEntity(any())).thenReturn(tileEntityMock);

        this.tileEntityConnectorSpy.setNetwork(this.connectorNetworkMock);

        this.tileEntityConnectorSpy.exportItem(Direction.NORTH);

        verify(this.worldMock, times(1)).getTileEntity(any());
    }

    @Test
    public void exportItem_shouldNotThrowError_whenConnectorSideIsNotFound() throws Exception
    {
        when(this.connectorNetworkMock.isDisabled()).thenReturn(false);
        when(this.connectorNetworkMock.findNextIndex(anyInt())).thenReturn(0);

        BlockPos chestPos = mock(BlockPos.class);
        when(this.connectorNetworkMock.findInventoryPositionBy(anyInt())).thenReturn(chestPos);

        TileEntityChest tileEntityChestMock = mock(TileEntityChest.class);
        when(this.worldMock.getTileEntity(any())).thenReturn(tileEntityChestMock);

        this.tileEntityConnectorSpy.setNetwork(this.connectorNetworkMock);

        this.tileEntityConnectorSpy.exportItem(Direction.NORTH);

        verifyPrivate(this.tileEntityConnectorSpy, times(1)).invoke("findConnectorByDirection", any());
    }

    @Test
    public void exportItem_shouldNotFail_whenThereAreNoPossibleIndexes() throws Exception
    {
        when(this.connectorNetworkMock.isDisabled()).thenReturn(false);
        when(this.connectorNetworkMock.findNextIndex(anyInt())).thenReturn(0);

        BlockPos chestPos = mock(BlockPos.class);
        when(this.connectorNetworkMock.findInventoryPositionBy(anyInt())).thenReturn(chestPos);

        TileEntityChest tileEntityChestMock = mock(TileEntityChest.class);
        when(this.worldMock.getTileEntity(any())).thenReturn(tileEntityChestMock);

        ConnectorSide connectorSideMock = mock(ConnectorSide.class);
        PowerMockito.doReturn(connectorSideMock).when( this.tileEntityConnectorSpy, "findConnectorByDirection", Direction.NORTH);

        this.tileEntityConnectorSpy.setNetwork(this.connectorNetworkMock);

        this.tileEntityConnectorSpy.exportItem(Direction.NORTH);

        verify(this.connectorNetworkMock, times(1)).getPossibleSlots(connectorSideMock);
    }

    @Test
    public void exportItem_shouldTryToExport_whenEverythingIsAvailable() throws Exception
    {
        when(this.connectorNetworkMock.isDisabled()).thenReturn(false);
        when(this.connectorNetworkMock.findNextIndex(anyInt())).thenReturn(0);

        BlockPos chestPos = mock(BlockPos.class);
        when(this.connectorNetworkMock.findInventoryPositionBy(anyInt())).thenReturn(chestPos);

        TileEntityChest tileEntityChestMock = mock(TileEntityChest.class);
        when(this.worldMock.getTileEntity(any())).thenReturn(tileEntityChestMock);

        ConnectorSide connectorSideMock = mock(ConnectorSide.class);
        PowerMockito.doReturn(connectorSideMock).when( this.tileEntityConnectorSpy, "findConnectorByDirection", Direction.NORTH);

        List<Integer> possibleIndex = new ArrayList<>();
        possibleIndex.add(0);
        possibleIndex.add(1);

        List<List<Integer>> possibleIndexes = new ArrayList<>();
        possibleIndexes.add(possibleIndex);

        when(this.connectorNetworkMock.getPossibleSlots(connectorSideMock)).thenReturn(possibleIndexes);

        IItemHandler itemHandlerMock = mock(IItemHandler.class);
        ItemStack mockItemStack = PowerMockito.mock(ItemStack.class);
        when(mockItemStack.isEmpty())
            .thenReturn(true);
        when(mockItemStack.getCount())
            .thenReturn(0);

        PowerMockito.when(itemHandlerMock.extractItem(0, 1, false)).thenReturn(mockItemStack);
        PowerMockito.when(itemHandlerMock.extractItem(1, 1, false)).thenReturn(mockItemStack);

        when(tileEntityChestMock.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).thenReturn(itemHandlerMock);

        this.tileEntityConnectorSpy.setNetwork(this.connectorNetworkMock);

        this.tileEntityConnectorSpy.exportItem(Direction.NORTH);

        verifyPrivate(this.tileEntityConnectorSpy, times(1)).invoke("extractItemFromChest", eq(tileEntityChestMock), eq(possibleIndex.get(0)), eq(1));
    }
}