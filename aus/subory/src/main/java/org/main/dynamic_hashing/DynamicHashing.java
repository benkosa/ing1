package org.main.dynamic_hashing;

import org.main.app.NodeMap;
import org.main.bst.BSTree;
import org.main.hashing.Block;
import org.main.hashing.Hashing;
import org.main.hashing.IData;
import org.main.shared.Tokens;

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.PriorityQueue;
import java.util.Scanner;

public class DynamicHashing<T extends IData> extends Hashing<T> {

    Node root;

    PriorityQueue<Long> emptyMemoryManager = new PriorityQueue<>();

    public DynamicHashing(String fileName,int blockFactor, Class classType) {
        super(fileName, blockFactor, 1, classType);
        root = new ExternalNode(new BitSet());
    }

    public DynamicHashing(String fileName, Class classType) {
        super(fileName, classType, true);
        this.loadTree();
    }

    @Override
    public boolean insert(T data) {

        if (root instanceof ExternalNode) {
            ExternalNode actualExternalNode = ((ExternalNode) root);
            final long adress = bitSetToLong(actualExternalNode.adress);
            Block<T> b = loadBlock(data, adress);

            // blok je plny
            if (b.validCount >= blockFactor) {


                //prerozdelenie
                Block<T> newBlock = new Block<>(blockFactor, data.getClass());
                blockRedistribution(b, newBlock, 0);

                long newAdress = 0;
                ExternalNode newExternal = null;
                // ak su obra plne
                if (b.validCount > 0 && newBlock.validCount > 0) {
                    increaseFile();
                    newAdress = (numberOfBlocks - 1) * getBlockSize();
                    newExternal = new ExternalNode(
                                    BitSet.valueOf(new long[]{newAdress})
                            );
                } else {
                    //ak je povodny node prazdny
                    if (b.validCount <= 0) {
                        newAdress = adress;
                        newExternal = new ExternalNode(
                                BitSet.valueOf(new long[]{newAdress})
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

            // ak je node externy
            if (actualNode instanceof ExternalNode) {
                ExternalNode actualExternalNode = ((ExternalNode) actualNode);
                final long adress = bitSetToLong(actualExternalNode.adress);
                Block<T> b = loadBlock(data, adress);

                // blok je plny
                if (b.validCount >= blockFactor) {

                    actualHeight++;
                    //actualExternalNode.incNodeHeight();

                    //prerozdelenie
                    Block<T> newBlock = new Block<>(blockFactor, data.getClass());
                    blockRedistribution(b, newBlock, actualHeight);

                    long newAdress = 0;
                    ExternalNode newExternal = null;
                    // ak su obra plne
                    if (b.validCount > 0 && newBlock.validCount > 0) {
                        increaseFile();
                        newAdress = (numberOfBlocks - 1) * getBlockSize();
                        newExternal = new ExternalNode(
                                BitSet.valueOf(new long[]{newAdress})
                        );
                    } else {

                        //ak je povodny node prazdny
                        if (b.validCount <= 0) {

                            newAdress = adress;
                            newExternal = new ExternalNode(
                                    BitSet.valueOf(new long[]{newAdress})
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

        final long removeAdress = bitSetToLong(nodeToRemove.adress);
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

                //skratit strom o prazdne internalnodes
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

                        //if (node.parent.parent != null) node = node.parent;

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
            if (removeBlock.validCount <= 0) {
                manageMemory(removeAdress);
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
            final long brotherAdress = bitSetToLong(externalBrotherNode.adress);
            Block<T> brotherBlock = loadBlock(data, brotherAdress);

            // prebehol merge
            if (mergeSons(removeBlock, brotherBlock, removeAdress, brotherAdress)) {
                reWriteBloc(removeBlock, removeAdress);
                reWriteBloc(brotherBlock, brotherAdress);
                // uvolnit blok
                ExternalNode fullNode;
                Block fullBlock;
                if (removeBlock.validCount == 0) {
                    manageMemory(removeAdress);
                    fullNode = externalBrotherNode;
                    fullBlock = brotherBlock;

                    //vymazat pointer na prazdneho brata
                    if (nodeToRemove.parent.leftNode == nodeToRemove) {
                        nodeToRemove.parent.leftNode = null;
                    } else {
                        nodeToRemove.parent.rightNode = null;
                    }
                } else {
                    manageMemory(brotherAdress);
                    fullNode = nodeToRemove;
                    fullBlock = removeBlock;

                    //vymazat pointer na prazdneho brata
                    if (brotherNode.parent.leftNode == brotherNode) {
                        brotherNode.parent.leftNode = null;
                    } else {
                        brotherNode.parent.rightNode = null;
                    }
                }

                //skartit strom o prazdne internal len ak je aj druhy brat prazdny
                if (fullBlock.validCount == 0) {
                    //sk je parent root
                    if (fullNode.parent == root) {
                        root = fullNode;
                        fullNode.parent = null;
                    }

                    //najst node na skratenie
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


                Node tmpParetn = fullNode.parent;

                if (tmpParetn.parent == null) {
                    root = fullNode;
                    fullNode.parent = null;
                    return true;
                }

                if (tmpParetn.parent.leftNode == tmpParetn) {
                    tmpParetn.parent.leftNode = fullNode;
                } else {
                    tmpParetn.parent.rightNode = fullNode;
                }
                fullNode.parent = tmpParetn.parent;
                return true;
            }
        }
        return true;
    }

    @Override
    public T find(T data) {
        final ExternalNode actualExternalNode = findNode(data);
        if (actualExternalNode == null) {
            return null;
        }
        final long adress = bitSetToLong(actualExternalNode.adress);
        Block<T> b = loadBlock(data, adress);
        return b.find(data);

    }


    private ExternalNode findNode(T data) {

        if (root instanceof ExternalNode) {
            final ExternalNode actualExternalNode = ((ExternalNode) root);
            final long adress = bitSetToLong(actualExternalNode.adress);
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

                final long adress = bitSetToLong(actualExternalNode.adress);
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

    private boolean mergeSons(Block block1, Block block2, long adress1, long adress2) {
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

    private void alocateSingleExternal(T data, Node parent, int actualHeight, BitSet hash) {
        long newAdress;

        if (emptyMemoryManager.size() != 0) {
            newAdress = emptyMemoryManager.poll();
        } else {
            increaseFile();
            newAdress = (numberOfBlocks - 1) * getBlockSize();
        }


        Block b = new Block(blockFactor, classType);
        b.insert(data);
        reWriteBloc(b, newAdress);
        Node newNode = new ExternalNode(BitSet.valueOf(new long[]{newAdress}));
        newNode.parent = parent;
        if (hash.get(actualHeight)) {
            parent.rightNode = newNode;
        } else {
            parent.leftNode = newNode;
        }
    }

    public void manageMemory(long adress) {
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
            }
            numberOfBlocks-=count;
            try {
                file.setLength(fileSize()-(count*getBlockSize()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else emptyMemoryManager.add(adress);

    }

    public void saveTree() {
        ArrayList<Node> nodes = levelOrderNodes();

        if (nodes != null && nodes.size() == 0) {
            return;
        }

        String path = fileName+".tree";
        //delete file content
        try {
            PrintWriter pw = new PrintWriter(path);
            pw.close();
        } catch(IOException e) {}

        try (FileWriter fstream = new FileWriter(path);
             BufferedWriter info = new BufferedWriter(fstream)) {
            for (Node node : nodes) {
                if (node instanceof InternalNode) {
                    info.write(String.format(
                            "i;"+
                            Integer.toHexString(node.hashCode())+";"+
                            (node.parent == null ? "null" : Integer.toHexString(node.parent.hashCode()))+";"+
                            (node.leftNode == null ? "null" : Integer.toHexString(node.leftNode.hashCode()))+";"+
                            (node.rightNode == null ? "null" : Integer.toHexString(node.rightNode.hashCode()))
                            +"%n"));
                } else if (node instanceof ExternalNode) {
                    ExternalNode exNode = (ExternalNode) node;
                    info.write(String.format(
                            "e;"+
                            Integer.toHexString(exNode.hashCode())+";"+
                            (exNode.parent == null ? "null" : Integer.toHexString(exNode.parent.hashCode()))+";"+
                            (exNode.leftNode == null ? "null" : Integer.toHexString(exNode.leftNode.hashCode()))+";"+
                            (exNode.rightNode == null ? "null" : Integer.toHexString(exNode.rightNode.hashCode()))+";"+
                            bitSetToLong(exNode.adress)
                            +"%n"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream f = new FileOutputStream(new File(fileName+".block"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeInt(blockFactor);
            o.writeInt(numberOfBlocks);
            o.writeInt(emptyMemoryManager.size());
            for (Long aLong : emptyMemoryManager) {
                o.writeLong(aLong);
            }

            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found write");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    public void loadTree() {
        ArrayList<NodeMap> loadedNodes = new ArrayList<>();
        BSTree<String> tree = new BSTree<>();
        Scanner sc = null;
        try {
            File file = new File(fileName + ".tree"); // java.io.File
            sc = new Scanner(file);     // java.util.Scanner
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String tokens[] = line.split(";");
                if (tokens[Tokens.TYPE.ordinal()].equals("i")) {
                    InternalNode node = new InternalNode(null, null);
                    NodeMap loadedNode = new NodeMap(node, tokens);
                    tree.insert(loadedNode);
                    loadedNodes.add(loadedNode);
                } else if (tokens[Tokens.TYPE.ordinal()].equals("e")) {
                    long adres = Long.parseLong(tokens[Tokens.ADDRESS.ordinal()]);
                    ExternalNode node = new ExternalNode(BitSet.valueOf(new long[]{adres}));
                    NodeMap loadedNode = new NodeMap(node, tokens);
                    tree.insert(loadedNode);
                    loadedNodes.add(loadedNode);
                }
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally {
            if (sc != null) sc.close();
        }


        this.root = loadedNodes.get(0).node;

        for (NodeMap loadedNode : loadedNodes) {
            if (!loadedNode.tokens[Tokens.PARENT.ordinal()].equals("null")) {
                loadedNode.node.parent =
                        ((NodeMap)tree.find(loadedNode.tokens[Tokens.PARENT.ordinal()])).node;
            }
            if (!loadedNode.tokens[Tokens.LEFT.ordinal()].equals("null")) {
                loadedNode.node.leftNode =
                        ((NodeMap)tree.find(loadedNode.tokens[Tokens.LEFT.ordinal()])).node;
            }
            if (!loadedNode.tokens[Tokens.RIGHT.ordinal()].equals("null")) {
                loadedNode.node.rightNode =
                        ((NodeMap)tree.find(loadedNode.tokens[Tokens.RIGHT.ordinal()])).node;
            }
        }


        try {

            FileInputStream fi = new FileInputStream(new File(fileName+".block"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            this.blockFactor = oi.readInt();
            this.numberOfBlocks = oi.readInt();
            int size = oi.readInt();
            for (int i = 0; i < size; i++) {
                this.emptyMemoryManager.add(oi.readLong());
            }

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found read");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    private ArrayList<Node> levelOrderNodes () {
        if (root == null) {
            return null;
        }
        final ArrayList<Node> levelOrderList = new ArrayList<>();
        int startIndex = 0;
        int endIndex = 1;
        int newEndIndex = 1;
        levelOrderList.add(root);
        // while new elements were added to list
        while (startIndex != newEndIndex) {
            // cycle through new elements
            for (; startIndex < endIndex; startIndex++) {
                final Node rightNode = levelOrderList.get(startIndex).rightNode;
                final Node leftNode = levelOrderList.get(startIndex).leftNode;
                if (leftNode != null) {
                    levelOrderList.add(leftNode);
                    newEndIndex++;
                }
                if (rightNode != null) {
                    levelOrderList.add(rightNode);
                    newEndIndex++;
                }
            }
            endIndex = newEndIndex;
        }
        return levelOrderList;
    }
}
