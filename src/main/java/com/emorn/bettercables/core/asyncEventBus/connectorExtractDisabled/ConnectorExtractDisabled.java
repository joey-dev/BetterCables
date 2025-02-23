package com.emorn.bettercables.core.asyncEventBus.connectorExtractDisabled;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEvent;
import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventInput;
import com.emorn.bettercables.core.blocks.connector.network.PossibleSlotCalculator;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectorExtractDisabled implements IAsyncEvent
{
    private final BlockingQueue<ConnectorExtractDisabledInput> blockingQueue = new LinkedBlockingQueue<>();

    @Override
    public void addToQueue(IAsyncEventInput input) {
        ConnectorExtractDisabledInput jobInput = (ConnectorExtractDisabledInput) input;
        boolean wasAbleToAdd = blockingQueue.offer(jobInput);
        if (!wasAbleToAdd) {
            Logger.error("Failed to add job to queue");
        }
    }

    @Override
    public void executeEverySecond() {
        Queue<ConnectorExtractDisabledInput> jobQueue = new LinkedList<>();

        blockingQueue.drainTo(jobQueue);

        for (ConnectorExtractDisabledInput jobInput : jobQueue) {
            ConnectorSettings settings = jobInput.connectorSettings();
            PossibleSlotCalculator possibleSlotCalculator = jobInput.possibleSlotCalculator();

            possibleSlotCalculator.removeExtract(settings);

            jobInput.network().enableNetwork();
        }
    }
}