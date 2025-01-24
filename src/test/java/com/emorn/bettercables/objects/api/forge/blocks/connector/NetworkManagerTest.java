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
import java.util.Arrays;
import java.util.List;

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
    private BlockPos posMock;

    private List<Block> neighborBlockTypes;
    private int expectedNetworkSize;

    @Before
    public void setUp()
    {
        worldMock = mock(World.class);
        posMock = new BlockPos(10, 10, 10);
    }

    @Test
    public void oneConnectorOneCable_test()
    {
        this.neighborBlockTypes = Arrays.asList(mock(BlockConnector.class), mock(BlockCable.class));
        this.expectedNetworkSize = 2;
        this.reCalculateNetworksAround_test();
    }

    private void reCalculateNetworksAround_test()
    {
        BlockPos[] neighborPositions = new BlockPos[6];
        for (int i = 0; i < 6; i++) {
            IBlockState mockBlockState = mock(IBlockState.class);
            Block mockBlock;

            if (neighborBlockTypes.size() <= i) {
                mockBlock = mock(Block.class);
            } else {
                mockBlock = neighborBlockTypes.get(i);
            }

            int x = 10;
            int y = 10;
            int z = 10;
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

            neighborPositions[i] = new BlockPos(x, y, z);

            when(mockBlockState.getBlock()).thenReturn(mockBlock);

            PowerMockito.when(worldMock.getBlockState(neighborPositions[i])).thenReturn(mockBlockState);

            final int finalI = i;
            PowerMockito.when(worldMock.getBlockState(argThat(pos ->
                pos != null &&
                    pos.getX() == neighborPositions[finalI].getX() &&
                    pos.getY() == neighborPositions[finalI].getY() &&
                    pos.getZ() == neighborPositions[finalI].getZ()
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

        NetworkManager.reCalculateNetworksAround(posMock, worldMock);

        if (expectedNetworkSize > 0) {
            assertEquals(expectedNetworkSize, createdNetworks.size());

            // ... (rest of your assertions) ...
        } else {
            assertEquals(0, createdNetworks.size());
        }
    }
}