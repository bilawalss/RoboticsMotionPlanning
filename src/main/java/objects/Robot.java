package objects;

public class Robot {
    /*
     * x and z are opposite and y is on the right of the line xz.
     * xz will always be the longer diameter.
     */
    private Point x,y;

    public Robot(Point x, Point y) {
        this.x = new Point(x);
        this.y = new Point(y);
    }

    public void rotate (double angle) {
        // rotates around point center
        double x = (this.x.getX() + this.y.getX())/2;
        double y = (this.x.getY() + this.y.getY())/2;

        this.x.setX((this.x.getX() - x)*Math.cos(angle) - (this.x.getY() - y)*Math.sin(angle) + x);
        this.y.setY((this.x.getX() - x)*Math.sin(angle) - (this.x.getY() - y)*Math.cos(angle) + y);

        this.y.setX((this.y.getX() - x)*Math.cos(angle) - (this.y.getY() - y)*Math.sin(angle) + x);
        this.y.setY((this.y.getX() - x)*Math.sin(angle) - (this.y.getY() - y)*Math.cos(angle) + y);
        
    }

    public void move (double dx, double dy) {
         x.setX(x.getX() + dx);
         x.setY(x.getY() + dy);

         y.setX(y.getX() + dx);
         y.setY(y.getY() + dy);

    }

    public double getHypotenuse() {
        return Math.sqrt(Math.pow(this.x.getX() - this.y.getX(), 2) + Math.pow(this.x.getX() - this.y.getX(), 2));
    }

    public Point[] getPoints() {
        Point[] points = new Point[4];
        for (int i = 0; i < 4; i++) {
            points[i] = new Point(0.0, 0.0);
        }
        points[0].setX(this.x.getX());
        points[0].setY(this.x.getY());

        points[1].setX((this.x.getX() + this.y.getX() + this.x.getY() - this.y.getY())/2);
        points[1].setY((this.x.getY() + this.y.getY() + this.y.getX() - this.x.getX())/2);

        points[2].setX(this.y.getX());
        points[2].setY(this.y.getY());

        points[3].setX((this.x.getX() + this.y.getX() + this.y.getY() - this.x.getY())/2);
        points[3].setY((this.x.getY() + this.y.getY() + this.x.getX() - this.y.getX())/2);

      


        for (Point p : points) {
            System.out.println(p.getX()+" "+p.getY());
        }
        System.out.println();
        return points;
    }

    public double getSlope() {
        if (this.x.getX() - this.y.getX() == 0) return Double.POSITIVE_INFINITY;

        if (this.x.getY() - this.y.getY() == 0) {
            return (this.x.getY() - this.y.getY())/(this.x.getX() - this.y.getX());
        } else {
            return (this.y.getY() - this.x.getY())/(this.y.getX() - this.x.getX());
        }
    }
}
