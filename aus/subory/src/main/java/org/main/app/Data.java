package org.main.app;

import org.main.hashing.Hashing;

public class Data {
    Hashing<Pacient> hashing;

    public Data(String fileName, int blockSize, int blockNumber) {
        hashing = new Hashing<>(fileName, blockSize, blockNumber, Pacient.class);
    }

    public Data(String fileName) {
        hashing = new Hashing<>(fileName, Pacient.class);
    }
    public boolean addPacient(Pacient pacient) {
        return hashing.insert(pacient);
    }

    public boolean removePacient(Pacient pacient) {
        return hashing.delete(pacient);
    }

    public Pacient getPacient(Pacient pacient) {
        return hashing.find(pacient);
    }

}
