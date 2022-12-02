package org.main.app;

import org.main.bst.BSData;
import org.main.bst.Comparators;
import org.main.bst.Compare;
import org.main.dynamic_hashing.Node;
import org.main.shared.Tokens;

import java.lang.reflect.Type;

public class NodeMap extends BSData<String> {

    public Node node;

    public String tokens[];

    public NodeMap (Node node, String tokens[]) {
        this.key = tokens[Tokens.THIS.ordinal()];
        this.node = node;
        this.tokens = tokens;
    }

    @Override
    public Compare compare(BSData<String> data) {
        Comparators comparators = new Comparators();
        return comparators.stringCompare(data.key, this.key);
    }
}
