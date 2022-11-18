package org.main.app;

import org.main.hashing.IData;
import org.main.shared.StringStore;

import java.io.*;
import java.util.BitSet;
import java.util.Date;

public class Pacient implements IData {
    private String meno;
    private final int menoMax = 15;
    private String priezvisko;
    private final int priezviskoMax = 20;
    private String rodneCislo;
    private final int rodneCisloMax = 10;
    private int poistovna;
    private Date datumNarodenia;

    public Pacient(String meno, String priezvisko, String rodneCislo, int poistovna, Date datumNarodenia) {
        this.poistovna = poistovna;
        this.datumNarodenia = datumNarodenia;
        try {
            this.meno = meno.substring(0, menoMax);
        } catch (StringIndexOutOfBoundsException ex) {
            this.meno = meno;
        }
        try {
            this.priezvisko = priezvisko.substring(0, priezviskoMax);
        } catch (StringIndexOutOfBoundsException ex) {
            this.priezvisko = priezvisko;
        }
        try {
            this.rodneCislo = rodneCislo.substring(0, rodneCisloMax);
        } catch (StringIndexOutOfBoundsException ex) {
            this.rodneCislo = rodneCislo;
        }
    }

    public Pacient() {
        this.poistovna = 0;
        this.meno = "";
        this.priezvisko = "";
        this.rodneCislo = "";
        this.datumNarodenia = new Date();
    }

    @Override
    public String toString() {
        return "Pacient: " + meno + " " + priezvisko + " " + rodneCislo + " " + poistovna + " " + datumNarodenia.toString();
    }


    @Override
    public BitSet getHash() {
        return BitSet.valueOf(rodneCislo.getBytes());
    }

    @Override
    public boolean myEqual(Object data) {
        return false;
    }

    @Override
    public Object createClass() {
        return new Pacient("", "", "", 0, new Date());
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
            hlpOutStream.writeLong(datumNarodenia.getTime());
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
            this.datumNarodenia = new Date(hlpInStream.readLong());
        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    @Override
    public int getSize() {
        return Character.BYTES * (menoMax + priezviskoMax + rodneCisloMax) + Integer.BYTES * 4 + Long.BYTES;
    }
}
