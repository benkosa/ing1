package org.main.dynamic_hashing;

import org.main.hashing.Block;
import org.main.hashing.Hashing;
import org.main.hashing.IData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.PriorityQueue;

public class DynamicHashing<T extends IData> extends Hashing<T> {

    Node root;

    PriorityQueue<Integer> emptyMemoryManager = new PriorityQueue<>();

    public DynamicHashing(String fileName,int blockFactor, Class classType) {
        super(fileName, blockFactor, 1, classType);
        root = new ExternalNode(new BitSet(), -1);
    }

    @Override
    public boolean insert(T data) {

        if (root instanceof ExternalNode) {
            ExternalNode actualExternalNode = ((ExternalNode) root);
            final int adress = bitSetToInt(actualExternalNode.adress);
            Block<T> b = loadBlock(data, adress);

            // blok je plny
            if (b.validCount >= blockFactor) {

                actualExternalNode.incNodeHeight();

                //prerozdelenie
                Block<T> newBlock = new Block<>(blockFactor, data.getClass());
                blockRedistribution(b, newBlock, actualExternalNode.getNodeHeight());

                int newAdress = 0;
                ExternalNode newExternal = null;
                // ak su obra plne
                if (b.validCount > 0 && newBlock.validCount > 0) {
                    increaseFile();
                    newAdress = (numberOfBlocks - 1) * getBlockSize();
                    newExternal = new ExternalNode(
                                    BitSet.valueOf(new long[]{newAdress}),
                                    actualExternalNode.getNodeHeight()
                            );
                } else {
                    //ak je povodny node prazdny
                    if (b.validCount <= 0) {
                        newAdress = adress;
                        newExternal = new ExternalNode(
                                BitSet.valueOf(new long[]{newAdress}),
                                actualExternalNode.getNodeHeight()
                        );
                        actualExternalNode = null;
                    }

                }

                //swap pointers
                final Node newInternal = new InternalNode(
                        actualExternalNode,
                        newExternal
                );

                root = newInternal;

                //zapis
                if (b.validCount > 0) {
                    reWriteBloc(b, adress);
                }
                if (newBlock.validCount > 0) {
                    reWriteBloc(newBlock, newAdress);
                }

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
        Node actualNode;
        if (dataHash.get(actualHeight)) {
            actualNode = root.rightNode;
        } else {
            actualNode = root.leftNode;
        }
        if(actualNode == null) {
            alocateSingleExternal(data, root, actualHeight, dataHash);
            return true;
        }

        while (true) {

            // ak je root externy
            if (actualNode instanceof ExternalNode) {
                ExternalNode actualExternalNode = ((ExternalNode) actualNode);
                final int adress = bitSetToInt(actualExternalNode.adress);
                Block<T> b = loadBlock(data, adress);

                // blok je plny
                if (b.validCount >= blockFactor) {

                    actualExternalNode.incNodeHeight();

                    //prerozdelenie
                    Block<T> newBlock = new Block<>(blockFactor, data.getClass());
                    blockRedistribution(b, newBlock, actualExternalNode.getNodeHeight());

                    int newAdress = 0;
                    ExternalNode newExternal = null;
                    // ak su obra plne
                    if (b.validCount > 0 && newBlock.validCount > 0) {
                        increaseFile();
                        newAdress = (numberOfBlocks - 1) * getBlockSize();
                        newExternal = new ExternalNode(
                                BitSet.valueOf(new long[]{newAdress}),
                                actualExternalNode.getNodeHeight()
                        );
                    } else {

                        //ak je povodny node prazdny
                        if (b.validCount <= 0) {

                            newAdress = adress;
                            newExternal = new ExternalNode(
                                    BitSet.valueOf(new long[]{newAdress}),
                                    actualExternalNode.getNodeHeight()
                            );
                            actualExternalNode = null;
                        }

                    }

                    //swap pointers
                    final Node parent = actualNode.parent;

                    final Node newInternal = new InternalNode(
                            actualExternalNode,
                            newExternal
                    );

                    if (parent.leftNode == actualNode) {
                        parent.leftNode = newInternal;
                    } else {
                        parent.rightNode = newInternal;
                    }
                    newInternal.parent = parent;

                    //zapis
                    if (b.validCount > 0) {
                        reWriteBloc(b, adress);
                    }
                    if (newBlock.validCount > 0) {
                        reWriteBloc(newBlock, newAdress);
                    }

                    //opakujem pridanie
                    actualHeight = 0;
                    if (dataHash.get(actualHeight)) {
                        actualNode = root.rightNode;
                    } else {
                        actualNode = root.leftNode;
                    }
                } else {

                    // kontrola originality kluca
                    if (!b.insert(data)) {
                        return false;
                    }

                    // uspesny zapis
                    reWriteBloc(b, adress);
                    return true;

                }

            // node je null
            } else {
                actualHeight++;
                final Node parent;
                if (dataHash.get(actualHeight)) {
                    parent = actualNode;
                    actualNode = actualNode.rightNode;
                } else {
                    parent = actualNode;
                    actualNode = actualNode.leftNode;
                }

                if(actualNode == null) {
                    alocateSingleExternal(data, parent, actualHeight, dataHash);
                }

            }

        }
    }

    @Override
    public boolean delete(T data) {
        final ExternalNode nodeToRemove = findNode(data);

        final int removeAdress = bitSetToInt(nodeToRemove.adress);
        Block<T> removeBlock = loadBlock(data, removeAdress);
        if (!removeBlock.remove(data)) {
            return false;
        }
        reWriteBloc(removeBlock, removeAdress);
        // mergenut synov
        ExternalNode brotherNode = getBrother(nodeToRemove);
        //oba bratia su listovy
        if (brotherNode != null) {
            final int brotherAdress = bitSetToInt(brotherNode.adress);
            Block<T> brotherBlock = loadBlock(data, brotherAdress);
            // prebehol merge
            if (mergeSons(removeBlock, brotherBlock, removeAdress, brotherAdress)) {
                reWriteBloc(removeBlock, removeAdress);
                reWriteBloc(brotherBlock, brotherAdress);
                // uvolnit blok
                ExternalNode fullNode;
                if (removeBlock.validCount == 0) {
                    this.emptyMemoryManager.add(removeAdress);
                    fullNode = brotherNode;
                } else {
                    this.emptyMemoryManager.add(brotherAdress);
                    fullNode = nodeToRemove;
                }
                // skratit strom
                final Node oldparent = fullNode.parent;
                final Node newParent = fullNode.parent.parent;
                fullNode.parent = newParent;
                if (newParent.rightNode == oldparent) {
                    newParent.rightNode = fullNode;
                } else {
                    newParent.leftNode = fullNode;
                }
                System.out.println("removeAdress: "+removeAdress);
                System.out.println("brotherAdress: "+brotherAdress);
                System.out.println("brotherAdress: "+(((int)fileSize())-getBlockSize()));
                return true;
                //brotherAdress = new
                //ziskat novych bratov
                // ak su obaja listovy pokracovat v cykle
                //else koniec

            }

            //brat je prezdny

        }

//        // blok je prazdny
//        if (b.validCount <= 0) {
//            //  blok je posledny
//            if (bAdress == numberOfBlocks) {
//                //file.
//            }
//
//            // blok nieje posledny pridame do memory managera
//        }

        return false;
    }

    @Override
    public T find(T data) {
        final ExternalNode actualExternalNode = findNode(data);
        final int adress = bitSetToInt(actualExternalNode.adress);
        Block<T> b = loadBlock(data, adress);
        return b.find(data);

    }


    private ExternalNode findNode(T data) {

        if (root instanceof ExternalNode) {
            final ExternalNode actualExternalNode = ((ExternalNode) root);
            final int adress = bitSetToInt(actualExternalNode.adress);
            Block<T> b = loadBlock(data, adress);
            return actualExternalNode;
        }

        final BitSet dataHash = data.getHash();
        int actualHeight = 0;
        Node actualNode;
        if (dataHash.get(actualHeight)) {
            actualNode = root.rightNode;
        } else {
            actualNode = root.leftNode;
        }

        while (true) {

            // ak je root externy
            if (actualNode instanceof ExternalNode) {

                final ExternalNode actualExternalNode = ((ExternalNode) actualNode);


//                actualHeight++;
//                if (dataHash.get(actualHeight)) {
//                    actualNode = actualNode.rightNode;
//                } else {
//                    actualNode = actualNode.leftNode;
//                }
//                if (actualNode != null) {
//                    continue;
//                }

                final int adress = bitSetToInt(actualExternalNode.adress);
                Block<T> b = loadBlock(data, adress);



                return actualExternalNode;

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

    private boolean mergeSons(Block block1, Block block2, int adress1, int adress2) {
        if (block1.validCount + block2.validCount <= blockFactor) {
            final ArrayList<T> records1 =  new ArrayList<>(block1.getRecords());
            final ArrayList<T> records2 =  new ArrayList<>(block2.getRecords());

            //vkladame do block1
            if (adress1 < adress2) {
                for (T t : records2) {
                    block1.insert(t);
                    block2.remove(t);
                }
            //vkladame do block2
            } else {
                for (T t : records1) {
                    block2.insert(t);
                    block1.remove(t);
                }
            }
            return true;
        }
        return false;
    }

    private ExternalNode getBrother(ExternalNode node) {
        Node brotherNode;
        if (node.parent.leftNode == node) {
            brotherNode = node.parent.rightNode;
        } else {
            brotherNode = node.parent.leftNode;
        }
        if (brotherNode instanceof ExternalNode) {
            return (ExternalNode) brotherNode;
        }
        return null;
    }

    private void emptyBlock() {

    }

    private ArrayList<Node> levelOrderNode(Node root) {
        return null;
    }

    private void alocateSingleExternal(T data, Node parent, int actualHeight, BitSet hash) {
        increaseFile();
        final int newAdress = (numberOfBlocks - 1) * getBlockSize();
        Block b = new Block(blockFactor, classType);
        b.insert(data);
        reWriteBloc(b, newAdress);
        Node newNode = new ExternalNode(BitSet.valueOf(new long[]{newAdress}), actualHeight);
        newNode.parent = parent;
        if (hash.get(actualHeight)) {
            parent.rightNode = newNode;
        } else {
            parent.leftNode = newNode;
        }
    }
}
