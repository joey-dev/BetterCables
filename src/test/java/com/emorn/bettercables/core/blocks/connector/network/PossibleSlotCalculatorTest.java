package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PossibleSlotCalculatorTest
{
    private PossibleSlotCalculator calculator;
    private ConnectorSettings mockInsertSettings1;
    private ConnectorSettings mockInsertSettings2;
    private ConnectorSettings mockExtractSettings1;
    private ConnectorSettings mockExtractSettings2;
    private IPositionInWorld mockPosition;

    @Before
    public void setUp()
    {
        calculator = new PossibleSlotCalculator();
        mockInsertSettings1 = mock(ConnectorSettings.class);
        mockInsertSettings2 = mock(ConnectorSettings.class);
        mockExtractSettings1 = mock(ConnectorSettings.class);
        mockExtractSettings2 = mock(ConnectorSettings.class);
        mockPosition = mock(IPositionInWorld.class);

        when(mockInsertSettings1.inventorySlotCount()).thenReturn(2);
        when(mockInsertSettings2.inventorySlotCount()).thenReturn(3);
        when(mockExtractSettings1.inventorySlotCount()).thenReturn(4);
        when(mockExtractSettings2.inventorySlotCount()).thenReturn(1);
    }

    @Test
    public void calculate_generatesCorrectCombinations()
    {
        List<List<Integer>> combinations = calculator.calculate(mockExtractSettings1, mockInsertSettings1);

        assertEquals(8, combinations.size());
        assertTrue(combinations.contains(Arrays.asList(0, 0)));
        assertTrue(combinations.contains(Arrays.asList(0, 1)));
        assertTrue(combinations.contains(Arrays.asList(0, 2)));
        assertTrue(combinations.contains(Arrays.asList(0, 3)));
        assertTrue(combinations.contains(Arrays.asList(1, 0)));
        assertTrue(combinations.contains(Arrays.asList(1, 1)));
        assertTrue(combinations.contains(Arrays.asList(1, 2)));
        assertTrue(combinations.contains(Arrays.asList(1, 3)));


        combinations = calculator.calculate(mockExtractSettings2, mockInsertSettings2);
        assertEquals(3, combinations.size());
        assertTrue(combinations.contains(Arrays.asList(0, 0)));
        assertTrue(combinations.contains(Arrays.asList(1, 0)));
        assertTrue(combinations.contains(Arrays.asList(2, 0)));
    }

    @Test
    public void getPossibleSlots_returnsEmptyListForNewSettings()
    {
        List<List<Integer>> slots = calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings1);
        assertNotNull(slots);
        assertTrue(slots.isEmpty());
    }

    @Test
    public void addInsert_calculatesSlotsCorrectly()
    {
        Map<ConnectorSettings, IPositionInWorld> extractSettings = new HashMap<>();
        extractSettings.put(mockExtractSettings1, mockPosition);

        calculator.addInsert(mockInsertSettings1, extractSettings);

        List<List<Integer>> slots = calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings1);
        assertEquals(8, slots.size());
        assertTrue(slots.contains(Arrays.asList(0, 0)));
    }

    @Test
    public void addExtract_calculatesSlotsCorrectly()
    {
        Map<ConnectorSettings, IPositionInWorld> insertSettings = new HashMap<>();
        insertSettings.put(mockInsertSettings1, mockPosition);

        calculator.addExtract(mockExtractSettings1, insertSettings);

        List<List<Integer>> slots = calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings1);
        assertEquals(8, slots.size());
        assertTrue(slots.contains(Arrays.asList(0, 0)));

    }

    @Test
    public void reCalculateAllPossibleSlots_calculatesCorrectly()
    {
        Map<ConnectorSettings, IPositionInWorld> insertSettings = new HashMap<>();
        insertSettings.put(mockInsertSettings1, mockPosition);
        insertSettings.put(mockInsertSettings2, mockPosition);
        Map<ConnectorSettings, IPositionInWorld> extractSettings = new HashMap<>();
        extractSettings.put(mockExtractSettings1, mockPosition);
        extractSettings.put(mockExtractSettings2, mockPosition);

        calculator.reCalculateAllPossibleSlots(insertSettings, extractSettings);

        assertEquals(8, calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings1).size());
        assertEquals(3, calculator.getPossibleSlots(mockExtractSettings2, mockInsertSettings2).size());
        assertEquals(12, calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings2).size());
        assertEquals(2, calculator.getPossibleSlots(mockExtractSettings2, mockInsertSettings1).size());
    }

    @Test
    public void removeInsert_removesCorrectSlots()
    {
        Map<ConnectorSettings, IPositionInWorld> extractSettings = new HashMap<>();
        extractSettings.put(mockExtractSettings1, mockPosition);

        calculator.addInsert(mockInsertSettings1, extractSettings);

        Map<ConnectorSettings, IPositionInWorld> extractSettings2 = new HashMap<>();
        extractSettings2.put(mockExtractSettings1, mockPosition);
        calculator.addInsert(mockInsertSettings2, extractSettings2);


        calculator.removeInsert(mockInsertSettings1);

        assertTrue(calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings1).isEmpty());
        assertFalse(calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings2).isEmpty());
    }

    @Test
    public void removeExtract_removesCorrectSlots()
    {
        Map<ConnectorSettings, IPositionInWorld> insertSettings = new HashMap<>();
        insertSettings.put(mockInsertSettings1, mockPosition);

        calculator.addExtract(mockExtractSettings1, insertSettings);
        Map<ConnectorSettings, IPositionInWorld> insertSettings2 = new HashMap<>();
        insertSettings2.put(mockInsertSettings1, mockPosition);
        calculator.addExtract(mockExtractSettings2, insertSettings2);


        calculator.removeExtract(mockExtractSettings1);

        assertTrue(calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings1).isEmpty());
        assertFalse(calculator.getPossibleSlots(mockExtractSettings2, mockInsertSettings1).isEmpty());

    }

    @Test
    public void updateSlotCount_insert_callsRecalculateInserts()
    {
        when(mockInsertSettings1.isInsertEnabled()).thenReturn(true);

        Map<ConnectorSettings, IPositionInWorld> insertSettings = new HashMap<>();
        Map<ConnectorSettings, IPositionInWorld> extractSettings = new HashMap<>();
        insertSettings.put(mockInsertSettings1, mockPosition);
        extractSettings.put(mockExtractSettings1, mockPosition);

        calculator.updateSlotCount(mockInsertSettings1, insertSettings, extractSettings);

        List<List<Integer>> slots = calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings1);
        assertEquals(8, slots.size());
        assertTrue(slots.contains(Arrays.asList(0, 0)));
    }

    @Test
    public void updateSlotCount_extract_callsRecalculateExtracts()
    {
        when(mockExtractSettings1.isExtractEnabled()).thenReturn(true);

        Map<ConnectorSettings, IPositionInWorld> insertSettings = new HashMap<>();
        Map<ConnectorSettings, IPositionInWorld> extractSettings = new HashMap<>();
        insertSettings.put(mockInsertSettings1, mockPosition);
        extractSettings.put(mockExtractSettings1, mockPosition);

        calculator.updateSlotCount(mockExtractSettings1, insertSettings, extractSettings);

        List<List<Integer>> slots = calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings1);
        assertEquals(8, slots.size());
        assertTrue(slots.contains(Arrays.asList(0, 0)));

    }

    @Test
    public void updateSlotCount_bothDisabled_noRecalculation()
    {
        Map<ConnectorSettings, IPositionInWorld> insertSettings = new HashMap<>();
        Map<ConnectorSettings, IPositionInWorld> extractSettings = new HashMap<>();
        insertSettings.put(mockInsertSettings1, mockPosition);
        extractSettings.put(mockExtractSettings1, mockPosition);

        calculator.updateSlotCount(mockInsertSettings1, insertSettings, extractSettings);

        assertTrue(calculator.getPossibleSlots(mockExtractSettings1, mockInsertSettings1).isEmpty());
    }
}