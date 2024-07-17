package org.cis1200.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.LinkedList;

public class GameCourt extends JPanel {

    // snake game states
    private Background background;
    private Snake snakeElement;
    private Snake snakeHead;
    private int fruitEaten = 0;

    public static final Color HEADCOLOR = new Color(40, 73, 182);
    public static final Color BODYCOLOR = new Color(59, 103, 248);
    private static LinkedList<Snake> snake = new LinkedList<>();
    private static LinkedList<Fruit> fruitList = new LinkedList<>();
    private Fruit apple;
    private Fruit grape;
    private Fruit burger;
    private int maxFruitEaten = 0;

    private boolean playing = false;
    private final JLabel status;
    private final JLabel blueHighScore;

    // Game constants
    public static final int COURT_WIDTH = 500;
    public static final int COURT_HEIGHT = 500;
    public static final int COURT_SPLIT = 4;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 150;

    private static boolean isPaused = false;
    private static final File GAMESTATEFILE = new File("game_state.txt");

    public GameCourt(JLabel status, JLabel blueHighScore) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // timer is an object which triggers an action periodically with the
        // given interval. actionPerformed() method is called each time the timer triggers. 
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            int currVx;
            int currVy;

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT && snake.get(0).getVx() == 0) {
                    snake.get(0).setVx(-Snake.SIZE);
                    snake.get(0).setVy(0);

                    currVx = -Snake.SIZE;
                    currVy = 0;
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && snake.get(0).getVx() == 0) {
                    snake.get(0).setVy(0);
                    snake.get(0).setVx(Snake.SIZE);

                    currVx = Snake.SIZE;
                    currVy = 0;
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && snake.get(0).getVy() == 0) {
                    snake.get(0).setVy(Snake.SIZE);
                    snake.get(0).setVx(0);

                    currVx = 0;
                    currVy = Snake.SIZE;
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_UP && snake.get(0).getVy() == 0) {
                    snake.get(0).setVy(-Snake.SIZE);
                    snake.get(0).setVx(0);

                    currVx = 0;
                    currVy = -Snake.SIZE;
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !playing) {
                    reset();
                } else if (e.getKeyCode() == KeyEvent.VK_P) {
                    saveGame();
                }
            }
        });

        this.status = status;
        this.blueHighScore = blueHighScore;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        // setting up background
        background = new Background(COURT_WIDTH, COURT_HEIGHT, 20);

        fruitEaten = 0;

        // setting up fruit
        apple = new Apple(
                COURT_WIDTH, COURT_HEIGHT,
                GameCourt.COURT_HEIGHT / GameCourt.COURT_SPLIT + (7 * Snake.SIZE),
                (GameCourt.COURT_WIDTH / 2) - (2 * Snake.SIZE)
        );
        grape = new Grape(
                COURT_WIDTH, COURT_HEIGHT, GameCourt.COURT_WIDTH - Fruit.SIZE,
                GameCourt.COURT_HEIGHT - Fruit.SIZE
        );
        burger = new Burger(COURT_WIDTH, COURT_HEIGHT, GameCourt.COURT_WIDTH - Fruit.SIZE, 0);

        // clearing the fruit list
        for (int i = fruitList.size() - 1; i >= 0; i--) {
            fruitList.remove(i);
        }

        fruitList.add(apple);
        fruitList.add(grape);
        fruitList.add(burger);

        // clearing player1 snake
        for (int i = snake.size() - 1; i >= 0; i--) {
            snake.remove(i);
        }

        // setting original snake
        // player 1
        snakeHead = new Snake(
                COURT_WIDTH, COURT_HEIGHT,
                (COURT_WIDTH / COURT_SPLIT) - Snake.SIZE, COURT_WIDTH / 2 - (2 * Snake.SIZE),
                Snake.SIZE, 0, HEADCOLOR, HEADCOLOR, BODYCOLOR, false
        );
        snake.add(snakeHead);

        for (int i = 2; i < 5; i++) {
            boolean tail;
            tail = i == 4;

            snakeElement = new Snake(
                    COURT_WIDTH, COURT_HEIGHT,
                    ((COURT_WIDTH / COURT_SPLIT) - (i * Snake.SIZE)),
                    COURT_WIDTH / 2 - (2 * Snake.SIZE),
                    Snake.SIZE, 0, BODYCOLOR, HEADCOLOR, BODYCOLOR, tail
            );

            snake.add(snakeElement);
        }

        playing = true;
        status.setText("Fruit Eaten: " + fruitEaten);
        blueHighScore.setText("Blue High Score: " + maxFruitEaten);

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing && !isPaused) {
            // updating fruitEaten display
            if (fruitEaten > maxFruitEaten) {
                maxFruitEaten = fruitEaten;
                blueHighScore.setText("Blue High Score: " + maxFruitEaten);
            }

            // updating player 1 snake blocks
            for (Snake s : new LinkedList<>(snake)) {
                // creating a new snake head and deleting the tail
                boolean intersects = generateNewHead(s);
                if (intersects) {
                    break;
                }

                if (s.getTail()) {
                    if (!snake.isEmpty()) {
                        snake.removeLast();
                        if (!snake.isEmpty()) {
                            snake.get(snake.size() - 1).setTail(true);
                        }
                    }

                }

                // if the snake hits the wall bounds
                int headX = snake.get(0).getPx();
                int headY = snake.get(0).getPy();

                if (headX > (COURT_WIDTH - Snake.SIZE) || headX < 0
                        || headY > (COURT_HEIGHT - Snake.SIZE) || headY < 0) {
                    // make snake stop moving
                    snake.get(0).setVy(0);
                    snake.get(0).setVx(0);

                    playing = false;
                    status.setText("You lose :(!");
                    break;
                }
                s.move();
            }

            // if player 1 snake eats stuff
            if (snake.getFirst().intersects(apple)) {
                fruitEaten++;
                apple.eaten(snake);
                apple.generateNewFruit(snake);
            }
            if (snake.getFirst().intersects(grape)) {
                fruitEaten++;
                grape.eaten(snake);
                grape.generateNewFruit(snake);
            }
            if (snake.getFirst().intersects(burger)) {
                fruitEaten++;
                burger.eaten(snake);
                burger.generateNewFruit(snake);
            }

            repaint();

        }
    }

    private boolean generateNewHead(Snake s) {
        if (s.getColor().equals(HEADCOLOR)) {
            Snake newHead = new Snake(
                    COURT_WIDTH, COURT_HEIGHT,
                    snake.get(0).getPx() + s.getVx(), snake.get(0).getPy() + s.getVy(),
                    snake.getFirst().getVx(), snake.getFirst().getVy(),
                    HEADCOLOR, HEADCOLOR, BODYCOLOR, false
            );

            // if the snake intersects with its body
            for (Snake s1 : snake) {
                if (s1.getPx() == newHead.getPx() && s1.getPy() == newHead.getPy()) {
                    playing = false;
                    status.setText("You lose :(!");
                    snake.get(0).setVx(0);
                    snake.get(0).setVy(0);
                    return true;
                }
            }

            snake.addFirst(newHead);
        }
        return false;
    }

    // resuming and saving game function
    void saveGame() {
        isPaused = true;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("game_state.txt"))) {
            // saving game info

            // snake velocity
            bw.write("SnakeVx: " + snake.get(0).getVx());
            bw.newLine();
            bw.write("SnakeVx: " + snake.get(0).getVy());
            bw.newLine();

            // apple position
            bw.write("ApplePosX: " + apple.getPx());
            bw.newLine();
            bw.write("ApplePosY: " + apple.getPy());
            bw.newLine();

            // grape position
            bw.write("GrapePosX: " + grape.getPx());
            bw.newLine();
            bw.write("GrapePosY: " + grape.getPy());
            bw.newLine();

            // burger position
            bw.write("BurgerPosX: " + burger.getPx());
            bw.newLine();
            bw.write("BurgerPosY: " + burger.getPy());
            bw.newLine();

            // score
            bw.write("FruitEaten: " + snake1Eaten());
            bw.newLine();
            bw.write("BlueHighScore: " + getBlueHighScore());
            bw.newLine();

            // each snake segments positions
            for (Snake s : snake) {
                bw.write("sX: " + s.getPx());
                bw.newLine();
                bw.write("sY: " + s.getPy());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        snake.getFirst().setVx(0);
        snake.getFirst().setVy(0);
        status.setText("Game Paused");
    }

    void loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GAMESTATEFILE))) {
            background = new Background(COURT_WIDTH, COURT_HEIGHT, 20);

            // reading game state
            String line = reader.readLine();

            // snake velocities
            int snakeVx = Integer.parseInt(line.split(":")[1].trim());
            line = reader.readLine();
            int snakeVy = Integer.parseInt(line.split(":")[1].trim());
            line = reader.readLine();

            // apple positions
            int appleX = Integer.parseInt(line.split(":")[1].trim());
            line = reader.readLine();
            int appleY = Integer.parseInt(line.split(":")[1].trim());
            apple = new Apple(COURT_WIDTH, COURT_HEIGHT, appleX, appleY);
            apple.setName("apple");
            line = reader.readLine();

            // grape positions
            int grapeX = Integer.parseInt(line.split(":")[1].trim());
            line = reader.readLine();
            int grapeY = Integer.parseInt(line.split(":")[1].trim());
            grape = new Grape(COURT_WIDTH, COURT_HEIGHT, grapeX, grapeY);
            grape.setName("grape");
            line = reader.readLine();

            // burger positions
            int burgerX = Integer.parseInt(line.split(":")[1].trim());
            line = reader.readLine();
            int burgerY = Integer.parseInt(line.split(":")[1].trim());
            burger = new Burger(COURT_WIDTH, COURT_HEIGHT, burgerX, burgerY);
            burger.setName("burger");

            // clearing the fruit list
            for (int i = fruitList.size() - 1; i >= 0; i--) {
                fruitList.remove(i);
            }

            fruitList.add(apple);
            fruitList.add(grape);
            fruitList.add(burger);

            // loading scores
            line = reader.readLine();
            fruitEaten = Integer.parseInt(line.split(":")[1].trim());
            status.setText("Fruit Eaten: " + fruitEaten);

            line = reader.readLine();
            maxFruitEaten = Integer.parseInt(line.split(":")[1].trim());
            blueHighScore.setText("Blue High Score: " + maxFruitEaten);

            // loading snake segments
            line = reader.readLine();
            LinkedList<Snake> snakeResumed = new LinkedList<>();

            while (line != null) {
                int bodyX = Integer.parseInt(line.split(":")[1].trim());
                line = reader.readLine();
                int bodyY = Integer.parseInt(line.split(":")[1].trim());

                Snake bodySeg = new Snake(
                        COURT_WIDTH, COURT_HEIGHT, bodyX, bodyY, snakeVx, snakeVy, BODYCOLOR,
                        HEADCOLOR, BODYCOLOR, false
                );
                snakeResumed.add(bodySeg);

                line = reader.readLine();
            }

            Snake headSeg = new Snake(
                    COURT_WIDTH, COURT_HEIGHT, snakeResumed.get(0).getPx(),
                    snakeResumed.get(0).getPy(), snakeVx, snakeVy, HEADCOLOR, HEADCOLOR, BODYCOLOR,
                    false
            );
            Snake tailSeg = new Snake(
                    COURT_WIDTH, COURT_HEIGHT, snakeResumed.getLast().getPx(),
                    snakeResumed.getLast().getPy(), snakeVx, snakeVy, BODYCOLOR, HEADCOLOR,
                    BODYCOLOR, true
            );

            // edit the snake so that it resembles the head and tail
            snakeResumed.set(0, headSeg);
            snakeResumed.set(snakeResumed.size() - 1, tailSeg);

            snake = snakeResumed;

            playing = true;
            isPaused = false;
            requestFocusInWindow();

            // deleting the file after reading it
            reader.close();
            boolean deleted = GAMESTATEFILE.delete();

            if (deleted) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }

        } catch (IOException | NumberFormatException e) {
            reset();
        }
    }

    // getters
    public LinkedList<Snake> player1Snake() {
        return new LinkedList<>(snake);
    }

    public static LinkedList<Fruit> getFruitList() {
        return new LinkedList<>(fruitList);
    }

    public int snake1Eaten() {
        return fruitEaten;
    }

    public int getBlueHighScore() {
        return maxFruitEaten;
    }

    public int velocityXSnake1() {
        return snake.getFirst().getVx();
    }

    public int velocityYSnake1() {
        return snake.getFirst().getVy();
    }

    public boolean getPlayingState() {
        return playing;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        background.draw(g);

        // draw first snake
        for (int i = 0; i < snake.size(); i++) {
            Snake currSnake = snake.get(i);
            currSnake.draw(g);
        }

        apple.draw(g);
        grape.draw(g);
        burger.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}