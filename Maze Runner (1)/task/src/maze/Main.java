package maze;

import java.io.File;
import java.util.Scanner;

public class Main {
    private static boolean isMazeCreated = false;
    private static boolean isMazeGeneratedManually = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isSizeCorrect = false;
        int height = 0;
        int width = 0;
        Maze maze = null;

        while (true) {
            showMenu();

            switch (scanner.nextLine()) {
                case "1":
                    while (!isSizeCorrect) {
                        System.out.println("Please, enter the size of a maze");
                        try {
                            String[] mazeSize = scanner.nextLine().split(" ");
                            if (mazeSize.length == 2) {
                                height = Integer.parseInt(mazeSize[0]);
                                width = Integer.parseInt(mazeSize[1]);
                            } else {
                                height = Integer.parseInt(mazeSize[0]);
                                width = height;
                            }

                            if (height > 4 && width > 4) {
                                isSizeCorrect = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong size format. Input height and width of a maze. Both sizes should be bigger than 4.");
                        }
                    }
                    isSizeCorrect = false;

                    maze = new Maze(height, width);
                    isMazeCreated = true;
                    isMazeGeneratedManually = true;
                    maze.printMaze();

                    break;
                case "2":
                    boolean isMazeLoadedFromFile = false;
                    maze = new Maze(5, 5);
                    while (!isMazeLoadedFromFile) {
                        System.out.println("Enter path to the file and its name:");
                        File file = new File(scanner.nextLine());
                        isMazeLoadedFromFile = maze.loadFromFile(file);
                    }
                    isMazeCreated = true;
                    isMazeGeneratedManually = false;
                    break;
                case "3":
                    if (isMazeCreated && maze != null) {
                        boolean isMazeSavedToFile = false;
                        while (!isMazeSavedToFile) {
                            System.out.println("Enter path to the file and its name:");
                            File file = new File(scanner.nextLine());
                            isMazeSavedToFile = maze.saveToFile(file);
                        }
                    } else {
                        System.out.println("Incorrect option. Please try again");
                    }
                    break;
                case "4":
                    if (isMazeCreated && maze != null) {
                        maze.printMaze();
                    } else {
                        System.out.println("Incorrect option. Please try again");
                    }
                    break;
                case "5":
                    maze.printPath(isMazeGeneratedManually);
                    break;
                case "0":
                    System.out.println("Bye!");
                    System.exit(0);
                default:
                    System.out.println("Incorrect option. Please try again");
                    break;
            }
        }
    }

    public static void showMenu() {

        if (!isMazeCreated) {
            System.out.print("\n=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "0. Exit\n");
        } else {
            System.out.print("\n=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "3. Save the maze\n" +
                    "4. Display the maze\n" +
                    "5. Find the escape\n" +
                    "0. Exit\n");
        }
    }
}
