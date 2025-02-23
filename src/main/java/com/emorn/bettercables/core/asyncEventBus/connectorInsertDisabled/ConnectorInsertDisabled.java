package com.emorn.bettercables.core.asyncEventBus.connectorInsertDisabled;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEvent;
import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventInput;
import com.emorn.bettercables.core.blocks.connector.network.PossibleSlotCalculator;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectorInsertDisabled implements IAsyncEvent
{
    private final BlockingQueue<ConnectorInsertDisabledInput> blockingQueue = new LinkedBlockingQueue<>();

    @Override
    public void addToQueue(IAsyncEventInput input) {
        ConnectorInsertDisabledInput jobInput = (ConnectorInsertDisabledInput) input;
        boolean wasAbleToAdd = blockingQueue.offer(jobInput);
        if (!wasAbleToAdd) {
            Logger.error("Failed to add job to queue");
        }
    }

    @Override
    public void executeEverySecond() {
        Queue<ConnectorInsertDisabledInput> jobQueue = new LinkedList<>();

        blockingQueue.drainTo(jobQueue);

        for (ConnectorInsertDisabledInput jobInput : jobQueue) {
            ConnectorSettings settings = jobInput.connectorSettings();
            PossibleSlotCalculator possibleSlotCalculator = jobInput.possibleSlotCalculator();

            possibleSlotCalculator.removeInsert(settings);

            jobInput.network().enableNetwork();
        }
    }
}