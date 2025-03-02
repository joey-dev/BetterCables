package com.emorn.bettercables.api.v1_12_2.gui;

import com.emorn.bettercables.api.v1_12_2.blocks.connector.Data;
import com.emorn.bettercables.api.v1_12_2.blocks.connector.ForgeTileEntityConnector;
import com.emorn.bettercables.api.v1_12_2.common.PositionInWorld;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Direction;
import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
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
    private IPositionInWorld inventoryPosition;

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
        this.inventoryPosition = new PositionInWorld(pos.getX(), pos.getY(), pos.getZ()).offset(direction);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        if (tag != null) {
            this.settingsNBT = new Data(tag);
        }

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
        this.inventoryPosition = new PositionInWorld(pos.getX(), pos.getY(), pos.getZ()).offset(direction);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        Data settings = (Data) settingsNBT;
        ByteBufUtils.writeTag(buf, settings.tag());

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
                    ForgeTileEntityConnector connector = (ForgeTileEntityConnector) tileEntity;
                    ConnectorSettings settings = connector.settings(message.direction);

                    settings.deserializeNBT(message.settingsNBT, "");

                    ConnectorNetwork network = connector.getNetwork();
                    if (message.didExtractChange) {
                        if (settings.isExtractEnabled()) {
                            network.addExtract(message.inventoryPosition, settings);
                        } else {
                            network.removeExtract(settings);
                        }
                    }

                    if (message.didInsertChange) {
                        if (settings.isInsertEnabled()) {
                            network.addInsert(message.inventoryPosition, settings);
                        } else {
                            network.removeInsert(settings);
                        }
                    }

                    //connector.markDirty();
                }
            });
            return null;
        }
    }
}

