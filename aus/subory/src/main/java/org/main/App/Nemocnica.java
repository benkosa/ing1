package org.main.App;

import org.main.hashing.IData;

import java.io.*;
import java.util.BitSet;

public class Nemocnica implements IData {

    private int testData;

    public Nemocnica(int testData) {
        this.testData = testData;
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
        return null;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        try{
            hlpOutStream.writeInt(this.testData);
            return hlpByteArrayOutputStream.toByteArray();
        }catch (IOException e){
            throw new IllegalStateException("Error during conversion to byte array.");
        }
    }

    @Override
    public void fromByteArray(byte[] paArray) {
        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(paArray);
        DataInputStream hlpInStream = new DataInputStream(hlpByteArrayInputStream);

        try {
            this.testData = hlpInStream.readInt();
        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    @Override
    public int getSize() {
        return 0;
    }
}
