package com.mangione;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class Test {

	public static void main(String[] args) {
		OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();

		double[] data = { 2, 1, 4, 2 }; // 1
		int obs = 2;
		int vars = 1; // 2
		try {
			ols.newSampleData(data, obs, vars); // 3
		}
		catch(IllegalArgumentException e) {
			System.out.print("Can't sample data: ");
			e.printStackTrace();
			return;
		}

		double[] coe = null;
		try {
			coe = ols.estimateRegressionParameters(); // 4
		}
		catch(IllegalArgumentException e) { // 5
			System.out.print("Can't estimate parameters: ");
			e.printStackTrace();
			return;
		}

		dumpEstimation(coe);
	}

	private static void dumpEstimation(double[] coe) {
		if(coe == null)
			return;

		for(double d : coe)
			System.out.print(d + " ");
		System.out.println();

		System.out.println("Estimations:");
		System.out.println("x = 1, y = " + calculateEstimation(1, coe));
		System.out.println("x = 2, y = " + calculateEstimation(2, coe));
		System.out.println("x = 3, y = " + calculateEstimation(3, coe));
		System.out.println("x = 4, y = " + calculateEstimation(4, coe));
	}

	private static double calculateEstimation(double x, double[] coe) {
		double result = 0;
		for(int i = 0; i < coe.length; ++i)
			result += coe[i] * Math.pow(x, i); // 1
		return result;
	}
}
