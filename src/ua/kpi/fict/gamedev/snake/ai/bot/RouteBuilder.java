package ua.kpi.fict.gamedev.snake.ai.bot;

import ua.kpi.fict.gamedev.snake.ai.model.Square;
import ua.kpi.fict.gamedev.snake.ai.model.SquareType;

import java.util.List;

public interface RouteBuilder {
    List<Square> buildRoute(SquareType[][] gameBoard, Square head, Square kibble);
}
