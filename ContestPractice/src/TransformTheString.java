import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Implementation for
 *
 *   Round H 2021 - (Google's) Kick Start 2021 - Transform the String
 *
 */
public class TransformTheString {

    /** Constant with size of alphabet */
    private static final int ALPHABET = 26;

    /** Conversion factor */
    private static final double NANOS_PER_SEC = 1_000_000_000.00;


    /**
     * Computes minimum distance between two letters. For example, the shortest distance between a and c is 2 and the
     * shortest distance between a and y is also 2.
     *
     * @param from char first letter
     * @param to char second letter
     * @return shortest distance between letters
     */
    static int minDistance(char from, char to) {
        // Assume that the distance we want is the absolute distance between the two letters
        int distance = Math.abs((int)from - (int)to);
        // If the distance is longer than half the alphabet, it's not the shortest.
        if (distance > ALPHABET/2)
            // Move in the other direction.
            distance = ALPHABET-distance;
        return distance;
    }  // method minDistance


    /**
     * Finds the minimum number of operations that are required such that each letter in string S after applying the
     * operations is present in string F.
     *
     * An operation is the change of a letter of the string to the one following it or preceding it in the alphabetical
     * order. For example: the letter c can change to either b or d in an operation. The letters can be considered in
     * a cyclic order, i.e., the preceding letter for letter a would be the letter z. Similarly, the following letter
     * for letter z would be the letter a.
     *
     * We count number of operations as the number of steps it takes to change a letter. This number is the distance
     * between the two letters.
     *
     * @param S String to operate on
     * @param F String with target letters
     * @return int with minimum number of operations for each letter in S to be one of F's letters.
     */
    static int minNumberOfOperations(String S, String F) {
        // Guard statement: if either strings are empty, nothing to do
        if (S.length() == 0 || F.length() == 0)
            return 0;
        // Declare variables to measure distance between letters
        int currentDistance, shortestDistance;
        // Declare char variables, one for letters in S, one for letters in F.
        char s, f;
        // Initialize operations counter
        int operationsCount = 0;
        // Grab the first letter of F -- safe since we checked against 0-length already
        char first = F.charAt(0);
        // For every letter in S, find which letter in F is at the shortest distance
        for (int i = 0; i < S.length(); i++) {
            s = S.charAt(i);
            // Assume that the shortest distance is between s and the first letter in F
            shortestDistance = minDistance(s, first);
            // Check the rest of the letters in F (loop starts at 1) to see if there is one closer to s
            for (int j = 1; j < F.length(); j++) {
                f = F.charAt(j);
                currentDistance = minDistance(s, f);
                // Simple smallest-of-two-values check
                if (currentDistance < shortestDistance)
                    shortestDistance = currentDistance;
            }
            // We found the shortest distance between s and a letter in F. Add it to the count
            operationsCount += shortestDistance;
        }
        return operationsCount;
    }  // minNumberOfOperations


    /**
     * Tests the code using Google-provided test cases and expected results.
     *
     * The test data are placed into two folders:
     *
     *          test_set_1
     *                      ts1_input.txt
     *                      ts1_output.txt
     *
     *          test_set_2
     *                      ts2_input.txt
     *                      ts2_output.txt
     *
     * The local paths (test_set1/... and test_set2/...) are hard-coded into the method below. As long as these folders
     * are within a working directory for the JVM, they can be accessed without a problem.
     *
     * This testing method reads a test case (comprising strings S and F) from the input file. Then it constructs
     * a string with the result of the minNumberOfOperations. It compares that string with the result provided in the
     * test output file. The two strings are expected to be the same, and if so the method updates the count of
     * successful tests.
     *
     * The number of test cases in each test set is given by the first line of the test input file.
     *
     * @throws FileNotFoundException in case there is a problem :-)
     */
    static void test() throws FileNotFoundException {
        // Constants needed for file operations
        final int TESTS = 2;
        final String TEST_FOLDER = "test_set_";
        final String TEST_INPUT_PREFIX = "ts";
        final String TEST_INPUT_SUFFIX = "_input.txt";
        final String TEST_OUTPUT_PREFIX = "ts";
        final String TEST_OUTPUT_SUFFIX = "_output.txt";
        // Strings for test files
        String testInput, testOutput;
        // Strings to read from files
        String S, F, googleResults;
        // Scanners for the test input file and the test output file
        Scanner testIn, testOut;
        // Count for successful tests
        int successfulTests;
        // Timing variables
        long start, finish;
        double timeInSeconds;
        // Loop over the number of test cases
        for (int test = 1; test <= TESTS; test++) {
            // Paths to input and output data files
            testInput =  String.format("%s%d/%s%d%s", TEST_FOLDER, test, TEST_INPUT_PREFIX, test, TEST_INPUT_SUFFIX);
            testOutput = String.format("%s%d/%s%d%s", TEST_FOLDER, test, TEST_OUTPUT_PREFIX, test, TEST_OUTPUT_SUFFIX);
            // Set up file points
            File testCases = new File(testInput);
            File testResults = new File(testOutput);
            // Connect the scanners
            testIn = new Scanner(testCases);
            testOut = new Scanner(testResults);
            // Read the number of test cases
            int numberOfCases = testIn.nextInt();
            // Initialize counter of successful tests
            successfulTests = 0;
            // Start stopwatch
            start = System.nanoTime();
            // Set up loop to read test csases
            for (int i = 1; i <= numberOfCases; i++) {
                S = testIn.next();  // String to rearrange
                F = testIn.next();  // String with target letters
                googleResults = testOut.nextLine();  // Expected results string
                // Build our result string by passing S, F to our minNumberOfOperations method
                String ourResults = String.format("Case #%d: %d", i, minNumberOfOperations(S, F));
                // Compare the two strings
                if (ourResults.equals(googleResults))
                    successfulTests++;  // yeah!
            }
            // Stopwatch update and convert nanoseconds to seconds
            finish = System.nanoTime();
            // To convert. we subtract the long values first, then cast them as double
            timeInSeconds = ((double)(finish -  start))/NANOS_PER_SEC;
            // Report findings and close the scanners
            System.out.printf("\nTest set %d\n\t%d cases\n\tYour score: %d/%d\n\t%12.10f sec\n",
                    test, numberOfCases, successfulTests, numberOfCases, timeInSeconds);
            testIn.close();
            testOut.close();
        }
    }  // method test


    /** Driver code */
    public static void main(String[] args) throws FileNotFoundException {
        test();
    }
}
