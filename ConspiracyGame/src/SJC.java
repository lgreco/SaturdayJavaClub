import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 * The Defenders of the Saturday Java Club
 */
public class SJC {

    /* ***********************************************
     * CLASS-WIDE VARIABLES AVAILABLE TO ALL METHODS *
     *************************************************/

    /**
     * Delimiter symbol for files
     */
    static final char DELIMITER = '#';
    /**
     * Default content when we're out of questions
     */
    static final String NO_MORE_QUESTIONS = "NO QUESTIONS AVAILABLE";

    /* Trust score for current guest */
    static double userScore = 0.0; // player-assessed risk
    static double machineScore = 0.0; // actual risk

    /**
     * Number of questions on file
     */
    static int numberOfQuestions;

    /**
     * Asked questions
     */
    static boolean[] questionAlreadyAsked;


    /* ***************
     *     METHOD    *
     * ***************/


    /**
     * Method that scans a file and counts the number of lines
     *
     * @param file file object to evaluate
     * @return number of lines in file in f
     * @throws FileNotFoundException Notes: the method does not return any specific values for file-not-found
     *                               situations or other errors. Missing files are handled by the thrown
     *                               exception.
     */
    public static int countLines(File file) throws FileNotFoundException {
        Scanner f = new Scanner(file);
        int lineCounter = 0;
        while (f.hasNext()) { // keep reading until the next of file
            f.nextLine(); // not interested in "saving" the contents we read
            lineCounter++;
        }
        f.close(); // It costs nothing to be nice to the system!
        return lineCounter;
    } // method countLines

    /**
     * Method to obtain the n-th lineIndex of a file. The method ensures that the requested
     * lineIndex does not exceed the length of the file (measured in lines).
     *
     * @param questions File object to use for obtaining the line at lineIndex
     * @param lineIndex The position of the line that we want to obtain
     * @return The line at the requested position
     * @throws FileNotFoundException
     */
    public static String getLine(File questions, int lineIndex) throws FileNotFoundException {
        Scanner q = new Scanner(questions); // new scanner object places read pointer at top of file
        String requestedLine = q.nextLine(); // start from the first line of the file
        if (lineIndex <= numberOfQuestions) { // make sure that we are not exceeding file length (in lines)
            /*
            Loop to keep reading the questions file, until the requested line is found. If the
            requested line is the file's first line (lineCounter=0), the loop will not run.
            That's ok, because requestedLine above has been initialized to the first line of the file.
            The hasNext() below is redundant; the loop is executed within the scope of the if
            statement that checks to make sure the requested lineIndex is less than the number
            of questions (ie, lines) in the file.
             */
            int lineCounter = 0;
            while (q.hasNext() && lineCounter != lineIndex) {
                requestedLine = q.nextLine();
                lineCounter++;
            }
        }
        q.close();
        return requestedLine;
    } // method getLine

    /**
     * Method to find a question that has been not asked before.
     *
     * @param questions File with questions
     * @return A string with an unanswered question
     * @throws IOException
     */
    public static String getUnaskedQuestion(File questions) throws IOException {
        String question = NO_MORE_QUESTIONS; // initialize to message that we are out of questions
        Random rng = new Random();
        /*
        Create a safety net about the number of attempts to find an unasked question. As
        more and more questions are asked, it may take longer to find one that has been not
        asked. (Extreme scenario, 99 of 100 questions asked, no rng has to find the last
        available question. If this takes too long, we should give up and essentially assume
        that we are out of questions. safetyNet is the number of attempts allowed at finding
        the next question. This protection is moot once we move to data structures implementation.
         */
        int safetyNet = 100 * numberOfQuestions;
        int randomQuestionNumber = rng.nextInt(numberOfQuestions);
        // Was this question asked before?
        int safetyCounter = 0;
        while (questionAlreadyAsked[randomQuestionNumber] && safetyCounter < safetyNet) {
            randomQuestionNumber = rng.nextInt(numberOfQuestions);
            safetyCounter++;
        }
        /*
        At this point either we have the index number for a fresh (not previously asked)
        question, or we have exceeded the safety net, i.e., we have tried enough times and
        have not found an unasked question. It is safe to assume that there aren't any
        such questions available.
         */
        if (safetyCounter < safetyNet) { // safety counter has not exceeded the safety net.
            // randomQuestionNumber has not been asked before
            // Add it to the list of questions asked before
            questionAlreadyAsked[randomQuestionNumber] = true;
            // pull question
            question = getLine(questions, randomQuestionNumber);
        }
        return question;
    } // method getUnaskedQuestion

    /**
     * Method to pull the answers for a given question to a separate file.
     *
     * @param tag     identifies which answers to pull out
     * @param answers file with answers to all questions
     * @throws IOException
     */
    public static void extractAnswers(String tag, File answers) throws IOException {
        File temp = new File("temp.txt"); // temp file to hold answers to question labeled by tag
        if (temp.exists()) { // if temp file already exists from previous question
            temp.delete(); // then delete it.
        }
        FileWriter fw = new FileWriter("temp.txt"); // new Filewriter for temp.txt
        BufferedWriter bw = new BufferedWriter(fw); // new Bufferedwriter for temp.txt
        Scanner a = new Scanner(answers); // Scanner for file with answers to all questions
        while (a.hasNext()) {
            String answer = a.nextLine(); // pull an answer
            String label = answer.substring(0, answer.indexOf(DELIMITER) - 1); // extract its label
            if (label.equals(tag)) { // if label matches question tag
                bw.write(answer); // write answer to temp file
                bw.newLine();
            }
        }
        bw.close();
        fw.close();
    } // method extractAnswers

