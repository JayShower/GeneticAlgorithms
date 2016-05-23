package base;

import java.util.ArrayList;

public class Population {
	
	private ArrayList<Individual> population = new ArrayList<Individual>(0);
	private int size; // Size of population
	private CrossOver crossOver; // Crossover algorithm

	public Population(int size, int genes, int geneLength, CrossOver crossOver, Mutate mutate, CalculateFitness calcFit) {
		this.size = size;
		this.crossOver = crossOver;
		for(int i = 0; i < size; i++) population.add(new Individual(genes, geneLength, calcFit, mutate));
	}

	public static void evolve() {
		
	}
	
	public Individual getIndividual(int i) {
		return population.get(i);
	}
	
	public int getSize() {
		return size;
	}
	
	public String toString() {
		String s = "";
		for(int i = 0; i < population.size(); i++) s+= (i+1) + ":\t" + population.get(i).toString() + "\n";
		return s;
	}
	
}