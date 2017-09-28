import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnvironmentTest {
    Environment e;
    Robot r;
    Polygon p;

    @BeforeEach
    void init() {

        double[] array = new double[] { -1, 2, 3, 1, 1, 2, -1, -1 };

        SimpleMatrix points = new SimpleMatrix(2, 4, true, array);
        robot = new Robot(points);

        double[] array2 = new double[] { -1, 2, 3, 1, 1, 2, -1, -1 };

        SimpleMatrix points2 = new SimpleMatrix(2, 4, true, array2);
        p = new polygon(points);

        List<Polygon> obstacles = new ArrayList<>();
        obstacles.add(p);

        e = new Environment(r, obstacles);    
    }

    @Test
    void testCollisionCheck() {
        assertTrue(e.collisionCheck());
    }
}
