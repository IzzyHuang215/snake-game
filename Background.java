package org.cis1200.snake;

import java.awt.*;

public class Background extends GameObj {

    public static final int SIZE = 25;
    public static final int INIT_POS_X = 170;
    public static final int INIT_POS_Y = 170;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    private static int numSquares;

    private Color darkColor = new Color(162, 209, 73);
    private Color lightColor = new Color(170, 215, 81);

    public Background(int courtWidth, int courtHeight, int numSquares) {
        super(
                INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y,
                courtWidth / numSquares, courtWidth / numSquares, courtWidth, courtHeight
        );
        Background.numSquares = numSquares;
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i < numSquares; i++) {
            for (int j = 0; j < numSquares; j++) {
                int x = i * SIZE;
                int y = j * SIZE;

                if ((i + j) % 2 == 0) {
                    g.setColor(lightColor);
                } else {
                    g.setColor(darkColor);
                }

                g.fillRect(x, y, SIZE, SIZE);
            }
        }

    }

}
