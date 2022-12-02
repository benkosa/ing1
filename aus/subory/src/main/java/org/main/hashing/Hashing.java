package org.main.hashing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hashing<T extends IData> {

    protected int blockFactor;
    protected int numberOfBlocks;
    protected RandomAccessFile file;
    protected Class classType;

    protected String fileName;

    public int getBlockSize() {
        return blockSize;
    }

    private int blockSize = 0;

    public Hashing( String fileName, int blockFactor, int numberOfBlocks, Class classType) {
        this.fileName = fileName;
        this.blockFactor = blockFactor;
        this.numberOfBlocks = numberOfBlocks;
        this.classType = classType;
        this.blockSize = new Block<>(blockFactor, classType).getSize();

        // open file
        try {
            this.file = new RandomAccessFile(fileName, "rw");
            file.setLength(0);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Hashing.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // alocate file
        try {
            for (int i = 0; i < numberOfBlocks; i++) {
                Block<T> emptyBlock = new Block<>(blockFactor, classType);
                file.write(emptyBlock.toByteArray());
            }
            file.writeInt(blockFactor);
            file.writeInt(numberOfBlocks);
        } catch (IOException ex) {
            Logger.getLogger(Hashing.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public Hashing( String fileName, Class classType) {
        this.fileName = fileName;
        this.classType = classType;
        // open file
        try {
            this.file = new RandomAccessFile(fileName, "rw");
            file.seek(fileSize()-Integer.BYTES*2);
            this.blockFactor = file.readInt();
            this.numberOfBlocks = file.readInt();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Hashing.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected Hashing(String fileName, Class classType, boolean isDynamic) {
        this.fileName = fileName;
        this.classType = classType;
        // open file
        try {

            this.file = new RandomAccessFile(fileName, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Hashing.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    public T find (T data) {
        // file nacitaj blok
        int adress =(bitSetToInt(data.getHash()) % numberOfBlocks)* blockSize;
        return loadBlock(data, adress).find(data);
    }



    public boolean insert (T data) {

        // file nacitaj blok
        int adress =(bitSetToInt(data.getHash()) % numberOfBlocks)* blockSize;
        Block<T> b = loadBlock(data, adress);

        if (b.validCount >= blockFactor) {
            return false;
        }

        // pridaj do recordov a uloz do suboru
        if (!b.insert(data)) {
            return false;
        }
        reWriteBloc(b, adress);
        return true;
    }

    public boolean delete (T data) {
        // vymaz z records

        int adress =(bitSetToInt(data.getHash()) % numberOfBlocks)* blockSize;
        Block<T> b = loadBlock(data, adress);
        if (!b.remove(data)) {
            return false;
        }

        //sync so suborom
        reWriteBloc(b, adress);
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

    /**
     * write all vlid data to console
     */
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

    /**
     * write every data to console even invalid data
     */
    public void readWholeFileNoValid() {
        byte[] fullFile;
        int fileLength;
        try {
            fileLength = (int)file.length();
            fullFile = new byte[fileLength];
            file.seek(0);
            file.read(fullFile);

            System.out.println("dlzka sunboru: " + file.length());
            System.out.println("pocet_blokov * velkost_bloku: " + numberOfBlocks*blockSize);
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
            newBlock.getNoValidRecords().forEach(a -> System.out.println(a.toString()));
        }
    }

    public ArrayList<T> getWholeFile() {
        ArrayList<T> ret = new ArrayList<>();
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

            ret.addAll(newBlock.getRecords());
        }

        return ret;
    }


    //https://stackoverflow.com/questions/2473597/bitset-to-and-from-integer-long
    public static int bitSetToInt(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return (int) value;
    }

    protected Block<T> loadBlock(T data, int adress) {
        // file nacitaj blok
        Block<T> b = new Block<>(blockFactor, data.getClass());

        byte[] blockFile = new byte[b.getSize()];
        try {
            file.seek(adress);
            file.read(blockFile);
            b.fromByteArray(blockFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    protected void reWriteBloc(Block b, int adress) {
        try {
            file.seek(adress);
            file.write(b.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
