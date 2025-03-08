package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventBus;
import com.emorn.bettercables.contract.common.IBlock;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.contract.common.ITileEntity;
import com.emorn.bettercables.contract.common.IWorld;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.core.common.Logger;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class NetworkManager
{
    private final Map<String, Boolean> foundCablePositions = new HashMap<>();
    private final Map<Integer, ConnectorNetwork> createdNetworksById = new HashMap<>();
    private int lastId = 1;

    private NetworkManager()
    {
    }

    public static NetworkManager getInstance()
    {
        return new NetworkManager();
    }

    public ConnectorNetwork createNewNetwork(
        int savedId,
        IAsyncEventBus eventBus
    )
    {
        if (createdNetworksById.containsKey(savedId)) {
            ConnectorNetwork network = createdNetworksById.get(savedId);
            network.updateEventBus(eventBus);
            return network;
        }

        if (savedId >= lastId) {
            lastId = savedId + 1;
        }

        ConnectorNetwork network = new ConnectorNetwork(savedId, eventBus);
        createdNetworksById.put(savedId, network);
        return network;
    }

    public ConnectorNetwork createNewNetwork(
        IAsyncEventBus eventBus
    )
    {
        lastId++;
        int id = lastId;
        ConnectorNetwork network = new ConnectorNetwork(id, eventBus);
        createdNetworksById.put(id, network);
        return network;
    }

    public boolean mergeNetworks(
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

        foundCablePositions.put(pos.toKey(), true);

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

    public void reCalculateNetworksAround(
        IPositionInWorld position,
        IWorld worldIn,
        IAsyncEventBus eventBus
    )
    {
        foundCablePositions.clear();

        foundCablePositions.put(position.toKey(), true);
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
            ConnectorNetwork newNetwork = this.createNewNetwork(eventBus);
            reCalculateNetworkFrom(neighborIPositionInWorldPosition, worldIn, newNetwork);
        }

        foundCablePositions.clear();
    }

    private void reCalculateNetworkFrom(
        IPositionInWorld position,
        IWorld worldIn,
        ConnectorNetwork network
    )
    {
        List<IPositionInWorld> neighborIPositionInWorldPositions = getPossibleConnectedBlocks(position);
        neighborIPositionInWorldPositions.add(position);

        for (IPositionInWorld neighborIPositionInWorldPosition : neighborIPositionInWorldPositions) {
            if (foundCablePositions.containsKey(neighborIPositionInWorldPosition.toKey())) {
                continue;
            }

            foundCablePositions.put(neighborIPositionInWorldPosition.toKey(), true);
            IBlock neighborBlock = worldIn.getBlockState(neighborIPositionInWorldPosition).getBlock();

            if (!(neighborBlock.isBlockConnector()) && !(neighborBlock.isBlockCable())) {
                continue;
            }

            if (neighborBlock.isBlockConnector()) {
                ITileEntity neighborBlockEntity = worldIn.getTileEntity(neighborIPositionInWorldPosition);
                ConnectorNetwork oldNetwork = neighborBlockEntity.getNetworkIfConnector();

                if (oldNetwork == null) {
                    Logger.error("Connector has no network");
                    continue;
                }

                if (oldNetwork.id() == network.id()) {
                    continue;
                }

                oldNetwork.remove(neighborIPositionInWorldPosition, network);
                addConnectorToNetwork(neighborBlockEntity, network, neighborIPositionInWorldPosition);
            }

            reCalculateNetworkFrom(neighborIPositionInWorldPosition, worldIn, network);
        }
    }

    private void addConnectorToNetwork(
        ITileEntity neighborBlockEntity,
        ConnectorNetwork network,
        IPositionInWorld position
    )
    {
        addConnectorToNetworkForDirection(
            Direction.NORTH,
            neighborBlockEntity,
            network,
            position.north()
        );
        addConnectorToNetworkForDirection(
            Direction.EAST,
            neighborBlockEntity,
            network,
            position.east()
        );
        addConnectorToNetworkForDirection(
            Direction.SOUTH,
            neighborBlockEntity,
            network,
            position.south()
        );
        addConnectorToNetworkForDirection(
            Direction.WEST,
            neighborBlockEntity,
            network,
            position.west()
        );
        addConnectorToNetworkForDirection(
            Direction.UP,
            neighborBlockEntity,
            network,
            position.up()
        );
        addConnectorToNetworkForDirection(
            Direction.DOWN,
            neighborBlockEntity,
            network,
            position.down()
        );
    }

    private void addConnectorToNetworkForDirection(
        Direction direction,
        ITileEntity neighborBlockEntity,
        ConnectorNetwork network,
        IPositionInWorld position
    )
    {
        ConnectorSettings settings = neighborBlockEntity.settings(direction);
        if (settings.isInsertEnabled()) {
            network.addInsert(position, settings);
        }
        if (settings.isExtractEnabled()) {
            network.addExtract(position, settings);
        }
    }

    private Map<Integer, ConnectorNetwork> findNetworks(
        IWorld worldIn,
        IPositionInWorld pos,
        int totalConnections,
        Map<Integer, ConnectorNetwork> networks
    )
    {
        List<IPositionInWorld> neighborIPositionInWorldPositions = getPossibleConnectedBlocks(pos);

        for (IPositionInWorld neighborIPositionInWorldPosition : neighborIPositionInWorldPositions) {
            if (foundCablePositions.containsKey(neighborIPositionInWorldPosition.toKey())) {
                continue;
            }

            foundCablePositions.put(neighborIPositionInWorldPosition.toKey(), true);
            IBlock neighborBlock = worldIn.getBlockState(neighborIPositionInWorldPosition).getBlock();

            if (!neighborBlock.isBaseCable()) {
                continue;
            }

            if (neighborBlock.isBlockConnector()) {
                ConnectorNetwork network = worldIn.getTileEntity(neighborIPositionInWorldPosition).getNetworkIfConnector();

                if (network == null) {
                    Logger.error("Connector has no network");
                    continue;
                }

                networks.putIfAbsent(network.id(), network);

                if (networks.size() == totalConnections) {
                    return networks;
                }
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

    private List<IPositionInWorld> getPossibleConnectedBlocks(
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
