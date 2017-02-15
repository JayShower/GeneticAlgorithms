package algorithms.circles;

import java.util.ArrayList;

import base.Individual;
import base.Population;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Circles extends Population<ArrayList<Circle>> {

	public ArrayList<Circle> circleList;
	// private ArrayList<Circle> bestCircleList = new ArrayList<Circle>(0);
	public static final int	GENES		= 3;
	public static final int	GENE_LENGTH	= 9;
	public static final int	SIDE_LENGTH	= (int) Math.pow(2, GENE_LENGTH);

	public static void main(String[] args) {
		CircleDisplay.main(args);
	}

	public Circles(int size, int circles) {
		super(size, GENES, GENE_LENGTH, randomCircleList(circles));
		circleList = this.data;
	}

	public void runOnce() {
		evolve();
	}

	// @Override
	// public void run(int n) {
	// int count = 0;
	// while (count < n) {
	// evolve();
	// if (this.getBest().compareTo(best) > 0) {
	// best = this.getBest();
	// bestCircleList.add(new Circle(best.getGene(0), best.getGene(1),
	// best.getGene(2), Color.RED));
	// System.out.println(count);
	// }
	// count++;
	// }
	// }

	@Override
	protected double calculateFitness(Individual ind, ArrayList<Circle> data) {
		int x = ind.getGene(0);
		int y = ind.getGene(1);
		int r = ind.getGene(2);
		int newR = r;
		// int error = 0;
		// boolean change = false;
		if (x - r < 0) {
			newR = Math.min(newR, x);
			// error = Math.max(error, r - x);
			// change = true;
		}
		if (x + r > SIDE_LENGTH) {
			newR = Math.min(newR, SIDE_LENGTH - x);
			// error = Math.max(error, (x + r) - SIDE_LENGTH);
			// change = true;
		}
		if (y - r < 0) {
			newR = Math.min(newR, y);
			// error = Math.max(error, y - x);
			// change = true;
		}
		if (y + r > SIDE_LENGTH) {
			newR = Math.min(newR, SIDE_LENGTH - y);
			// error = Math.max(error, (y + r) - SIDE_LENGTH);
			// change = true;
		}

		// if (x - r < 0 || x + r >= SIDE_LENGTH || y - r < 0 || y + r >=
		// SIDE_LENGTH) {
		// return 0;
		// }
		for (Circle check : data) {
			double checkX = check.getCenterX();
			double checkY = check.getCenterY();
			double checkR = check.getRadius();
			double distanceCenter = getDistance(checkX, checkY, x, y);
			double totalRadius = (checkR + r);
			if (distanceCenter <= totalRadius) {
				newR = Math.min(newR, (int) (distanceCenter - checkR));
				// error = Math.max(error, (int) totalRadius - (int)
				// distanceCenter);
				// change = true;
			}
		}
		// double radius = r;
		// if (change) {
		// ind.setGene(2, newR);
		// radius = radius - Math.abs(error);
		// radius = newR
		// }
		// double radius = ind.getGene(2);
		// printData(ind);
		ind.setGene(2, newR);
		return Math.PI * newR * newR;
	}

	// @Override
	// protected void mutate(Individual ind) {
	// for (int i = 0; i < ind.getTotalBits(); i++) {
	// boolean flip = Math.random() < mutationRate / (ind.getTotalBits() / 3 - i
	// % (ind.getTotalBits() / 3));
	// boolean value = flip ? !ind.getBit(i) : ind.getBit(i);
	// ind.setBit(i, value);
	// }
	// }

	@Override
	protected void crossOver(Individual a, Individual b) {}

	@Override
	public Individual getBest() {
		return population.get(population.size() - 1);
	}

	public static ArrayList<Circle> randomCircleList(int circlesAmount) {
		ArrayList<Circle> circles = new ArrayList<Circle>(0);
		int minimumRadius = SIDE_LENGTH / (circlesAmount * 4);
		int maximumRadius = SIDE_LENGTH / (circlesAmount);
		int count = 0;
		loop:
		while (count < circlesAmount) {
			double radius = (int) (Math.random() * (maximumRadius - minimumRadius)) + minimumRadius;
			double x = (int) (Math.random() * (SIDE_LENGTH - 2 * radius) + radius);
			double y = (int) (Math.random() * (SIDE_LENGTH - 2 * radius) + radius);
			double checkX;
			double checkY;
			double checkR;
			for (Circle check : circles) {
				checkX = check.getCenterX();
				checkY = check.getCenterY();
				checkR = check.getRadius();
				if ((checkX - x) * (checkX - x) + (checkY - y) * (checkY - y) <= (checkR + radius)
						* (checkR + radius)) {
					continue loop;
				}
			}
			Circle c = new Circle(x, y, radius, Color.BLACK);
			circles.add(c);
			count++;
		}
		circles.sort((Circle a, Circle b) -> {
			return (int) (a.getRadius() - b.getRadius());
		});
		return circles;
	}

	public static void printData(Individual ind) {
		System.out.println("Chromosome:\t" + ind.toStringFormatted());
		System.out.println("Data:\t" + getData(ind));
		System.out.println("Fitness:\t" + ind.getFitness());
	}

	public static String getData(Individual ind) {
		int x = ind.getGene(0);
		int y = ind.getGene(1);
		int radius = ind.getGene(2);
		return String.format("(%d, %d), r = %d", x, y, radius);
	}

	public static double getDistance(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}

}