package org.main.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StringStore {
    public void writeString(DataOutputStream hlpOutStream, String string, int maxLength) {
        // write length
        try {
            if (string.length() < maxLength) {
                hlpOutStream.writeInt(string.length());
            } else {
                hlpOutStream.writeInt(maxLength);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // set string to maxLength
        if (string.length() < maxLength) {
            for (int i = string.length(); i < maxLength; i++) {
                string += "x";
            }
        } else if (string.length() > maxLength) {
            string.substring(0, maxLength);
        }

        // write string
        try {
            hlpOutStream.writeChars(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String loadString(DataInputStream hlInputStream, int maxString) {
        // load real length of string
        int realLength;
        try {
            realLength = hlInputStream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // load string
        String ret = "";
        for (int i = 0; i < maxString; i++) {
            try {
                ret += hlInputStream.readChar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return ret.substring(0, realLength);
    }

}
