package com.mangione.continuous.classifiers.unsupervised;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

import org.apache.commons.math3.random.MersenneTwister;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

import com.mangione.cse151.observationproviders.ArrayObservationProvider;
import com.mangione.cse151.observations.Observation;
import com.mangione.cse151.observations.ObservationFactory;

public class ClusterDemo implements KMeansListener<Observation> {

	private final ApplicationFrame applicationFrame;

	public ClusterDemo(String s) throws Exception {
		applicationFrame = new ApplicationFrame(s);
		List<Observation> observations = generateTwoClusters();

		ArrayObservationProvider<Observation> provider =
				new ArrayObservationProvider<>(observations, new ObservationFactory());

		JPanel jpanel = createDemoPanel(Color.red, observations.subList(100, 200),
				observations.subList(0, 100), null);
		jpanel.setPreferredSize(new Dimension(1000, 500));
		applicationFrame.setContentPane(jpanel);
		applicationFrame.pack();
		RefineryUtilities.centerFrameOnScreen(applicationFrame);
		applicationFrame.setVisible(true);

		new KMeans<>(2, provider, this);

	}


	public JPanel createDemoPanel(Color red, List<Observation> observations1,
			List<Observation> observations2, List<Observation> centroids) {

		JFreeChart jfreechart = ChartFactory.createScatterPlot("Scatter Plot Demo",
				"X", "Y", samplexydataset2(observations1,
						observations2, centroids), PlotOrientation.VERTICAL,
				true, true, false);
		Shape cross = ShapeUtilities.createDiamond(100);

		XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setBaseShape(cross);
		renderer.setBasePaint(red);
		renderer.setSeriesPaint(0, red);
		renderer.setSeriesPaint(1, Color.blue);
		renderer.setSeriesPaint(2, Color.black);
		xyPlot.setRenderer(renderer);

		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);

		return new ChartPanel(jfreechart);
	}

	@Override
	public void reassignmentCompleted(List<Cluster<Observation>> currentClusters) {

		List<Observation> centroids = new ArrayList<>();
		centroids.add(currentClusters.get(0).getCentroid());
		centroids.add(currentClusters.get(1).getCentroid());
		JPanel jpanel = createDemoPanel(Color.red, currentClusters.get(0).getObservations(),
				currentClusters.get(1).getObservations(), centroids);
		try {
			Thread.sleep(1000);
			SwingUtilities.invokeAndWait(
					() -> {
						jpanel.setPreferredSize(new Dimension(1000, 500));
						applicationFrame.setContentPane(jpanel);
						applicationFrame.pack();
						applicationFrame.repaint();
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private XYDataset samplexydataset2(List<Observation> observations1, List<Observation>
			observations2, List<Observation> centroids) {

		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		xySeriesCollection.addSeries(getXYSeries(observations1, "1"));
		xySeriesCollection.addSeries(getXYSeries(observations2, "2"));
		if (centroids != null)
			xySeriesCollection.addSeries(getXYSeries(centroids, "3"));

		return xySeriesCollection;
	}

	private XYSeries getXYSeries(List<Observation> observations, String random) {
		XYSeries series1 = new XYSeries(random);
		observations.forEach(observation -> series1.add(observation.getFeatures()[0],
				observation.getFeatures()[1]));
		return series1;
	}

	public static void main(String args[]) throws Exception {
		new ClusterDemo("Scatter Plot Demo 4");
	}


	private static List<Observation> generateTwoClusters() {
		MersenneTwister random = new MersenneTwister(198273);
		List<Observation> observationList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			observationList.add(new Observation(
					new double[]{10 + 2* random.nextGaussian(), 4 + 1 * random.nextGaussian()}));
			observationList.add(new Observation(
					new double[]{8 + 2* random.nextGaussian(), 3 + 2 * random.nextGaussian()}));
		}
		Collections.shuffle(observationList);
		return observationList;
	}
}
