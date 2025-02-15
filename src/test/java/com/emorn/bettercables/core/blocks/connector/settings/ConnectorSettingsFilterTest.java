package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.contract.blocks.connector.DataType;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IItemStack;
import com.emorn.bettercables.contract.gui.ComparisonOperator;
import com.emorn.bettercables.core.common.EmptyItemStack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConnectorSettingsFilterTest
{
    private ConnectorSettingsFilter filter;
    private IData mockNbt;
    private IItemStack mockItemStack;

    @Before
    public void setUp()
    {
        filter = new ConnectorSettingsFilter();
        mockNbt = mock(IData.class);
        mockItemStack = mock(IItemStack.class);
    }

    @Test
    public void serializeNBT_savesAllFields()
    {
        filter.changeOverwriteEnabled(true);
        filter.changeMinSlotRange(5);
        filter.changeMaxSlotRange(10);
        filter.changeOreDictEnabled(true);
        filter.changeNbtDataEnabled(true);
        filter.changeBlackListEnabled(true);
        filter.changeItemCount(2);
        filter.changeDurabilityType(ComparisonOperator.GREATER_THAN);
        filter.changeDurabilityPercentage(75);
        filter.changeItemStack(mockItemStack);
        when(mockItemStack.serialize()).thenReturn(mockNbt);

        IData resultNbt = filter.serializeNBT(mockNbt);

        verify(mockNbt).save(ConnectorDataConstants.IS_OVERWRITE_ENABLED, true);
        verify(mockNbt).save(ConnectorDataConstants.MIN_SLOT_RANGE, 5);
        verify(mockNbt).save(ConnectorDataConstants.MAX_SLOT_RANGE, 10);
        verify(mockNbt).save(ConnectorDataConstants.IS_ORE_DICT_ENABLED, true);
        verify(mockNbt).save(ConnectorDataConstants.IS_NBT_DATA_ENABLED, true);
        verify(mockNbt).save(ConnectorDataConstants.IS_BLACK_LIST_ENABLED, true);
        verify(mockNbt).save(ConnectorDataConstants.ITEM_COUNT, 2);
        verify(mockNbt).save(ConnectorDataConstants.DURABILITY_TYPE, "GREATER_THAN");
        verify(mockNbt).save(ConnectorDataConstants.DURABILITY_PERCENTAGE, 75);
        verify(mockNbt).save(eq(ConnectorDataConstants.ITEM_STACK), any(IData.class));
        assertEquals(mockNbt, resultNbt);
    }

    @Test
    public void serializeNBT_savesAllFields_nullItemStack()
    {
        filter.changeOverwriteEnabled(true);
        filter.changeMinSlotRange(5);
        filter.changeMaxSlotRange(10);
        filter.changeOreDictEnabled(true);
        filter.changeNbtDataEnabled(true);
        filter.changeBlackListEnabled(true);
        filter.changeItemCount(2);
        filter.changeDurabilityType(ComparisonOperator.GREATER_THAN);
        filter.changeDurabilityPercentage(75);

        IData resultNbt = filter.serializeNBT(mockNbt);

        verify(mockNbt).save(ConnectorDataConstants.IS_OVERWRITE_ENABLED, true);
        verify(mockNbt).save(ConnectorDataConstants.MIN_SLOT_RANGE, 5);
        verify(mockNbt).save(ConnectorDataConstants.MAX_SLOT_RANGE, 10);
        verify(mockNbt).save(ConnectorDataConstants.IS_ORE_DICT_ENABLED, true);
        verify(mockNbt).save(ConnectorDataConstants.IS_NBT_DATA_ENABLED, true);
        verify(mockNbt).save(ConnectorDataConstants.IS_BLACK_LIST_ENABLED, true);
        verify(mockNbt).save(ConnectorDataConstants.ITEM_COUNT, 2);
        verify(mockNbt).save(ConnectorDataConstants.DURABILITY_TYPE, "GREATER_THAN");
        verify(mockNbt).save(ConnectorDataConstants.DURABILITY_PERCENTAGE, 75);
        verify(mockNbt, never()).save(eq(ConnectorDataConstants.ITEM_STACK), any(IData.class));
        assertEquals(mockNbt, resultNbt);
    }

    @Test
    public void deserializeNBT_loadsAllFields()
    {
        when(mockNbt.hasKey(ConnectorDataConstants.IS_OVERWRITE_ENABLED, DataType.BOOLEAN)).thenReturn(true);
        when(mockNbt.loadBoolean(ConnectorDataConstants.IS_OVERWRITE_ENABLED)).thenReturn(true);

        when(mockNbt.hasKey(ConnectorDataConstants.MIN_SLOT_RANGE, DataType.INTEGER)).thenReturn(true);
        when(mockNbt.loadInteger(ConnectorDataConstants.MIN_SLOT_RANGE)).thenReturn(12);

        when(mockNbt.hasKey(ConnectorDataConstants.MAX_SLOT_RANGE, DataType.INTEGER)).thenReturn(true);
        when(mockNbt.loadInteger(ConnectorDataConstants.MAX_SLOT_RANGE)).thenReturn(24);

        when(mockNbt.hasKey(ConnectorDataConstants.IS_ORE_DICT_ENABLED, DataType.BOOLEAN)).thenReturn(true);
        when(mockNbt.loadBoolean(ConnectorDataConstants.IS_ORE_DICT_ENABLED)).thenReturn(true);

        when(mockNbt.hasKey(ConnectorDataConstants.IS_NBT_DATA_ENABLED, DataType.BOOLEAN)).thenReturn(true);
        when(mockNbt.loadBoolean(ConnectorDataConstants.IS_NBT_DATA_ENABLED)).thenReturn(true);

        when(mockNbt.hasKey(ConnectorDataConstants.IS_BLACK_LIST_ENABLED, DataType.BOOLEAN)).thenReturn(true);
        when(mockNbt.loadBoolean(ConnectorDataConstants.IS_BLACK_LIST_ENABLED)).thenReturn(true);

        when(mockNbt.hasKey(ConnectorDataConstants.ITEM_COUNT, DataType.INTEGER)).thenReturn(true);
        when(mockNbt.loadInteger(ConnectorDataConstants.ITEM_COUNT)).thenReturn(3);

        when(mockNbt.hasKey(ConnectorDataConstants.DURABILITY_TYPE, DataType.STRING)).thenReturn(true);
        when(mockNbt.loadString(ConnectorDataConstants.DURABILITY_TYPE)).thenReturn("LESS_THAN");

        when(mockNbt.hasKey(ConnectorDataConstants.DURABILITY_PERCENTAGE, DataType.INTEGER)).thenReturn(true);
        when(mockNbt.loadInteger(ConnectorDataConstants.DURABILITY_PERCENTAGE)).thenReturn(42);

        when(mockNbt.hasKey(ConnectorDataConstants.ITEM_STACK, DataType.COMPOUND)).thenReturn(true);
        when(mockNbt.loadItemStack(ConnectorDataConstants.ITEM_STACK)).thenReturn(mockItemStack);


        filter.deserializeNBT(mockNbt);

        assertTrue(filter.isOverwriteEnabled());
        assertEquals(12, filter.minSlotRange());
        assertEquals(24, filter.maxSlotRange());
        assertTrue(filter.isOreDictEnabled());
        assertTrue(filter.isNbtDataEnabled());
        assertTrue(filter.isBlackListEnabled());
        assertEquals(3, filter.itemCount());
        assertEquals(ComparisonOperator.LESS_THAN, filter.durabilityType());
        assertEquals(42, filter.durabilityPercentage());
        assertEquals(mockItemStack, filter.itemStack());
    }

    @Test
    public void deserializeNBT_loadsAllFields_ItemStack()
    {
        when(mockNbt.hasKey(ConnectorDataConstants.IS_OVERWRITE_ENABLED, DataType.BOOLEAN)).thenReturn(true);
        when(mockNbt.loadBoolean(ConnectorDataConstants.IS_OVERWRITE_ENABLED)).thenReturn(true);

        when(mockNbt.hasKey(ConnectorDataConstants.MIN_SLOT_RANGE, DataType.INTEGER)).thenReturn(true);
        when(mockNbt.loadInteger(ConnectorDataConstants.MIN_SLOT_RANGE)).thenReturn(12);

        when(mockNbt.hasKey(ConnectorDataConstants.MAX_SLOT_RANGE, DataType.INTEGER)).thenReturn(true);
        when(mockNbt.loadInteger(ConnectorDataConstants.MAX_SLOT_RANGE)).thenReturn(24);

        when(mockNbt.hasKey(ConnectorDataConstants.IS_ORE_DICT_ENABLED, DataType.BOOLEAN)).thenReturn(true);
        when(mockNbt.loadBoolean(ConnectorDataConstants.IS_ORE_DICT_ENABLED)).thenReturn(true);

        when(mockNbt.hasKey(ConnectorDataConstants.IS_NBT_DATA_ENABLED, DataType.BOOLEAN)).thenReturn(true);
        when(mockNbt.loadBoolean(ConnectorDataConstants.IS_NBT_DATA_ENABLED)).thenReturn(true);

        when(mockNbt.hasKey(ConnectorDataConstants.IS_BLACK_LIST_ENABLED, DataType.BOOLEAN)).thenReturn(true);
        when(mockNbt.loadBoolean(ConnectorDataConstants.IS_BLACK_LIST_ENABLED)).thenReturn(true);

        when(mockNbt.hasKey(ConnectorDataConstants.ITEM_COUNT, DataType.INTEGER)).thenReturn(true);
        when(mockNbt.loadInteger(ConnectorDataConstants.ITEM_COUNT)).thenReturn(3);

        when(mockNbt.hasKey(ConnectorDataConstants.DURABILITY_TYPE, DataType.STRING)).thenReturn(true);
        when(mockNbt.loadString(ConnectorDataConstants.DURABILITY_TYPE)).thenReturn("LESS_THAN");

        when(mockNbt.hasKey(ConnectorDataConstants.DURABILITY_PERCENTAGE, DataType.INTEGER)).thenReturn(true);
        when(mockNbt.loadInteger(ConnectorDataConstants.DURABILITY_PERCENTAGE)).thenReturn(42);

        when(mockNbt.hasKey(ConnectorDataConstants.ITEM_STACK, DataType.COMPOUND)).thenReturn(false);

        filter.deserializeNBT(mockNbt);

        assertTrue(filter.isOverwriteEnabled());
        assertEquals(12, filter.minSlotRange());
        assertEquals(24, filter.maxSlotRange());
        assertTrue(filter.isOreDictEnabled());
        assertTrue(filter.isNbtDataEnabled());
        assertTrue(filter.isBlackListEnabled());
        assertEquals(3, filter.itemCount());
        assertEquals(ComparisonOperator.LESS_THAN, filter.durabilityType());
        assertEquals(42, filter.durabilityPercentage());
        assertTrue(filter.itemStack() instanceof EmptyItemStack);
    }

    @Test
    public void deserializeNBT_missingData_usesDefaults()
    {
        filter.deserializeNBT(mockNbt);

        assertFalse(filter.isOverwriteEnabled());
        assertEquals(-1, filter.minSlotRange());
        assertEquals(-1, filter.maxSlotRange());
        assertFalse(filter.isOreDictEnabled());
        assertFalse(filter.isNbtDataEnabled());
        assertFalse(filter.isBlackListEnabled());
        assertEquals(-1, filter.itemCount());
        assertEquals(ComparisonOperator.EQUALS, filter.durabilityType());
        assertEquals(-1, filter.durabilityPercentage());
        assertTrue(filter.itemStack() instanceof EmptyItemStack);
    }


    @Test
    public void changeItemStack_setsItemStack()
    {
        filter.changeItemStack(mockItemStack);
        assertEquals(mockItemStack, filter.itemStack());
    }

    @Test
    public void initialItemStack_isEmptyItemStack()
    {
        assertTrue(filter.itemStack() instanceof EmptyItemStack);
    }
}