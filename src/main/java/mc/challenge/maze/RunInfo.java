package mc.challenge.maze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Store data on runs for feedback.
 *
 * @param mazename        the type of the maze
 * @param rows            number of rows the maze has
 * @param cols            number of rows the maze has
 * @param moves           amount of moves spend to reach finish
 * @param tilesExplored   amount of tiles that have been explored in this run
 * @param totalFloorTiles total ammount of walkable tiles in this map
 * @param startPosition   row/column of the start position
 * @param finishPosition  row/column of the finish position
 * @param timeTakenMS     the time it took to complete the maze in milliseconds
 * @param challengeEntry  The name/version for the challenge implementation
 */
public record RunInfo(
        LocalDateTime currentDateTime,
        String mazename,
        int rows,
        int cols,
        int moves,
        int tilesExplored,
        int totalFloorTiles,
        Position startPosition,
        Position finishPosition,
        long timeTakenMS,
        String challengeEntry
) {

    static RunInfo fromString(String str) {
        if (!str.startsWith("RunInfo[")) throw new RuntimeException("bad record");
        if (!str.endsWith("]")) throw new RuntimeException("bad record");
        var sb = new StringBuilder(str.substring(str.indexOf("[") + 1, str.length() - 1));
        str = str.substring(str.indexOf("[") + 1, str.length() - 1);
        var split = str.split(", ");

        for (var a : split) System.out.println(a);
        System.out.println(str);

        return null;
    }

    public static void main(String[] args) throws IOException {
        var list = Files.lines(Path.of("data/runsv1")).collect(Collectors.toList());


        fromString(list.get(0));

    }
}
