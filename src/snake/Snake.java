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

    public Action chooseAction() {
//        Direction direction = LocalInformation.getRandomDirection(information.getFreeDirections());
//
        if (this.canReproduce()) {

            return new Action(Action.Type.REPRODUCE);
        } else if (this.canAttack()) {

            return new Action(Action.Type.ATTACK, Direction.DOWN);
        } else if (this.canMove()) {

            return new Action(Action.Type.MOVE, Direction.DOWN);
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

        this.moveHeadToDirection(body, direction);
    }

    public Body attack(Direction direction) {
        Body snakeHead = this.snakeBody.getLast();

        snakeHead = this.addHead(snakeHead.getX(), snakeHead.getY());

        this.moveHeadToDirection(snakeHead, direction);

        return snakeHead;
    }

    public void stay() {

    }

    private void moveHeadToDirection(Body head, Direction direction) {
        if (direction == Direction.DOWN) {
            head.setLocation(head.getX(), head.getY() + 1);
        } else if (direction == Direction.UP) {
            head.setLocation(head.getX(), head.getY() - 1);
        } else if (direction == Direction.LEFT) {
            head.setLocation(head.getX() - 1, head.getY());
        } else if (direction == Direction.RIGHT) {
            head.setLocation(head.getX() + 1, head.getY());
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

    private boolean canMove() {
        return Math.random() > .2;
    }

    private boolean canAttack() {
        return Math.random() > .7;
    }
//
//    @Override
//    public String toString() {
//        String result = "";
//
//        for (Body snake : this.snakeBody) {
//
//            result += snake.getX() + " " + snake.getY() + " \n";
//        }
//
//        return result;
//    }
}
