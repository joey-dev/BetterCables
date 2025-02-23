package com.emorn.bettercables.core.asyncEventBus.connectorExtractDisabled;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventInput;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.network.PossibleSlotCalculator;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;

public class ConnectorExtractDisabledInput implements IAsyncEventInput
{
    private final ConnectorSettings settings;
    private final PossibleSlotCalculator possibleSlotCalculator;
    private final ConnectorNetwork network;

    public ConnectorExtractDisabledInput(
        ConnectorSettings settings,
        PossibleSlotCalculator possibleSlotCalculator,
        ConnectorNetwork network
    )
    {
        this.settings = settings;
        this.possibleSlotCalculator = possibleSlotCalculator;
        this.network = network;
    }

    @Override
    public String identifier()
    {
        return "";
    }

    public ConnectorSettings connectorSettings()
    {
        return this.settings;
    }

    public PossibleSlotCalculator possibleSlotCalculator()
    {
        return this.possibleSlotCalculator;
    }

    public ConnectorNetwork network()
    {
        return this.network;
    }
}
