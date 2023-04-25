package _2zadanie;

import shared.EventSimulation.EventSimulationCore;

public interface ISimDelegate<T> {
    /* Metóda určená pre aktualizáciu GUI*/
    void refresh(T core, String message);
}
