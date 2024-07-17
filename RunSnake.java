package org.cis1200.snake;

// imports necessary libraries for Java swing

import javax.swing.*;
import java.awt.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunSnake implements Runnable {
    private static boolean isPaused;

    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("SNAKE");
        frame.setLocation(500, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel player1Status = new JLabel("Running...");

        final JLabel blueHighScore = new JLabel("Blue High Score: ");
        status_panel.add(player1Status);
        status_panel.add(blueHighScore);

        // Main playing area
        final GameCourt court = new GameCourt(player1Status, blueHighScore);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // button to trigger popup
        JButton popupButton = new JButton("Instructions");
        control_panel.add(popupButton, BorderLayout.EAST);

        // action listener for popup
        popupButton.addActionListener(e -> showPopup());

        // pause button
        JButton pauseButton = new JButton("Pause");
        control_panel.add(pauseButton, BorderLayout.WEST);
        pauseButton.addActionListener(e -> togglePause(court));

        // Start game
        court.loadGame();
    }

    private void togglePause(GameCourt court) {
        isPaused = !isPaused;
        if (isPaused) {
            // save game state when paused
            court.saveGame();
        } else {
            // resume game state when resumed
            court.loadGame();
        }
        court.repaint();
    }

    private static void showPopup() {
        // creating popup window
        JDialog popup = new JDialog();
        popup.setTitle("Instructions");
        popup.setSize(300, 200);
        popup.setLocationRelativeTo(null);

        // centering
        JPanel centerPanel = new JPanel(new GridBagLayout());

        // adding instructions
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("goal: eat the food"));

        // adding spacing
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("ways to die:"));
        panel.add(new JLabel("1. running into yourself"));
        panel.add(new JLabel("2. running into a wall"));

        // adding spacing
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("special power ups:"));
        panel.add(new JLabel("burger: increases your speed for 10 sec"));
        panel.add(new JLabel("grape: increases your snake body by 2 segments"));

        centerPanel.add(panel);

        popup.getContentPane().add(panel);

        // make it visible
        popup.setVisible(true);
    }
}