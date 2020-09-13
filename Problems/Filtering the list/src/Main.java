import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Integer> numbers = new ArrayList<>();
        int i = 0;

        while (scanner.hasNextInt()) {
            if (i % 2 != 0) {
                numbers.add(scanner.nextInt());
            } else {
                scanner.nextInt();
            }
            i++;
        }

        for (int j = numbers.size() - 1; j >= 0; j--) {
            System.out.print(numbers.get(j) + " ");
        }
    }
}