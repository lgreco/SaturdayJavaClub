import java.util.ArrayList;
import java.util.Random;

/**
 *
 */

public class Question {

    /** The text of the question */
    String questionText;

    /** Question ID */
    String questionID;

    /** The answers to this question */
    ArrayList<Answer> answers;

    /**
     * Constructor for text and tag; it also initializes the arraylist for the
     * question's answers.
     *
     * @param questionText Text of the question
     */
    public Question(String questionText, String questionID) {
        this.questionText = questionText;
        this.questionID = questionID;
        answers = new ArrayList<>();
    } // constructor Question

    /**
     * Method to add an answer to the list of answers for this question.
     *
     * @param answer Text of the new answer
     * @return       true if addition successful. The method will not add a
     *               duplicate answer nor a null answer. In either case, the method
     *               will return false.
     */
    public boolean addAnswer(Answer answer) {
        boolean result = false;
        if (!answers.contains(answer) && answer!=null) {
            answers.add(answer);
            result = true;
        }
        return result;
    } // method addAnswer

    public void multiChoice() {
        String text = this.questionText; // obtain the test of the question
        text = String.format("\n\n\t%s\n",text);
        int answerCounter = 1;
        for (Answer a:this.answers) {
            text += String.format("\n\t\t(%d) %s",answerCounter,a.getAnswerText());
            answerCounter++;
        }
        System.out.println(text);
    } // method multiChoice

    public double presentSingleAnswer() {
        String text = this.questionText; // obtain the test of the question
        text = String.format("\n\n\t%s\n",text);
        int numberOfAnswers = this.answers.size();
        Random rng = new Random();
        int randomAnswerIndex = rng.nextInt(numberOfAnswers);
        Answer randomAnswer = this.answers.get(randomAnswerIndex);
        text += String.format("\n\t\t%s",randomAnswer.getAnswerText());
        text += String.format("\n\n\tWhat's the likelihood this is a Python infiltrator? ");
        System.out.print(text);
        return randomAnswer.getPythonness();
    }

    /**
     * Reports the contents of a question as a string that can be printed.
     *
     * @return  A formatted string with the contents of the question, including
     *          answers and their level of pythonness.
     */
    public String toString() {
        String output = String.format("\n%s\n\n%s\n","=".repeat(80),questionText);
        for (Answer a:answers) {
            output += String.format("\n\t(%4.2f) %s",a.getPythonness(), a.getAnswerText());
        }
        return output + String.format("\n");
    } // method toString
}
