package project4;

import java.util.ArrayList;

public class Ant
{
	private ArrayList<Integer> trail;
	private double[][] attrCenters;
	private boolean[] visited;
	
	public Ant(int instances, int attr, int clusters)
	{
		visited = new boolean[instances];
		
		attrCenters = new double[clusters][attr];
		trail = new ArrayList<Integer>();
	}
	
	public void visitCity(int city)
	{
	    trail.add(city);
	    visited[city] = true;
	}
	 
	public boolean visited(int i)
	{
	    return visited[i];
	}
	
	public int getCurrentCity()
	{
		return trail.get(trail.size()-1);
	}
    public int trailLength()
    {
        return trail.size();
    }
    
    public int[] getTrail()
    {
    	int[] arrayTrail = convertToArray(trail);
    	return arrayTrail;
    }
    public void addWinner(int index, int winnerIndex)
    {
    	visited[index] = true;
    	int loop = attrCenters[index].length;
    	
    	for(int i = 0; i < loop; i++)
    	{
    		if(i == winnerIndex)
    		{
    			attrCenters[index][i] = 1;
    		}
    		else
    		{
    			attrCenters[index][i] = 0;
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
					}
    				count++;
    			}
    		}
    		for(int attr = 0; attr < attrCenters[cluster].length; attr++)
    		{
    			if(count > 0)
    			{
    				attrCenters[cluster][attr] = attrCenters[cluster][attr] / count;
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
    			return false;
    		}
    	}
    	return true;
    }

    public void clear()
    {
        for (int i = 0; i < visited.length; i++)
        {
            visited[i] = false;
        }
        trail = new ArrayList<Integer>();
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
