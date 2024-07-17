package org.cis1200;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        //setting game
        Runnable game = new org.cis1200.snake.RunSnake();

        SwingUtilities.invokeLater(game);
    }
}
