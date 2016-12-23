package algorithms.circles;

import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CircleDisplay extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter number of background circles: ");
		int bgCircles = in.nextInt();
		in.close();

		Circles population = new Circles(3000, bgCircles);
		population.setMutationRate(3);

		long start;
		System.out.println("\nstarted");
		start = System.currentTimeMillis();
		population.run(2000);
		System.out.printf("%n%.3f seconds%n%n", (System.currentTimeMillis() - start) / 1000.0);
		Circles.printData(population.getBest());
		System.out.println();

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

		primaryStage.show();

		Thread.sleep(1000);

		circles.getChildren().add(population.bestCircleList.get(0));// population.bestCircleList.size()-
																	// 1));

		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						int length = circles.getChildren().size();
						for (Circle c : population.bestCircleList) {
							circles.getChildren().set(length - 1, c);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				return null;
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}

	public static void main(String[] args) {
		launch(args);
	}

}