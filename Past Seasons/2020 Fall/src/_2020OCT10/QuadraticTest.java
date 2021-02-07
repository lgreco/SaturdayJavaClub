package _2020OCT10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuadraticTest {

    @Test
    public void testRealSolutions() {
        int N = 10;

        for (int a = -N; a < N; a++) {
            for (int b = -N; b < N; b++) {
                for (int c = -N; c < N; c++) {
                    int d = b*b - 4*a*c;
                    Quadratic testEquation = new Quadratic(a,b,c);
                    if ( d < 0 ) {
                        assertFalse(testEquation.hasRealSolutions());
                    } else {
                        assertTrue(testEquation.hasRealSolutions());
                    }
                }
            }
        }

    }

}