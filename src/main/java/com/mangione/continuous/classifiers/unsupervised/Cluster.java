package com.mangione.continuous.classifiers.unsupervised;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.distance.EuclideanDistance;


public class Cluster<S> {


	private final int numDimensions;
    private S centroid;
    private final List<S> observations = new ArrayList<>();
    private final EuclideanDistance euclideanDistance = new EuclideanDistance();

    public Cluster(int numDimensions) {

        this.numDimensions = numDimensions;
    }

    public S getCentroid() {
        return centroid;
    }

    public void add(S observation) {
        observations.add(observation);
       // updateCentroid();
    }

    public List<S> getObservations() {
        return observations;
    }

    public void remove(S observation) {
        observations.remove(observation);
       // updateCentroid();
    }

    public double distanceToCentroid(double[] observation) {
    	try {
		    return euclideanDistance.compute((double[]) centroid, observation);
	    } catch(Throwable e) {
		    System.out.println(centroid);
//		    System.out.println(observation);
		    e.printStackTrace();
		    System.exit(0);
	    }
	    return 0.0;
    }

//    public double withinClusterSumOfSquares() {
//        final double[] sumOfSquares = {0};
//        observations.forEach(x->
//                sumOfSquares[0] +=
//                        Math.pow(euclideanDistance.compute(centroid, x), 2));
//        return sumOfSquares[0];
//    }

//    void updateCentroid() {
//        if (observations.isEmpty()) {
//            centroid = null;
//        } else {
//            double[] sumsOfDimensions = new double[numDimensions];
//            observations.forEach(x -> {
//                for (int i = 0; i < sumsOfDimensions.length; i++) {
//	                try {
//		                sumsOfDimensions[i] += x[i] / observations.size();
//	                } catch (Throwable e) {
//						if(x == null)
//							continue;
//	                }
//                }
//            });
//            centroid = sumsOfDimensions;
//        }
//    }

    @Override
	public String toString() {
    	return observations.size() + "";
    }

	void setCentroid(S obs) {
		this.centroid = obs;
	}

	int getNumDimensions() {
		return numDimensions;
	}
}
