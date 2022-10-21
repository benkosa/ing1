package org.example;

import org.example.Shared.Compare;

import java.util.ArrayList;
import java.util.Objects;

public class BSTree<T> {

    private BSNode<T> root;

    public BSData<T> getRoot() {
        return root == null ? null : root.data;
    }

    public BSNode<T> getRootNode() {
        return root;
    }

    /**
     * iterative insert
     * @param incomingNode incomingNode
     */
    public boolean insert(BSData<T> incomingNode) {
        if (root == null) {
            root = new BSNode<>(incomingNode);
            return true;
        }
        BSNode<T>currentNode = root;

        while (true) {
            //no duplicates
            final Compare compareResult = currentNode.data.compare(incomingNode);
            if (compareResult == Compare.EQUAL) {
                return false;
            //leftNode
            } else if (compareResult == Compare.LESS) {
                if (currentNode.leftNode == null) {
                    currentNode.leftNode = new BSNode<>(incomingNode);
                    currentNode.leftNode.parent = currentNode;
                    this.improve(currentNode);
                    return true;
                } else {
                    currentNode = currentNode.leftNode;
                }
            //rightNode
            } else if (compareResult == Compare.MORE) {
                if (currentNode.rightNode == null) {
                    currentNode.rightNode = new BSNode<>(incomingNode);
                    currentNode.rightNode.parent = currentNode;
                    this.improve(currentNode);
                    return true;
                } else {
                    currentNode = currentNode.rightNode;
                }
            }
        }
    }

    /**
     * used in insert method after insertion
     * look at parent of inserted node. if imbalance is found
     * make improvement
     *
     * @param node node
     */
    public void improve(BSNode<T> node) {
        if (node.parent == null || node.parent.parent == null) {
            return;
        }
        final BSNode<T> pivot = node.parent;
        final BSNode<T> root = pivot.parent;

        // if pivot and root has only one child
        if ((pivot.rightNode == null ^ pivot.leftNode == null) &&
                (root.rightNode == null ^ root.leftNode == null)) {
            if (pivot.rightNode!= null) {
                this.leftRotation(pivot);
            } else {
                this.rightRotation(pivot);
            }
        }
    }

    /**
     * insert array of elements into tree and create perfect tree
     *
     * @param elementsList unsorted list of elements
     * @return number of inserted elements
     */
    public int insertMultiple(ArrayList<BSData<T>> elementsList) {
        elementsList.sort((o1, o2) -> switch (o1.compare(o2)) {
            case LESS -> 1;
            case MORE -> -1;
            default -> 0;
        });

        ArrayList<Integer> medians = getMediansIndexes(elementsList.size());

        int countInserted = 0;
        for (int median: medians ) {
            countInserted += insert(elementsList.get(median)) ? 1 : 0;
        }
        return countInserted;
    }

    /**
     * used to find data by key
     * @param searchKey T
     * @return data
     */
    public BSData<T> find(T searchKey) {
        final BSNode<T> element = this.findNode(searchKey);
        return element == null ? null : element.data;
    }

    /**
     * used to remove node from BSTree
     * @param key T
     * @return removed data
     */
    public BSData<T> remove(T key) {
        final BSNode<T> nodeToRemove = this.findNode(key);
        if (nodeToRemove == null) {
            return null;
        }
        //both children null
        if (bothChildNull(nodeToRemove)) return nodeToRemove.data;

        //one child null
        if (oneChildNull(nodeToRemove)) return nodeToRemove.data;

        //both children
        final BSNode<T> inorderSuccessor = findInorderPredecessor(nodeToRemove);
        bothChildNull(inorderSuccessor);
        oneChildNull(inorderSuccessor);
        final BSData<T> retData = nodeToRemove.data;
        nodeToRemove.data = inorderSuccessor.data;
        return retData;
    }

    /**
     * iterative level order
     * @return level order array of data
     */
    public ArrayList<BSData<T>> levelOrder() {
        return getOrderData(levelOrderNodes());
    }

