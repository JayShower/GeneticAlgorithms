package algorithms.numberSequence;

import java.util.Scanner;

import base.Individual;
import base.Population;

// Given the numbers 1-9 and +, -, *, and /, try to find an expression
// (ignoring order of operations) that gives a certain number

public class NumberSequence extends Population<Double> {

	// More numbers allowed makes the program run faster, lower target number
	// makes program run faster

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		System.out.print("Enter target number: ");
		double data = Math.abs(in.nextDouble());
		System.out.print("Enter max numbers used: ");
		int numbers = in.nextInt();

		NumberSequence sequence = new NumberSequence(10, numbers, data);
		long start;
		System.out.println();
		start = System.currentTimeMillis();
		sequence.run(20000);
		System.out.printf("%.3f seconds%n", (System.currentTimeMillis() - start) / 1000.0);
		System.out.printf("Found in generation %d%n", sequence.getGeneration());
		printData(sequence.getBest());
		System.out.println();
	}

	public NumberSequence(int size, int maxNumbersUsed, double data) {
		super(size, maxNumbersUsed * 2 - 1, 4, data);
	}

	@Override
	public void run(int maxGenerations) {
		while (population.get(0).getFitness() != 0 && generation < maxGenerations) {
			evolve();
		}
	}

	@Override
	public void calculateProbabilities() {
		double totalProbability = 0;
		for (Individual i : population) {
			totalProbability += 1.0 - (i.getFitness() / totalFitness);
			i.setProbability(totalProbability);
		}
	}

	@Override
	public double calculateFitness(Individual i, Double targetValue) {
		double result = 0;
		int instruction = 10;
		boolean lookForInstruction = false;
		i.resetGeneIndex();
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

	public static String toStringDecoded(Individual i) {
		String decoded = "";
		i.resetGeneIndex();
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

	public static String toStringExpression(Individual i) {
		String expression = "";
		i.resetGeneIndex();
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

	public static void printData(Individual ind) {
		System.out.println("Chromosome:\t" + ind.toStringFormatted());
		System.out.println("Decoded:\t" + toStringDecoded(ind));
		System.out.println("Expression:\t" + toStringExpression(ind));
		System.out.println("Fitness:\t" + ind.getFitness());
	}

}
