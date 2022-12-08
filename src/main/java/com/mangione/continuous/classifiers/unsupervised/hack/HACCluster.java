package com.mangione.continuous.classifiers.unsupervised.hack;

import java.util.ArrayList;
import java.util.List;

import org.opencompare.hac.HierarchicalAgglomerativeClusterer;
import org.opencompare.hac.agglomeration.AgglomerationMethod;
import org.opencompare.hac.agglomeration.AverageLinkage;
import org.opencompare.hac.dendrogram.Dendrogram;
import org.opencompare.hac.dendrogram.DendrogramBuilder;
import org.opencompare.hac.dendrogram.DendrogramNode;
import org.opencompare.hac.dendrogram.MergeNode;
import org.opencompare.hac.dendrogram.ObservationNode;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.classifiers.unsupervised.ClusterFactoryInterface;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.sampling.SMOTE.DiscreteExperiment;
import com.mangione.continuous.sampling.SMOTE.HammingDistanceDissimilarityMeasure;

public class HACCluster<OBSERVATION extends ObservationInterface<Integer>>
		implements ClusterFactoryInterface<OBSERVATION, ObservationProviderInterface<Integer,OBSERVATION>> {
	private final int cutHeight;
	private Dendrogram dendogram;
	private ListObservationProvider<Integer, OBSERVATION> listObservationProvider;

	public HACCluster(int cutHeight) {
		this.cutHeight = cutHeight;
	}

	@Override
	public void cluster(ObservationProviderInterface<Integer, OBSERVATION> provider) {
		listObservationProvider = new ListObservationProvider<>(provider);

		DiscreteExperiment<ListObservationProvider<Integer, OBSERVATION>> discreteExperiment
				= new DiscreteExperiment<>(listObservationProvider);
		HammingDistanceDissimilarityMeasure dissimilarityMeasure = new HammingDistanceDissimilarityMeasure();

		AgglomerationMethod agglomerationMethod = new AverageLinkage();
		DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(discreteExperiment.getNumberOfObservations());
		HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(discreteExperiment,
				dissimilarityMeasure, agglomerationMethod);
		clusterer.cluster(dendrogramBuilder);
		dendogram = dendrogramBuilder.getDendrogram();
	}

	@Override
	public List<Cluster<Integer, OBSERVATION>> getClusters() {
		List<Cluster<Integer, OBSERVATION>> collectedClusters = new ArrayList<>();
		recurseThroughTreeCollectingClustersNearTarget(dendogram.getRoot(), cutHeight, collectedClusters);
		return collectedClusters;
	}

	private void recurseThroughTreeCollectingClustersNearTarget(DendrogramNode node, int currentCutHeight,
			List<Cluster<Integer, OBSERVATION>> collectedClusters) {
		if (currentCutHeight == 0 || node instanceof ObservationNode) {
			if (node instanceof MergeNode)
				collectedClusters.add(new Cluster<>(((MergeNode)node).getDissimilarity(), getObservationsForCluster(node)));
		} else {
			int remainingCuts = currentCutHeight - 1;
			recurseThroughTreeCollectingClustersNearTarget(node.getLeft(), remainingCuts, collectedClusters);
			recurseThroughTreeCollectingClustersNearTarget(node.getRight(), remainingCuts, collectedClusters);
		}

	}

	private List<OBSERVATION> getObservationsForCluster(DendrogramNode node) {
		List<OBSERVATION> collectedObservations = new ArrayList<>();
		recurseToGetAllObservationsForNode(node, collectedObservations);
		return collectedObservations;
	}

	private void recurseToGetAllObservationsForNode(DendrogramNode node, List<OBSERVATION> collectedObservations) {
		if (node instanceof ObservationNode)
			collectedObservations.add(listObservationProvider.getByIndex(((ObservationNode)node).getObservation()));
		else {
			recurseToGetAllObservationsForNode(node.getLeft(), collectedObservations);
			recurseToGetAllObservationsForNode(node.getRight(), collectedObservations);
		}
	}

}
