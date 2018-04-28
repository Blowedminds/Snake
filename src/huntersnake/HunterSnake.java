package huntersnake;

import game.Action;
import game.Direction;
import game.GridGame;
import game.Point;
import bodies.Body;
import bodies.Food;
import bodies.Snake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HunterSnake extends GridGame {

    private List<Snake> snakes;
    private Body[][] bodiesMap;
    private Food food;

    public HunterSnake(int gridWidth, int gridHeight, int gridSquareSize, int frameRate) {
        super(gridWidth, gridHeight, gridSquareSize, frameRate);

        this.snakes = new ArrayList<>();
        this.bodiesMap = new Body[gridWidth][gridHeight];
        this.food = new Food((int) (Math.random() * gridWidth), (int) (Math.random() * gridHeight));
        this.bodiesMap[this.food.getX()][this.food.getY()] = this.food;

        this.addDrawable(this.food);
    }

    @Override
    protected void timerTick() {
        // Determine and execute actions for all bodies
        ArrayList<Snake> snakeCopy = new ArrayList<>(this.snakes);

        for (Snake snake : snakeCopy) {
            // Choose action
            Action action = snake.chooseAction(this.createLocalInformationForSnake(snake));
            // Execute action
            if (action.getType() == Action.Type.STAY) {
                // STAY
                snake.stay();
            } else if (action.getType() == Action.Type.REPRODUCE) {
                // REPRODUCE
                removeSnake(snake);
                addSnake(snake.reproduce());
                addSnake(snake);
            } else if (action.getType() == Action.Type.MOVE) {
                Body snakeHead = snake.getSnakeBody().getLast();
                // MOVE
                if (this.isDirectionFree(snakeHead.getX(), snakeHead.getY(), action.getDirection())) {
                    updateSnakeCurrentLocation(snake.getSnakeBody(), null);
                    snake.move(action.getDirection());
                    updateSnakeCurrentLocation(snake.getSnakeBody());
                }
            } else if (action.getType() == Action.Type.ATTACK) {

                this.addDrawable(snake.attack(action.getDirection()));
                this.replaceFood();
            }
        }
    }

    private LocalInformation createLocalInformationForSnake(Snake snake) {
        Body snakeHead = snake.getSnakeBody().getLast();
        int x = snakeHead.getX();
        int y = snakeHead.getY();

        HashMap<Direction, Body> bodies = new HashMap<>();
        bodies.put(Direction.UP, this.getBodyAtPosition(x, y - 1));
        bodies.put(Direction.DOWN, this.getBodyAtPosition(x, y + 1));
        bodies.put(Direction.LEFT, this.getBodyAtPosition(x - 1, y));
        bodies.put(Direction.RIGHT, this.getBodyAtPosition(x + 1, y));

        ArrayList<Direction> freeDirections = new ArrayList<>();
        if (this.isPointMovable(new Point(x, y - 1))) {
            freeDirections.add(Direction.UP);
        }
        if (this.isPointMovable(new Point(x, y + 1))) {
            freeDirections.add(Direction.DOWN);
        }
        if (this.isPointMovable(new Point(x - 1, y))) {
            freeDirections.add(Direction.LEFT);
        }
        if (this.isPointMovable(new Point(x + 1, y))) {
            freeDirections.add(Direction.RIGHT);
        }

        return new LocalInformation(this.food, freeDirections, bodies);
    }

    public void addSnake(Snake snake) {

        this.snakes.add(snake);

        for (Body body : snake.getSnakeBody()) {
            if (!isPositionInsideGrid(body.getX(), body.getY())) {
                continue;
            }

            this.addDrawable(body);
            this.bodiesMap[body.getX()][body.getY()] = body;
        }
    }

    private boolean isPointMovable(Point point){

        int x = point.getX();
        int y = point.getY();

        if(this.isDirectionFree(x, y , Direction.UP)) {
            return true;
        }
        if(this.isDirectionFree(x, y , Direction.DOWN)) {
            return true;
        }
        if(this.isDirectionFree(x, y , Direction.LEFT)) {
            return true;
        }
        if(this.isDirectionFree(x, y , Direction.RIGHT)) {
            return true;
        }

        return false;
    }

    private void removeSnake(Snake snake) {

        this.snakes.remove(snake);
        for (Body body : snake.getSnakeBody()) {
            if (!isPositionInsideGrid(body.getX(), body.getY())) {
                continue;
            }

            this.removeDrawable(body);
            this.bodiesMap[body.getX()][body.getY()] = null;
        }
    }

    private void updateSnakeCurrentLocation(LinkedList<Body> snakeBody) {
        for (Body body : snakeBody) {
            this.updateBodiesMap(body.getX(), body.getY(), body);
        }
    }

    private void updateSnakeCurrentLocation(LinkedList<Body> snakeBody, Body updateBody) {
        for (Body body : snakeBody) {
            this.updateBodiesMap(body.getX(), body.getY(), updateBody);
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
        if (isPositionInsideGrid(x, y)) {
            this.bodiesMap[x][y] = body;
        }
    }

    private boolean isPositionFree(int x, int y) {
        if (!isPositionInsideGrid(x, y)) {
            System.out.println("Snake over drawn");
        }
        if (getBodyAtPosition(x, y) != null) {
//            System.out.println("Snake crawled");
        }
        return isPositionInsideGrid(x, y) && getBodyAtPosition(x, y) == null;
    }

    private Body getBodyAtPosition(int x, int y) {
        if (!this.isPositionInsideGrid(x, y)) {
            return null;
        }
        return this.bodiesMap[x][y];
    }

    private void replaceFood() {
        this.updateBodiesMap(this.food.getX(), this.food.getY(), null);

        int x = (int) (Math.random() * this.getGridWidth());
        int y = (int) (Math.random() * this.getGridHeight());
        int loop_time = 0;


        while (!isPositionFree(x, y)) {
            if(loop_time > 10) {
                Point empty_set = this.findEmptyPlace();

                x = empty_set.getX();
                y = empty_set.getY();

                break;
            }

            x = (int) (Math.random() * this.getGridWidth());
            y = (int) (Math.random() * this.getGridHeight());

            loop_time++;
        }

        this.food.setLocation(x, y);

        this.updateBodiesMap(this.food.getX(), this.food.getY(), food);
    }

    private Point findEmptyPlace() {
        for(int y = this.getGridHeight() - 1; y > 0; y--) {
            for(int x = this.getGridWidth() - 1; x > 0; x--) {
                if(this.bodiesMap[x][y] == null) {
                    return new Point(x, y);
                }
            }
        }

        this.stop();

        return new Point(-1, -1);
    }
}
