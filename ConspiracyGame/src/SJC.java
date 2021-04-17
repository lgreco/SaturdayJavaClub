import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 * The Defenders of the Saturday Java Club
 */
public class SJC {

    public static final char DELIMITER = '#';
    public static final String OPEN_ENDED = "*";
    public static final String NO_MORE_QUESTIONS = "NO QUESTIONS AVAILABLE";

    /**
     * Trust score for current guest
     */
    public static double score = 0.0;

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
     * @param f         File object to use for obtaining a lineIndex from
     * @param lineIndex The position of the line that we want to obtain
     * @return The line at the requested position
     * @throws FileNotFoundException
     */
    public static String getLine(File f, int lineIndex) throws FileNotFoundException {
        Scanner fInput = new Scanner(f); // new scanner object places read pointer at top of file
        int lineCounter = 0; // WE ARE OFF BY ONE ... THE ETERNAL 0- VS 1-BASED COUNTING
        String requestedLine = "DATA NOT FOUND"; // default answer if something goes wrong
        if (lineIndex <= countLines(f)) { // make sure that we are not exceeding file length (in lines)
            while (fInput.hasNext() && lineCounter != lineIndex) { // keep reading until line found
                // in ... theory ... hasNext() above is redundant ... q for students: why?
                requestedLine = fInput.nextLine();
                lineCounter++;
            }
        }
        fInput.close();
        return requestedLine;
    } // method getLine

    public static boolean questionAvailable(int questionNumber) throws IOException {
        Scanner alreadyAsked = new Scanner(new File("alreadyAsked.txt"));
        boolean available = true;
        while (alreadyAsked.hasNext()) {
            int q = alreadyAsked.nextInt();
            if (q == questionNumber) {
                available = false;
            }
        }
        alreadyAsked.close();
        return available;
    } // method QuestionAvailable

    public static String getUnaskedQuestion(File questions) throws IOException {
        int N = countLines(questions); // how many questions are there?
        int randomQuestionNumber = 0;
        String question = NO_MORE_QUESTIONS;
        Random rng = new Random();
        // Was this question asked before?
        int safetyCounter = 0;
        while (!questionAvailable(randomQuestionNumber) && safetyCounter < 100*N ) {
            randomQuestionNumber = 1 + rng.nextInt(N);
            safetyCounter++;
        }
        System.out.printf("\nSafety counter %d\n", safetyCounter);
        if (safetyCounter < 100*N) {
            // randomQuestionNumber has not been asked before
            // Add it to the list of questions asked before
            FileWriter fw = new FileWriter("alreadyAsked.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(String.format("%d", randomQuestionNumber));
            bw.newLine();
            bw.close();
            // pull question
            question = getLine(questions, randomQuestionNumber);
        }
        return question;
    } // method getUnaskedQuestion

    public static void extractAnswers(String tag, File answers) throws IOException {
        Scanner a = new Scanner(answers);
        File temp = new File("temp.txt");
        if (temp.exists()) { // if temp file already exists from previous question
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
    } // method extractAnswers

    public static String getAnswer() throws IOException {
        String randomAnswer = "NO ANSWER AVAILABLE";
        File temp = new File("temp.txt");
        // There are N possible answers in the file
        int N = countLines(temp);
        System.out.printf("\n\t*** this question has %d answers ***\n",N);
        Random rng = new Random();
        int randomAnswerNumber = 1 + rng.nextInt(N);
        Scanner t = new Scanner(temp);
        int lineCounter = 0;
        while (t.hasNext() && lineCounter!=randomAnswerNumber) {
            randomAnswer = t.nextLine();
            lineCounter++;
        }
        return randomAnswer;
    } // method getAnswer

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
        askQuestionAtRandom(q, a);
    }
}
