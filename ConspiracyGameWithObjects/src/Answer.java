/**
 *
 */
public class Answer {

    /** The text of the answer */
    private String answerText;
    /** The pythoness of an answer */
    private double pythoness;

    /**
     * Constructor with answer text and pythoness of the answer
     *
     * @param answerText  The text of the answer
     * @param pythoness   double, with the pythoness of an answer; the range of
     *                    pythoness is from 0 to 1. Zero means the answer has no
     *                    trace of python-ness. One means the answer is a full of
     *                    Python-background.
     */
    public Answer(String answerText, double pythoness) {
        this.answerText = answerText;
        this.pythoness = pythoness;
    } // constructor Answer

    /**
     * Accessor for the text of an answer
     *
     * @return String with the text of an answer
     */
    public String getAnswerText() {
        return answerText;
    } // accessor getAnswerText

    /**
     * Accessor for the pythoness of an answer
     *
     * @return double with the value of the answer's pythoness
     */
    public double getPythonness() {
        return pythoness;
    } // accessor getPythonness
}
