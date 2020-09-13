// Don't delete this import statement
import java.util.Scanner;

class SimpleCalculator {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        long num1 = scanner.nextLong();
        String operator = scanner.next();
        long num2 = scanner.nextLong();

        // Change switch statement so it calls implemented methods
        switch (operator) {
            case "+":
                System.out.println(sumTwoNumbers(num1, num2));
                break;

            case "-":
                System.out.println(subtractTwoNumbers(num1, num2));
                break;

            case "/":
                System.out.println(divideTwoNumbers(num1, num2));
                break;

            case "*":
                System.out.println(multiplyTwoNumbers(num1, num2));
                break;

            default:
                break;
        }
    }

    //Implement your methods here
    public static long subtractTwoNumbers(long a, long b) {
        return a - b;
    }


    public static long sumTwoNumbers(long a, long b) {
        return a + b;
    }


    public static long divideTwoNumbers(long a, long b) {
        if (b == 0) {
            return a;
        }
        return a / b;
    }


    public static long multiplyTwoNumbers(long a, long b) {
        return a * b;
    }
}