import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class TestSJC {

  public static void testRandomReads() throws FileNotFoundException {
    File file = new File("questions.txt");
    int numberOfTests = 10;
    Random rng = new Random();
    int len = SJC.countLines(file);
    for (int i = 0; i < numberOfTests; i++) {
      int r = 1 + rng.nextInt(len);
      System.out.printf("\nTrial no. %2d: %s", i, SJC.getLine(file, r));
    }
  } // method testRandomReads

  public static void displayQA(File questions, File answers) throws FileNotFoundException {
    // For every question in the questions file:
    //   if the question is open ended:
    //     print the question, followed by the message "open ended"
    //   else:
    //     print the question, followed by all each answers, and each
    //       each answer is numbered 1,2,3,..., in sep lines
    //   fi
    Scanner a, q = new Scanner(questions);
    while (q.hasNext()) {
      String question = q.nextLine();
      int firstDelimiterPosition = question.indexOf(SJC.DELIMITER)-1;
      String tag = question.substring(0,firstDelimiterPosition);
      question = question.substring(question.indexOf(SJC.DELIMITER,firstDelimiterPosition) + 2); // +2 to account for space after delimiter
      System.out.printf("\n\n%s",question);

        int aCounter = 1;
        a = new Scanner(answers);
        while (a.hasNext()) {
          String answer = a.nextLine();
          /* Extract answer label */
          int delimiterIndex = answer.indexOf(SJC.DELIMITER);
          String label = answer.substring(0,delimiterIndex-1);
          answer = answer.substring(delimiterIndex+2); // +2 to account for space after delimiter
          /* Extract answer bias value */
          delimiterIndex = answer.indexOf(SJC.DELIMITER);
          String value = answer.substring(0,delimiterIndex-1);
          answer = answer.substring(delimiterIndex+2); // +2 to account for space after delimiter
          /* Extract answer content */
          delimiterIndex = answer.indexOf(SJC.DELIMITER);
          String content = answer.substring(delimiterIndex+1);
          /* Match answer to question */
          if (label.equals(tag)) {
            System.out.printf("\n\t( %d )  %s",aCounter,content);
            aCounter++;
          }
        }
        System.out.println();

    }
  } // method displayQA

  public static void main(String[] args) throws FileNotFoundException {
    testRandomReads();
    System.out.println("-".repeat(82));
    File q = new File("questions.txt");
    File a = new File("answers.txt");
    displayQA(q,a);
  } // method main

}
