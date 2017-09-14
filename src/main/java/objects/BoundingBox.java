package objects;


public class BoundingBox {
    private double x, y, width, height;

    /**
     * Instantiate a bounding box.
     * @param points the list of (x, y) points, which should contain at least 1 point.
     */
    public BoundingBox (Point[] points) {
        if (points == null || points.length == 0)
            return;

        // compute parameters for the bounding box by getting min and max of both x and y.
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = 0.0, maxY = 0.0;
        for (Point p : points) {
            double px = p.getX(), py = p.getY();
            minX = Math.min(minX, px);
            maxX = Math.max(maxX, px);
            minY = Math.min(minY, py);
            maxY = Math.max(maxY, py);
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
        Point topLeft = other.getTopLeftPoint(), topRight = other.getTopRightPoint(),
                 bottomLeft = other.getBottomLeftPoint(), bottomRight = other.getBottomRightPoint();

        return containsPoint(topLeft) || containsPoint(topRight) ||
               containsPoint(bottomLeft) || containsPoint(bottomRight);
    }

    public boolean containsPoint (Point point) {
        double px = point.getX(), py = point.getY();
        double rightX = x + width;
        double rightY = y + height;

        return px >= x && px <= rightX && py >= y && py <= rightY;
    }

    public Point getTopLeftPoint () {
        return new Point(x, y);
    }

    public Point getTopRightPoint () {
        return new Point(x + width, y);
    }

    public Point getBottomLeftPoint () {
        return new Point(x, y + height);
    }

    public Point getBottomRightPoint () {
        return new Point(x + width, y + height);
    }
}
