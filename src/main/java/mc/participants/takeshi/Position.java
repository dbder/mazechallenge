package mc.participants.takeshi;

import mc.challenge.maze.Direction;
import mc.challenge.maze.Maze;

import java.io.Serializable;

public interface Position extends Serializable {

    int row();

    int col();

    static double square(double x) {
        return x * x;
    }

    default double distanceTo(Position another) {
        return Math.sqrt(square(this.col() - another.col()) + square(this.row() - another.row()));
    }

    default int stepDistance(Position another) {
        return Math.abs(this.col() - another.col()) + Math.abs(this.row() - another.row());
    }

    default Direction directionTo(Position target) {
//        if (this.col() == remove.col()) { // north or south
//            if (this.row() == remove.row() - 1) {
//                return Direction.NORTH;
//            } else if (this.row() == remove.row() + 1) {
//                return Direction.SOUTH;
//            }
//        } else if (this.row() == remove.row()) {
//            if (this.col() == remove.col() - 1) {
//                return Direction.EAST;
//            } else if (this.col() == remove.col() + 1) {
//                return Direction.WEST;
//            }
//        }
        for (var dir : Direction.values()) {
            var next = walk(dir);
            if (next.col() == target.col() && next.row() == target.row()) {
                return dir;
            }
        }
        throw new RuntimeException("cannot move from " + this + " to " + target);
    }

    interface PositionConstructor<E> {
        E build(int row, int col);
    }

    default Maze.CellType cellAt(Maze.CellType[][] maze) {
        return maze[this.row()][this.col()];
    }

    default boolean isWithin(Maze.CellType[][] maze) {
        return this.row() < maze.length && this.col() < maze[0].length;
    }

    default Position walk(Direction direction) {
        return walk(direction, (r, c) -> new Position() {
            @Override
            public int row() {
                return r;
            }

            @Override
            public int col() {
                return c;
            }
        });
    }

    default <E extends Position> E walk(Direction direction, PositionConstructor<E> constructor) {
        return switch (direction) {
            case NORTH -> constructor.build(this.row() + 1, this.col());
            case EAST -> constructor.build(this.row(), this.col() + 1);
            case SOUTH -> constructor.build(this.row() - 1, this.col());
            case WEST -> constructor.build(this.row(), this.col() - 1);
        };
    }
//    public LosPosition move(Direction direction) {
//        return switch (direction) {
//            case NORTH -> new LosPosition(this.row + 1, this.col);
//            case EAST -> new LosPosition(this.row, this.col + 1);
//            case SOUTH -> new LosPosition(this.row - 1, this.col);
//            case WEST -> new LosPosition(this.row, this.col - 1);
//        };
//    }


}
