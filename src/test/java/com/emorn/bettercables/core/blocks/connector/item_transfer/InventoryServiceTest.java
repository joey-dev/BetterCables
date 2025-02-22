package com.emorn.bettercables.core.blocks.connector.item_transfer;

import com.emorn.bettercables.contract.common.IInventory;
import com.emorn.bettercables.contract.common.IItemHandler;
import com.emorn.bettercables.contract.common.IItemStack;
import com.emorn.bettercables.core.common.EmptyItemStack;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    private InventoryService service;
    private IInventory mockInventory;
    private IItemHandler mockItemHandler;
    private IItemStack mockItemStack;

    @Before
    public void setUp() {
        service = new InventoryService();
        mockInventory = mock(IInventory.class);
        mockItemHandler = mock(IItemHandler.class);
        mockItemStack = mock(IItemStack.class);
    }

    @Test
    @Ignore
    public void extractItemFromInventory_successfulExtraction() {
        // Arrange
        when(mockInventory.getItemHandler()).thenReturn(mockItemHandler);
        when(mockItemHandler.extractItem(5, 10, false)).thenReturn(mockItemStack);
        when(mockItemStack.isEmpty()).thenReturn(false); // Simulate a successful extraction

        // Act
        IItemStack result = service.extractItemFromInventory(mockInventory, 5, 10);

        // Assert
        assertEquals(mockItemStack, result);
        verify(mockInventory).markDirty(); // markDirty should be called
    }

    @Test
    @Ignore
    public void extractItemFromInventory_emptyExtraction() {
        // Arrange
        when(mockInventory.getItemHandler()).thenReturn(mockItemHandler);
        when(mockItemHandler.extractItem(5, 10, false)).thenReturn(mockItemStack);
        when(mockItemStack.isEmpty()).thenReturn(true); // Simulate a successful extraction

        // Act
        IItemStack result = service.extractItemFromInventory(mockInventory, 5, 10);

        // Assert
        assertEquals(mockItemStack, result);
        verify(mockInventory, never()).markDirty(); // markDirty should not be called
    }

    @Test
    public void extractItemFromInventory_nullItemHandler_returnsEmptyStack() {
        // Arrange: Inventory returns a null item handler
        when(mockInventory.getItemHandler()).thenReturn(null);

        // Act
        IItemStack result = service.extractItemFromInventory(mockInventory, 5, 10);

        // Assert
        assertTrue(result instanceof EmptyItemStack);
        verify(mockInventory, never()).markDirty();
        verifyNoInteractions(mockItemHandler); // No interaction with the (null) handler
    }

    @Test
    public void extractItemFromInventory_arrayIndexOutOfBounds_returnsEmptyStack() {
        // Arrange
        when(mockInventory.getItemHandler()).thenReturn(mockItemHandler);
        // Simulate an ArrayIndexOutOfBoundsException
        when(mockItemHandler.extractItem(anyInt(), anyInt(), anyBoolean())).thenThrow(new ArrayIndexOutOfBoundsException());

        // Act
        IItemStack result = service.extractItemFromInventory(mockInventory, 5, 10);

        // Assert
        assertTrue(result instanceof EmptyItemStack);
        verify(mockInventory, never()).markDirty(); // markDirty should *not* be called
    }

    @Test
    public void insertItemIntoInventory_successfulInsertion() {
        // Arrange
        when(mockInventory.getItemHandler()).thenReturn(mockItemHandler);
        IItemStack returnedMockItemStack = mock(IItemStack.class);
        when(mockItemHandler.insertItem(3, mockItemStack, false)).thenReturn(returnedMockItemStack); // Simulate partial insertion
        when(mockItemStack.getCount()).thenReturn(5);
        when(returnedMockItemStack.getCount()).thenReturn(2); // 3 items were inserted

        // Act
        IItemStack result = service.insertItemIntoInventory(mockInventory, 3, mockItemStack);

        // Assert
        assertEquals(returnedMockItemStack, result); // Should return the remaining items
        verify(mockInventory).markDirty(); // markDirty should be called
    }
    @Test
    public void insertItemIntoInventory_noItemsInserted() {
        // Arrange
        when(mockInventory.getItemHandler()).thenReturn(mockItemHandler);
        IItemStack returnedMockItemStack = mock(IItemStack.class);
        when(mockItemHandler.insertItem(3, mockItemStack, false)).thenReturn(returnedMockItemStack);
        when(mockItemStack.getCount()).thenReturn(5);
        when(returnedMockItemStack.getCount()).thenReturn(5);

        // Act
        IItemStack result = service.insertItemIntoInventory(mockInventory, 3, mockItemStack);

        // Assert
        assertEquals(returnedMockItemStack, result);
        verify(mockInventory, never()).markDirty();
    }

    @Test
    public void insertItemIntoInventory_nullItemHandler_returnsEmptyStack() {
        // Arrange: Inventory returns a null item handler
        when(mockInventory.getItemHandler()).thenReturn(null);

        // Act
        IItemStack result = service.insertItemIntoInventory(mockInventory, 3, mockItemStack);

        // Assert
        assertTrue(result instanceof EmptyItemStack);
        verify(mockInventory, never()).markDirty();
        verifyNoInteractions(mockItemHandler); // No interaction with (null) handler
    }

    @Test
    public void insertItemIntoInventory_arrayIndexOutOfBounds_returnsOriginalStack() {
        // Arrange
        when(mockInventory.getItemHandler()).thenReturn(mockItemHandler);
        // Simulate an ArrayIndexOutOfBoundsException
        when(mockItemHandler.insertItem(anyInt(), any(IItemStack.class), anyBoolean())).thenThrow(new ArrayIndexOutOfBoundsException());

        // Act
        IItemStack result = service.insertItemIntoInventory(mockInventory, 3, mockItemStack);

        // Assert
        assertEquals(mockItemStack, result); // Should return the *original* stack
        verify(mockInventory, never()).markDirty();  // markDirty should *not* be called
    }
}