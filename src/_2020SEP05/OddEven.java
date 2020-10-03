package _2020SEP05;

public class OddEven {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {

            // System.out.print(i + " is " + ((i % 2 == 0) ? "Even" : "Odd"));

            if ( i%2 == 0 ) {
                System.out.println("Even");
            } else {
                System.out.println("Odd");
            }


            System.out.println(" \t... indeed it is " +  ((i & 1) == 0 ? "Even" : "Odd") );

        }
        System.out.println();
    }
}
