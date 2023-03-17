package org.main.shared;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.main._1zadanie.UpdateGui;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public abstract class MonteCarlo extends SwingWorker<Boolean, Double> {

    final LinkedList<Double> seriesData = new LinkedList<>();
    int offset;
    long replications;
    int sample_offset;
    int base_sample_offset;
    SwingWrapper<XYChart> sw;
    XYChart chart;
    String chartTitle;
    private boolean interrupt = false;
    private UpdateGui updateGui;


    public MonteCarlo(
            final long REPLICATIONS,
            final int OFFSET,
            final int MAXIMUM_CHART_X,
            final String CHART_TITLE,
            JLabel replication,
            JLabel result
    ) {
        this.replications = REPLICATIONS;
        this.offset = OFFSET;
        this.sample_offset = MAXIMUM_CHART_X;
        this.base_sample_offset = MAXIMUM_CHART_X;
        this.chartTitle = CHART_TITLE;
        this.updateGui = new UpdateGui(replication, result);
    }

    public void stopChart() {
        this.interrupt = true;
        this.cancel(true);
    }

    public abstract double onePass();
    public abstract void beforeSimulation();
    public abstract void afterSimulation();

    public void go() {
        // Create Chart
        chart = QuickChart.getChart(chartTitle, "Replikacie", "Vysledok pokusu", chartTitle, new double[] { 0 }, new double[] { 0 });
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setLegendPadding(10);
        chart.getStyler().setXAxisMaxLabelCount(50);

        // Show it
        sw = new SwingWrapper<>(chart);
        sw.displayChart().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.execute();
    }

    @Override
    protected Boolean doInBackground() {

        this.beforeSimulation();
        long i ;
        double result = 0;
        for (i = 0; i < replications; i++) {

            result = onePass();

            if (offset < i && i % sample_offset == 0) {
                publish(result);
            }
            if (interrupt) {
                this.afterSimulation();
                return false;
            }
            if (i % offset == 0)
                updateGui.updateResults(result, i);
        }

        this.afterSimulation();
        updateGui.updateResults(result, i);

        return true;
    }

    @Override
    protected void process(List<Double> chunks) {
        if (!interrupt) {
            seriesData.addAll(chunks);
        } else {
            return;
        }

        // remove data from chart
        while (seriesData.size() > base_sample_offset) {
            ListIterator<Double> listIterator = seriesData.listIterator();
            int i = 0;
            while (listIterator.hasNext()) {
                listIterator.next();
                i+=1;
                if (i % 2 == 1) {
                    listIterator.remove();
                }
            }
            sample_offset *= 2;
            chart.setXAxisTitle("Replikacie x " + sample_offset + " result: " + seriesData.getLast());
        }

        chart.updateXYSeries(chartTitle, null, seriesData, null);
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
