package project4;

import java.util.Random;
import java.util.ArrayList;

//supposed to be really fast
public class Kmeans {

	double data[][];
	int clusterNum;
	ArrayList<Double> cluster1 = new ArrayList<Double>();
	ArrayList<Double> cluster2 = new ArrayList<Double>();
	ArrayList<Double> cenList = new ArrayList<Double>();
	ArrayList<Double> dataList = new ArrayList<Double>();

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

		// mark the time and alert user
		long startTime = System.currentTimeMillis();
		System.out.println("K-Means clustering started");

		// initialize random centers and assign the rest of the data to a cluster
		// according to the distance from centers
		calculateDistance(data, clusterNum, initializeCenters(data, clusterNum));

		// prints size of initial clusters with no centroid updates
		System.out.println("Iteration number: 1");
		System.out.println("cluster1 size = " + cluster1.size());
		System.out.println("cluster2 size = " + cluster2.size());

		while (i < maxIterations) {		
			updateCenters();
			System.out.println("Iteration number: " + (i+2));
			System.out.println("cluster1 size = " + cluster1.size());
			System.out.println("cluster2 size = " + cluster2.size());
			i++;
		}

		long endTime = System.currentTimeMillis();
		System.out.println("K-Means performed in " + (endTime - startTime) + " ms");

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

	/**
	 * calculates the distance of each point to the center of a cluster
	 * 
	 * @param data
	 * @param clusterNum
	 * @param clusterCenters
	 * @return
	 */
	public void calculateDistance(double[][] data, int clusterNum, double[][] clusterCenters) {
		int finalDistance = 0;
		double dataSum = 0;
		double dataAvg = 0;
		double cenSum = 0;
		double cenAvg = 0;

		// uses averages to compare the distances between the centers and the data
		// this method was implemented as there didn't seem to be any best option - based
		// off of no free lunch principle
		for (int instanceIT = 0; instanceIT < clusterCenters.length; instanceIT++) {
			for (int attrIT = 0; attrIT < clusterCenters[0].length; attrIT++) {
				cenSum += clusterCenters[instanceIT][attrIT];
			}
			cenAvg = (cenSum / clusterCenters[0].length);
			cenList.add(cenAvg);
			cenSum = 0;
		}

		// for (int i = 0; i < cenList.size(); i++) {
		// System.out.println(cenList.get(i));
		// }
		// System.out.println("cen size = " + cenList.size());

		for (int instanceIT = 0; instanceIT < data.length; instanceIT++) {
			for (int attrIT = 0; attrIT < data[0].length; attrIT++) {
				dataSum += data[instanceIT][attrIT];
			}
			dataAvg = (dataSum / data[0].length);
			dataList.add(dataAvg);
			dataSum = 0;
		}

		getClosest(cenList, dataList);
		// for (int i = 0; i < dataList.size(); i++) {
		// System.out.println(dataList.get(i));
		// }
		// System.out.println("data size = " + dataList.size());
	}

	/**
	 * Calculates which centroid the data is closest too
	 */
	public void getClosest(ArrayList<Double> cenList, ArrayList<Double> dataList) {
		int dataIndex = 0;
		int centerIndex = 0;

		for (int i = 0; i < data.length; i++) {
			double closest = 10000; // set to arbitrarily high number to be overwritten immediately
			for (int j = 0; j < clusterNum; j++) {
				double num = Math.abs(cenList.get(j) - dataList.get(i));

				if (num < closest) {
					closest = num;
					dataIndex = i;
					centerIndex = j;
				}
			}
			assignToCluster(cenList.get(centerIndex), dataList.get(dataIndex), centerIndex);
		}

	}

	/**
	 * assigns the data to a specific cluster
	 * 
	 * @param cenValue
	 * @param dataVal
	 * @param centerIndex
	 */
	public void assignToCluster(double cenValue, double dataVal, int centerIndex) {
		// System.out.println(dataVal);
		if (centerIndex == 0) {
			cluster1.add(dataVal);
		}
		if (centerIndex == 1) {
			cluster2.add(dataVal);
		}
	}

	/**
	 * update center locations in the data
	 */
	public void updateCenters() {
		int clusterSum = 0;
		int clusterAvg = 0;
		int newCenterIndex = 0;
		// empty the list of centers to update
		cenList.clear();

		// get actual average of cluster
		for (int i = 0; i < cluster1.size(); i++) {
			clusterSum += cluster1.get(i);
		}
		clusterAvg = clusterSum / cluster1.size();
		clusterSum = 0;

		// find actual average data point in cluster
		double closest = 1000;
		for (int i = 0; i < cluster1.size(); i++) {
			double num = Math.abs(clusterAvg - cluster1.get(i));
			if (num < closest) {
				closest = num;
				newCenterIndex = i;
			}
		}
		cenList.add(cluster1.get(newCenterIndex)); // add new center to the list of centers
		cluster1.clear(); // reset clusters to be overwritten

		closest = 10000;
		for (int i = 0; i < cluster2.size(); i++) {
			double num = Math.abs(clusterAvg - cluster2.get(i));
			if (num < closest) {
				closest = num;
				newCenterIndex = i;
			}
		}
		cenList.add(cluster2.get(newCenterIndex)); // add new center to the list of centers
		cluster2.clear(); // reset clusters to be overwritten
		
		getClosest(cenList, dataList);
		
		
	}

}
