package shared;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import _1zadanie.UpdateGui;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public abstract class MonteCarloGraph extends MonteCarlo {

    protected abstract double onePass();
    protected abstract void beforeSimulation();
    protected abstract void afterSimulation();
    SwingWorker<Boolean, Double> graph;
    int offset;
    int sample_offset;
    int base_sample_offset;
    SwingWrapper<XYChart> sw;
    XYChart chart;
    private UpdateGui updateGui;
    final LinkedList<Double> seriesData = new LinkedList<>();

    String chartTitle;

    public MonteCarloGraph(
            final long REPLICATIONS,
            final int OFFSET,
            final int MAXIMUM_CHART_X,
            final String CHART_TITLE,
            JLabel replication,
            JLabel result
    ) {
        super(REPLICATIONS);
        this.initializeGraph();
        this.offset = OFFSET;
        this.sample_offset = MAXIMUM_CHART_X;
        this.base_sample_offset = MAXIMUM_CHART_X;
        this.chartTitle = CHART_TITLE;
        this.updateGui = new UpdateGui(replication, result);
        // Create Chart
        chart = QuickChart.getChart(chartTitle, "Replikacie", "Vysledok pokusu", chartTitle, new double[] { 0 }, new double[] { 0 });
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setLegendPadding(10);
        chart.getStyler().setXAxisMaxLabelCount(50);

        // Show it
        sw = new SwingWrapper<>(chart);
        sw.displayChart().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.graph.execute();
    }



    private void initializeGraph() {
        this.graph = new SwingWorker<Boolean, Double>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                beforeSimulation();
                long i ;
                double result = 0;
                for (i = 0; i < replications; i++) {

                    result = onePass();

            if (offset < i && i % sample_offset == 0) {
                publish(result);
            }
                    if (interrupt) {
                        afterSimulation();
                        return false;
                    }
            if (i % offset == 0)
                updateGui.updateResults(result, i);
                }

                afterSimulation();
                updateGui.updateResults(result, i);

                return true;
            }

            @Override
            protected void process(List<Double> chunks) {
                if (interrupt) {
                    chart.setXAxisTitle("x x " + sample_offset + " result: " + seriesData.getLast());
                    return;
                } else {
                    seriesData.addAll(chunks);
                }

                // if there is more data than maximum
                while (seriesData.size() > base_sample_offset) {
                    //remove every other data
                    ListIterator<Double> listIterator = seriesData.listIterator();
                    int i = 0;
                    while (listIterator.hasNext()) {
                        listIterator.next();
                        i+=1;
                        if (i % 2 == 1) {
                            listIterator.remove();
                        }
                    }
                    //increase offset size
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
                } catch (Exception ignored) { }

            }
        };
    }


}
