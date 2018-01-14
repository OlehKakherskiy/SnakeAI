package ua.kpi.fict.gamedev.snake.ai;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameControls implements KeyListener {


    public void keyPressed(KeyEvent ev) {
        if (SnakeGame.getGameStage() == SnakeGame.BEFORE_GAME) {
            SnakeGame.newGame();
            return;
        }

        if (SnakeGame.getGameStage() == SnakeGame.GAME_OVER) {
            SnakeGame.newGame();
            return;
        }

    }


    @Override
    public void keyReleased(KeyEvent ev) {
    }


    @Override
    public void keyTyped(KeyEvent ev) {
        char keyPressed = ev.getKeyChar();
        char q = 'q';
        if (keyPressed == q) {
            System.exit(0);    //quit if user presses the q key.
        }
    }

}
