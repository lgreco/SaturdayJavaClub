import java.util.Random;
import java.util.Scanner;

/**
 * The game engine.
 */
public class Conspiracy {

    // Some constants
    private static final String YES = "Y"; // for use with equals()
    private static final String NO = "N"; // for use with equals()
    private static final double INITIAL_FRIEND = 0.25; // the prob that an arriving guest is legit, is 25%
    public static final double FOE_THRESHOLD = 0.66; // when we can be certain that arriving guest is foe
    public static final int MIN_CHALLENGE_QUESTIONS = 4; // Min and max number of challenge questions ...
    public static final int MAX_CHALLENGE_QUESTIONS = 6; // ... the guard can ask an arriving guest.

    // Scanner object for the entire class
    private static Scanner keyboard = new Scanner(System.in);

    /**
     *
     * Driver method. As the code evolves, the plan is to take all textual content
     * and "encode" it in a file, where it can be read from.
     *
     */
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
                "infiltrators or a pure-at-heart, loyal member of the Club.\n\n" +
                "You. Are. The. Guard.\n");

        // Loop to start the game
        boolean appropriateResponse = false;
        while ( !appropriateResponse ) {
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
        Random r = new Random(); // local rng
        boolean keepAskingQuestions = true; // condition for interrogation loop
        int questionsAsked = 0; // counter fo questions asked
        // Determine how many questions to ask this guest.
        int numberOfQuestionsToAsk = MIN_CHALLENGE_QUESTIONS + r.nextInt(MAX_CHALLENGE_QUESTIONS-MIN_CHALLENGE_QUESTIONS);
        while ( keepAskingQuestions ) {
            // Pull a question from the questions pool
            // Pull an answer from the answers pool that somehow fits the question
            // Analyze the answer (look for _ and lots of spaces or extraneous ":" )
            // Update the confidence profile of the guest
            // If confidence falls below threshold, identify as FOE
            //   otherwise ask another question if possible
            // If all allowed questions are asked, and confidence profile ok, admit.
            questionsAsked++; // increment the questions counter
            // keepAskingQuestions = questionsAsked < numberOfQuestionsToAsk; // Can we ask one more question?
            if ( questionsAsked >= numberOfQuestionsToAsk ) {
                keepAskingQuestions = false;
            }
        }
    } // method playGame


    /**
     * Method to analyze a string and return a measure of its "Pythoness",
     * ie, how likely is it to have been written by a Python infiltrator.
     * @param s String to analyze
     * @return a value between 0 and 1., measuring the Pythoness of s
     */
    public static double measurePython(String s) {
        double pythoness = 0.0; //
        // check for extraneous spaces
        // check for underscores
        // check for :
        return pythoness;
    } // method measurePython
}
