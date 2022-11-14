package org.main.hashing;

public interface IRecord<T> {
    byte[] toByteArray();
    void fromByteArray(byte[] paArray);
    int getSize();
}
