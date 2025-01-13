package com.emorn.bettercables.objects.blocks.connector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityConnector extends TileEntity implements ITickable
{
    private String customName;
    private boolean isInsertEnabled;
    private boolean isExtractEnabled;

    public TileEntityConnector()
    {
        this.isInsertEnabled = false;
        this.isExtractEnabled = false;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.isInsertEnabled = compound.getBoolean("isInsertEnabled");
        this.isExtractEnabled = compound.getBoolean("isExtractEnabled");

        if (compound.hasKey("CustomName", 8)) {
            this.setCustomName(compound.getString("CustomName"));
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
        compound.setBoolean("isInsertEnabled", this.isInsertEnabled);
        compound.setBoolean("isExtractEnabled", this.isExtractEnabled);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName()
            ? new TextComponentString(this.customName)
            : new TextComponentTranslation("container.connector");
    }

    //    @Override
    //    public boolean hasCapability(
    //        Capability<?> capability,
    //        EnumFacing facing
    //    )
    //    {
    //        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    //    }
    //
    //    @Override
    //    public <T> T getCapability(
    //        Capability<T> capability,
    //        EnumFacing facing
    //    )
    //    {
    //        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
    //            return (T) this.handler;
    //        }
    //        return super.getCapability(capability, facing);
    //    }

    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void update()
    {
        // TODO later
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return
            this.world.getTileEntity(this.pos) == this
                && player.getDistanceSq(
                (double) this.pos.getX() + 0.5D,
                (double) this.pos.getY() + 0.5D,
                (double) this.pos.getZ() + 0.5D
            ) <= 64.0D;
    }

    public boolean isInsertEnabled()
    {
        return isInsertEnabled;
    }

    public void setInsertEnabled(boolean checked)
    {
        this.isInsertEnabled = checked;
        this.markDirty();
        if (!this.world.isRemote) {
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        }
    }

    public boolean isExtractEnabled()
    {
        return isExtractEnabled;
    }

    public void setExtractEnabled(boolean checked)
    {
        this.isExtractEnabled = checked;
        this.markDirty();
        if (!this.world.isRemote) {
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        }
    }
}
