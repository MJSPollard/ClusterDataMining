package project4;

import java.util.Random;

//supposed to be really fast
public class Kmeans {

	double data[][];
	int clusterNum;

	/**
	 * Constructor for the K means clustering algorithm
	 * 
	 * @param data
	 * @param clusterNum
	 */
	Kmeans(double data[][], int clusterNum) {
		this.data = data;
		this.clusterNum = clusterNum;
		int maxIterations = 30;
		int i = 0;

		if (data.length == 0) {
			System.out.println("The dataset is empty");
			System.exit(0);
		}
		if (clusterNum == 0) {
			System.out.println("There can't be 0 clusters");
			System.exit(0);
		}

		//mark the time and alert user
		long startTime = System.currentTimeMillis();
		System.out.println("K-Means clustering started");

		//set up initial requirements
		initializeCenters(data, clusterNum);
		calculateDistance();
		clusterAssignment();
		
		
		while(i < maxIterations) {
			
			i++;
		}

	}

	/**
	 * Initializes the center of the clusters to random points within the data
	 * 
	 * @param data
	 *            - The data to be clustered
	 * @param clusterNum
	 *            - the amount of clusters for the algorithm
	 */
	public double[][] initializeCenters(double[][] data, int clusterNum) {

		Random rand = new Random();
		double[][] clusterCenters = new double[clusterNum][];

		// randomly initialize the cluster centers to points in the data set
		for (int i = 0; i < clusterNum; i++) {
			double[] num = new double[data[0].length];
			for (int j = 0; j < num.length; j++) {
				num[j] = data[rand.nextInt(data.length)][j];
			}
			clusterCenters[i] = num;
		}

		// prints out the centers of the clusters
		int count = 1;
		for (int i = 0; i < clusterCenters.length; i++) {
			System.out.print("cluster " + count + " Randomly initialized center = ");
			count++;
			for (int j = 0; j < clusterCenters[0].length; j++) {
				System.out.print(clusterCenters[i][j] + " ");
			}
			System.out.println();

		}

		return clusterCenters;

	}

	public void calculateDistance() {

	}

	public void clusterAssignment() {

	}

	public void calculateCenters() {

	}

	//
	// public void updateclusterCenters() {
	//
	// }
	//

	//
	// public void convergence() {
	//
	// }
}
