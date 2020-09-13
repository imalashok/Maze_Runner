package maze;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Maze {
    private String[][] maze;
    private int height;
    private int width;

    public Maze(int height, int width) {
        this.maze = PrimAlgorithm.generateMazeWithPrimAlgorithm(height, width);
        this.height = height;
        this.width = width;
    }

    public void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }

    public void printPath(boolean isMazeGeneratedManually) {
        String[][] mazeWithPath;

        if (isMazeGeneratedManually) {
            mazeWithPath = PrimAlgorithm.getMazeWithPath();
        } else {
            mazeWithPath = PrimAlgorithm.createMazeWithPath();
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(mazeWithPath[i][j]);
            }
            System.out.println();
        }
    }

    public boolean saveToFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(height + " " + width + "\n");

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.write(maze[i][j]);
                }
                writer.write("\n");
            }

            //write Entry/Exit nodes to the file
            writer.write(PrimAlgorithm.getDijkstraEntryNode() + " " + PrimAlgorithm.getDijkstraExitNode() + "\n");

            //write nodeToCoordinatesMap to the file
            for (var entry : PrimAlgorithm.getNodeToCoordinatesMap().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "|");
            }
            writer.write("\n");

            //write visitMap to the file
            for (var entry : PrimAlgorithm.getVisitMap().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "|");
            }
            writer.write("\n");

            if (PrimAlgorithm.getIsHorizontalEntranceExit()) {
                writer.write("1");
            } else {
                writer.write("0");
            }

        } catch (IOException e) {
            System.out.println("Cannot save the maze. It has an invalid format " + file);
            return false;
        }
        return true;
    }


    public boolean loadFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            String[] mazeSize = scanner.nextLine().split(" ");
            int tempHeight = Integer.parseInt(mazeSize[0]);
            int tempWeight = Integer.parseInt(mazeSize[1]);

            String[][] tempMaze = new String[tempHeight][tempWeight];

            for (int i = 0; i < tempHeight; i++) {
                String line = scanner.nextLine();
                int k = 0;
                for (int j = 0; j < tempWeight; j++) {
                    tempMaze[i][j] = line.charAt(k) + "" + line.charAt(k + 1);
                    k += 2;
                }
            }

            height = tempHeight;
            width = tempWeight;
            PrimAlgorithm.setMazeHeight(height);
            PrimAlgorithm.setMazeWidth(width);
            maze = tempMaze;
            PrimAlgorithm.setMaze(tempMaze);

            //load Entry/Exit nodes from file
            String[] entryExitNodes = scanner.nextLine().split(" ");
            PrimAlgorithm.setDijkstraEntryNode(Integer.parseInt(entryExitNodes[0]));
            PrimAlgorithm.setDijkstraExitNode(Integer.parseInt(entryExitNodes[1]));

            //load NodeToCoordinatesMap nodes from file
            String[] entriesMap1 = scanner.nextLine().split("\\|");
            Map<Integer, String> tempMap1 = new HashMap<>();

            for (String entry : entriesMap1) {
                String[] temp = entry.split("=");
                tempMap1.put(Integer.parseInt(temp[0]), temp[1]);
            }

            PrimAlgorithm.setNodeToCoordinatesMap(tempMap1);

            //load visitMap nodes from file
            String[] entriesMap2 = scanner.nextLine().split("\\|");
            Map<Integer, Integer> tempMap2 = new HashMap<>();

            for (String entry : entriesMap2) {
                String[] temp = entry.split("=");
                tempMap2.put(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
            }

            PrimAlgorithm.setVisitMap(tempMap2);

            int isHorizontalEntranceExit = Integer.parseInt(scanner.nextLine());

            PrimAlgorithm.setIsHorizontalEntranceExit(isHorizontalEntranceExit == 1);

        } catch (FileNotFoundException ex) {
            System.out.println("The file " + file + " does not exist");
            return false;
        } catch (IndexOutOfBoundsException | NoSuchElementException | NumberFormatException exc) {
            System.out.println("Cannot load the maze. It has an invalid format");
            return false;
        }
        return true;
    }
}
