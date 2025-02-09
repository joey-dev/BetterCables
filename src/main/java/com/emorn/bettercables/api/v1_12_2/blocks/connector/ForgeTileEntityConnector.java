package com.emorn.bettercables.api.v1_12_2.blocks.connector;

import com.emorn.bettercables.api.v1_12_2.common.PositionInWorld;
import com.emorn.bettercables.contract.blocks.connector.ITileEntityConnector;
import com.emorn.bettercables.core.blocks.connector.ConnectorNetworkHandler;
import com.emorn.bettercables.core.blocks.connector.itemTransfer.ConnectorUpdateHandler;
import com.emorn.bettercables.objects.api.forge.blocks.connector.ConnectorNetwork;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ForgeTileEntityConnector extends TileEntity implements ITickable, ITileEntityConnector
{
    private final ConnectorUpdateHandler connectorUpdateHandler;
    private final ConnectorNetworkHandler connectorNetworkHandler;

    public ForgeTileEntityConnector()
    {
        super();

        BlockPos position = this.getPos();
        World worldIn = this.getWorld();
        PositionInWorld positionInWorld = new PositionInWorld(
            position.getX(),
            position.getY(),
            position.getZ()
        );

        connectorNetworkHandler = new ConnectorNetworkHandler(
            positionInWorld
        );

        connectorUpdateHandler = new ConnectorUpdateHandler(
            positionInWorld,
            new com.emorn.bettercables.api.v1_12_2.common.World(worldIn),
            connectorNetworkHandler
        );
    }

    public void update()
    {
        boolean isClient = !this.getWorld().isRemote;

        connectorUpdateHandler.invoke(
            isClient
        );
        connectorNetworkHandler.tick(
            isClient
        );
    }

    public ConnectorNetwork getNetwork()
    {
        return connectorNetworkHandler.getNetwork();
    }
}
