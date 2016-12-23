package algorithms.circles;

import java.util.ArrayList;

import base.Individual;
import base.Population;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Circles extends Population<ArrayList<Circle>> {

	public ArrayList<Circle>	circleList;
	public ArrayList<Circle>	bestCircleList	= new ArrayList<Circle>(0);
	public static final int		GENES			= 3;
	public static final int		GENE_LENGTH		= 9;
	public static final int		SIDE_LENGTH		= (int) Math.pow(2, GENE_LENGTH);

	public static void main(String[] args) {
		CircleDisplay.main(args);
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
				bestCircleList.add(new Circle(best.getGene(0), best.getGene(1), best.getGene(2), Color.RED));
				System.out.println(count);
			}
			count++;
		}
	}

	@Override
	public double calculateFitness(Individual ind, ArrayList<Circle> data) {
		int x = ind.getGene(0);
		int y = ind.getGene(1);
		int radius = ind.getGene(2);
		if (x - radius < 0 || x + radius >= SIDE_LENGTH || y - radius < 0 || y + radius >= SIDE_LENGTH) {
			return 0;
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