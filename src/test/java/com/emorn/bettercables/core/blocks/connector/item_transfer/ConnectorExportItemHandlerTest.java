package com.emorn.bettercables.core.blocks.connector.item_transfer;

import com.emorn.bettercables.contract.common.*;
import com.emorn.bettercables.core.blocks.connector.IConnectorNetworkService;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.network.ExtractSlot;
import com.emorn.bettercables.core.blocks.connector.network.InsertSlot;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorNetworkSettingsService;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSide;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSides;
import com.emorn.bettercables.core.common.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.reflect.Whitebox.getInternalState;

public class ConnectorExportItemHandlerTest
{
    private ConnectorExportItemHandler handler;
    private ConnectorSides mockConnectorSides;
    private IConnectorNetworkService mockConnectorNetworkService;
    private InventoryService mockInventoryService;
    private ConnectorInventoryLocator mockConnectorInventoryLocator;
    private ConnectorNetworkSettingsService mockConnectorNetworkSettingsService;
    private ConnectorNetwork mockNetwork;
    private IPositionInWorld mockPositionInWorld;
    private IWorld mockWorld;
    private IItemHandler mockImportInventoryHandler;
    private IInventory mockImportInventory;
    private IItemHandler mockExportInventoryHandler;
    private IInventory mockExportInventory;
    private ConnectorSettings mockExportSettings;
    private ConnectorSettings mockImportSettings;
    private ConnectorSide mockConnectorSide;

