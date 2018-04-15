package snake;

import game.Drawable;
import ui.GridPanel;

import java.awt.*;

public class Body implements Drawable {

    private int x, y;

    private Color color;

    public Body(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void draw(GridPanel panel) {
        panel.drawSquare(this.x, this.y, this.color);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y  = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
