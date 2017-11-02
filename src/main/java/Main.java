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
import planning.RRT;
import planning.PRM;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import static global.Constants.DEBUG;


/**
 * A simulator for the class Robotics Motion Planning.
 * @author: Bilawal Shaikh and Thang Le
 */
public class Main extends Application {

    private AnimationTimer timer;
    
    private static void printObjectArray(Object[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + ", ");
        }
        System.out.println();
    }


    private static void drawGraph(Group root, Graph g, Color color) {
        // draw nodes
        for (int i = 0; i < g.size(); i++) {
            Configuration c = g.getVertex(i);
            Circle circle = new Circle();
            circle.setCenterX(c.getX());
            circle.setCenterY(c.getY());
            circle.setRadius(10.0);
            circle.setFill(color);
            root.getChildren().add(circle);
        } 

        // draw edges
        for (int i = 0; i < g.size() - 1; i++) {
            for (int j = i + 1; j < g.size(); j++) {
                if (g.isConnected(i, j)) {
                    Line line = new Line();

                    line.setStartX(g.getVertex(i).getX());
                    line.setStartY(g.getVertex(i).getY());
                    line.setEndX(g.getVertex(j).getX());
                    line.setEndY(g.getVertex(j).getY());

                    line.setStroke(color);
                    root.getChildren().add(line);
                }
            } 
        }       
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


        if (!DEBUG) {
            Group debugRoot = new Group();
            Stage debugStage = new Stage();
            Scene debugScene = new Scene(debugRoot, 600, 400);

            // add obstacle
            for (PolygonObject obstacle : obstacles ) {
                Polygon obstaclePolygon = new Polygon();
                obstaclePolygon.getPoints().addAll(obstacle.getPointArray());

                debugRoot.getChildren().add(obstaclePolygon);
            }
        
            Polygon original = new Polygon();
            Robot r = new Robot(new double[] { -10, 0, 0, 10, 10, 0, 0, -10 });
            original.setFill(Color.BLUE);

            // create motion planner
            Environment env = new Environment(r, obstacles, 600, 400);
            Sampler sampler = new Sampler();
            LocalPlanner localPlanner = new LocalPlanner();

            Configuration start = new Configuration(200, 120, 0.3);
            Configuration end = new Configuration(150, 350, 1.2);

            RRT rrt = new RRT(env, sampler, localPlanner);
            Graph g1 = rrt.build(start, 20);
            Graph g2 = rrt.build(end, 20);

            if (rrt.merge(g1, g2, 1000)) {
                // draw the trees after merge
                drawGraph(debugRoot, g1, Color.RED);                    
                drawGraph(debugRoot, g2, Color.BLUE);
            }

            debugStage.setScene(debugScene);
            debugStage.show();
            return;
        }

        if (DEBUG) {
            // construct PRM planner
            Group prmRoot = new Group();
            Stage prmStage = new Stage();
            Scene prmScene = new Scene(prmRoot, 600, 400);

            for (PolygonObject obstacle : obstacles ) {
                  Polygon obstaclePolygon = new Polygon();
                  obstaclePolygon.getPoints().addAll(obstacle.getPointArray());
                 
                 prmRoot.getChildren().add(obstaclePolygon);
              }

            Polygon original = new Polygon();
            Robot r = new Robot(new double[] { -10, 0, 0, 10, 10, 0, 0, -10 });
            original.setFill(Color.BLUE);

            // create motion planner
            Environment env = new Environment(r, obstacles, 600, 400);
            Sampler sampler = new Sampler();

            
            LocalPlanner localPlanner = new LocalPlanner();

            Configuration start = new Configuration(200, 120, 0.3);
            Configuration end = new Configuration(150, 350, 1.2);

            PRM prm = new PRM(env, sampler, localPlanner);

            // third parameter is the number of sample iterations
            Graph g = prm.buildRoadMap(600, 400, 100);       
            drawGraph(prmRoot, g, Color.RED); 
                
            List<Configuration> points = prm.pathPlanning(g, start, end);    

            Timeline timeline = new Timeline();

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(true);

            //You can add a specific action when each frame is started.
            /*
            timer = new AnimationTimer() {
                private long lastUpdate = 0;
                int i = 0;
                int j = 0;
                @Override
                public void handle(long now) {
                    //drawGraph(prmRoot, g, Color.RED);
                    Polygon poly = new Polygon();
                    Configuration c = points.get(i);
                    Configuration d = points.get(i+1);
                    List<Configuration> secondaryPoints = localPlanner.getPath(env, c, d, 10.0);

                    Configuration e = secondaryPoints.get(j);
                    poly.getPoints().addAll(env.getRobotPointArray(e));
                    prmRoot.getChildren().add(poly);

                    //prmRoot.getChildren().remove(poly);
                    //if (now - lastUpdate >= 28) {
                        j++;
                        if (e.equals(d)) {
                            i++;
                            j = 0;
                        }
                      //  lastUpdate = now;
                   // }
                }
            };
            */
            
            //timer.start();
            //timeline.play();

            prmStage.setScene(prmScene);
            prmStage.show();
  
            return;
        }

        if (DEBUG) {
            // test graph
            Group debugRoot = new Group();
            Stage debugStage = new Stage();
            Scene debugScene = new Scene(debugRoot, 600, 600);

            Configuration a = new Configuration(100, 100);
            Configuration b = new Configuration(200, 200);
            Configuration c = new Configuration(260, 300);
            Configuration d = new Configuration(350, 100);

            Graph g = new Graph();
            g.addVertex(a);
            g.addVertex(b);
            g.addVertex(c);
            g.addVertex(d);

            g.addEdge(0, 1);
            g.addEdge(0, 2);
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

            // find shortest path
            List<Integer> path = g.shortestPath(0, 3);

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
