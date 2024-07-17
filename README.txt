=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

-Four Core Concepts

  1. Collections (LinkedList)
  I decided to use a LinkedList to model the Snake and the background components as I thought it would be easiest
  when deciding between 2D arrays and Collections. The LinkedList allows me to easily add snake body segments (when
  a fruit is eaten) to extend the snake length. It also allows me to easily create the moving animation of the snake.
  I use the LinkedList to remove the tail of the list, change the original head to the color of the body, and add a
  new head given the snake's current x and y velocity. I also used a LinkedList to store the fruits that are laid out
  on the board, so I can easily loop through each and check to make sure when a fruit generates a new position, it
  doesn't generate on top of the 2 other fruits.

  2. File I/O (Pausing the game)
  I used File I/O to create a pause function in the game. If the user clicks pause or clicks the 'P' button, the snake
  game will pause. The game will use a buffered writer to save the positions of the snake, fruits, high scores, and
  current number of fruit eaten. Then, if the game is closed and reopened, the game can open up the saved file and read
  its contents to resume the game they were previously playing. When implementing File I/O, I made sure to catch any
  IOExceptions using try catches. For example, if there is no file found, then that must mean the user did not pause
  the game in a previous session, so the game will run the reset() method and start the game from the beginning.

  3. Inheritance
  My snake game inherits the GameObj class in the snake, fruit, and background class. That way, these classes all
  have the coordinate, velocity, and size variables and the getter and setter methods defined in GameObj. This creates
  unrepetitive code. I also extended the Fruit class to create different fruit which gave the snake different functions
  when it consumed it. The apple extends the snake body by one segment, the burger allows the snake to move faster for
  10 seconds, and the grape extends the snake body by two segments. I choose to inherit the fruit class when creating
  the apple, grape, and burger class because the basic function of all three were the same, the only thing that
  significantly differed was how the snake responded when the fruit was consumed. When extending the Fruit class,
  dynamic dispatch is present. For example, when I create a grape (Fruit grape), at compile time the eaten method is
  only known from the Fruit class. However, at runtime, grape will now know its own eaten function, which overrides
  the eaten method in the Fruit class.

  4. JUnit Testing
  My snake gave implements JUnit testing to ensure that the snake, fruit consumption, and death worked properly.
  Through calling the tick function and manually setting the snake's velocity, I was able to replicate what would
  happen if a user pressed the arrow keys. This allowed me to test if a certain fruit was eaten, if the snake hit a
  wall, or if the snake hit itself. The code is designed to be testable through its getter and setter functions.
  Encapsulation is kept when getting the snake (a linkedList) or the fruitList (linkedList) by returning a copy of
  the object.

=========================
=: Implementation :=
=========================

- Overview of Purpose of each Class

  Apple: snake will eat the apple and its body will grow longer by 1 segments

  Background: sets the background (dark and light green squares) for the board

  Burger: snake will eat the burger and its speed will increase

  Fruit: parent class of the apple, grape, and burger class. Provides the basic functions for each fruit
  (i.e generating new fruit, if the fruit won't spawn on top of another fruit or snake, or drawing the fruit).

  GameCourt: provides the game court of the snake game, controls the reset(), loadgame(), and savegame(). It also runs
  the main methods of the game.

  GameObj: class which provides the basic functions for the objects implemented in the game (snake, fruit, etc.)

  Grape: snake will eat the grape and its body will grow longer by 2 segments

  RunSnake: runs the snake game

  Snake: represents each segment of the snake



- Future Iterations
 Multiplayer!!!

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.)

  Apple Image: https://miro.medium.com/v2/resize:fit:1129/0*A-rPR3YwWkXonkWl.jpg
  Grape Image: https://static.vecteezy.com/system/resources/previews/005/210/265/
               non_2x/fruit-grapes-cartoon-flat-design-free-vector.jpg
  Burger Image: https://media.istockphoto.com/id/1184633031/vector/cartoon-burger-vector-isolated-illustration.jpg?
                s=612x612&w=0&k=20&c=Z66WFszea0EkDxLe2179qxjBi4zvsOVvQsZ3AbQRjB8=



