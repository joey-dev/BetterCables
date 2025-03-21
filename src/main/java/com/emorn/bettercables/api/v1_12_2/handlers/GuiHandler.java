package com.emorn.bettercables.api.v1_12_2.handlers;

import com.emorn.bettercables.api.v1_12_2.gui.ContainerConnector;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.api.v1_12_2.gui.GuiConnector;
import com.emorn.bettercables.api.v1_12_2.blocks.connector.ForgeTileEntityConnector;
import com.emorn.bettercables.core.common.Reference;
import com.google.common.collect.ImmutableMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiHandler implements IGuiHandler
{
    @Nullable
    @Override
    public Object getServerGuiElement(
        int id,
        EntityPlayer player,
        World world,
        int x,
        int y,
        int z
    )
    {
        ImmutableMap<Integer, Direction> connectorIds = ImmutableMap.<Integer, Direction>builder()
            .put(Reference.GUI_CONNECTOR_NORTH, Direction.NORTH)
            .put(Reference.GUI_CONNECTOR_EAST, Direction.EAST)
            .put(Reference.GUI_CONNECTOR_SOUTH, Direction.SOUTH)
            .put(Reference.GUI_CONNECTOR_WEST, Direction.WEST)
            .put(Reference.GUI_CONNECTOR_UP, Direction.UP)
            .put(Reference.GUI_CONNECTOR_DOWN, Direction.DOWN)
            .build();

        Direction direction = connectorIds.get(id);

        if (direction != null) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (!(tileEntity instanceof ForgeTileEntityConnector)) {
                return null;
            }
            return new ContainerConnector(
                player.inventory,
                (ForgeTileEntityConnector) tileEntity
            );
        }
        return null;
    }

    @Override
    @Nullable
    public Object getClientGuiElement(
        int id,
        EntityPlayer player,
        World world,
        int x,
        int y,
        int z
    )
    {
        ImmutableMap<Integer, Direction> connectorIds = ImmutableMap.<Integer, Direction>builder()
            .put(Reference.GUI_CONNECTOR_NORTH, Direction.NORTH)
            .put(Reference.GUI_CONNECTOR_EAST, Direction.EAST)
            .put(Reference.GUI_CONNECTOR_SOUTH, Direction.SOUTH)
            .put(Reference.GUI_CONNECTOR_WEST, Direction.WEST)
            .put(Reference.GUI_CONNECTOR_UP, Direction.UP)
            .put(Reference.GUI_CONNECTOR_DOWN, Direction.DOWN)
            .build();

        Direction direction = connectorIds.get(id);

        if (direction != null) {
            return new GuiConnector(
                player.inventory,
                (ForgeTileEntityConnector) world.getTileEntity(new BlockPos(x, y, z)),
                direction
            );
        }
        return null;
    }
}