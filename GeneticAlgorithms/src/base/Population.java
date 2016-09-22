package base;

import java.util.ArrayList;

public class Population {

	private ArrayList<Individual> population = new ArrayList<Individual>(0);
	private int size; // Size of population
	private Sort sort;
	private PickIndividual pickInd;
	private CrossOver crossOver; // Crossover algorithm
	private double target;
	private double totalFitness;

	public Population(int size, int genes, int geneLength, Sort sort, PickIndividual pickInd, CrossOver crossOver,
			Mutate mutate, CalculateFitness calcFit, double target) {
		if (size % 2 != 0)
			size++;
		this.size = size;
		this.sort = sort;
		this.pickInd = pickInd;
		this.crossOver = crossOver;
		this.target = target;
		for (int i = 0; i < size; i++) {
			Individual ind = new Individual(genes, geneLength, calcFit, mutate, target, size);
			population.add(ind);
			totalFitness += ind.getFitness();
		}
	}

	public ArrayList<Individual> getPopulation() {
		return population;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public void run(int generations) {
		for (int i = 0; i < generations; i++) {
			evolve();
		}
	}

	public void run() {
		sort.run(population);
		while (population.get(0).getFitness() != 0) {
			evolve();
		}
	}

	public void evolve() {
		sort.run(population);
		ArrayList<Individual> newPop = new ArrayList<>(0);
		while (population.size() > 0) {
			Individual a = pickInd.run(population);
			Individual b = pickInd.run(population);
			crossOver.run(a, b);
			a.mutate();
			b.mutate();
			population.remove(a);
			population.remove(b);
			newPop.add(a);
			newPop.add(b);
		}
		population = newPop;
		totalFitness = 0;
		for (Individual i : population) {
			i.calculateFitness(target, size);
			totalFitness += i.getFitness();
		}
	}

	public void sort() {
		sort.run(population);
	}

	public Individual getIndividual(int i) {
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