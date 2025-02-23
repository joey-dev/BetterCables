package com.emorn.bettercables.core.asyncEventBus.connectorInsertEnabled;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventInput;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.network.PossibleSlotCalculator;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;

import java.util.List;

public class ConnectorInsertEnabledInput implements IAsyncEventInput
{
    private final ConnectorSettings settings;
    private final PossibleSlotCalculator possibleSlotCalculator;
    private final List<ConnectorSettings> insertConnectorSettings;
    private final ConnectorNetwork network;

    public ConnectorInsertEnabledInput(
        ConnectorSettings settings,
        PossibleSlotCalculator possibleSlotCalculator,
        List<ConnectorSettings> insertConnectorSettings,
        ConnectorNetwork network
    )
    {
        this.settings = settings;
        this.possibleSlotCalculator = possibleSlotCalculator;
        this.insertConnectorSettings = insertConnectorSettings;
        this.network = network;
    }

    @Override
    public String identifier()
    {
        return "";
    }

    public ConnectorSettings settings()
    {
        return settings;
    }

    public PossibleSlotCalculator possibleSlotCalculator()
    {
        return possibleSlotCalculator;
    }

    public List<ConnectorSettings> insertConnectorSettings()
    {
        return insertConnectorSettings;
    }

    public ConnectorNetwork network()
    {
        return network;
    }
}
