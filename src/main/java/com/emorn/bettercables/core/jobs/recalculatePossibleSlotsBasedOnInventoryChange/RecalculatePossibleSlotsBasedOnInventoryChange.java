package com.emorn.bettercables.core.jobs.recalculatePossibleSlotsBasedOnInventoryChange;

import com.emorn.bettercables.api.v1_12_2.blocks.connector.ForgeTileEntityConnector;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.core.jobs.IJob;
import com.emorn.bettercables.core.jobs.IJobInput;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecalculatePossibleSlotsBasedOnInventoryChange implements IJob
{
    private final Queue<RecalculatePossibleSlotsBasedOnInventoryChangeInput> threadSafeQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void addToQueue(IJobInput input)
    {
        RecalculatePossibleSlotsBasedOnInventoryChangeInput jobInput = (RecalculatePossibleSlotsBasedOnInventoryChangeInput) input;
        threadSafeQueue.add(jobInput);
    }

    @Override
    public void executeEverySecond()
    {
        Map<String, RecalculatePossibleSlotsBasedOnInventoryChangeInput> jobInputs = new HashMap<>();

        RecalculatePossibleSlotsBasedOnInventoryChangeInput jobInput;
        Queue<RecalculatePossibleSlotsBasedOnInventoryChangeInput> jobQueue = new LinkedList<>();

        while ((jobInput = threadSafeQueue.poll()) != null)
        {
            if (jobInputs.containsKey(jobInput.identifier())) {
                continue;
            }
            jobInputs.put(jobInput.identifier(), jobInput);
            jobQueue.add(jobInput);
        }

        while ((jobInput = jobQueue.poll()) != null)
        {
            ForgeTileEntityConnector connector = jobInput.connector();
            IInventory neighborTileEntity = jobInput.neighborTileEntity();
            Direction direction = jobInput.direction();
            IBlockAccess world = jobInput.world();
            BlockPos neighbor = jobInput.neighborPosition();

            ConnectorNetwork network = connector.getNetwork();
            int slotCount = neighborTileEntity.getSizeInventory();

            if (neighborTileEntity instanceof TileEntityChest) {
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    TileEntity chestNeighbor = world.getTileEntity(neighbor.offset(facing));
                    if (chestNeighbor instanceof TileEntityChest) {
                        slotCount += ((TileEntityChest) chestNeighbor).getSizeInventory();
                        break; // Only add once, since there should be one extra chest max
                    }
                }
            }

            network.updateSlotCount(slotCount, connector.settings(direction));
        }
    }
}
