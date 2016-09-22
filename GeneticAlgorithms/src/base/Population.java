package base;

import java.util.ArrayList;

public class Population<T> {

	private ArrayList<Individual<T>> population = new ArrayList<Individual<T>>(0);
	private int size; // Size of population
	private Sort<T> sort;
	private PickIndividual<T> pickInd;
	private CrossOver<T> crossOver; // Crossover algorithm
	private T target;
	private double totalFitness;

	public Population(int size, int genes, int geneLength, Sort<T> sort, PickIndividual<T> pickInd,
			CrossOver<T> crossOver, Mutate<T> mutate, CalculateFitness<T> calcFit, T target) {
		this.size = size;
		this.sort = sort;
		this.pickInd = pickInd;
		this.crossOver = crossOver;
		this.target = target;
		for (int i = 0; i < size; i++) {
			population.add(new Individual<T>(genes, geneLength, calcFit, mutate, target));
		}
	}

	public ArrayList<Individual<T>> getPopulation() {
		return population;
	}

	public void run(int generations) {
		for (int i = 0; i < generations; i++) {
			evolve();
		}
	}

	// May need to change this so that instead of checking if the fitness is 0,
	// check if it is greater than/equal to or less than/equal to what a person
	// inputs
	public void run() {
		while (population.get(0).getFitness() != 0) {
			evolve();
		}
	}

	public void evolve() {
		totalFitness = 0;
		for (Individual<T> i : population) {
			totalFitness += i.calculateFitness(target);
		}
		for (Individual<T> i : population) {
			i.calculateNormalFitness(totalFitness);
		}
		sort.run(population);
		ArrayList<Individual<T>> newPop = new ArrayList<>(0);
		// Make the body of this while loop customizable
		while (population.size() > 0) {
			Individual<T> a = pickInd.run(this);
			Individual<T> b = pickInd.run(this);
			crossOver.run(a, b);
			a.mutate();
			b.mutate();
			population.remove(a);
			population.remove(b);
			newPop.add(a);
			newPop.add(b);
		}
		population = newPop;
	}

	public void sort() {
		sort.run(population);
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

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < population.size(); i++)
			s += (i + 1) + ":\t" + population.get(i).toString() + "\n";
		return s;
	}

}