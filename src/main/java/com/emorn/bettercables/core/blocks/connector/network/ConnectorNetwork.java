package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventBus;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.asyncEventBus.connectorExtractDisabled.ConnectorExtractDisabled;
import com.emorn.bettercables.core.asyncEventBus.connectorExtractDisabled.ConnectorExtractDisabledInput;
import com.emorn.bettercables.core.asyncEventBus.connectorExtractEnabled.ConnectorExtractEnabled;
import com.emorn.bettercables.core.asyncEventBus.connectorExtractEnabled.ConnectorExtractEnabledInput;
import com.emorn.bettercables.core.asyncEventBus.connectorInsertDisabled.ConnectorInsertDisabled;
import com.emorn.bettercables.core.asyncEventBus.connectorInsertDisabled.ConnectorInsertDisabledInput;
import com.emorn.bettercables.core.asyncEventBus.connectorInsertEnabled.ConnectorInsertEnabled;
import com.emorn.bettercables.core.asyncEventBus.connectorInsertEnabled.ConnectorInsertEnabledInput;
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
    private final IAsyncEventBus eventBus;

    public ConnectorNetwork(
        int id,
        IAsyncEventBus eventBus
    )
    {
        this.eventBus = eventBus;
        this.id = id;
        this.reCalculateAllPossibleSlots();
    }

    public void enableNetwork()
    {
        this.isDisabled = false;
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
        this.eventBus.publish(
            ConnectorInsertEnabled.class,
            new ConnectorInsertEnabledInput(
                settings,
                this.possibleSlotCalculator,
                this.connectorManager.findAllExtractConnectorSettings(),
                this
            )
        );
    }

    public void insertSlotCountChanged(
        ConnectorSettings settings
    )
    {
        this.isDisabled = true;

        this.eventBus.publish(
            ConnectorInsertEnabled.class,
            new ConnectorInsertEnabledInput(
                settings,
                this.possibleSlotCalculator,
                this.connectorManager.findAllExtractConnectorSettings(),
                this
            )
        );
    }

    public void addExtract(
        IPositionInWorld inventoryPosition,
        ConnectorSettings settings
    )
    {
        this.isDisabled = true;

        this.connectorManager.addExtract(inventoryPosition, settings);
        this.eventBus.publish(
            ConnectorExtractEnabled.class,
            new ConnectorExtractEnabledInput(
                settings,
                this.possibleSlotCalculator,
                this.connectorManager.findAllInsertConnectorSettings(),
                this
            )
        );
    }

    public void extractSlotCountChanged(
        ConnectorSettings settings
    )
    {
        this.isDisabled = true;

        this.eventBus.publish(
            ConnectorExtractEnabled.class,
            new ConnectorExtractEnabledInput(
                settings,
                this.possibleSlotCalculator,
                this.connectorManager.findAllInsertConnectorSettings(),
                this
            )
        );
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
        this.eventBus.publish(
            ConnectorInsertDisabled.class,
            new ConnectorInsertDisabledInput(
                connectorSettings,
                this.possibleSlotCalculator,
                this
            )
        );
    }

    public void removeExtract(
        ConnectorSettings connectorSettings
    )
    {
        this.isDisabled = true;

        this.connectorManager.removeExtract(connectorSettings);
        this.eventBus.publish(
            ConnectorExtractDisabled.class,
            new ConnectorExtractDisabledInput(
                connectorSettings,
                this.possibleSlotCalculator,
                this
            )
        );
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
        if (!mergeToNetwork.containsKey(null)) {
            return mergeToNetwork.get(position);
        }

        ConnectorNetwork newNetwork = mergeToNetwork.get(null);

        return newNetwork;
    }

    public void updateSlotCount(
        int sizeInventory,
        ConnectorSettings connector
    )
    {
        this.connectorManager.updateSlotCount(sizeInventory, connector);
        this.possibleSlotCalculator.calculateForConnector(
            connector,
            this.connectorManager.findAllInsertConnectorSettings(),
            this.connectorManager.findAllExtractConnectorSettings()
        );
    }
}
