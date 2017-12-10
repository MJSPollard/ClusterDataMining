package project4;

public class ACO
{
	private double[][] graph;
	private final int TotalAnts;
	private Ant[] ants;
	private double[] probability;
	private int activeAnts;
	
	
	public ACO(double[][] data)
	{
		activeAnts = 0;
		graph = data;
		TotalAnts = (int) (graph.length * .8);
		probability = new double[graph.length];
	}
	
	public void initialize()
	{
		ants = new Ant[TotalAnts];
		for(int i = 0; i < TotalAnts; i++)
		{
			ants[i] = new Ant(graph.length, graph.length);
		}
	}
	
	// Store in probability array the probability of moving to each town
    // [1] describes how these are calculated.
    // In short: As like to follow stronger and shorter trails more.
    private void probTo(Ant A) {
        int i = A.tour[currentIndex];

        double denom = 0.0;
        for (int l = 0; l < n; l++)
            if (!A.visited(l))
                denom += pow(trails[i][l], alpha)
                        * pow(1.0 / graph[i][l], beta);


        for (int j = 0; j < n; j++) {
            if (A.visited(j)) {
                probability[j] = 0.0;
            } else {
                double numerator = pow(trails[i][j], alpha)
                        * pow(1.0 / graph[i][j], beta);
                probability[j] = numerator / denom;
            }
        }

    }
	
	public void calculateNextPos(Ant A)
	{
		 // sometimes just randomly select
        if (rand.nextDouble() < pr) {
            int t = rand.nextInt(n - currentIndex); // random town
            int j = -1;
            for (int i = 0; i < n; i++) {
                if (!A.visited(i))
                    j++;
                if (j == t)
                    return i;
            }

        }
        // calculate probabilities for each town (stored in probs)
        probTo(A);
        // randomly select according to probs
        double r = rand.nextDouble();
        double tot = 0;
        for (int i = 0; i < n; i++) {
            tot += probs[i];
            if (tot >= r)
                return i;
        }
	}
	
	public void calculateDistance()
	{
		
	}
	
}
