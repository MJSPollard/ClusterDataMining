package project4;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Handler {

	static Handler handler = new Handler();
	Scanner scan = new Scanner(System.in);
	int count;
	double[][] data;
	double radius;
	int minPoints;

	/**
	 * Main method for program
	 * 
	 * @param args
	 *            the command line argument
	 * @throws IOException
	 *             if file is not found
	 */
	public static void main(String[] args) throws IOException {

		// checks the working directory
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		System.out.println(
				"If data set file cannot be found check working directory above and adjust path in DatasetMenu\n");
		handler.mainLoop();
	}

	/**
	 * main loop that continually runs whole program until exited
	 * 
	 * @throws IOException
	 *             if the file is not found
	 */
	private void mainLoop() throws IOException {
		handler.DatasetMenu();
		handler.normalizeData();
		System.out.println("Data has been normalized");
		handler.AlgorithmMenu();
		mainLoop();
	}

	/**
	 * Method that reads in the csv to a 2d double type array
	 * 
	 * @param dataset
	 *            the read in dataset
	 * @param numInstances
	 *            the number of instances in the dataset
	 * @param numAttributes
	 *            the number of attributes in the dataset
	 * @throws IOException
	 *             if file is not found
	 */
	public void getDataSet(String dataset, int numInstances, int numAttributes) throws IOException {
		String[][] stringArray = new String[numInstances][numAttributes];
		data = new double[numInstances][numAttributes];
		Scanner inFile = new Scanner(new FileReader(dataset));
		int i = 0;
		int j = 0;
		String line1;

		// read in csv file to 2d string array
		while (inFile.hasNextLine()) {
			line1 = inFile.nextLine();
			StringTokenizer st = new StringTokenizer(line1, ",", false);
			j = 0;
			while (st.hasMoreTokens()) {
				stringArray[i][j] = st.nextToken();
				j++;
			}
			i++;
		}
		inFile.close();

		// converts string array to double array
		for (i = 0; i < numInstances; i++) {
			for (j = 0; j < numAttributes; j++) {
				data[i][j] = Double.parseDouble(stringArray[i][j]);
			}
		}

	}

	/**
	 * Method to print data set for testing purposes
	 * 
	 * @param numInstances
	 * @param numAttributes
	 */
	public void printArray() {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				System.out.print(data[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * This method lets the user choose the clustering algorithm to run and
	 * initializes needed things.
	 * 
	 * @return change this to be void and just call algorithms
	 */
	public void AlgorithmMenu() {
		int choice = 0;

		System.out.println("Enter the number of an Algorithm\n" + "1. K-Means\n" + "2. DB-Scan\n"
				+ "3. Unsupervised Competitive Neural Network\n" + "4. Particle Swarm Optimization\n"
				+ "5. Ant Colony Optimization\n");
		choice = Integer.parseInt(scan.nextLine());
		long startTime = System.currentTimeMillis();
		switch (choice) {

		case 1:
			Kmeans kmeans = new Kmeans(data, 2);
			break;
		case 2:
			DBscan dbscan = new DBscan(data, radius, minPoints);
			break;
		case 3:
			UnsupervisedNetwork un = new UnsupervisedNetwork(data[0].length, 2, .02);
			un.initLayers(data);
			for (int i = 0; i < data.length; i++) {
				un.train(data[i]);
				un.evaluateOutput();
				if(i % 1000 == 0)
				{
					System.out.println(un.getFitness());
				}
			}
			un.showClusters();
			break;
		case 4:
			PSO pos = new PSO();
			pos.runPSO(data, 100, 3);
			break;
		case 5:
			ACO aco = new ACO(data, 3);
			aco.initialize();
			aco.run();
			break;
		default:
			System.out.println("Input Error, try again");
			AlgorithmMenu();
			break;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Algorithm time to perform: " + (endTime - startTime) + " ms\n\n");

	}

	/**
	 * This method lets the user choose the dataset to run.
	 * 
	 * @return the chosen dataset
	 * @throws IOException
	 */
	public void DatasetMenu() throws IOException {
		int choice = 0;
		String dataSet = "";
		// user is presented with various printStatements for dataset choice and exit
		// function
		System.out.println("Enter the number of a Dataset\n" + "1. iris\n" + "2. wine_small\n" + "3. wine_big\n"
				+ "4. htru_2\n" + "5. student_eval\n" + "6. Exit\n");
		choice = Integer.parseInt(scan.nextLine());

		// dataset sizes are hard coded in for efficiency
		// radius and minpoint defaulted by dataset
		switch (choice) {
		case 1:
			radius = .1;
			minPoints = 5;
			handler.getDataSet("Project4_447/src/resources/iris.csv", 150, 4);
			break;
		case 2:
			radius = .6;
			minPoints = 12;
			handler.getDataSet("Project4_447/src/resources/wine_small.csv", 178, 14);
			break;
		case 3:
			radius = .09;
			minPoints = 80;
			handler.getDataSet("Project4_447/src/resources/wine_big.csv", 4898, 12);
			break;
		case 4:
			radius = .001;
			minPoints = 100;
			handler.getDataSet("Project4_447/src/resources/htru_2.csv", 17898, 9);
			break;
		case 5:
			radius = .1;
			minPoints = 70;
			handler.getDataSet("Project4_447/src/resources/student_eval.csv", 5820, 33);
			break;
		case 6:
			System.exit(0); // end loop and exit program
			break;
		default:
			System.out.println("Input Error, try again");
			DatasetMenu();
			break;
		}
	}

	/**
	 * Normalizes the data
	 */
	public void normalizeData() {
		double[][] tempData = transpose(handler.data);

		count = 0;
		for (double[] instance : tempData) {
			double[] instanceData = new double[instance.length];

			for (int i = 0; i < instance.length; i++) {
				instanceData[i] = instance[i];
			}
			tempData[count] = handler.normalize(instanceData);
			count++;
		}
		data = transpose(tempData);
	}

	/**
	 * Used in assistance to normalize data
	 * 
	 * @param array
	 *            the array to be transposed
	 * @return the transposed array
	 */

	public double[][] transpose(double[][] array) {
		if (array == null || array.length == 0) {
			return array;
		}

		double[][] newArray = new double[array[0].length][array.length];

		for (int x = 0; x < newArray[0].length; x++) {
			for (int y = 0; y < newArray.length; y++) {
				newArray[y][x] = array[x][y];
			}
		}
		return newArray;
	}

	/**
	 * This method normalizes the data set using Zero-mean, unit variance Formual:
	 * x' = xi - xavg / standard deviation
	 *
	 * @param data
	 *            the data that is not yet normalized
	 * @return the new normalized data set
	 */
	public double[] normalize(double[] data) {
		double sum = 0;
		double mean = 0;
		double meanSum = 0;
		double stdDev = 0;

		double[] newData = new double[data.length];

		for (int j = 0; j < 4; j++) {
			if (j == 1) {
				mean = sum / data.length;
			}
			for (int i = 0; i < data.length; i++) {
				// Switches based upon section of function to run
				switch (j) {
				case 0:
					sum += data[i];
					break;

				case 1:
					meanSum += Math.pow((data[i] - mean), 2);
					break;

				case 2:
					stdDev = Math.sqrt((meanSum / data.length));
					break;

				case 3:
					newData[i] = (data[i] - mean) / stdDev;
					break;

				default:
					break;
				}
			}
		}
		return newData;
	}

}
