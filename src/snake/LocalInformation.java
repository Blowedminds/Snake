package snake;

import game.Direction;
import game.Point;

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

    public Direction getOptimizedFoodDirection(Point point) {
        int dx = point.getX() - this.food.getX();
        int dy = -(point.getY() - this.food.getY());

        int foodQuadrant = this.getFoodCoordinateQuadrant(dx, dy);

        dx = Math.abs(dx);
        dy = Math.abs(dy);

        Direction direction;

        if (dx - dy >= 0) {
            if (foodQuadrant == 1 || foodQuadrant == 4) {
                direction = Direction.RIGHT;
            } else if(foodQuadrant == 2 || foodQuadrant == 3){
                direction = Direction.LEFT;
            } else {
                direction = LocalInformation.getRandomDirection(this.freeDirections);
            }
        } else {
            if(foodQuadrant == 1 || foodQuadrant == 2) {
                direction = Direction.UP;
            } else if( foodQuadrant == 3 || foodQuadrant == 4){
                direction = Direction.DOWN;
            } else {
                direction = LocalInformation.getRandomDirection(this.freeDirections);
            }
        }

        if(this.bodies.get(direction) != null) {
            return LocalInformation.getRandomDirection(this.freeDirections);
        }

        return direction;
    }

    public boolean canAttackFood() {
        Iterator iterator = this.bodies.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();

            if(pair.getValue() instanceof Food) {
                return true;
            }
        }
        return false;
    }

    public Direction getFoodDirection(  ) {

        for(Direction direction: Direction.values()) {
            if(bodies.get(direction) instanceof Food) {
                return direction;
            }
        }

        return null;
    }

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
     * @param possibleDirections list of possible directions
     * @return direction randomly selected from the list of possible directions
     */
    public static Direction getRandomDirection(List<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return null;
        }
        int randomIndex = (int)(Math.random() * possibleDirections.size());
        return possibleDirections.get(randomIndex);
    }

}
