package project4;

import java.io.File;
import java.util.Scanner;

public class Handler {

	Scanner scan = new Scanner(System.in);
	double[][] data;

	public static void main(String[] args) {
		Handler handler = new Handler();
		

		// Get user options and read in file choice
		String algorithm = handler.AlgorithmMenu();
		String dataset = handler.DatasetMenu() + ".csv";

		try {
			Scanner file = new Scanner(new File(dataset));
		} catch (Exception e) {
			System.out.println("There was an error");
		}

		switch (dataset) {
		case "papers2013.csv":
			handler.data = handler.getDataSet(dataset, 123, 1);
			break;
		case "papers2014.csv":
			handler.data = handler.getDataSet(dataset, 123, 1);
			break;
		case "gesturephase.csv":
			handler.data = handler.getDataSet(dataset, 123, 1);
			break;
		case "htru_2.csv":
			handler.data = handler.getDataSet(dataset, 17898, 8);
			break;
		case "electric.csv":
			handler.data = handler.getDataSet(dataset, 123, 1);
			break;

		}

		handler.printDataset();
	}

	public double[][] getDataSet(String file, int numInstances, int numAttributes) {
		double dataInfo[][] = new double[numInstances][numAttributes];
		
		for(int i = 0; i < numInstances; i++) {
			for(int j = 0; j < numAttributes; j++) {
			
			}		
		}
		return dataInfo;
	}

	public void printDataset() {

	}

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

	public String DatasetMenu() {
		int choice = 0;
		System.out.println("Enter the number of an Algorithm\n" + "1. papers2013\n" + "2. papers2014\n"
				+ "3. gesturephase\n" + "4. htru_2\n" + "5. electric\n");
		choice = Integer.parseInt(scan.nextLine());
		switch (choice) {
		case 1:
			return "papers2013";
		case 2:
			return "papers2014";
		case 3:
			return "gesturephase";
		case 4:
			return "htru_2";
		case 5:
			return "electric";
		default:
			System.out.println("Input Error, try again");
			DatasetMenu();
			break;
		}
		return "Error, a datset wasn't chosen";
	}

	public void normalizeData() {

	}

}
