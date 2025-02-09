package com.emorn.bettercables.objects.api.forge.blocks.connector;

import com.emorn.bettercables.api.v1_12_2.blocks.connector.Data;
import com.emorn.bettercables.api.v1_12_2.blocks.connector.ForgeTileEntityConnector;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Direction;
import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
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
    private Direction direction;
    private IData settingsNBT;
    private boolean didInsertChange;
    private boolean didExtractChange;

    public PacketUpdateConnector()
    {
    }

    public PacketUpdateConnector(
        BlockPos pos,
        Direction direction,
        ConnectorSettings settings,
        boolean didInsertChange,
        boolean didExtractChange
    )
    {
        this.pos = pos;
        this.settingsNBT = new Data(new NBTTagCompound());
        settings.serializeNBT(this.settingsNBT, "");
        this.direction = direction;
        this.didInsertChange = didInsertChange;
        this.didExtractChange = didExtractChange;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        // todo fix
        //this.settingsNBT = ByteBufUtils.readTag(buf);

        ImmutableMap<Character, Direction> directions = ImmutableMap.<Character, Direction>builder()
            .put(Direction.NORTH.name().charAt(0), Direction.NORTH)
            .put(Direction.EAST.name().charAt(0), Direction.EAST)
            .put(Direction.SOUTH.name().charAt(0), Direction.SOUTH)
            .put(Direction.WEST.name().charAt(0), Direction.WEST)
            .put(Direction.UP.name().charAt(0), Direction.UP)
            .put(Direction.DOWN.name().charAt(0), Direction.DOWN)
            .build();

        this.direction = directions.getOrDefault(buf.readChar(), Direction.NORTH);

        this.didInsertChange = buf.readBoolean();
        this.didExtractChange = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        // todo fix
        //ByteBufUtils.writeTag(buf, settingsNBT);

        buf.writeChar(direction.name().charAt(0));

        buf.writeBoolean(didInsertChange);
        buf.writeBoolean(didExtractChange);
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
                if (tileEntity instanceof ForgeTileEntityConnector) {
//                    ForgeTileEntityConnector connector = (ForgeTileEntityConnector) tileEntity;
//                    ConnectorSettings settings = connector.settings(message.direction);
//                    if (settings == null) {
//                        Logger.error("Could not find settings for direction " + message.direction);
//                        return;
//                    }
//                    settings.deserializeNBT(message.settingsNBT);
//                    if (message.didInsertChange) {
//                        ((ForgeTileEntityConnector) tileEntity).setInsertEnabled(settings.isInsertEnabled(), message.direction);
//                    }
//                    if (message.didExtractChange) {
//                        ((ForgeTileEntityConnector) tileEntity).setExtractEnabled(settings.isInsertEnabled(), message.direction);
//                    }
//                    connector.markDirty();
                }
            });
            return null;
        }
    }
}

