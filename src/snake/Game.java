package snake;

import game.Action;
import game.Direction;
import game.GridGame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game extends GridGame {

    private List<Snake> snakes;
    private Body[][] bodiesMap;
    private Food food;

    public Game(int gridWidth, int gridHeight, int gridSquareSize, int frameRate) {
        super(gridWidth, gridHeight, gridSquareSize, frameRate);

        this.snakes = new ArrayList<>();
        this.bodiesMap = new Body[gridWidth][gridHeight];
        this.food = new Food((int) (Math.random() * gridWidth), (int) (Math.random() * gridHeight));
        this.bodiesMap[this.food.getX()][this.food.getY()] = this.food;

        this.addDrawable(this.food);
    }

    @Override
    protected void timerTick() {
        // Determine and execute actions for all creatures
        ArrayList<Snake> snakeCopy = new ArrayList<>(this.snakes);

        for (Snake snake : snakeCopy) {
            // Choose action
            Action action = snake.chooseAction();

            updateSnakeCurrentLocation(snake.getSnakeBody(), null);
            // Execute action
            if (action != null) {
                if (action.getType() == Action.Type.STAY) {
                    // STAY
                    snake.stay();
                } else if (action.getType() == Action.Type.REPRODUCE) {
                    // REPRODUCE
                    addSnake(snake.reproduce());
                } else if (action.getType() == Action.Type.MOVE) {
                    Body snakeHead = snake.getSnakeBody().getLast();
                    // MOVE
                    if (this.isDirectionFree(snakeHead.getX(), snakeHead.getY(), action.getDirection())) {
                        snake.move(action.getDirection());
                    }
                } else if (action.getType() == Action.Type.ATTACK) {

//                    Body snakeHead = snake.getSnakeBody().getLast();
                    // ATTACK
//                    Body attackedFood = this.getBodyAtDirection(snakeHead.getX(), snakeHead.getY(), action.getDirection());
                    this.addDrawable(snake.attack(action.getDirection()));
                    this.replaceFood();
                }
            }

            updateSnakeCurrentLocation(snake.getSnakeBody());
        }
    }

    private void updateSnakeCurrentLocation(LinkedList<Body> snakeBody) {
        for(Body body: snakeBody) {
            this.updateBodiesMap(body.getX(), body.getY(), body);
        }
    }

    private void updateSnakeCurrentLocation(LinkedList<Body> snakeBody, Body updateBody) {
        for(Body body: snakeBody) {
            this.updateBodiesMap(body.getX(), body.getY(), updateBody);
        }
    }

    public void addSnake(Snake snake) {

        snakes.add(snake);

        for (Body body : snake.getSnakeBody()) {
            if (!isPositionInsideGrid(body.getX(), body.getY())) {
                continue;
            }

            this.addDrawable(body);
            this.bodiesMap[body.getX()][body.getY()] = body;
        }
    }

    private boolean isPositionInsideGrid(int x, int y) {
        return (x >= 0 && x < this.getGridWidth()) && (y >= 0 && y < this.getGridHeight());
    }

    private boolean isDirectionFree(int x, int y, Direction direction) {

        if (direction == Direction.UP) {
            y--;
        } else if (direction == Direction.DOWN) {
            y++;
        } else if (direction == Direction.LEFT) {
            x--;
        } else if (direction == Direction.RIGHT) {
            x++;
        }
        return this.isPositionFree(x, y);
    }

    private void updateBodiesMap(int x, int y, Body body) {
        if(isPositionInsideGrid(x, y)) {
            this.bodiesMap[x][y] = body;
        }
    }

    private boolean isPositionFree(int x, int y) {
        return isPositionInsideGrid(x, y) && getCreatureAtPosition(x, y) == null;
    }

    private Body getCreatureAtPosition(int x, int y) {
        if (!this.isPositionInsideGrid(x, y)) {
            return null;
        }
        return this.bodiesMap[x][y];
    }

    private void replaceFood() {

    }
}
