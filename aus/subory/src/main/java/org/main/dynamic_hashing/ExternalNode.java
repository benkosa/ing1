package org.main.dynamic_hashing;

import java.util.BitSet;

public class ExternalNode extends Node {

    public BitSet adress;


   // private int nodeHeight;

    public ExternalNode(BitSet adress) {
        this.adress = adress;
    }

    public ExternalNode(long adress, Node parent) {
        this.parent = parent;
        this.adress = BitSet.valueOf(new long[]{adress});
    }
}