    /**
     * Selects an answer, at random, from the temporary file that holds
     * all answers to a particular question. The answers are collected in
     * the file thanks to the extractAnswers() method.
     *
     * @return An answer from the tempo file
     * @throws IOException
     */
    public static String getAnswer() throws IOException {
        File temp = new File("temp.txt");
        // There are N possible answers in the file
        int N = countLines(temp);
        Random rng = new Random();
        int randomAnswerNumber = rng.nextInt(N);
        Scanner t = new Scanner(temp);
        String randomAnswer = t.nextLine(); // first answer in the file
        int lineCounter = 0;
        while (t.hasNext() && lineCounter != randomAnswerNumber) {
            randomAnswer = t.nextLine();
            lineCounter++;
        }
        t.close();
        return randomAnswer;
    } // method getAnswer

    /**
     * The method that controls the game.
     *
     * @param questions File with questions
     * @param answers   File with answers
     * @throws IOException
     */
    public static void playTheGame(File questions, File answers) throws IOException {
        numberOfQuestions = countLines(questions); // how many questions are there?
        questionAlreadyAsked = new boolean[numberOfQuestions]; // array to track asked questions
        // reset scores
        userScore = 0.0;
        machineScore = 0.0;
        // Announce arrival of guest
        System.out.println("\nA guest is approaching ...");
        // Decide how many questions to ask? (3-5)
        Random rng = new Random();
        int howManyQuestions = 3 + rng.nextInt(3); // M + rng.nextInt(N)
        System.out.printf("\nYou will be asking the arriving guest %d questions.\n", howManyQuestions);
        Scanner s = new Scanner(System.in); // keyboard input
        // Loop of the number of decided questions
        int questionsCounter = 0;
        while (questionsCounter < howManyQuestions) {
            // Pull a question at random
            String question = getUnaskedQuestion(questions);
            // separate question from its tag
            int delimiterAt = question.indexOf(DELIMITER);
            String tag = question.substring(0, delimiterAt - 1);
            question = question.substring(delimiterAt + 2); // +2 because we leave a space after the delimiter
            // extract an answer at random
            extractAnswers(tag, answers);
            String answer = getAnswer();
            /*
            Extract prob that guest is infiltrator based on this answer. Answers are
            formatted as
            AAAA # x.y # Aaaaa....
            The probability is shown between the two delimiters, so first we grab thei
            delimiters' positions. Next we extract the prob as a substring to the right
            of the first delimiter and to the left of the second.
             */
            int firstDelimiter = answer.indexOf(DELIMITER); // we need +2 below, to account for space after delimiter
            int secondDelimiter = answer.indexOf(DELIMITER,firstDelimiter+1); // we need -1 below to account for space before delimiter
            double probabilityInfiltrator = Double.valueOf(answer.substring(firstDelimiter+2, secondDelimiter-1));
            answer = answer.substring(secondDelimiter+2); // +2 to accommodate for space after delimiter
            // ask the question and show the answer
            System.out.printf("\nYou are asking the guest: %s.\nThe guest responds: [%s]", question, answer);
            // ask user to determine if answer legit or not
            System.out.printf("\nIf you think answer is suspicious enter 1; otherwise 0: ");
            int evaluation = s.nextInt();
            System.out.println();
            //   update user score
            userScore += evaluation;
            machineScore += probabilityInfiltrator;
            //   update machine score
            questionsCounter++;
        }
        // Show user score v. machine score
        double userEvaluation = userScore / ((double) howManyQuestions);
        double machineEvaluation = machineScore / ((double) howManyQuestions);
        System.out.printf("\nInterview of the guest is completed.");
        System.out.printf("\nYour evaluation is: %7.4f", userEvaluation);
        System.out.printf("\nThe actual score is: %7.4f", machineEvaluation);
        if (machineEvaluation > 0.5) {
            if (userEvaluation < 0.5) {
                System.out.printf("\nBased on your evaluation, a Python infiltrator would have been allowed in.");
            } else {
                System.out.printf("\nYou just prevented a Python infiltrator from entering the SJC! Good job.");
            }
        } else if (machineEvaluation <= 0.5) {
            if (userEvaluation > 0.5) {
                System.out.printf("\nBased on your evaluation, a legitimate guest would have been denied entry.");
            } else {
                System.out.printf("\nYou just admitted a legitimate guest to the club!. Good job.");
            }
        }
        System.out.println();
        s.close(); // need to close scanner to release control of File temp so that we can delete it.
    } // method playTheGame


    public static void main(String[] args) throws IOException {
        File q = new File("questions.txt");
        File a = new File("answers.txt");
        //askQuestionAtRandom(q, a);
        playTheGame(q, a);
    } // method main
}
