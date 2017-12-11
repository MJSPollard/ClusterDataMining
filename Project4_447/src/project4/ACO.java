package project4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;

public class ACO
{
	private double[][] pheromoneMatrix, data;
	private final int TotalAnts;
	private Ant[] ants;
	private int size, maxIterations;
	private Random random;
	private Ant best;
	private final double alpha = .1;
	private final double beta = .5;

	public ACO(double[][] data, int instances, int attr, int clusters)
	{
		random = new Random(420);
		this.data = data;
		pheromoneMatrix = new double[instances][clusters];

		size = data.length;
		//TotalAnts = (int) (size * .5);
		TotalAnts = 200;

		maxIterations = 500;
	}

	public void initialize()
	{
		ants = new Ant[TotalAnts];
		for (int i = 0; i < TotalAnts; i++)
		{
			ants[i] = new Ant(data.length, data[0].length, pheromoneMatrix[0].length);
		}

	}

	private void setupAnts()
	{
		for (int i = 0; i < TotalAnts; i++)
		{
			ants[i].clear();
		}
	}

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

	private int moveAnt(Ant ant, int index)
	{
		double[] dataPoint = data[index];

		double[][] clusters = ant.getAttrCenters();

		double highest = 0;
		int winnerIndex = 0;

		for (int i = 0; i < clusters.length; i++)
		{
			double divider = 0.0;
			int cc = 0;

			for (double[] cluster : clusters)
			{
				double pheromoneFactor = Math.pow(pheromoneMatrix[index][cc], alpha);

				double distanceFactor = calculateDistance(dataPoint, clusters[i]);
				distanceFactor = Math.pow((1 / distanceFactor), beta);

				divider += pheromoneFactor * distanceFactor;
				cc++;
			}

			double distance = calculateDistance(dataPoint, clusters[i]);
			distance = Math.pow((1 / distance), beta);

			double pheromone = Math.pow(pheromoneMatrix[index][i], alpha);

			double total = pheromone * distance;

			double clusterProb = total / divider;

			if (clusterProb > highest)
			{
				highest = clusterProb;
				winnerIndex = i;
			}
		}
		return winnerIndex;
	}

	private double calculateDistance(double[] dp, double[] cluster)
	{
		double dist = 0;

		for (int i = 0; i < dp.length; i++)
		{
			dist += Math.abs(dp[i] - cluster[i]);
		}

		dist = dist / dp.length;

		return dist;
	}

	public void calculateFitness(Ant ant, double[][] data)
	{
		double fitness = 0;
		boolean[][] activation = ant.getActivation();
		double[][] center = ant.getAttrCenters();
		
		for(int i = 0; i < data.length; i++)
		{
			for(int j = 0; j < pheromoneMatrix[0].length; j++)
			{
                if(activation[i][j])
                {
                    for(int k = 0; k < data[i].length; k++)
                    {
                    	fitness += Math.abs(data[i][k] - center[j][k]);
                    }
                    
                    fitness = fitness / data[i].length;
                    break;
                }
			}
		}
		
		ant.setFitness(fitness);
	}
	
	public void updatePheromones()
	{
		for(int i = 0; i < pheromoneMatrix.length; i++)
		{
			for(int j = 0; j < pheromoneMatrix[i].length; j++)
			{
				pheromoneMatrix[i][j] *= .5;
				for(Ant ant : ants)
				{
					double contributionFactor = ants.length / 500;
					pheromoneMatrix[i][j] += ant.getFitness() * contributionFactor;
				}
			}
			
		}
	}

	public void run()
	{
		setupPM();
		
		best = new Ant(1,1,1);
		best.setFitness(maxIterations);

		int iter = 0;
		// run for maxIterations
		// preserve best tour
		while (iter < maxIterations)
		{
			setupAnts();

			for (Ant ant : ants)
			{
				ArrayList<Integer> randomDataCounter = getRandomCounter();

				int k = 0;
				while (ant.finish())
				{
					int dataIndex = randomDataCounter.get(k);

					ant.addWinner(dataIndex, moveAnt(ant, dataIndex));

					ant.updateCenter(data);

					k++;
				}

				calculateFitness(ant, data);
				
				updatePheromones();
				
				if(ant.getFitness() < best.getFitness())
				{
					best = new Ant(ant);
				}
			}
			iter++;
			System.out.println("Best Fitness: " + best.getFitness());
		}
		
		System.out.println("Final Fitness: " + best.getFitness());
	}

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
