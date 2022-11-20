package org.main.app;

import org.main.hashing.IData;
import org.main.shared.StringStore;

import java.io.*;
import java.util.BitSet;
import java.util.Date;

public class Hospitalizacia extends IData {

    private int idHospitalizacie;

    public int getIdHospitalizacie() {
        return idHospitalizacie;
    }

    public Date getDatumZaciatku() {
        return datumZaciatku;
    }

    public Date getDatumKonca() {
        return datumKonca;
    }

    public String getDiagnoza() {
        return diagnoza;
    }

    private Date datumZaciatku;
    private Date datumKonca;
    private String diagnoza;
    private final int diagnozaMax = 20;

    public Hospitalizacia(int idHospitalizacie, Date datumZaciatku, Date datumKonca, String diagnoza) {
        this.idHospitalizacie = idHospitalizacie;
        this.datumZaciatku = datumZaciatku;
        this.datumKonca = datumKonca;
        try {
            this.diagnoza = diagnoza.substring(0, diagnozaMax);
        } catch (StringIndexOutOfBoundsException ex) {
            this.diagnoza = diagnoza;
        }
    }

    public Hospitalizacia() {
        this.idHospitalizacie = 0;
        this.datumZaciatku = new Date();
        this.datumKonca = new Date(0);
        this.diagnoza = "";
    }

    @Override
    public String toString() {
        return "Hospitalizacia: " + idHospitalizacie + " | " + datumZaciatku.toString() + " | " + (datumKonca == null ? " " : datumKonca.toString()) + " | " + diagnoza;
    }

    @Override
    public BitSet getHash() {
        return BitSet.valueOf(new long[]{idHospitalizacie});
    }

    @Override
    public boolean myEqual(Object data) {
        Hospitalizacia hosp2 = (Hospitalizacia) data;
        return this.idHospitalizacie == hosp2.idHospitalizacie;
    }

    @Override
    public Object createClass() {
        return new Hospitalizacia(0, new Date(), new Date(0), "");
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
        StringStore ss = new StringStore();
        try{
            ss.writeString(hlpOutStream, diagnoza, diagnozaMax);
            hlpOutStream.writeInt(this.idHospitalizacie);
            hlpOutStream.writeLong(datumZaciatku.getTime());
            hlpOutStream.writeLong(datumKonca == null ? 0 : datumKonca.getTime());
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
            this.diagnoza = ss.loadString(hlpInStream, diagnozaMax);
            this.idHospitalizacie = hlpInStream.readInt();
            this.datumZaciatku = new Date(hlpInStream.readLong());
            this.datumKonca = new Date(hlpInStream.readLong());
            if (this.datumKonca.getTime() == 0) {
                this.datumKonca = null;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    @Override
    public int getSize() {
        return
                Character.BYTES * (diagnozaMax) + Integer.BYTES  //diagnoza
                        + Integer.BYTES //id hospitalizacie
                        + Long.BYTES * 2; //datumy
    }

    public void ukonci(Date endDate) {
        this.datumKonca = endDate;
    }
}
