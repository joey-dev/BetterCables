package com.emorn.bettercables.objects.blocks.connector;

import com.emorn.bettercables.objects.api.forge.blocks.connector.TileEntityConnector;
import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PacketUpdateConnector implements IMessage
{
    private BlockPos pos;
    private boolean isInsertEnabled;
    private boolean isExtractEnabled;
    private Direction direction;

    public PacketUpdateConnector()
    {
    }

    public PacketUpdateConnector(
        BlockPos pos,
        boolean isInsertEnabled,
        boolean isExtractEnabled,
        Direction direction
    )
    {
        this.pos = pos;
        this.isInsertEnabled = isInsertEnabled;
        this.isExtractEnabled = isExtractEnabled;
        this.direction = direction;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.isInsertEnabled = buf.readBoolean();
        this.isExtractEnabled = buf.readBoolean();

        ImmutableMap<Character, Direction> directions = ImmutableMap.<Character, Direction>builder()
            .put(Direction.NORTH.name().charAt(0), Direction.NORTH)
            .put(Direction.EAST.name().charAt(0), Direction.EAST)
            .put(Direction.SOUTH.name().charAt(0), Direction.SOUTH)
            .put(Direction.WEST.name().charAt(0), Direction.WEST)
            .put(Direction.UP.name().charAt(0), Direction.UP)
            .put(Direction.DOWN.name().charAt(0), Direction.DOWN)
            .build();

        this.direction = directions.getOrDefault(buf.readChar(), Direction.NORTH);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeBoolean(isInsertEnabled);
        buf.writeBoolean(isExtractEnabled);

        buf.writeChar(direction.name().charAt(0));
    }

    public static class Handler implements IMessageHandler<PacketUpdateConnector, IMessage>
    {
        @Override
        @Nullable
        public IMessage onMessage(
            PacketUpdateConnector message,
            MessageContext ctx
        )
        {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                TileEntity tileEntity = ctx.getServerHandler().player.world.getTileEntity(message.pos);
                if (tileEntity instanceof TileEntityConnector) {
                    TileEntityConnector connector = (TileEntityConnector) tileEntity;
                    connector.setInsertEnabled(message.isInsertEnabled, message.direction);
                    connector.setExtractEnabled(message.isExtractEnabled, message.direction);
                    connector.markDirty();
                }
            });
            return null;
        }
    }
}

