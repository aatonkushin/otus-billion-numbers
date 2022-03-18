package org.tonkushin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Stopwatch {
    private LocalDateTime start;
    private long elapsedTime=0;

    public void start(){
        start = LocalDateTime.now();
    }

    public void stop(){
        elapsedTime = ChronoUnit.MILLIS.between(start, LocalDateTime.now());
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public String toString() {
        return elapsedTime + " ms";
    }
}
