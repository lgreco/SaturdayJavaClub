import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

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

  public static void main(String[] args) throws FileNotFoundException {
    testRandomReads();
  } // method main

}
