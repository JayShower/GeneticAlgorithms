package base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public abstract class Population<T> {

	protected ArrayList<Individual>	population		= new ArrayList<Individual>(0);
	protected T						data;
	protected int					generation		= 0;
	protected double				mutationRate	= 1;
	protected double				totalFitness;

	public Population(int size, int genes, int geneLength, T data) {
		if (size % 2 != 0)
			size++;
		this.data = data;
		for (int i = 0; i < size; i++)
			population.add(new Individual(genes, geneLength));
		sort();
	}

	public void run(int generations) {
		for (int i = generation; i < generation + generations; i++) {
			evolve();
		}
	}

	public void run() {
		while (population.get(0).getFitness() != 0) {
			evolve();
		}
	}

	protected void evolve() {
		generation++;
		ArrayList<Individual> newPop = new ArrayList<>(0);
		while (newPop.size() < population.size()) {
			// much frustration caused by not cloning here
			Individual a = pickIndividual().clone();
			Individual b = pickIndividual().clone();
			crossOver(a, b);
			mutate(a);
			mutate(b);
			newPop.add(a);
			newPop.add(b);
		}
		population = newPop;
		sort();
	}

	protected void calculatePopulationFitness() {
		totalFitness = 0;
		for (Individual i : population) {
			double fitness = calculateFitness(i, data);
			i.setFitness(fitness);
			totalFitness = totalFitness + fitness;
		}
	}

	protected void calculateProbabilities() {
		double totalProbability = 0;
		for (Individual i : population) {
			totalProbability += (i.getFitness() / totalFitness);
			i.setProbability(totalProbability);
		}
	}

	public void sort() {
		calculatePopulationFitness();
		Collections.sort(population);
		calculateProbabilities();
	}

	protected Individual pickIndividual() {
		double random = Math.random();
		if (random < population.get(0).getProbability()) {
			return population.get(0);
		}
		for (int i = 1; i < population.size(); i++) {
			if (population.get(i - 1).getProbability() < random && random < population.get(i).getProbability()) {
				return population.get(i);
			}
		}
		return population.get(population.size() - 1);
	}

	// Default is single crossover at some point
	protected void crossOver(Individual a, Individual b) {
		int length = Math.min(a.getTotalBits(), b.getTotalBits());
		int crossOverIndex = (int) (Math.random() * length);
		BitSet aCopy = (BitSet) a.getBitSet().clone();
		for (int i = crossOverIndex; i < length; i++) {
			a.setBit(i, b.getBit(i));
			b.setBit(i, aCopy.get(i));
		}
	}

	protected void mutate(Individual ind) {
		for (int i = 0; i < ind.getTotalBits(); i++) {
			boolean flip = Math.random() < mutationRate / ind.getTotalBits();
			boolean value = flip ? !ind.getBit(i) : ind.getBit(i);
			ind.setBit(i, value);
		}
	}

	protected abstract double calculateFitness(Individual ind, T data);

	protected ArrayList<Individual> getPopulation() {
		return population;
	}

	public Individual getIndividual(int i) {
		return population.get(i);
	}

	public int getSize() {
		return population.size();
	}

	public double getTotalFitness() {
		return totalFitness;
	}

	public int getGeneration() {
		return generation;
	}

	public Individual getBest() {
		return population.get(0);
	}

	public void setMutationRate(double i) {
		mutationRate = i;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < population.size(); i++)
			s += (i + 1) + ":\t" + population.get(i).toString() + "\n";
		return s;
	}

}