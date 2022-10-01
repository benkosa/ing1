package org.example;

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
     * @param data data
     * @return Compare
     */
    public abstract Compare compare(BSData<T> data);
}

enum Compare {
    LESS,
    EQUAL,
    MORE
}