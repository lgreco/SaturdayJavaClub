import java.util.Scanner;

/**
 * The game engine.
 */
public class Conspiracy {

    // Some constants
    private static final String YES = "Y"; // for use with equals()
    private static final String NO = "N"; // for use with equals()
    private static final double INITIAL_FRIEND = 0.25 // assume an arriving guest is 25% a legit club member

    // Scanner object for the entire class
    private static Scanner keyboard = new Scanner(System.in);

    /** Driver method */
    public static void main(String[] args) {
        System.out.println("\n\n" +
                "The sinister overlords of the Friday Python Circle are\n" +
                "alarmed by the popularity of the Saturday Java Club.\n" +
                "They plan to infiltrate the Club and seed discord and doubt.\n" +
                "The Circle's tumultuous plan is good but not perfect. Circle\n" +
                "agitators can sometimes be recognized by the way they communicate:\n" +
                "it reeks of Python. Lots of empty spaces and overuse of underscores,\n" +
                "are some of the signs.\n" +
                "   The Saturday Java Club becomes aware of the Circle's malevolent\n" +
                "plans. In response, the Club posts a guard outside the place it meets.\n" +
                "The guard's job is to tell if somone arriving to the meeting is a Circle\n" +
                "inflitrator or a pure-at-heart, loyal member of the Club.\n\n" +
                "You. Are. The. Guard.\n");

        // Loop to start the game
        boolean appropriateResponse = false;
        while ( !appropriateResponse) {
            System.out.println("Are you ready to defend your Club?");
            System.out.println("[ Enter Y for yes, N for No (to end game) ]");
            String response = keyboard.next();
            if ( response.equalsIgnoreCase(YES) ) {
                appropriateResponse = true;
                playGame();
            } else if ( response.equalsIgnoreCase(NO) ) {
                appropriateResponse = true;
            }
        } // end while
    } // method main

    /**
     * The engine of the game.
     *
     * playGame runs a loop that produces an exchange between the guard and the
     * arriving guest to a Club meeting. The guard "analyzes" the guests responses
     * to build a profile: friend-or-foe. The guard can only ask a limited number
     * of questions before making a decision.
     */
    public static void playGame() {


    } // method playGame
}
