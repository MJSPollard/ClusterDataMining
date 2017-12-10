package project4;

public class ACO
{
	private double[][] graph;
	private final int TotalAnts = 500;
	private Ant[] ants;
	private int activeAnts, mapSize;
	
	
	public ACO(double[][] data)
	{
		activeAnts = 0;
		graph = data;
		mapSize = data.length * data[0].length;
	}
	
	public void initialize()
	{
		ants = new Ant[TotalAnts];
		for(int i = 0; i < TotalAnts; i++)
		{
			ants[i] = new Ant(mapSize);
		}
	}
	
	public void calculateNextPos()
	{
		
	}
	
	public void calculateDistance()
	{
		
	}
	
}
