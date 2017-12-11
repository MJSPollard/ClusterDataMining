package project4;

/**
 * ACO class uses Ant objects to cluster data based on pheromone
 * influences and distance between their centers and the data.
 * Then it will compute a fitness based on their center values, and save a best.
 * 
 * @authors Hugh Jackovich, Mike Pollard, Cory Petersen
 */

import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

public class ACO
{
	private double[][] pheromoneMatrix, data;
	private final int TotalAnts;
	private Ant[] ants;
	private int size, maxIterations;
	private Ant best;
	private final double alpha = 1;
	private final double beta = 2;

	/**
	 * Constructs an ACO object, that reads in data, clusters
	 * and initializes variables to be used by the optimization
	 * @param data double[][] data read from file
	 * @param clusters int number of clusters
	 */
	public ACO(double[][] data, int clusters)
	{
		this.data = data;
		size = data.length;
		
		//a pheromone towards clusters, given datapoint
		pheromoneMatrix = new double[size][clusters];


		//TotalAnts = (int) (size * .75);
		TotalAnts = 20;

		maxIterations = 10;
	}

	/**
	 * Initializes Ant objects
	 */
	public void initialize()
	{
		ants = new Ant[TotalAnts];
		for (int i = 0; i < TotalAnts; i++)
		{
			ants[i] = new Ant(data.length, data[0].length, pheromoneMatrix[0].length);
		}

	}

	/**
	 * Calls the clear method on each ant
	 */
	private void resetAnts()
	{
		for (int i = 0; i < TotalAnts; i++)
		{
			ants[i].clear();
		}
	}

	/**
	 * Initializes pheromoneMatrix with random values
	 */
	private void setupPM()
	{
		for (int i = 0; i < pheromoneMatrix.length; i++)
		{
			for (int j = 0; j < pheromoneMatrix[0].length; j++)
			{
				pheromoneMatrix[i][j] = Math.random() * .05;
			}
		}
	}

	/**
	 * calculates the next datapoint's cluster by determining the probability
	 * by distance and pheromone factors to Cluster.
	 * @param ant Ant ant to be moved
	 * @param index int instance of the data
	 * @return int cluster index of the winner
	 */
	private int moveAnt(Ant ant, int index)
	{
		double[] dataPoint = data[index];

		double[][] clusters = ant.getAttrCenters();

		double highest = 0;
		int winnerIndex = 0;

		for (int i = 0; i < clusters.length; i++)
		{
			double divider = 0.0;

			//calculates the denominator of the probability
			for(int cluster = 0; cluster < clusters.length; cluster++)
			{
				double pheromoneFactor = Math.pow(pheromoneMatrix[index][cluster], alpha);

				double distanceFactor = calculateDistance(dataPoint, clusters[i]);

				distanceFactor = Math.pow((1 / distanceFactor), beta);

				divider += pheromoneFactor * distanceFactor;
			}
			
			//begins calculating the numerator of the probability
			double distance = calculateDistance(dataPoint, clusters[i]);
			distance = Math.pow((1 / distance), beta);

			double pheromone = Math.pow(pheromoneMatrix[index][i], alpha);

			double total = pheromone * distance;
			
			double clusterProb = total / divider;

			//if the probability is greater than the highest, replace highest
			if (clusterProb > highest)
			{
				highest = clusterProb;
				winnerIndex = i;
			}
		}
		return winnerIndex;
	}

	/**
	 * Calculates the mean distance between a datapoint and the center of a cluster
	 * @param dp double[] datapoint
	 * @param cluster double[] cluster center values
	 * @return double the distance
	 */
	private double calculateDistance(double[] dp, double[] cluster)
	{
		double dist = 0;

		//For every value in both, sum their absolute difference
		for (int i = 0; i < dp.length; i++)
		{
			dist += Math.abs(dp[i] - cluster[i]);
		}
		
		//divide the difference sum by the number of attributes
		dist = dist / dp.length;

		return dist;
	}

	/**
	 * Calculates the fitness of an ant by calculating their overall
	 * mean distance of their center to each datapoint
	 * @param ant Ant current ant's fitness to be calculated
	 * @param data double[][] data read from file
	 */
	public void calculateFitness(Ant ant, double[][] data)
	{
		double fitness = 0;
		boolean[][] activation = ant.getActivation();
		double[][] center = ant.getAttrCenters();
		
		//for every datapoint
		for(int i = 0; i < size; i++)
		{
			//for every cluster
			for(int j = 0; j < pheromoneMatrix[i].length; j++)
			{
				//if that datapoint i belongs to cluster j
                if(activation[i][j])
                {
                	double totalDistance = 0;
                	
                	//for each attribute, sum the distance from the datapoint to the center
                    for(int k = 0; k < data[i].length; k++)
                    {
                    	totalDistance += Math.abs(data[i][k] - center[j][k]);
                    }
                    
                    //divide distance by total items to get mean distance
                    fitness += totalDistance / data[i].length;
                }
			}
		}
		
		ant.setFitness(fitness);
	}
	
	/**
	 * updates pheromoneMatrix by each ant, determined by a general contribution factor
	 * and based on the strength of their fitness function
	 */
	public void updatePheromones()
	{
		for(int i = 0; i < pheromoneMatrix.length; i++)
		{
			for(int j = 0; j < pheromoneMatrix[i].length; j++)
			{
				pheromoneMatrix[i][j] *= .5;
				for(Ant ant : ants)
				{
					double contributionFactor = ants.length / 100;
					pheromoneMatrix[i][j] += ant.getFitness() * contributionFactor;
				}
			}
			
		}
	}

	/**
	 * runs the ACO clustering algorithm
	 */
	public void run()
	{
		setupPM();
		
		best = new Ant(1,1,1);
		best.setFitness(100000000);

		int iter = 0;
		
		//loops for maxIterations
		while (iter < maxIterations)
		{
			resetAnts();

			for (Ant ant : ants)
			{
				ArrayList<Integer> randomDataCounter = getRandomCounter();

				int k = 0;
				//until ant has travelled to every datapoint
				while(ant.finish())
				{
					int dataIndex = randomDataCounter.get(k);
					
					//adds winning cluster index with dataIndex
					ant.addWinner(dataIndex, moveAnt(ant, dataIndex));

					//updates center based on newest datapoint
					ant.updateCenter(data);

					k++;
				}

				//calculates the fitness of an ant's cluster and the entire data
				calculateFitness(ant, data);
				
				//updates pheromoneMatrix
				updatePheromones();
				
				//if better than the best, replace the best
				if(ant.getFitness() < best.getFitness())
				{
					best = new Ant(ant);
				}
			}
			iter++;
			System.out.println("Iteration Best Fitness: " + best.getFitness());
		}
		
		System.out.println("Final Fitness: " + best.getFitness());
	}

	/**
	 * returns shuffled arraylist of the indexes of the data
	 * @return ArrayList<Integer>
	 */
	public ArrayList<Integer> getRandomCounter()
	{
		ArrayList<Integer> dataIndex = new ArrayList<>();
		for (int i = 0; i < size; i++)
		{
			dataIndex.add(i);
		}
		Collections.shuffle(dataIndex);

		return dataIndex;
	}

}
