package algorithms.circles;

import java.util.Scanner;

import javafx.application.Application;
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

		Circles population = new Circles(300, bgCircles);
		long start;
		System.out.println("\nstarted");
		start = System.currentTimeMillis();
		population.run(20000);
		System.out.printf("%n%.3f seconds%n%n", (System.currentTimeMillis() - start) / 1000.0);
		Circles.printData(population.getBest());
		System.out.println();

		Pane root = new Pane();
		root.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, BorderStroke.THIN)));
		Scene scene = new Scene(root, Circles.SIDE_LENGTH, Circles.SIDE_LENGTH, Color.WHITE);
		primaryStage.setScene(scene);

		Group circles = new Group();
		for (Circle c : population.circleList) {
			circles.getChildren().add(c);
		}

		circles.getChildren().add(population.bestCircle);

		root.getChildren().add(circles);

		// Group bestCircles = new Group();
		// for (int i = 0; i < bestCircleList.size(); i++) {
		// bestCircles.getChildren().add(new Circle(0, Color.RED));
		// }
		// root.getChildren().add(bestCircles);

		primaryStage.show();

		// ObservableList<Node> children = bestCircles.getChildren();
		// Timeline timeline = new Timeline();
		// for (int i = 0; i < bestCircleList.size(); i++) {
		// timeline.getKeyFrames()
		// .addAll(new KeyFrame(Duration.ZERO,
		// new KeyValue(children.get(i).translateXProperty(),
		// bestCircleList.get(i).getCenterX()),
		// new KeyValue(children.get(i).translateYProperty(),
		// bestCircleList.get(i).getCenterY())),
		// new KeyFrame(Duration.ZERO,
		// new KeyValue(((Circle) children.get(i)).radiusProperty(),
		// bestCircleList.get(i).getRadius())),
		// new KeyFrame(Duration.millis(500)),
		// new KeyFrame(Duration.ZERO, new KeyValue(((Circle)
		// children.get(i)).radiusProperty(), 0)));
		// }
		// timeline.play();
		// System.out.println();
		// System.out.println("finished");
	}

	public static void main(String[] args) {
		launch(args);
	}

}