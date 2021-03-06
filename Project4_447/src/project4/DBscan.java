package project4;

import java.util.ArrayList;

public class DBscan {

	// core points = points in a radius
	// border points = points within theta distance of a core point
	// noise = points that are not core or border points
	// instance fields
	double data[][];
	double radiusDistance; // aka: theta
	int minPoints; // the mimimum amount of core point for a radius to be a cluster
	ArrayList<Double> dataList = new ArrayList<Double>();
	ArrayList<Double> dataAroundCore = new ArrayList<Double>();
	ArrayList<ArrayList<Double>> allClusters = new ArrayList<ArrayList<Double>>();
	double[] labelData;

	/**
	 * Constructor for DBscan - a density based clustering algorithm
	 * 
	 * @param data
	 *            - the input data set
	 */
	DBscan(double data[][], double radiusDistance, int minPoints) {
		this.data = data;
		this.radiusDistance = radiusDistance;
		this.minPoints = minPoints;
		labelData = new double[data.length]; // variable that keeps track of indices of core points and border points
		int coreCount = 0;
		int borderCount = 0;

		// check parameter errors
		if (data.length == 0) {
			System.out.println("The dataset is empty");
			System.exit(0);
		}

		// initialize all labels to be 0
		for (int i = 0; i < labelData.length; i++) {
			labelData[i] = 0;
		}

		System.out.println("DB-Scan clustering started");
		System.out.println("theta (radius) = " + radiusDistance);
		System.out.println("min allowed points per original cluster = " + minPoints);

		// setup and find points
		normalize(data);
		findCorePoints();
		findBorderPoints();

		// count special data points for viewer to see
		for (int i = 0; i < labelData.length; i++) {
			if (labelData[i] == 2) {
				coreCount++;
			}
			if (labelData[i] == 1) {
				borderCount++;
			}
		}

		System.out.println("Original number of core points in Data: " + coreCount);
		System.out.println("Original number of border Points in Data: " + borderCount);
		System.out.println("Expanded final clusters: ");
		// finds data in radius and expands clusters
		addDataAroundCoreToCluster();

	}

	/**
	 * Puts the data in a usable format
	 * 
	 * @param data
	 *            - the read in data set
	 * @return
	 */
	public void normalize(double[][] data) {
		double dataSum = 0;
		double dataAvg = 0;

		for (int instanceIT = 0; instanceIT < data.length; instanceIT++) {
			for (int attrIT = 0; attrIT < data[0].length; attrIT++) {
				dataSum += data[instanceIT][attrIT];
			}
			dataAvg = (dataSum / data[0].length);
			dataList.add(dataAvg);
			dataSum = 0;
		}
	}

	/**
	 * Method that decides whether a data point has enough data points around it to
	 * be a core point
	 */
	public void findCorePoints() {
		int pointCount = 0;

		// loop through data set and test if a point meets centroid requirements
		for (int i = 0; i < dataList.size(); i++) {
			pointCount = 0;
			for (int j = 0; j < dataList.size(); j++) {

				// check if point within radius of other points
				if ((dataList.get(j) > (dataList.get(i) + radiusDistance))
						|| (dataList.get(j) < (dataList.get(i) - radiusDistance))) {
					// do nothing - point not in radius
				} else {
					pointCount++;
				}
			}
			// checks to see point in radius are above minimum points required
			if (pointCount >= minPoints) {
				labelData[i] = 2; // data at index i is a core point, it has enough points around it, mark it;
			}
		}

	}

	/**
	 * Method that sets the data labels for border points
	 */
	public void findBorderPoints() {
		for (int i = 0; i < dataList.size(); i++) {
			// if not core point check to see if border point
			if (labelData[i] != 2) {
				for (int j = 0; j < dataList.size(); j++) {
					if (labelData[j] == 2) {
						if ((dataList.get(i) > (dataList.get(j) + radiusDistance))
								|| (dataList.get(i) < (dataList.get(j) - radiusDistance))) {
							// do nothing, not a border point
						} else {
							labelData[i] = 1; // mark in label index as border
						}
					}
				}
			}
		}
	}

	/**
	 * Expands the current cluster by finding all points around the found core index
	 * 
	 * @param coreIndex
	 *            - the location of the found core in a radius of a cluster
	 */
	public void expandCluster(int coreIndex) {
		for (int j = 0; j < labelData.length; j++) {
			if ((dataList.get(j) > (dataList.get(coreIndex) + radiusDistance))
					|| (dataList.get(j) < (dataList.get(coreIndex) - radiusDistance))) {
				// do nothing, data point not in this radius
			} else {
				// in radius
				if (labelData[j] == 0) { // check if already visted
					dataAroundCore.add(dataList.get(j));
				}
			}
		}
	}

	/**
	 * calculates and adds points to a cluster
	 */
	public void addDataAroundCoreToCluster() {
		int count = 0;

		for (int i = 0; i < labelData.length; i++) {
			// if core point then check surroundings
			if (labelData[i] == 2) {
				labelData[i] += .001; // mark core point as visited
				// cycle through all data
				for (int j = 0; j < labelData.length; j++) {
					// check if point is in radius of core
					if ((dataList.get(j) > (dataList.get(i) + radiusDistance))
							|| (dataList.get(j) < (dataList.get(i) - radiusDistance))) {
						// do nothing, data point not in this radius
					} else {
						dataAroundCore.add(dataList.get(j));
						labelData[j] += .005; // mark visited
					}
				}
			}
			// check if other cores are connected to this core
			for (int j = 0; j < dataAroundCore.size(); j++) {
				for (int k = 0; k < dataList.size(); k++) {
					if (dataAroundCore.get(j) == dataList.get(k)) {
						// check if this is a data point in the current cluster also a core node
						if (labelData[k] == 2) {
							labelData[k] += .001; // mark as visited
							//expand the cluster
							expandCluster(k);
						}

					}

				}

			}

			// shows the user the data from the resulting clustering
			if (dataAroundCore.size() != 0) {
				count++;
				System.out.println("Expanded cluster number: " + count);
				System.out.println("Amount of data in cluster: " + dataAroundCore.size());
				System.out.print("Data in Cluster: ");
				System.out.println(dataAroundCore);
			}
			dataAroundCore.clear();
		}
	}

}