    /**
     * iterative in order
     * @return in order list of data
     */
    public ArrayList<BSData<T>> inOrder() {
        return getOrderData(inOrderNodes());
    }

    public void leftRotation(BSNode<T> root) {
        if (root.rightNode == null) {
            return;
        }
        final BSNode<T> pivot = root.rightNode;
        final BSNode<T> A = pivot.rightNode;
        final BSNode<T> B = pivot.leftNode;
        final BSNode<T> C = root.leftNode;

        BSData<T> tmpRootData = root.data;
        root.data = pivot.data;
        pivot.data = tmpRootData;
        root.leftNode = pivot;
        if(A != null) A.parent = root;
        if(C != null) C.parent = pivot;
        root.rightNode = A;
        pivot.rightNode = B;
        pivot.leftNode = C;
    }

    public void rightRotation(BSNode<T> root) {
        if (root.leftNode == null) {
            return;
        }
        final BSNode<T> pivot = root.leftNode;
        final BSNode<T> A = pivot.leftNode;
        final BSNode<T> B = pivot.rightNode;
        final BSNode<T> C = root.rightNode;

        BSData<T> tmpRootData = root.data;
        root.data = pivot.data;
        pivot.data = tmpRootData;
        if(C != null) C.parent = pivot;
        if(A != null) A.parent = root;
        root.rightNode = pivot;
        root.leftNode = A;
        pivot.leftNode = B;
        pivot.rightNode = C;
    }

    /**
     * balance tree
     * - get array of all medians
     * - find median in tree and rotate him to position
     */
    public void balanceTree() {
        ArrayList<BSData<T>> inOrderData = inOrder();
        boolean mark = !root.isVisited;
        int median = inOrderData.size()/2;
        BSNode<T> nodeToBubble = findNode(inOrderData.get(median).key);

        if (nodeToBubble == null) return;

        //move median to root
        for (; nodeToBubble.parent != null; nodeToBubble = nodeToBubble.parent) {
            if (nodeToBubble.parent.leftNode == nodeToBubble) {
                this.rightRotation(nodeToBubble.parent);
            } else {
                this.leftRotation(nodeToBubble.parent);
            }
        }

        nodeToBubble.isVisited = mark;

        ArrayList<Integer> medians = getMediansIndexes(inOrderData.size());

        //bubble all others nodes until node reach balanced node
        for (Integer selectedMedian: medians) {
            nodeToBubble = findNode(inOrderData.get(selectedMedian).key);
            if (nodeToBubble == null) return;
            if (nodeToBubble.isVisited != mark) {
                for (; nodeToBubble.parent.isVisited != mark; nodeToBubble = nodeToBubble.parent) {
                    if (nodeToBubble.parent.leftNode == nodeToBubble) {
                        this.rightRotation(nodeToBubble.parent);
                    } else {
                        this.leftRotation(nodeToBubble.parent);
                    }
                }
                nodeToBubble.isVisited = mark;
            }
        }

        //tidy visited
        Objects.requireNonNull(
                levelOrderNodes()
        ).forEach(a -> a.isVisited = false);

    }

    /**
     * create array of data from array of nodes
     * @param sortedNodes array of nodes
     * @return array of data
     */
    private ArrayList<BSData<T>> getOrderData(ArrayList<BSNode<T>> sortedNodes) {
        if (sortedNodes == null) return null;
        final ArrayList<BSData<T>> sortedData = new ArrayList<>(sortedNodes.size());
        sortedNodes.forEach(n -> sortedData.add(n.data));
        return sortedData;
    }

    /**
     * @return in order list of nodes
     */
    private ArrayList<BSNode<T>> inOrderNodes () {
        if (root == null) {
            return null;
        }
        final ArrayList<BSNode<T>> inOrderList = new ArrayList<>();
        BSNode<T> startNode = root;
        boolean mark = !root.isVisited;

        do {
            startNode = inOrderMove(startNode, mark, inOrderList);
        } while (startNode != null);
        return inOrderList;
    }


