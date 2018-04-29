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

    /**
     * Define the functionality of the game executed after each timer clock
     */
    @Override
    protected void timerTick() {
        /**
         * While we are iterating over snakes, we cannot add some new snakes to original List due to uncompleted iteration.
         * So we clone our original snake object
         *
         */
        ArrayList<Snake> snakeCopy = new ArrayList<>(this.snakes);

        //This will be our condition to stop the game, If it reaches to snakes size, then stop the game
        int stayedSnake = 0;

        for (Snake snake : snakeCopy) {
            // Choose action
            Action action = snake.chooseAction(this.createLocalInformationForSnake(snake));
            // Execute action
            if (action.getType() == Action.Type.STAY) {
                // STAY
                snake.stay();
                stayedSnake++;
            } else if (action.getType() == Action.Type.REPRODUCE) {
                // REPRODUCE
                //Firstly remove snake
                removeSnake(snake);
                //Add reproduced snake
                addSnake(snake.reproduce());
                //Then add snake
                addSnake(snake);
            } else if (action.getType() == Action.Type.MOVE) {
                Body snakeHead = snake.getSnakeBody().getLast();
                // MOVE
                if (this.isDirectionFree(snakeHead.getX(), snakeHead.getY(), action.getDirection())) {
                    //Firstly remove snake from map
                    updateSnakeCurrentLocation(snake.getSnakeBody(), null);
                    //Move the snake
                    snake.move(action.getDirection());
                    //Update with new point
                    updateSnakeCurrentLocation(snake.getSnakeBody());
                }
            } else if (action.getType() == Action.Type.ATTACK) {
                //Add the produced body
                this.addDrawable(snake.attack(action.getDirection()));
                //Place food to different place
                this.replaceFood();
            }
        }

        if (this.snakes.size() == stayedSnake) {
            System.out.println("There is left no move, game is stopped");
            this.stop();
        }
    }

    /**
     * Create LocalInformation for the snake
     *
     * @param snake
     * @return LocalInformation for the snake
     */
    private LocalInformation createLocalInformationForSnake(Snake snake) {
        //Get the snake head
        Body snakeHead = snake.getSnakeBody().getLast();
        int x = snakeHead.getX();
        int y = snakeHead.getY();

        //Find out environment
        HashMap<Direction, Body> bodies = new HashMap<>();
        bodies.put(Direction.UP, this.getBodyAtPosition(x, y - 1));
        bodies.put(Direction.DOWN, this.getBodyAtPosition(x, y + 1));
        bodies.put(Direction.LEFT, this.getBodyAtPosition(x - 1, y));
        bodies.put(Direction.RIGHT, this.getBodyAtPosition(x + 1, y));
        //Find free directions
        ArrayList<Direction> freeDirections = new ArrayList<>();
        if (this.isDirectionFree(x, y, Direction.UP)) {
            freeDirections.add(Direction.UP);
        }
        if (this.isDirectionFree(x, y, Direction.DOWN)) {
            freeDirections.add(Direction.DOWN);
        }
        if (this.isDirectionFree(x, y, Direction.LEFT)) {
            freeDirections.add(Direction.LEFT);
        }
        if (this.isDirectionFree(x, y, Direction.RIGHT)) {
            freeDirections.add(Direction.RIGHT);
        }

        return new LocalInformation(this.food, freeDirections, bodies);
    }

    /**
     * Add snake to drawables
     *
     * @param snake
     */
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

    /**
     * Remove snake from drawables
     *
     * @param snake
     */
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

    /**
     * Update snake current location with its body
     *
     * @param snakeBody
     */
    private void updateSnakeCurrentLocation(LinkedList<Body> snakeBody) {
        for (Body body : snakeBody) {
            this.updateBodiesMap(body.getX(), body.getY(), body);
        }
    }

    /**
     * Update snake current location with the given updateBody
     *
     * @param snakeBody
     * @param updateBody
     */
    private void updateSnakeCurrentLocation(LinkedList<Body> snakeBody, Body updateBody) {
        for (Body body : snakeBody) {
            this.updateBodiesMap(body.getX(), body.getY(), updateBody);
        }
    }

    /**
     * Check if given position is inside grid
     *
     * @param x
     * @param y
     * @return inside grid
     */
    private boolean isPositionInsideGrid(int x, int y) {
        return (x >= 0 && x < this.getGridWidth()) && (y >= 0 && y < this.getGridHeight());
    }

    /**
     * Check if given direction is free
     *
     * @param x
     * @param y
     * @param direction
     * @return free direction
     */
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

    /**
     * Fill the given position with given body
     *
     * @param x
     * @param y
     * @param body
     */
    private void updateBodiesMap(int x, int y, Body body) {
        if (isPositionInsideGrid(x, y)) {
            this.bodiesMap[x][y] = body;
        }
    }

    /**
     * Check if given position is free
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isPositionFree(int x, int y) {
        return isPositionInsideGrid(x, y) && getBodyAtPosition(x, y) == null;
    }

    /**
     * get body at position
     *
     * @param x
     * @param y
     * @return body at position
     */
    private Body getBodyAtPosition(int x, int y) {
        if (!this.isPositionInsideGrid(x, y)) {
            return null;
        }
        return this.bodiesMap[x][y];
    }

    /**
     * Replace food randomly
     */
    private void replaceFood() {
        //Remove food current location with
        this.updateBodiesMap(this.food.getX(), this.food.getY(), null);

        //Select a random position
        int x = (int) (Math.random() * this.getGridWidth());
        int y = (int) (Math.random() * this.getGridHeight());
        //If looped over 10 times, find an empty place
        int loop_time = 0;

        //Loop until random position is free
        while (!isPositionFree(x, y)) {
            if (loop_time > 10) {
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

    /**
     * Find an empty point
     *
     * @return empty point
     */
    private Point findEmptyPlace() {
        for (int y = this.getGridHeight() - 1; y > 0; y--) {
            for (int x = this.getGridWidth() - 1; x > 0; x--) {
                if (this.bodiesMap[x][y] == null) {
                    return new Point(x, y);
                }
            }
        }

        this.stop();

        return new Point(-1, -1);
    }
}
