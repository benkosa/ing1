package org.main.dynamic_hashing;

public class InternalNode extends Node{
    InternalNode (Node leftNode, Node rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }
}
