package project4;

import java.util.ArrayList;

public class Ant
{
	private double[][] attrCenters;
	private boolean[][] activation;
	private boolean[] visited;
	private double fitness;
	
	public Ant(Ant clone)
	{
		this.attrCenters = clone.attrCenters;
		this.activation = clone.activation;
		this.visited = clone.visited;
		this.fitness = clone.fitness;
	}
	
	public Ant(int instances, int attr, int clusters)
	{
		visited = new boolean[instances];
		activation = new boolean[instances][clusters];
		
		attrCenters = new double[clusters][attr];
	}
	 
	public boolean visited(int i)
	{
	    return visited[i];
	}
	
	public void setFitness(double fitness)
	{
		this.fitness = fitness;
	}
	
	public double getFitness()
	{
		return fitness;
	}

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
    
    public void updateCenter(double[][] data)
    {
    	for(int cluster = 0; cluster < attrCenters.length; cluster++)
    	{
    		int count = 0;
    		for(int instance = 0; instance < visited.length; instance++)
    		{
    			if(visited[instance])
    			{
    				for(int attr = 0; attr < attrCenters[cluster].length; attr++)
					{
    					if(count == 0)
    					{
    						attrCenters[cluster][attr] = 0;
    					}
    					attrCenters[cluster][attr] += data[instance][attr];
    					//System.out.println("TOP: " + data[instance][attr] + " || " + attrCenters[cluster][attr]);
					}
    				count++;
    			}
    		}
    		if(count != 0)
    		{
				for(int attr = 0; attr < attrCenters[cluster].length; attr++)
				{
					attrCenters[cluster][attr] = attrCenters[cluster][attr] / count;
					//System.out.println("BOT: " + count + " || " + attrCenters[cluster][attr]);
				}
    		}
    	}
    }
    
    public double[][] getAttrCenters()
    {
    	return attrCenters;
    }
    
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
    
    public boolean[][] getActivation()
    {
    	return activation;
    }

    public void clear()
    {
        for (int i = 0; i < visited.length; i++)
        {
            visited[i] = false;
        }
        
        activation = new boolean[activation.length][activation[0].length];
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
