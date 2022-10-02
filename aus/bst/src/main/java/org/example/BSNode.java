package org.example;

public class BSNode<T> {
    BSNode(BSData<T> node) {
        this.data = node;
    }
    public BSNode<T> rightNode;
    public BSNode<T> leftNode;
    public BSNode<T> parent;
    public BSData<T> data;
    public  boolean isVisited = false;
}
