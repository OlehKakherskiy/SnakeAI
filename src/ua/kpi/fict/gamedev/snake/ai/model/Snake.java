package ua.kpi.fict.gamedev.snake.ai.model;

import ua.kpi.fict.gamedev.snake.ai.SnakeGame;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Snake {

    private LinkedList<Square> snakeSquares;  //List of snake squares or segments. Square class is simply an int x and y value.

    private Direction currentHeading;  //Direction snake is going in, not direction user is telling snake to go
    private Direction lastHeading;    //Last confirmed movement of snake. See moveSnake method

    private int growthIncrement = 2; //how many squares the snake grows after it eats a kibble

    private int growThisManySquares = 0;  //Snake grows 1 square at a time, one per clock tick. This tracks how many squares are left to add over a number of clock ticks.

    private int maxX, maxY;

    public Snake(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        createStartSnake();
    }

    public void createStartSnake() {

        //snake starts as 3 horizontal squares in the center of the screen, moving left.
        //Figure out where center is,
        int screenXCenter = maxX / 2;
        int screenYCenter = maxY / 2;

        //Create empty list
        snakeSquares = new LinkedList<>();
        snakeSquares.add(new Square(screenXCenter, screenYCenter));
        snakeSquares.add(new Square(screenXCenter + 1, screenYCenter));
        snakeSquares.add(new Square(screenXCenter + 2, screenYCenter));

        currentHeading = Direction.LEFT;
        lastHeading = Direction.LEFT;

        growThisManySquares = 0;
    }


    /* Make a copy of the list of squares and return it. Can't return the actual list in
    case something else modifies it. When an object is returned, it's actually a reference
    to that object, not a copy. And then anything that modifies that returned reference
    also modifies the original. */
    public List<Square> getSnakeSquares() {
        return snakeSquares.stream().map(s -> new Square(s.x, s.y)).collect(Collectors.toList());
    }

    public void snakeUp() {
        if (currentHeading == Direction.UP || currentHeading == Direction.DOWN) {
            return;
        }
        currentHeading = Direction.UP;
    }

    public void snakeDown() {
        if (currentHeading == Direction.DOWN || currentHeading == Direction.UP) {
            return;
        }
        currentHeading = Direction.DOWN;
    }

    public void snakeLeft() {
        if (currentHeading == Direction.LEFT || currentHeading == Direction.RIGHT) {
            return;
        }
        currentHeading = Direction.LEFT;
    }

    public void snakeRight() {
        if (currentHeading == Direction.RIGHT || currentHeading == Direction.LEFT) {
            return;
        }
        currentHeading = Direction.RIGHT;
    }


    public void moveSnake() {
        //Called every clock tick

        //Must check that the direction snake is being sent in is not contrary to current heading
        //So if current heading is down, and snake is being sent up, then should ignore.
        //Without this code, if the snake is heading up, and the user presses left then down quickly, the snake will back into itself.
        if (currentHeading == Direction.DOWN && lastHeading == Direction.UP) {
            currentHeading = Direction.UP; //keep going the same way
        }
        if (currentHeading == Direction.UP && lastHeading == Direction.DOWN) {
            currentHeading = Direction.DOWN; //keep going the same way
        }
        if (currentHeading == Direction.LEFT && lastHeading == Direction.RIGHT) {
            currentHeading = Direction.RIGHT; //keep going the same way
        }
        if (currentHeading == Direction.RIGHT && lastHeading == Direction.LEFT) {
            currentHeading = Direction.LEFT; //keep going the same way
        }


        //Make new head, and current heading, to move snake
        Square currentHead = snakeSquares.getFirst();
        int headX = currentHead.x;
        int headY = currentHead.y;

        Square newHead = new Square(headX + currentHeading.getXUnit(), headY + currentHeading.getYUnit());

        //Does this make snake hit the wall? Game over.
        if (headX >= maxX || headX < 0 || headY >= maxY || headY < 0) {
            SnakeGame.setGameStage(SnakeGame.GAME_OVER);
            return;
        }

        //Does this make the snake eat its tail? Game over.
        if (isThisInSnake(newHead)) {
            SnakeGame.setGameStage(SnakeGame.GAME_OVER);
            return;
        }

        //Otherwise, game is still on. Add new head
        snakeSquares.add(0, newHead);


        //Does new position indicate game won?
        if (wonGame()) {
            SnakeGame.setGameStage(SnakeGame.GAME_WON);
            return;
        }

        // Still playing.
        // If snake did not just eat, then remove tail segment
        // to keep snake the same length.
        if (growThisManySquares == 0) {
            snakeSquares.removeLast();
        } else {
            //Snake has just eaten. leave tail as is.  Decrease growThisManySquares variable by 1.
            growThisManySquares--;
        }

        lastHeading = currentHeading; //Update last confirmed heading

    }


    /* Game controller calls this to notify snake */
    public void youAteKibble() {
        growThisManySquares += growthIncrement;
    }


    public String toString() {
        StringBuilder textSnake = new StringBuilder();
        int segment = 0;
        for (Square s : snakeSquares) {
            textSnake.append(format("Segment %d [%d, %d]", segment, s.x, s.y));
            segment++;
        }

        return textSnake.toString();
    }

    public boolean wonGame() {

        //If all of the squares have snake segments in, the snake has eaten so much kibble
        //that it has filled the screen. Win!.

        //Number of squares on screen = x squares * y squares

        return snakeSquares.size() == (maxX * maxY);
    }


    /* Convenience method for testing if a square is one of the snake squares.
     * This is helpful to decide if a kibble and snake are in the same place i.e. snake
     * has eaten the kibble. Could also be useful to test if the snake has hit a wall or maze. */
    public boolean isThisInSnake(Square testSquare) {
        return snakeSquares.stream().anyMatch(square -> square.x == testSquare.x && square.y == testSquare.y);
    }

}


