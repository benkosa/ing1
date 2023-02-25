package org.main;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Creates a real-time chart using SwingWorker
 */
public class SwingWorkerRealTime {

    MySwingWorker mySwingWorker;
    SwingWrapper<XYChart> sw;
    XYChart chart;


    public SwingWorkerRealTime() {
        this.go();
    }

    public static void main(String[] args) throws Exception {

        SwingWorkerRealTime swingWorkerRealTime = new SwingWorkerRealTime();
        swingWorkerRealTime.go();
    }

    private void go() {

        // Create Chart
        chart = QuickChart.getChart("SwingWorker XChart Real-time Demo", "Time", "Value", "randomWalk", new double[] { 0 }, new double[] { 0 });
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisTicksVisible(false);

        // Show it
        sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();

        mySwingWorker = new MySwingWorker();
        mySwingWorker.execute();
    }

    private class MySwingWorker extends SwingWorker<Boolean, double[]> {

        LinkedList<Double> fifo = new LinkedList<>();

        final int L = 9;
        final int D = 10;
        final int replications = 10000000;

        int m = 0;
        int n = 0;

        Random genA = new Random();
        Random genAlfa = new Random();
        double a, alfa, y;

        private void throwNeedle() {
            n+=1;
            a = genA.nextDouble() * D;
            alfa = genAlfa.nextDouble() * Math.PI;
            y = L * Math.sin(alfa);

            if (a+y >= D) {
                m+=1;
            }
            if (m > 0) {
                fifo.add((double) (L * n * 2) / (D * m));
            }
        }

        @Override
        protected Boolean doInBackground() throws Exception {

            while (n < replications) {

                throwNeedle();

                double[] array = new double[fifo.size()];
                for (int i = 0; i < fifo.size(); i++) {
                    array[i] = fifo.get(i);
                }
                publish(array);

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // eat it. caught when interrupt is called
                    System.out.println("MySwingWorker shut down.");
                }

            }

            return true;
        }

        @Override
        protected void process(List<double[]> chunks) {

            System.out.println("number of chunks: " + chunks.size());
            System.out.println("pi: " + ((double) (L * n * 2) / (D * m)));

            double[] mostRecentDataSet = chunks.get(chunks.size() - 1);

            chart.updateXYSeries("randomWalk", null, mostRecentDataSet, null);
            try {
                sw.repaintChart();
            } catch (Exception ignored) {}

            long start = System.currentTimeMillis();
            long duration = System.currentTimeMillis() - start;
            try {
                Thread.sleep(40 - duration); // 40 ms ==> 25fps
                // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
            } catch (Exception ignored) { }

        }
    }
}
