package org.cis1200.snake;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Fruit extends GameObj {

    private String imgFile;
    public static final int SIZE = Snake.SIZE;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    private int px;
    private int py;
    private String name;

    private BufferedImage img;

    public Fruit(int courtWidth, int courtHeight, String file, int x, int y, String name) {
        super(INIT_VEL_X, INIT_VEL_Y, x, y, SIZE, SIZE, courtWidth, courtHeight);
        imgFile = file;
        px = x;
        py = y;
        this.name = name;

        try {
            if (img == null) {
                img = ImageIO.read(new File(imgFile));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public void generateNewFruit(LinkedList<Snake> snake) {
        // get current values, fruit doesn't generate in the same line
        int currx = px;
        int curry = py;

        int min = 0;
        int max = GameCourt.COURT_WIDTH;

        int newXPosition = generatePosition(min, max);
        int newYPosition = generatePosition(min, max);

        while (newXPosition % (GameCourt.COURT_WIDTH / GameCourt.COURT_SPLIT) != 0
                || !fruitClear(newXPosition, newYPosition, snake) || newXPosition == currx) {
            newXPosition = generatePosition(min, max);
        }

        while (newYPosition % (GameCourt.COURT_WIDTH / GameCourt.COURT_SPLIT) != 0
                || !fruitClear(newXPosition, newYPosition, snake) || newYPosition == curry) {
            newYPosition = generatePosition(min, max);
        }

        setPx(newXPosition);
        setPy(newYPosition);
    }

    private boolean fruitClear(int x, int y, LinkedList<Snake> snake) {
        boolean clear = true;

        for (Snake s : snake) {
            if (s.getPx() == x && s.getPy() == y) {
                clear = false;
                return clear;
            }
        }

        // checking that the fruit doesn't overlap with other fruit
        for (Fruit f : new LinkedList<>(GameCourt.getFruitList())) {
            String currFruit = f.getName();
            if (currFruit.equals(name)) {
                continue;
            }

            if (f.getPx() == px && f.getPy() == py) {
                clear = false;
                return clear;
            }
        }

        return clear;
    }

    private static int generatePosition(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void eaten(LinkedList<Snake> snake) {
        // creating new tail
        Snake currTail = snake.getLast();
        Snake newTail = new Snake(
                GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT, currTail.getPx() - currTail.getVx(),
                currTail.getPy() - currTail.getVy(), snake.get(0).getVx(), snake.get(0).getVy(),
                currTail.getColor(), currTail.getHeadColor(), currTail.getBodyColor(), true
        );

        // updating old tail to be body
        snake.getLast().setTail(false);

        // adding tail to LinkedList
        snake.add(newTail);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}
