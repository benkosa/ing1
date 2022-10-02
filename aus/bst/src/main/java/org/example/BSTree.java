package org.example;

import java.util.ArrayList;

public class BSTree<T> {
    private BSNode<T> root;

    public BSData<T> getRoot() {
        return root == null ? null : root.data;
    }

    /**
     * iterative insert
     * @param incomingNode incomingNode
     */
    public void insert(BSData<T> incomingNode) {
        if (root == null) {
            root = new BSNode<>(incomingNode);
            return;
        }
        BSNode<T>currentNode = root;

        while (true) {
            //no duplicates
            final Compare compareResult = currentNode.data.compare(incomingNode);
            if (compareResult == Compare.EQUAL) {
                return;
            //leftNode
            } else if (compareResult == Compare.LESS) {
                if (currentNode.leftNode == null) {
                    currentNode.leftNode = new BSNode<>(incomingNode);
                    currentNode.leftNode.parent = currentNode;
                    return;
                } else {
                    currentNode = currentNode.leftNode;
                }
            //rightNode
            } else if (compareResult == Compare.MORE) {
                if (currentNode.rightNode == null) {
                    currentNode.rightNode = new BSNode<>(incomingNode);
                    currentNode.rightNode.parent = currentNode;
                    return;
                } else {
                    currentNode = currentNode.rightNode;
                }
            }
        }
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
        BSNode<T> nodeToRemove = this.findNode(key);
        if (nodeToRemove == null) {
            return null;
        }
        //both children null
        if (bothChildNull(nodeToRemove)) return nodeToRemove.data;

        //one child null
        if (oneChildNull(nodeToRemove)) return nodeToRemove.data;

        //both children
        BSNode<T> inorderSuccessor = findInorderSuccessor(nodeToRemove.leftNode);
        bothChildNull(inorderSuccessor);
        oneChildNull(inorderSuccessor);
        final BSData<T> retData = nodeToRemove.data;
        swapData(nodeToRemove, inorderSuccessor);
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
            if (node.leftNode != null) {
                if (node.leftNode.isVisited == mark) {
                    node.isVisited = mark;
                    list.add(node);
                } else {
                    return  node.leftNode;
                }
            }
            if (node.rightNode != null && node.rightNode.isVisited != mark) {
                return  node.rightNode;
            }
            if (node.isVisited != mark) {
                node.isVisited = mark;
                list.add(node);
            }
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

    /**
     * used to find inorder successor when removing node
     * @param startNode leftNode of nodeToRemove
     * @return successor
     */
    private BSNode<T> findInorderSuccessor(BSNode<T> startNode) {
        BSNode<T> ret = startNode;
        for (BSNode<T> i = ret ; i != null; i = i.rightNode) {
            ret = i;
        }
        return ret;
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
     * @param parent BSNode<T>
     * @param child BSNode<T>
     * @param nodeToRemove BSNode<T>
     */
    private void swapNodes(BSNode<T> parent, BSNode<T> child, BSNode<T> nodeToRemove) {
        if (child != null) {
            child.parent = parent == null ? null : parent.parent;
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
     * used to swap data when removing node with both children
     *
     * @param nodeToRemove BSNode<T>
     * @param inorderSuccessor BSNode<T>
     */
    private void swapData(BSNode<T> nodeToRemove, BSNode<T> inorderSuccessor) {
        nodeToRemove.data = inorderSuccessor.data;
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

        BSData<T> searchElement = new BSData<>(searchKey) {
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

}
