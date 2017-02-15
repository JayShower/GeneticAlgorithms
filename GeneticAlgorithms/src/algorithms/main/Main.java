package algorithms.main;

import java.util.Scanner;

import algorithms.circles.CircleDisplay;
import algorithms.numberSequence.NumberSequence;

public class Main {

	// add programs here
	private enum Programs {
		CIRCLE(CircleDisplay::main, "Find the largest circle that fits between background circles"), NUMBER(
				NumberSequence::main, "Find an expression (using 1-9, -,+,*,/) that equals another number");
		public final MainInterface	mainInterface;
		private final String		description;

		private Programs(MainInterface mi, String s) {
			mainInterface = mi;
			description = s;
		}

		@Override
		public String toString() {
			return super.toString() + "- " + description;
		}
	}

	public static void main(String[] args) {
		String input = "";
		Scanner in = new Scanner(System.in);
		while (!input.equals("Q")) {
			System.out.println("Available Programs: (Q to quit)");
			for (Programs p : Programs.values())
				System.out.println(p);
			System.out.print("Enter program: ");
			input = in.nextLine().trim().toUpperCase();
			for (Programs p : Programs.values()) {
				if (input.equals(p.name())) {
					System.out.println("Running " + p.name());
					p.mainInterface.run(args);
				}
			}
			System.out.println();
		}
		in.close();
	}

}