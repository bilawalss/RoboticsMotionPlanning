import java.io.InputStream;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import objects.BaseObject;
import objects.Point;

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

        int numberOfObstacles = sc.nextInt();
        BaseObject [] obstacles = new BaseObject[numberOfObstacles];

        if (sc.hasNextInt()) {

            for (int i = 0; i < numberOfObstacles; i++) {
                Polygon polygon = new Polygon();
                int numberOfPoints = sc.nextInt();
                Point [] points = new Point[numberOfPoints];
                for (int j = 0; j < numberOfPoints; j++) {
                    //Adding coordinates to the polygon
                    Point point = new Point(sc.nextDouble(), sc.nextDouble());
                    polygon.getPoints().addAll(point.getX(), point.getY());
                    points[j] = point;
                }
                polygons.getChildren().add(polygon);
                obstacles[i] = new BaseObject(points);
            }
        }
        System.out.println(obstacles[0].collidesWith(obstacles[1]));
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
