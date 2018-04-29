package bodies;

import game.Action;
import ui.Colors;
import game.Direction;
import game.Point;
import huntersnake.LocalInformation;

import java.util.Iterator;
import java.util.LinkedList;

public class Snake {

    public static int reproduceSize = 8;

    private LinkedList<Body> snakeBody;

    public Snake(Point[] points) {
        this.snakeBody = new LinkedList<>();

        //Set the bodies for the given points
        for (Point point : points) {
            this.addHead(point);
        }
    }

    /**
     * get Snake Body
     *
     * @return snake body
     */
    public LinkedList<Body> getSnakeBody() {
        return this.snakeBody;
    }

    /**
     * Choose an appropriate action for the given LocalInformation
     *
     * @param information
     * @return appropriate action
     */
    public Action chooseAction(LocalInformation information) {

        if (this.canReproduce()) {
            return new Action(Action.Type.REPRODUCE);
        } else if (this.canAttack(information)) {
            return new Action(Action.Type.ATTACK, information.getFoodDirection());
        } else if (this.canMove(information)) {
            Body snakeHead = this.snakeBody.getLast();

            return new Action(Action.Type.MOVE, information.getOptimizedFoodDirection(new Point(snakeHead.getX(), snakeHead.getY())));
        }

        //If nothing is possible, then stay at there
        return new Action(Action.Type.STAY);
    }

    /**
     * Reproduce the snake
     *
     * @return reproduced snake
     */
    public Snake reproduce() {

        //Create a point array for the new snake
        Point[] points = new Point[Snake.reproduceSize / 2];

        //Halve the snake and assign the removed points to array
        for (int i = Snake.reproduceSize / 2 - 1; this.snakeBody.size() > Snake.reproduceSize / 2; i--) {

            Body body = this.snakeBody.remove(0);

            points[i] = new Point(body.getX(), body.getY());
        }
        //Create a new snake with these points
        return new Snake(points);
    }

    /**
     * Move the whole snake body to desired direction
     *
     * @param direction
     */
    public void move(Direction direction) {
        //Create an iterator for snake body
        Iterator<Body> iterator = this.snakeBody.iterator();

        Body body = iterator.next();
        //Move every body to ancestor except head
        while (iterator.hasNext()) {

            Body nextBody = iterator.next();

            body.setLocation(nextBody.getX(), nextBody.getY());

            body = nextBody;
        }

        //Lastly, move the head to desired direction
        this.moveBodyToDirection(body, direction);
    }

    /**
     * Add one body to snake body, and move it to desired direction
     *
     * @param direction
     * @return new head
     */
    public Body attack(Direction direction) {
        //Get head
        Body snakeHead = this.snakeBody.getLast();
        //Add new head with snake head coordinate
        snakeHead = this.addHead(new Point(snakeHead.getX(), snakeHead.getY()));
        //move the new head to direction
        this.moveBodyToDirection(snakeHead, direction);

        return snakeHead;
    }

    /**
     * Just stay
     */
    public void stay() {

    }

    /**
     * Move the body to desired direction
     *
     * @param body
     * @param direction
     */
    private void moveBodyToDirection(Body body, Direction direction) {
        if (direction == Direction.DOWN) {
            body.setLocation(body.getX(), body.getY() + 1);
        } else if (direction == Direction.UP) {
            body.setLocation(body.getX(), body.getY() - 1);
        } else if (direction == Direction.LEFT) {
            body.setLocation(body.getX() - 1, body.getY());
        } else if (direction == Direction.RIGHT) {
            body.setLocation(body.getX() + 1, body.getY());
        }
    }

    /**
     * Add a body to head
     *
     * @param point
     * @return created body
     */
    private Body addHead(Point point) {
        //Set current head color to body color
        if (this.snakeBody.size() != 0) {
            this.snakeBody.getLast().setColor(Colors.snakeBody);
        }
        //Create a new body
        Body producedSnakeBody = new Body(point.getX(), point.getY(), Colors.snakeHead);
        //Add to head
        this.snakeBody.add(producedSnakeBody);

        return producedSnakeBody;
    }

    /**
     * Check if it can reproduce
     *
     * @return can reproduce
     */
    private boolean canReproduce() {
        return this.snakeBody.size() >= Snake.reproduceSize;
    }

    /**
     * Check if it can move
     *
     * @param information
     * @return can move
     */
    private boolean canMove(LocalInformation information) {
        return !information.getFreeDirections().isEmpty();
    }

    /**
     * Check if it can attack
     *
     * @param information
     * @return can attack
     */
    private boolean canAttack(LocalInformation information) {
        return information.canAttackFood();
    }
}
