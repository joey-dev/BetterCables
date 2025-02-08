package com.emorn.bettercables.api.v1_12_2.blocks.connector;

import com.emorn.bettercables.api.v1_12_2.common.PositionInWorld;
import com.emorn.bettercables.core.blocks.connector.ConnectorUpdateHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ForgeTileEntityConnector extends TileEntity implements ITickable
{
    ConnectorUpdateHandler connectorUpdateHandler;

    public ForgeTileEntityConnector()
    {
        super();

        BlockPos position = this.getPos();
        World worldIn = this.getWorld();

        connectorUpdateHandler = new ConnectorUpdateHandler(
            new PositionInWorld(position.getX(), position.getY(), position.getZ()),
            new com.emorn.bettercables.api.v1_12_2.common.World(worldIn)
        );
    }

    public void update()
    {
        connectorUpdateHandler.invoke(
            !this.getWorld().isRemote
        );
    }

//    private void tick()
//    {
//        north.tick();
//        east.tick();
//        south.tick();
//        west.tick();
//        up.tick();
//        down.tick();
//    }
//
//    public void exportItem(Direction direction) // todo might want to extract this to another class?
//    {
//        if (this.network == null || this.network.isDisabled()) {
//            return;
//        }
//
//        this.directionToIndexMap.putIfAbsent(direction, -1);
//
//        int currentIndex = this.directionToIndexMap.get(direction);
//        Integer nextIndex = this.network.findNextIndex(currentIndex);
//        if (nextIndex == null) {
//            return;
//        }
//
//        this.directionToIndexMap.put(direction, nextIndex);
//
//        BlockPos inventoryPosition = this.network.findInventoryPositionBy(nextIndex);
//
//        if (inventoryPosition == null) {
//            Logger.error("No inventory found at: " + direction);
//            return;
//        }
//
//        ConnectorSettings inventorySettings = this.network.findInsertSettingsBy(nextIndex);
//
//        if (inventorySettings == null) {
//            Logger.error("No settings found at: " + direction);
//            return;
//        }
//
//        BlockPos exportInventoryPosition = this.findPositionByDirection(direction);
//
//        IInventory exportInventory = this.findInventoryEntityByPosition(
//            this.world,
//            exportInventoryPosition
//        );
//        if (exportInventory == null) {
//            Logger.error("Failed to get inventory inventory for export.");
//            return;
//        }
//
//        IInventory importInventory = this.findInventoryEntityByPosition(this.world, inventoryPosition);
//        if (importInventory == null) {
//            Logger.error("Failed to get inventory inventory for import.");
//            return;
//        }
//
//        ConnectorSide connectorSide = this.findConnectorByDirection(direction);
//        if (connectorSide == null) {
//            return;
//        }
//
//        if (this.network == null || this.network.isDisabled()) {
//            return;
//        }
//
//        ConnectorSettings exportConnectorSettings = settings(direction);
//
//        if (exportConnectorSettings == null) {
//            return;
//        }
//
//        List<List<Integer>> possibleIndexes = this.network.getPossibleSlots(
//            connectorSide.connectorSettings,
//            inventorySettings
//        );
//
//        exportItemFromSlots(possibleIndexes, exportInventory, importInventory);
//    }
//
//    private BlockPos findPositionByDirection(Direction direction)
//    {
//        if (direction == Direction.NORTH) {
//            return this.getPos().north();
//        }
//
//        if (direction == Direction.EAST) {
//            return this.getPos().east();
//        }
//
//        if (direction == Direction.SOUTH) {
//            return this.getPos().south();
//        }
//
//        if (direction == Direction.WEST) {
//            return this.getPos().west();
//        }
//
//        if (direction == Direction.UP) {
//            return this.getPos().up();
//        }
//
//        if (direction == Direction.DOWN) {
//            return this.getPos().down();
//        }
//
//        throw new IllegalStateException("Unknown direction: " + direction);
//    }
//
//    @Nullable
//    private IInventory findInventoryEntityByPosition(
//        World world,
//        BlockPos inventoryPosition
//    )
//    {
//        TileEntity tileEntity = world.getTileEntity(inventoryPosition);
//        if (!(tileEntity instanceof IInventory)) {
//            Logger.error("No inventory found at: " + inventoryPosition);
//            return null;
//        }
//
//        return (IInventory) tileEntity;
//    }
//
//    @Nullable
//    private ConnectorSide findConnectorByDirection(Direction direction)
//    {
//        switch (direction) {
//            case NORTH:
//                return this.north;
//            case EAST:
//                return this.east;
//            case SOUTH:
//                return this.south;
//            case WEST:
//                return this.west;
//            case UP:
//                return this.up;
//            case DOWN:
//                return this.down;
//            default:
//                return null;
//        }
//    }
//
//    @Nullable
//    public ConnectorSettings settings(Direction direction)
//    {
//        return this.findConnectorSettingsByDirection(direction);
//    }
//
//    private void exportItemFromSlots(
//        List<List<Integer>> possibleIndexes,
//        IInventory exportInventory,
//        IInventory importInventory
//    )
//    {
//        // todo maybe a queue
//        Map<Integer, Boolean> cannotExtractPositions = new HashMap<>();
//
//        for (List<Integer> possibleIndex : possibleIndexes) {
//            if (Boolean.TRUE.equals(cannotExtractPositions.get(possibleIndex.get(1)))) {
//                continue;
//            }
//
//            ItemStack items = this.extractItemFromInventory(exportInventory, possibleIndex.get(1), 1);
//            if (items.isEmpty()) {
//                cannotExtractPositions.put(possibleIndex.get(1), true);
//                continue;
//            }
//
//            ItemStack itemsNotInserted = this.insertItemIntoInventory(importInventory, possibleIndex.get(0), items);
//            if (itemsNotInserted.isEmpty()) {
//                return;
//            }
//
//            this.insertItemIntoInventory(exportInventory, possibleIndex.get(1), itemsNotInserted);
//
//            if (itemsNotInserted.getCount() != items.getCount()) {
//                return;
//            }
//        }
//    }
//
//    @Nullable
//    private ConnectorSettings findConnectorSettingsByDirection(Direction direction)
//    {
//        ConnectorSide connectorSide = this.findConnectorByDirection(direction);
//        if (connectorSide == null) {
//            return null;
//        }
//
//        return connectorSide.connectorSettings;
//    }
//
//    private ItemStack extractItemFromInventory(
//        IInventory inventory,
//        Integer slot,
//        int amount
//    )
//    {
//        TileEntity inventoryEntity = (TileEntity) inventory;
//        IItemHandler exportInventory = inventoryEntity.getCapability(
//            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
//            null
//        );
//
//        if (exportInventory == null) {
//            Logger.error("Failed to get inventory inventory.");
//            return ItemStack.EMPTY;
//        }
//
//        ItemStack extracted;
//        try {
//            extracted = exportInventory.extractItem(slot, amount, false);
//        } catch (ArrayIndexOutOfBoundsException e) {
//            Logger.error("Failed to extract item from inventory.");
//            return ItemStack.EMPTY;
//        }
//
//        if (!extracted.isEmpty()) {
//            inventory.markDirty();
//        }
//
//        return extracted;
//    }
//
//    private ItemStack insertItemIntoInventory(
//        IInventory inventory,
//        Integer slot,
//        ItemStack items
//    )
//    {
//        TileEntity inventoryEntity = (TileEntity) inventory;
//        IItemHandler insertInventory = inventoryEntity.getCapability(
//            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
//            null
//        );
//
//        if (insertInventory == null) {
//            Logger.error("Failed to get inventory.");
//            return ItemStack.EMPTY;
//        }
//
//        ItemStack itemsLeft;
//
//        try {
//            itemsLeft = insertInventory.insertItem(slot, items, false);
//            /**
//             * todo does not auto update when
//             * there was a large chest with full slots
//             * large chest is changed to small chest
//             */
//        } catch (ArrayIndexOutOfBoundsException e) {
//            Logger.error("Failed to insert item from inventory.");
//            return items;
//        }
//
//        if (itemsLeft.getCount() != items.getCount()) {
//            inventory.markDirty();
//        }
//
//        return itemsLeft;
//    }
//
//    public boolean isExtractEnabled(Direction direction)
//    {
//        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);
//
//        if (connectorSettings == null) {
//            return false;
//        }
//
//        return connectorSettings.isExtractEnabled();
//    }
//
//    public boolean isUsableByPlayer(EntityPlayer player)
//    {
//        return
//            this.world.getTileEntity(this.pos) == this
//                && player.getDistanceSq(
//                this.pos.getX() + 0.5D,
//                this.pos.getY() + 0.5D,
//                this.pos.getZ() + 0.5D
//            ) <= 64.0D;
//    }
//
//    public void setInsertEnabled(
//        boolean checked,
//        Direction direction
//    )
//    {
//        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);
//
//        if (this.network == null || this.network.isDisabled()) {
//            return;
//        }
//
//        if (connectorSettings == null) {
//            return;
//        }
//
//        connectorSettings.changeInsertEnabled(checked);
//
//        boolean isRemote = this.getWorld().isRemote;
//        if (isRemote) {
//            return;
//        }
//
//        if (checked) {
//            this.network.addInsert(
//                this.findPositionByDirection(direction),
//                this.settings(direction)
//            );
//        } else {
//            this.network.removeInsert(this.settings(direction));
//        }
//
//        notifyUpdate();
//    }
//
//    private void notifyUpdate()
//    {
//        this.markDirty();
//        if (!this.world.isRemote) {
//            this.world.notifyBlockUpdate(
//                this.pos,
//                this.world.getBlockState(this.pos),
//                this.world.getBlockState(this.pos),
//                3
//            );
//        }
//    }
//
//    public boolean isInsertEnabled(Direction direction)
//    {
//        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);
//
//        if (connectorSettings == null) {
//            return false;
//        }
//
//        return connectorSettings.isInsertEnabled();
//    }
//
//    public void setExtractEnabled(
//        boolean checked,
//        Direction direction
//    )
//    {
//        ConnectorSettings connectorSettings = this.findConnectorSettingsByDirection(direction);
//
//        if (this.network == null || this.network.isDisabled()) {
//            return;
//        }
//
//        if (connectorSettings == null) {
//            return;
//        }
//
//        connectorSettings.changeExtractEnabled(checked);
//
//        boolean isRemote = this.getWorld().isRemote;
//        if (isRemote) {
//            return;
//        }
//
//        if (checked) {
//            this.network.addExtract(
//                this.findPositionByDirection(direction),
//                this.settings(direction)
//            );
//        } else {
//            this.network.removeExtract(this.settings(direction));
//        }
//
//        notifyUpdate();
//    }
//
//    public ConnectorNetwork getNetwork()
//    {
//        if (this.network == null) {
//            throw new IllegalStateException("Network is null");
//        }
//        return this.network;
//    }
//
//    public void setNetwork(ConnectorNetwork connectorNetwork)
//    {
//        this.network = connectorNetwork;
//    }
//
//    @Override
//    public void readFromNBT(NBTTagCompound compound)
//    {
//        super.readFromNBT(compound);
//
//        Settings.load(this.north.connectorSettings, compound, "north");
//        Settings.load(this.east.connectorSettings, compound, "east");
//        Settings.load(this.south.connectorSettings, compound, "south");
//        Settings.load(this.west.connectorSettings, compound, "west");
//        Settings.load(this.up.connectorSettings, compound, "up");
//        Settings.load(this.down.connectorSettings, compound, "down");
//
//        this.network = this.retrieveNetworkFromNBT(compound);
//
//        if (compound.hasKey(CUSTOM_NAME, 8)) {
//            this.setCustomName(compound.getString(CUSTOM_NAME));
//        }
//    }
//
//    @Override
//    public NBTTagCompound writeToNBT(NBTTagCompound compound)
//    {
//        super.writeToNBT(compound);
//
//        Settings.save(this.north.connectorSettings, compound, "north");
//        Settings.save(this.east.connectorSettings, compound, "east");
//        Settings.save(this.south.connectorSettings, compound, "south");
//        Settings.save(this.west.connectorSettings, compound, "west");
//        Settings.save(this.up.connectorSettings, compound, "up");
//        Settings.save(this.down.connectorSettings, compound, "down");
//
//        this.storeNetworkFromNBT(compound);
//
//        if (this.hasCustomName()) {
//            compound.setString(CUSTOM_NAME, this.customName);
//        }
//
//        return compound;
//    }
//
//    @Override
//    public SPacketUpdateTileEntity getUpdatePacket()
//    {
//        return new SPacketUpdateTileEntity(this.pos, 1, this.writeToNBT(new NBTTagCompound()));
//    }
//
//    @Override
//    public NBTTagCompound getUpdateTag()
//    {
//        return this.writeToNBT(new NBTTagCompound());
//    }
//
//    private void storeNetworkFromNBT(
//        NBTTagCompound compound
//    )
//    {
//        if (this.network == null) {
//            compound.setInteger(NETWORK_ID, 0);
//        } else {
//            compound.setInteger(NETWORK_ID, this.network.id());
//        }
//    }
//
//    public boolean hasCustomName()
//    {
//        return this.customName != null && !this.customName.isEmpty();
//    }
//
//    @Override
//    public ITextComponent getDisplayName()
//    {
//        return this.hasCustomName()
//            ? new TextComponentString(this.customName)
//            : new TextComponentTranslation("container.connector");
//    }
//
//    @Override
//    public void onDataPacket(
//        net.minecraft.network.NetworkManager net,
//        net.minecraft.network.play.server.SPacketUpdateTileEntity pkt
//    )
//    {
//        this.readFromNBT(pkt.getNbtCompound());
//    }
//
//    @Nullable
//    private ConnectorNetwork retrieveNetworkFromNBT(
//        NBTTagCompound compound
//    )
//    {
//        int networkId = compound.getInteger(NETWORK_ID);
//        if (networkId == 0) {
//            return null;
//        }
//
//        ConnectorNetwork foundNetwork = ConnectorNetwork.create(compound.getInteger(NETWORK_ID));
//
//        this.addInsertConnectorInformationToNetwork(foundNetwork);
//        this.addExtractConnectorInformationToNetwork(foundNetwork);
//
//        return foundNetwork;
//    }
//
//    private void addInsertConnectorInformationToNetwork(ConnectorNetwork network)
//    {
//        if (this.isInsertEnabled(Direction.NORTH)) {
//            network.addInsert(this.getPos().offset(EnumFacing.NORTH), this.settings(Direction.NORTH));
//        } else {
//            network.removeInsert(this.settings(Direction.NORTH));
//        }
//
//        if (this.isInsertEnabled(Direction.EAST)) {
//            network.addInsert(this.getPos().offset(EnumFacing.EAST), this.settings(Direction.EAST));
//        } else {
//            network.removeInsert(this.settings(Direction.EAST));
//        }
//
//        if (this.isInsertEnabled(Direction.SOUTH)) {
//            network.addInsert(this.getPos().offset(EnumFacing.SOUTH), this.settings(Direction.SOUTH));
//        } else {
//            network.removeInsert(this.settings(Direction.SOUTH));
//        }
//
//        if (this.isInsertEnabled(Direction.WEST)) {
//            network.addInsert(this.getPos().offset(EnumFacing.WEST), this.settings(Direction.WEST));
//        } else {
//            network.removeInsert(this.settings(Direction.WEST));
//        }
//
//        if (this.isInsertEnabled(Direction.UP)) {
//            network.addInsert(this.getPos().offset(EnumFacing.UP), this.settings(Direction.UP));
//        } else {
//            network.removeInsert(this.settings(Direction.UP));
//        }
//
//        if (this.isInsertEnabled(Direction.DOWN)) {
//            network.addInsert(this.getPos().offset(EnumFacing.DOWN), this.settings(Direction.DOWN));
//        } else {
//            network.removeInsert(this.settings(Direction.DOWN));
//        }
//    }
//
//    private void addExtractConnectorInformationToNetwork(ConnectorNetwork network)
//    {
//        if (this.isExtractEnabled(Direction.NORTH)) {
//            network.addExtract(this.getPos().offset(EnumFacing.NORTH), this.settings(Direction.NORTH));
//        } else {
//            network.removeExtract(this.settings(Direction.NORTH));
//        }
//
//        if (this.isExtractEnabled(Direction.EAST)) {
//            network.addExtract(this.getPos().offset(EnumFacing.EAST), this.settings(Direction.EAST));
//        } else {
//            network.removeExtract(this.settings(Direction.EAST));
//        }
//
//        if (this.isExtractEnabled(Direction.SOUTH)) {
//            network.addExtract(this.getPos().offset(EnumFacing.SOUTH), this.settings(Direction.SOUTH));
//        } else {
//            network.removeExtract(this.settings(Direction.SOUTH));
//        }
//
//        if (this.isExtractEnabled(Direction.WEST)) {
//            network.addExtract(this.getPos().offset(EnumFacing.WEST), this.settings(Direction.WEST));
//        } else {
//            network.removeExtract(this.settings(Direction.WEST));
//        }
//
//        if (this.isExtractEnabled(Direction.UP)) {
//            network.addExtract(this.getPos().offset(EnumFacing.UP), this.settings(Direction.UP));
//        } else {
//            network.removeExtract(this.settings(Direction.UP));
//        }
//
//        if (this.isExtractEnabled(Direction.DOWN)) {
//            network.addExtract(this.getPos().offset(EnumFacing.DOWN), this.settings(Direction.DOWN));
//        } else {
//            network.removeExtract(this.settings(Direction.DOWN));
//        }
//    }
//
//    public void setCustomName(String customName)
//    {
//        this.customName = customName;
//    }
}
