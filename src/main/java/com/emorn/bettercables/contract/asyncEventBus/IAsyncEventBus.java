package com.emorn.bettercables.contract.asyncEventBus;

public interface IAsyncEventBus
{
    public <T extends IAsyncEvent> void publish(Class<T> jobClass, IAsyncEventInput input);
}
