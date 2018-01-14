package ua.kpi.fict.gamedev.snake.ai.bot;

import ua.kpi.fict.gamedev.snake.ai.model.Direction;
import ua.kpi.fict.gamedev.snake.ai.model.Square;
import ua.kpi.fict.gamedev.snake.ai.model.SquareType;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class AStarRouteBuilder implements RouteBuilder {

    private static class CellFRate implements Comparable<CellFRate> {
        private Square square;
        private CellFRate parent;
        private int gScore;
        private int fScore;

        public CellFRate(Square square, CellFRate parent, Square kibble) {
            this.square = square;
            this.parent = parent;
            this.gScore = isNull(parent) ? 0 : parent.gScore + 1;
            this.fScore = calculateFScore(kibble);
        }

        private int calculateFScore(Square kibble) {
            fScore = gScore + calculateHScore(kibble);
            return fScore;
        }

        private int calculateHScore(Square kibble) {
            return Math.abs(kibble.getX() - square.getX()) + Math.abs(kibble.getY() - square.getY());
        }

        private Square getSquare() {
            return square;
        }

        @Override
        public int compareTo(CellFRate anotherCell) {
            return Integer.compare(this.fScore, anotherCell.fScore);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CellFRate cellFRate = (CellFRate) o;
            return Objects.equals(square, cellFRate.square);
        }

        @Override
        public int hashCode() {
            return Objects.hash(square);
        }
    }

    @Override
    public List<Square> buildRoute(SquareType[][] gameBoard, Square head, Square kibble) {
        Set<Square> seenSquares = new HashSet<>();
        PriorityQueue<CellFRate> squaresToLook = new PriorityQueue<>();
        squaresToLook.add(new CellFRate(head, null, kibble));
        CellFRate lastProcessed = null;
        while (!squaresToLook.isEmpty()) {
            lastProcessed = squaresToLook.poll();
            seenSquares.add(lastProcessed.square);
            if (kibble.equals(lastProcessed.square)) {
                break;
            }
            fillSquaresToProcess(lastProcessed, kibble, squaresToLook, seenSquares, gameBoard);
        }
        List<Square> route = processRoute(lastProcessed);
        if (!canReachKibble(kibble, route)) {
            route = new ArrayList<>(findLongestPath(gameBoardToOccupyBoard(gameBoard, head), head, kibble));
        }
        route.remove(0);
        return route;
    }

    private boolean canReachKibble(Square kibble, List<Square> route) {
        return kibble.equals(route.get(route.size() - 1));
    }

    private boolean[][] gameBoardToOccupyBoard(SquareType[][] gameBoard, Square head) {
        boolean[][] occupyBoard = new boolean[gameBoard.length][gameBoard[0].length];
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                occupyBoard[i][j] = gameBoard[i][j] == SquareType.SNAKE;
            }
        }
        occupyBoard[head.getX()][head.getY()] = false;
        return occupyBoard;
    }

    private void fillSquaresToProcess(CellFRate lastProcessed, Square kibble, PriorityQueue<CellFRate> squaresToLook, Set<Square> seenSquares, SquareType[][] board) {
        squaresToLook.addAll(Arrays.stream(Direction.values())
                .map(direction -> lastProcessed.square.moveInDirection(direction))
                .filter(square -> !seenSquares.contains(square))
                .filter(square -> !square.isOutOfBounds(board.length, board[0].length))
                .filter(square -> board[square.getX()][square.getY()] != SquareType.SNAKE)
                .map(square -> new CellFRate(square, lastProcessed, kibble)).collect(toList()));
    }

    private List<Square> processRoute(CellFRate routeEnd) {
        if (isNull(routeEnd)) {
            throw new RuntimeException("Route end is null");
        }
        List<Square> routePart = new ArrayList<>();
        if (nonNull(routeEnd.parent)) {
            routePart.addAll(processRoute(routeEnd.parent));
        }
        routePart.add(routeEnd.square);
        return routePart;
    }

    private List<Square> findLongestPath(boolean[][] board, Square start, Square finish) {
        if (isNull(start) || start.isOutOfBounds(board.length, board[0].length) || board[start.getX()][start.getY()]) {
            return emptyList();
        }
        List<Square> path = new ArrayList<>();
        path.add(start);
        board[start.getX()][start.getY()] = true;
        if (!start.equals(finish)) {
            path.addAll(Arrays.stream(Direction.values())
                    .map(start::moveInDirection)
                    .map(nextSquare -> findLongestPath(board, nextSquare, finish))
                    .max(comparingInt(List::size))
                    .orElse(emptyList()));
        }
        return path;
    }
}
