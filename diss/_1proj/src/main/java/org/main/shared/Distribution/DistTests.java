package org.main.shared.Distribution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DistTests {

    public void  testExponential() throws IOException {
        ExponentialDistribution  test = new ExponentialDistribution(new SeedGenerator(0), 5);
        FileWriter myWriter;
        try {
            myWriter = new FileWriter("exponential.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 10000; i++) {
            myWriter.write(test.sample().toString()+'\n');
        }
    }
}
