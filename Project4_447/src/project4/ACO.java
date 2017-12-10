package project4;

import java.util.Random;
import java.lang.Math;

public class ACO
{
	private double[][] graph, edges;
	private final int TotalAnts;
	private Ant[] ants;
	private double[] probability;
	private int size, bestLength, maxIterations;
	private Random random;
	private int[] bestTour;

	public ACO(double[][] data)
	{
		random = new Random(420);
		graph = data;
		size = graph.length;
		edges = new double[size][size];
		TotalAnts = (int) (size * .8);
		probability = new double[size];
		
		ants = new Ant[TotalAnts];
		for (int i = 0; i < TotalAnts; i++)
		{
			ants[i] = new Ant(size, size);
		}
		
		maxIterations = 500;
	}

//	public void initialize()
//	{
//		ants = new Ant[TotalAnts];
//		for (int i = 0; i < TotalAnts; i++)
//		{
//			ants[i] = new Ant(size, size);
//		}
//
//		setupAnts();
//		clearEdges();
//	}
	
	private void setupAnts()
	{
		for (int i = 0; i < TotalAnts; i++)
		{
			ants[i].clear();
			ants[i].visitCity(random.nextInt(size));
		}
	}
	
	private void clearEdges()
	{
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				edges[i][j] = 1.0;
			}
		}
	}

	private void moveAnts()
	{
		// each ant follows edges...
		int iteration = 0;
		while (iteration < size - 1)
		{
			for (Ant ant : ants)
				ant.visitCity(calculateNextPos(ant));
			iteration++;
		}
	}

	public int calculateNextPos(Ant ant)
	{
		// Small probability to select next position purely randomly
		if (random.nextDouble() < .05)
		{
			int t = random.nextInt(size - ant.trailLength());
			int j = -1;
			for (int i = 0; i < size; i++)
			{
				if (!ant.visited(i))
					j++;
				if (j == t)
					return i;
			}

		}
		// calculate probabilities for each town (stored in probs)
		probTo(ant);
		// randomly select according to probs
		double threshold = random.nextDouble();
		double chance = 0;
		for (int i = 0; i < size; i++)
		{
			chance += probability[i];
			if (chance >= threshold)
				return i;
		}
		return -1;
	}

	// Store in probability array the probability of moving to each town
	// [1] describes how these are calculated.
	// In short: As like to follow stronger and shorter edges more.
	private void probTo(Ant ant)
	{
		int currentCity = ant.getCurrentCity();

		double lower = 0.0;

		for (int i = 0; i < size; i++)
		{
			if (!ant.visited(i))
			{
				lower += Math.pow(edges[currentCity][i], 1) * Math.pow(1 / graph[currentCity][i], 5);
			}

		}
		for (int i = 0; i < size; i++)
		{
			if (ant.visited(i))
			{
				probability[i] = 0.0;
			} else
			{
				double upper = Math.pow(edges[currentCity][i], 1) * Math.pow(1.0 / graph[currentCity][i], 5);

				probability[i] = upper / lower;
			}
		}

	}

	// Update edges based on ants tours
	private void updateTrails()
	{
		// evaporation
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				edges[i][j] *= .5;

		// each ants contribution
		for (Ant ant : ants)
		{
			int[] trail = ant.getTrail();
			double contribution = 500 / ant.trailLength();
			for (int i = 0; i < size - 1; i++)
			{
				edges[trail[i]][trail[i + 1]] += contribution;
			}
			edges[trail[size - 1]][trail[0]] += contribution;
		}
	}

	private void updateBest()
	{
		if (bestTour == null)
		{
			bestTour = ants[0].getTrail();
			bestLength = ants[0].trailLength();
		}
		for (Ant ant : ants)
		{
			if (ant.trailLength() < bestLength)
			{
				bestLength = ant.trailLength();
				bestTour = ant.getTrail().clone();
			}
		}
	}

	public int[] solve()
	{
		clearEdges();

		int iter = 0;
		// run for maxIterations
		// preserve best tour
		while (iter < maxIterations)
		{
			setupAnts();
			moveAnts();
			updateTrails();
			updateBest();
			iter++;
		}
		// Subtract size because we added one to edges on load
		System.out.println("Best tour length: " + (bestLength - size));
		return bestTour.clone();
	}

}
