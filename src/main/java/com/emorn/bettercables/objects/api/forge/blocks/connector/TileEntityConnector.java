package com.emorn.bettercables.objects.api.forge.blocks.connector;

import com.emorn.bettercables.objects.api.forge.common.Direction;
import com.emorn.bettercables.objects.api.forge.common.Logger;
import com.emorn.bettercables.objects.api.forge.common.Settings;
import com.emorn.bettercables.objects.application.blocks.connector.ConnectorSide;
import com.emorn.bettercables.objects.gateway.blocks.ConnectorSettings;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TileEntityConnector extends TileEntity implements ITickable
{
    public static final String NETWORK_ID = "NetworkId";
    public static final String CUSTOM_NAME = "CustomName";
    private final Map<Direction, Integer> directionToIndexMap = new HashMap<>();
    private final ConnectorSide north = new ConnectorSide();
    private final ConnectorSide east = new ConnectorSide();
    private final ConnectorSide south = new ConnectorSide();
    private final ConnectorSide west = new ConnectorSide();
    private final ConnectorSide up = new ConnectorSide();
    private final ConnectorSide down = new ConnectorSide();
    private String customName;
    @Nullable
    private ConnectorNetwork network = null;

    public void update()
    {
        // isRemote means isClient
        if (this.getWorld().isRemote) {
            return;
        }

        if (this.network == null || this.network.isDisabled()) {
            return;
        }

        if (this.network.isRemoved()) {
            this.network = this.network.mergeToNetwork(this.getPos());
        }

        this.tick();

        if (north.canExport()) {
            this.exportItem(Direction.NORTH);
        }

        if (east.canExport()) {
            this.exportItem(Direction.EAST);
        }

        if (south.canExport()) {
            this.exportItem(Direction.SOUTH);
        }

        if (west.canExport()) {
            this.exportItem(Direction.WEST);
        }

        if (up.canExport()) {
            this.exportItem(Direction.UP);
        }

        if (down.canExport()) {
            this.exportItem(Direction.DOWN);
        }
    }

    private void tick()
    {
        north.tick();
        east.tick();
        south.tick();
        west.tick();
        up.tick();
        down.tick();
    }

    public void exportItem(Direction direction) // todo might want to extract this to another class?
    {
        if (this.network == null || this.network.isDisabled()) {
            return;
        }

        this.directionToIndexMap.putIfAbsent(direction, -1);

        int currentIndex = this.directionToIndexMap.get(direction);
        Integer nextIndex = this.network.findNextIndex(currentIndex);
        if (nextIndex == null) {
            return;
        }

        this.directionToIndexMap.put(direction, nextIndex);

        BlockPos inventoryPosition = this.network.findInventoryPositionBy(nextIndex);

        if (inventoryPosition == null) {
            Logger.error("No chest found at: " + direction);
            return;
        }

        TileEntityChest exportInventory = this.findInventoryEntityByPosition(
            this.world,
            this.findPositionByDirection(direction)
        );
        if (exportInventory == null) {
            Logger.error("Failed to get chest inventory for export.");
            return;
        }

        TileEntityChest importInventory = this.findInventoryEntityByPosition(this.world, inventoryPosition);
        if (importInventory == null) {
            Logger.error("Failed to get chest inventory for import.");
            return;
        }

        ConnectorSide connectorSide = this.findConnectorByDirection(direction);
        if (connectorSide == null) {
            return;
        }

        if (this.network == null || this.network.isDisabled()) {
            return;
        }

        List<List<Integer>> possibleIndexes = this.network.getPossibleSlots(connectorSide);

        exportItemFromSlots(possibleIndexes, exportInventory, importInventory);
    }

    @Nullable
    private TileEntityChest findInventoryEntityByPosition(
        World world,
        BlockPos chestPos
    )
    {
        TileEntity tileEntity = world.getTileEntity(chestPos);
        if (!(tileEntity instanceof TileEntityChest)) {
            Logger.error("No chest found at: " + chestPos);
            return null;
        }

        return (TileEntityChest) tileEntity;
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

    @Nullable
    private ConnectorSide findConnectorByDirection(Direction direction)
    {
        switch (direction) {
            case NORTH:
                return this.north;
            case EAST:
                return this.east;
            case SOUTH:
                return this.south;
            case WEST:
                return this.west;
            case UP:
                return this.up;
            case DOWN:
                return this.down;
            default:
                return null;
        }
    }

    private void exportItemFromSlots(
        List<List<Integer>> possibleIndexes,
        TileEntityChest exportInventory,
        TileEntityChest importInventory
    )
    {
        for (List<Integer> possibleIndex : possibleIndexes) {
            ItemStack items = this.extractItemFromChest(exportInventory, possibleIndex.get(0), 1);
            if (items.isEmpty()) {
                continue;
            }

            ItemStack itemsNotInserted = this.insertItemIntoChest(importInventory, possibleIndex.get(1), items);
            if (itemsNotInserted.isEmpty()) {
                return;
            }

            this.insertItemIntoChest(exportInventory, possibleIndex.get(0), itemsNotInserted);

            if (itemsNotInserted.getCount() != items.getCount()) {
                return;
            }
        }
    }

    private ItemStack extractItemFromChest(
        TileEntityChest chest,
        Integer slot,
        int amount
    )
    {
        IItemHandler exportInventory = chest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if (exportInventory == null) {
            Logger.error("Failed to get chest inventory.");
            return ItemStack.EMPTY;
        }

        ItemStack extracted = exportInventory.extractItem(slot, amount, false);
        if (!extracted.isEmpty()) {
            chest.markDirty();
        }

        return extracted;
    }

    private ItemStack insertItemIntoChest(
        TileEntityChest chest,
        Integer slot,
        ItemStack items
    )
    {
        IItemHandler inventory = chest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if (inventory == null) {
            Logger.error("Failed to get chest inventory.");
            return ItemStack.EMPTY;
        }

        ItemStack itemsLeft = inventory.insertItem(slot, items, false);
        if (itemsLeft.getCount() != items.getCount()) {
            chest.markDirty();
        }

        return itemsLeft;
    }

    public boolean isExtractEnabled(Direction direction)
    {
        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);

        if (connectorSettings == null) {
            return false;
        }

        return connectorSettings.isExtractEnabled;
    }

    @Nullable
    private ConnectorSettings findConnectorSettingsByDirection(Direction direction)
    {
        ConnectorSide connectorSide = this.findConnectorByDirection(direction);
        if (connectorSide == null) {
            return null;
        }

        return connectorSide.connectorSettings;
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

    public void setInsertEnabled(
        boolean checked,
        Direction direction
    )
    {
        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);

        if (this.network == null || this.network.isDisabled()) {
            return;
        }

        if (connectorSettings == null) {
            return;
        }

        connectorSettings.isInsertEnabled = checked;

        boolean isRemote = this.getWorld().isRemote;
        if (isRemote) {
            return;
        }

        if (checked) {
            this.network.addInsertInventoryPosition(this.findPositionByDirection(direction), this.getPos());
        } else {
            this.network.removeInsertInventoryPosition(this.findPositionByDirection(direction), this.getPos());
        }

        notifyUpdate();
    }    public boolean isInsertEnabled(Direction direction)
    {
        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);

        if (connectorSettings == null) {
            return false;
        }

        return connectorSettings.isInsertEnabled;
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

    public void setExtractEnabled(
        boolean checked,
        Direction direction
    )
    {
        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);

        if (connectorSettings == null) {
            return;
        }

        connectorSettings.isExtractEnabled = checked;

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

        Settings.load(this.north.connectorSettings, compound, "north");
        Settings.load(this.east.connectorSettings, compound, "east");
        Settings.load(this.south.connectorSettings, compound, "south");
        Settings.load(this.west.connectorSettings, compound, "west");
        Settings.load(this.up.connectorSettings, compound, "up");
        Settings.load(this.down.connectorSettings, compound, "down");

        this.network = this.retrieveNetworkFromNBT(compound);

        if (compound.hasKey(CUSTOM_NAME, 8)) {
            this.setCustomName(compound.getString(CUSTOM_NAME));
        }
    }

    @Nullable
    private ConnectorNetwork retrieveNetworkFromNBT(
        NBTTagCompound compound
    )
    {
        int networkId = compound.getInteger(NETWORK_ID);
        if (networkId == 0) {
            return null;
        }

        ConnectorNetwork foundNetwork = ConnectorNetwork.create(compound.getInteger(NETWORK_ID));

        this.addConnectorInformationToNetwork(foundNetwork);

        return foundNetwork;
    }

    private void addConnectorInformationToNetwork(ConnectorNetwork network)
    {
        if (this.isInsertEnabled(Direction.NORTH)) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.NORTH), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.NORTH), this.getPos());
        }

        if (this.isInsertEnabled(Direction.EAST)) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.EAST), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.EAST), this.getPos());
        }

        if (this.isInsertEnabled(Direction.SOUTH)) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.SOUTH), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.SOUTH), this.getPos());
        }

        if (this.isInsertEnabled(Direction.WEST)) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.WEST), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.WEST), this.getPos());
        }

        if (this.isInsertEnabled(Direction.UP)) {
            network.addInsertInventoryPosition(this.getPos().offset(EnumFacing.UP), this.getPos());
        } else {
            network.removeInsertInventoryPosition(this.getPos().offset(EnumFacing.UP), this.getPos());
        }

        if (this.isInsertEnabled(Direction.DOWN)) {
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

        Settings.save(this.north.connectorSettings, compound, "north");
        Settings.save(this.east.connectorSettings, compound, "east");
        Settings.save(this.south.connectorSettings, compound, "south");
        Settings.save(this.west.connectorSettings, compound, "west");
        Settings.save(this.up.connectorSettings, compound, "up");
        Settings.save(this.down.connectorSettings, compound, "down");

        this.storeNetworkFromNBT(compound);

        if (this.hasCustomName()) {
            compound.setString(CUSTOM_NAME, this.customName);
        }

        return compound;
    }

    private void storeNetworkFromNBT(
        NBTTagCompound compound
    )
    {
        if (this.network == null) {
            compound.setInteger(NETWORK_ID, 0);
        } else {
            compound.setInteger(NETWORK_ID, this.network.id());
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
