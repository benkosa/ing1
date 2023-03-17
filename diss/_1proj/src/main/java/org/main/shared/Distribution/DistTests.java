package org.main.shared.Distribution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DistTests {

    public void  testExponencial() throws IOException {
        ExponentialDistribution  test = new ExponentialDistribution(new Random(0), 5);
        FileWriter myWriter;
        try {
            myWriter = new FileWriter("exponencial.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 10000; i++) {
            myWriter.write(test.sample().toString()+'\n');
        }
    }
}
