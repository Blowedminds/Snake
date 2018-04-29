package huntersnake;

import game.Direction;
import game.Point;
import bodies.Body;
import bodies.Food;

import java.util.*;

public class LocalInformation {

    private Food food;
    private ArrayList<Direction> freeDirections;
    private HashMap<Direction, Body> bodies;

    public LocalInformation(Food food, ArrayList<Direction> freeDirections, HashMap<Direction, Body> bodies) {
        this.food = food;
        this.freeDirections = freeDirections;
        this.bodies = bodies;
    }

    public ArrayList<Direction> getFreeDirections() {
        return this.freeDirections;
    }

    /**
     * Find the optimal way going to Food for the given poing
     *
     * @param point
     * @return optimal Direction
     */
    public Direction getOptimizedFoodDirection(Point point) {
        // Get difference between this point and food
        int dx = point.getX() - this.food.getX();
        int dy = -(point.getY() - this.food.getY());
        //Find out food quadrant
        int foodQuadrant = this.getFoodCoordinateQuadrant(dx, dy);

        dx = Math.abs(dx);
        dy = Math.abs(dy);

        Direction direction;

        //If x is bigger than y, then move in the x axis, otherwise y axis
        if (dx - dy >= 0) {
            //According to foodQuadrant move in the right or left direction, If foodQuadrant is 0 then move in the random direction
            if (foodQuadrant == 1 || foodQuadrant == 4) {
                direction = Direction.RIGHT;
            } else if (foodQuadrant == 2 || foodQuadrant == 3) {
                direction = Direction.LEFT;
            } else {
                direction = LocalInformation.getRandomDirection(this.freeDirections);
            }
        } else {
            //According to foodQuadrant move in the up or down direction, If foodQuadrant is 0 then move in the random direction
            if (foodQuadrant == 1 || foodQuadrant == 2) {
                direction = Direction.UP;
            } else if (foodQuadrant == 3 || foodQuadrant == 4) {
                direction = Direction.DOWN;
            } else {
                direction = LocalInformation.getRandomDirection(this.freeDirections);
            }
        }

        //If the selected direction is not null, then move in the one of the free direction
        if (this.bodies.get(direction) != null) {
            return LocalInformation.getRandomDirection(this.freeDirections);
        }

        return direction;
    }

    /**
     * Check if food is near
     *
     * @return is food near
     */
    public boolean canAttackFood() {
        return this.getFoodDirection() != null;
    }

    /**
     * Looks up every Direction, If food exists return it, otherwise return null
     *
     * @return food Direction
     */
    public Direction getFoodDirection() {

        //Check for each direction
        for (Direction direction : Direction.values()) {
            if (bodies.get(direction) instanceof Food) {
                return direction;
            }
        }

        return null;
    }

    /**
     * Find out Food Quadrant according to this point
     *
     * @param dx
     * @param dy
     * @return food quadrant
     */
    private int getFoodCoordinateQuadrant(int dx, int dy) {
        if (dx <= 0 && dy < 0) {
            return 1;
        } else if (dx > 0 && dy <= 0) {
            return 2;
        } else if (dx >= 0 && dy > 0) {
            return 3;
        } else if (dx < 0 && dy >= 0) {
            return 4;
        }

        return 0;
    }

    /**
     * Utility function to get a randomly selected direction among multiple directions.
     * The selection is uniform random: All directions in the list have an equal chance to be chosen.
     *
     * @param possibleDirections list of possible directions
     * @return direction randomly selected from the list of possible directions
     */
    public static Direction getRandomDirection(List<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * possibleDirections.size());
        return possibleDirections.get(randomIndex);
    }

}
