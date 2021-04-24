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

    static final char DELIMITER = '#';
    static final String NO_MORE_QUESTIONS = "NO QUESTIONS AVAILABLE";

    /* Trust score for current guest */
    static double userScore = 0.0;
    static double machineScore = 0.0;

    /** Number of questions on file */
    static int numberOfQuestions;

    /** Asked questions */
    static boolean[] questionAlreadyAsked;

    /**
     * Method that scans a file and counts the number of lines
     *
     * @param f file object to evaluate
     * @return number of lines in file in f
     * @throws FileNotFoundException Notes: the method does not return any specific values for file-not-found
     *                               situations or other errors. Missing files are handled by the thrown
     *                               exception.
     */
    public static int countLines(File f) throws FileNotFoundException {
        Scanner fInput = new Scanner(f);
        int lineCounter = 0;
        while (fInput.hasNext()) { // keep reading until the next of file
            fInput.nextLine(); // not interested in "saving" the contents we read
            lineCounter++;
        }
        fInput.close(); // It costs nothing to be nice to the system!
        return lineCounter;
    } // method countLines

    /**
     * Method to obtain the n-th lineIndex of a file. The method ensures that the requested
     * lineIndex does not exceed the length of the file (measured in lines).
     *
     * @param questions         File object to use for obtaining a lineIndex from
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

    public static boolean questionAsked(int questionNumber) throws IOException {
        return questionAlreadyAsked[questionNumber];
    } // method QuestionAvailable

    public static String getUnaskedQuestion(File questions) throws IOException {
        int randomQuestionNumber = 0;
        String question = NO_MORE_QUESTIONS;
        Random rng = new Random();
        int safetyNet = 100*numberOfQuestions;
        // Was this question asked before?
        int safetyCounter = 0;
        while (questionAsked(randomQuestionNumber) && safetyCounter < safetyNet ) {
            randomQuestionNumber = 1 + rng.nextInt(numberOfQuestions);
            safetyCounter++;
        }
        //System.out.printf("\nSafety counter %d\n", safetyCounter);
        if (safetyCounter < safetyNet) {
            // randomQuestionNumber has not been asked before
            // Add it to the list of questions asked before
            questionAlreadyAsked[randomQuestionNumber] = true;
            // pull question
            question = getLine(questions, randomQuestionNumber);
        }
        return question;
    } // method getUnaskedQuestion

    public static void extractAnswers(String tag, File answers) throws IOException {
        Scanner a = new Scanner(answers);
        File temp = new File("temp.txt");
        if (temp.exists()) { // if temp file already exists from previous question
            System.out.println("*** temp file exists ... deleting it! ***");
            temp.delete(); // then delete it.
        }
        FileWriter fw = new FileWriter("temp.txt", true); // new Filewriter for temp.txt
        BufferedWriter bw = new BufferedWriter(fw); // new Bufferedwriter for temp.txt
        while (a.hasNext()) {
            String answer = a.nextLine();
            String label = answer.substring(0, answer.indexOf(DELIMITER) - 1);
            if (label.equals(tag)) {
                bw.write(answer);
                bw.newLine();
            }
        }
        bw.close();
        fw.close();
    } // method extractAnswers

    public static String getAnswer() throws IOException {
        String randomAnswer = "NO ANSWER AVAILABLE";
        File temp = new File("temp.txt");
        // There are N possible answers in the file
        int N = countLines(temp);
        //System.out.printf("\n\t*** this question has %d answers ***\n",N);
        Random rng = new Random();
        int randomAnswerNumber = 1 + rng.nextInt(N);
        Scanner t = new Scanner(temp);
        int lineCounter = 0;
        while (t.hasNext() && lineCounter!=randomAnswerNumber) {
            randomAnswer = t.nextLine();
            lineCounter++;
        }
        t.close();
        return randomAnswer;
    } // method getAnswer

    public static void playTheGame(File questions, File answers) throws IOException {
        numberOfQuestions = countLines(questions); // how many questions are there?
        questionAlreadyAsked = new boolean[numberOfQuestions];
        // reset scores
        userScore = 0;
        machineScore = 0;
        Scanner s = new Scanner(System.in);
        // Announce arrival of guest
        System.out.println("\nA guest is approaching ...");
        // Decide how many questions to ask? (3-5)
        Random rng = new Random();
        int howManyQuestions  = 3 + rng.nextInt(3); // M + rng.nextInt(N)
        System.out.printf("\nYou will be asking the arriving guest %d questions.\n", howManyQuestions);
        // Loop of the number of decided questions
        int questionsCounter = 0;
        while (questionsCounter < howManyQuestions) {
            //   Pull a question at random
            String question = getUnaskedQuestion(questions);
            int delimiterAt = question.indexOf(DELIMITER);
            String tag = question.substring(0,delimiterAt-1);
            question = question.substring(delimiterAt+1+1); // justify this, why 1+1
            //   extract an answer at random
            extractAnswers(tag,answers);
            String answer = getAnswer();
           double answerTrustLevel = Double.valueOf(answer.substring(7,10));
           answer = answer.substring(13);
            //   ask the question and show the answer
            System.out.printf("\nYou are asking the guest: %s.\nThe guest responds: %s",question,answer);
            //   ask user to determine if answer legit or not
            System.out.printf("\nIf you think answer is suspicious enter 1; otherwise 0: ");
            int evaluation = s.nextInt();
            System.out.println();
            //   update user score
            userScore += evaluation;
            machineScore += answerTrustLevel;
            //   update machine score
            questionsCounter++;
        }
        // Show user score v. machine score
        double userEvaluation = userScore / ((double) howManyQuestions);
        double machineEvaluation = machineScore / ((double) howManyQuestions);
        System.out.printf("\nInterview of the guest is completed.");
        System.out.printf("\nYour evaluation is: %7.4f",userEvaluation);
        System.out.printf("\nThe actual score is: %7.4f",machineEvaluation);
        if ( machineEvaluation > 0.5) {
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

    public static void askQuestionAtRandom(File questions, File answers) throws IOException {
        String question = getUnaskedQuestion(questions);
        if ( question.equals(NO_MORE_QUESTIONS)) {
            System.out.printf("\n\n*****************************\n*    G A M E     O V E R    *\n*****************************\n");
        } else {
            // get the question's ID tag
            System.out.printf("\n QUESTiON: [%S]\n",question);
            String tag = question.substring(0, question.indexOf(DELIMITER) - 1);
            // pull this question's answers to a separate file
            extractAnswers(tag, answers);
            // present question and choices to user:
            System.out.printf("\n\nYou are asking the guest the following question:\n%s", question);
            String randomAnswer = getAnswer();
            System.out.printf("\n\nThe guest answers: %s", randomAnswer);
        }
    } // method askQuestionAtRandom

    public static void main(String[] args) throws IOException {
        File q = new File("questions.txt");
        File a = new File("answers.txt");
        //askQuestionAtRandom(q, a);
        playTheGame(q,a);
    }
}
