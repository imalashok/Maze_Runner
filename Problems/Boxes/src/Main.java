import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Integer> box1 = new ArrayList<>();
        List<Integer> box2 = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            if (i < 3) {
                box1.add(scanner.nextInt());
            } else {
                box2.add(scanner.nextInt());
            }
        }

        Collections.sort(box1);
        Collections.sort(box2);

        int box1Dimensions = 0;
        int box2Dimensions = 0;

        for (int i = 0; i < 3; i++) {
            if (box1.get(i) < box2.get(i)) {
                box2Dimensions++;
            } else if (box1.get(i) > box2.get(i)) {
                box1Dimensions++;
            } else {
                System.out.println("Incompatible");
                return;
            }
        }

        if (box1Dimensions == 3) {
            System.out.println("Box 1 > Box 2");
        } else if (box2Dimensions == 3) {
            System.out.println("Box 1 < Box 2");
        } else {
            System.out.println("Incompatible");
        }
    }
}