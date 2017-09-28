import java.io.InputStream;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;

/**
 * A simulator for the class Robotics Motion Planning.
 * @author: Bilawal Shaikh and Thang Le
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(root, 600, 600);

        // Group that contains shapes
        Group polygons = new Group();

        // load the obstacles file as stream
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream obstacleStream = classLoader.getResourceAsStream("obstacles.txt");
        Scanner sc = new Scanner(obstacleStream);

        //Creating a scene object
        scene.setRoot(polygons);
        //Setting title to the Stage
        stage.setTitle("Robotics Simulator");

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim(); 
            if (line.isEmpty())
                break;
            String[] tokens = line.split(" ");
            double[][] points = new double[2][tokens.length];
            
            for (int i = 0; i < tokens.length; i += 2) {
                points[0][i] = Double.parseDouble(tokens[i]);
                points[1][i] = Double.parseDouble(tokens[i+1]);
            }            

            SimpleMatrix pointMat = new SimpleMatrix(points);
        }

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}
