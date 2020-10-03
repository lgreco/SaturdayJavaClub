package _2020SEP26;

public class Person {

    private String firstName;       // required
    private String lastName;        // required

    private char gender;            // optional
    private String birthday;        // optional
    private String email;           // optional
    private String mobileNumber;    // optional

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public static void main(String[] args) {
        Person paulina = new Person("Pauleena", "Kucharska");
        paulina.setEmail("paulina@luc.edu");

        /*

        Person paulina = PersonBuilder("Pauleena", "Kucharska").addEmail("paulina@luc.edu")

         */

    }
}
