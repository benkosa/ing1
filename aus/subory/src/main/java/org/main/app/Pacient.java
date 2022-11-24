package org.main.app;

import org.main.hashing.IData;
import org.main.shared.ArrayStore;
import org.main.shared.StringStore;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;

public class Pacient extends IData {
    public String getMeno() {
        return meno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public String getRodneCislo() {
        return rodneCislo;
    }

    public int getPoistovna() {
        return poistovna;
    }

    public Date getDatumNarodenia() {
        return datumNarodenia;
    }

    private String meno;
    private final int menoMax = 15;
    private String priezvisko;
    private final int priezviskoMax = 20;
    private String rodneCislo;
    private final int rodneCisloMax = 10;
    private int poistovna;
    private Date datumNarodenia;

    public ArrayList<Hospitalizacia> getHospitalizacie() {
        return hospitalizacie;
    }

    private ArrayList<Hospitalizacia> hospitalizacie= new ArrayList();

    private ArrayList<Hospitalizacia> hospitalizacieNotValid= new ArrayList();

    public int getHospitalizacieMax() {
        return hospitalizacieMax;
    }

    private int hospitalizacieMax = 10;

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
        String ret = "Pacient: " + meno + " | " + priezvisko + " | " + rodneCislo + " | " + poistovna + " | " + datumNarodenia.toString();
//        ret += "\n      valid hosp: " + hospitalizacie.size();
//        for (Hospitalizacia hospitalizacia : hospitalizacieNotValid) {
//            ret += "\n      " + hospitalizacia.toString();
//        }
        return ret;
    }


    @Override
    public BitSet getHash() {
        return BitSet.valueOf(rodneCislo.getBytes());
    }

    @Override
    public boolean myEqual(Object data) {
        Pacient pacient2 = (Pacient) data;
        return this.rodneCislo.equals(pacient2.rodneCislo);
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
        ArrayStore<Hospitalizacia> as = new ArrayStore();
        try{
            ss.writeString(hlpOutStream, meno, menoMax);
            ss.writeString(hlpOutStream, priezvisko, priezviskoMax);
            ss.writeString(hlpOutStream, rodneCislo, rodneCisloMax);

            hlpOutStream.writeInt(this.poistovna);
            hlpOutStream.writeLong(datumNarodenia.getTime());
            as.writeArray(hlpOutStream, hospitalizacie, hospitalizacieMax, new Hospitalizacia());
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
        ArrayStore<Hospitalizacia> as = new ArrayStore();

        try {
            this.meno = ss.loadString(hlpInStream, menoMax);
            this.priezvisko = ss.loadString(hlpInStream, priezviskoMax);
            this.rodneCislo = ss.loadString(hlpInStream, rodneCisloMax);
            this.poistovna = hlpInStream.readInt();
            this.datumNarodenia = new Date(hlpInStream.readLong());
            this.hospitalizacie = as.loadArray(hlpInStream, hospitalizacieMax, new Hospitalizacia());
        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }

        // this part is for testing to store invalid data
        byte[] n = Arrays.copyOfRange(
                paArray,
                Character.BYTES * (menoMax + priezviskoMax + rodneCisloMax) + Integer.BYTES * 3 //stringy
                        + Integer.BYTES //poistovna
                        + Long.BYTES, //datum
                paArray.length
        );

        ByteArrayInputStream hlpByteArrayInputStream2 = new ByteArrayInputStream(n);
        DataInputStream hlpInStream2 = new DataInputStream(hlpByteArrayInputStream2);
        this.hospitalizacieNotValid = as.loadArrayNoValid(hlpInStream2, hospitalizacieMax, new Hospitalizacia());
        //end of testing part

    }

    @Override
    public int getSize() {
        Hospitalizacia emptyHosp = new Hospitalizacia();
        return
                Character.BYTES * (menoMax + priezviskoMax + rodneCisloMax) + Integer.BYTES * 3 //stringy
                        + Integer.BYTES //poistovna
                        + Long.BYTES //datum
                        + hospitalizacieMax * (emptyHosp.getSize()) + Integer.BYTES; //hospitalizacie
    }
}
