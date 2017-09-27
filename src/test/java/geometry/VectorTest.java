package geometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorTest {
    Vector v;

    @BeforeEach
    void init() {
        v = new Vector(1, 2);    
    }

    @Test
    void testGetSet() {
        double delta = 0.001;

        assertEquals(v.get(0), 1, delta);
        assertEquals(v.get(1), 2, delta);
    }
}
