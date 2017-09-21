import java.io.InputStream;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import objects.Obstacle;
import objects.Point;
import objects.Robot;

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
        Obstacle[] obstacles = new Obstacle[numberOfObstacles];

        if (sc.hasNextInt()) {

            for (int i = 0; i < numberOfObstacles; i++) {
                Polygon polygon = new Polygon();
                int numberOfPoints = sc.nextInt();
                System.out.println(numberOfPoints);
                Point [] points = new Point[numberOfPoints];
                for (int j = 0; j < numberOfPoints; j++) {
                    //Adding coordinates to the polygon
                    Point point = new Point(sc.nextDouble(), sc.nextDouble());
                    polygon.getPoints().addAll(point.getX(), point.getY());
                    points[j] = point;
                }
                polygons.getChildren().add(polygon);
                obstacles[i] = new Obstacle(points);
            }
        }
       
        Point a = new Point(sc.nextDouble(), sc.nextDouble());
        Point c = new Point(sc.nextDouble(), sc.nextDouble());

        Polygon robotPolygon = new Polygon();
        Robot robot = new Robot(a, c);

        Point [] robotPoints = robot.getPoints();

        for (int i = 0; i < 4; i++) {
            robotPolygon.getPoints().addAll(robotPoints[i].getX(), robotPoints[i].getY());
        }
        robotPolygon.setFill(Color.BLUE);
        
        polygons.getChildren().add(robotPolygon);
    
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
