package base;

import java.util.BitSet;

// This class may have unused methods/extra methods right now, but I wanted to provide as much
// functionality as needed for anyone's implementation of a genetic algorithm
// And yes I know this still isn't very flexible, but it works for my simple needs.

public class Individual<T> implements Comparable<Individual<T>> {

	private BitSet chromosome;
	private int genes; // How many genes it has
	private final int geneLength; // How long each gene is
	private int nucleobases; // Actual number of bits needed for this chromosome
	private int polymeraseIndex = 0; // What gene the RNA polymerase is at
	private double fitness;
	private double normalizedFitness;
	private CalculateFitness<T> calcFit;
	private Mutate<T> mutate;

	public Individual(int genes, int geneLength, CalculateFitness<T> calcFit, Mutate<T> mutate, T target) {
		this.genes = genes;
		this.geneLength = geneLength;
		this.nucleobases = genes * geneLength;
		chromosome = new BitSet(nucleobases);
		for (int i = 0; i < nucleobases; i++)
			chromosome.set(i, (Math.random() > 0.5 ? true : false));
		this.calcFit = calcFit;
		this.mutate = mutate;
		calculateFitness(target);
	}

	public int getGenes() {
		return genes;
	}

	public int getGeneLength() {
		return geneLength;
	}

	public int getGene(int gene) {
		int bits = 0;
		int index = gene * geneLength;
		for (int i = index; i < index + geneLength; i++)
			bits = chromosome.get(i) ? (bits << 1) | 1 : (bits << 1);
		return bits;
	}

	public void setGene(int gene, int value) {
		for (int i = geneLength; i > 0; i--) {
			boolean bit = (value & 1) == 1 ? true : false;
			value = value >> 1;
			// checks if the last bit is 1 or 0, shifts it to the right
			chromosome.set(gene * geneLength + i, bit);
		}
	}

	public int getNextGene() {
		int bits = 0;
		int startIndex = polymeraseIndex * geneLength;
		for (int i = startIndex; i < startIndex + geneLength; i++)
			bits = chromosome.get(i) ? (bits << 1) | 1 : (bits << 1);
		polymeraseIndex++;
		return bits;
	}

	public boolean hasNextGene() {
		if (polymeraseIndex < genes)
			return true;
		else
			return false;
		// I know the else isn't necessary but I think this looks better
	}

	public void setPolymeraseIndex(int index) {
		polymeraseIndex = index;
	}

	public void resetPolymeraseIndex() {
		setPolymeraseIndex(0);
	}

	public int getPolymeraseIndex() {
		return polymeraseIndex;
	}

	public int getNucleobases() {
		return nucleobases;
	}

	public boolean getNucleobase(int index) {
		return chromosome.get(index);
	}

	public void setNucleobase(int index, boolean value) {
		chromosome.set(index, value);
	}

	public double calculateFitness(T target) {
		fitness = calcFit.run(this, target);
		return fitness;
	}

	public void calculateNormalFitness(double total) {
		normalizedFitness = fitness / total;
	}

	public double getFitness() {
		return fitness;
	}

	public double getNormalizedFitness() {
		return normalizedFitness;
	}

	public void mutate() {
		mutate.run(this);
	}

	public BitSet getChromosome(int start, int end) {
		return chromosome.get(start, end);
	}

	public void setChromosome(BitSet chromosome) {
		this.chromosome = chromosome;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < nucleobases; i++)
			s += chromosome.get(i) ? 1 : 0;
		return s;
	}

	public String toStringFormatted() {
		String s = "";
		for (int i = 0; i < nucleobases; i++) {
			s += chromosome.get(i) ? 1 : 0;
			if ((i + 1) % geneLength == 0)
				s += "\t";
		}
		return s;
	}

	@Override
	public int compareTo(Individual<T> ind) {
		if (this.getFitness() < ind.getFitness())
			return -1;
		else if (this.getFitness() > ind.getFitness())
			return 1;
		else
			return 0;
	}

}
