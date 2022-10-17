package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public int insertMultiple(ArrayList<BSData<T>> elementsList) {
        elementsList.sort(new Comparator<BSData<T>>() {
            @Override
            public int compare(BSData<T> o1, BSData<T> o2) {
                switch (o1.compare(o2)) {
                    case LESS:
                        return 1;
                    case MORE:
                        return -1;
                    default:
                        return 0;
                }
            }
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
        final BSNode<T> inorderSuccessor = findInorderSuccessor(nodeToRemove);
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

    /**
     * iterative post order
     * @return in order list of data
     */
    public ArrayList<BSData<T>> postOrder() {
        return getOrderData(postOrderNodes());
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
     * n+n/2*n/2+n
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
     * @return post order list of nodes
     */
    private ArrayList<BSNode<T>> postOrderNodes () {
        if (root == null) {
            return null;
        }
        final ArrayList<BSNode<T>> inOrderList = new ArrayList<>();
        BSNode<T> startNode = root;
        boolean mark = !root.isVisited;

        do {
            startNode = postOrderMove(startNode, mark, inOrderList);
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
     * used in postOrderNodes to move to next node
     * and add to list of nodes
     *
     * @param node start node
     * @param mark mark added node
     * @param list sorted list of nodes
     * @return node to move to
     */
    private BSNode<T> postOrderMove(BSNode<T> node, boolean mark, ArrayList<BSNode<T>> list) {
        if (node.isVisited != mark) {
            if (node.leftNode != null && node.leftNode.isVisited != mark) {
                return  node.leftNode;
            }
            if (node.rightNode != null && node.rightNode.isVisited != mark) {
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
        while (startIndex != newEndIndex) {
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
        while (startIndex != newEndIndex) {
            height++;
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
     * used to find inorder predecesor when removing node
     * @param startNode node to remove
     * @return successor
     */
    private BSNode<T> findInorderSuccessor(BSNode<T> startNode) {
        for (startNode = startNode.leftNode; startNode.rightNode != null; startNode = startNode.rightNode);
        return startNode;
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
                improve(currentNode);
                return currentNode;
                //leftNode
            } else if (compareResult == Compare.LESS) {
                if (currentNode.leftNode == null) {
                    improve(currentNode);
                    return null;
                } else {
                    currentNode = currentNode.leftNode;
                }
                //rightNode
            } else if (compareResult == Compare.MORE) {
                if (currentNode.rightNode == null) {
                    improve(currentNode);
                    return null;
                } else {
                    currentNode = currentNode.rightNode;
                }
            }
        }
    }

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
            int start;
            int end;
            int mid;
        }

        ArrayList<Parameters> parametersList = new ArrayList<>(arrayLength);

        int startIndex = 0;
        int endIndex = 1;
        int newEndIndex = 1;
        parametersList.add(new Parameters(0, arrayLength-1));

        while (startIndex != newEndIndex) {
            for (; startIndex < endIndex; startIndex++) {
                final Parameters parameters = parametersList.get(startIndex);

                if (parameters.start <= parameters.mid - 1) {
                    parametersList.add(new Parameters(parameters.start, parameters.mid - 1));
                    newEndIndex++;
                }
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

}
