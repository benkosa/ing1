package org.main.hashing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hashing<T extends IData> {

    private int blockFactor;
    private RandomAccessFile file;

    public Hashing( String fileName, int blockFactor) {
        this.blockFactor = blockFactor;
        try {
            this.file = new RandomAccessFile(fileName, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Hashing.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public T find (T data) {
        Block<T> b;
        BitSet hash = data.getHash();

        //TODO

        b = new Block<>(blockFactor, data.getClass());

        byte[] blockBytes = new byte[b.getSize()];
        try {
            // TODO file seek adresa bloku
            file.read(blockBytes);
        } catch (IOException ex) {
            Logger.getLogger(Hashing.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        b.fromByteArray((blockBytes));

        //TODO kontrola validity prehladavat len validne data
        for (T zaznam: b.getRecords()) {
            if (data.myEqual(zaznam) == true) {
                return zaznam;
            }
        }
        return  null;
    }



    public boolean insert (T data) {
        //TODO file najdi adresu bloku
        // file nacitaj blok
        Block<T> b = new Block<>(blockFactor, data.getClass());
        try {
            file.write(data.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(Hashing.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public boolean delete (T data) {
        Block<T> b;
        b = new Block<>(blockFactor, data.getClass());

        return true;
    }

    public long fileSize() {
        try {
            return file.length();
        } catch (IOException ex) {
            Logger.getLogger(Hashing.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return -1;
    }


}
