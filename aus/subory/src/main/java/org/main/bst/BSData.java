package org.main.bst;

/**
 * used to warp element you want to insert into BSTree
 * tree is sorted by key parameter with compare function
 * @param <T> param of key
 */
public abstract class BSData<T> {

    public BSData(T key) {
        this.key = key;
    }

    public BSData() {

    }

    public T key;

    /**
     * tree is sorted by key parameter with compare function
     * @param data data
     * @return Compare
     */
    public abstract Compare compare(BSData<T> data);
}

