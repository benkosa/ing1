package org.main.dynamic_hashing;

import org.main.hashing.Hashing;
import org.main.hashing.IData;

public class DynamicHashing<T extends IData> extends Hashing<T> {
    public DynamicHashing(String fileName, int blockFactor, int numberOfBlocks, Class classType) {
        super(fileName, blockFactor, numberOfBlocks, classType);
    }

    public DynamicHashing(String fileName,int blockFactor, Class classType) {
        super(fileName, blockFactor, 1, classType);

    }

    @Override
    public boolean insert(T data) {
        return super.insert(data);
    }

    @Override
    public boolean delete(T data) {
        return super.delete(data);
    }

    @Override
    public T find(T data) {
        return super.find(data);
    }
}
