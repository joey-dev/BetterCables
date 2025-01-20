package com.emorn.bettercables.init;

import com.emorn.bettercables.objects.blocks.cable.BlockCable;
import com.emorn.bettercables.objects.blocks.connector.BlockConnector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockInit
{
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final Block CONNECTOR = new BlockConnector("connector");
    public static final Block CABLE = new BlockCable("cable");
    private BlockInit()
    {
        throw new IllegalStateException();
    }
}
