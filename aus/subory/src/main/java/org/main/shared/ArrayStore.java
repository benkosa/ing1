package org.main.shared;

import org.main.hashing.IData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ArrayStore<T extends IData> {

    public void writeArray(DataOutputStream hlpOutStream, ArrayList<T> arrayIn, int maxLength, T emptyElement) {
        // write real length
        ArrayList<T> array = new ArrayList<>(arrayIn);
        try {
            if (array.size() < maxLength) {
                hlpOutStream.writeInt(array.size());
            } else {
                hlpOutStream.writeInt(maxLength);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // set array to maxLength
        if (array.size() < maxLength) {
            for (int i = array.size(); i < maxLength; i++) {
                array.add(emptyElement);
            }
        } else if (array.size() > maxLength) {
            array = new ArrayList<>(array.subList(0, maxLength));
        }

        // write array
        try {
            for (int i = 0; i < maxLength; i++) {
                hlpOutStream.write(array.get(i).toByteArray());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<T> loadArray(DataInputStream hlInputStream, int maxLength, T emptyElement) {
        // load real length of array
        int realLength;
        try {
            realLength = hlInputStream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // load array
        ArrayList<T> ret = new ArrayList<>();
        for (int i = 0; i < maxLength; i++) {
            try {
                T element = (T)emptyElement.createClass();
                hlInputStream.read(element.toByteArray());
                ret.add(emptyElement);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>(ret.subList(0, realLength));
    }

}
