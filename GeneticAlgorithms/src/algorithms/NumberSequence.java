package algorithms;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Scanner;

import base.Individual;
import base.Population;

// Given the numbers 1-9 and +, -, *, and /, try to find an expression
// (ignoring order of operations) that gives a certain number

// Lol: this program will run forever if you enter a prime number greater than 9
public class NumberSequence {

	public static void main(String[] args) {
		// Population
		Scanner in = new Scanner(System.in);
		System.out.print("Enter population size number: ");
		int size = in.nextInt();
		System.out.print("Enter maximum parts to expression: ");
		int genes = in.nextInt();
		System.out.print("Enter target number: ");
		double target = in.nextDouble();
		in.close();
		long start;
		start = System.currentTimeMillis();
		Population p = makeNumberSequence(size, genes, target);
		p.run();
		p.sort();
		System.out.println();
		System.out.println(System.currentTimeMillis() - start + " milliseconds");
		System.out.println();
		for (int i = 0; i < 2; i++) {
			System.out.println("Chromosome:\t" + p.getIndividual(i).toStringFormatted());
			System.out.println("Decoded:\t" + toStringDecoded(p.getIndividual(i)));
			System.out.println("Expression:\t" + toStringExpression(p.getIndividual(i)));
			System.out.println("Fitness:\t" + p.getIndividual(i).getFitness());
			System.out.println();
		}

	}

	public static Population makeNumberSequence(int size, int genes, double target) {
		return new Population(size, genes, 4, NumberSequence::sort, NumberSequence::pickIndividual,
				NumberSequence::crossOver, NumberSequence::mutate, NumberSequence::getFitness, target);
	}

	// Printing methods below
	// toStringDecoded() converts the binary values into their symbolic
	// representations
	// toStringExpression() converts the binary values into the symbols as the
	// actual expression to be evaluated

	// I decided to put the lambda expressions in methods rather than above in
	// the code for readability

	private static void sort(ArrayList<Individual> p) {
		Collections.sort(p);
	}

	private static Individual pickIndividual(ArrayList<Individual> p) {
		double random = Math.random();
		for (Individual i : p) {
			if (i.getNormalizedFitness() < random)
				return i;
		}
		return p.get(0);
	}

	private static void crossOver(Individual a, Individual b) {
		int crossOverIndex = (int) (Math.random() * a.getNucleobases());
		BitSet aCopy = (BitSet) a.getChromosome(0, a.getNucleobases()).clone();
		for (int i = crossOverIndex; i < a.getNucleobases(); i++) {
			a.setNucleobase(i, b.getNucleobase(i));
			b.setNucleobase(i, aCopy.get(i));
		}

	}

	private static void mutate(Individual ind) {
		for (int i = 0; i < ind.getNucleobases(); i++) {
			boolean flip = Math.random() < (10.0 / ind.getNucleobases()) ? true : false;
			boolean value = flip ? !ind.getNucleobase(i) : ind.getNucleobase(i);
			ind.setNucleobase(i, value);
		}
	}

	private static double getFitness(Individual i, double targetValue) {
		boolean[] usedNumbers = new boolean[10];
		double result = 0;
		int instruction = 10;
		boolean lookForInstruction = false;
		i.resetPolymeraseIndex();
		while (i.hasNextGene()) {
			int gene = i.getNextGene();
			if (lookForInstruction && gene >= 10 && gene <= 13) {
				instruction = gene;
				lookForInstruction = false;
			} else if (!lookForInstruction && gene >= 1 && gene <= 9 && !usedNumbers[gene]) {
				usedNumbers[gene] = true;
				lookForInstruction = true;
				if (instruction == 10) {
					result += gene;
				} else if (instruction == 11) {
					result -= gene;
				} else if (instruction == 12) {
					result *= gene;
				} else if (instruction == 13) {
					result /= gene;
				}
			}
		}
		return Math.abs(targetValue - result) / targetValue;
	}

	public static String toStringDecoded(Individual i) {
		String decoded = "";
		i.resetPolymeraseIndex();
		boolean lookForInstruction = false;
		boolean[] usedNumbers = new boolean[10];
		while (i.hasNextGene()) {
			int gene = i.getNextGene();
			if (lookForInstruction) {
				if (10 <= gene && gene <= 13) {
					if (gene == 10)
						decoded += "+";
					else if (gene == 11)
						decoded += "-";
					else if (gene == 12)
						decoded += "*";
					else if (gene == 13)
						decoded += "/";
					lookForInstruction = false;
				} else
					decoded += "nai";
			} else {
				if (0 <= gene && gene <= 9) {
					if (usedNumbers[gene])
						decoded += "repeat";
					else {
						usedNumbers[gene] = true;
						lookForInstruction = true;
						decoded += gene;
					}
				} else
					decoded += "nan";
			}
			decoded += "\t";
		}
		return decoded;
	}

	public static String toStringExpression(Individual i) {
		String expression = "";
		i.resetPolymeraseIndex();
		double result = 0;
		int instruction = 10;
		boolean lookForInstruction = false;
		boolean[] usedNumbers = new boolean[10];
		while (i.hasNextGene()) {
			int gene = i.getNextGene();
			if (lookForInstruction && 10 <= gene && gene <= 13) {
				instruction = gene;
				lookForInstruction = false;
			} else if (!lookForInstruction && 1 <= gene && gene <= 9 && !usedNumbers[gene]) {
				usedNumbers[gene] = true;
				lookForInstruction = true;
				if (instruction == 10) {
					expression += " + " + gene;
					result += gene;
				} else if (instruction == 11) {
					expression += " - " + gene;
					result -= gene;
				} else if (instruction == 12) {
					expression += " * " + gene;
					result *= gene;
				} else if (instruction == 13) {
					expression += " / " + gene;
					result /= gene;
				}
			}
		}
		expression += " = " + result;
		return expression;
	}
}
