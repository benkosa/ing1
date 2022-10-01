package org.example;

enum Compare {
    LESS,
    EQUAL,
    MORE
}

/**
 * used to warp element you want to insert into BSTree
 * tree is sorted by key parameter with compare function
 * @param <T> param of key
 */
public abstract class BSData<T> {

    BSData(T key) {
        this.key = key;
    }

    public T key;

    /**
     * tree is sorted by key parameter with compare function
     * @param data
     * @return
     */
    public abstract Compare compare(BSData<T> data);
}
