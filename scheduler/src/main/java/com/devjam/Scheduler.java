package com.devjam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        // Schedule the task to run once a day at a specific time
        scheduler.scheduleAtFixedRate(() -> {
            DataFetcher dataFetcher = new DataFetcher();
            dataFetcher.fetchDataAndPublishToQueue();
        }, initialDelay, 1, TimeUnit.DAYS); // update initial delay to 0 for demo run
    }
    
    private static long initialDelay() {
        // Calculate initial delay to start the task at the desired time
        // Example: If you want to start at 2:00 AM
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(2).withMinute(0).withSecond(0);
        
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1); // Schedule for next day if time has passed today
        }
        
        return Duration.between(now, nextRun).toMillis();
    }
}
