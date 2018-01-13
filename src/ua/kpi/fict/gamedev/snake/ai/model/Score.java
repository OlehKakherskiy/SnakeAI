package ua.kpi.fict.gamedev.snake.ai.model;

/**
 * Keeps track of, and display the user's score
 */


public class Score {

    protected int score;
    protected int highScore = 0;
    protected int increment;

    public Score() {
        score = 0;
        increment = 1;
    }

    public void resetScore() {
        score = 0;
    }


    public void increaseScore() {
        score = score + increment;
    }

    //These methods are useful for displaying score at the end of the game

    public String getStringScore() {
        return Integer.toString(score);
    }

    public String newHighScore() {

        if (score > highScore) {
            highScore = score;
            return "New High Score!!";
        } else {
            return "";
        }
    }

    public String getStringHighScore() {
        return Integer.toString(highScore);
    }

}

