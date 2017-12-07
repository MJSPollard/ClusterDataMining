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
		private RBF[] hiddenLayers;
		private OutputNeuron outputNeuron;
		//private ArrayList<Neuron[]> network;
		private double l_rate, fitness;
		private static final double momentum = .1;
		private double error, prevError = 0;
		//Used HashMap will be used to add fast indexing through the layers
		private HashMap<RBF,Integer> hiddenIndex;
		private HashMap<Double, Integer> inputIndex;
		//hiddenIndex is <Integer, HashMap>, Integer is hidden layer depth, Hashmap are indexes of the layer
		private Random rand;
		
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
			this.inputIndex = clone.inputIndex;
			this.hiddenIndex = clone.hiddenIndex;
		}
	        
	        /**
	         * The constructor for neural network - sets appropriate value for clone
	         * @param inputSize the size of the input layer of the network
	         * @param outputSize the size of the output layer of the network
	         * @param hiddenSize the amount of neurons in each hidden layer
	         * @param depth the amount of hidden layers
	         * @param l_rate the rate at which an algorithm learns
	         */
		public UnsupervisedNetwork(int inputSize, int outputSize, int hiddenSize, double l_rate)
		{
			rand = new Random(420);
			//network = new ArrayList<Neuron[]>();
			//Initialize properties of UnsupervisedNetwork
			this.l_rate = l_rate;
			
			inputLayer = new double[inputSize];
			inputIndex = new HashMap<Double,Integer>();
			
			hiddenLayers = new RBF[hiddenSize];
	 		hiddenIndex = new HashMap<RBF, Integer>();
	 		
	 		outputNeuron = new OutputNeuron();
	 		
	 		initLayers();
		}
		
	        /**
	         * This method is used to initialize all of the layers of the MLP
	         */
		public void initLayers()
		{
			int i,j;
			//Loop adds a Neuron object to the layer
			for(i = 0; i < inputLayer.length; i++)
			{
	 			//inputLayer[i] = data
	 		}									   	
			

			//loop places the Neuron as the HashMap key, with a value of its index
			count = 0;
	 		for(double iter : inputLayer)
	 		{
	 			this.inputIndex.put(iter, count);
	 			count++;
	 		}
	 		
	 		//2.
	 		//Because hiddenLayers may be multi-dimensional, we use two loops
	 		for(i = 0; i < hiddenLayers.length; i++)
	 		{
 				hiddenLayers[i] = new RBF();	//True, we want sigmoid activation
	 		}
	 		
	 		//Firstly creates a new HashMap of each Neuron as the key and its index
	 		count = 0;
	 		for(RBF neuron : hiddenLayers)
	 		{
	 			//Secondly Maps the new hidden layer with its index as the key
	 			hiddenIndex.put(neuron, count);
	 			count++;
	 		}
	 		
	 		//Connects the input layer to the first hidden layer with a random weight
	 		//Connects hidden layers to the next hidden layer with a random weight
	 			//For every neuron in a layer, connect to every neuron in the next hidden layer
	 		//Connects neurons in last hiddenLayer to the outputNeuron with a random weight

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
			for(int i = 0; i < input.length; i++)
			{
				inputLayer[i] = input[i];
			}
			//check output??
		}
		
	        /**
	         * Evaluates the output of the neurons using the softmax function
	         */
		private void evaluateOutput()
		{
			//linear sum of all values produced ai * fi (||x - cj||)
		}
		
		public void setFitness(double fit)
		{
			fitness = fit;
		}
		
		public double[] getInputLayer()
		{
			return inputLayer;
		}
		public RBF[] getHiddenLayers()
		{
			return hiddenLayers;
		}
		public OutputNeuron getoutputNeuron()
		{
			return outputNeuron;
		}
		
		public double getFitness()
		{
			return fitness;
		}
		
		/**
	         * Class for the Neurons that make up part of the MLP 
	         */
		public class OutputNeuron
		{
			private int best, connectCounter, winnerIndex;
			private double highest, output, sum;
			private Synapses biasSynapse;
			private ArrayList<Synapses> connections; //Connections to every Neuron in the next layer
			
	                /**
	                 * @param Activation the Activation value
	                 */
			public OutputNeuron()
			{
				connectCounter = 0;
				best = 0;
			}
			
			public void setConnection(RBF n, double weight)
			{
				/*
				 * sets connections & the weights associated with them
				 */
				connections.add(new Synapses(n, weight));
				connectCounter++;
			}
			
			public void calculateOutput()
			{
				sum = 0;
				highest = 0;
				int i = 0;
				for(Synapses synapse: connections)
				{
					output = synapse.getOutput();
					sum += output;
					if(synapse.getOutput() > highest)
					{
						highest = output;
						winnerIndex = i;
					}
					i++;
				}
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
			private RBF connected;
			private double weight, deltaW, prevDeltaW;
			
			public Synapses(RBF neuron, double weight)
			{
				this.connected = neuron;
				this.weight = weight;
			}
			
			public double getOutput()
			{
				return connected.getOutput() * weight;
			}
			
			public double getWeight()
			{
				return weight;
			}
			
			public void adjustWeight(double weight)
			{
				this.weight = weight;
			}
			
			public RBF getConnector()
			{
				return connected;
			}
			
			public void setDelta(double delta)
			{
				prevDeltaW = deltaW;
				deltaW = delta;
			}
			
			public double getDelta()
			{
				return deltaW;
			}
			
			public double getPrevDeltaW()
			{
				return prevDeltaW;
			}
			public String toString(){
				String line = "";
				line += weight;
				return line;
			}
		}
	}
