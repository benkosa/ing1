package org.main.app;

import org.main.hashing.IData;

import java.util.BitSet;

// • id hospitalizácie – neunikátne náhodné celé kladné číslo,
//      ktoré je unikátne pre pacienta
// • dátum začiatku hospitalizácie
// • dátum konca hospitalizácie
// • diagnózu s ktorou bol prijatý – reťazec [max. 20 znakov]
public class Hospitalizacia implements IData {

    @Override
    public BitSet getHash() {
        return null;
    }

    @Override
    public boolean myEqual(Object data) {
        return false;
    }

    @Override
    public Object createClass() {
        return null;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public void fromByteArray(byte[] paArray) {

    }

    @Override
    public int getSize() {
        return 0;
    }
}
