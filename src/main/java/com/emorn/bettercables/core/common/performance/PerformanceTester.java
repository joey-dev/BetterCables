package com.emorn.bettercables.core.common.performance;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PerformanceTester
{
    private static final Map<String, List<Long>> results = new HashMap<>();
    private static int lastRan = 0;

    public static void start(String test)
    {
        results.putIfAbsent(test, new ArrayList<>());
        results.get(test).add(System.nanoTime());
    }

    public static void end(String test)
    {
        long startTime = results.get(test).remove(results.get(test).size() - 1);
        long endTime = System.nanoTime();
        results.get(test).add(endTime - startTime);
    }

    public static void printResults()
    {
        lastRan++;
        if (lastRan < 1000) return;

        for (Map.Entry<String, List<Long>> entry : results.entrySet()) {
            String testName = entry.getKey();
            List<Long> times = entry.getValue();
            long min = times.stream().min(Long::compare).orElse(0L);
            long max = times.stream().max(Long::compare).orElse(0L);
            double average = times.stream().mapToLong(Long::longValue).average().orElse(0.0);
            System.out.printf("Test: %s, Min: %d ns, Max: %d ns, Average: %.2f ns%n", testName, min, max, average);
        }

        results.clear();
        lastRan = 0;

        //Minecraft.getMinecraft().player.sendChatMessage("");
    }
}
