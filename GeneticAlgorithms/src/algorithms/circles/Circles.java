package algorithms.circles;

import java.util.ArrayList;

import base.Individual;
import base.Population;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Circles extends Population<ArrayList<Circle>> {

	public ArrayList<Circle>	circleList;
	public Circle				bestCircle;
	public static final int		GENES		= 3;
	public static final int		GENE_LENGTH	= 9;
	public static final int		SIDE_LENGTH	= (int) Math.pow(2, GENE_LENGTH);

	public static void main(String[] args) {
		CircleDisplay.main(args);
		// Circles population = new Circles(200, 50);
		// System.out.println("started");
		// long start;
		// start = System.currentTimeMillis();
		// population.run(20000);
		// System.out.println();
		// System.out.printf("%.3f seconds%n", (System.currentTimeMillis() -
		// start) / 1000.0);
		// System.out.println();
		// printData(population.getBest());
		// System.out.println();
		//
		// CircleDisplay.circleList = circleList;
		// CircleDisplay.bestCircle = bestCircle;
		// CircleDisplay.size = SIDE_LENGTH;
		// CircleDisplay.main(args);
	}

	public Circles(int size, int circles) {
		super(size, GENES, GENE_LENGTH, randomCircleList(circles));
		circleList = this.data;
	}

	@Override
	public void run(int n) {
		Individual best = this.getBest();
		int count = 0;
		while (count < n) {
			evolve();
			if (this.getBest().compareTo(best) > 0) {
				best = this.getBest();
			}
			count++;
		}
		bestCircle = new Circle(best.getGene(0), best.getGene(1), best.getGene(2), Color.RED);
		population.add(best); // inserts the best into the end
	}

	@Override
	public double calculateFitness(Individual ind, ArrayList<Circle> data) {
		int x = ind.getGene(0);
		int y = ind.getGene(1);
		int radius = ind.getGene(2);
		if (radius > SIDE_LENGTH / 2)
			return 0;
		if (x - radius < 0) {
			x = radius;
			ind.setGene(0, x);
		} else if (x + radius >= SIDE_LENGTH) {
			x = SIDE_LENGTH - radius;
			ind.setGene(0, x);
		}
		if (y - radius < 0) {
			y = radius;
			ind.setGene(1, y);
		} else if (y + radius >= SIDE_LENGTH) {
			y = SIDE_LENGTH - radius;
			ind.setGene(1, y);
		}
		double checkX;
		double checkY;
		double checkR;
		double distSq;
		for (Circle check : data) {
			checkX = check.getCenterX();
			checkY = check.getCenterY();
			checkR = check.getRadius();
			distSq = getDistSq(checkX, checkY, x, y);
			if (distSq <= radius * radius) {
				radius = (int) Math.abs((Math.sqrt(distSq) - checkR));
				ind.setGene(2, radius);
			}
			if (distSq <= (checkR + radius) * (checkR + radius)) {
				return 0;
			}
		}
		return Math.PI * radius * radius;
	}

	@Override
	public Individual getBest() {
		return population.get(population.size() - 1);
	}

	public static ArrayList<Circle> randomCircleList(int circlesAmount) {
		ArrayList<Circle> circles = new ArrayList<Circle>(0);
		int minimumRadius = SIDE_LENGTH / (circlesAmount * 4);
		int maximumRadius = SIDE_LENGTH / (circlesAmount);
		int count = 0;
		loop: while (count < circlesAmount) {
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

	public static double getDistSq(double x1, double y1, double x2, double y2) {
		return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
	}

}