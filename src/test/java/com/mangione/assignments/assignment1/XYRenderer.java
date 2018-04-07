package com.mangione.assignments.assignment1;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;

public class XYRenderer extends ApplicationFrame {

    public XYRenderer(Stats[] stats, int[] numberOfRuns) {
        super("Error plot versus run");
        JPanel chartPanel = createDemoPanel(stats, numberOfRuns);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

        JFrame frame = new JFrame("Sampling without replacement.");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private static JFreeChart createChart(IntervalXYDataset dataset) {
        LogAxis xAxis = new LogAxis("Number of runs");
        NumberAxis yAxis = new NumberAxis("Mean");
        XYErrorRenderer renderer = new XYErrorRenderer();
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);

        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        JFreeChart chart = new JFreeChart("Sample Without Replacement Performance", plot);
        chart.setBackgroundPaint(Color.white);

        return chart;
    }

    private static IntervalXYDataset createDataset(Stats[] stats, int[] numberOfRuns) {
        YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();
        YIntervalSeries s1 = new YIntervalSeries("Mean and SD");
        for (int i = 0; i < stats.length; i++) {
            final double mean = stats[i].getMean();
            final double sd = stats[i].getStandardDeviation();
            s1.add(numberOfRuns[i], mean, mean - sd, mean + sd);
        }
        dataset.addSeries(s1);
        return dataset;
    }

    public static JPanel createDemoPanel(Stats[] stats, int[] numberOfRuns) {
        return new ChartPanel(createChart(createDataset(stats, numberOfRuns)));
    }
}