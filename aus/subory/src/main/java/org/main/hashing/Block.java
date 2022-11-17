package org.main.hashing;

import org.main.shared.StringStore;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Block <T extends IData> implements IRecord {
    // pocet zaznamov v bloku
    private final int blockFacktor;
    /**
     * pocet platnych zaznamov v bloku
     * poznacim si ich v bloku
     * prve zazanmy v bloku su vzdy platne
     */
    public int validCount;

    /**
     * v bloku mam pole recrdov, arraylist generickych tried
     */
    private final ArrayList<T> records;
    /**
     * definujem si typ triedy s ktorou budem pracovat
     */
    private final Class<T> classType;

    /**
     * vutvoru novy blok s 0 platnymi recordami
     * @param blockFacktor
     * @param classType
     */
    public Block(int blockFacktor, Class classType) {
        this.blockFacktor = blockFacktor;
        this.classType = classType;

        this.records = new ArrayList<T>(blockFacktor);

        for (int i = 0; i < blockFacktor; i++) {
            try {
                //TODO MUSI BYT JEDNPOARAMETRICKY KONSTRUKTOR DIVNE
                this.records.add((T) this.classType.newInstance().createClass());
            } catch (InstantiationException ex) {
                Logger.getLogger(Block.class.getName())
                        .log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Block.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        validCount = 0;
    }

    /**
     * prida novy record do arraylistu records
     * @param pNew
     * @return
     */
    public boolean insertRecord(T pNew) {

        return true;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlOutputStream = new DataOutputStream(hlpByteArrayOutputStream);

        try{
            hlOutputStream.writeInt(validCount);
            for (int i = 0; i < blockFacktor; i++) {
                hlOutputStream.write(records.get(i).toByteArray());
            }
            return hlpByteArrayOutputStream.toByteArray();
        }catch (IOException e){
            throw new IllegalStateException("Error during conversion to byte array.");
        }

        //TODO hlpByteArrayOutputStream.write(dummy.ToByteArray());
    }

    @Override
    public void fromByteArray(byte[] paArray) {
        // TODO validcount
        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(paArray);
        DataInputStream hlInputStream = new DataInputStream(hlpByteArrayInputStream);
        // load valid count
        try {
            validCount = hlInputStream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // split array to array of only records
        byte[] objectsBytes = Arrays.copyOfRange(
                paArray,
                Integer.BYTES,
                paArray.length
        );

        // load records
        for (int i = 0; i < blockFacktor; i++) {
            byte[] n = Arrays.copyOfRange(
                    objectsBytes,
                    i * records.get(i).getSize(),
                    (i + 1) * records.get(i).getSize()
            );
            records.get(i).fromByteArray(n);
        }

    }

    @Override
    public int getSize() {
        try {
            // velkost class * pocet recordov v bloku + velkost dat na poznacenie
            return classType.newInstance().getSize() * blockFacktor + Integer.BYTES;
        } catch ( InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Block.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ArrayList<T> getRecords() {
        return records;
    }


}
