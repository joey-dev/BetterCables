package com.emorn.bettercables.objects.blocks.connector;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TileEntityConnector extends TileEntity implements ITickable
{
    private String customName;
    private ConnectorSettings northConnectorSettings = new ConnectorSettings(false, false);
    private ConnectorSettings eastConnectorSettings = new ConnectorSettings(false, false);
    private ConnectorSettings southConnectorSettings = new ConnectorSettings(false, false);
    private ConnectorSettings westConnectorSettings = new ConnectorSettings(false, false);
    private ConnectorSettings upConnectorSettings = new ConnectorSettings(false, false);
    private ConnectorSettings downConnectorSettings = new ConnectorSettings(false, false);

    @Nullable
    private ConnectorNetwork network = null;

    public void update()
    {
        if (this.network == null) {
            return;
        }

        if (this.network.isRemoved()) {
            this.network = this.network.mergeToNetwork(this.getPos());
        }
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return
            this.world.getTileEntity(this.pos) == this
                && player.getDistanceSq(
                this.pos.getX() + 0.5D,
                this.pos.getY() + 0.5D,
                this.pos.getZ() + 0.5D
            ) <= 64.0D;
    }

    public boolean isInsertEnabled(Direction direction)
    {
        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);

        if (connectorSettings == null) {
            return false;
        }

        return connectorSettings.isInsertEnabled();
    }

    @Nullable
    private ConnectorSettings findConnectorSettingsByDirection(Direction direction)
    {
        switch (direction) {
            case NORTH:
                return this.northConnectorSettings;
            case EAST:
                return this.eastConnectorSettings;
            case SOUTH:
                return this.southConnectorSettings;
            case WEST:
                return this.westConnectorSettings;
            case UP:
                return this.upConnectorSettings;
            case DOWN:
                return this.downConnectorSettings;
            default:
                return null;
        }
    }

    public void setInsertEnabled(
        boolean checked,
        Direction direction
    )
    {
        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);

        if (this.network == null) {
            return;
        }

        if (connectorSettings == null) {
            return;
        }

        boolean isRemote = this.getWorld().isRemote;
        if (checked) {
            connectorSettings.enableInsert();
            if (!isRemote) {
                this.network.addInsertInventoryPosition(this.findPositionByDirection(direction), this.getPos());
            }
        } else {
            connectorSettings.disableInsert();
            if (!isRemote) {
                this.network.removeInsertInventoryPosition(this.findPositionByDirection(direction), this.getPos());
            }
        }
        notifyUpdate();
        notifyUpdate();
    }

    private BlockPos findPositionByDirection(Direction direction)
    {
        if (direction == Direction.NORTH) {
            return this.getPos().north();
        }

        if (direction == Direction.EAST) {
            return this.getPos().east();
        }

        if (direction == Direction.SOUTH) {
            return this.getPos().south();
        }

        if (direction == Direction.WEST) {
            return this.getPos().west();
        }

        if (direction == Direction.UP) {
            return this.getPos().up();
        }

        if (direction == Direction.DOWN) {
            return this.getPos().down();
        }

        throw new IllegalStateException("Unknown direction: " + direction);
    }

    private void notifyUpdate()
    {
        this.markDirty();
        if (!this.world.isRemote) {
            this.world.notifyBlockUpdate(
                this.pos,
                this.world.getBlockState(this.pos),
                this.world.getBlockState(this.pos),
                3
            );
        }
    }

    public boolean isExtractEnabled(Direction direction)
    {
        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);

        if (connectorSettings == null) {
            return false;
        }

        return connectorSettings.isExtractEnabled();
    }

    public void setExtractEnabled(
        boolean checked,
        Direction direction
    )
    {
        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);

        if (connectorSettings == null) {
            return;
        }

        if (checked) {
            connectorSettings.enableExtract();
        } else {
            connectorSettings.disableExtract();
        }
        notifyUpdate();
    }

    public ConnectorNetwork getNetwork()
    {
        if (this.network == null) {
            throw new IllegalStateException("Network is null");
        }
        return this.network;
    }

    public void setNetwork(ConnectorNetwork connectorNetwork)
    {
        this.network = connectorNetwork;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(
        net.minecraft.network.NetworkManager net,
        net.minecraft.network.play.server.SPacketUpdateTileEntity pkt
    )
    {
        this.readFromNBT(pkt.getNbtCompound());
    }


    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 1, this.writeToNBT(new NBTTagCompound()));
    }


    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.northConnectorSettings = this.retrieveConnectorSettingsFromNBT(Direction.NORTH, compound);
        this.eastConnectorSettings = this.retrieveConnectorSettingsFromNBT(Direction.EAST, compound);
        this.southConnectorSettings = this.retrieveConnectorSettingsFromNBT(Direction.SOUTH, compound);
        this.westConnectorSettings = this.retrieveConnectorSettingsFromNBT(Direction.WEST, compound);
        this.upConnectorSettings = this.retrieveConnectorSettingsFromNBT(Direction.UP, compound);
        this.downConnectorSettings = this.retrieveConnectorSettingsFromNBT(Direction.DOWN, compound);
        this.network = this.retrieveNetworkFromNBT(compound);

        if (compound.hasKey("CustomName", 8)) {
            this.setCustomName(compound.getString("CustomName"));
        }
    }

    private ConnectorSettings retrieveConnectorSettingsFromNBT(
        Direction direction,
        NBTTagCompound compound
    )
    {
        return new ConnectorSettings(
            compound.getBoolean(direction + "-isInsertEnabled"),
            compound.getBoolean(direction + "-isExtractEnabled")
        );
    }

    @Nullable
    private ConnectorNetwork retrieveNetworkFromNBT(
        NBTTagCompound compound
    )
    {
        int networkId = compound.getInteger("NetworkId");
        if (networkId == 0) {
            return null;
        }

        ConnectorNetwork network = ConnectorNetwork.create(compound.getInteger("NetworkId"));

        this.addConnectorInformationToNetwork(network);

        return network;
    }

    private void addConnectorInformationToNetwork(ConnectorNetwork network)
    {
        if (this.northConnectorSettings.isInsertEnabled()) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.NORTH), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.NORTH), this.getPos());
        }

        if (this.eastConnectorSettings.isInsertEnabled()) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.EAST), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.EAST), this.getPos());
        }

        if (this.southConnectorSettings.isInsertEnabled()) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.SOUTH), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.SOUTH), this.getPos());
        }

        if (this.westConnectorSettings.isInsertEnabled()) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.WEST), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.WEST), this.getPos());
        }

        if (this.upConnectorSettings.isInsertEnabled()) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.UP), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.UP), this.getPos());
        }

        if (this.downConnectorSettings.isInsertEnabled()) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.DOWN), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.DOWN), this.getPos());
        }
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        this.storeConnectorSettingsFromNBT(this.northConnectorSettings, Direction.NORTH, compound);
        this.storeConnectorSettingsFromNBT(this.eastConnectorSettings, Direction.EAST, compound);
        this.storeConnectorSettingsFromNBT(this.southConnectorSettings, Direction.SOUTH, compound);
        this.storeConnectorSettingsFromNBT(this.westConnectorSettings, Direction.WEST, compound);
        this.storeConnectorSettingsFromNBT(this.upConnectorSettings, Direction.UP, compound);
        this.storeConnectorSettingsFromNBT(this.downConnectorSettings, Direction.DOWN, compound);
        this.storeNetworkFromNBT(compound);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    private void storeConnectorSettingsFromNBT(
        ConnectorSettings settings,
        Direction direction,
        NBTTagCompound compound
    )
    {
        compound.setBoolean(direction + "-isInsertEnabled", settings.isInsertEnabled());
        compound.setBoolean(direction + "-isExtractEnabled", settings.isExtractEnabled());
    }

    private void storeNetworkFromNBT(
        NBTTagCompound compound
    )
    {
        if (this.network == null) {
            compound.setInteger("NetworkId", 0);
        } else {
            compound.setInteger("NetworkId", this.network.id());
        }
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName()
            ? new TextComponentString(this.customName)
            : new TextComponentTranslation("container.connector");
    }

    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }


}
