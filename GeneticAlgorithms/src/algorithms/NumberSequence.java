package algorithms;

import base.CrossOver;
import base.CalculateFitness;
import base.Individual;
import base.Mutate;
import base.Population;

// Given the numbers 0-9 (used only once) and +, -, *, and /, try to find an expression
// (ignoring order of operations) that gives a certain number

public class NumberSequence {

	public static void main(String[] args) {
		Population p = new Population(300, 19, 4, (Individual i1, Individual i2) -> {

		}, (Individual i) -> {

		}, (Individual i) -> {
			boolean[] usedNumbers = new boolean[10];
			final double targetValue = 45;
			double result = 0;
			int instruction = 10;
			boolean lookForInstruction = false;
			while (i.hasNextGene()) {
				int gene = i.getNextGene();
				if (lookForInstruction && gene >= 10 && gene <= 13) {
					instruction = gene;
					lookForInstruction = false;
				} else if (!lookForInstruction && gene >= 0 && gene <= 9 && !usedNumbers[gene]) {
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
		});
		Individual i = p.getIndividual(0);
		System.out.println(i.toStringFormatted());
		System.out.println(i.toStringDecoded());
		System.out.println(i.toStringExpression());
		System.out.println(i.getFitness());
	}

	// public static Individual pickIndividual(Population p) {
	// int size = p.getSize();

	// }

	private static boolean[] usedNumbers = new boolean[10];
	private static final double targetValue = 45;

	public static double getFitness(Individual i) {
		double result = 0;
		int instruction = 10;
		boolean lookForInstruction = false;
		while (i.hasNextGene()) {
			int gene = i.getNextGene();
			if (lookForInstruction && gene >= 10 && gene <= 13) {
				instruction = gene;
				lookForInstruction = false;
			} else if (!lookForInstruction && gene >= 0 && gene <= 9 && !usedNumbers[gene]) {
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

}
