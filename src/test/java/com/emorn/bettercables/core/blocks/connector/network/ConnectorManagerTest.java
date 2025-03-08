package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConnectorManagerTest {

    private ConnectorManager manager;
    private IPositionInWorld mockPosition1;
    private IPositionInWorld mockPosition2;
    private ConnectorSettings mockSettings1;
    private ConnectorSettings mockSettings2;

    @Before
    public void setUp() {
        manager = new ConnectorManager();
        mockPosition1 = mock(IPositionInWorld.class);
        mockPosition2 = mock(IPositionInWorld.class);
        mockSettings1 = mock(ConnectorSettings.class);
        mockSettings2 = mock(ConnectorSettings.class);
    }

    @Test
    public void totalInsertConnections_initiallyZero() {
        assertEquals(0, manager.totalInsertConnections());
    }

    @Test
    public void addInsert_addsConnection() {
        manager.addInsert(mockPosition1, mockSettings1);
        assertEquals(1, manager.totalInsertConnections());
    }

    @Test
    public void addInsert_duplicate_doesNotAdd() {
        manager.addInsert(mockPosition1, mockSettings1);
        manager.addInsert(mockPosition1, mockSettings1);
        assertEquals(1, manager.totalInsertConnections());
    }

    @Test
    public void addExtract_addsConnection() {
        manager.addExtract(mockPosition1, mockSettings1);
    }

    @Test
    public void addExtract_duplicate_doesNotAdd() {
        manager.addExtract(mockPosition1, mockSettings1);
        manager.addExtract(mockPosition1, mockSettings1);
    }
    @Test
    public void addExtract_duplicate_differentPositions_doesAdd() {
        manager.addExtract(mockPosition1, mockSettings1);
        manager.addExtract(mockPosition2, mockSettings1);
    }
    @Test
    public void addInsert_duplicate_differentPositions_doesAddButNotToTotalConnections() {
        manager.addInsert(mockPosition1, mockSettings1);
        manager.addInsert(mockPosition2, mockSettings1);
        assertEquals(1, manager.totalInsertConnections());
    }

    @Test
    public void removeInsert_removesConnection() {
        manager.addInsert(mockPosition1, mockSettings1);
        manager.removeInsert(mockSettings1);
        assertEquals(0, manager.totalInsertConnections());
    }

    @Test
    public void removeInsert_nonExistent_doesNothing() {
        manager.addInsert(mockPosition1, mockSettings1);
        manager.removeInsert(mockSettings2);
        assertEquals(1, manager.totalInsertConnections());
    }

    @Test
    public void removeExtract_removesConnection() {
        manager.addExtract(mockPosition1, mockSettings1);
        manager.removeExtract(mockSettings1);
    }

    @Test
    public void removeExtract_nonExistent_doesNothing() {
        manager.addExtract(mockPosition1, mockSettings1);
        manager.removeExtract(mockSettings2);
    }

    @Test
    public void updateSlotCount_updatesIfDifferent() {
        when(mockSettings1.inventorySlotCount()).thenReturn(5);
        manager.updateSlotCount(10, mockSettings1);
        verify(mockSettings1).changeInventorySlotCount(10);
    }

    @Test
    public void updateSlotCount_doesNotUpdateIfSame() {
        when(mockSettings1.inventorySlotCount()).thenReturn(10);
        manager.updateSlotCount(10, mockSettings1);
        verify(mockSettings1, never()).changeInventorySlotCount(anyInt());
    }

    @Test
    public void findInventoryPositionBy_indexOutOfBounds_returnsNull() {
        assertNull(manager.findInventoryPositionBy(0));
        assertNull(manager.findInventoryPositionBy(1));
        assertNull(manager.findInventoryPositionBy(-1));
        manager.addInsert(mockPosition1, mockSettings1);
        assertNull(manager.findInventoryPositionBy(2));
    }

    @Test
    public void findInsertSettingsBy_indexOutOfBounds_returnsNull() {
        assertNull(manager.findInsertSettingsBy(0));
        assertNull(manager.findInsertSettingsBy(1));
        assertNull(manager.findInsertSettingsBy(-1));
        manager.addInsert(mockPosition1, mockSettings1);
        assertNull(manager.findInsertSettingsBy(2));
    }

    @Test
    public void findInventoryPositionBy_validIndex_returnsPosition() {
        manager.addInsert(mockPosition1, mockSettings1);
        manager.addInsert(mockPosition2, mockSettings2);

        IPositionInWorld result = manager.findInventoryPositionBy(0);

        assertSame(mockPosition1, result);
    }

    @Test
    public void findInsertSettingsBy_validIndex_returnsSettings() {
        manager.addInsert(mockPosition1, mockSettings1);
        manager.addInsert(mockPosition2, mockSettings2);

        ConnectorSettings result = manager.findInsertSettingsBy(0);
        assertSame(mockSettings1, result);
    }
    @Test
    public void findAllInsertConnectors_returnCorrectMap(){
        manager.addInsert(mockPosition1, mockSettings1);
        Map<ConnectorSettings, IPositionInWorld> map = manager.findAllInsertConnectors();

        assertEquals(1, map.size());
        assertTrue(map.containsKey(mockSettings1));
        assertTrue(map.containsValue(mockPosition1));
    }
    @Test
    public void findAllExtractConnectors_returnCorrectMap(){
        manager.addExtract(mockPosition1, mockSettings1);
        Map<ConnectorSettings, IPositionInWorld> map = manager.findAllExtractConnectors();

        assertEquals(1, map.size());
        assertTrue(map.containsKey(mockSettings1));
        assertTrue(map.containsValue(mockPosition1));
    }
}