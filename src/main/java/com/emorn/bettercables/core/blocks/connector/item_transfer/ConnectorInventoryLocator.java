package com.emorn.bettercables.core.blocks.connector.item_transfer;

import com.emorn.bettercables.contract.common.IInventory;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.contract.common.ITileEntity;
import com.emorn.bettercables.contract.common.IWorld;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.common.Logger;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorInventoryLocator
{
    @Nullable
    public IInventory findImportInventory(
        Direction direction,
        ConnectorNetwork network,
        Integer nextIndex,
        IWorld world
    )
    {
        IPositionInWorld inventoryPosition = network.findInventoryPositionBy(nextIndex);

        if (inventoryPosition == null) {
            Logger.error("No inventory found at: " + direction);
            return null;
        }

        IInventory importInventory = this.findInventoryEntityByPosition(world, inventoryPosition);
        if (importInventory == null) {
            Logger.error("Failed to get inventory for import.");
            /**
             * todo now
             * 1. disable insert
             * 2. remove cache (in background)
             */
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
            Logger.error("No inventory found at: " + inventoryPosition); // here
            return null;
        }

        return tileEntity.getInventory();
    }

    @Nullable
    public IInventory findExportInventory(
        Direction direction,
        IPositionInWorld positionInWorld,
        IWorld world
    )
    {
        IPositionInWorld exportInventoryPosition = this.findPositionByDirection(
            direction,
            positionInWorld
        );

        IInventory exportInventory = this.findInventoryEntityByPosition(
            world,
            exportInventoryPosition
        );
        if (exportInventory == null) {
            Logger.error("Failed to get inventory inventory for export.");
            /**
             * todo now
             * 1. disable extract
             * 2. remove cache (in background)
             */
            return null;
        }
        return exportInventory;
    }

    private IPositionInWorld findPositionByDirection(
        Direction direction,
        IPositionInWorld positionInWorld
    )
    {
        return positionInWorld.offset(direction);
    }
}
