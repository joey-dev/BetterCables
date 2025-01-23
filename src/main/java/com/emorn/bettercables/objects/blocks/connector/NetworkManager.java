package com.emorn.bettercables.objects.blocks.connector;

import com.emorn.bettercables.objects.api.forge.blocks.connector.BlockConnector;
import com.emorn.bettercables.objects.api.forge.blocks.cable.BlockCable;
import com.emorn.bettercables.objects.api.forge.blocks.connector.TileEntityConnector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class NetworkManager
{
    private static final Map<BlockPos, Boolean> foundCablePositions = new HashMap<>();

    private NetworkManager()
    {
    }

    public static boolean mergeNetworks(
        World worldIn,
        BlockPos pos,
        int totalConnections
    )
    {
        foundCablePositions.clear();
        boolean isRemovingANetwork = false;

        if (totalConnections == 0 || totalConnections == 1) {
            return isRemovingANetwork;
        }

        foundCablePositions.put(pos, true);

        Map<Integer, ConnectorNetwork> networks = findNetworks(
            worldIn,
            pos,
            totalConnections,
            new HashMap<>()
        );
        if (networks.size() > 1) {
            TreeMap<Integer, ConnectorNetwork> sortedNetworks = new TreeMap<>(networks);
            ConnectorNetwork firstNetwork = sortedNetworks.firstEntry().getValue();

            for (Map.Entry<Integer, ConnectorNetwork> entry : sortedNetworks.entrySet()) {
                ConnectorNetwork network = entry.getValue();

                if (network.id() == firstNetwork.id()) {
                    continue;
                }

                isRemovingANetwork = true;
                network.remove(firstNetwork);
            }

            foundCablePositions.clear();
            return isRemovingANetwork;
        }

        foundCablePositions.clear();
        return isRemovingANetwork;
    }

    public static void reCalculateNetworksAround(
        BlockPos position,
        World worldIn
    )
    {
        foundCablePositions.clear();

        foundCablePositions.put(position, true);
        List<BlockPos> neighborBlockPositions = getPossibleConnectedBlocks(position);

        List<BlockPos> actualNeighborBlockPositions = new ArrayList<>();

        for (BlockPos neighborBlockPosition : neighborBlockPositions) {
            Block neighborBlock = worldIn.getBlockState(neighborBlockPosition).getBlock();
            if (!(neighborBlock instanceof BlockConnector) && !(neighborBlock instanceof BlockCable)) {
                continue;
            }

            actualNeighborBlockPositions.add(neighborBlockPosition);
        }

        if (actualNeighborBlockPositions.size() < 2) {
            return;
        }

        for (BlockPos neighborBlockPosition : actualNeighborBlockPositions) {
            ConnectorNetwork newNetwork = ConnectorNetwork.create();
            reCalculateNetworkFrom(neighborBlockPosition, worldIn, newNetwork);
        }

        foundCablePositions.clear();
    }

    private static void reCalculateNetworkFrom(
        BlockPos position,
        World worldIn,
        ConnectorNetwork network
    )
    {
        List<BlockPos> neighborBlockPositions = getPossibleConnectedBlocks(position);

        for (BlockPos neighborBlockPosition : neighborBlockPositions) {
            if (foundCablePositions.containsKey(neighborBlockPosition)) {
                continue;
            }

            foundCablePositions.put(neighborBlockPosition, true);
            Block neighborBlock = worldIn.getBlockState(neighborBlockPosition).getBlock();

            if (!(neighborBlock instanceof BlockConnector) && !(neighborBlock instanceof BlockCable)) {
                continue;
            }

            if (neighborBlock instanceof BlockConnector) {
                ConnectorNetwork oldNetwork = (
                    (TileEntityConnector) Objects.requireNonNull(
                        worldIn.getTileEntity(neighborBlockPosition)
                    )
                )
                    .getNetwork();

                if (oldNetwork.id() == network.id()) {
                    continue;
                }

                oldNetwork.remove(neighborBlockPosition, network);
            }

            reCalculateNetworkFrom(neighborBlockPosition, worldIn, network);
        }
    }

    private static Map<Integer, ConnectorNetwork> findNetworks(
        World worldIn,
        BlockPos pos,
        int totalConnections,
        Map<Integer, ConnectorNetwork> networks
    )
    {
        List<BlockPos> neighborBlockPositions = getPossibleConnectedBlocks(pos);

        for (BlockPos neighborBlockPosition : neighborBlockPositions) {
            if (foundCablePositions.containsKey(neighborBlockPosition)) {
                continue;
            }

            foundCablePositions.put(neighborBlockPosition, true);
            Block neighborBlock = worldIn.getBlockState(neighborBlockPosition).getBlock();

            if (!(neighborBlock instanceof BlockConnector) && !(neighborBlock instanceof BlockCable)) {
                continue;
            }

            if (neighborBlock instanceof BlockConnector) {
                ConnectorNetwork network = (
                    (TileEntityConnector) Objects.requireNonNull(
                        worldIn.getTileEntity(neighborBlockPosition)
                    )
                ).getNetwork();

                if (!networks.containsKey(network.id())) {
                    networks.put(network.id(), network);

                    if (networks.size() == totalConnections) {
                        return networks;
                    }
                }
            }
            Map<Integer, ConnectorNetwork> connectorNetworks = findNetworks(
                worldIn,
                neighborBlockPosition,
                totalConnections,
                networks
            );
            if (connectorNetworks.size() == totalConnections) {
                return connectorNetworks;
            }
        }

        return networks;
    }

    private static List<BlockPos> getPossibleConnectedBlocks(
        BlockPos pos
    )
    {
        List<BlockPos> connectedBlocks = new ArrayList<>();
        connectedBlocks.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1));
        connectedBlocks.add(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()));
        connectedBlocks.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1));
        connectedBlocks.add(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()));
        connectedBlocks.add(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
        connectedBlocks.add(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));

        return connectedBlocks;
    }

}
