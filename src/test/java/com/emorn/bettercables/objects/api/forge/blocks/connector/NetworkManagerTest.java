package com.emorn.bettercables.objects.api.forge.blocks.connector;

import com.emorn.bettercables.objects.api.forge.blocks.cable.BlockCable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class) // static mocking
@PrepareForTest({NetworkManager.class, Block.class, ConnectorNetwork.class})
@PowerMockIgnore({"javax.script.*", "org.apache.log4j.*", "javax.management.*"})
public class NetworkManagerTest
{
    private World worldMock;

    @Before
    public void setUp()
    {
        worldMock = mock(World.class);
    }

    @Test
    public void connector_and_cable()
    {
        BlockPos start = new BlockPos(10, 10, 10);
        Map<BlockPos, Block> blocks = this.createEmptyBlocksAround(start);
        blocks.putAll(this.createEmptyBlocksAround(new BlockPos(10, 10, 9)));
        blocks.putAll(this.createEmptyBlocksAround(new BlockPos(10, 9, 10)));

        blocks.put(
            new BlockPos(10, 10, 9),
            mock(BlockConnector.class)
        );

        blocks.put(
            new BlockPos(10, 9, 10),
            mock(BlockCable.class)
        );


        this.reCalculateNetworksAround_test(
            start,
            blocks,
            2
        );
    }

    @Test
    public void nothing()
    {
        BlockPos start = new BlockPos(10, 10, 10);
        Map<BlockPos, Block> blocks = this.createEmptyBlocksAround(start);

        this.reCalculateNetworksAround_test(
            start,
            blocks,
            0
        );
    }

    @Test
    public void connectors_all_around()
    {
        BlockPos start = new BlockPos(10, 10, 10);
        Map<BlockPos, Block> blocks = this.createEmptyBlocksAround(start);
        blocks.putAll(this.createEmptyBlocksAround(new BlockPos(10, 10, 9)));
        blocks.putAll(this.createEmptyBlocksAround(new BlockPos(10, 9, 10)));
        blocks.putAll(this.createEmptyBlocksAround(new BlockPos(9, 10, 10)));
        blocks.putAll(this.createEmptyBlocksAround(new BlockPos(10, 10, 11)));
        blocks.putAll(this.createEmptyBlocksAround(new BlockPos(10, 11, 10)));
        blocks.putAll(this.createEmptyBlocksAround(new BlockPos(11, 10, 10)));

        blocks.put(
            new BlockPos(10, 10, 9),
            mock(BlockConnector.class)
        );
        blocks.put(
            new BlockPos(10, 9, 10),
            mock(BlockConnector.class)
        );
        blocks.put(
            new BlockPos(9, 10, 10),
            mock(BlockConnector.class)
        );
        blocks.put(
            new BlockPos(10, 10, 11),
            mock(BlockConnector.class)
        );
        blocks.put(
            new BlockPos(10, 11, 10),
            mock(BlockConnector.class)
        );
        blocks.put(
            new BlockPos(11, 10, 10),
            mock(BlockConnector.class)
        );

        this.reCalculateNetworksAround_test(
            start,
            blocks,
            6
        );
    }

    private Map<BlockPos, Block> createEmptyBlocksAround(
        BlockPos position
    )
    {
        Map<BlockPos, Block> blocksAround = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            int x = position.getX();
            int y = position.getY();
            int z = position.getZ();
            if (i == 0) {
                z = z - 1;
            } else if (i == 1) {
                x = x + 1;
            } else if (i == 2) {
                z = z + 1;
            } else if (i == 3) {
                x = x - 1;
            } else if (i == 4) {
                y = y + 1;
            } else {
                y = y - 1;
            }

            blocksAround.put(
                new BlockPos(x, y, z),
                mock(Block.class)
            );
        }

        return blocksAround;
    }

    private void reCalculateNetworksAround_test(
        BlockPos start,
        Map<BlockPos, Block> blocks,
        int expectedNetworkSize
    )
    {
        for (Map.Entry<BlockPos, Block> block : blocks.entrySet()) {
            IBlockState mockBlockState = mock(IBlockState.class);
            when(mockBlockState.getBlock()).thenReturn(block.getValue());

            PowerMockito.when(worldMock.getBlockState(block.getKey())).thenReturn(mockBlockState);

            PowerMockito.when(worldMock.getBlockState(argThat(pos ->
                pos != null &&
                    pos.getX() == block.getKey().getX() &&
                    pos.getY() == block.getKey().getY() &&
                    pos.getZ() == block.getKey().getZ()
            ))).thenReturn(mockBlockState);
        }

        List<ConnectorNetwork> createdNetworks = new ArrayList<>();

        PowerMockito.mockStatic(ConnectorNetwork.class);

        when(ConnectorNetwork.create(anyInt())).thenCallRealMethod();

        when(ConnectorNetwork.create()).thenAnswer(invocation -> {
            ConnectorNetwork network = ConnectorNetwork.create(createdNetworks.size());

            createdNetworks.add(network);
            return network;
        });

        NetworkManager.reCalculateNetworksAround(start, worldMock);

        assertEquals(expectedNetworkSize, createdNetworks.size());
    }
}