package snake;

import game.Colors;
import game.Direction;

import java.util.LinkedList;

public class Snake {

    private LinkedList<Body> snakeBody;

    public Snake(int quantity, int tailX, int tailY) {
        this.snakeBody = new LinkedList<>();

        for (int i = 0; i < quantity; i++) {
            this.addBodyToHead(tailX + i, tailY);
        }
    }

    public LinkedList<Body> getSnakeBody() {
        return this.snakeBody;
    }

    public void move(Direction direcion()) {

    }

    private void addBodyToHead(int x, int y) {
        if(this.snakeBody.size() != 0) {

            this.snakeBody.get(0).setColor(Colors.snakeBody);
        }

        this.snakeBody.add(0, new Body(x, y, Colors.snakeBody));
    }

    @Override
    public String toString() {
        String result = "";

        for(Body snake: this.snakeBody) {

            result += snake.getX() + " " + snake.getY() + " \n";
        }

        return result;
    }
}
