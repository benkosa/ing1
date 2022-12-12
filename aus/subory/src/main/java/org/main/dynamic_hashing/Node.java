package org.main.dynamic_hashing;

import java.io.Serializable;

public class Node implements Serializable {
    public Node parent;
    public Node rightNode;
    public Node leftNode;

    @Override
    public String toString() {
        return super.toString();
    }
}
