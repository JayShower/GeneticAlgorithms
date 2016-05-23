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
			int i = 0; // testing testing
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
		System.out.println(toStringValues(i));
		System.out.println(toStringDecoded(i));
		System.out.println(toStringExpression(i));
		System.out.println(i.getFitness());
	}

	public static Individual pickIndividual(Population p) {
		int size = p.getSize();

		return new Individual(size, size, null, null);
	}

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

	public static String toStringValues(Individual i) {
		String values = "";
		i.resetPolymeraseIndex();
		while (i.hasNextGene()) {
			int gene = i.getNextGene();
			if (0 <= gene && gene <= 9)
				values += gene;
			else if (gene == 10)
				values += "+";
			else if (gene == 11)
				values += "-";
			else if (gene == 12)
				values += "*";
			else if (gene == 13)
				values += "/";
			else
				values += "NA";
			values += "\t";
		}
		return values;
	}

	public static String toStringDecoded(Individual i) {
		String decoded = "";
		decoded+="NAI = Not an instruction, NAN = Not a number\n";
		i.resetPolymeraseIndex();
		double result = 0;
		boolean lookForInstruction = false;
		boolean[] usedNumbers = new boolean[10];
		while (i.hasNextGene()) {
			int gene = i.getNextGene();
			if(lookForInstruction) {
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
				}
				else decoded+="NAI";
			}
			else {
				if (0 <= gene && gene <= 9) {
					if(usedNumbers[gene]) decoded+="Repeated";
					else{
						usedNumbers[gene] = true;
						lookForInstruction = true;
						decoded+=gene;
					}
				}
				else decoded+= "Nan";
			}
			decoded+="\t";
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
			} else if (!lookForInstruction && 0 <= gene && gene <= 9 && !usedNumbers[gene]) {
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
