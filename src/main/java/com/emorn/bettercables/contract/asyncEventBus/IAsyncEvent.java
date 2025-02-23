package com.emorn.bettercables.contract.asyncEventBus;

public interface IAsyncEvent
{
    public void addToQueue(IAsyncEventInput input);
    public void executeEverySecond();
}
