package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.contract.common.IBlock;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.contract.common.IWorld;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class NetworkManager
{
    private static final Map<IPositionInWorld, Boolean> foundCablePositions = new HashMap<>();
    private static final Map<Integer, ConnectorNetwork> createdNetworksById = new HashMap<>();
    private static int lastId = 1;

    private NetworkManager()
    {
    }

    public static ConnectorNetwork createNewNetwork(int savedId)
    {
        if (createdNetworksById.containsKey(savedId)) {
            return createdNetworksById.get(savedId);
        }

        if (savedId >= lastId) {
            lastId = savedId + 1;
        }

        ConnectorNetwork network = new ConnectorNetwork(savedId);
        createdNetworksById.put(savedId, network);
        return network;
    }

    public static ConnectorNetwork createNewNetwork()
    {
        lastId++;
        int id = lastId;
        ConnectorNetwork network = new ConnectorNetwork(id);
        createdNetworksById.put(id, network);
        return network;
    }

    public static boolean mergeNetworks(
        IWorld worldIn,
        IPositionInWorld pos,
        int totalConnections
    )
    {
        foundCablePositions.clear();
        boolean isRemovingANetwork = false;

        if (totalConnections == 0 || totalConnections == 1) {
            return false;
        }

        foundCablePositions.put(pos, true);

        Map<Integer, ConnectorNetwork> networks = findNetworks(
            worldIn,
            pos,
            totalConnections,
            new HashMap<>()
        );
        if (networks.size() == 1) {
            foundCablePositions.clear();
            return false;
        }

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

    public static void reCalculateNetworksAround(
        IPositionInWorld position,
        IWorld worldIn
    )
    {
        foundCablePositions.clear();

        foundCablePositions.put(position, true);
        List<IPositionInWorld> neighborIPositionInWorldPositions = getPossibleConnectedBlocks(position);

        List<IPositionInWorld> actualNeighborIPositionInWorldPositions = new ArrayList<>();

        for (IPositionInWorld neighborIPositionInWorldPosition : neighborIPositionInWorldPositions) {
            IBlock neighborBlock = worldIn.getBlockState(neighborIPositionInWorldPosition).getBlock();
            if (!(neighborBlock.isBlockConnector()) && !(neighborBlock.isBlockCable())) {
                continue;
            }

            actualNeighborIPositionInWorldPositions.add(neighborIPositionInWorldPosition);
        }

        if (actualNeighborIPositionInWorldPositions.size() < 2) {
            return;
        }

        for (IPositionInWorld neighborIPositionInWorldPosition : actualNeighborIPositionInWorldPositions) {
            ConnectorNetwork newNetwork = NetworkManager.createNewNetwork();
            reCalculateNetworkFrom(neighborIPositionInWorldPosition, worldIn, newNetwork);
        }

        foundCablePositions.clear();
    }

    private static void reCalculateNetworkFrom(
        IPositionInWorld position,
        IWorld worldIn,
        ConnectorNetwork network
    )
    {
        List<IPositionInWorld> neighborIPositionInWorldPositions = getPossibleConnectedBlocks(position);

        for (IPositionInWorld neighborIPositionInWorldPosition : neighborIPositionInWorldPositions) {
            if (foundCablePositions.containsKey(neighborIPositionInWorldPosition)) {
                continue;
            }

            foundCablePositions.put(neighborIPositionInWorldPosition, true);
            IBlock neighborBlock = worldIn.getBlockState(neighborIPositionInWorldPosition).getBlock();

            if (!(neighborBlock.isBlockConnector()) && !(neighborBlock.isBlockCable())) {
                continue;
            }

            if (neighborBlock.isBlockConnector()) {
//                ConnectorNetwork oldNetwork = (
//                    (ForgeTileEntityConnector) Objects.requireNonNull(
//                        worldIn.getTileEntity(neighborIPositionInWorldPosition)
//                    )
//                )
//                    .getNetwork();
//
//                if (oldNetwork.id() == network.id()) {
//                    continue;
//                }
//
//                oldNetwork.remove(neighborIPositionInWorldPosition, network);
            }

            reCalculateNetworkFrom(neighborIPositionInWorldPosition, worldIn, network);
        }
    }

    private static Map<Integer, ConnectorNetwork> findNetworks(
        IWorld worldIn,
        IPositionInWorld pos,
        int totalConnections,
        Map<Integer, ConnectorNetwork> networks
    )
    {
        List<IPositionInWorld> neighborIPositionInWorldPositions = getPossibleConnectedBlocks(pos);

        for (IPositionInWorld neighborIPositionInWorldPosition : neighborIPositionInWorldPositions) {
            if (foundCablePositions.containsKey(neighborIPositionInWorldPosition)) {
                continue;
            }

            foundCablePositions.put(neighborIPositionInWorldPosition, true);
            IBlock neighborBlock = worldIn.getBlockState(neighborIPositionInWorldPosition).getBlock();

            if (!neighborBlock.isBaseCable()) {
                continue;
            }

            if (neighborBlock.isBlockConnector()) {
//                ConnectorNetwork network = (
//                    (ForgeTileEntityConnector) Objects.requireNonNull(
//                        worldIn.getTileEntity(neighborIPositionInWorldPosition)
//                    )
//                ).getNetwork();
//
//                networks.putIfAbsent(network.id(), network);
//
//                if (networks.size() == totalConnections) {
//                    return networks;
//                }
            }
            Map<Integer, ConnectorNetwork> connectorNetworks = findNetworks(
                worldIn,
                neighborIPositionInWorldPosition,
                totalConnections,
                networks
            );
            if (connectorNetworks.size() == totalConnections) {
                return connectorNetworks;
            }
        }

        return networks;
    }

    private static List<IPositionInWorld> getPossibleConnectedBlocks(
        IPositionInWorld pos
    )
    {
        List<IPositionInWorld> connectedBlocks = new ArrayList<>();

        connectedBlocks.add(pos.north());
        connectedBlocks.add(pos.east());
        connectedBlocks.add(pos.south());
        connectedBlocks.add(pos.west());
        connectedBlocks.add(pos.up());
        connectedBlocks.add(pos.down());

        return connectedBlocks;
    }

}
