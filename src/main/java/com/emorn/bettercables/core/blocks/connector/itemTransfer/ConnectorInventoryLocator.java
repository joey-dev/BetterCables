package com.emorn.bettercables.core.blocks.connector.itemTransfer;

import com.emorn.bettercables.contract.common.IInventory;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.contract.common.ITileEntity;
import com.emorn.bettercables.contract.common.IWorld;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.objects.api.forge.blocks.connector.ConnectorNetwork;
import com.emorn.bettercables.objects.api.forge.common.Logger;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorInventoryLocator
{
    private final IPositionInWorld positionInWorld;
    private final IWorld world;

    public ConnectorInventoryLocator(
        IPositionInWorld positionInWorld,
        IWorld world
    )
    {
        this.positionInWorld = positionInWorld;
        this.world = world;
    }

    @Nullable
    public IInventory findImportInventory(
        Direction direction,
        ConnectorNetwork network,
        Integer nextIndex
    )
    {
        IPositionInWorld inventoryPosition = network.findInventoryPositionBy(nextIndex);

        if (inventoryPosition == null) {
            Logger.error("No inventory found at: " + direction);
            return null;
        }

        IInventory importInventory = this.findInventoryEntityByPosition(this.world, inventoryPosition);
        if (importInventory == null) {
            Logger.error("Failed to get inventory inventory for import.");
            return null;
        }
        return importInventory;
    }

    @Nullable
    private IInventory findInventoryEntityByPosition(
        IWorld world,
        IPositionInWorld inventoryPosition
    )
    {
        ITileEntity tileEntity = world.getTileEntity(inventoryPosition);
        if (!tileEntity.isInventory()) {
            Logger.error("No inventory found at: " + inventoryPosition);
            return null;
        }

        return tileEntity.getInventory();
    }

    @Nullable
    public IInventory findExportInventory(Direction direction)
    {
        IPositionInWorld exportInventoryPosition = this.findPositionByDirection(direction);

        IInventory exportInventory = this.findInventoryEntityByPosition(
            this.world,
            exportInventoryPosition
        );
        if (exportInventory == null) {
            Logger.error("Failed to get inventory inventory for export.");
            return null;
        }
        return exportInventory;
    }

    private IPositionInWorld findPositionByDirection(Direction direction)
    {
        return this.positionInWorld.offset(direction);
    }
}
