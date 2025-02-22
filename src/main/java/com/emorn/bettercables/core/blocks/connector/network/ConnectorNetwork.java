package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import mcp.MethodsReturnNonnullByDefault;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorNetwork
{
    private final int id;
    private final Map<IPositionInWorld, ConnectorNetwork> mergeToNetwork = new HashMap<>();
    private boolean shouldMerge = false;
    private boolean isDisabled = false;
    private final ConnectorManager connectorManager = new ConnectorManager();
    private final PossibleSlotCalculator possibleSlotCalculator = new PossibleSlotCalculator();

    public ConnectorNetwork(int id)
    {
        this.id = id;
        this.reCalculateAllPossibleSlots();
    }

    @Nullable
    public Integer findNextIndex(int index)
    {
        int totalItems = this.connectorManager.totalInsertConnections();
        if (totalItems == 0) {
            return null;
        }

        index++;

        if (index >= totalItems) {
            index = 0;
        }

        return index;
    }

    @Nullable
    public IPositionInWorld findInventoryPositionBy(Integer index)
    {
        return this.connectorManager.findInventoryPositionBy(index);
    }

    @Nullable
    public ConnectorSettings findInsertSettingsBy(Integer index)
    {
        return this.connectorManager.findInsertSettingsBy(index);
    }

    public int id()
    {
        return id;
    }

    public void remove(ConnectorNetwork newNetwork)
    {
        this.shouldMerge = true;
        this.mergeToNetwork.put(null, newNetwork);
    }

    public void addInsert(
        IPositionInWorld inventoryPosition,
        ConnectorSettings settings
    )
    {
        this.isDisabled = true;

        this.connectorManager.addInsert(inventoryPosition, settings);
        this.possibleSlotCalculator.addInsert(
            settings,
            this.connectorManager.findAllExtractConnectorSettings()
        );

        this.isDisabled = false;
    }

    public void addExtract(
        IPositionInWorld inventoryPosition,
        ConnectorSettings settings
    )
    {
        this.isDisabled = true;

        this.connectorManager.addExtract(inventoryPosition, settings);
        this.possibleSlotCalculator.addExtract(
            settings,
            this.connectorManager.findAllInsertConnectorSettings()
        );

        this.isDisabled = false;
    }

    public List<ExtractSlot> getPossibleSlots(
        ConnectorSettings exportSettings
    )
    {
        return this.possibleSlotCalculator.getPossibleSlots(exportSettings);
    }

    public void reCalculateAllPossibleSlots()
    {
        this.isDisabled = true;
        this.possibleSlotCalculator.reCalculateAllPossibleSlots(
            this.connectorManager.findAllInsertConnectorSettings(),
            this.connectorManager.findAllExtractConnectorSettings()
        );

        this.isDisabled = false;
    }

    public void removeInsert(
        ConnectorSettings connectorSettings
    )
    {
        this.isDisabled = true;

        this.connectorManager.removeInsert(connectorSettings);
        this.possibleSlotCalculator.removeInsert(connectorSettings);

        this.isDisabled = false;
    }

    public void removeExtract(
        ConnectorSettings connectorSettings
    )
    {
        this.isDisabled = true;

        this.connectorManager.removeExtract(connectorSettings);
        this.possibleSlotCalculator.removeExtract(connectorSettings);

        this.isDisabled = false;
    }

    public boolean isDisabled()
    {
        return isDisabled;
    }

    public void remove(
        IPositionInWorld position,
        ConnectorNetwork newNetwork
    )
    {
        this.shouldMerge = true;

        this.mergeToNetwork.put(position, newNetwork);
    }

    public boolean isRemoved()
    {
        return shouldMerge;
    }

    @Nullable
    public ConnectorNetwork mergeToNetwork(IPositionInWorld position)
    {
        if (mergeToNetwork.containsKey(null)) {
            return mergeToNetwork.get(null);
        }

        return mergeToNetwork.get(position);
    }

    public void updateSlotCount(
        int sizeInventory,
        ConnectorSettings connector
    )
    {
        this.connectorManager.updateSlotCount(sizeInventory, connector);
        this.possibleSlotCalculator.updateSlotCount(
            connector,
            this.connectorManager.findAllInsertConnectorSettings(),
            this.connectorManager.findAllExtractConnectorSettings()
        );
    }
}
