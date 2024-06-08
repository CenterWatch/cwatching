package com.cw.services;

import java.util.TimerTask;

public class Timeout extends TimerTask {
    private Boolean isRunning;
    private Double timer;

    private static final Double TIME_MS = 30000.0;

    public Timeout() {
        this.isRunning = false;
        this.timer = TIME_MS;
    }

    @Override
    public void run() {
        if (!isRunning) return;

        timer -= 100;

        if (timer <= 0) {
            timer = TIME_MS;
            isRunning = false;
        }
    }

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }
}
