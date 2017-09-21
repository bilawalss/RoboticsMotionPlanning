package objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SamplerTest {

	private boolean validPoint(Point p, int width, int height) {
		return p.getX() >= 0 && p.getY() >= 0 && p.getX() <= width && p.getY() <= height;
	}

	@Test
	public void getSquareTest() {
		int width = 600, height = 400;

		Sampler sampler = new Sampler(width, height);
		Point[] points = sampler.getSquare(50, 1);

		// distance between 2 points should be 100
		Point p1 = points[0];
		Point p2 = points[1];

		assertTrue(validPoint(p1, width, height));
		assertTrue(validPoint(p2, width, height));

		double xSquare = Math.pow(p1.getX() - p2.getX(), 2);
		double ySquare = Math.pow(p1.getY() - p2.getY(), 2);

		double distance = Math.sqrt(xSquare + ySquare);

		assertEquals(distance, 100.0, 0.001);

		// if the diameter exceeds one of the dimensions, return null
		Point[] nullArray = sampler.getSquare(201, 1);
		assertEquals(nullArray, null);	
	}
}