package snake;

import game.Action;
import game.Colors;
import game.Direction;
import game.Point;

import java.util.Iterator;
import java.util.LinkedList;

public class Snake {

    public static int reproduceSize = 8;

    private LinkedList<Body> snakeBody;

    public Snake(Point[] points) {
        this.snakeBody = new LinkedList<>();

        for (Point point : points) {
            this.addHead(point.getX(), point.getY());
        }
    }

    public LinkedList<Body> getSnakeBody() {
        return this.snakeBody;
    }

    public Action chooseAction(LocalInformation information) {
        Body snakeHead = this.snakeBody.getLast();

        if (this.canReproduce()) {
            return new Action(Action.Type.REPRODUCE);
        } else if (this.canAttack(information)) {
            return new Action(Action.Type.ATTACK, information.getOptimizedFoodDirection(new Point(snakeHead.getX(), snakeHead.getY())));
        } else if (this.canMove(information)) {
            return new Action(Action.Type.MOVE, information.getOptimizedFoodDirection(new Point(snakeHead.getX(), snakeHead.getY())));
        }

        return new Action(Action.Type.STAY);
    }

    public Snake reproduce() {

        Point[] points = new Point[Snake.reproduceSize / 2];

        for (int i = Snake.reproduceSize / 2 - 1; this.snakeBody.size() > Snake.reproduceSize / 2; i--) {

            Body body = this.snakeBody.remove(0);

            points[i] = new Point(body.getX(), body.getY());
        }

        return new Snake(points);
    }

    public void move(Direction direction) {
        Iterator<Body> iterator = this.snakeBody.iterator();

        Body body = iterator.next();

        while (iterator.hasNext()) {

            Body nextBody = iterator.next();

            body.setLocation(nextBody.getX(), nextBody.getY());

            body = nextBody;
        }

        this.moveBodyToDirection(body, direction);
    }

    public Body attack(Direction direction) {
        Body snakeHead = this.snakeBody.getLast();

        snakeHead = this.addHead(snakeHead.getX(), snakeHead.getY());

        this.moveBodyToDirection(snakeHead, direction);

        return snakeHead;
    }

    public void stay() {

    }

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

    private Body addHead(int x, int y) {
        if (this.snakeBody.size() != 0) {
            this.snakeBody.getLast().setColor(Colors.snakeBody);
        }

        Body producedSnakeBody = new Body(x, y, Colors.snakeHead);

        this.snakeBody.add(producedSnakeBody);

        return producedSnakeBody;
    }

    private boolean canReproduce() {
        return this.snakeBody.size() >= Snake.reproduceSize;
    }

    private boolean canMove(LocalInformation information) {
        return !information.getFreeDirections().isEmpty();
    }

    private boolean canAttack(LocalInformation information) {
        return information.canAttackFood();
    }
}
