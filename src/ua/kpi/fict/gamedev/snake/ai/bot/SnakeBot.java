package ua.kpi.fict.gamedev.snake.ai.bot;

import ua.kpi.fict.gamedev.snake.ai.model.Direction;
import ua.kpi.fict.gamedev.snake.ai.model.Square;
import ua.kpi.fict.gamedev.snake.ai.model.SquareType;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.signum;
import static java.util.Objects.isNull;

public class SnakeBot {

    private static final int FIRST_ROUTE_POINT = 0;

    private List<Square> route;

    private RouteBuilder routeBuilder;

    public SnakeBot(RouteBuilder routeBuilder) {
        this.routeBuilder = routeBuilder;
    }

    public Direction doMovement(SquareType[][] gameBoard, Square head, Square kibble) {
        System.out.println("head = " + head);
        System.out.println("kibble = " + kibble);
        if (isNull(route) || route.isEmpty()) {
            route = routeBuilder.buildRoute(gameBoard, head, kibble);
        }
        route = routeBuilder.buildRoute(gameBoard, head, kibble);
        System.out.println("route = " + Arrays.toString(route.toArray()));
        return route.isEmpty() ? null : toDirection(head, route.remove(FIRST_ROUTE_POINT));
    }

    private Direction toDirection(Square from, Square to) {
        Square delta = new Square(to.getX() - from.getX(), to.getY() - from.getY());
        return Direction.from((int) signum(delta.getX()), (int) signum(delta.getY()));
    }
}
