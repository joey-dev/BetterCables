package com.emorn.bettercables.objects.blocks.connector;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
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

    public void update()
    {
        // TODO later
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

        if (connectorSettings == null) {
            return;
        }

        if (checked) {
            connectorSettings.enableInsert();
        } else {
            connectorSettings.disableInsert();
        }
        notifyUpdate();
        notifyUpdate();
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
    }    @Override
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
