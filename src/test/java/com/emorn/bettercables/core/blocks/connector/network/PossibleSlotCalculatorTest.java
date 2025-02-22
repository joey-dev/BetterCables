package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PossibleSlotCalculatorTest {

    private PossibleSlotCalculator slotCalculator;

    @Mock private ConnectorSettings mockExportSettings;
    @Mock private ConnectorSettings mockInsertSettings;
    @Mock private ConnectorSettings mockExtractSettings;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        slotCalculator = new PossibleSlotCalculator();

        when(mockExportSettings.inventorySlotCount()).thenReturn(5);
        when(mockInsertSettings.inventorySlotCount()).thenReturn(3);
        when(mockExtractSettings.inventorySlotCount()).thenReturn(4);

        when(mockInsertSettings.isInsertEnabled()).thenReturn(true);
        when(mockExtractSettings.isExtractEnabled()).thenReturn(true);
    }

    @Test
    public void testSlotCalculation_AddExtractAndInsert_ShouldReturnCorrectSlots() {
        slotCalculator.addExtract(mockExtractSettings, Collections.singletonList(mockInsertSettings));

        slotCalculator.addInsert(mockInsertSettings, Collections.singletonList(mockExtractSettings));

        List<ExtractSlot> possibleSlots = slotCalculator.getPossibleSlots(mockExtractSettings);

        assertNotNull(possibleSlots);
        assertFalse(possibleSlots.isEmpty());

        assertEquals(4, possibleSlots.size());

        for (ExtractSlot slot : possibleSlots) {
            assertEquals(3, slot.find(mockInsertSettings).insertSlots().size());
        }
    }

    @Test
    public void testSlotCalculation_AddInsertAndExtract_ShouldReturnCorrectSlots() {
        slotCalculator.addInsert(mockInsertSettings, Collections.singletonList(mockExtractSettings));

        slotCalculator.addExtract(mockExtractSettings, Collections.singletonList(mockInsertSettings));

        List<ExtractSlot> possibleSlots = slotCalculator.getPossibleSlots(mockExtractSettings);

        assertNotNull(possibleSlots);
        assertFalse(possibleSlots.isEmpty());

        assertEquals(4, possibleSlots.size());

        for (ExtractSlot slot : possibleSlots) {
            assertEquals(3, slot.find(mockInsertSettings).insertSlots().size());
        }
    }
}

