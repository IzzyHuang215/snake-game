package org.cis1200.snake;

import java.util.LinkedList;

public class Grape extends Fruit {
    public Grape(int courtWidth, int courtHeight, int x, int y) {
        super(courtWidth, courtHeight, "files/grape.png", x, y, "grape");
    }

    // eating a grape adds 2 blocks to the snake
    @Override
    public void eaten(LinkedList<Snake> snake) {
        // creating new tail
        Snake currTail = snake.getLast();
        Snake newBody = new Snake(
                GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT, currTail.getPx() - currTail.getVx(),
                currTail.getPy() - currTail.getVy(), snake.get(0).getVx(), snake.get(0).getVy(),
                currTail.getColor(), currTail.getHeadColor(), currTail.getBodyColor(), false
        );

        Snake newTail = new Snake(
                GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT, newBody.getPx() - currTail.getVx(),
                newBody.getPy() - currTail.getVy(), snake.get(0).getVx(), snake.get(0).getVy(),
                currTail.getColor(), currTail.getHeadColor(), currTail.getBodyColor(), true
        );

        // updating old tail to be body
        snake.getLast().setTail(false);

        // adding tail to LinkedList
        snake.add(newBody);
        snake.add(newTail);
    }
}
