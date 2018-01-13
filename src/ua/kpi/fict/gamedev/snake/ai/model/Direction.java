package ua.kpi.fict.gamedev.snake.ai.model;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import static java.util.Arrays.stream;

public enum Direction {
    UP(new int[]{0, -1}, KeyEvent.VK_UP),
    DOWN(new int[]{0, 1}, KeyEvent.VK_DOWN),
    LEFT(new int[]{-1, 0}, KeyEvent.VK_LEFT),
    RIGHT(new int[]{1, 0}, KeyEvent.VK_RIGHT);

    public static final int X_DIRECTION = 0;
    public static final int Y_DIRECTION = 1;

    Direction(int[] directionUnit, int keyboardEvent) {
        this.directionUnit = directionUnit;
        this.keyboardEvent = keyboardEvent;
    }

    private int[] directionUnit;
    private int keyboardEvent;

    public static Direction from(int xUnit, int yUnit) {
        System.out.println("xUnit = " + xUnit + ", yUnit = " + yUnit);
        return stream(values())
                .filter(direction -> Arrays.equals(direction.directionUnit, new int[]{xUnit, yUnit}))
                .findAny().orElseThrow(() -> new RuntimeException(String.format("No direction with units {%s,%s}", xUnit, yUnit)));
    }

    public int getXUnit() {
        return this.directionUnit[X_DIRECTION];
    }

    public int getYUnit() {
        return this.directionUnit[Y_DIRECTION];
    }

    public int getKeyPressed() {
        return keyboardEvent;
    }

}
