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
                    if(actualNode == null) {
                        alocateSingleExternal(data, root, actualHeight, dataHash);
                        return true;
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


            }  else {
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
                    return true;
                }

            }

        }
    }

    @Override
    public boolean delete(T data) {
        //hladany element neexistuje
        if (find(data) == null) {
            return false;
        }
        final ExternalNode nodeToRemove = findNode(data);

        final int removeAdress = bitSetToInt(nodeToRemove.adress);
        Block<T> removeBlock = loadBlock(data, removeAdress);
        //kluc sa nenasiel
        if (!removeBlock.remove(data)) {
            return false;
        }
        reWriteBloc(removeBlock, removeAdress);

        Node brotherNode = getBrother(nodeToRemove);
        //nema brata je listovy
        if (brotherNode == null) {
            //blok je prazdny
            if (removeBlock.validCount <= 0) {
                // skratit strom o prazdne internal
                manageMemory(removeAdress);
                // node is nulll node is root
//                if (nodeToRemove.parent.parent == null) {
//                    root = nodeToRemove;
//                    nodeToRemove.parent = null;
//                    return true;
//                }

                Node node;
                for ( node = nodeToRemove;
                     (node.parent instanceof InternalNode) && getBrother(node.parent) == null;
                     node = node.parent);

                if (node == root) {
                    root = nodeToRemove;
                    nodeToRemove.parent = null;
                    return true;
                }

                if (node == nodeToRemove) {
                    if (getBrother(node.parent) != null) {
                        node = node.parent;

                        if (node.parent.leftNode == node) {
                            node.parent.leftNode = nodeToRemove;
                        } else {
                            node.parent.rightNode = nodeToRemove;
                        }
                        nodeToRemove.parent = node.parent;
                    return true;
                    }
                }


                if (node.parent.leftNode == node) {
                    node.parent.leftNode = nodeToRemove;
                } else {
                    node.parent.rightNode = nodeToRemove;
                }

                return true;
            // v bloku este nieco je
            } else {
                return true;
            }
        }
        //brat je internal
        if (brotherNode instanceof InternalNode) {
            //blok je prazdny
            manageMemory(removeAdress);
            if (removeBlock.validCount <= 0) {
                if (nodeToRemove.parent.leftNode == nodeToRemove) {
                    nodeToRemove.parent.leftNode = null;
                } else {
                    nodeToRemove.parent.rightNode = null;
                }
                return true;
            }
            //v bloku este nieco je
            return true;
        }
        //oba bratia su external
        if (brotherNode instanceof ExternalNode) {
            ExternalNode externalBrotherNode = (ExternalNode)brotherNode;
            final int brotherAdress = bitSetToInt(externalBrotherNode.adress);
            Block<T> brotherBlock = loadBlock(data, brotherAdress);

            // prebehol merge
            if (mergeSons(removeBlock, brotherBlock, removeAdress, brotherAdress)) {
                reWriteBloc(removeBlock, removeAdress);
                reWriteBloc(brotherBlock, brotherAdress);
                // uvolnit blok
                ExternalNode fullNode;
                if (removeBlock.validCount == 0) {
                    manageMemory(removeAdress);
                    fullNode = externalBrotherNode;

                    //vymazat pointer na prazdneho brata
                    if (nodeToRemove.parent.leftNode == nodeToRemove) {
                        nodeToRemove.parent.leftNode = null;
                    } else {
                        nodeToRemove.parent.rightNode = null;
                    }
                } else {
                    manageMemory(brotherAdress);
                    fullNode = nodeToRemove;

                    //vymazat pointer na prazdneho brata
                    if (brotherNode.parent.leftNode == brotherNode) {
                        brotherNode.parent.leftNode = null;
                    } else {
                        brotherNode.parent.rightNode = null;
                    }
                }

                //skartit strom o prazdne internal

                // node is nulll node is root
//                if (fullNode.parent.parent == null) {
//                    root = fullNode;
//                    fullNode.parent = null;
//                    return true;
//                }

                Node node;
                for ( node = fullNode;
                      (node.parent instanceof InternalNode) && getBrother(node.parent) == null;
                      node = node.parent);

                if (node == root) {
                    root = fullNode;
                    fullNode.parent = null;
                    return true;
                }

                if (node.parent.leftNode == node) {
                    node.parent.leftNode = fullNode;
                } else {
                    node.parent.rightNode = fullNode;
                }
                fullNode.parent = node.parent;
                return true;

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
        if (actualExternalNode == null) {
            return null;
        }
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
                // element neexistuje
                if (actualNode == null) {
                    return null;
                }
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

    private Node getBrother(Node node) {
        if(node.parent == null) {
            if (root.leftNode == node) {
                return root.rightNode;
            } else {
                return root.leftNode;
            }
        }

        Node brotherNode;
        if (node.parent.leftNode == node) {
            brotherNode = node.parent.rightNode;
        } else {
            brotherNode = node.parent.leftNode;
        }
        return brotherNode;
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

    public void manageMemory(int adress) {
        long lastElement = fileSize()-getBlockSize();

        //posledny blok nemazeme
        if (fileSize() == lastElement) {
            return;
        }
        //nachadzame sa na konci subou
        if (lastElement == adress) {
            int count = 1;
            while (emptyMemoryManager.remove(adress-(count*getBlockSize()))) {
                count++;
                numberOfBlocks--;
            }
            try {
                file.setLength(fileSize()-(count*getBlockSize()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else emptyMemoryManager.add(adress);

    }
}
