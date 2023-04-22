package shared.Distribution;

import java.io.FileWriter;
import java.io.IOException;

public class DistTests {

    public void  testExponential(long samples, int seed, int mean) {
        ExponentialDistribution  test = new ExponentialDistribution(new SeedGenerator(seed), mean);
        FileWriter myWriter;
        try {
            myWriter = new FileWriter("exponential.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < samples; i++) {
            try {
                myWriter.write(test.sample().toString()+'\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void  testTriangular(long samples, int seed, int a, int b, int c) {
        TriangularDistribution  test = new TriangularDistribution(new SeedGenerator(seed),a, b, c);
        FileWriter myWriter;
        try {
            myWriter = new FileWriter("triangular.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < samples; i++) {
            try {
                myWriter.write(test.sample().toString()+'\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
