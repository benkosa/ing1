package org.main.shared.EventSimulation;

import org.main._2zadanie.ISimDelegate;
import org.main._2zadanie.STK;
import org.main.shared.MonteCarlo;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

public abstract class EventSimulationCore extends MonteCarlo implements ISimDelegate {
    private final PriorityQueue<EventSimulation> timeLine = new PriorityQueue<>();

    public double getCurrentTime() {
        return currentTime;
    }

    public void registerDelegate(ISimDelegate delegate) {
        delegates.add(delegate);
    }

    final private List< ISimDelegate > delegates = new ArrayList<>();
    private void refreshGUI() { }

    @Override
    public void refresh(EventSimulationCore stk) {
        if (isLiveMode()) {
            for (ISimDelegate delegate : delegates) {
                delegate.refresh(this);
            }
        }
    }

    private double currentTime;
    private final double maxTime;

    private boolean pause = false;

    public int stepLength = 60;
    public int sleepTime = 1000;

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    @Override
    protected double onePass() {
        simulate();
        return 0;
    }

    public EventSimulationCore(long replications, long maxTime) {
        super(replications);
        this.maxTime = maxTime;
        this.currentTime = 0;
    }

    public void addEvent(EventSimulation event) {
        if (event.eventTime < 0) {
            System.out.println("event time is less than 0");
            return;
        }
        event.eventTime += currentTime;
        this.timeLine.add(event);
    }

    private void sleep(int ms) {
        while (pause) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addStep() {
        if (isLiveMode()) {
            timeLine.add(new RealTimeEvent(stepLength, this, sleepTime));
        }
    }

    public void simulate () {
        addStep();
        while (!timeLine.isEmpty() && currentTime <= maxTime) {
            sleep(1000);
            final EventSimulation event = timeLine.poll();
            this.currentTime = event.eventTime;
            event.execute();
            refresh(null);
        }
        currentTime = 0;
        timeLine.clear();
    }

    public boolean isLiveMode() {
        return stepLength > 0;
    }


}
