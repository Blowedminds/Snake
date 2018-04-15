package snake;

import game.GridGame;

import java.util.ArrayList;
import java.util.List;

public class Game extends GridGame {

    private List<Body> bodies;
    private Body[][] bodyMap;

    public Game(int gridWidth, int gridHeight, int gridSquareSize, int frameRate) {
        super(gridWidth, gridHeight, gridSquareSize, frameRate);

        this.bodies = new ArrayList<>();
        this.bodyMap = new Body[gridWidth][gridHeight];
    }

    public void addSnake(Snake snake) {

        for (Body body : snake.getSnakeBody()) {
            if (!isPositionInsideGrid(body.getX(), body.getY())) {
                continue;
            }

            bodies.add(body);
            addDrawable(body);
            bodyMap[body.getX()][body.getY()] = body;
        }
    }

    @Override
    protected void timerTick() {

    }

    private boolean isPositionInsideGrid(int x, int y) {
        return (x >= 0 && x < getGridWidth()) && (y >= 0 && y < getGridHeight());
    }

}
