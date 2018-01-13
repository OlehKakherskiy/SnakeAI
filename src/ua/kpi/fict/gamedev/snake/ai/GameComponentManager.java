package ua.kpi.fict.gamedev.snake.ai;

import ua.kpi.fict.gamedev.snake.ai.bot.SnakeBot;
import ua.kpi.fict.gamedev.snake.ai.model.*;

import java.util.Objects;

/**
 * Created by Clara. Manages game components such as the Snake, Kibble... and their interactions.
 */
public class GameComponentManager {

    private static int widthCount = SnakeGame.xPixelMaxDimension / SnakeGame.squareSize;
    private static int heightCount = SnakeGame.yPixelMaxDimension / SnakeGame.squareSize;
    private Kibble kibble;
    private Snake snake;
    private Score score;

    private SnakeBot snakeBot;

    public void update() {
        Direction direction = snakeBot.doMovement(prepareBoard(), snake.getSnakeSquares().get(0), kibble.getSquare());
        if (Objects.nonNull(direction)) {
            switch (direction) {
                case LEFT:
                    snake.snakeLeft();
                    break;
                case UP:
                    snake.snakeUp();
                    break;
                case DOWN:
                    snake.snakeDown();
                    break;
                case RIGHT:
                    snake.snakeRight();
                    break;
            }
        }
        snake.moveSnake();

        //Ask the snake if it is on top of the kibble
        if (snake.isThisInSnake(kibble.getSquare())) {
            //If so, tell the snake that it ate the kibble
            snake.youAteKibble();
            //And, update the kibble - move it to a new square. Got to check to make sure
            //that the new square is not inside the snake.
            Square kibbleLoc;
            do {
                kibbleLoc = kibble.moveKibble();
            } while (snake.isThisInSnake(kibbleLoc));

            score.increaseScore();
        }
    }

    private SquareType[][] prepareBoard() {
        SquareType[][] board = new SquareType[widthCount][heightCount];
        board[kibble.getKibbleX()][kibble.getKibbleY()] = SquareType.KIBBLE;
        snake.getSnakeSquares().stream().filter(square -> !square.isOutOfBounds(widthCount, heightCount)).forEach(square -> board[square.getX()][square.getY()] = SquareType.SNAKE);
        return board;
    }

    public void newGame() {
        score.resetScore();
        snake.createStartSnake();
    }


    public void addKibble(Kibble kibble) {
        this.kibble = kibble;
    }

    public void addSnake(Snake snake) {
        this.snake = snake;
    }

    public void addScore(Score score) {
        this.score = score;
    }

    public Score getScore() {
        return score;
    }

    public Kibble getKibble() {
        return kibble;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnakeBot(SnakeBot snakeBot) {
        this.snakeBot = snakeBot;
    }
}
