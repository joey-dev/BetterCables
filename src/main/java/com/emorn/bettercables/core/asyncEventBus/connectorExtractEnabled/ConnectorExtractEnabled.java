package com.emorn.bettercables.core.asyncEventBus.connectorExtractEnabled;

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

public class ConnectorExtractEnabled implements IAsyncEvent
{    
    private final BlockingQueue<ConnectorExtractEnabledInput> blockingQueue = new LinkedBlockingQueue<>();

    @Override
    public void addToQueue(IAsyncEventInput input) {
        ConnectorExtractEnabledInput jobInput = (ConnectorExtractEnabledInput) input;
        boolean wasAbleToAdd = blockingQueue.offer(jobInput);
        if (!wasAbleToAdd) {
            Logger.error("Failed to add job to queue");
        }
    }

    @Override
    public void executeEverySecond() {
        Queue<ConnectorExtractEnabledInput> jobQueue = new LinkedList<>();

        blockingQueue.drainTo(jobQueue);

        for (ConnectorExtractEnabledInput jobInput : jobQueue) {
            ConnectorSettings settings = jobInput.settings();
            PossibleSlotCalculator possibleSlotCalculator = jobInput.possibleSlotCalculator();
            List<ConnectorSettings> extractConnectorSettings = jobInput.extractConnectorSettings();

            possibleSlotCalculator.addExtract(
                settings,
                extractConnectorSettings
            );

            jobInput.network().enableNetwork();
        }
    }
}
