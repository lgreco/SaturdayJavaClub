import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * The Defenders of the Saturday Java Club
 */
public class SJC {

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
    int lineCounter = 0;
    String requestedLine = "DATA NOT FOUND"; // default answer if something goes wrong
    if (lineIndex <= countLines(f)) { // make sure that we are not exceeding file length (in lines)
      while (fInput.hasNext() && lineCounter != lineIndex) { // keep reading until line found
        // in ... theory ... hasNext() above is redundant ... q for students: why?
        requestedLine = fInput.nextLine();
        lineCounter++;
      }
    }
    return requestedLine;
  } // method getLine

}
