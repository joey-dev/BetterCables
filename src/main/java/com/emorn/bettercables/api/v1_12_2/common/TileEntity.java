package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.api.v1_12_2.blocks.connector.ForgeTileEntityConnector;
import com.emorn.bettercables.contract.common.IInventory;
import com.emorn.bettercables.contract.common.ITileEntity;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Direction;

public class TileEntity implements ITileEntity
{
    private final net.minecraft.tileentity.TileEntity forgeTileEntity;

    public TileEntity(net.minecraft.tileentity.TileEntity forgeTileEntity)
    {
        this.forgeTileEntity = forgeTileEntity;
    }

    public boolean isInventory()
    {
        return this.forgeTileEntity instanceof net.minecraft.inventory.IInventory;
    }

    public IInventory getInventory()
    {
        if (!this.isInventory()) {
            throw new IllegalStateException("TileEntity is not an inventory");
        }

        return new Inventory((net.minecraft.inventory.IInventory) this.forgeTileEntity);
    }

    public ConnectorNetwork getNetworkIfConnector()
    {
        if (!(this.forgeTileEntity instanceof ForgeTileEntityConnector)) {
            return null;
        }

        return ((ForgeTileEntityConnector) this.forgeTileEntity).getNetwork();
    }

    @Override
    public ConnectorSettings settings(Direction direction)
    {
        if (!(this.forgeTileEntity instanceof ForgeTileEntityConnector)) {
            return null;
        }

        return ((ForgeTileEntityConnector) this.forgeTileEntity).settings(direction);
    }

    public <T> T getCapability(
        net.minecraftforge.common.capabilities.Capability<T> capability,
        net.minecraft.util.EnumFacing facing
    )
    {
        return this.forgeTileEntity.getCapability(capability, facing);
    }
}
