package org.main.dynamic_hashing;

import java.util.BitSet;

public class ExternalNode extends Node {

    public BitSet adress;


   // private int nodeHeight;

    public ExternalNode(BitSet adress) {
        this.adress = adress;
    }

    public ExternalNode (InternalNode node) {

    }
}
