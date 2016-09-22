package base;

import java.util.BitSet;

// Determine what functionality this class actually needs (instance variables, methods, etc.)

public class Individual<T> implements Comparable<Individual<T>> {

	private BitSet chromosome;
	private int genes; // How many genes it has
	private final int geneLength; // How long each gene is
	private int nucleobases; // Actual number of bits needed for this chromosome
	private int polymeraseIndex = 0; // What gene the RNA polymerase is at
	private double fitness;

	public Individual(int genes, int geneLength) {
		this.genes = genes;
		this.geneLength = geneLength;
		this.nucleobases = genes * geneLength;
		chromosome = new BitSet(nucleobases);
		for (int i = 0; i < nucleobases; i++)
			chromosome.set(i, (Math.random() > 0.5 ? true : false));
	}

	public int getGenes() {
		return genes;
	}

	public int getGeneLength() {
		return geneLength;
	}

	public int getGene(int geneIndex) {
		int gene = 0;
		int baseIndex = geneIndex * geneLength;
		for (int i = baseIndex; i < baseIndex + geneLength; i++)
			gene = chromosome.get(i) ? (gene << 1) | 1 : (gene << 1);
		return gene;
	}

	public void setGene(int geneIndex, int gene) {
		for (int i = geneLength - 1; i >= 0; i--) {
			boolean bit = (gene & 1) == 1 ? true : false;
			gene = gene >> 1;
			// checks if the last bit is 1 or 0, shifts it to the right
			chromosome.set(geneIndex * geneLength + i, bit);
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

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
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
