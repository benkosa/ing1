package org.example.Opetations.Data;

import org.example.BSTree;

public class Data {
    public final BSTree<String> nemocnice = new BSTree<>();

    public void addNemocnica(String name) {
        nemocnice.insert(new Nemocnica(name));
    }
}
