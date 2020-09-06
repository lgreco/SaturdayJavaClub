package _12SEP2020;

public class VariableArguments {
    static void foo (String... args) {
        for (String arg : args) {
            System.out.println(arg);
        }
    }

    public static void main(String[] args) {
        VariableArguments.foo("Hello");
        VariableArguments.foo("Hi", "there");
    }
}
