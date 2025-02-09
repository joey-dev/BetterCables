package com.emorn.bettercables.api.v1_12_2.blocks.connector;

import com.emorn.bettercables.api.v1_12_2.common.PositionInWorld;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.blocks.connector.ITileEntityConnector;
import com.emorn.bettercables.core.blocks.connector.ConnectorNetworkHandler;
import com.emorn.bettercables.core.blocks.connector.itemTransfer.ConnectorUpdateHandler;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSavedDataHandler;
import com.emorn.bettercables.core.common.Logger;
import com.emorn.bettercables.objects.api.forge.blocks.connector.ConnectorNetwork;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ForgeTileEntityConnector extends TileEntity implements ITickable, ITileEntityConnector
{
    private static final String CUSTOM_NAME = "custom name";
    private final ConnectorUpdateHandler connectorUpdateHandler;
    private final ConnectorNetworkHandler connectorNetworkHandler;
    private final ConnectorSavedDataHandler connectorSavedDataHandler;

    @Nullable
    private String customName;

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

        connectorSavedDataHandler = new ConnectorSavedDataHandler(
            connectorUpdateHandler.getConnectorSides(),
            positionInWorld,
            connectorNetworkHandler
        );
    }

    @Override
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

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        this.connectorSavedDataHandler.readFromNBT(
            new Data(compound)
        );

        if (compound.hasKey(CUSTOM_NAME, Constants.NBT.TAG_STRING)) {
            this.setCustomName(compound.getString(CUSTOM_NAME));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        IData data = this.connectorSavedDataHandler.writeToNBT(
            new Data(compound)
        );

        if (this.hasCustomName()) {
            compound.setString(CUSTOM_NAME, this.customName);
        }

        if (data instanceof Data) {
            return ((Data) data).tag();
        }

        Logger.error("Failed to write connector data to NBT");
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(
            this.getPos(),
            1,
            this.writeToNBT(new NBTTagCompound())
        );
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName()
            ? new TextComponentString(this.customName)
            : new TextComponentTranslation("container.connector");
    }

    @Override
    public void onDataPacket(
        net.minecraft.network.NetworkManager net,
        SPacketUpdateTileEntity pkt
    )
    {
        this.readFromNBT(pkt.getNbtCompound());
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }
}
