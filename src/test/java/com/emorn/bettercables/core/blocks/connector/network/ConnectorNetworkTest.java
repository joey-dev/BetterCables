package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventBus;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConnectorNetworkTest
{
    private ConnectorNetwork network;
    private ConnectorManager mockConnectorManager;
    private PossibleSlotCalculator mockPossibleSlotCalculator;
    private IPositionInWorld mockPosition;
    private ConnectorSettings mockSettings;
    private IAsyncEventBus mockAsyncEventBus;

    @Before
    public void setUp()
    {
        mockAsyncEventBus = mock(IAsyncEventBus.class);
        network = new ConnectorNetwork(123, mockAsyncEventBus);
        mockConnectorManager = mock(ConnectorManager.class);
        mockPossibleSlotCalculator = mock(PossibleSlotCalculator.class);
        mockPosition = mock(IPositionInWorld.class);
        mockSettings = mock(ConnectorSettings.class);

        setInternalState(network, "connectorManager", mockConnectorManager);
        setInternalState(network, "possibleSlotCalculator", mockPossibleSlotCalculator);
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
    public void constructor_initializesCorrectly()
    {
        assertEquals(123, network.id());
        assertFalse(network.isDisabled());
        assertFalse(network.isRemoved());
    }

    @Test
    public void findNextIndex_noConnections_returnsNull()
    {
        when(mockConnectorManager.totalInsertConnections()).thenReturn(0);
        assertNull(network.findNextIndex(5));
    }

    @Test
    public void findNextIndex_cyclesCorrectly()
    {
        when(mockConnectorManager.totalInsertConnections()).thenReturn(3);

        assertEquals((Integer) 0, network.findNextIndex(-1));
        assertEquals((Integer) 1, network.findNextIndex(0));
        assertEquals((Integer) 2, network.findNextIndex(1));
        assertEquals((Integer) 0, network.findNextIndex(2));
        assertEquals((Integer) 1, network.findNextIndex(0));
    }

    @Test
    public void findInventoryPositionBy_delegatesToConnectorManager()
    {
        when(mockConnectorManager.findInventoryPositionBy(2)).thenReturn(mockPosition);
        assertEquals(mockPosition, network.findInventoryPositionBy(2));
        verify(mockConnectorManager).findInventoryPositionBy(2);
    }

    @Test
    public void findInsertSettingsBy_delegatesToConnectorManager()
    {
        when(mockConnectorManager.findInsertSettingsBy(7)).thenReturn(mockSettings);
        assertEquals(mockSettings, network.findInsertSettingsBy(7));
        verify(mockConnectorManager).findInsertSettingsBy(7);
    }

    @Test
    public void addInsert_addsConnectionAndUpdatesCalculator()
    {
        network.addInsert(mockPosition, mockSettings);

        verify(mockConnectorManager).addInsert(mockPosition, mockSettings);
    }

    @Test
    public void addExtract_addsConnectionAndUpdatesCalculator()
    {
        network.addExtract(mockPosition, mockSettings);

        verify(mockConnectorManager).addExtract(mockPosition, mockSettings);
    }

    @Test
    public void removeInsert_removesConnectionAndUpdatesCalculator()
    {
        network.removeInsert(mockSettings);
        verify(mockConnectorManager).removeInsert(mockSettings);
    }

    @Test
    public void removeExtract_removesConnectionAndUpdatesCalculator()
    {
        network.removeExtract(mockSettings);

        verify(mockConnectorManager).removeExtract(mockSettings);
    }

    @Test
    public void getPossibleSlots_delegatesToCalculator()
    {
        ConnectorSettings mockExportSettings = mock(ConnectorSettings.class);
        List<ExtractSlot> slotPairs = new ArrayList<>();
        List<InsertSlot> insertSlots = new ArrayList<>();
        insertSlots.add(new InsertSlot(0));
        ExtractSlot extractSlot = new ExtractSlot(0);
        extractSlot.addInsert(new ConnectorSettings(), insertSlots);

        slotPairs.add(extractSlot);
        when(mockPossibleSlotCalculator.getPossibleSlots(mockExportSettings)).thenReturn(
            slotPairs);

        List<ExtractSlot> actualSlots = network.getPossibleSlots(mockExportSettings);

        assertEquals(slotPairs, actualSlots);
        verify(mockPossibleSlotCalculator).getPossibleSlots(mockExportSettings);
    }

    @Test
    public void reCalculateAllPossibleSlots_callsCalculator()
    {
        network.reCalculateAllPossibleSlots();
        verify(mockPossibleSlotCalculator).reCalculateAllPossibleSlots(anyList(), anyList());
    }

    @Test
    public void remove_network_setsShouldMergeAndMergeNetwork()
    {
        ConnectorNetwork newNetwork = new ConnectorNetwork(456, mockAsyncEventBus);
        network.remove(newNetwork);
        assertTrue(network.isRemoved());
        assertEquals(newNetwork, network.mergeToNetwork(null));
    }

    @Test
    public void remove_position_setsShouldMergeAndMergeNetwork()
    {
        ConnectorNetwork newNetwork = new ConnectorNetwork(456, mockAsyncEventBus);
        network.remove(mockPosition, newNetwork);
        assertTrue(network.isRemoved());
        assertEquals(newNetwork, network.mergeToNetwork(mockPosition));
        assertNull(network.mergeToNetwork(null));
    }

    @Test
    public void mergeToNetwork_returnsNullWhenNotRemoved()
    {
        assertNull(network.mergeToNetwork(mockPosition));
        assertNull(network.mergeToNetwork(null));
    }

    @Test
    public void updateSlotCount_delegatesToConnectorManagerAndCalculator()
    {
        int newSize = 20;
        network.updateSlotCount(newSize, mockSettings);

        verify(mockConnectorManager).updateSlotCount(newSize, mockSettings);
        verify(mockPossibleSlotCalculator).calculateForConnector(
            eq(mockSettings),
            anyList(),
            anyList()
        );
    }

    @Test
    public void isDisabled_correctlyToggledDuringOperations()
    {
        ConnectorNetwork spyNetwork = Mockito.spy(network);

        assertFalse(spyNetwork.isDisabled());

        doNothing().when(mockConnectorManager).addInsert(any(), any());
        doNothing().when(mockConnectorManager).addExtract(any(), any());
        doNothing().when(mockConnectorManager).removeInsert(any());
        doNothing().when(mockConnectorManager).removeExtract(any());

        spyNetwork.addInsert(mockPosition, mockSettings);
        assertTrue(spyNetwork.isDisabled());

        spyNetwork.addExtract(mockPosition, mockSettings);
        assertTrue(spyNetwork.isDisabled());

        spyNetwork.removeInsert(mockSettings);
        assertTrue(spyNetwork.isDisabled());

        spyNetwork.removeExtract(mockSettings);
        assertTrue(spyNetwork.isDisabled());

        spyNetwork.reCalculateAllPossibleSlots();
        assertFalse(spyNetwork.isDisabled());
    }
}