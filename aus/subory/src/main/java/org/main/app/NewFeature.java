package org.main.app;

import org.main.hashing.IData;
import org.main.shared.ArrayStore;
import org.main.shared.StringStore;

import java.io.*;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;

public class NewFeature extends IData {
    int id;
    int hodnota;
    String popis;
    private final int popisMax = 10;
    String rodneCislo;
    private final int rodneCisloMax = 10;

    public NewFeature() {
        this.id = 0;
        this.hodnota = 0;
        this.popis = "";
        this.rodneCislo = "";

    }

    public NewFeature(int id, String rodneCislo, String popis, int hodnota) {
        this.id = id;
        try {
            this.rodneCislo = rodneCislo.substring(0, rodneCisloMax);
        } catch (StringIndexOutOfBoundsException ex) {
            this.rodneCislo = rodneCislo;
        }
        try {
            this.popis = popis.substring(0, popisMax);
        } catch (StringIndexOutOfBoundsException ex) {
            this.popis = popis;
        }
        this.hodnota = hodnota;
    }

    @Override
    public BitSet getHash() {
        BitSet bId = BitSet.valueOf(new long[]{id});
        BitSet bRc = BitSet.valueOf(rodneCislo.getBytes());

        return concatenate_vectors(bId, bRc);
    }

    BitSet concatenate_vectors(BitSet vector_1_in, BitSet vector_2_in) {
        BitSet vector_1_in_clone = (BitSet)vector_1_in.clone();
        BitSet vector_2_in_clone = (BitSet)vector_2_in.clone();
        int n = 5;//_desired length of the first (leading) vector
        int index = -1;
        while (index < (vector_2_in_clone.length() - 1)) {
            index = vector_2_in_clone.nextSetBit((index + 1));
            vector_1_in_clone.set((index + n));
        }
        return vector_1_in_clone;
    }

    @Override
    public boolean myEqual(Object data) {
        NewFeature feeature = (NewFeature) data;
        return this.id == feeature.id && this.rodneCislo.equals(feeature.rodneCislo);
    }

    @Override
    public Object createClass() {
        return new NewFeature();
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
        StringStore ss = new StringStore();
        try{
            ss.writeString(hlpOutStream, rodneCislo, rodneCisloMax);
            ss.writeString(hlpOutStream, popis, popisMax);
            hlpOutStream.writeInt(this.id);
            hlpOutStream.writeInt(this.hodnota);
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
            this.rodneCislo = ss.loadString(hlpInStream, rodneCisloMax);
            this.popis = ss.loadString(hlpInStream, popisMax);
            this.id = hlpInStream.readInt();
            this.hodnota = hlpInStream.readInt();
        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }

    }

    @Override
    public int getSize() {
        return
                Character.BYTES * (rodneCisloMax + popisMax) + Integer.BYTES *2  //stringy
                        + Integer.BYTES * 2; //id  + hodnota
    }
}