    /**
     * used in inOrderNodes to move to next node
     * and add to list of sorted nodes
     *
     * @param node start node
     * @param mark mark added node
     * @param list sorted list of nodes
     * @return node to move to
     */
    private BSNode<T> inOrderMove(BSNode<T> node, boolean mark, ArrayList<BSNode<T>> list) {
        if (node.isVisited != mark) {
            if (node.leftNode != null && node.leftNode.isVisited != mark) {
                return  node.leftNode;
            }
            if (node.rightNode != null && node.rightNode.isVisited != mark) {
                list.add(node);
                node.isVisited = mark;
                return  node.rightNode;
            }
            node.isVisited = mark;
            list.add(node);
        }
        return node.parent;
    }


    /**
     * iterative level order
     * @return array if nodes
     */
    private ArrayList<BSNode<T>> levelOrderNodes () {
        if (root == null) {
            return null;
        }
        final ArrayList<BSNode<T>> levelOrderList = new ArrayList<>();
        int startIndex = 0;
        int endIndex = 1;
        int newEndIndex = 1;
        levelOrderList.add(root);
        // while new elements were added to list
        while (startIndex != newEndIndex) {
            // cycle through new elements
            for (; startIndex < endIndex; startIndex++) {
                final BSNode<T> rightNode = levelOrderList.get(startIndex).rightNode;
                final BSNode<T> leftNode = levelOrderList.get(startIndex).leftNode;
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

    /**
     * get overall height of tree by level order method
     * @return height of tree
     */
    public int getHeight() {
        if (root == null) {
            return 0;
        }
        final ArrayList<BSNode<T>> levelOrderList = new ArrayList<>();
        int startIndex = 0;
        int endIndex = 1;
        int newEndIndex = 1;
        int height = 0;
        levelOrderList.add(root);
        // while new elements were added to list
        while (startIndex != newEndIndex) {
            height++;
            // cycle through new elements
            for (; startIndex < endIndex; startIndex++) {
                final BSNode<T> rightNode = levelOrderList.get(startIndex).rightNode;
                final BSNode<T> leftNode = levelOrderList.get(startIndex).leftNode;
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
        return height;
    }

    /**
     * used to find inorder predecessor when removing node
     * @param startNode node to remove
     * @return successor
     */
    private BSNode<T> findInorderPredecessor(BSNode<T> startNode) {
        for (startNode = startNode.leftNode; startNode.rightNode != null; startNode = startNode.rightNode);
        return startNode;
    }

    private BSNode<T> findInorderSuccessor(BSNode<T> startNode) {
        if (startNode.rightNode != null) {
            for (startNode = startNode.rightNode; startNode.leftNode != null; startNode = startNode.leftNode);
            return startNode;
        }

        BSNode<T> parent = startNode.parent;
        while (parent != null && startNode == parent.rightNode) {
            startNode = parent;
            parent = parent.parent;
        }
        return parent;
    }

    /**
     *  used to remove node when booth children are null
     * @param nodeToRemove BSNode<T>
     * @return true if remove was made
     */
    private boolean bothChildNull(BSNode<T> nodeToRemove) {
        if (nodeToRemove.leftNode == null &&
                nodeToRemove.rightNode == null) {

            final BSNode<T> parent = nodeToRemove.parent;
            swapNodes(parent, null, nodeToRemove);
            return true;
        }
        return false;
    }

    /**
     * used to remove node when one child is null
     * @param nodeToRemove BSNode<T>
     * @return true if remove was made
     */
    private boolean oneChildNull(BSNode<T> nodeToRemove) {
        if (nodeToRemove.leftNode == null ^
                nodeToRemove.rightNode == null) {
            final BSNode<T> child = nodeToRemove.leftNode == null ?
                    nodeToRemove.rightNode : nodeToRemove.leftNode;
            final BSNode<T> parent = nodeToRemove.parent;

            swapNodes(parent, child, nodeToRemove);
            return true;
        }
        return false;
    }

    /**
     * used to remove node when node has one or none child
     *
     * @param parent nodeToRemove.parent
     * @param child nodeToRemove.child
     * @param nodeToRemove BSNode<T>
     */
    private void swapNodes(BSNode<T> parent, BSNode<T> child, BSNode<T> nodeToRemove) {
        if (child != null) {
            child.parent = parent;
        }
        if (parent == null) {
            this.root = child;
        } else if (parent.leftNode == nodeToRemove) {
            parent.leftNode = child;
        } else {
            parent.rightNode = child;
        }
    }

    /**
     * used to find node by key
     * @param searchKey BSNode<T>
     * @return node
     */
    private BSNode<T> findNode(T searchKey)  {
        if (root == null) {
            return null;
        }

        // create tmp empty element from search key
        final BSData<T> searchElement = new BSData<>(searchKey) {
            @Override
            public Compare compare(BSData<T> data) {
                return null;
            }
        };

        BSNode<T>currentNode = root;

        while (true) {
            final Compare compareResult = currentNode.data.compare(searchElement);
            //found duplicate
            if (compareResult == Compare.EQUAL) {
                return currentNode;
                //leftNode
            } else if (compareResult == Compare.LESS) {
                if (currentNode.leftNode == null) {
                    return null;
                } else {
                    currentNode = currentNode.leftNode;
                }
                //rightNode
            } else if (compareResult == Compare.MORE) {
                if (currentNode.rightNode == null) {
                    return null;
                } else {
                    currentNode = currentNode.rightNode;
                }
            }
        }
    }

    /**
     * < startKey, endKey )
     *
     * @param startKey must be present in tree
     * @param endKey don't have to be present in tree
     * @return interval of elements
     */
    public ArrayList<BSNode<T>> intervalSearchNode(T startKey, T endKey) {
        BSNode<T> startNode = findNode(startKey);
        if (startNode == null) {
            return null;
        }
        final BSData<T> endElement = createTmpElement(endKey);

        final ArrayList<BSNode<T>> interval = new ArrayList<>();

        while (startNode != null && startNode.data.compare(endElement) != Compare.LESS) {
            interval.add(startNode);
            startNode = findInorderSuccessor(startNode);
        }
        return interval;
    }

    /**
     * < startKey, endKey )
     *
     * @param startKey must be present in tree
     * @param endKey don't have to be present in tree
     * @return interval of elements
     */
    public ArrayList<BSData<T>> intervalSearch(T startKey, T endKey) {
        return getOrderData(intervalSearchNode(startKey, endKey));
    }

    /**
     * iterative generator of medians
     * @param arrayLength length of array of medians
     * @return array of medians
     */
    public ArrayList<Integer> getMediansIndexes(int arrayLength) {

        if (arrayLength == 0) {
            return null;
        }
        class Parameters {
            Parameters(int start, int end) {
                this.start = start;
                this.end = end;
                this.mid = (start + end) / 2;
            }
            final int start;
            final int end;
            final int mid;
        }

        ArrayList<Parameters> parametersList = new ArrayList<>(arrayLength);

        int startIndex = 0;
        int endIndex = 1;
        int newEndIndex = 1;
        parametersList.add(new Parameters(0, arrayLength-1));

        // while new parameters were added to list
        while (startIndex != newEndIndex) {
            // cycle through new parameters
            for (; startIndex < endIndex; startIndex++) {
                final Parameters parameters = parametersList.get(startIndex);
                // right sub array
                if (parameters.start <= parameters.mid - 1) {
                    parametersList.add(new Parameters(parameters.start, parameters.mid - 1));
                    newEndIndex++;
                }
                // left sub array
                if (parameters.mid + 1 <= parameters.end) {
                    parametersList.add(new Parameters(parameters.mid + 1, parameters.end));
                    newEndIndex++;
                }
            }
            endIndex = newEndIndex;
        }

        final ArrayList<Integer> medians = new ArrayList<>(arrayLength);
        parametersList.forEach(a -> medians.add(a.mid));
        return medians;
    }

    /**
     * create  empty element from compare purpose
     * @param key
     * @return
     */
    private BSData<T> createTmpElement (T key) {
        return new BSData<>(key) {
            @Override
            public Compare compare(BSData<T> data) {
                return null;
            }
        };
    }

}
