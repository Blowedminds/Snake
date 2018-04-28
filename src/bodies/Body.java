package bodies;

import game.Drawable;
import game.Point;
import ui.GridPanel;

import java.awt.*;

public class Body implements Drawable {

    private Point point;

    private Color color;

    public Body(int x, int y, Color color) {
        this.point = new Point(x, y);
        this.color = color;
    }

    @Override
    public void draw(GridPanel panel) {
        panel.drawSquare(this.point.getX(), this.point.getY(), this.color);
    }

    /**
     * get X
     * @return x
     */
    public int getX() {
        return this.point.getX();
    }

    /**
     * get Y
     * @return y
     */
    public int getY() {
        return this.point.getY();
    }

    /**
     * set X and Y
     * @param x
     * @param y
     */
    public void setLocation(int x, int y) {
        this.point.setLocation(x, y);
    }

    /**
     * set the Color
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
