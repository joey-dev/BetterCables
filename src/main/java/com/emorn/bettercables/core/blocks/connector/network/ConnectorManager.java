package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Logger;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorManager
{
    // key = settings, value = inventory
    private final Map<ConnectorSettings, IPositionInWorld> insertConnectorSettings = new HashMap<>();
    // key = settings, value = inventory
    private final Map<ConnectorSettings, IPositionInWorld> extractConnectorSettings = new HashMap<>();

    public int totalInsertConnections()
    {
        return this.insertConnectorSettings.size();
    }

    @Nullable
    public IPositionInWorld findInventoryPositionBy(Integer index)
    {
        int totalItems = this.insertConnectorSettings.size();

        if (index >= totalItems || index < 0) {
            return null;
        }

        if (this.insertConnectorSettings.keySet().toArray()[index] == null) {
            Logger.error("Tried to get empty");
            return null;
        }

        // no pos (value)
        return (IPositionInWorld) this.insertConnectorSettings.values().toArray()[index];
    }

    @Nullable
    public ConnectorSettings findInsertSettingsBy(Integer index)
    {
        int totalItems = this.insertConnectorSettings.size();

        if (index >= totalItems || index < 0) {
            return null;
        }

        ConnectorSettings settings = (ConnectorSettings) this.insertConnectorSettings.keySet().toArray()[index];

        if (settings == null) {
            Logger.error("Tried to get empty");
            return null;
        }

        return settings;
    }

    public void addInsert(
        IPositionInWorld inventoryPosition,
        ConnectorSettings settings
    )
    {
        if (this.insertConnectorSettings.containsKey(settings) &&
            this.insertConnectorSettings.get(settings).equals(inventoryPosition)) {
            return;
        }

        this.insertConnectorSettings.put(settings, inventoryPosition);
    }

    public void addExtract(
        IPositionInWorld inventoryPosition,
        ConnectorSettings settings
    )
    {
        if (this.extractConnectorSettings.containsKey(settings) &&
            this.extractConnectorSettings.get(settings).equals(inventoryPosition)) {
            return;
        }

        this.extractConnectorSettings.put(settings, inventoryPosition);
    }

    public void removeInsert(
        ConnectorSettings connectorSettings
    )
    {
        if (!this.insertConnectorSettings.containsKey(connectorSettings)) {
            return;
        }

        this.insertConnectorSettings.remove(connectorSettings);
    }

    public void removeExtract(
        ConnectorSettings connectorSettings
    )
    {
        if (!this.extractConnectorSettings.containsKey(connectorSettings)) {
            return;
        }

        this.extractConnectorSettings.remove(connectorSettings);
    }

    public void updateSlotCount(
        int sizeInventory,
        ConnectorSettings connector
    )
    {
        if (connector.inventorySlotCount() == sizeInventory) {
            return;
        }

        connector.changeInventorySlotCount(sizeInventory);
    }

    public Map<ConnectorSettings, IPositionInWorld> findAllInsertConnectors()
    {
        return this.insertConnectorSettings;
    }

    public Map<ConnectorSettings, IPositionInWorld> findAllExtractConnectors()
    {
        return this.extractConnectorSettings;
    }

    public List<ConnectorSettings> findAllInsertConnectorSettings()
    {
        return new ArrayList<>(this.insertConnectorSettings.keySet());
    }

    public List<ConnectorSettings> findAllExtractConnectorSettings()
    {
        return new ArrayList<>(this.extractConnectorSettings.keySet());
    }
}
