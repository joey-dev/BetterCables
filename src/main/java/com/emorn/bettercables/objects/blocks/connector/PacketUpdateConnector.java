package com.emorn.bettercables.objects.blocks.connector;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateConnector implements IMessage {
    private BlockPos pos;
    private boolean isInsertEnabled;
    private boolean isExtractEnabled;
    private Direction direction;

    public PacketUpdateConnector() {}

    public PacketUpdateConnector(BlockPos pos, boolean isInsertEnabled, boolean isExtractEnabled, Direction direction) {
        this.pos = pos;
        this.isInsertEnabled = isInsertEnabled;
        this.isExtractEnabled = isExtractEnabled;
        this.direction = direction;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.isInsertEnabled = buf.readBoolean();
        this.isExtractEnabled = buf.readBoolean();

        switch (buf.readChar()) {
            case 'N':
                this.direction = Direction.NORTH;
                break;
            case 'E':
                this.direction = Direction.EAST;
                break;
            case 'S':
                this.direction = Direction.SOUTH;
                break;
            case 'W':
                this.direction = Direction.WEST;
                break;
            case 'U':
                this.direction = Direction.UP;
                break;
            case 'D':
                this.direction = Direction.DOWN;
                break;
            default:
                this.direction = Direction.NORTH;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeBoolean(isInsertEnabled);
        buf.writeBoolean(isExtractEnabled);

        buf.writeChar(direction.name().charAt(0));
    }

    public static class Handler implements IMessageHandler<PacketUpdateConnector, IMessage> {
        @Override
        public IMessage onMessage(PacketUpdateConnector message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                TileEntity tileEntity = ctx.getServerHandler().player.world.getTileEntity(message.pos);
                if (tileEntity instanceof TileEntityConnector) {
                    TileEntityConnector connector = (TileEntityConnector) tileEntity;
                    connector.setInsertEnabled(message.isInsertEnabled, message.direction);
                    connector.setExtractEnabled(message.isExtractEnabled, message.direction);
                    connector.markDirty(); // Mark the TileEntity as dirty to save changes
                }
            });
            return null;
        }
    }
}

