package mc.challenge.maze;

import mc.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        str = str.substring(str.indexOf("[") + 1, str.length() - 1);
        var split = str.split(", ");
        var map = new HashMap<String, String>();
        for (var a : split) {
            var kv = a.split("=");
            map.put(kv[0], kv[1]);
        }
        return new RunInfo(
                LocalDateTime.parse(map.get("currentDateTime")),
                map.get("mazename"),
                Integer.parseInt(map.get("rows")),
                Integer.parseInt(map.get("cols")),
                Integer.parseInt(map.get("moves")),
                Integer.parseInt(map.get("tilesExplored")),
                Integer.parseInt(map.get("totalFloorTiles")),
                null,
                null,
                Integer.parseInt(map.get("timeTakenMS")),
                map.get("challengeEntry")

        );

    }

    static boolean printVerbose = false;

    public static void main(String[] args) throws IOException {
        doEmptyMazes();
        doScatteredMazes();
        doW1();
        doDungeon();
        doFlowinCaveMaze();

    }

    private static void doFlowinCaveMaze() throws IOException {
        System.out.println("\n\n- Start: FlowinCaveMaze");
        msPerMoveMaze(Configuration.MEDIUM, "FlowinCaveMaze");
        msPerMoveMaze(Configuration.LARGE, "FlowinCaveMaze");
        msPerMoveMaze(Configuration.HUGE, "FlowinCaveMaze");
    }

    private static void doDungeon() throws IOException {
        System.out.println("\n\n- Start: DungeonMaze");
        msPerMoveMaze(Configuration.SMALL, "DungeonMaze");
        msPerMoveMaze(Configuration.MEDIUM, "DungeonMaze");
        msPerMoveMaze(Configuration.LARGE, "DungeonMaze");
        msPerMoveMaze(Configuration.HUGE, "DungeonMaze");
    }

    private static void doW1() throws IOException {
        System.out.println("\n\n- Start: Width1Maze");
        msPerMoveMaze(Configuration.LARGE, "Width1Maze");
        msPerMoveMaze(Configuration.HUGE, "Width1Maze");
    }

    private static void doScatteredMazes() throws IOException {
        System.out.println("\n\n- Start: ScatterMazes");
        msPerMoveMaze(Configuration.MEDIUM, "ScatterMaze");
        msPerMoveMaze(Configuration.LARGE, "ScatterMaze");
        msPerMoveMaze(Configuration.HUGE, "ScatterMaze");
    }

    static void doEmptyMazes() throws IOException {
        System.out.println("\n\n- Start: EmptyMazes");
        msPerMoveMaze(Configuration.MEDIUM, "EmptyMaze");
        msPerMoveMaze(Configuration.LARGE, "EmptyMaze");
    }


    static void msPerMoveMaze(int size, String mazename) throws IOException {
        System.out.println("--- (ms / 1000moves) for " + mazename + " :  size: " + size);
        for (var e : getMap(mazename, size).entrySet()) {
//            System.out.println(e.getKey());
            if (printVerbose) {
                e.getValue().stream().sorted((a, b) -> (int) (a.timeTakenMS - b.timeTakenMS)).forEach(System.out::println);
            }
            var avg = e.getValue().stream().mapToInt(i -> (int) ((double) i.timeTakenMS() / ((double) i.moves() / 1000))).average().orElse(0);
            System.out.printf("%20s %.5f\n", e.getKey(), avg);
        }
    }


    static Map<String, List<RunInfo>> getMap(String type, int size) throws IOException {
        return Files.lines(Path.of("data/runsv1")).map(RunInfo::fromString).toList().stream()
                .filter(i -> i.mazename().equals(type))
                .filter(i -> i.rows() == size)
                .collect(Collectors.groupingBy(i -> i.challengeEntry));
    }

}
