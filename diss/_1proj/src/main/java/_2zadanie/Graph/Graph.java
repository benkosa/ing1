package _2zadanie.Graph;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public abstract class Graph extends SwingWorker<Double, Double> {

    private int start;
    private int end;
    LinkedList<Double> seriesData = new LinkedList<>();
    String chartTitle;

    SwingWrapper<XYChart> sw;
    XYChart chart;

    public Graph(int start, int end, String labelX, String labelY, String chartTitle) {
        this.chartTitle = chartTitle;
        this.start = start;
        this.end = end;

        chart = QuickChart.getChart(chartTitle, labelX, labelY, chartTitle, new double[] { 0 }, new double[] { 0 });
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setLegendPadding(10);
        //chart.getStyler().setXAxisMaxLabelCount(50);
        chart.getStyler();

        // Show it
        sw = new SwingWrapper<>(chart);
        sw.displayChart().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        execute();
    }

    @Override
    protected Double doInBackground() throws Exception {
        for (int i = start; i <= end; i++) {
            publish(onePass(i));
        }
        return .0;
    }

    public abstract double onePass(int i);

    @Override
    protected void process(List<Double> chunks) {
        seriesData.addAll(chunks);
        List<Double> xData = new LinkedList<>();
        for (double i = start; i < seriesData.size()+start; i++) {
            xData.add(i);
        }
        chart.updateXYSeries(chartTitle, xData, seriesData, null);
        try {
            sw.repaintChart();
        } catch (Exception ignored) {}

        long start = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - start;
        try {
            Thread.sleep(40 - duration); // 40 ms ==> 25fps
        } catch (Exception ignored) { }

    }


}
