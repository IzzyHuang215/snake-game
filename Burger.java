package org.cis1200.snake;

import java.util.LinkedList;

public class Burger extends Fruit {

    private long startTime;
    private boolean eaten;

    public Burger(int courtWidth, int courtHeight, int x, int y) {
        super(courtWidth, courtHeight, "files/burger.png", x, y, "burger");
    }

    // if eaten makes the snake slower for 30 seconds
    @Override
    public void eaten(LinkedList<Snake> snake) {
        int originalVx = snake.getFirst().getVx();
        int originalVy = snake.getFirst().getVy();

        // record start time
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }

        // check if 10 sec passed
        if (System.currentTimeMillis() - startTime < 10000 && !eaten) {
            for (Snake s : snake) {
                s.setVx(s.getVx() / 2);
                s.setVy(s.getVy() / 2);
            }
            eaten = true;
        } else {
            // resets values after 10 seconds
            startTime = 0;
            for (Snake s : snake) {
                s.setVx(originalVx);
                s.setVy(originalVy);
            }
            eaten = false;
        }
    }
}