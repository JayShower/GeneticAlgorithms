package base;

import java.util.BitSet;

public class Individual {

	private BitSet chromosome;
	private int genes; // How many genes it has
	private int geneLength; // How long each gene is
	private int nucleobases; // Actual number of bits needed for this chromosome
	private int polymeraseIndex = 0; // What gene the RNA polymerase is at
	private double fitness = 1;
	private CalculateFitness calcFit;
	private Mutate mutate;

	public Individual(int genes, int geneLength, CalculateFitness calcFit, Mutate mutate) {
		this.genes = genes;
		this.geneLength = geneLength;
		this.nucleobases = genes * geneLength;
		chromosome = new BitSet(nucleobases);
		for (int i = 0; i < nucleobases; i++)
			chromosome.set(i, (Math.random() > 0.5 ? true : false));
		this.calcFit = calcFit;
		this.mutate = mutate;
		fitness = calcFit.run(this);
	}

	public BitSet getChromosome() {
		return chromosome;
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

	public int getNextGene() {
		int bits = 0;
		int index = polymeraseIndex * geneLength;
		for (int i = index; i < index + geneLength; i++)
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

	public boolean getNucleobase(int gene, int base) {
		return chromosome.get(gene * geneLength + base) ? true : false;
	}

	public void setFitness() {
		fitness = calcFit.run(this);
	}

	public double getFitness() {
		return fitness;
	}

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

}
