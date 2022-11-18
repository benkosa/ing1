package org.main.hashing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hashing<T extends IData> {

    private int blockFactor;
    private int numberOfBlocks;
    private RandomAccessFile file;
    private Class classType;

    public Hashing( String fileName, int blockFactor, int numberOfBlocks, Class classType, boolean readOnly) {
        this.blockFactor = blockFactor;
        this.numberOfBlocks = numberOfBlocks;
        this.classType = classType;

        // open file
        try {
            this.file = new RandomAccessFile(fileName, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Hashing.class.getName())
                    .log(Level.SEVERE, null, ex);
        }


        // alocate file
        if (!readOnly) {
            try {
                for (int i = 0; i < numberOfBlocks; i++) {
                    Block<T> emptyBlock = new Block<>(blockFactor, classType);
                    file.write(emptyBlock.toByteArray());
                }
            } catch (IOException ex) {
                Logger.getLogger(Hashing.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
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

        // file nacitaj blok
        Block<T> b = new Block<>(blockFactor, data.getClass());
        int adress =(bitSetToInt(data.getHash()) % numberOfBlocks)* b.getSize();

        byte[] blockFile = new byte[b.getSize()];
        try {
            file.seek(adress);
            file.read(blockFile);
            b.fromByteArray(blockFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (b.validCount >= blockFactor) {
            return false;
        }

        // pridaj do recordov a uloz do suboru
        b.insert(data);
        try {
            file.seek(adress);
            file.write(b.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public void readWholeFile() {
        byte[] fullFile;
        int fileLength;
        try {
            fileLength = (int)file.length();
            fullFile = new byte[fileLength];
            file.seek(0);
            file.read(fullFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for (int i = 0; i < numberOfBlocks; i++) {
            final Block<T> newBlock = new Block<>(blockFactor, classType);
            byte[] n = Arrays.copyOfRange(
                    fullFile,
                    i * newBlock.getSize(),
                    (i + 1) * newBlock.getSize()
            );
            newBlock.fromByteArray(n);

            System.out.println("Block: " + newBlock);
            System.out.println("Valid count: " + newBlock.validCount);
            newBlock.getRecords().forEach(a -> System.out.println(a.toString()));
        }
    }


    //https://stackoverflow.com/questions/2473597/bitset-to-and-from-integer-long
    public static int bitSetToInt(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return (int) value;
    }


}
