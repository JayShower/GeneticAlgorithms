package algorithms;

import java.util.Scanner;

import base.Individual;
import base.Population;

// Given the numbers 1-9 and +, -, *, and /, try to find an expression
// (ignoring order of operations) that gives a certain number

public class NumberSequence extends Population<Double> {

	public NumberSequence(int size, int maxNumbersUsed, double data) {
		super(size, maxNumbersUsed * 2 - 1, 4, data);
	}

	// More numbers allowed makes the program run faster, lower target number
	// makes program run faster

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.print("Enter target number: ");
			double data = Math.abs(in.nextDouble());

			// System.out.print("Enter maximum numbers allowed in expression:
			// ");
			// int max = in.nextInt();

			NumberSequence sequence = new NumberSequence(1000, 20, data);

			long start;
			start = System.currentTimeMillis();

			sequence.run();

			System.out.println();
			System.out.printf("%.3f seconds%n", (System.currentTimeMillis() - start) / 1000.0);
			System.out.println();
			System.out.println("Chromosome:\t" + sequence.getIndividual(0).toStringFormatted());
			System.out.println("Decoded:\t" + toStringDecoded(sequence.getIndividual(0)));
			System.out.println("Expression:\t" + toStringExpression(sequence.getIndividual(0)));
			System.out.println("Fitness:\t" + sequence.getIndividual(0).getFitness());
			System.out.println();
		}
	}

	// Printing methods below
	// toStringDecoded() converts the binary values into their symbolic
	// representations
	// toStringExpression() converts the binary values into the symbols as the
	// actual expression to be evaluated

	// I decided to put the lambda expressions in methods rather than above in
	// the code for readability

	@Override
	public double calculateFitness(Individual i, Double targetValue) {
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

	public static String toStringDecoded(Individual i) {
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

	public static String toStringExpression(Individual i) {
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
