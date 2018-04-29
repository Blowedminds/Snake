package game;

public class Point {

    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * get X
     *
     * @return x
     */
    public int getX() {
        return this.x;
    }

    /**
     * get Y
     *
     * @return y
     */
    public int getY() {
        return this.y;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
