package com.emorn.bettercables.api.v1_12_2.asyncEventBus;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEvent;
import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventBus;
import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventInput;
import com.emorn.bettercables.core.asyncEventBus.connectorExtractDisabled.ConnectorExtractDisabled;
import com.emorn.bettercables.core.asyncEventBus.connectorExtractEnabled.ConnectorExtractEnabled;
import com.emorn.bettercables.core.asyncEventBus.connectorInsertDisabled.ConnectorInsertDisabled;
import com.emorn.bettercables.core.asyncEventBus.connectorInsertEnabled.ConnectorInsertEnabled;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;
import java.util.concurrent.*;

public class AsyncEventBus implements IAsyncEventBus
{
    private final ThreadPoolExecutor executor;
    private final Map<Class<? extends IAsyncEvent>, IAsyncEvent> jobInstances = new ConcurrentHashMap<>();

    private long lastSecondTick = 0;
    private static AsyncEventBus instance;

    private static final Class<? extends IAsyncEvent>[] JOB_CLASSES = new Class[]{
        ConnectorInsertDisabled.class,
        ConnectorInsertEnabled.class,
        ConnectorExtractDisabled.class,
        ConnectorExtractEnabled.class,
    };

    public static synchronized AsyncEventBus getInstance() {
        if (instance == null) {
            instance = new AsyncEventBus(2);
            MinecraftForge.EVENT_BUS.register(instance);
            MinecraftForge.EVENT_BUS.register(new AsyncEventBus.MinecraftServerProxy());
        }
        return instance;
    }

    private AsyncEventBus(int maxConcurrentThreads) {
        if (maxConcurrentThreads <= 0) {
            throw new IllegalArgumentException("maxConcurrentThreads must be greater than 0");
        }
        // Use ArrayBlockingQueue instead of LinkedBlockingQueue (reduces lock contention)
        this.executor = new ThreadPoolExecutor(
            maxConcurrentThreads,
            maxConcurrentThreads,
            30L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(maxConcurrentThreads * 2),  // Small bounded queue
            new MinecraftThreadFactory(),
            new ThreadPoolExecutor.DiscardPolicy()  // Prevents overloading the queue
        );

        // Instantiate all job classes at startup
        for (Class<? extends IAsyncEvent> jobClass : JOB_CLASSES) {
            try {
                jobInstances.put(jobClass, jobClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                FMLLog.log.error("Failed to create instance of job class: " + jobClass.getName(), e);
            }
        }
    }

    public <T extends IAsyncEvent> void publish(Class<T> jobClass, IAsyncEventInput input) {
        IAsyncEvent jobInstance = jobInstances.get(jobClass);
        if (jobInstance == null) {
            throw new IllegalArgumentException("Job class not registered: " + jobClass.getName());
        }
        jobInstance.addToQueue(input);
    }

    @SubscribeEvent
    public void run(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        try {
            if (MinecraftServerProxy.currentTick - lastSecondTick >= 20) {
                lastSecondTick = MinecraftServerProxy.currentTick;
                for (IAsyncEvent job : jobInstances.values()) {
                    if (executor.getQueue().remainingCapacity() > 0) {
                        executor.execute(() -> {
                            try {
                                job.executeEverySecond();
                            } catch (Throwable t) {
                                FMLLog.log.error("Error executing per-second job", t);
                            }
                        });
                    }
                }
            }
        } catch (RejectedExecutionException e) {
            FMLLog.log.error("Job queue rejected execution", e);
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                FMLLog.log.warn("BackgroundJobQueue did not shut down cleanly after 60 seconds.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            instance = null;
        }
    }

    public void shutdownNow() {
        executor.shutdownNow();
    }

    private static class MinecraftThreadFactory implements ThreadFactory {
        private int threadCount = 0;
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "BackgroundJobQueue-" + threadCount++);
            t.setDaemon(true);
            return t;
        }
    }

    public static class MinecraftServerProxy {
        public static long currentTick = 0;
        @SubscribeEvent
        public void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                currentTick++;
            }
        }
    }
}

