package base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public abstract class Population<T> {

	private ArrayList<Individual<T>> population = new ArrayList<Individual<T>>(0);
	private int size; // Size of population
	private T data;
	private int generation = 0;
	private double totalFitness;

	public Population(int size, int genes, int geneLength, T data) {
		if (size % 2 != 0)
			size++;
		this.size = size;
		this.data = data;
		for (int i = 0; i < size; i++)
			population.add(new Individual<T>(genes, geneLength));
		sort();
	}

	public void run(int generations) {
		generation += generations;
		for (int i = 0; i < generations; i++) {
			evolve();
		}
	}

	// May need to change this so that instead of checking if the fitness is 0,
	// check if it is greater than/equal to or less than/equal to what a person
	// inputs
	public void run() {
		while (getBest().getFitness() != 0) {
			evolve();
		}
	}

	public void evolve() {
		ArrayList<Individual<T>> newPop = new ArrayList<>(0);
		while (population.size() > 0) {
			Individual<T> a = pickIndividual();
			Individual<T> b = pickIndividual();
			crossOver(a, b);
			mutate(a);
			mutate(b);
			population.remove(a);
			population.remove(b);
			newPop.add(a);
			newPop.add(b);
		}
		population = newPop;
		sort();
	}

	public void calculatePopulationFitness() {
		totalFitness = 0;
		for (Individual<T> i : population) {
			double fitness = calculateFitness(i, data);
			i.setFitness(fitness);
			totalFitness += fitness;
		}
	}

	public void sort() {
		calculatePopulationFitness();
		Collections.sort(population);
	}

	public Individual<T> pickIndividual() {
		double random = Math.random();
		for (Individual<T> i : population) {
			if (i.getFitness() / totalFitness < random)
				return i;
		}
		return population.get(0);
	}

	// Default is single crossover at some point
	public void crossOver(Individual<T> a, Individual<T> b) {
		int length = Math.min(a.getNucleobases(), b.getNucleobases());
		int crossOverIndex = (int) (Math.random() * length);
		BitSet aCopy = (BitSet) a.getChromosome(0, a.getNucleobases()).clone();
		for (int i = crossOverIndex; i < a.getNucleobases(); i++) {
			a.setNucleobase(i, b.getNucleobase(i));
			b.setNucleobase(i, aCopy.get(i));
		}
	}

	public void mutate(Individual<T> ind) {
		for (int i = 0; i < ind.getNucleobases(); i++) {
			boolean flip = Math.random() < 1.0 / ind.getNucleobases();
			boolean value = flip ? !ind.getNucleobase(i) : ind.getNucleobase(i);
			ind.setNucleobase(i, value);
		}
	}

	public abstract double calculateFitness(Individual<T> ind, T data);

	public ArrayList<Individual<T>> getPopulation() {
		return population;
	}

	public Individual<T> getIndividual(int i) {
		return population.get(i);
	}

	public int getSize() {
		return size;
	}

	public double getTotalFitness() {
		return totalFitness;
	}

	public int getGeneration() {
		return generation;
	}

	public Individual<T> getBest() {
		sort();
		return population.get(0);
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < population.size(); i++)
			s += (i + 1) + ":\t" + population.get(i).toString() + "\n";
		return s;
	}

}