package org.main.shared.Statistics;

public class SampleStandardDeviation {

    double sum1 = 0;
    double sum2 = 0;
    double samples = 0;

    public void countReplication(double result) {
        sum1+= Math.pow(result, 2);
        sum2+= result;
        samples+=1;
    }

    public double getStd() {
        return Math.sqrt(
                (sum1-(Math.pow(sum2, 2)/samples))
                        /
                 (samples - 1)
        );
    }

    public Double[] getConfidenceInterval(double tAlfa) {
        double xLine = (sum2)/samples;
        double s = getStd();
        return new Double[]{
                xLine-(s*tAlfa/Math.sqrt(samples)),
                xLine+(s*tAlfa/Math.sqrt(samples))
        };
    }

}
