package ua.kpi.fict.gamedev.snake.ai.model;

import java.util.Objects;

// Convenient class to hold x and y value. Represents a square in the game's grid - not pixels.
public class Square {

    int x;
    int y;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Square moveInDirection(Direction direction) {
        return new Square(x + direction.getXUnit(), y + direction.getYUnit());
    }

    public boolean isOutOfBounds(int xMax, int yMax) {
        return x < 0 || x >= xMax || y < 0 || y >= yMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return x == square.x && y == square.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Square{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
