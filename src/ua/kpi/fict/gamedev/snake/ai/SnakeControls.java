package ua.kpi.fict.gamedev.snake.ai;

import ua.kpi.fict.gamedev.snake.ai.model.Snake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by clara. Handles key presses that affect the snake.
 */
public class SnakeControls implements KeyListener {

    Snake snake;

    SnakeControls(Snake s) {
        this.snake = s;
    }

    @Override
    public void keyPressed(KeyEvent ev) {
        switch (ev.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                snake.snakeDown();
                break;
            case KeyEvent.VK_LEFT:
                snake.snakeLeft();
                break;
            case KeyEvent.VK_RIGHT:
                snake.snakeRight();
                break;
            case KeyEvent.VK_UP:
                snake.snakeUp();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent ev) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
