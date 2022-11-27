package org.main.app;

import org.main.dynamic_hashing.DynamicHashing;
import org.main.hashing.Hashing;

public class Data {
    Hashing<Pacient> hashing;

    //  alocade new staic hahsing
    public Data(String fileName, int blockSize, int blockNumber) {
        hashing = new Hashing<>(fileName, blockSize, blockNumber, Pacient.class);
    }

    // load hashing from file
    public Data(String fileName, boolean isStatic) {
        if (isStatic) {
            hashing = new Hashing<>(fileName, Pacient.class);
        } else {
            hashing = new DynamicHashing<>(fileName, Pacient.class);
        }
    }

    //load dynamic hashing
    public Data(String fileName, int blockSize) {
        hashing = new DynamicHashing<>(fileName, blockSize, Pacient.class);
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
