package base;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;

public final class Population<T> {

	private final int	size;
	private final int	genes;
	private final int	geneLength;

	private final T			data;
	private final double	mutationRate;

	private ArrayList<Individual>					population	= new ArrayList<Individual>(0);
	private int										generation	= 0;
	private double									totalFitness;
	private final Comparator<Individual>			comparator;
	private final Function<Individual, T, Double>	fitnessCalculator;

	/**
	 * 
	 * @param size
	 *            how many individuals are in the population
	 * @param genes
	 *            how many genes each individual has
	 * @param geneLength
	 *            how long each gene in an individual is
	 * @param data
	 *            the data uses to evaluate the fitness of individual
	 * @param mutationRate
	 * @param comparator
	 * @param fitnessCalculator
	 */
	public Population(int size, int genes, int geneLength, T data, double mutationRate,
			Comparator<Individual> comparator, Function<Individual, T, Double> fitnessCalculator) {
		this.size = size;
		this.genes = genes;
		this.geneLength = geneLength;

		this.data = data;
		this.mutationRate = mutationRate;

		this.comparator = comparator;
		this.fitnessCalculator = fitnessCalculator;

		if (size % 2 != 0)
			size++;
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

	public void evolve() {
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

	private void calculateAndSetFitnesses() {
		totalFitness = 0;
		for (Individual i : population) {
			double fitness = fitnessCalculator.apply(i, data);
			i.setFitness(fitness);
			totalFitness = totalFitness + fitness;
		}
	}

	private void calculateAndSetProbabilities() {
		double totalProbability = 0;
		for (Individual i : population) {
			totalProbability += (i.getFitness() / totalFitness);
			i.setProbability(totalProbability);
		}
	}

	private void sort() {
		calculateAndSetFitnesses();
		Collections.sort(population, comparator);
		calculateAndSetProbabilities();
	}

	private Individual pickIndividual() {
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
	private void crossOver(Individual a, Individual b) {
		int length = Math.min(a.getTotalBits(), b.getTotalBits());
		int crossOverIndex = (int) (Math.random() * length);
		BitSet aCopy = (BitSet) a.getBitSet().clone();
		for (int i = crossOverIndex; i < length; i++) {
			a.setBit(i, b.getBit(i));
			b.setBit(i, aCopy.get(i));
		}
	}

	private void mutate(Individual ind) {
		for (int i = 0; i < ind.getTotalBits(); i++) {
			boolean flip = Math.random() < mutationRate / ind.getTotalBits();
			boolean value = flip ? !ind.getBit(i) : ind.getBit(i);
			ind.setBit(i, value);
		}
	}

	public ArrayList<Individual> getPopulation() {
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

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < population.size(); i++)
			s += (i + 1) + ":\t" + population.get(i).toString() + "\n";
		return s;
	}

}