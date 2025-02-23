package com.emorn.bettercables.core.asyncEventBus.connectorInsertEnabled;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEvent;
import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventInput;
import com.emorn.bettercables.core.blocks.connector.network.PossibleSlotCalculator;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectorInsertEnabled implements IAsyncEvent
{    
    private final BlockingQueue<ConnectorInsertEnabledInput> blockingQueue = new LinkedBlockingQueue<>();

    @Override
    public void addToQueue(IAsyncEventInput input) {
        ConnectorInsertEnabledInput jobInput = (ConnectorInsertEnabledInput) input;
        boolean wasAbleToAdd = blockingQueue.offer(jobInput);
        if (!wasAbleToAdd) {
            Logger.error("Failed to add job to queue");
        }
    }

    @Override
    public void executeEverySecond() {
        Queue<ConnectorInsertEnabledInput> jobQueue = new LinkedList<>();

        blockingQueue.drainTo(jobQueue);

        for (ConnectorInsertEnabledInput jobInput : jobQueue) {
            ConnectorSettings settings = jobInput.settings();
            PossibleSlotCalculator possibleSlotCalculator = jobInput.possibleSlotCalculator();
            List<ConnectorSettings> extractConnectorSettings = jobInput.insertConnectorSettings();

            possibleSlotCalculator.addInsert(
                settings,
                extractConnectorSettings
            );

            jobInput.network().enableNetwork();
        }
    }
}
