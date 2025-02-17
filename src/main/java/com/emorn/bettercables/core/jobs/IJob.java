package com.emorn.bettercables.core.jobs;

public interface IJob
{
    public void addToQueue(IJobInput input);
    public void executeEverySecond();
}
