package mc;

import mc.challenge.maze.HeadlessMain;
import mc.participants.deboder1.DeboChallenge1;

/**
 * Runs the mazes configured in {@link Configuration} headless
 *
 * Write the code in : {@link mc.renamebeforepr.ChallengeImpl}
 */
public class HeadlessLauncher {

    /**
     * Run all configured mazes.
     */
    public static void main(String[] args) {

        for (var maze : Configuration.MAZES) {
            new HeadlessMain(new DeboChallenge1(), maze.get()).doAllMoves();
        }
    }
}
