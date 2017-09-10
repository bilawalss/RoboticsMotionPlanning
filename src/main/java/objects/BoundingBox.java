package objects;


public class BoundingBox {
    private double x, y, width, height;

    /**
     * Instantiate a bounding box.
     * @param points the list of (x, y) points, which should contain at least 1 point.
     */
    public BoundingBox (double... points) {
        if (points == null || points.length == 0 || points.length % 2 != 0)
            return;

        // compute parameters for the bounding box by getting min and max of both x and y.
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = 0.0, maxY = 0.0;
        for (int i = 0; i < points.length; i++) {
            // x or y coordinate
            if (i % 2 == 0) {
                minX = Math.min(minX, points[i]);
                maxX = Math.max(maxX, points[i]);
            } else {
                minY = Math.min(minY, points[i]);
                maxY = Math.max(maxY, points[i]);
            }
        }

        this.x = minX;
        this.y = minY;
        this.width = maxX - minX;
        this.height = maxY - minY;
    }

    /**
     * Check if this bouding box collides with another bouding box.
     * @param other another bounding box
     * @return true if they collide and false otherwise
     */
    public boolean collidesWith (BoundingBox other) {
        // check if this bounding box contains all 4 corners of the other bounding box
        double[] topLeft = other.getTopLeftPoint(), topRight = other.getTopRightPoint(),
                 bottomLeft = other.getBottomLeftPoint(), bottomRight = other.getBottomRightPoint();

        return containsPoint(topLeft[0], topLeft[1]) || containsPoint(topRight[0], topRight[1]) ||
               containsPoint(bottomLeft[0], bottomLeft[1]) || containsPoint(bottomRight[0], bottomRight[1]);
    }

    /**
     * Check if this bounding box contains a given point.
     * @param px the x-coordinate of the point
     * @param py the y-coordinate of the point
     * @return true if this bouding box contains the point and false otherwise
     */
    public boolean containsPoint (double px, double py) {
        double rightX = x + width;
        double rightY = y + height;

        return px >= x && px <= rightX && py >= y && py <= rightY;
    }

    public double[] getTopLeftPoint () {
        return new double[] { x, y };
    }

    public double[] getTopRightPoint () {
        return new double[] { x + width , y };
    }

    public double[] getBottomLeftPoint () {
        return new double[] { x, y + height };
    }

    public double[] getBottomRightPoint () {
        return new double[] { x + width , y + height };
    }
}
