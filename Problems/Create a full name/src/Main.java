// Don't delete scanner import
import java.util.Scanner;

class Name {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // use this program as a source of inspiration for your method

        for (int i = 0; i < 3; i++) {
            String firstName1 = scanner.next();
            String lastName1 = scanner.next();
            System.out.println(Name.createFullName(firstName1, lastName1));
        }
    }

    //implement your method here
    public static String createFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
}