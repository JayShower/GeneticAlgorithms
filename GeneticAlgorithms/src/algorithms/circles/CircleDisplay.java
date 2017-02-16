package algorithms.circles;

import java.util.Scanner;

import base.Individual;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CircleDisplay extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter number of background circles: ");
		int bgCircles = in.nextInt();
		System.out.print("Enter number of iterations: ");
		int iterations = in.nextInt();
		System.out.print("Enter size of population: ");
		int size = in.nextInt();
		System.out.print("Enter mutation rate (0.2 good): ");
		double rate = in.nextDouble();
		in.close();

		Circles population = new Circles(size, bgCircles);
		population.setMutationRate(rate);

		// long start;
		// System.out.println("\nstarted");
		// start = System.currentTimeMillis();
		// population.run(2000);
		// System.out.printf("%n%.3f seconds%n%n", (System.currentTimeMillis() -
		// start) / 1000.0);
		// Circles.printData(population.getBest());
		// System.out.println();

		Pane root = new Pane();
		// add event that always runs
		root.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, BorderStroke.THIN)));
		Scene scene = new Scene(root, Circles.SIDE_LENGTH, Circles.SIDE_LENGTH, Color.WHITE);
		primaryStage.setScene(scene);

		Group circles = new Group();
		for (Circle c : population.circleList) {
			circles.getChildren().add(c);
		}

		// circles.getChildren().add(population.bestCircleList.get(population.bestCircleList.size()
		// - 1));

		root.getChildren().add(circles);

		Individual bestInd = population.getBest();
		Circle bestCircle = new Circle(bestInd.getGene(0), bestInd.getGene(1), bestInd.getGene(2), Color.RED);
		root.getChildren().add(bestCircle);

		primaryStage.show();

		// Thread.sleep(1000);

		// circles.getChildren().add(population.bestCircleList.get(0));
		// population.bestCircleList.size()- 1));
		KeyFrame keyFrame = new KeyFrame(Duration.millis(5), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				population.runOnce();
				Individual best = population.getBest();
				bestCircle.setCenterX(best.getGene(0));
				bestCircle.setCenterY(best.getGene(1));
				bestCircle.setRadius(best.getGene(2));
			}
		});
		Timeline loop = new Timeline(keyFrame);
		loop.setCycleCount(iterations);
		loop.play();
		loop.setOnFinished(e -> {
			System.out.println("\nFINISHED");
			Circles.printData(population.getBest());
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}