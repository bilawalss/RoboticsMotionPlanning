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

import javafx.animation.SequentialTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;

import javafx.util.Duration;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static global.Constants.DEBUG;
import utils.PairRes;


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

    private static void animate(Group root, Environment env, LocalPlanner localPlanner, 
            List<Configuration> configs) {

        SequentialTransition seq = new SequentialTransition();

        // get the starting point
        Configuration start = configs.get(0);
        Polygon poly = new Polygon();
        poly.getPoints().addAll(env.getRobotPointArray(start));
        root.getChildren().add(poly);

        double xOffset = 0.0;
        double yOffset = 0.0;
        double angleOffset = 0.0;

        for (int i = 0; i < configs.size() - 1; i++) {
            Configuration end = configs.get(i+1);
            PairRes<Integer, SimpleMatrix> numAndStep  = 
                localPlanner.computeStepVector(start, end, 10.0);
            int numSteps = numAndStep.first;
            SimpleMatrix step = numAndStep.second;

            for (int j = 0; j < numSteps; j++) {
                TranslateTransition translation = new TranslateTransition();
                RotateTransition rotation = new RotateTransition();

                translation.setDuration(Duration.millis(500));

                translation.setFromX(xOffset);
                translation.setFromY(yOffset);
                translation.setByX(step.get(0, 0));
                translation.setByY(step.get(1, 0));

                translation.setRate(2);
                
                rotation.setDuration(Duration.millis(500));
                rotation.setFromAngle(Math.toDegrees(angleOffset));
                rotation.setByAngle(Math.toDegrees(step.get(2, 0)));

                rotation.setRate(2);

                ParallelTransition parallel = new ParallelTransition(poly, translation, rotation);

                seq.getChildren().add(parallel);

                // add the offsets (how far from the original position)
                xOffset += step.get(0, 0);
                yOffset += step.get(1, 0);
                angleOffset += step.get(2, 0);
            }

            start = end;
        }

        seq.play();
    }


    private static void drawGraph(Group root, Graph g, Color color) {
        // draw nodes
        for (int i = 0; i < g.size(); i++) {
            Configuration c = g.getVertex(i);
            Circle circle = new Circle();
            circle.setCenterX(c.getX());
            circle.setCenterY(c.getY());
            circle.setRadius(5.0);
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

    private static void drawGraphPRM(Group root, Graph g, Color color) {
        // draw nodes
        for (int i = 0; i < g.size() - 2; i++) {
            Configuration c = g.getVertex(i);
            Circle circle = new Circle();
            circle.setCenterX(c.getX());
            circle.setCenterY(c.getY());
            circle.setRadius(10.0);
            circle.setFill(color);
            root.getChildren().add(circle);
        } 

        Configuration start = g.getVertex(g.size()-2);
        Configuration end = g.getVertex(g.size()-1);

        Circle circle = new Circle();
        circle.setCenterX(start.getX());
        circle.setCenterY(start.getY());
        circle.setRadius(10.0);
        circle.setFill(Color.RED);
        root.getChildren().add(circle);

        Circle circle2 = new Circle();
        circle2.setCenterX(end.getX());
        circle2.setCenterY(end.getY());
        circle2.setRadius(10.0);
        circle2.setFill(Color.PURPLE);
        root.getChildren().add(circle2);


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
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600);

        // Group that contains shapes
        Group polygons = new Group();

        // load the obstacles file as stream
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resourceStream = classLoader.getResourceAsStream("resources.txt");
        Scanner sc = new Scanner(resourceStream);
        
        //Creating a scene object
        scene.setRoot(polygons);
        //Setting title to the Stage
        stage.setTitle("Robotics Simulator");

        List<PolygonObject> obstacles = new ArrayList<>();
   
        if (!sc.next().equals("OBSTACLES")) {
            return;
        }

        int obstacleNumber = sc.nextInt();
        sc.nextLine();
        // read obstacle file
        for (int n = 0; n < obstacleNumber; n++) {
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
        
            Robot r = new Robot(new double[] { -10, 0, 0, 10, 10, 0, 0, -10 });

            // create motion planner
            Environment env = new Environment(r, obstacles, 600, 400);
            Sampler sampler = new Sampler();
            LocalPlanner localPlanner = new LocalPlanner();

            Configuration start = new Configuration(200, 120, 0.3);
            Configuration end = new Configuration(150, 350, 1.2);

            RRT rrt = new RRT(env, sampler, localPlanner);

            // draw start and end
            Polygon startPoly = new Polygon();
            Polygon endPoly = new Polygon();
            startPoly.getPoints().addAll(env.getRobotPointArray(start));
            endPoly.getPoints().addAll(env.getRobotPointArray(end));

            startPoly.setFill(Color.RED);
            endPoly.setFill(Color.BLUE);

            debugRoot.getChildren().add(startPoly);
            debugRoot.getChildren().add(endPoly);

            /*
            Graph g1 = rrt.build(start, 20);
            Graph g2 = rrt.build(end, 20);

            if (rrt.merge(g1, g2, 1000) != null) {
                // draw the trees after merge
                drawGraph(debugRoot, g1, Color.RED);                    
                drawGraph(debugRoot, g2, Color.BLUE);
            }*/

            List<Configuration> path = rrt.query(start, end, 10, 1000);
            drawGraph(debugRoot, rrt.srcRRT, Color.RED);
            drawGraph(debugRoot, rrt.tarRRT, Color.BLUE);

            animate(debugRoot, env, localPlanner, path);

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

    
            if (!sc.next().equals("ROBOT")) {
                return;
            }

            sc.nextLine();

            String line = sc.nextLine();

            String[] robotTokens = line.split(" ");
            double[] robotData = new double[robotTokens.length];
            
            for (int i = 0; i < robotTokens.length; i++) {
                robotData[i] = Double.parseDouble(robotTokens[i]);
            } 

            Robot r = new Robot(robotData);
            original.setFill(Color.BLUE);

            sc.next();
            int worldWidth = sc.nextInt();
            sc.next();
            int worldHeight = sc.nextInt();


            // create motion planner
            Environment env = new Environment(r, obstacles, worldWidth, worldHeight);
            Sampler sampler = new Sampler();

            LocalPlanner localPlanner = new LocalPlanner();

            sc.next();
            int startX = sc.nextInt();
            int startY = sc.nextInt();
            double startAngle = sc.nextDouble();

            System.out.println(startX+" "+startY+" "+startAngle);

            sc.next();
            int endX = sc.nextInt();
            int endY = sc.nextInt();
            double endAngle = sc.nextDouble();

            Configuration start = new Configuration(startX, startY, startAngle);
            Configuration end = new Configuration(endX, endY, endAngle);

            PRM prm = new PRM(env, sampler, localPlanner);
            

            sc.next();
            int samplePoints = sc.nextInt();

            sc.next();
            int closestVertices = sc.nextInt();
            // third parameter is the number of sample iterations
            Graph g = prm.buildRoadMap(worldWidth, worldHeight, samplePoints, closestVertices);       
            drawGraph(prmRoot, g, Color.RED); 
                
            g = prm.pathPlanning(g, start, end, closestVertices);    
            drawGraphPRM(prmRoot, g, Color.BLUE);

            List<Configuration> res = prm.getSolution();
            
            animate(prmRoot, env, localPlanner, res);

            Date date = new Date();
            String s = date.toString();
            try {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(s + ".txt"), "utf-8"))) {
                        writer.write(prm.getTimes());
                }

                
            } catch (IOException e) {
                System.out.println("Look here: "+e.getMessage());
            }

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

