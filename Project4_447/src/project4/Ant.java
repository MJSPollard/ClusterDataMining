package project4;

import java.util.ArrayList;

public class Ant
{
	private ArrayList<Integer> trail;
	private boolean[] visited;
	private int curCity;
	
	public Ant(int instances, int attributes)
	{
		visited = new boolean[instances * attributes];
       // public int trail[] = new int[graph.length];
		trail = new ArrayList<Integer>();
		visited = new boolean[instances];
	}
	
	public void visitCity(int city){
	    trail.add(city);
	    visited[city] = true;
	}
	 
	public boolean visited(int i)
	{
	    return visited[i];
	}
	 
    public double tourLength() {
        double length = graph[tour[n - 1]][tour[0]];
        for (int i = 0; i < n - 1; i++) {
            length += graph[tour[i]][tour[i + 1]];
        }
        return length;
    }

    public void clear()
    {
        for (int i = 0; i < visited.length; i++)
        {
            visited[i] = false;
        }
        trail = new ArrayList<Integer>();
    }
}
