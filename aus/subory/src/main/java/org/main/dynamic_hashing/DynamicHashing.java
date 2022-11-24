package org.main.dynamic_hashing;

import org.main.hashing.Block;
import org.main.hashing.Hashing;
import org.main.hashing.IData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;

public class DynamicHashing<T extends IData> extends Hashing<T> {

    Node root;
    public DynamicHashing(String fileName,int blockFactor, Class classType) {
        super(fileName, blockFactor, 1, classType);
        root = new ExternalNode(new BitSet(), -1);
    }

    @Override
    public boolean insert(T data) {

        if (root instanceof ExternalNode) {
            final ExternalNode actualExternalNode = ((ExternalNode) root);
            final int adress = bitSetToInt(actualExternalNode.adress);
            Block<T> b = loadBlock(data, adress);

            // blok je plny
            if (b.validCount >= blockFactor) {
                increaseFile();

                actualExternalNode.incNodeHeight();

                final int newAdress = (numberOfBlocks - 1) * getBlockSize();
                final ExternalNode newExternal =
                        new ExternalNode(
                                BitSet.valueOf(new long[]{newAdress}),
                                actualExternalNode.getNodeHeight()
                        );

                final Node newInternal = new InternalNode(
                        actualExternalNode,
                        newExternal
                );

                actualExternalNode.parent = newInternal;
                newExternal.parent = newInternal;
                root = newInternal;

                //prerozdelenie
                Block<T> newBlock = loadBlock(data, newAdress);
                blockRedistribution(b, newBlock, actualExternalNode.getNodeHeight());

                reWriteBloc(b, adress);
                reWriteBloc(newBlock, newAdress);

                //opakujem pridanie
            } else {

                // kontrola originality kluca
                if (!b.insert(data)) {
                    return false;
                }

                // uspesny zapis
                reWriteBloc(b, adress);
                return true;

            }

        }

        final BitSet dataHash = data.getHash();
        int actualHeight = 0;

        while (true) {
            Node actualNode;
            if (dataHash.get(actualHeight)) {
                actualNode = root.rightNode;
            } else {
                actualNode = root.leftNode;
            }
            // ak je root externy
            if (actualNode instanceof ExternalNode) {
                System.out.println("som v externom");
                //if(true)return false;


                final ExternalNode actualExternalNode = ((ExternalNode) actualNode);
                final int adress = bitSetToInt(actualExternalNode.adress);
                Block<T> b = loadBlock(data, adress);

                // blok je plny
                if (b.validCount >= blockFactor) {
                    increaseFile();

                    actualExternalNode.incNodeHeight();

                    final int newAdress = (numberOfBlocks - 1) * getBlockSize();
                    final ExternalNode newExternal =
                            new ExternalNode(
                                    BitSet.valueOf(new long[]{newAdress}),
                                    actualExternalNode.getNodeHeight()
                            );

                    final Node newInternal = new InternalNode(
                            actualExternalNode,
                            newExternal
                    );

                    actualExternalNode.parent = newInternal;
                    newExternal.parent = newInternal;
                    // som lavy
                    if (actualNode.parent.leftNode == actualNode) {
                        actualNode.parent.leftNode = newInternal;
                    // som pravy
                    } else {
                        actualNode.parent.rightNode = newInternal;
                    }
                    actualNode.parent = newInternal;

                    //prerozdelenie
                    Block<T> newBlock = loadBlock(data, newAdress);
                    blockRedistribution(b, newBlock, actualExternalNode.getNodeHeight());

                    reWriteBloc(b, adress);
                    reWriteBloc(newBlock, newAdress);

                    //opakujem pridanie
                } else {

                    // kontrola originality kluca
                    if (!b.insert(data)) {
                        return false;
                    }

                    // uspesny zapis
                    reWriteBloc(b, adress);
                    return true;

                }

            // node je interny
            } else {
                actualHeight++;
                if (dataHash.get(actualHeight)) {
                    actualNode = actualNode.rightNode;
                } else {
                    actualNode = actualNode.leftNode;
                }
            }

        }
    }

    @Override
    public boolean delete(T data) {
        return super.delete(data);
    }

    @Override
    public T find(T data) {
        return super.find(data);
    }


    private void increaseFile() {
        try {
            file.seek(numberOfBlocks*getBlockSize());
            Block<T> emptyBlock = new Block<>(blockFactor, classType);
            file.write(emptyBlock.toByteArray());
            numberOfBlocks++;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void blockRedistribution(Block fullBlock, Block newBlock, int bit) {
        final ArrayList<T> records =  new ArrayList<>(fullBlock.getRecords());

        for (T record : records) {
            if (record.getHash().get(bit) == true) {
                newBlock.insert(record);
                fullBlock.remove(record);
            }
        }
    }
}
