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
    private final Map<ConnectorSettings, IPositionInWorld> insertConnectorPositionBySettings = new HashMap<>();
    private final List<ConnectorSettings> insertConnectorSettings = new ArrayList<>();
    private final Map<ConnectorSettings, IPositionInWorld> extractConnectorPositionBySettings = new HashMap<>();
    private final List<ConnectorSettings> extractConnectorSettings = new ArrayList<>();

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

        ConnectorSettings settings = this.insertConnectorSettings.get(index);

        if (settings == null) {
            Logger.error("Tried to get empty");
            return null;
        }

        return this.insertConnectorPositionBySettings.get(settings);
    }

    @Nullable
    public ConnectorSettings findInsertSettingsBy(Integer index)
    {
        int totalItems = this.insertConnectorSettings.size();

        if (index >= totalItems || index < 0) {
            return null;
        }

        ConnectorSettings settings = this.insertConnectorSettings.get(index);

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
        if (this.insertConnectorPositionBySettings.containsKey(settings) &&
            this.insertConnectorPositionBySettings.get(settings).equals(inventoryPosition)) {
            return;
        }
        if (!this.insertConnectorPositionBySettings.containsKey(settings)) {
            this.insertConnectorSettings.add(settings);
        }

        this.insertConnectorPositionBySettings.put(settings, inventoryPosition);
    }

    public void addExtract(
        IPositionInWorld inventoryPosition,
        ConnectorSettings settings
    )
    {
        if (this.extractConnectorPositionBySettings.containsKey(settings) &&
            this.extractConnectorPositionBySettings.get(settings).equals(inventoryPosition)) {
            return;
        }
        if (!this.extractConnectorPositionBySettings.containsKey(settings)) {
            this.extractConnectorSettings.add(settings);
        }

        this.extractConnectorPositionBySettings.put(settings, inventoryPosition);
    }

    public void removeInsert(
        ConnectorSettings connectorSettings
    )
    {
        if (!this.insertConnectorPositionBySettings.containsKey(connectorSettings)) {
            return;
        }

        this.insertConnectorSettings.remove(connectorSettings);
        this.insertConnectorPositionBySettings.remove(connectorSettings);
    }

    public void removeExtract(
        ConnectorSettings connectorSettings
    )
    {
        if (!this.extractConnectorPositionBySettings.containsKey(connectorSettings)) {
            return;
        }

        this.extractConnectorSettings.remove(connectorSettings);
        this.extractConnectorPositionBySettings.remove(connectorSettings);
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
        return this.insertConnectorPositionBySettings;
    }

    public Map<ConnectorSettings, IPositionInWorld> findAllExtractConnectors()
    {
        return this.extractConnectorPositionBySettings;
    }

    public List<ConnectorSettings> findAllInsertConnectorSettings()
    {
        return this.insertConnectorSettings;
    }

    public List<ConnectorSettings> findAllExtractConnectorSettings()
    {
        return this.extractConnectorSettings;
    }

    public boolean isCurrentIndexSelfFeed(
        int index,
        ConnectorSettings settings
    )
    {
        return this.insertConnectorSettings.get(index) == settings;
    }
}
