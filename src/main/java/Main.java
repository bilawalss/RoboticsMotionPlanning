import java.io.*;
import java.util.*;

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

import javafx.util.Pair;
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

import static global.Constants.DEBUG;
import utils.PairRes;


/**
 * A simulator for the class Robotics Motion Planning.
 * @author: Bilawal Shaikh and Thang Le
 */
public class Main extends Application {
    private static MotionPlanner planner;
    private static Configuration[][] queries;

    private static void printObjectArray(Object[] data) {
        for (Object aData : data) {
            System.out.print(aData + ", ");
        }
        System.out.println();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600);

        stage.setTitle("Robotics Simulator");
        stage.setScene(scene);

        // draw obstacles
        for (PolygonObject obstacle: planner.getEnvironment().getObstacles()) {
            Polygon poly = new Polygon();
            poly.getPoints().addAll(obstacle.getPointArray());
            root.getChildren().add(poly);
        }

        // running queries
        if (planner instanceof RRT) {
            RRT rrtPlanner = (RRT) planner;

            Configuration[] q = queries[0];
            List<Configuration> path = rrtPlanner.query(q[0], q[1]);

            // draw trees
            drawGraph(root, rrtPlanner.getSrcRRT(), Color.RED);
            drawGraph(root, rrtPlanner.getTarRRT(), Color.BLUE);
            animate(root, rrtPlanner.getEnvironment(), rrtPlanner.getLocalPlanner(), path);

        } else if (planner instanceof  PRM) {
            PRM prmPlanner = (PRM) planner;

        } else {
            System.out.println("Unexpected error");
            return;
        }

        stage.show();
    }


    public static void main(String args[]) {
        System.out.println(Arrays.toString(args));
        if (args.length < 2) {
            System.out.println("Need environment file and config file");
            System.exit(0);
        }

        try {
            // parse environment and local planner
            PairRes<Environment, LocalPlanner> envAndLocalPlanner = parseEnvironment(args[0]);
            Environment env = envAndLocalPlanner.first;
            LocalPlanner localPlanner = envAndLocalPlanner.second;

            // parse algorithm information
            HashMap<String, String> props = parseConfig(args[1]);

            // initialize the planner
            if (props.get("ALGORITHM").equals("RRT")) {
                int numTrials = Integer.parseInt(props.get("TRIALS"));
                planner = new RRT(env, new Sampler(), localPlanner, numTrials);
            } else {

            }

            launch(args);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /* ---------------- HELPER FUNCTIONS FOR PARSING ---------------- */

    private static PairRes<Environment, LocalPlanner> parseEnvironment(String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        Scanner scan = new Scanner(file);

        // get world information
        double worldWidth = Double.parseDouble(scan.nextLine().split("=")[1].trim());
        double worldHeight = Double.parseDouble(scan.nextLine().split("=")[1].trim());
        double resDist = Double.parseDouble(scan.nextLine().split("=")[1].trim());

        // robot shape
        scan.nextLine(); // ROBOT
        double[] robotPoints = parsePoints(scan.nextLine().trim());
        Robot robot = new Robot(robotPoints);

        // obstacles
        scan.nextLine(); // OBSTACLES
        int numObstacles = Integer.parseInt(scan.nextLine().trim());
        List<PolygonObject> obstacleList = new ArrayList<>();
        for (int i = 0; i < numObstacles; i++) {
            double[] obsPoints = parsePoints(scan.nextLine().trim());
            PolygonObject obstacle = new PolygonObject(obsPoints);
            obstacleList.add(obstacle);
        }

        // queries
        scan.nextLine(); // QUERIES
        int numQueries = Integer.parseInt(scan.nextLine().trim());
        queries = new Configuration[numQueries][2];
        for (int i = 0; i < numQueries; i++) {
            double[] startPoints = parsePoints(scan.nextLine().trim());
            double[] endPoints = parsePoints(scan.nextLine().trim());
            queries[i][0] = new Configuration(startPoints[0], startPoints[1], startPoints[2]); // start
            queries[i][1] = new Configuration(endPoints[0], endPoints[1], endPoints[2]);       // end
        }

        // construct environment and local planner
        Environment env = new Environment(robot, obstacleList, worldWidth, worldHeight);
        LocalPlanner localPlanner = new LocalPlanner(resDist);

        return new PairRes<>(env, localPlanner);
    }


    private static HashMap<String, String> parseConfig (String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        Scanner scan = new Scanner(file);
        // store properties
        HashMap<String, String> props = new HashMap<>();

        while (scan.hasNextLine()) {
            String line = scan.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] tokens = line.split("=");
            props.put(tokens[0], tokens[1]);
        }

        return props;
    }


    private static double[] parsePoints(String line) {
        String[] pointStr = line.split("\\s+");
        double[] res = new double[pointStr.length];

        for (int i = 0; i < pointStr.length; i++) {
            res[i] = Double.parseDouble(pointStr[i]);
        }

        return res;
    }

    /* ---------------- HELPER FUNCTIONS FOR DRAWING ---------------- */

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
                    localPlanner.computeStepVector(start, end);
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

}

