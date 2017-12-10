package project4;

import java.util.ArrayList;

public class Ant
{
	private ArrayList<Integer> trail;
	private boolean[] visited;
	private int curCity;
	
	public Ant(int mapSize)
	{
		visited = new boolean[mapSize];
	}
	
	public void visitCity(int city){
	    trail.add(city);
	    visited[city] = true;
	}
	 
	public boolean visited(int i)
	{
	    return visited[i];
	}
	 
	public double trailLength(double graph[][]) {
	    double length = graph[trail[trailSize - 1]][trail[0]];
	    for (int i = 0; i < trailSize - 1; i++) {
	        length += graph[trail[i]][trail[i + 1]];
	    }
	    return length;
	}
}
