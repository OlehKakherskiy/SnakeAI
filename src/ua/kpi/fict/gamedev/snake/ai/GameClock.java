package ua.kpi.fict.gamedev.snake.ai;

import java.util.TimerTask;

public class GameClock extends TimerTask {

    GameComponentManager componentManager;
    DrawSnakeGamePanel gamePanel;

    public GameClock(GameComponentManager components, DrawSnakeGamePanel gamePanel) {
        this.componentManager = components;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        int stage = SnakeGame.getGameStage();


        switch (stage) {
            case SnakeGame.BEFORE_GAME: {
                break;
            }
            case SnakeGame.DURING_GAME: {
                componentManager.update();
                break;
            }
            case SnakeGame.GAME_OVER: {
                this.cancel();        //stop the Timer
                break;
            }
            case SnakeGame.GAME_WON: {
                this.cancel();   //stop Timer
                break;
            }
        }

        gamePanel.repaint();        //In every circumstance, must update screen

    }
}
