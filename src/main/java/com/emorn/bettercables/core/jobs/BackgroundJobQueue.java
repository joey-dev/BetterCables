package com.emorn.bettercables.core.jobs;

import com.emorn.bettercables.core.jobs.recalculatePossibleSlotsBasedOnInventoryChange.RecalculatePossibleSlotsBasedOnInventoryChange;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;
import java.util.concurrent.*;

public class BackgroundJobQueue {

    private final ThreadPoolExecutor executor;
    private final int maxConcurrentThreads;
    private final Map<Class<? extends IJob>, IJob> jobInstances = new ConcurrentHashMap<>();
    private long lastSecondTick = 0;

    private static BackgroundJobQueue instance;

    private static final Class<? extends IJob>[] JOB_CLASSES = new Class[]{
        RecalculatePossibleSlotsBasedOnInventoryChange.class
    };

    private BackgroundJobQueue(int maxConcurrentThreads) {
        if (maxConcurrentThreads <= 0) {
            throw new IllegalArgumentException("maxConcurrentThreads must be greater than 0");
        }
        this.maxConcurrentThreads = maxConcurrentThreads;

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
        for (Class<? extends IJob> jobClass : JOB_CLASSES) {
            try {
                jobInstances.put(jobClass, jobClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                FMLLog.log.error("Failed to create instance of job class: " + jobClass.getName(), e);
            }
        }
    }

    public static synchronized BackgroundJobQueue getInstance() {
        if (instance == null) {
            instance = new BackgroundJobQueue(2);
            MinecraftForge.EVENT_BUS.register(instance);
            MinecraftForge.EVENT_BUS.register(new MinecraftServerProxy());
        }
        return instance;
    }

    public <T extends IJob> void addToQueue(Class<T> jobClass, IJobInput input) {
        IJob jobInstance = jobInstances.get(jobClass);
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
            // Execute per-second jobs
            if (MinecraftServerProxy.currentTick - lastSecondTick >= 20) {
                lastSecondTick = MinecraftServerProxy.currentTick;
                for (IJob job : jobInstances.values()) {
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

