package com.emorn.bettercables.utils.handlers;

import com.emorn.bettercables.objects.api.forge.blocks.connector.ContainerConnector;
import com.emorn.bettercables.objects.api.forge.common.Direction;
import com.emorn.bettercables.objects.api.forge.blocks.connector.GuiConnector;
import com.emorn.bettercables.objects.api.forge.blocks.connector.TileEntityConnector;
import com.emorn.bettercables.utils.Reference;
import com.google.common.collect.ImmutableMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiHandler implements IGuiHandler
{
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
            if (!(tileEntity instanceof TileEntityConnector)) {
                return null;
            }
            return new ContainerConnector(
                player.inventory,
                (TileEntityConnector) tileEntity
            );
        }
        return null;
    }

    @Override
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
                (TileEntityConnector) world.getTileEntity(new BlockPos(x, y, z)),
                direction
            );
        }
        return null;
    }
}