package _2020OCT10;

public class Quadratic {

    int a, b, c; // a*x*x + b*x + c = 0
    int discriminant; // b*b - 4*a-c

    public Quadratic(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
        discriminant = b * b  - 4 * a * c;
    } // constructor Quadratic

    public boolean hasRealSolutions() {
        return discriminant >= 0.0;
    } // method hasRealSolutions

    public static void main(String[] args) {
        Quadratic equation1 = new Quadratic(1,0,-1);
        System.out.println(equation1.hasRealSolutions());
    }

}