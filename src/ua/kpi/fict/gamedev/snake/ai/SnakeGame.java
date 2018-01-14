package ua.kpi.fict.gamedev.snake.ai;

import ua.kpi.fict.gamedev.snake.ai.bot.AStarRouteBuilder;
import ua.kpi.fict.gamedev.snake.ai.bot.SnakeBot;
import ua.kpi.fict.gamedev.snake.ai.model.Kibble;
import ua.kpi.fict.gamedev.snake.ai.model.Score;
import ua.kpi.fict.gamedev.snake.ai.model.Snake;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;


public class SnakeGame {

    public final static int xPixelMaxDimension = 501;  //Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
    public final static int yPixelMaxDimension = 501;
    public final static int squareSize = 50;
    public static final int BEFORE_GAME = 1;
    public static final int DURING_GAME = 2;
    public static final int GAME_OVER = 3;
    public static final int GAME_WON = 4;
    public static int xSquares;
    public static int ySquares;
    protected static Snake snake;
    private static Score score;
    private static long clockInterval = 500; //controls game speed
    private static JFrame snakeFrame;
    private static DrawSnakeGamePanel snakePanel;
    private static GameComponentManager componentManager;
    private static int gameStage = BEFORE_GAME;

    public static void main(String[] args) {
        try {
            if (args.length != 0){
                clockInterval = Long.parseLong(args[0]);
            }
        }catch(Exception e){
            System.out.println("Cannot parse argument to long");
        }
        SwingUtilities.invokeLater(() -> {
            try {
                initializeGame();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            createAndShowGUI();
        });
    }


    private static void createAndShowGUI() {
        //Create and set up the window.
        snakeFrame = new JFrame();
        snakeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);
        snakeFrame.setUndecorated(true); //hide title bar
        snakeFrame.setVisible(true);
        snakeFrame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        snakeFrame.setLocation(dim.width/2-snakeFrame.getSize().width/2, dim.height/2-snakeFrame.getSize().height/2);

        snakePanel = new DrawSnakeGamePanel(componentManager);

        snakePanel.setFocusable(true);
        snakePanel.requestFocusInWindow(); //required to give this component the focus so it can generate KeyEvents

        snakeFrame.add(snakePanel);

        //Add listeners to listen for key presses
        snakePanel.addKeyListener(new GameControls());
        snakePanel.addKeyListener(new SnakeControls(snake));

        setGameStage(BEFORE_GAME);

        snakeFrame.setVisible(true);
    }

    private static void initializeGame() {

        //set up score, snake and first kibble
        xSquares = xPixelMaxDimension / squareSize;
        ySquares = yPixelMaxDimension / squareSize;

        componentManager = new GameComponentManager();
        snake = new Snake(xSquares, ySquares);
        SnakeBot snakeBot = new SnakeBot(new AStarRouteBuilder());
        componentManager.setSnakeBot(snakeBot);
        Kibble kibble = new Kibble();

        componentManager.addSnake(snake);
        componentManager.addKibble(kibble);

        score = new Score();

        componentManager.addScore(score);

        gameStage = BEFORE_GAME;
    }

    protected static void newGame() {
        Timer timer = new Timer();
        gameStage = DURING_GAME;
        GameClock clockTick = new GameClock(componentManager, snakePanel);
        componentManager.newGame();
        timer.scheduleAtFixedRate(clockTick, 0, clockInterval);
    }


    public static int getGameStage() {
        return gameStage;
    }

    public static void setGameStage(int gameStage) {
        SnakeGame.gameStage = gameStage;
    }
}
