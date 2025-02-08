package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.contract.IPositionInWorld;
import com.emorn.bettercables.contract.ITileEntity;
import com.emorn.bettercables.contract.IWorld;
import net.minecraft.util.math.BlockPos;

public class World implements IWorld
{
    private final net.minecraft.world.World forgeWorld;

    public World(net.minecraft.world.World forgeWorld)
    {
        this.forgeWorld = forgeWorld;
    }

    public ITileEntity getTileEntity(IPositionInWorld position)
    {
        return new TileEntity(
            this.forgeWorld.getTileEntity(
            new BlockPos(
                position.getX(),
                position.getY(),
                position.getZ()
            )
        )
        );
    }
}
