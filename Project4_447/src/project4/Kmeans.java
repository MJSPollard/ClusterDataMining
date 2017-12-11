package project4;

import java.util.Random;
import java.util.ArrayList;

public class Kmeans {

	// needed instance fields
	double data[][];
	int clusterNum;
	ArrayList<Double>[] clusterList;
	ArrayList<Double> cluster1 = new ArrayList<Double>();
	ArrayList<Double> cluster2 = new ArrayList<Double>();
	ArrayList<Double> cenList = new ArrayList<Double>();
	ArrayList<Double> dataList = new ArrayList<Double>();

	/**
	 * Constructor for the K means clustering algorithm
	 * 
	 * @param data
	 *            - the data set from the UCI ML repository
	 * @param clusterNum
	 *            - the number of clusters (tunable parameter)
	 */
	Kmeans(double data[][], int clusterNum) {
		this.data = data;
		this.clusterNum = clusterNum;
		int maxIterations = 30;
		int i = 0;
		clusterList = (ArrayList<Double>[]) new ArrayList[clusterNum];
		for (i = 0; i < clusterList.length; i++) {
			clusterList[i] = new ArrayList<Double>();
		}

		// checks for errors in parameter values
		if (data.length == 0) {
			System.out.println("The dataset is empty");
			System.exit(0);
		}
		if (clusterNum == 0) {
			System.out.println("There can't be 0 clusters");
			System.exit(0);
		}

		System.out.println("K-Means clustering started");

		// initialize random centers and format data into usable form
		normalize(data, clusterNum, initializeCenters(data, clusterNum));

		// prints size of initial clusters with no centroid updates
		System.out.println();
		System.out.println("Iteration number: 1");
		for (int j = 0; j < clusterNum; j++) {
			System.out.println("cluster " + j + " size " + clusterList[j].size());
		}
		i = 0;

		// updates the number of centroids until max iterations have been reached
		// (tunable)
		// could be terminated immediately when it converges
		while (i < maxIterations) {
			updateCenters();
			System.out.println("Iteration number: " + (i + 2));
			// print out cluster sizes
			for (int j = 0; j < clusterNum; j++) {
				System.out.println("cluster " + j+ " size: " + clusterList[j].size());
			}
			i++;
		}

	}

	/**
	 * Initializes the center of the clusters to random points within the data and
	 * prints them out
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
	 * formates data into usable format
	 * 
	 * @param data
	 *            - the input dataset
	 * @param clusterNum
	 *            - the amount of cluster
	 * @param clusterCenters
	 *            - the cluster centers
	 */
	public void normalize(double[][] data, int clusterNum, double[][] clusterCenters) {
		double dataSum = 0;
		double dataAvg = 0;
		double cenSum = 0;
		double cenAvg = 0;

		// uses averages to compare the data
		// this method was implemented as there didn't seem to be any best option -
		// based off of no free lunch principle
		for (int instanceIT = 0; instanceIT < clusterCenters.length; instanceIT++) {
			for (int attrIT = 0; attrIT < clusterCenters[0].length; attrIT++) {
				cenSum += clusterCenters[instanceIT][attrIT];
			}
			cenAvg = (cenSum / clusterCenters[0].length);
			cenList.add(cenAvg);
			cenSum = 0;
		}
		// delete
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

		// calculates which cluster centroid the data point is closest to
		getClosest(cenList, dataList);
		// delete
		// for (int i = 0; i < dataList.size(); i++) {
		// System.out.println(dataList.get(i));
		// }
		// System.out.println("data size = " + dataList.size());
	}

	/**
	 * Calculates which centroid the datapoint is closest too
	 * 
	 * @param cenList
	 *            - the list of centroids for each cluster
	 * @param dataList
	 *            - the list of of data points
	 */
	public void getClosest(ArrayList<Double> cenList, ArrayList<Double> dataList) {
		int dataIndex = 0;
		int centerIndex = 0;

		// loop through the data
		for (int i = 0; i < data.length; i++) {
			double closest = 10000; // set to arbitrarily high number to be overwritten immediately
			for (int j = 0; j < clusterNum; j++) {
				// figure out distance of each point to center
				double num = Math.abs(cenList.get(j) - dataList.get(i));

				// check which centroid is closest
				if (num < closest) {
					closest = num;
					dataIndex = i;
					centerIndex = j;
				}
			}
			// send information to be added to cluster
			assignToCluster(cenList.get(centerIndex), dataList.get(dataIndex), centerIndex);
		}

	}

	/**
	 * assigns the data to a specific cluster
	 * 
	 * @param cenValue
	 *            - the value of the closest center
	 * @param dataVal
	 *            - the value of the datapoint
	 * @param centerIndex
	 *            - the index of the center
	 */
	public void assignToCluster(double cenValue, double dataVal, int centerIndex) {

		// assign to a specific cluster
		for (int i = 0; i < clusterList.length; i++) {
			if (i == centerIndex) {
				clusterList[i].add(dataVal);
			}
		}

	}

	/**
	 * updates the center locations in the data
	 */
	public void updateCenters() {
		int clusterSum = 0;
		int clusterAvg = 0;
		int newCenterIndex = 0;
		// empty the list of centers to update
		cenList.clear();

		// get actual average of cluster
		for (int j = 0; j < clusterList.length; j++) {
			for (int i = 0; i < clusterList[j].size(); i++) {
				clusterSum += clusterList[j].get(i);
			}
			clusterAvg = clusterSum / clusterList[j].size();
			clusterSum = 0;

			// find actual average data point in cluster
			double closest = 1000;
			for (int i = 0; i < clusterList[j].size(); i++) {
				double num = Math.abs(clusterAvg - clusterList[j].get(i));
				if (num < closest) {
					closest = num;
					newCenterIndex = i;
				}
			}
			cenList.add(clusterList[j].get(newCenterIndex)); // add new center to the list of centers
			clusterList[j].clear(); // reset clusters to be overwritten
		}

		// // find actual average data point in cluster
		// double closest = 1000;
		// for (int i = 0; i < cluster1.size(); i++) {
		// double num = Math.abs(clusterAvg - cluster1.get(i));
		// if (num < closest) {
		// closest = num;
		// newCenterIndex = i;
		// }
		// }
		// cenList.add(cluster1.get(newCenterIndex)); // add new center to the list of
		// centers
		// cluster1.clear(); // reset clusters to be overwritten

		// // get actual average of cluster
		// for (int i = 0; i < cluster2.size(); i++) {
		// clusterSum += cluster2.get(i);
		// }
		// clusterAvg = clusterSum / cluster2.size();
		// clusterSum = 0;
		//
		// // find actual average data point in cluster
		// closest = 10000;
		// for (int i = 0; i < cluster2.size(); i++) {
		// double num = Math.abs(clusterAvg - cluster2.get(i));
		// if (num < closest) {
		// closest = num;
		// newCenterIndex = i;
		// }
		// }
		// cenList.add(cluster2.get(newCenterIndex)); // add new center to the list of
		// centers
		// cluster2.clear(); // reset clusters to be overwritten

		getClosest(cenList, dataList);

	}

}
