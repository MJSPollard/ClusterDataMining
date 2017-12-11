package project4;

/**
 * Ant class is used by ACO to cluster data based on similarities of centers
 * 
 * @authors Hugh Jackovich, Mike Pollard, Cory Petersen
 */

import java.util.ArrayList;

public class Ant
{
	private double[][] attrCenters; //center value for each attribute in each cluster
	private boolean[][] activation; //Which cluster has been chosen by ant w/ dataset
	private boolean[] visited;		//visited list
	private double fitness;
	
	/**
	 * Creates a clone of an Ant class
	 * @param clone Ant to be copied
	 */
	public Ant(Ant clone)
	{
		this.attrCenters = clone.attrCenters;
		this.activation = clone.activation;
		this.visited = clone.visited;
		this.fitness = clone.fitness;
	}
	
	/**
	 * Creates an Ant with parameters to initialize variables
	 * @param instances int number of datapoints
	 * @param attr int number of attributes associated w/ each datapoint
	 * @param clusters int number of clusters to be used
	 */
	public Ant(int instances, int attr, int clusters)
	{
		visited = new boolean[instances];
		activation = new boolean[instances][clusters];
		
		attrCenters = new double[clusters][attr];
	}
	
	/**
	 * returns visited value of datapoint i
	 * @param i int instance
	 * @return Boolean
	 */
	public boolean visited(int i)
	{
	    return visited[i];
	}
	
	/**
	 * Sets the fitness of Ant to the parameter
	 * @param fitness double
	 */
	public void setFitness(double fitness)
	{
		this.fitness = fitness;
	}
	
	/**
	 * returns fitness of the ant
	 * @return double
	 */
	public double getFitness()
	{
		return fitness;
	}

	/**
	 * Add the datapoint to the winning cluster
	 * @param index int of the datapoint
	 * @param winnerIndex int index of the winning cluster
	 */
    public void addWinner(int index, int winnerIndex)
    {
    	visited[index] = true;
    	int loop = activation[index].length;
    	
    	for(int i = 0; i < loop; i++)
    	{
    		if(i == winnerIndex)
    		{
    			activation[index][i] = true;
    		}
    		else
    		{
    			activation[index][i] = false;
    		}
    	}
    }
    
    /**
     * Updates the center of the attrCenters, by adding the data value to the center
     * and then reducing by a factor of added datapoints
     * @param data double[][] data read from file
     */
    public void updateCenter(double[][] data)
    {
    	//for every cluster
    	for(int cluster = 0; cluster < attrCenters.length; cluster++)
    	{
    		int count = 0;
    		
    		//for every instance in the data
    		for(int instance = 0; instance < visited.length; instance++)
    		{
    			if(visited[instance])
    			{
    				//for every attribute in both
    				for(int attr = 0; attr < attrCenters[cluster].length; attr++)
					{
    					if(count == 0)
    					{
    						attrCenters[cluster][attr] = 0;
    					}
    					attrCenters[cluster][attr] += data[instance][attr];
					}
    				count++;
    			}
    		}
    		if(count != 0)
    		{
    			//for every attr in cluster
				for(int attr = 0; attr < attrCenters[cluster].length; attr++)
				{
					attrCenters[cluster][attr] = attrCenters[cluster][attr] / count;
				}
    		}
    	}
    }
    
    /**
     * returns the cluster's center values
     * @return double[][]
     */
    public double[][] getAttrCenters()
    {
    	return attrCenters;
    }
    
    /**
     * checks if ant has visited every datapoint
     * @return boolean false if it has finished
     */
    public boolean finish()
    {
    	for(boolean visit: visited)
    	{
    		if(!visit)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * returns the clusters activated for datapoints
     * @return boolean[][]
     */
    public boolean[][] getActivation()
    {
    	return activation;
    }

    /**
     * resets the ants variables
     */
    public void clear()
    {
        for (int i = 0; i < visited.length; i++)
        {
            visited[i] = false;
        }
        
        activation = new boolean[activation.length][activation[0].length];
        attrCenters = new double[attrCenters.length][attrCenters[0].length];
    }
    
    /**
     * Converts a Integer arraylist into a int array
     * @param intList the current Integer arraylist
     * @return the new int array
     */
    public static int[] convertToArray(ArrayList<Integer> intList)
    {
        int[] result = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++)
        {
            result[i] = intList.get(i);
        }
        return result;
    }
}
