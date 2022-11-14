package org.main.hashing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    private final int validCount;

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
            //TODO treba upravit aby to pridalo do araylistu len valid count
            // TODO ziskat valid count z bloku
            try {
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

        //TODO hlpByteArrayOutputStream.write(dummy.ToByteArray());
        return new byte[0];
    }

    @Override
    public void fromByteArray(byte[] paArray) {
        //TODO validcount
        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(paArray);
        DataInputStream hlInputStream = new DataInputStream(hlpByteArrayInputStream);

        for (int i = 0; i < blockFacktor; i++) {
            byte[] n = Arrays.copyOfRange(
                    paArray,
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
