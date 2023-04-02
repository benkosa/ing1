package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public interface ISimDelegate {
    /* Metóda určená pre aktualizáciu GUI*/
    void refresh(EventSimulationCore core, String message);
}
