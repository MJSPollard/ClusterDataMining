package project4;
import project4.Particle.XYClass;
import project4.Particle.speedClass;
import java.util.*;

public class PSO
{
	ArrayList<Particle> swarm = new ArrayList<Particle>();
	double data[][];
	int clusterNum;
	int swarmNum;
	double w = 0.729844;
	double c1, c2 = 1.49618;
	double phi = c1 + c2;
	double chi = 2.0/Math.abs(2.0-phi-Math.sqrt(Math.pow(phi, 2)-4*phi));
	int numPart = 10;
	int numD = 2;
	int numT = 100;
	double[] pBestValue = new double[numPart];
    double[] gBestPosition = new double[numD];
    double[] bestFitnessHistory = new double[numT];
    double[] M = new double[numPart];

   double[][] pBestPosition = new double[numPart][numD];
   double[][] R = new double[numPart][numD];
   double[][] V = new double[numPart][numD];

	ArrayList<Double> cluster1 = new ArrayList<Double>();
	ArrayList<Double> cluster2 = new ArrayList<Double>();
	ArrayList<Double> cenList = new ArrayList<Double>();
	ArrayList<Double> dataList = new ArrayList<Double>();
	
	/*private void generatePSO(){
		
	Random rand = new Random();
	int swarmNum = 10;
	
	for(int i = 0; i < swarmNum; i++) {
		Particle part = new Particle();
		
		double X = rand.nextDouble() * 5.0 + 1.0;
		 double Y = rand.nextDouble() * 3.0 - 1.0;
		 XYClass loc = part.new XYClass(X, Y);
		 part.setLocation(loc);
		 
		 double velX = rand.nextDouble() * 2.0 - 1.0;
		 double velY = rand.nextDouble() * 2.0 - 1.0;
		 part.setSpeed(part.new speedClass(velX, velY));
		 
		 swarm.add(part);
	}
	}*/
	
	public static double fitness(double[] x){
        double retValue = 0;

        for(int i=0; i<x.length; i++){
            retValue = calculateDistance(clusterNum, initializeCenters(data, clusterNum));            
        }

        return retValue;

    }

	
	public void runPSO(double data[][], int swarmNum, int clusterNum) {
		Random rand = new Random();
		this.data = data;
		this.clusterNum = swarmNum;
		this.swarmNum = swarmNum;
		//generatePSO();
		int i = 0;

		if (data.length == 0) {
			System.out.println("The dataset is empty");
			System.exit(0);
		}
		if (swarmNum == 0) {
			System.out.println("There can't be 0 particles");
			System.exit(0);
		}
		
		long startTime = System.currentTimeMillis();
		System.out.println("PSO clustering started");
		
		
		
		double fitness[] = new double[10];
		double bestLoc[] = new double[10];
		ArrayList<XYClass> bestXY = new ArrayList<XYClass>();
		
		
		while(i < swarmNum) {
			swarm.get(i).generateFitness();
			fitness[i] = swarm.get(i).getFitness();
			i++;
		}
		
		for (int k = 0; i < swarmNum; k++) {
			 bestLoc[k] = fitness[k];
			 bestXY.add(swarm.get(k).getLocation());
			 }
		System.out.println("done");
	}
	
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
	
	public double calculateDistance(double[][] data, int clusterNum, double[][] clusterCenters) {
		int finalDistance = 0;
		double dataSum = 0;
		double dataAvg = 0;
		double cenSum = 0;
		double cenAvg = 0;

		// uses averages to compare the distances between the centers and the data
		// this method was implemented as there didn't seem to be a best option - based
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

		return finalDistance;
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
		}

	}
	
}

