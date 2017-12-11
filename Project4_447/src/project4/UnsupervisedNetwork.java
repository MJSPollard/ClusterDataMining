package project4;

/**
 * MLP Network Class that creates an ideal network based around Classification
 * The Network is used by our Evolutionary Classes on datasets from UCI ML Repository
 * 
 * @authors Hugh Jackovich, Mike Pollard, Cory Petersen
 */

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class UnsupervisedNetwork
{
		/**
		 * UnsupervisedNetwork class will construct a single MLP network with varying input,
		 * output, and hidden layer size/dimensions, along with a learning rate constant.
		 * 
		 * @param int inputSize size of the input layer
		 * @param int outputnSize size of the output layer
		 * @param int hiddenSize size of the hidden layer
		 * @param int depth number of layers the hidden layer should have
		 * @param double l_rate the learning rate constant
		 */
		
		//We use ArrayList so that we can get the position of values
		private int count;
		private double[] inputLayer;
		private Neuron[] hiddenLayers;
		private OutputNeuron outputNeuron;
		//private ArrayList<Neuron[]> network;
		private double l_rate, fitness;
		//Used HashMap will be used to add fast indexing through the layers
		private Random random;
		
	        /**
	         * The constructor for neural network - sets appropriate value for clone
	         * @param clone the copy of a network
	         */
		public UnsupervisedNetwork(UnsupervisedNetwork clone)
		{
			//System.out.println("CLONE TEST");
			this.inputLayer = clone.inputLayer;
			this.outputNeuron = clone.outputNeuron;
			this.hiddenLayers = clone.hiddenLayers;
			//this.fitness = clone.fitness;
		}
	        
	        /**
	         * The constructor for neural network - sets appropriate value for clone
	         * @param inputSize the size of the input layer of the network
	         * @param outputSize the size of the output layer of the network
	         * @param hiddenSize the amount of neurons in each hidden layer
	         * @param depth the amount of hidden layers
	         * @param l_rate the rate at which an algorithm learns
	         */
		public UnsupervisedNetwork(int inputSize, int hiddenSize, double l_rate)
		{
			random = new Random(420);
			//network = new ArrayList<Neuron[]>();
			//Initialize properties of UnsupervisedNetwork
			this.l_rate = l_rate;
			
			inputLayer = new double[inputSize];
			
			hiddenLayers = new Neuron[hiddenSize];
	 		
	 		outputNeuron = new OutputNeuron();
	 		
	 		random = new Random(420);
		}
		
	        /**
	         * This method is used to initialize all of the layers of the MLP
	         */
		public void initLayers(double[][] data)
		{
			int i;							   	
			
	 		for(i = 0; i < hiddenLayers.length; i++)
	 		{
	 			int randomInstance = random.nextInt(data.length);
	 			double[] randomData = data[randomInstance];
 				hiddenLayers[i] = new Neuron(randomData, l_rate);
 				outputNeuron.setConnection(hiddenLayers[i]);
	 		}
		}
	
	        /**
	         * Checks for convergence
	         * @return true if the error is less than .002
	         */
		public boolean converge()
		{return true;}
		
	        /**
	         * Used by backprop and calls the evaluation function
	         * @param input a layer of values
	         */
		public void train(double[] input)
		{
			for(int i = 0; i < hiddenLayers.length; i++)
			{
				hiddenLayers[i].setInput(input);
			}
		}
	        /**
	         * Evaluates the output of the neurons using the softmax function
	         */
		public void evaluateOutput()
		{
			outputNeuron.calculateOutput();
		}
		
		public String getFitness()
		{
			String line = "";
			Neuron winner = outputNeuron.getWinner();
			line += "Cluster " +outputNeuron.getWinnerIndex() + " Fitness: ";
			line += (1 / Math.abs(winner.getOutput()));
			return line;
		}
		
		public void showClusters()
		{
			for(int i = 0; i < hiddenLayers.length; i++)
			{
				System.out.println("Cluster " + i + ": " + hiddenLayers[i].getWins() + " wins");
			}
		}
		
		public class Neuron
		{
			private double output, lrate;
			private double[] centers, currentInput;
			private int winCount;
			
			public Neuron(double[] datapoint, double learning_rate)
			{
				centers = new double[datapoint.length];
				lrate = learning_rate;
				for(int i =0; i < datapoint.length; i++)
				{
					centers[i] = datapoint[i];
				}
			}
			
			public void setInput(double[] input)
			{
				output = 0;
				currentInput = input;
				for(int i = 0; i < input.length; i++)
				{
					double temp = Math.abs(currentInput[i] - centers[i]);
					output += (currentInput[i] * centers[i]) * temp;
				}
			}
			
			public double getOutput()
			{
				return output;
			}
			
			public double[] getCenters()
			{
				return centers;
			}
			
			public void updateCenters()
			{
				for(int i = 0; i < currentInput.length; i++)
				{
					double delta = lrate * (currentInput[i] - centers[i]);
					centers[i] += delta;
				}
			}
			
			public void addWin()
			{
				winCount++;
			}
			
			public int getWins()
			{
				return winCount;
			}
		}
		
		/**
         * Class for the Neurons that make up part of the MLP 
         */
		public class OutputNeuron
		{
			private int winnerIndex;
			private double highest, output;
			private Synapses winner;
			
			private ArrayList<Synapses> connections; //Connections to every Neuron in the next layer
			
	                /**
	                 * @param Activation the Activation value
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
			
			public void calculateOutput()
			{
				highest = 0;
				int i = 0;
				for(Synapses synapse: connections)
				{
					output = synapse.getConnector().getOutput();
					if(synapse.getConnector().getOutput() > highest)
					{
						highest = output;
						winnerIndex = i;
					}
					i++;
				}
				updateWinner();
			}
			
			public void updateWinner()
			{
				winner = connections.get(winnerIndex);
				winner.getConnector().addWin();
				winner.getConnector().updateCenters();
			}
			
			public Neuron getWinner()
			{
				return winner.getConnector();
			}
			
			public int getWinnerIndex()
			{
				return winnerIndex;
			}
			
		}
		
        /**
         * Private class Synapses represents a connection to a neuron
		 * and associates a weight with this connection
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
