package project4;

import java.util.ArrayList;

public class Ant
{
	private ArrayList<Integer> trail;
	private boolean[] visited;
	
	public Ant(int instances, int attributes)
	{
		visited = new boolean[instances * attributes];
       // public int trail[] = new int[graph.length];
		trail = new ArrayList<Integer>();
		visited = new boolean[instances];
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
		return trail.get(trail.size());
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
    
    public String toString()
    {
    	return "h";
    }
}
