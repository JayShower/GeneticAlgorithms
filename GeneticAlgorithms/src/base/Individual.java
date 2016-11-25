package base;

import java.util.BitSet;

// Determine what functionality this class actually needs (instance variables, methods, etc.)

public class Individual implements Comparable<Individual> {

	private BitSet	bitSet;
	private int		genes;
	private int		geneLength;
	private int		totalBits;
	private int		geneIndex	= 0;
	private double	fitness;
	private double	probability;

	public Individual(int genes, int geneLength) {
		this.genes = genes;
		this.geneLength = geneLength;
		this.totalBits = genes * geneLength;
		bitSet = new BitSet(totalBits);
		for (int i = 0; i < totalBits; i++)
			bitSet.set(i, (Math.random() > 0.5 ? true : false));
	}

	private Individual(int genes, int geneLength, BitSet bitSet) {
		this.genes = genes;
		this.geneLength = geneLength;
		this.totalBits = genes * geneLength;
		this.bitSet = bitSet;
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
			gene = bitSet.get(i) ? (gene << 1) | 1 : (gene << 1);
		return gene;
	}

	public void setGene(int geneIndex, int value) {
		// sets i equal to the bit that is at the end of the current gene
		for (int i = (geneLength * (geneIndex + 1) - 1); i >= geneLength * geneIndex; i--) {
			boolean bit = (value & 1) == 1 ? true : false;
			value = value >> 1;
			// checks if the last bit is 1 or 0, shifts it to the right
			bitSet.set(i, bit);
		}
	}

	public int getNextGene() {
		return getGene(geneIndex++);
	}

	public void setNextGene(int value) {
		setGene(geneIndex++, value);
	}

	public boolean hasNextGene() {
		if (geneIndex < genes) {
			return true;
		} else {
			return false;
		}
	}

	public void setGeneIndex(int index) {
		geneIndex = index;
	}

	public void resetGeneIndex() {
		geneIndex = 0;
	}

	public int getGeneIndex() {
		return geneIndex;
	}

	public int getTotalBits() {
		return totalBits;
	}

	public boolean getBit(int index) {
		return bitSet.get(index);
	}

	public void setBit(int index, boolean value) {
		bitSet.set(index, value);
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double prob) {
		this.probability = prob;
	}

	public BitSet getBitSet(int start, int end) {
		return bitSet.get(start, end);
	}

	public BitSet getBitSet() {
		return bitSet;
	}

	public void setBitSet(BitSet chromosome) {
		this.bitSet = chromosome;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < totalBits; i++)
			s += bitSet.get(i) ? 1 : 0;
		return s;
	}

	public String toStringFormatted() {
		String s = "";
		for (int i = 0; i < totalBits; i++) {
			s += bitSet.get(i) ? 1 : 0;
			if ((i + 1) % geneLength == 0)
				s += "\t";
		}
		return s;
	}

	@Override
	public int compareTo(Individual ind) {
		if (this.getFitness() < ind.getFitness())
			return -1;
		else if (this.getFitness() > ind.getFitness())
			return 1;
		else
			return 0;
	}

	@Override
	public boolean equals(Object o) {
		Individual ind = (Individual) o;
		return bitSet.equals(ind.bitSet) && totalBits == ind.totalBits;
	}

	@Override
	public Individual clone() {
		return new Individual(genes, geneLength, (BitSet) bitSet.clone());
	}

}