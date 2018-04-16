package main;

import game.Point;
import snake.Game;
import snake.Snake;
import ui.ApplicationWindow;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // Create GridGame
                // You can change the world width and height, size of each grid square in pixels or the GridGame speed
                Game game = new Game(50, 50, 10, 1);

                Point[] points = {
                        new Point(10,10),
                        new Point(11, 10),
                        new Point(12, 10),
                        new Point(13, 10)
                };

                game.addSnake(new Snake(points));

                // Create application window that contains the GridGame panel
                ApplicationWindow window = new ApplicationWindow(game.getGamePanel());
                window.getFrame().setVisible(true);

                // Start GridGame
                game.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
