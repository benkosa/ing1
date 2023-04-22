package _2zadanie;

import shared.EventSimulation.EventSimulationCore;

public interface ISimDelegate {
    /* Metóda určená pre aktualizáciu GUI*/
    void refresh(EventSimulationCore core, String message);
}
