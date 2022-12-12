package org.main.dynamic_hashing;

public class InternalNode extends Node{
    InternalNode (Node leftNode, Node rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;

        if (leftNode != null) {
            leftNode.parent = this;
        }
        if (rightNode != null) {
            rightNode.parent = this;
        }
    }

    InternalNode (Node parent) {
        this.parent = parent;
    }
}
