package project4;
import java.util.Random;

import project4.Particle.XYClass;
import project4.Particle.speedClass;
import java.util.*;

public class PSO
{
	ArrayList<Particle> swarm = new ArrayList<Particle>();
	private void generatePSO(){
		
	Random rand = new Random();
	int swarmNum = 10;
	
	for(int i = 0; i < swarmNum; i++) {
		Particle part = new Particle();
		
		double X = rand.nextDouble() * 5.0 + 1.0;
		 double Y = rand.nextDouble() * 3.0 - 1.0;
		 part.setLocation(new XYClass(X, Y));
		 
		 double velX = rand.nextDouble() * 2.0 - 1.0;
		 double velY = rand.nextDouble() * 2.0 - 1.0;
		 part.setSpeed(new speedClass(velX, velY));
		 
		 swarm.add(part);
	}
	}
	
	public void runPSO() {
		Random rand = new Random();
		generatePSO();
		int i = 0;
		int swarmNum = 10;
		double fitness[] = new double[10];
		double bestLoc[] = new double[10];
		ArrayList<XYClass> bestXY = new ArrayList<XYClass>();
		
		
		while(i < swarmNum) {
			swarm.get(i).generateFitness();
			fitness[i] = swarm.get(i).getFitness();
		}
		
		for (int k = 0; i < swarmNum; i++) {
			 bestLoc[i] = fitness[i];
			 bestXY.add(swarm.get(i).getLocation());
			 }
	}
}