    @Before
    public void setUp()
    {
        mockConnectorSides = mock(ConnectorSides.class);
        mockConnectorNetworkService = mock(IConnectorNetworkService.class);
        mockInventoryService = mock(InventoryService.class);
        mockConnectorInventoryLocator = mock(ConnectorInventoryLocator.class);
        mockConnectorNetworkSettingsService = mock(ConnectorNetworkSettingsService.class);

        handler = new ConnectorExportItemHandler(mockConnectorSides, mockConnectorNetworkService);

        setInternalState(handler, "inventoryService", mockInventoryService);
        setInternalState(handler, "connectorInventoryLocator", mockConnectorInventoryLocator);
        setInternalState(handler, "connectorNetworkSettingsService", mockConnectorNetworkSettingsService);

        mockNetwork = mock(ConnectorNetwork.class);
        mockPositionInWorld = mock(IPositionInWorld.class);
        mockWorld = mock(IWorld.class);
        mockImportInventoryHandler = mock(IItemHandler.class);
        mockImportInventory= mock(IInventory.class);
        mockExportInventoryHandler = mock(IItemHandler.class);
        mockExportInventory= mock(IInventory.class);
        mockExportSettings = mock(ConnectorSettings.class);
        mockImportSettings = mock(ConnectorSettings.class);
        mockConnectorSide = mock(ConnectorSide.class);

        when(mockConnectorNetworkService.getNetwork()).thenReturn(mockNetwork);
        when(mockConnectorSides.findConnectorByDirection(any())).thenReturn(mockConnectorSide);
        when(mockConnectorSide.connectorSettings()).thenReturn(mockExportSettings);
        when(mockConnectorNetworkSettingsService.settings(any())).thenReturn(mockExportSettings);
        when(mockImportInventory.getItemHandler()).thenReturn(mockImportInventoryHandler);
        when(mockExportInventory.getItemHandler()).thenReturn(mockExportInventoryHandler);

        when(mockImportInventoryHandler.slotCount()).thenReturn(64);
        when(mockExportInventoryHandler.slotCount()).thenReturn(64);
        when(mockImportSettings.inventorySlotCount()).thenReturn(64);
        when(mockExportSettings.inventorySlotCount()).thenReturn(64);
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
    public void invoke_networkDisabled_doesNothing()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(true);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockConnectorNetworkService).isNetworkDisabled();
        verifyNoMoreInteractions(
            mockConnectorNetworkService,
            mockNetwork,
            mockConnectorInventoryLocator,
            mockInventoryService
        );
    }

    @Test
    public void invoke_noNextIndex_doesNothing()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(null);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockConnectorNetworkService).isNetworkDisabled();
        verify(mockConnectorNetworkService).getNetwork();
        verify(mockNetwork).findNextIndex(-1);
        verifyNoMoreInteractions(
            mockConnectorNetworkService,
            mockNetwork,
            mockConnectorInventoryLocator,
            mockInventoryService
        );
    }

    @Test
    public void invoke_noImportInventory_doesNothing()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(null);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockConnectorNetworkService).getNetwork();
        verify(mockNetwork).findNextIndex(-1);
        verifyNoMoreInteractions(mockConnectorInventoryLocator, mockInventoryService);
    }

    @Test
    public void invoke_noInventorySettings_doesNothing()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(
            any(Direction.class),
            any(ConnectorNetwork.class),
            anyInt()
        )).thenReturn(null);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockConnectorNetworkService).getNetwork();
        verify(mockNetwork).findNextIndex(-1);
        verify(mockConnectorNetworkSettingsService).findImportSettings(Direction.NORTH, mockNetwork, 0);
        verifyNoMoreInteractions(mockConnectorInventoryLocator, mockInventoryService);
    }

    @Test
    public void invoke_noExportInventory_doesNothing()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(null);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockConnectorInventoryLocator).findExportInventory(
            Direction.NORTH,
            mockPositionInWorld,
            mockWorld,
            mockNetwork,
            mockExportSettings
        );
        verifyNoMoreInteractions(mockInventoryService);
    }

    @Test
    public void invoke_noConnectorSide_doesNothing()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        ConnectorSides connectorSides = new ConnectorSides();
        handler = new ConnectorExportItemHandler(connectorSides, mockConnectorNetworkService);

        setInternalState(handler, "inventoryService", mockInventoryService);
        setInternalState(handler, "connectorInventoryLocator", mockConnectorInventoryLocator);
        setInternalState(handler, "connectorNetworkSettingsService", mockConnectorNetworkSettingsService);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verifyNoMoreInteractions(mockInventoryService);
    }

    @Test
    public void invoke_noConnectorSettings_doesNothing()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        when(mockConnectorNetworkSettingsService.settings(any())).thenReturn(null);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockConnectorNetworkSettingsService).settings(Direction.NORTH);
        verifyNoMoreInteractions(mockInventoryService);
    }

    @Test
    public void invoke_successfulTransfer_singleSlotPair()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        List<ExtractSlot> slotPairs = new ArrayList<>();
        List<InsertSlot> insertSlots = new ArrayList<>();
        insertSlots.add(new InsertSlot(0));
        ExtractSlot extractSlot = new ExtractSlot(0);
        extractSlot.addInsert(mockImportSettings, insertSlots);

        slotPairs.add(extractSlot);
        when(mockNetwork.getPossibleSlots(any())).thenReturn(slotPairs);

        IItemStack mockItemStack = mock(IItemStack.class);
        when(mockItemStack.isEmpty()).thenReturn(false);
        when(mockInventoryService.extractItemFromInventory(mockExportInventoryHandler, 0, 1)).thenReturn(mockItemStack);
        when(mockInventoryService.insertItemIntoInventory(mockImportInventoryHandler, 0, mockItemStack)).thenReturn(mock(
            IItemStack.class));
        when(mockItemStack.getCount()).thenReturn(1);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockInventoryService).extractItemFromInventory(mockExportInventoryHandler, 0, 1);
        verify(mockInventoryService).insertItemIntoInventory(mockImportInventoryHandler, 0, mockItemStack);
        verify(mockInventoryService).insertItemIntoInventory(
            eq(mockExportInventoryHandler),
            eq(0),
            any(IItemStack.class)
        );
    }

    @Test
    public void invoke_exportItemFromSlots_noPossibleIndexes()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        when(mockNetwork.getPossibleSlots(any())).thenReturn(Collections.emptyList());

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verifyNoInteractions(mockInventoryService);
    }


    @Test
    public void invoke_exportItemFromSlots_itemsIsEmpty()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        List<ExtractSlot> slotPairs = new ArrayList<>();
        List<InsertSlot> insertSlots = new ArrayList<>();
        insertSlots.add(new InsertSlot(0));
        ExtractSlot extractSlot = new ExtractSlot(0);
        extractSlot.addInsert(mockImportSettings, insertSlots);

        slotPairs.add(extractSlot);
        when(mockNetwork.getPossibleSlots(any())).thenReturn(slotPairs);
        IItemStack mockItemStack = mock(IItemStack.class);
        when(mockItemStack.isEmpty()).thenReturn(true);
        when(mockInventoryService.extractItemFromInventory(mockExportInventoryHandler, 0, 1)).thenReturn(mockItemStack);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockInventoryService).extractItemFromInventory(mockExportInventoryHandler, 0, 1);
        verifyNoMoreInteractions(mockInventoryService);
    }

    @Test
    public void invoke_exportItemFromSlots_itemsNotInserted_reinserts()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        List<ExtractSlot> slotPairs = new ArrayList<>();
        List<InsertSlot> insertSlots = new ArrayList<>();
        insertSlots.add(new InsertSlot(0));
        ExtractSlot extractSlot = new ExtractSlot(0);
        extractSlot.addInsert(mockImportSettings, insertSlots);

        slotPairs.add(extractSlot);
        when(mockNetwork.getPossibleSlots(any())).thenReturn(slotPairs);

        IItemStack mockItemStack = mock(IItemStack.class);
        when(mockItemStack.isEmpty()).thenReturn(false);
        when(mockItemStack.getCount()).thenReturn(5);
        when(mockInventoryService.extractItemFromInventory(mockExportInventoryHandler, 0, 1)).thenReturn(mockItemStack);

        IItemStack mockNotInserted = mock(IItemStack.class);
        when(mockNotInserted.isEmpty()).thenReturn(false);
        when(mockNotInserted.getCount()).thenReturn(2);
        when(mockInventoryService.insertItemIntoInventory(mockImportInventoryHandler, 0, mockItemStack)).thenReturn(
            mockNotInserted);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockInventoryService).extractItemFromInventory(mockExportInventoryHandler, 0, 1);
        verify(mockInventoryService).insertItemIntoInventory(mockImportInventoryHandler, 0, mockItemStack);
        verify(mockInventoryService).insertItemIntoInventory(mockExportInventoryHandler, 0, mockNotInserted);
    }

    @Test
    public void invoke_multipleSlotPairs_iteratesCorrectly()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        List<ExtractSlot> slotPairs = new ArrayList<>();
        List<InsertSlot> insertSlots1 = new ArrayList<>();
        List<InsertSlot> insertSlots2 = new ArrayList<>();
        insertSlots1.add(new InsertSlot(0));
        insertSlots2.add(new InsertSlot(1));
        ExtractSlot extractSlot1 = new ExtractSlot(0);
        extractSlot1.addInsert(mockImportSettings, insertSlots1);
        ExtractSlot extractSlot2 = new ExtractSlot(1);
        extractSlot2.addInsert(mockImportSettings, insertSlots2);

        slotPairs.add(extractSlot1);
        slotPairs.add(extractSlot2);
        when(mockNetwork.getPossibleSlots(any())).thenReturn(slotPairs);

        IItemStack mockItemStack1 = mock(IItemStack.class);
        when(mockItemStack1.isEmpty()).thenReturn(false);
        when(mockInventoryService.extractItemFromInventory(mockExportInventoryHandler, 0, 1)).thenReturn(mockItemStack1);
        when(mockInventoryService.insertItemIntoInventory(mockImportInventoryHandler, 0, mockItemStack1)).thenReturn(mock(
            IItemStack.class));

        IItemStack mockItemStack2 = mock(IItemStack.class);
        when(mockItemStack2.isEmpty()).thenReturn(false);
        when(mockInventoryService.extractItemFromInventory(mockExportInventoryHandler, 1, 1)).thenReturn(mockItemStack2);
        when(mockInventoryService.insertItemIntoInventory(mockImportInventoryHandler, 1, mockItemStack2)).thenReturn(mock(
            IItemStack.class));

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockInventoryService).extractItemFromInventory(mockExportInventoryHandler, 0, 1);
        verify(mockInventoryService).insertItemIntoInventory(mockImportInventoryHandler, 0, mockItemStack1);
        verify(mockInventoryService).extractItemFromInventory(mockExportInventoryHandler, 1, 1);
        verify(mockInventoryService).insertItemIntoInventory(mockImportInventoryHandler, 1, mockItemStack2);
    }

    @Test
    public void invoke_multipleSlotPairs_stopsOnCannotExtract()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(
            any(),
            any(),
            anyInt()
        )).thenReturn(
            mockImportSettings
        );
        List<ExtractSlot> slotPairs = new ArrayList<>();
        List<InsertSlot> insertSlots1 = new ArrayList<>();
        List<InsertSlot> insertSlots2 = new ArrayList<>();
        insertSlots1.add(new InsertSlot(0));
        insertSlots2.add(new InsertSlot(1));
        ExtractSlot extractSlot1 = new ExtractSlot(0);
        extractSlot1.addInsert(mockImportSettings, insertSlots1);
        ExtractSlot extractSlot2 = new ExtractSlot(1);
        extractSlot2.addInsert(mockImportSettings, insertSlots2);

        slotPairs.add(extractSlot1);
        slotPairs.add(extractSlot2);
        when(mockNetwork.getPossibleSlots(any())).thenReturn(slotPairs);

        IItemStack mockItemStack1 = mock(IItemStack.class);
        when(mockItemStack1.isEmpty()).thenReturn(true);
        when(mockInventoryService.extractItemFromInventory(mockExportInventoryHandler, 0, 1)).thenReturn(mockItemStack1);
        when(mockInventoryService.extractItemFromInventory(mockExportInventoryHandler, 1, 1)).thenReturn(mockItemStack1);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockInventoryService).extractItemFromInventory(mockExportInventoryHandler, 0, 1);
        verify(mockInventoryService, never()).insertItemIntoInventory(any(), anyInt(), any());
    }

    @Test
    public void invoke_directionToIndexMap_updatesCorrectly()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        List<ExtractSlot> slotPairs = new ArrayList<>();
        List<InsertSlot> insertSlots = new ArrayList<>();
        insertSlots.add(new InsertSlot(0));
        ExtractSlot extractSlot = new ExtractSlot(0);
        extractSlot.addInsert(new ConnectorSettings(), insertSlots);

        slotPairs.add(extractSlot);
        when(mockNetwork.getPossibleSlots(any())).thenReturn(slotPairs);
        IItemStack mockItemStack = mock(IItemStack.class);
        when(mockItemStack.isEmpty()).thenReturn(false);
        when(mockItemStack.getCount()).thenReturn(5);
        when(mockInventoryService.extractItemFromInventory(mockExportInventoryHandler, 0, 1)).thenReturn(mockItemStack);
        IItemStack mockNotInserted = mock(IItemStack.class);
        when(mockNotInserted.isEmpty()).thenReturn(false);
        when(mockNotInserted.getCount()).thenReturn(2);
        when(mockInventoryService.insertItemIntoInventory(mockImportInventoryHandler, 0, mockItemStack)).thenReturn(
            mockNotInserted);


        when(mockNetwork.findNextIndex(-1)).thenReturn(0);
        when(mockNetwork.findNextIndex(0)).thenReturn(1);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);
        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockNetwork).findNextIndex(-1);
        verify(mockNetwork).findNextIndex(0);

        Map<Direction, Integer> map = (Map<Direction, Integer>) getInternalState(handler, "directionToIndexMap");
        Integer expected = 1;
        assertEquals(expected, map.get(Direction.NORTH));
    }

    @Test
    public void invoke_findNextInsertInventoryIndex_nullCheck()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(-1)).thenReturn(null);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockConnectorNetworkService).isNetworkDisabled();
        verify(mockNetwork).findNextIndex(-1);
    }

    @Test
    public void exportItemFromSlots_reinsertionFailure_noItemLoss()
    {
        when(mockConnectorNetworkService.isNetworkDisabled()).thenReturn(false);
        when(mockNetwork.findNextIndex(anyInt())).thenReturn(0);
        when(mockConnectorInventoryLocator.findImportInventory(
            any(),
            any(),
            anyInt(),
            any(),
            any()
        )).thenReturn(
            mockImportInventory
        );
        when(mockConnectorInventoryLocator.findExportInventory(
            any(),
            any(),
            any(),
            any(),
            any()
        )).thenReturn(
            mockExportInventory
        );
        when(mockConnectorNetworkSettingsService.findImportSettings(any(), any(), anyInt())).thenReturn(
            mockImportSettings);
        List<ExtractSlot> slotPairs = new ArrayList<>();
        List<InsertSlot> insertSlots = new ArrayList<>();
        insertSlots.add(new InsertSlot(0));
        ExtractSlot extractSlot = new ExtractSlot(0);
        extractSlot.addInsert(mockImportSettings, insertSlots);

        slotPairs.add(extractSlot);
        when(mockNetwork.getPossibleSlots(any())).thenReturn(slotPairs);

        IItemStack mockItemStack = mock(IItemStack.class);
        when(mockItemStack.isEmpty()).thenReturn(false);
        when(mockItemStack.getCount()).thenReturn(5);
        when(mockInventoryService.extractItemFromInventory(mockExportInventoryHandler, 0, 1)).thenReturn(mockItemStack);

        IItemStack mockNotInserted = mock(IItemStack.class);
        when(mockNotInserted.isEmpty()).thenReturn(false);
        when(mockNotInserted.getCount()).thenReturn(2);
        when(mockInventoryService.insertItemIntoInventory(mockImportInventoryHandler, 0, mockItemStack)).thenReturn(
            mockNotInserted);

        when(mockInventoryService.insertItemIntoInventory(eq(mockExportInventoryHandler), eq(0), any())).thenReturn(
            mockNotInserted);

        handler.invoke(Direction.NORTH, mockPositionInWorld, mockWorld);

        verify(mockInventoryService).extractItemFromInventory(mockExportInventoryHandler, 0, 1);
        verify(mockInventoryService).insertItemIntoInventory(mockImportInventoryHandler, 0, mockItemStack);
        verify(mockInventoryService).insertItemIntoInventory(mockExportInventoryHandler, 0, mockNotInserted);
    }
}