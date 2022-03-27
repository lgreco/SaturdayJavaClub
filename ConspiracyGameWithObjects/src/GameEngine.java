import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameEngine {

    /**
     * The principal data structure for the game. It holds all the question objects.
     */
    ArrayList<Question> Questions;

    private static final char DELIMITER = '#';
    private static final String ENHANCED_DELIMITER = " #";

    /**
     * Method to setup the game. It reads the files with questions and answers
     * and builds the necessary data structures to host them. The method
     * creates an arraylist of Question objects and populates it based on
     * data found in the questions and answers files.
     */
    public void setup(String fileWithQuestions, String fileWithAnswers) {
        File questions = new File(fileWithQuestions); // where are the questions?
        File answers = new File(fileWithAnswers); // where are the answers?
        Questions = new ArrayList<>(); // Principal data structure for the game
        Scanner q = null;  // Setup a scanner connection to questions
        try {
            q = new Scanner(questions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Scanner established; let's go through the questions file
        while (q.hasNext()) {
            /*
            Read one line at a time. Parse the line into two parts: the question ID
            and the question text. Parsing is done based on the delimiter character
            and a space character we add to make the files more readable. For example
            the question with ID "DDDD" and text "Blah blah blah ...". has an ID that is
            the substring(0,X) where X = indexOf(ENHANCED_DELIMITER)

                +----------------- ENHANCED_DELIMITER starts here
                |
                v
            DDDD # Blah blah blah ...
                   ^
                   |
                   +---------------------- Question text is 3 positions to the right
                                           of start of ENHANCED_DELIMITER. Here
                                           3 = ENHANCED_DELIMITER.length() + 1
                                           The +1 is for the space that follows the
                                           ENHANCED_DELIMITER. This arithmetic can
                                           be simplified if we made
                                           ENHANCED_DELIMITER = " # " instead of " #"
             */
            String questionText = q.nextLine();
            String questionID = questionText.substring(0, questionText.indexOf(ENHANCED_DELIMITER));
            questionText = questionText.substring(questionText.indexOf(ENHANCED_DELIMITER) + 3);
            /* Create new question object */
            Question newQuestion = new Question(questionText, questionID);
            /* Set up a scanner to the answers file */
            Scanner a = null;
            try {
                a = new Scanner(answers);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            /* Scanner to answers established */
            while (a.hasNext()) {
                String answerText = a.nextLine(); // read answers, one line at a time
                /*
                Every line in this file has two delimiters, organizing it as follows:

                    +------------------------------ First enhanced delimiter
                    |                               starts here
                    |
                    |     +------------------------ Second enhanced delimiter
                    |     |                         starts here
                    V     V
                DDDD # a.b # Blah blah ...
                ^      ^     ^
                |      |     |
                |      |     +--------------------- Answer text starts +3 after
                |      |                            2nd enhanced delimiter
                |      |
                |      +--------------------------- Answer "pythoness" starts +3
                |                                   after 1st enhanced delimiter
                |
                +---------------------------------- ID tag matching an answer to
                                                    a question, starts at 0.

                Like before, the +3 is the length of the enhanced delimiter plus
                the one more space following it.
                 */
                int delBefore = answerText.indexOf(ENHANCED_DELIMITER);
                int delAfter = answerText.indexOf(ENHANCED_DELIMITER,delBefore+1);
                String answerID = answerText.substring(0,delBefore);
                double pythoness = Double.valueOf(answerText.substring(delBefore+3,delAfter));
                answerText = answerText.substring(delAfter+3);
                /* If the answerID matches the ID of the question currently under consideration */
                if (answerID.equals(questionID)) {
                    Answer newAnswer = new Answer(answerText,pythoness); // create Answer object
                    newQuestion.addAnswer(newAnswer); // Add to the answers list in the question object
                }
            }
            Questions.add(newQuestion); // Add the new question object to the principal data structure
        }
    } // method setup

    /**
     * Wrapper for setup(String, String) with default filenames.
     */
    public void setup() {
        setup("questions.txt", "answers.txt");
    } // method setup

    /**
     *
     * @param M How many questions to ask
     */
    public void play(int M) {
        double knownScore = 0.0; // Initialize known score sum
        double guessScore = 0.0; // Initialize guessed score sum
        int N = Questions.size(); // how many questions are available

        Scanner keyboard = new Scanner(System.in);
        if ( M < N ) { // make sure we ask fewer question than available
            ArrayList<Question> temporaryQuestions = Questions; // copy questions to temporary list
            int questionsCounter = 0; // initialize question counter
            Random rng = new Random(); // set up rng
            while (questionsCounter < M && N > 0) {
                int randomQuestionIndex = rng.nextInt(N); // select a question at random
                Question askThisQuestion = temporaryQuestions.remove(randomQuestionIndex); // remove the question from temp list
                double p = askThisQuestion.presentSingleAnswer();
                // receive answer
                double u = keyboard.nextDouble();
                // update scores
                knownScore += p;
                guessScore += u;
                questionsCounter++; // update questions counter
                N = temporaryQuestions.size(); // update size of temporary list
            }
            reportGameResults(knownScore,guessScore,M);
        }
    } // method play

    /**
     * Wrapper method for play with default value 5
     */
    public void play() {
        play(5);
    } // method play


    public void reportGameResults(double known, double guess, int M) {
        double m = (double) M;
        known = known/m;
        guess = guess/m;
        int len = 80;
        String finalGameReport = "*".repeat(len);
        int padding = (80-finalGameReport.length())/2;

        finalGameReport += String.format("\n%sYour guess: %4.2f -- Actual risk: %4.2f\n"," ".repeat(padding),guess,known);
        System.out.println(finalGameReport);    }

    /**
     * Helper method that displays the contents of the Question objects collected
     * in arraylist Questions.
     */
    public void report() {
        for (Question q:Questions) {
            System.out.println(q.toString());
        }
    } // method report

    public static void main(String[] args) {
        GameEngine play = new GameEngine();
        play.setup();
        play.play();
    }
}
