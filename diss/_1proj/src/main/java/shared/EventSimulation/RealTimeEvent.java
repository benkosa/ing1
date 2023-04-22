package shared.EventSimulation;

public class RealTimeEvent extends EventSimulation {


    @Override
    public void execute() {
        eventTime = myCore.stepLength;
        myCore.addEvent(this);
        try {
            Thread.sleep(myCore.sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public RealTimeEvent(EventSimulationCore myCore) {
        super(myCore.stepLength, myCore);
    }
}
