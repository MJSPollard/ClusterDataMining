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
		private int count, count2, i;
		private Neuron[] inputLayer, outputLayer, hiddenLayers;
		//private ArrayList<Neuron[]> network;
		private double l_rate, fitness;
		private static final double momentum = .1;
		private double error, prevError = 0;
		//Used HashMap will be used to add fast indexing through the layers
		private HashMap<Neuron,Integer> inputIndex, outputIndex, hiddenIndex;
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
			this.outputLayer = clone.outputLayer;
			this.hiddenLayers = clone.hiddenLayers;
			//this.fitness = clone.fitness;
			this.inputIndex = clone.inputIndex;
			this.outputIndex = clone.outputIndex;
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
			
			inputLayer = new Neuron[inputSize];
			inputIndex = new HashMap<Neuron,Integer>();
			
			hiddenLayers = new Neuron[hiddenSize];
	 		hiddenIndex = new HashMap<Neuron, Integer>();
	 		
	 		outputLayer = new Neuron[outputSize];
	 		outputIndex = new HashMap<Neuron,Integer>();
	 		
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
	 			inputLayer[i] = new Neuron(0); 	 	//False because we don't want
	 												//Sigmoid to activate on input layer
	 		}									   	
			

			//loop places the Neuron as the HashMap key, with a value of its index
			count = 0;
	 		for(Neuron iter : inputLayer)
	 		{
	 			this.inputIndex.put(iter, count);
	 			count++;
	 		}
	 		
	 		//2.
	 		//Because hiddenLayers may be multi-dimensional, we use two loops
	 		for(i = 0; i < hiddenLayers.length; i++)
	 		{
	 			Neuron[] layer = new Neuron[hiddenLayers[i].length];
	 			for(j = 0; j < layer.length; j++)
	 			{
	 				layer[j] = new Neuron(1);	//True, we want sigmoid activation
	 				//layer[j].setBias(new Neuron(0));
	 			}
	 			hiddenLayers[i] = layer;
	 		}
	 		
	 		//Firstly creates a new HashMap of each Neuron as the key and its index
	 		count = 0;
	 		for(Neuron[] row : hiddenLayers)
	 		{
	 			HashMap<Neuron,Integer> hid_layer = new HashMap<Neuron,Integer>();
	 			count2 = 0;
	 			for(Neuron neuron : row)
	 			{
	 				hid_layer.put(neuron, count2);
	 				count2++;
	 			}
	 			//Secondly Maps the new hidden layer with its index as the key
	 			hiddenIndex.put(count, hid_layer);
	 			count++;
	 		}
	 		
	 		//3.
	 		for(i = 0; i < outputLayer.length; i++)
	 		{
	 			outputLayer[i] = new Neuron(2);
	 			//outputLayer[i].setBias(new Neuron(0));
	 		}
	 		
	 		count = 0;
	 		for(Neuron output : outputLayer)
	 		{
	 			outputIndex.put(output, count);
	 			count++;
	 		}
	 		
	 		//Connects the input layer to the first hidden layer with a random weight
	 		for(Neuron input : inputLayer)
	 		{
	 			for(Neuron hidden : hiddenLayers[0])
	 			{
	 				input.setConnection(hidden, (rand.nextDouble() * 2 - 1) / 2);
	 			}
	 		}
	 		//Connects hidden layers to the next hidden layer with a random weight
	 		for(i = 0; i < hiddenLayers.length; i++)
	 		{
	 			//For every neuron in a layer, connect to every neuron in the next hidden layer
	 			for(Neuron hidden : hiddenLayers[i])
	 			{
	 				if(i != hiddenLayers.length-1)
	 				{
		 				for(Neuron next_layer_hid : hiddenLayers[i+1])
		 				{
		 					hidden.setConnection(next_layer_hid, (rand.nextDouble() * 2 - 1) / 2);
		 				}
	 				}
	 			}
	 		}
	 		//Connects neurons in last hiddenLayer to the outputLayer with a random weight
	 		for(Neuron hidden : hiddenLayers[hiddenLayers.length-1])
	 		{
	 			for(Neuron output : outputLayer){
	 				hidden.setConnection(output, (rand.nextDouble() * 2 - 1) /2);
	 			}
	 		}
		}
		
	        /**
	         * gets the error from the algorithm when compared to the expected values
	         * @param expectedValues the correct values for the data set
	         * @return the error rate
	         */
		public double getError(double[] expectedValues)
		{
			prevError = error;
			error = 0;
			count = 0;
			
			for(Neuron output : outputLayer)
			{
				//System.out.println("Out: " + output.getOutput() + " || " + expectedValues[count]);
				error += Math.pow((output.getOutput() - expectedValues[count]), 2);
				count++;
			}

			error = error / expectedValues.length;
			
			return error;
		}
		
	        /**
	         * Checks for convergence
	         * @return true if the error is less than .002
	         */
		public boolean converge()
		{
			double diff = Math.abs(prevError - error);
			if(diff > .002)
			{
				return true;
			}
			return false;
		}
		
	        /**
	         * Used by backprop and calls the evaluation function
	         * @param input a layer of values
	         */
		public void train(double[] input)
		{
			for(int i = 0; i < input.length; i++)
			{
				inputLayer[i].setValue(input[i]);
			}
			evaluateOutput();
		}
		
	        /**
	         * Classifies in accordance with the output layers
	         * @param input a layer of values
	         * @return the result of the classification for each output
	         */
		public double[] classify(double[] input)
		{
			for(i = 0; i < input.length; i++)
			{
				inputLayer[i].setValue(input[i]);
			}
			double[] result = new double[outputLayer.length];
			evaluateOutput();
			for(i = 0; i < result.length; i++)
			{
				result[i] = outputLayer[i].getOutput();
			}
			return result;
		}
		
	        /**
	         * Evaluates the output of the neurons using the softmax function
	         */
		private void evaluateOutput()
		{
			double totalSum = 0;
			
			for(Neuron n : outputLayer)
			{
//				System.out.println("n: " + n.getSum());
//				if(n.getSum() == 0)
//				{
//					System.out.println("in: " + n.getOutput());
//				}
				totalSum += Math.exp(n.getSum());
			}
			for(Neuron n : outputLayer)
			{
				if(totalSum == 0)
				{
					System.out.println("Avoided Division by Zero - Results may be Altered");
					n.setOutput(.3333);
				}
				double output = Math.exp(n.getSum())/totalSum;
				//n.setOutput(output + n.getBias().getWeight()*n.bias);
				n.setOutput(output);
			}
		}
		
		public void setFitness(double fit)
		{
			fitness = fit;
		}
		
		public Neuron[] getInputLayer()
		{
			return inputLayer;
		}
		public Neuron[] getHiddenLayers()
		{
			return hiddenLayers;
		}
		public Neuron[] getOutputLayer()
		{
			return outputLayer;
		}
		
		public double getFitness()
		{
			return fitness;
		}
		
		/**
	         * Class for the Neurons that make up part of the MLP 
	         */
		public class Neuron
		{
			private int Activation;
			private int counter, connectCounter;
			private double input, output, sum;
			private Synapses biasSynapse;
			private final double bias = 1;
			private ArrayList<Synapses> connections; //Connections to every Neuron in the next layer
			
	                /**
	                 * @param Activation the Activation value
	                 */
			public Neuron(int Activation)
			{
				this.Activation = Activation;
				connections = new ArrayList<Synapses>();;
			}
			
	                /**
	                 * The ReLu activation function
	                 * @param sum used by the activation function
	                 * @return the sum or .0001
	                 */
			public double activate(double sum)
			{
				return 1;
			}
			
	                /**
	                 * sets the connection between neuron and weight
	                 * @param n the neuron
	                 * @param weight the weight
	                 */
			public void setConnection(Neuron n, double weight)
			{
				/*
				 * sets connections & the weights associated with them
				 */
				connections.add(new Synapses(n, weight));
				n.incrConnection();
			}
			
	                /**
	                 * @param input the input value 
	                 */
			public void setValue(double input)
			{
				this.input += input;
				counter++;
				if(counter >= connectCounter)
				{
					if(input == 0)
					{
						System.out.println(counter);
					}
					sum = input;
					setOutput();
				}
			}
			
	                /**
	                 * @param sum the sum for the activation function
	                 * @return the sigmoid function value
	                 */
			public double getSig(double sum)
			{
				return(1.0 / (1 + Math.exp(-sum)));
			}
			
	                /**
	                 * @return the sum for activation function
	                 */
			public double getSum()
			{
				return sum;
			}
			
	                /**
	                 * This method sets the outputs in accordance with the activation function
	                 */
			public void setOutput()
			{
				for(Synapses synapse : connections)
				{
					if(Activation != 0)
					{
						//if(outputIndex.get(synapse.getConnector()))
						synapse.getConnector().setValue(activate(input)*synapse.getWeight());
					}
					else
					{
						synapse.getConnector().setValue(input*synapse.getWeight());
					}
				}
				if(Activation == 1)
				{
					//output = getSig(input) + biasSynapse.getWeight() * bias;
					output = activate(input);
				}
				else if(Activation == 0)
				{
					output = input;
				}
				
				input = 0;
				counter = 0;
			}
	                
	                /**
	                 * @param value the value that output will equal
	                 */
			public void setOutput(double value)
			{
				output = value;
			}
			
	                /**
	                 * @return The value of the weights
	                 */
			public double[] getWeights()
			{
				double[] weights = new double[connections.size()];
				for(i = 0; i < connections.size(); i++)
				{
					weights[i] = connections.get(i).getWeight();
				}
				return weights;
			}
			
	                /**
	                 * @return the list of neuron connections
	                 */
			public ArrayList<Synapses> getConnections()
			{
				return connections;
			}
		
	                /**
	                 * @return the output of the network
	                 */
			public double getOutput()
			{
				return output;
			}
			
	                /**
	                 * Count the network connections
	                 */
			public void incrConnection()
			{
				connectCounter++;
			}
			
	                /**
	                 * @param the intended neuron with a bias
	                 */
			public void setBias(Neuron n)
			{
				biasSynapse = new Synapses(n, 1);
				connections.add(biasSynapse);
				incrConnection();
			}
			
	                /**
	                 * @return the value of the bias
	                 */
			public Synapses getBias()
			{
				return biasSynapse;
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
			private double weight, deltaW, prevDeltaW;
			
			public Synapses(Neuron n, double weight)
			{
				this.connected = n;
				this.weight = weight;
			}
			
			public double getWeight()
			{
				return weight;
			}
			
			public void adjustWeight(double weight)
			{
				this.weight = weight;
			}
			
			public Neuron getConnector()
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
