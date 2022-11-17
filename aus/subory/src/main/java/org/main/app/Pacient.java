package org.main.app;

import org.main.hashing.IData;
import org.main.shared.StringStore;

import java.io.*;
import java.util.BitSet;

public class Pacient implements IData {
    private String meno;
    private int menoMax = 15;
    private String priezvisko;
    private int priezviskoMax = 20;
    private String rodneCislo;
    private int rodneCisloMax = 10;
    private int poistovna;

    public Pacient(String meno, String priezvisko, String rodneCislo, int poistovna) {
        this.poistovna = poistovna;
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.rodneCislo = rodneCislo;
    }

    public Pacient() {
        this.poistovna = 0;
        this.meno = "";
        this.priezvisko = "";
        this.rodneCislo = "";
    }

    @Override
    public String toString() {
        return "Pacient: " + meno + " " + priezvisko + " " + rodneCislo + " " + poistovna;
    }


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
        return new Pacient("meno", "priezvisko", "rodnecislo", 0);
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
        StringStore ss = new StringStore();
        try{
            ss.writeString(hlpOutStream, meno, menoMax);
            ss.writeString(hlpOutStream, priezvisko, priezviskoMax);
            ss.writeString(hlpOutStream, rodneCislo, rodneCisloMax);

            hlpOutStream.writeInt(this.poistovna);
            return hlpByteArrayOutputStream.toByteArray();
        }catch (IOException e){
            throw new IllegalStateException("Error during conversion to byte array.");
        }
    }

    @Override
    public void fromByteArray(byte[] paArray) {
        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(paArray);
        DataInputStream hlpInStream = new DataInputStream(hlpByteArrayInputStream);
        StringStore ss = new StringStore();

        try {
            this.meno = ss.loadString(hlpInStream, menoMax);
            this.priezvisko = ss.loadString(hlpInStream, priezviskoMax);
            this.rodneCislo = ss.loadString(hlpInStream, rodneCisloMax);
            this.poistovna = hlpInStream.readInt();
        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    @Override
    public int getSize() {
        return Character.BYTES * (menoMax + priezviskoMax + rodneCisloMax) + Integer.BYTES * 4;
    }
}
