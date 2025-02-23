package com.emorn.bettercables.core.asyncEventBus.connectorExtractEnabled;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventInput;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.network.PossibleSlotCalculator;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;

import java.util.List;

public class ConnectorExtractEnabledInput implements IAsyncEventInput
{
    private final ConnectorSettings settings;
    private final PossibleSlotCalculator possibleSlotCalculator;
    private final List<ConnectorSettings> extractConnectorSettings;
    private final ConnectorNetwork network;

    public ConnectorExtractEnabledInput(
        ConnectorSettings settings,
        PossibleSlotCalculator possibleSlotCalculator,
        List<ConnectorSettings> extractConnectorSettings,
        ConnectorNetwork network
    )
    {
        this.settings = settings;
        this.possibleSlotCalculator = possibleSlotCalculator;
        this.extractConnectorSettings = extractConnectorSettings;
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

    public List<ConnectorSettings> extractConnectorSettings()
    {
        return extractConnectorSettings;
    }

    public ConnectorNetwork network()
    {
        return network;
    }
}
