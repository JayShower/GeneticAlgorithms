package test;

import base.Individual;
import base.Population;

public class Testing extends Population<Integer> {

	public static void main(String[] args) {
		Testing t = new Testing();
	}

	public static void print(Individual i) {
		System.out.println(i.toStringFormatted());
		System.out.printf("Fitness: %f, Probability: %f%n", i.getFitness(), i.getProbability());
	}

	public Testing() {
		super(100, 10, 3, Integer.valueOf(0));
	}

	@Override
	public double calculateFitness(Individual ind, Integer data) {
		int sum = 0;
		ind.resetGeneIndex();
		while (ind.hasNextGene())
			sum += ind.getNextGene();
		return sum;
	}

	@Override
	public Individual getBest() {
		return population.get(population.size() - 1);
	}
}