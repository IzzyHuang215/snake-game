package org.cis1200.snake;

import org.junit.jupiter.api.*;

import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    // edge case
    @Test
    public void testOriginalState() {
        JLabel player1status = new JLabel();
        JLabel blueFruit = new JLabel();
        GameCourt g = new GameCourt(player1status, blueFruit);

        g.reset();

        // checking original snake size
        assertEquals(4, g.player1Snake().size());

        // checking each snake's original status
        assertEquals(0, g.snake1Eaten());
    }

    @Test
    public void testEatingFruit() {
        JLabel player1status = new JLabel();
        JLabel blueFruit = new JLabel();
        GameCourt g = new GameCourt(player1status, blueFruit);

        g.reset();

        g.tick();
        g.tick();
        g.tick();
        g.tick();
        g.tick();
        g.tick();
        g.tick();
        g.tick();
        g.tick();
        g.tick();

        // checking fruit list size
        assertEquals(3, GameCourt.getFruitList().size());

        // checking snake size
        assertEquals(1, g.snake1Eaten());
        assertEquals(5, g.player1Snake().size());

        // checking each snake's eating status
        assertEquals(1, g.snake1Eaten());

        // reset to check game states
        g.reset();

        assertEquals(4, g.player1Snake().size());

        assertEquals(0, g.snake1Eaten());
    }

    @Test
    public void testHitWall() {
        JLabel player1status = new JLabel();
        JLabel blueFruit = new JLabel();
        GameCourt g = new GameCourt(player1status, blueFruit);

        g.reset();

        for (int i = 0; i < 14; i++) {
            g.tick();
        }

        // checking velocity of each snake
        assertEquals(25, g.velocityXSnake1());
        assertEquals(0, g.velocityYSnake1());

        // check the playing state is false
        assertTrue(g.getPlayingState());
    }

    @Test
    public void testEatBurger() {
        JLabel player1status = new JLabel();
        JLabel blueFruit = new JLabel();
        GameCourt g = new GameCourt(player1status, blueFruit);

        g.reset();

        for (int i = 0; i < 15; i++) {
            g.tick();
        }
        assertTrue(g.getPlayingState());

        g.player1Snake().get(0).setVx(0);
        g.player1Snake().get(0).setVy(-25);

        for (int i = 0; i < 8; i++) {
            g.tick();
        }

        assertTrue(g.getPlayingState());

        // checking velocity of snake when eats burger
        assertEquals(0, g.velocityXSnake1());
        assertEquals(-12, g.velocityYSnake1());

        g.tick();
        // checking velocity of snake when it died
        assertEquals(0, g.velocityXSnake1());
        assertEquals(0, g.velocityYSnake1());

        // check the playing state is false - hit a wall and died
        assertFalse(g.getPlayingState());

        assertEquals(6, g.player1Snake().size());
    }

    @Test
    public void testEatGrape() {
        JLabel player1status = new JLabel();
        JLabel blueFruit = new JLabel();
        GameCourt g = new GameCourt(player1status, blueFruit);

        g.reset();

        for (int i = 0; i < 14; i++) {
            g.tick();
        }
        assertTrue(g.getPlayingState());

        g.player1Snake().get(0).setVx(0);
        g.player1Snake().get(0).setVy(25);

        for (int i = 0; i < 13; i++) {
            g.tick();
        }

        assertFalse(g.getPlayingState());

        // checking velocity of snake when it died
        assertEquals(0, g.velocityXSnake1());
        assertEquals(0, g.velocityYSnake1());

        assertEquals(6, g.player1Snake().size());

        // testing high score
        assertEquals(1, g.getBlueHighScore());
    }

    @Test
    public void testKillingItself() {
        JLabel player1status = new JLabel();
        JLabel blueFruit = new JLabel();
        GameCourt g = new GameCourt(player1status, blueFruit);

        g.reset();

        for (int i = 0; i < 8; i++) {
            g.tick();
        }
        assertTrue(g.getPlayingState());

        // going up
        g.player1Snake().get(0).setVx(0);
        g.player1Snake().get(0).setVy(-25);

        g.tick();

        // going left
        g.player1Snake().get(0).setVx(-25);
        g.player1Snake().get(0).setVy(0);

        g.tick();

        // going down
        g.player1Snake().get(0).setVx(0);
        g.player1Snake().get(0).setVy(25);

        g.tick();

        assertFalse(g.getPlayingState());

        // checking velocity of snake when it died
        assertEquals(0, g.velocityXSnake1());
        assertEquals(0, g.velocityYSnake1());

    }

}
