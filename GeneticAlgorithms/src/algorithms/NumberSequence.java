package algorithms;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Scanner;

import base.Individual;
import base.Population;

// Given the numbers 1-9 and +, -, *, and /, try to find an expression
// (ignoring order of operations) that gives a certain number

public class NumberSequence {

	public static void main(String[] args) {
		// Individual<Double> ind = new Individual<Double>(21, 4,
		// NumberSequence::getFitness, NumberSequence::mutate);
		// ind.calculateFitness(20.0);
		// System.out.println("Chromosome:\t" + ind.toStringFormatted());
		// System.out.println("Decoded:\t" + toStringDecoded(ind));
		// System.out.println("Expression:\t" + toStringExpression(ind));
		// System.out.println("Fitness:\t" + ind.getFitness());
		// Population
		Scanner in = new Scanner(System.in);
		System.out.print("Enter population size number: ");
		int size = in.nextInt();
		System.out.print("Enter maximum numbers to be used: ");
		int genes = in.nextInt();
		System.out.print("Enter target number: ");
		double target = in.nextDouble();
		in.close();
		long start;
		start = System.currentTimeMillis();
		Population<Double> p = makeNumberSequence(size, genes, target);
		p.run();
		p.sort();
		System.out.println();
		System.out.println(System.currentTimeMillis() - start + "milliseconds");
		System.out.println();
		for (int i = 0; i < 2; i++) {
			System.out.println("Chromosome:\t" + p.getIndividual(i).toStringFormatted());
			System.out.println("Decoded:\t" + toStringDecoded(p.getIndividual(i)));
			System.out.println("Expression:\t" + toStringExpression(p.getIndividual(i)));
			System.out.println("Fitness:\t" + p.getIndividual(i).getFitness());
			System.out.println();
		}

	}

	public static Population<Double> makeNumberSequence(int size, int numbers, double target) {
		if (size % 2 != 0)
			size++;
		int genes = numbers * 2 - 1;
		return new Population<Double>(size, genes, 4, NumberSequence::sort, NumberSequence::pickIndividual,
				NumberSequence::crossOver, NumberSequence::mutate, NumberSequence::getFitness, Double.valueOf(target));
	}

	// Printing methods below
	// toStringDecoded() converts the binary values into their symbolic
	// representations
	// toStringExpression() converts the binary values into the symbols as the
	// actual expression to be evaluated

	// I decided to put the lambda expressions in methods rather than above in
	// the code for readability

	private static void sort(ArrayList<Individual<Double>> p) {
		Collections.sort(p);
	}

	private static Individual<Double> pickIndividual(Population<Double> p) {
		double random = Math.random();
		for (Individual<Double> i : p.getPopulation()) {
			if (i.getNormalizedFitness() < random)
				return i;
		}

		return p.getPopulation().get(0);
	}

	private static void crossOver(Individual<Double> a, Individual<Double> b) {
		int crossOverIndex = (int) (Math.random() * a.getNucleobases());
		BitSet aCopy = (BitSet) a.getChromosome(0, a.getNucleobases()).clone();
		for (int i = crossOverIndex; i < a.getNucleobases(); i++) {
			a.setNucleobase(i, b.getNucleobase(i));
			b.setNucleobase(i, aCopy.get(i));
		}

	}

	private static void mutate(Individual<Double> ind) {
		for (int i = 0; i < ind.getNucleobases(); i++) {
			boolean flip = Math.random() < (10.0 / ind.getNucleobases()) ? true : false;
			boolean value = flip ? !ind.getNucleobase(i) : ind.getNucleobase(i);
			ind.setNucleobase(i, value);
		}
	}

	private static Double getFitness(Individual<Double> i, Double targetValue) {
		double result = 0;
		int instruction = 10;
		boolean lookForInstruction = false;
		i.resetPolymeraseIndex();
		while (i.hasNextGene()) {
			int gene = i.getNextGene();
			if (lookForInstruction && gene >= 10 && gene <= 13) {
				instruction = gene;
				lookForInstruction = false;
			} else if (!lookForInstruction && gene >= 1 && gene <= 9) {
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
		return Math.abs(targetValue.doubleValue() - result) / targetValue.doubleValue();
	}

	public static String toStringDecoded(Individual<Double> i) {
		String decoded = "";
		i.resetPolymeraseIndex();
		boolean lookForInstruction = false;
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
				if (1 <= gene && gene <= 9) {
					lookForInstruction = true;
					decoded += gene;
				} else
					decoded += "nan";
			}
			decoded += "\t";
		}
		return decoded;
	}

	public static String toStringExpression(Individual<Double> i) {
		String expression = "";
		i.resetPolymeraseIndex();
		double result = 0;
		int instruction = 10;
		boolean lookForInstruction = false;
		while (i.hasNextGene()) {
			int gene = i.getNextGene();
			if (lookForInstruction && 10 <= gene && gene <= 13) {
				instruction = gene;
				lookForInstruction = false;
			} else if (!lookForInstruction && 1 <= gene && gene <= 9) {
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
