package org.cis1200.snake;

import java.awt.*;

public class Snake extends GameObj {
    public static final int SIZE = Background.SIZE;
    private static int vx = SIZE;
    private static int vy = 0;

    private int x;
    private int y;
    private Color color;
    private Color headColor;
    private Color bodyColor;
    private boolean tail;

    public Snake(
            int courtWidth, int courtHeight, int x, int y, int vx, int vy, Color color,
            Color headColor, Color bodyColor, boolean tail
    ) {
        super(vx, vy, x, y, SIZE, SIZE, courtWidth, courtHeight);

        this.x = x;
        this.y = y;
        this.color = color;
        this.headColor = headColor;
        this.bodyColor = bodyColor;
        this.tail = tail;
        this.vx = vx;
        this.vy = vy;
    }

    public Color getColor() {
        return color;
    }

    public Color getHeadColor() {
        return headColor;
    }

    public Color getBodyColor() {
        return bodyColor;
    }

    public boolean getTail() {
        return tail;
    }

    public void setTail(boolean tail) {
        this.tail = tail;
    }

    @Override
    public void draw(Graphics g) {
        // draw each snake block
        g.setColor(getColor());
        g.fillRect(x, y, SIZE, SIZE);
    }

    @Override
    public void move() {
        if (getColor().equals(headColor)) {
            color = bodyColor;
        }
    }

}
