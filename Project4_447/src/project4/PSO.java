package project4;
import project4.Particle.XYClass;
import project4.Particle.speedClass;
import java.util.*;

/**
 * 
 * public class PSO, runs the PSO clustering algorithms on the selected data set
 * 
 */
public class PSO
{
	//many parameters for this class, having large functions that need to be updated
	//parameters include swarm boundaries and best values 
	ArrayList<Particle> swarm = new ArrayList<Particle>();
	static double data[][];
	static int clusterNum;
	int swarmNum;
	int closI = 0;
	int in;
	double closest;
	double w = 0.729844;
	double c1, c2 = 1.49618;
	double phi = c1 + c2;
	double r1, r2 = 0;
	double chi = 2.0/Math.abs(2.0-phi-Math.sqrt(Math.pow(phi, 2)-4*phi));
	double xMax = 3;
	double xMin = 0;
	double yMax = 3;
	double yMin = 0;
	double vMax = 1;
	double vMin = 0;
	int numPart = 50;
	int numD = 2;
	int numT = 20;
	double gBestValue = -10000;
	double[] pBestValue = new double[numPart];
    double[] gBestPosition = new double[numD];
    double[] bestFitnessHistory = new double[numT];
    double[] M = new double[numPart];

   double[][] pBestPosition = new double[numPart][numD];
   double[][] R = new double[numPart][numD];
   double[][] V = new double[numPart][numD];
   double[][] clusterCenters;

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
		 double Y = rand.nextDouble() * 3.0 - 1.0;			//potential implementation of swarm optimization of algorithms
		 XYClass loc = part.new XYClass(X, Y);
		 part.setLocation(loc);
		 
		 double velX = rand.nextDouble() * 2.0 - 1.0;
		 double velY = rand.nextDouble() * 2.0 - 1.0;
		 part.setSpeed(part.new speedClass(velX, velY));
		 
		 swarm.add(part);
	}
	}*/
	
	

	/**
	 * PSO method called in handler
	 * @param data
	 * @param swarmNum
	 * @param clusterNum
	 */
	public void runPSO(double data[][], int swarmNum, int clusterNum) {
		//pulls parameters into class
		Random rand = new Random();
		this.data = data;
		this.clusterNum = clusterNum;
		this.swarmNum = swarmNum;
		this.in = in;
		int i = 0;

		//error handling functions
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
		
		initializeCenters(data, clusterNum);
		
		
		double fitness[] = new double[10];
		double bestLoc[] = new double[10];
		ArrayList<XYClass> bestXY = new ArrayList<XYClass>();
		
		//main loop of iteration
		
		for(int j=0; j<numPart; j++){
	         pBestValue[j] = -1000000; 
	     }

	     for(int j=0; j<numPart; j++){  
	         for(int l=0; l<numD; l++){
	             R[j][l] = xMin + (xMax-xMin)*rand.nextDouble();		//starts each particle at random location
	             V[j][l] = vMin + (vMax-vMin)*rand.nextDouble();		//sets particles velocity
	         }
	     }
		
	     for(int j=0; j<numPart; j++){
	            M[j] = fitness(R[j]);			//sets fitness of each particle
	            M[j] = -M[j];
	         
	        }

	     for(int j= 0; j<numT; j++){ 
	            for(int p=0; p<numPart; p++){         
	                for(int l=0; l<numD; l++){    
	                    R[p][l] = R[p][l] + V[p][l];		//sets local best fitness

	                    if(R[p][l] > xMax) 
	                    { R[p][l] = xMax;}
	                    else if(R[p][l] < xMin)    
	                    { R[p][l] = xMin;}
	                }           
	            }   

	            for(int p=0; p<numPart; p++){ 

	                M[p] = fitness(R[p]);		
	          
	            
	                if(M[p] > pBestValue[p]){
	                
	                     pBestValue[p] = M[p];
	                     for(int l=0; l<numD; l++){
	                        pBestPosition[p][l] = R[p][l];		//finds local best location
	                     }
	                 }
	                
	                if(M[p] > gBestValue){
	                    
	                    gBestValue = M[p];          
	                    for(int l=0; l<numD; l++){
	                       gBestPosition[l] =  R[p][l];		//updates best location
	                    }
	                }
	            
	            }
	            bestFitnessHistory[j] = gBestValue;			//stores best fitness history
	        
	            w = yMax - ((yMax-yMin)/numT) * j;
	           
	            for(int p=0; p<numPart; p++){
	            		//updates global best fitness
	                for(int l=0; l<numD; l++){
	                    
	                    r1 = rand.nextDouble();
	                    r2 = rand.nextDouble();
	                    V[p][l] = chi * w * (V[p][l] + r1 * c1 * (pBestPosition[p][l] - R[p][l]) + r2*c2 *(gBestPosition[l] - R[p][l]));
	                    // classic Velocity update formulate 
	                    
	                    if      (V[p][l] > vMax) 
	                    { V[p][l] = vMax; }        
	                    
	                    else if (V[p][l] < vMin) 
	                    { V[p][l] = vMin; }
	                }
	            }
	            //output global best value at current timestep
	            System.out.println("iteration: " + j + " BestValue " + gBestValue);
	        }  
	   
		




		
		/*while(i < swarmNum) {
			swarm.get(i).generateFitness();
			fitness[i] = swarm.get(i).getFitness();
			i++;
		}
		
		for (int k = 0; i < swarmNum; k++) {
			 bestLoc[k] = fitness[k];
			 bestXY.add(swarm.get(k).getLocation());
			 }*/
		long endTime = System.currentTimeMillis();
		System.out.println("PSO performed in " + (endTime - startTime) + " ms");
	}
	
	//fitness function calls functions imported from K-means class to find distances within the data
	public double fitness(double[] in){
        double retValue = 0;

            retValue = calculateDistance(R, clusterNum, clusterCenters);            

        return retValue;

    }
	
	 

	/**
	 * Imported methods from the K-means class (documented there) below
	 * @param data
	 * @param clusterNum
	 * @return
	 */
	public double[][] initializeCenters(double[][] data, int clusterNum) {

		Random rand = new Random();
		clusterCenters = new double[clusterNum][];

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
	
	public double calculateDistance(double[][] R, int clusterNum, double[][] clusterCenters) {
		double finalDistance = 0;
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

		for (int instanceIT = 0; instanceIT < R.length; instanceIT++) {
			for (int attrIT = 0; attrIT < R[0].length; attrIT++) {
				dataSum += R[instanceIT][attrIT];
			}
			dataAvg = (dataSum / R[0].length);
			dataList.add(dataAvg);
			dataSum = 0;
		}

		getClosest(cenList, dataList);
		// for (int i = 0; i < dataList.size(); i++) {
		// System.out.println(dataList.get(i));
		// }
		// System.out.println("data size = " + dataList.size());
		finalDistance = closest;
		return finalDistance;
	}

	/**
	 * Calculates which centroid the data is closest too
	 */
	public void getClosest(ArrayList<Double> cenList, ArrayList<Double> dataList) {
		int dataIndex = 0;
		int centerIndex = 0;

		for (int i = 0; i < R[0].length; i++) {
			closest = 10000; // set to arbitrarily high number to be overwritten immediately
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

