import java.io.InputStream;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/**
 * A simulator for the class Robotics Motion Planning.
 * @author: Bilawal Shaikh and Thang Le
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600);

        // Group that contains shapes
        Group polygons = new Group();

        // load the obstacles file as stream
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream obstacleStream = classLoader.getResourceAsStream("obstacles.txt");
        Scanner sc = new Scanner(obstacleStream);

        if (sc.hasNextInt()) {
            int numberOfObstacles = sc.nextInt();

            for (int i = 0; i < numberOfObstacles; i++) {
                Polygon polygon = new Polygon();
                int points = sc.nextInt();
                for (int j = 0; j < points; j++) {
                    //Adding coordinates to the polygon
                    polygon.getPoints().addAll(sc.nextDouble(), sc.nextDouble());
                }
                polygons.getChildren().add(polygon);
            }
        }

        //Creating a scene object
        scene.setRoot(polygons);
        //Setting title to the Stage
        stage.setTitle("Robotics Simulator");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}
