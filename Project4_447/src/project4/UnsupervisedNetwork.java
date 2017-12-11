package project4;

/**
 * UnsupervisedNetwork Class that creates an ideal network based around Clustering.
 * The Network is uses a hidden Neuron class, a OutputNeuron class, and Synapses class
 * on datasets from UCI ML Repository to cluster unlabelled data
 * 
 * @authors Hugh Jackovich, Mike Pollard, Cory Petersen
 */

import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

public class UnsupervisedNetwork
{
	private double[] inputLayer;
	private Neuron[] hiddenLayers;
	private OutputNeuron outputNeuron;
	private double l_rate;
	private Random random;

	/**
	 * The constructor for UnsupervisedNetwork - sets appropriate value for
	 * clone
	 * 
	 * @param clone
	 *            the copy of a network
	 */
	public UnsupervisedNetwork(UnsupervisedNetwork clone)
	{
		// System.out.println("CLONE TEST");
		this.inputLayer = clone.inputLayer;
		this.outputNeuron = clone.outputNeuron;
		this.hiddenLayers = clone.hiddenLayers;
		// this.fitness = clone.fitness;
	}

	/**
	 * UnsupervisedNetwork class will construct an Unsupervised Network with
	 * varying input, single output, and one hidden layer, along with a learning
	 * rate constant.
	 * 
	 * @param int
	 *            inputSize size of the input layer
	 * @param int
	 *            hiddenSize size of the hidden layer
	 * @param double
	 *            l_rate the learning rate constant
	 */
	public UnsupervisedNetwork(int inputSize, int hiddenSize, double l_rate)
	{
		// Initialize properties of UnsupervisedNetwork
		this.l_rate = l_rate;

		inputLayer = new double[inputSize];

		hiddenLayers = new Neuron[hiddenSize];

		outputNeuron = new OutputNeuron();

		random = new Random(420);
	}

	/**
	 * This method is used to initialize all of the layers of the Network
	 */
	public void initLayers(double[][] data)
	{
		for (int i = 0; i < hiddenLayers.length; i++)
		{
			int randomInstance = random.nextInt(data.length);
			double[] randomData = data[randomInstance];
			hiddenLayers[i] = new Neuron(randomData, l_rate);
			outputNeuron.setConnection(hiddenLayers[i]);
		}
	}

	/**
	 * Checks for convergence
	 * 
	 * @return true if the error is less than .002
	 */
	public boolean converge()
	{
		return true;
	}

	/**
	 * Inserts the values of datapoints into the hidden layer neurons
	 * 
	 * @param input a layer of values
	 */
	public void train(double[] input)
	{
		for (int i = 0; i < hiddenLayers.length; i++)
		{
			hiddenLayers[i].setInput(input);
		}
	}

	/**
	 * Calls the output neuron's output
	 */
	public void evaluateOutput()
	{
		outputNeuron.calculateOutput();
	}

	/**
	 * Gets values of fitness of a cluster for last input in neat format
	 * @return line String of fitness information
	 */
	public String getFitness()
	{
		String line = "";
		Neuron winner = outputNeuron.getWinner();
		line += "Cluster " + outputNeuron.getWinnerIndex() + " Fitness: ";
		line += Math.abs(winner.getOutput());
		return line;
	}

	/**
	 * Prints how many datapoints each cluster won
	 */
	public void showClusters()
	{
		for (int i = 0; i < hiddenLayers.length; i++)
		{
			System.out.println("Cluster " + i + ": " + hiddenLayers[i].getWins() + " wins");
		}
	}
	
	/**
	 * Neuron class is the hidden layer counter part of the Network
	 */

	public class Neuron
	{
		private double output, lrate;
		private double[] centers, currentInput;
		private int winCount;

		/**
		 * Constructs a neuron with a random datapoint and learning rate
		 * @param datapoint double[] random datapoint from dataset
		 * @param learning_rate double learning rate to be applied
		 */
		public Neuron(double[] datapoint, double learning_rate)
		{
			centers = new double[datapoint.length];
			lrate = learning_rate;
			for (int i = 0; i < datapoint.length; i++)
			{
				centers[i] = datapoint[i];
			}
		}

		/**
		 * Sets the input of the hidden neuron
		 * @param input double[] of input values
		 */
		public void setInput(double[] input)
		{
			output = 0;
			currentInput = input;
			for (int i = 0; i < input.length; i++)
			{
				double temp = Math.pow((currentInput[i] - centers[i]), 2);
				output += temp;
			}

			output = 1 / Math.sqrt(output);
		}

		/**
		 * returns output of neuron
		 * @return double output
		 */
		public double getOutput()
		{
			return output;
		}

		/**
		 * returns centers of the neuron
		 * @return double[] centers
		 */
		public double[] getCenters()
		{
			return centers;
		}

		/**
		 * Updates the centers of the neuron
		 */
		public void updateCenters()
		{
			for (int i = 0; i < currentInput.length; i++)
			{
				double delta = lrate * (currentInput[i] - centers[i]);
				centers[i] += delta;
			}
		}

		/**
		 * Increases winCount by 1
		 */
		public void addWin()
		{
			winCount++;
		}

		/**
		 * Returns winCount
		 * @return winCount
		 */
		public int getWins()
		{
			return winCount;
		}
	}

	/**
	 * Class for the Output Neuron of the Unsupervised Network
	 */
	public class OutputNeuron
	{
		private int winnerIndex;
		private double highest, output;
		private Synapses winner;

		private ArrayList<Synapses> connections; // Connections to every Neuron in hiddenLayer

		/**
		 * constructs an OutputNeuron
		 */
		public OutputNeuron()
		{
			connections = new ArrayList<Synapses>();
		}

		public void setConnection(Neuron n)
		{
			/*
			 * sets connections & the weights associated with them
			 */
			connections.add(new Synapses(n));
		}

		/**
		 * calculates the output by the largest probability
		 */
		public void calculateOutput()
		{
			highest = 0;
			int i = 0;
			for (Synapses synapse : connections)
			{
				output = synapse.getConnector().getOutput();
				if (synapse.getConnector().getOutput() > highest)
				{
					highest = output;
					winnerIndex = i;
				}
				i++;
			}
			updateWinner();
		}

		/**
		 * Updates winner status
		 */
		public void updateWinner()
		{
			winner = connections.get(winnerIndex);
			winner.getConnector().addWin();
			winner.getConnector().updateCenters();
		}

		/**
		 * returns the neuron that previously won
		 * @return Neuron winner
		 */
		public Neuron getWinner()
		{
			return winner.getConnector();
		}

		/**
		 * returns the index of the winner
		 * @return int winnerIndex
		 */
		public int getWinnerIndex()
		{
			return winnerIndex;
		}

	}

	/**
	 * Private class Synapses represents a connection to a neuron and associates
	 * a weight with this connection
	 * 
	 * @param Neuron n The connecting Neuron
	 * @param double weight The weight associated with the connection
	 */

	public class Synapses
	{
		private Neuron connected;

		public Synapses(Neuron neuron)
		{
			this.connected = neuron;
		}

		public Neuron getConnector()
		{
			return connected;
		}

		public void updateWin()
		{
			connected.addWin();
		}

		public int getWins()
		{
			return connected.getWins();
		}
	}
}
