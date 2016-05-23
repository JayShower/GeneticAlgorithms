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
	
	public String toStringDecoded() {
		boolean[] usedNumbers = new boolean[10];
		String decoded = "";
		this.resetPolymeraseIndex();
		while (this.hasNextGene()) {
			int gene = this.getNextGene();
			if(gene == 10) decoded+="+"; 
			else if(gene == 11) decoded+="-"; 
			else if(gene == 12) decoded+="*"; 
			else if(gene == 13) decoded+="/";
			else if(gene < 10 && usedNumbers[gene]) decoded+="repeat";
			else if(0<= gene && gene <= 9) decoded+=gene;
			else decoded+="NA";
			decoded+="\t";
		}
		return decoded;
	}

	public String toStringExpression() {
		String expression = "";
		this.resetPolymeraseIndex();
		int instruction = 10;
		boolean lookForInstruction = false;
		boolean[] usedNumbers = new boolean[10];
		while (this.hasNextGene()) {
			int gene = this.getNextGene();
			if (lookForInstruction && 10 <= gene && gene <= 13) {
				instruction = gene;
				lookForInstruction = false;
			} else if (!lookForInstruction && 0 <= gene && gene <= 9 && !usedNumbers[gene] ) {
				usedNumbers[gene] = true;
				lookForInstruction = true;
				if (instruction == 10) expression += " + " + gene;
				else if (instruction == 11) expression += " - " + gene;
				else if (instruction == 12) expression += " * " + gene;
				else if (instruction == 13) expression += " / " + gene;
			}
		}
		return expression;
	}

}
