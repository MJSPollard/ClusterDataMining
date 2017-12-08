package project4;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Handler {

	static Handler handler = new Handler();
	Scanner scan = new Scanner(System.in);
	double[][] data;

	/**
	 * Main method for program
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// checks the working directory
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		handler.DatasetMenu();
		handler.normalizeData();
		handler.AlgorithmMenu();
		System.exit(1);

	}

	/**
	 * Method that reads in the csv to a 2d double type array
	 * 
	 * @param dataset
	 * @param numInstances
	 * @param numAttributes
	 * @return the chosen data set in 2d array format
	 * @throws IOException
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

		printArray(numInstances, numAttributes);

	}

	/**
	 * Method to print data set for testing purposes
	 * 
	 * @param numInstances
	 * @param numAttributes
	 */
	public void printArray(int numInstances, int numAttributes) {
		for (int i = 0; i < numInstances; i++) {
			for (int j = 0; j < numAttributes; j++) {
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
	public String AlgorithmMenu() {
		int choice = 0;

		System.out.println("Enter the number of an Algorithm\n" + "1. K-Means\n" + "2. DB-Scan\n"
				+ "3. Unsupervised Competitive Neural Network\n" + "4. Particle Swarm Optimization\n"
				+ "5. Ant Colony Optimization\n");
		choice = Integer.parseInt(scan.nextLine());
		switch (choice) {
		case 1:
			return "K-Means";
		case 2:
			return "DB-Scan";
		case 3:
			return "UnsupNetwork";
		case 4:
			return "POS";
		case 5:
			return "ACO";
		default:
			System.out.println("Input Error, try again");
			AlgorithmMenu();
			break;
		}
		return "Error, an Algorithm wasn't chosen";

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
		System.out.println("Enter the number of a Dataset\n" + "1. papers2013\n" + "2. papers2014\n"
				+ "3. gesturephase\n" + "4. htru_2\n" + "5. electric\n");
		choice = Integer.parseInt(scan.nextLine());
		switch (choice) {
		case 1:
			handler.getDataSet("Project4_447/src/resources/papers2013.csv", 17898, 9);
			break;
		case 2:
			handler.getDataSet("Project4_447/src/resources/papers2014.csv", 17898, 9);
			break;
		case 3:
			handler.getDataSet("Project4_447/src/resources/gesturephase.csv", 17898, 9);
			break;
		case 4:
			handler.getDataSet("Project4_447/src/resources/htru_2.csv", 17898, 9);
			break;
		case 5:
			handler.getDataSet("Project4_447/src/resources/electric.csv", 17898, 9);
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

	}

}
