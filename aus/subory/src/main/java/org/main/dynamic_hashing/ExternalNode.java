package org.main.dynamic_hashing;

import java.util.BitSet;

public class ExternalNode extends Node {

    public BitSet adress;

    public int getNodeHeight() {
        return nodeHeight;
    }

    public void incNodeHeight() {
        this.nodeHeight+=1;
    }
    public void decNodeHeight() {
        this.nodeHeight-=1;
    }

    private int nodeHeight;

    public ExternalNode(BitSet adress, int nodeHeight) {
        this.adress = adress;
        this.nodeHeight = nodeHeight;
    }

    public ExternalNode (InternalNode node) {

    }
}
