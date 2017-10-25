import java.util.*;
import java.io.InputStream;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import org.ejml.simple.SimpleMatrix;

import geometry.PolygonObject;
import geometry.Robot;
import geometry.Configuration;
import geometry.Graph;

import planning.Sampler;
import planning.Environment;
import planning.MotionPlanner;
import planning.LocalPlanner;

import static global.Constants.DEBUG;


/**
 * A simulator for the class Robotics Motion Planning.
 * @author: Bilawal Shaikh and Thang Le
 */
public class Main extends Application {
    
    private static void printObjectArray(Object[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + ", ");
        }
        System.out.println();
    }

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

        //Creating a scene object
        scene.setRoot(polygons);
        //Setting title to the Stage
        stage.setTitle("Robotics Simulator");

        List<PolygonObject> obstacles = new ArrayList<>();

        // read obstacle file
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim(); 
            if (line.isEmpty())
                break;

            String[] tokens = line.split(" ");
            double[] data = new double[tokens.length];
            
            for (int i = 0; i < tokens.length; i++) {
                data[i] = Double.parseDouble(tokens[i]);
            } 

            obstacles.add(new PolygonObject(data));
        }


        // draw the list of obstacles
        for (PolygonObject o: obstacles) {
            Polygon poly = new Polygon();
            poly.getPoints().addAll(o.getPointArray()); 
            

            if (DEBUG) {
                System.out.println("Point array: ");
                printObjectArray(o.getPointArray());
            }

            polygons.getChildren().add(poly); 
        }

        if (DEBUG) {
            // test graph
            Group debugRoot = new Group();
            Stage debugStage = new Stage();
            Scene debugScene = new Scene(debugRoot, 600, 600);

            Configuration a = new Configuration(100, 100);
            Configuration b = new Configuration(200, 200);
            Configuration c = new Configuration(250, 300);
            Configuration d = new Configuration(350, 100);

            Graph g = new Graph();
            g.addVertex(a);
            g.addVertex(b);
            g.addVertex(c);
            g.addVertex(d);

            g.addEdge(0, 3);
            g.addEdge(0, 1);
            g.addEdge(2, 3);
            g.addEdge(1, 3);
            
            // draw vertices
            for (int i = 0; i < g.size(); i++) {
                Configuration x = g.getVertex(i);
                Circle circle = new Circle();
                circle.setCenterX(x.getX());
                circle.setCenterY(x.getY());
                circle.setRadius(10.0);
                debugRoot.getChildren().add(circle);
            }

            // draw shortest path
            List<Integer> path = g.shortestPath(0, 2);
            System.out.println(path);
            for (int i = 0; i < path.size() - 1; i++) {
                Configuration u = g.getVertex(path.get(i));
                Configuration v = g.getVertex(path.get(i+1));

                Line line = new Line();
                line.setStartX(u.getX());
                line.setStartY(u.getY());
                line.setEndX(v.getX());
                line.setEndY(v.getY());

                debugRoot.getChildren().add(line);
            }
            
            debugStage.setScene(debugScene);
            debugStage.show();
            return;
        }


        if (DEBUG) {
           
             System.out.println("Debug motion planner");
 
             Group debugRoot = new Group();
             Stage debugStage = new Stage();
             Scene debugScene = new Scene(debugRoot, 600, 600);

              for (PolygonObject obstacle : obstacles ) {
                  Polygon obstaclePolygon = new Polygon();
                  obstaclePolygon.getPoints().addAll(obstacle.getPointArray());
                 
                 debugRoot.getChildren().add(obstaclePolygon);
              }
  
            Robot r = new Robot(new double[] { -62.5, -75, 37.5, -75, 87.5, 75, -62.5, 75 });
            
              
             
             // create motion planner
             Environment env = new Environment(r, obstacles, 600, 400);
             Sampler sampler = new Sampler();
 
             MotionPlanner planner = MotionPlanner.getInstance();
             planner.setEnvironment(env);
             planner.setSampler(sampler);
 
             Polygon original = new Polygon();
 
             // add a button
             Button btn = new Button();
             btn.setText("sampling");
             btn.setOnAction(new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {
                     Configuration c = sampler.getSamplePoint(600, 400);
                     original.getPoints().clear();
                     original.getPoints().addAll(r.getPointArray(c));
                     original.setFill(Color.BLUE);
                     for (double rp : r.getPointArray(c)) {
                        System.out.print(rp+" ");
                     }
                     System.out.println("Gap here");

                     if (env.checkCollision(c)) {
                         original.setFill(Color.RED);
                     }
                 }
             }); 
 
             
 
             debugRoot.getChildren().add(original);
             debugRoot.getChildren().add(btn);
             debugStage.setScene(debugScene);
             debugStage.show();
  
              return;
        }

        // debug local planner
        if (DEBUG) {
            System.out.println("Debug local planner");

            Group debugRoot = new Group();
            Stage debugStage = new Stage();
            Scene debugScene = new Scene(debugRoot, 600, 600);

            // draw obstacles
            /*
            for (PolygonObject obstacle : obstacles ) {
                Polygon obstaclePolygon = new Polygon();
                obstaclePolygon.getPoints().addAll(obstacle.getPointArray());
                debugRoot.getChildren().add(obstaclePolygon);
            } */

            // set up robot, environment and local planner
            Robot r = new Robot(new double[] { -10, 10, 10, 10, 10, -10, -10, -10 });
            Environment env = new Environment(r, obstacles, 600, 400);
            LocalPlanner localPlanner = new LocalPlanner();

            // initialize configurations
            Configuration start = new Configuration(200, 200, 0);
            Configuration end = new Configuration(300, 300, Math.PI / 3.0);
            List<Configuration> configs = localPlanner.getPath(env, start, end, 10);

            // draw all the configurations
            if (configs != null) {
                for (Configuration c: configs) {
                    System.out.println("(" + c.getX() + "," + c.getY() + "," + c.getAngle() + ")");
                    Polygon poly = new Polygon();
                    poly.getPoints().addAll(env.getRobotPointArray(c));
                    debugRoot.getChildren().add(poly);                 
                }
            }
             
            debugStage.setScene(debugScene);
            debugStage.show();

            return;
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
