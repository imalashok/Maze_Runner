package maze;

import java.util.*;

public class PrimAlgorithm {
    private static final Random random = new Random();
    private static final List<String> minimumSpanningTree = new ArrayList<>();
    private static final List<String> nodesInTree = new ArrayList<>();
    private static List<Integer> visitedNodes = new ArrayList<>();
    private static final List<Integer> unvisitedNodes = new ArrayList<>();
    private static List<Integer> unvisitedNodesTemp = new ArrayList<>();
    private static Map<Integer, Integer> distanceMap = new HashMap<>(); //to store distance values between nodes
    private static Map<Integer, Integer> visitMap = new HashMap<>(); //to store the path between the nodes
    private static Map<Integer, String> nodeToCoordinatesMap = new HashMap<>();
//    private static String dijkstraEntryNodeCoordinates;
    private static int dijkstraEntryNode;
//    private static String dijkstraExitNodeCoordinates;
    private static int dijkstraExitNode;
    private static int[][] nodeMatrix;
    private static int[][] weightsMatrix;
    private static int weightsMatrixSize = 0;
    private static int primHeight = 0;
    private static int primWidth = 0;
    private static int mazeHeight = 0;
    private static int mazeWidth = 0;
    private static String[][] maze;
    private static String[][] mazeWithPath;
    private static boolean isHorizontalEntranceExit;


    public static String[][] generateMazeWithPrimAlgorithm(int height, int width) {
        unvisitedNodes.clear();
        minimumSpanningTree.clear();
        nodesInTree.clear();
        nodeToCoordinatesMap.clear();

        primHeight = height % 2 == 0 ? (height / 2) - 1 : height / 2;
        primWidth = width % 2 == 0 ? (width / 2) - 1 : width / 2;


        weightsMatrixSize = primHeight * primWidth;
        mazeHeight = height;
        mazeWidth = width;
        maze = new String[height][width];

        nodeMatrix = new int[height][width];
        weightsMatrix = new int[weightsMatrixSize][weightsMatrixSize];
        int node = 0;

        for (int i = 0; i < primHeight; i++) {
            for (int j = 0; j < primWidth; j++) {
                nodeMatrix[i][j] = node;

                unvisitedNodes.add(node);
                nodeToCoordinatesMap.put(node, i + " " + j);

//                System.out.print(nodeMatrix[i][j] + " ");
                node++;
            }
//            System.out.println();
        }

        for (int i = 0; i < weightsMatrixSize; i++) {
            for (int j = 0; j < weightsMatrixSize; j++) {
                weightsMatrix[i][j] = 0;
            }
        }

        for (int i = 0; i < primHeight; i++) {
            for (int j = 0; j < primWidth; j++) {
                if (i < primHeight - 1 && j < primWidth - 1) {
                    weightsMatrix[nodeMatrix[i][j]][nodeMatrix[i][j + 1]] = random.nextInt(9) + 1;
                    weightsMatrix[nodeMatrix[i][j + 1]][nodeMatrix[i][j]] = weightsMatrix[nodeMatrix[i][j]][nodeMatrix[i][j + 1]];

                    weightsMatrix[nodeMatrix[i][j]][nodeMatrix[i + 1][j]] = random.nextInt(9) + 1;
                    weightsMatrix[nodeMatrix[i + 1][j]][nodeMatrix[i][j]] = weightsMatrix[nodeMatrix[i][j]][nodeMatrix[i + 1][j]];

                } else if (j == primWidth - 1 && i != primHeight - 1) {
                    weightsMatrix[nodeMatrix[i][j]][nodeMatrix[i + 1][j]] = random.nextInt(9) + 1;
                    weightsMatrix[nodeMatrix[i + 1][j]][nodeMatrix[i][j]] = weightsMatrix[nodeMatrix[i][j]][nodeMatrix[i + 1][j]];
                } else if (j != primWidth - 1 && i == primHeight - 1) {
                    weightsMatrix[nodeMatrix[i][j]][nodeMatrix[i][j + 1]] = random.nextInt(9) + 1;
                    weightsMatrix[nodeMatrix[i][j + 1]][nodeMatrix[i][j]] = weightsMatrix[nodeMatrix[i][j]][nodeMatrix[i][j + 1]];
                }
            }
        }


//        for debug only - prints weights matrix
//        for (int i = 0; i < weightsMatrixSize; i++) {
//            for (int j = 0; j < weightsMatrixSize; j++) {
//                System.out.print(weightsMatrix[i][j] + " ");
//            }
//            System.out.println();
//        }

        int rootNodeHeight = random.nextInt(primHeight);
        int rootNodeWidth = random.nextInt(primWidth);
        nodesInTree.add(rootNodeHeight + " " + rootNodeWidth);

//        for debug only - prints the root node where findMinimumSpanningTree algorithm starts from
//        System.out.println(rootNodeHeight + " " + rootNodeWidth);

        findMinimumSpanningTree(nodesInTree);
        createMaze();
        createEntranceExitInMaze();

        mazeWithPath = PrimAlgorithm.runDijkstraAlgorithm();

        return maze;
    }

    public static List<String> findMinimumSpanningTree(List<String> nodesInTree) {
        if (nodesInTree.size() == weightsMatrixSize) {
            return nodesInTree;
        } else {
            int minWeight = 100;
            int nextNodeHeight = 0;
            int nextNodeWidth = 0;
            String pairNode = "";

            for (String nodes : nodesInTree) {
                String[] coordinates = nodes.split(" ");
                int coordinateHeight = Integer.parseInt(coordinates[0]);
                int coordinateWidth = Integer.parseInt(coordinates[1]);

                if (coordinateHeight == 0) {
                    if (coordinateWidth < primWidth - 1) {
                        if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth + 1]] < minWeight && !nodesInTree.contains(coordinateHeight + " " + (coordinateWidth + 1))) {
                            minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth + 1]];
                            nextNodeHeight = coordinateHeight;
                            nextNodeWidth = coordinateWidth + 1;
                            pairNode = nodes;
                        }
                    }

                    if (coordinateWidth > 0) {
                        if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth - 1]] < minWeight && !nodesInTree.contains(coordinateHeight + " " + (coordinateWidth - 1))) {
                            minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth - 1]];
                            nextNodeHeight = coordinateHeight;
                            nextNodeWidth = coordinateWidth - 1;
                            pairNode = nodes;
                        }
                    }

                    if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight + 1][coordinateWidth]] < minWeight && !nodesInTree.contains((coordinateHeight + 1) + " " + coordinateWidth)) {
                        minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight + 1][coordinateWidth]];
                        nextNodeHeight = coordinateHeight + 1;
                        nextNodeWidth = coordinateWidth;
                        pairNode = nodes;
                    }
                }

                if (coordinateHeight == primHeight - 1) {
                    if (coordinateWidth > 0) {
                        if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth - 1]] < minWeight && !nodesInTree.contains(coordinateHeight + " " + (coordinateWidth - 1))) {
                            minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth - 1]];
                            nextNodeHeight = coordinateHeight;
                            nextNodeWidth = coordinateWidth - 1;
                            pairNode = nodes;
                        }
                    }

                    if (coordinateWidth < primWidth - 1) {
                        if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth + 1]] < minWeight && !nodesInTree.contains(coordinateHeight + " " + (coordinateWidth + 1))) {
                            minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth + 1]];
                            nextNodeHeight = coordinateHeight;
                            nextNodeWidth = coordinateWidth + 1;
                            pairNode = nodes;
                        }
                    }

                    if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight - 1][coordinateWidth]] < minWeight && !nodesInTree.contains((coordinateHeight - 1) + " " + coordinateWidth)) {
                        minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight - 1][coordinateWidth]];
                        nextNodeHeight = coordinateHeight - 1;
                        nextNodeWidth = coordinateWidth;
                        pairNode = nodes;
                    }
                }


                if (coordinateWidth == 0) {
                    if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth + 1]] < minWeight && !nodesInTree.contains(coordinateHeight + " " + (coordinateWidth + 1))) {
                        minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth + 1]];
                        nextNodeHeight = coordinateHeight;
                        nextNodeWidth = coordinateWidth + 1;
                        pairNode = nodes;
                    }

                    if (coordinateHeight > 0) {
                        if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight - 1][coordinateWidth]] < minWeight && !nodesInTree.contains((coordinateHeight - 1) + " " + coordinateWidth)) {
                            minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight - 1][coordinateWidth]];
                            nextNodeHeight = coordinateHeight - 1;
                            nextNodeWidth = coordinateWidth;
                            pairNode = nodes;
                        }
                    }

                    if (coordinateHeight < primHeight - 1) {
                        if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight + 1][coordinateWidth]] < minWeight && !nodesInTree.contains((coordinateHeight + 1) + " " + coordinateWidth)) {
                            minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight + 1][coordinateWidth]];
                            nextNodeHeight = coordinateHeight + 1;
                            nextNodeWidth = coordinateWidth;
                            pairNode = nodes;
                        }
                    }
                }


                if (coordinateWidth == primWidth - 1) {
                    if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth - 1]] < minWeight && !nodesInTree.contains(coordinateHeight + " " + (coordinateWidth - 1))) {
                        minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth - 1]];
                        nextNodeHeight = coordinateHeight;
                        nextNodeWidth = coordinateWidth - 1;
                        pairNode = nodes;
                    }

                    if (coordinateHeight > 0) {
                        if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight - 1][coordinateWidth]] < minWeight && !nodesInTree.contains((coordinateHeight - 1) + " " + coordinateWidth)) {
                            minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight - 1][coordinateWidth]];
                            nextNodeHeight = coordinateHeight - 1;
                            nextNodeWidth = coordinateWidth;
                            pairNode = nodes;
                        }
                    }

                    if (coordinateHeight < primHeight - 1) {
                        if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight + 1][coordinateWidth]] < minWeight && !nodesInTree.contains((coordinateHeight + 1) + " " + coordinateWidth)) {
                            minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight + 1][coordinateWidth]];
                            nextNodeHeight = coordinateHeight + 1;
                            nextNodeWidth = coordinateWidth;
                            pairNode = nodes;
                        }
                    }
                }


                if (coordinateHeight > 0 && coordinateHeight < primHeight - 1 && coordinateWidth > 0 && coordinateWidth < primWidth - 1) {
                    if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth - 1]] < minWeight && !nodesInTree.contains(coordinateHeight + " " + (coordinateWidth - 1))) {
                        minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth - 1]];
                        nextNodeHeight = coordinateHeight;
                        nextNodeWidth = coordinateWidth - 1;
                        pairNode = nodes;
                    }
                    if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth + 1]] < minWeight && !nodesInTree.contains(coordinateHeight + " " + (coordinateWidth + 1))) {
                        minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight][coordinateWidth + 1]];
                        nextNodeHeight = coordinateHeight;
                        nextNodeWidth = coordinateWidth + 1;
                        pairNode = nodes;
                    }
                    if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight - 1][coordinateWidth]] < minWeight && !nodesInTree.contains((coordinateHeight - 1) + " " + coordinateWidth)) {
                        minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight - 1][coordinateWidth]];
                        nextNodeHeight = coordinateHeight - 1;
                        nextNodeWidth = coordinateWidth;
                        pairNode = nodes;
                    }
                    if (weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight + 1][coordinateWidth]] < minWeight && !nodesInTree.contains((coordinateHeight + 1) + " " + coordinateWidth)) {
                        minWeight = weightsMatrix[nodeMatrix[coordinateHeight][coordinateWidth]][nodeMatrix[coordinateHeight + 1][coordinateWidth]];
                        nextNodeHeight = coordinateHeight + 1;
                        nextNodeWidth = coordinateWidth;
                        pairNode = nodes;
                    }
                }
            }

            nodesInTree.add(nextNodeHeight + " " + nextNodeWidth);
            minimumSpanningTree.add(pairNode + " " + nextNodeHeight + " " + nextNodeWidth);

//          for debug only - prints coordinates of minimum spanning tree
//          System.out.println(minimumSpanningTree);
        }
        return findMinimumSpanningTree(nodesInTree);
    }

    public static void createMaze() {

        //fill the maze with walls
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                maze[i][j] = "\u2588\u2588";
            }
        }

        //
        for (String edge : minimumSpanningTree) {
            String[] coordinates = edge.split(" ");
            int nodeH1 = Integer.parseInt(coordinates[0]) * 2 + 1;
            int nodeW1 = Integer.parseInt(coordinates[1]) * 2 + 1;
            int nodeH2 = Integer.parseInt(coordinates[2]) * 2 + 1;
            int nodeW2 = Integer.parseInt(coordinates[3]) * 2 + 1;

            int nodeH3 = (nodeH1 + nodeH2) / 2;
            int nodeW3 = (nodeW1 + nodeW2) / 2;

            //create paths between two connected nodes
            maze[nodeH1][nodeW1] = "  ";
            maze[nodeH2][nodeW2] = "  ";
            maze[nodeH3][nodeW3] = "  ";

        }
    }

    public static void createEntranceExitInMaze() { //generates either horizontal or vertical entrance and exit of the maze
        isHorizontalEntranceExit = random.nextBoolean();
        boolean isGenerated = false;

        if (isHorizontalEntranceExit) {
            while (!isGenerated) {
                int i = random.nextInt(primHeight) * 2 + 1;
                int j = 0;

                if ("  ".equals(maze[i][j + 1])) {
                    maze[i][j] = "  ";
                    isGenerated = true;
                }

//                dijkstraEntryNodeCoordinates = (i - 1) / 2 + " 0";
                dijkstraEntryNode = nodeMatrix[(i - 1) / 2][0];

//                for debug only - prints Entry node for dijkstraAlgorithm()
//                System.out.println("Entry node = " + dijkstraEntryNodeCoordinates);
//                System.out.println("Entry node = " + dijkstraEntryNode);
            }

            isGenerated = false;

            while (!isGenerated) {
                int i = random.nextInt(primHeight) * 2 + 1;
                int j = mazeWidth - 1;

                if (mazeWidth % 2 > 0 && "  ".equals(maze[i][j - 1])) {
                    maze[i][j] = "  ";
                    isGenerated = true;
                }

                if (mazeWidth % 2 == 0 && "  ".equals(maze[i][j - 2])) {
                    maze[i][j] = "  ";
                    maze[i][j - 1] = " ";
                    isGenerated = true;
                }

//                dijkstraExitNodeCoordinates = (i - 1) / 2 + " " + (primWidth - 1);
                dijkstraExitNode = nodeMatrix[(i - 1) / 2][primWidth - 1];

//                for debug only - prints Exit node for dijkstraAlgorithm()
//                System.out.println("Exit node = " + dijkstraExitNodeCoordinates);
//                System.out.println("Exit node = " + dijkstraExitNode);
            }
        } else {
            while (!isGenerated) {
                int i = 0;
                int j = random.nextInt(primWidth) * 2 + 1;

                if ("  ".equals(maze[i + 1][j])) {
                    maze[i][j] = "  ";
                    isGenerated = true;
                }

//                dijkstraEntryNodeCoordinates = "0 " + (j - 1) / 2;
                dijkstraEntryNode = nodeMatrix[0][(j - 1) / 2];

//                for debug only - prints Entry node for dijkstraAlgorithm()
//                System.out.println("Entry node = " + dijkstraEntryNodeCoordinates);
//                System.out.println("Entry node = " + dijkstraEntryNode);
            }

            isGenerated = false;

            while (!isGenerated) {
                int i = mazeHeight - 1;
                int j = random.nextInt(primWidth) * 2 + 1;

                if (mazeHeight % 2 > 0 && "  ".equals(maze[i - 1][j])) {
                    maze[i][j] = "  ";
                    isGenerated = true;
                }

                if (mazeHeight % 2 == 0 && "  ".equals(maze[i - 2][j])) {
                    maze[i][j] = "  ";
                    maze[i - 1][j] = "  ";
                    isGenerated = true;
                }

//                dijkstraExitNodeCoordinates = (primHeight - 1) + " " + (j - 1) / 2;
                dijkstraExitNode = nodeMatrix[primHeight - 1][(j - 1) / 2];

//                for debug only - prints Exit node for dijkstraAlgorithm()
//                System.out.println("Exit node = " + dijkstraExitNodeCoordinates);
//                System.out.println("Exit node = " + dijkstraExitNode);
            }
        }
    }

    public static String[][] dijkstraAlgorithm(int currentNode) {

        String[] currentNodeCoordinates = nodeToCoordinatesMap.get(currentNode).split(" ");
        int currentNodeHeight = Integer.parseInt(currentNodeCoordinates[0]);
        int currentNodeWidth = Integer.parseInt(currentNodeCoordinates[1]);

        if (currentNodeHeight > 0 && currentNodeHeight < primHeight - 1 && currentNodeWidth > 0 && currentNodeWidth < primWidth - 1) {

            if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth - 1]] < distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth - 1]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight][currentNodeWidth - 1])) {
//                System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth - 1));
                if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth - 1)) ||
                        minimumSpanningTree.contains(currentNodeHeight + " " + (currentNodeWidth - 1) + " " + currentNodeHeight + " " + currentNodeWidth)) {
                    distanceMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth - 1], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth - 1]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                    visitMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth - 1], currentNode);
                }
            }

            if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth + 1]] < distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth + 1]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight][currentNodeWidth + 1])) {
//                System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth + 1));
                if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth + 1)) ||
                        minimumSpanningTree.contains(currentNodeHeight + " " + (currentNodeWidth + 1) + " " + currentNodeHeight + " " + currentNodeWidth)) {
                    distanceMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth + 1], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth + 1]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                    visitMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth + 1], currentNode);
                }
            }

            if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight - 1][currentNodeWidth]] < distanceMap.get(nodeMatrix[currentNodeHeight - 1][currentNodeWidth]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight - 1][currentNodeWidth])) {
//                System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight - 1) + " " + currentNodeWidth);
                if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight - 1) + " " + currentNodeWidth) ||
                        minimumSpanningTree.contains((currentNodeHeight - 1) + " " + currentNodeWidth + " " + currentNodeHeight + " " + currentNodeWidth)) {
                    distanceMap.put(nodeMatrix[currentNodeHeight - 1][currentNodeWidth], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight - 1][currentNodeWidth]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                    visitMap.put(nodeMatrix[currentNodeHeight - 1][currentNodeWidth], currentNode);
                }
            }

            if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight + 1][currentNodeWidth]] < distanceMap.get(nodeMatrix[currentNodeHeight + 1][currentNodeWidth]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight + 1][currentNodeWidth])) {
//                System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight + 1) + " " + currentNodeWidth);
                if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight + 1) + " " + currentNodeWidth) ||
                        minimumSpanningTree.contains((currentNodeHeight + 1) + " " + currentNodeWidth + " " + currentNodeHeight + " " + currentNodeWidth)) {
                    distanceMap.put(nodeMatrix[currentNodeHeight + 1][currentNodeWidth], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight + 1][currentNodeWidth]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                    visitMap.put(nodeMatrix[currentNodeHeight + 1][currentNodeWidth], currentNode);
                }
            }
        }

        //edge conditions
        //upper border
        if (currentNodeHeight == 0) {
            if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight + 1][currentNodeWidth]] < distanceMap.get(nodeMatrix[currentNodeHeight + 1][currentNodeWidth]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight + 1][currentNodeWidth])) {
//                System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight + 1) + " " + currentNodeWidth);
                if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight + 1) + " " + currentNodeWidth) ||
                        minimumSpanningTree.contains((currentNodeHeight + 1) + " " + currentNodeWidth + " " + currentNodeHeight + " " + currentNodeWidth)) {
                    distanceMap.put(nodeMatrix[currentNodeHeight + 1][currentNodeWidth], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight + 1][currentNodeWidth]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                    visitMap.put(nodeMatrix[currentNodeHeight + 1][currentNodeWidth], currentNode);
                }
            }

            if (currentNodeWidth > 0) {
                if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth - 1]] < distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth - 1]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight][currentNodeWidth - 1])) {
//                    System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth - 1));
                    if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth - 1)) ||
                            minimumSpanningTree.contains(currentNodeHeight + " " + (currentNodeWidth - 1) + " " + currentNodeHeight + " " + currentNodeWidth)) {
                        distanceMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth - 1], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth - 1]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                        visitMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth - 1], currentNode);
                    }
                }
            }

            if (currentNodeWidth < primWidth - 1) {
                if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth + 1]] < distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth + 1]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight][currentNodeWidth + 1])) {
//                    System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth + 1));
                    if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth + 1)) ||
                            minimumSpanningTree.contains(currentNodeHeight + " " + (currentNodeWidth + 1) + " " + currentNodeHeight + " " + currentNodeWidth)) {
                        distanceMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth + 1], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth + 1]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                        visitMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth + 1], currentNode);
                    }
                }
            }
        }

        //lower border
        if (currentNodeHeight == primHeight - 1) {
            if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight - 1][currentNodeWidth]] < distanceMap.get(nodeMatrix[currentNodeHeight - 1][currentNodeWidth]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight - 1][currentNodeWidth])) {
//                System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight - 1) + " " + currentNodeWidth);
                if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight - 1) + " " + currentNodeWidth) ||
                        minimumSpanningTree.contains((currentNodeHeight - 1) + " " + currentNodeWidth + " " + currentNodeHeight + " " + currentNodeWidth)) {
                    distanceMap.put(nodeMatrix[currentNodeHeight - 1][currentNodeWidth], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight - 1][currentNodeWidth]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                    visitMap.put(nodeMatrix[currentNodeHeight - 1][currentNodeWidth], currentNode);
                }
            }

            if (currentNodeWidth > 0) {
                if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth - 1]] < distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth - 1]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight][currentNodeWidth - 1])) {
//                    System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth - 1));
                    if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth - 1)) ||
                            minimumSpanningTree.contains(currentNodeHeight + " " + (currentNodeWidth - 1) + " " + currentNodeHeight + " " + currentNodeWidth)) {
                        distanceMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth - 1], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth - 1]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                        visitMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth - 1], currentNode);
                    }
                }
            }

            if (currentNodeWidth < primWidth - 1) {
                if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth + 1]] < distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth + 1]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight][currentNodeWidth + 1])) {
//                    System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth + 1));
                    if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth + 1)) ||
                            minimumSpanningTree.contains(currentNodeHeight + " " + (currentNodeWidth + 1) + " " + currentNodeHeight + " " + currentNodeWidth)) {
                        distanceMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth + 1], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth + 1]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                        visitMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth + 1], currentNode);
                    }
                }
            }
        }

        //left border
        if (currentNodeWidth == 0) {
            if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth + 1]] < distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth + 1]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight][currentNodeWidth + 1])) {
//                System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth + 1));
                if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth + 1)) ||
                        minimumSpanningTree.contains(currentNodeHeight + " " + (currentNodeWidth + 1) + " " + currentNodeHeight + " " + currentNodeWidth)) {
                    distanceMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth + 1], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth + 1]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                    visitMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth + 1], currentNode);
                }
            }

            if (currentNodeHeight > 0) {
                if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight - 1][currentNodeWidth]] < distanceMap.get(nodeMatrix[currentNodeHeight - 1][currentNodeWidth]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight - 1][currentNodeWidth])) {
//                    System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight - 1) + " " + currentNodeWidth);
                    if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight - 1) + " " + currentNodeWidth) ||
                            minimumSpanningTree.contains((currentNodeHeight - 1) + " " + currentNodeWidth + " " + currentNodeHeight + " " + currentNodeWidth)) {
                        distanceMap.put(nodeMatrix[currentNodeHeight - 1][currentNodeWidth], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight - 1][currentNodeWidth]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                        visitMap.put(nodeMatrix[currentNodeHeight - 1][currentNodeWidth], currentNode);
                    }
                }
            }

            if (currentNodeHeight < primHeight - 1) {
                if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight + 1][currentNodeWidth]] < distanceMap.get(nodeMatrix[currentNodeHeight + 1][currentNodeWidth]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight + 1][currentNodeWidth])) {
//                    System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight + 1) + " " + currentNodeWidth);
                    if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight + 1) + " " + currentNodeWidth) ||
                            minimumSpanningTree.contains((currentNodeHeight + 1) + " " + currentNodeWidth + " " + currentNodeHeight + " " + currentNodeWidth)) {
                        distanceMap.put(nodeMatrix[currentNodeHeight + 1][currentNodeWidth], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight + 1][currentNodeWidth]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                        visitMap.put(nodeMatrix[currentNodeHeight + 1][currentNodeWidth], currentNode);
                    }
                }
            }
        }

        //right border
        if (currentNodeWidth == primWidth - 1) {
            if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth - 1]] < distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth - 1]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight][currentNodeWidth - 1])) {
//                System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth - 1));
                if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + currentNodeHeight + " " + (currentNodeWidth - 1)) ||
                        minimumSpanningTree.contains(currentNodeHeight + " " + (currentNodeWidth - 1) + " " + currentNodeHeight + " " + currentNodeWidth)) {
                    distanceMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth - 1], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight][currentNodeWidth - 1]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                    visitMap.put(nodeMatrix[currentNodeHeight][currentNodeWidth - 1], currentNode);
                }
            }

            if (currentNodeHeight > 0) {
                if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight - 1][currentNodeWidth]] < distanceMap.get(nodeMatrix[currentNodeHeight - 1][currentNodeWidth]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight - 1][currentNodeWidth])) {
//                    System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight - 1) + " " + currentNodeWidth);
                    if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight - 1) + " " + currentNodeWidth) ||
                            minimumSpanningTree.contains((currentNodeHeight - 1) + " " + currentNodeWidth + " " + currentNodeHeight + " " + currentNodeWidth)) {
                        distanceMap.put(nodeMatrix[currentNodeHeight - 1][currentNodeWidth], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight - 1][currentNodeWidth]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                        visitMap.put(nodeMatrix[currentNodeHeight - 1][currentNodeWidth], currentNode);
                    }
                }
            }

            if (currentNodeHeight < primHeight - 1) {
                if (weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight + 1][currentNodeWidth]] < distanceMap.get(nodeMatrix[currentNodeHeight + 1][currentNodeWidth]) && !visitedNodes.contains(nodeMatrix[currentNodeHeight + 1][currentNodeWidth])) {
//                    System.out.println(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight + 1) + " " + currentNodeWidth);
                    if (minimumSpanningTree.contains(currentNodeHeight + " " + currentNodeWidth + " " + (currentNodeHeight + 1) + " " + currentNodeWidth) ||
                            minimumSpanningTree.contains((currentNodeHeight + 1) + " " + currentNodeWidth + " " + currentNodeHeight + " " + currentNodeWidth)) {
                        distanceMap.put(nodeMatrix[currentNodeHeight + 1][currentNodeWidth], weightsMatrix[nodeMatrix[currentNodeHeight][currentNodeWidth]][nodeMatrix[currentNodeHeight + 1][currentNodeWidth]] + distanceMap.get(nodeMatrix[currentNodeHeight][currentNodeWidth]));
                        visitMap.put(nodeMatrix[currentNodeHeight + 1][currentNodeWidth], currentNode);
                    }
                }
            }

        }


        visitedNodes.add(currentNode);

        //remove current node from the list of unvisited nodes:
        for (int i = 0; i < unvisitedNodesTemp.size(); i++) {
            if (unvisitedNodesTemp.get(i) == currentNode) {
                unvisitedNodesTemp.remove(i);
                break;
            }
        }

        if (!unvisitedNodesTemp.isEmpty()) {
            int nextNode = findNextNodeForDijkstraAlgorithm();

//            for debug only
//            System.out.println("Current node = " + currentNode);
//            System.out.println("Visited nodes = " + visitedNodes);
//            System.out.println("Unvisited nodes = " + unvisitedNodesTemp);
//            System.out.println("Distance map = " + distanceMap);
//            System.out.println("Visit map = " + visitMap);
//            System.out.println("Next node = " + nextNode);
            return dijkstraAlgorithm(nextNode);
        }

        return createMazeWithPath();
    }

    public static String[][] runDijkstraAlgorithm() {

        initializeDijkstraAlgorithmProcess(dijkstraEntryNode);

        return dijkstraAlgorithm(dijkstraEntryNode);
    }

    public static void initializeDijkstraAlgorithmProcess(int currentNode) {

        distanceMap.clear();
        visitMap.clear();
        visitedNodes.clear();

        unvisitedNodesTemp = new ArrayList<>(unvisitedNodes);
//        System.out.println("Unvisited = " + unvisitedNodes);
//        System.out.println("Temp = " + unvisitedNodesTemp);

        //creates Dijkstra table with distance 0 for the entry node, and with high temporary value "Integer.MAX_VALUE" for the other nodes
        //See for more details - https://www.youtube.com/watch?v=pVfj6mxhdMw
        distanceMap.put(currentNode, 0); //distance from current node to itself is 0
        visitMap.put(currentNode, currentNode); //entry node visited from itself
        for (Integer node : nodeToCoordinatesMap.keySet()) {
            if (currentNode != node) {
                distanceMap.put(node, Integer.MAX_VALUE); //initiate distances to the current nodes with large distance "Integer.MAX_VALUE"
                visitMap.put(node, -1); // initialise visitMap for all tables, "-1" means that the node was not visited yet
            }
        }
    }

    private static int findNextNodeForDijkstraAlgorithm() {
        int minimumDistance = Integer.MAX_VALUE;
        int nextNode = -1;

        for (var entry : distanceMap.entrySet()) {
//            System.out.println("Node candidate = " + entry);
//            System.out.println(visitedNodes.contains(entry.getKey()));
            if (!visitedNodes.contains(entry.getKey())) {
                if (entry.getValue() < minimumDistance) {
                    minimumDistance = entry.getValue();
                    nextNode = entry.getKey();
                }
            }
        }
        return nextNode;
    }

    public static String[][] createMazeWithPath() {
        mazeWithPath = new String[mazeHeight][mazeWidth];

        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                mazeWithPath[i][j] = maze[i][j];
            }
        }

        int currentNode = dijkstraExitNode;

        while (currentNode != dijkstraEntryNode) {

            String[] currentNodeCoordinates = nodeToCoordinatesMap.get(currentNode).split(" ");
            int currentNodeHeight = Integer.parseInt(currentNodeCoordinates[0]);
            int currentNodeWidth = Integer.parseInt(currentNodeCoordinates[1]);

            int linkedNode = visitMap.get(currentNode);
            String[] linkedNodeCoordinates = nodeToCoordinatesMap.get(linkedNode).split(" ");
            int linkedNodeHeight = Integer.parseInt(linkedNodeCoordinates[0]);
            int linkedNodeWidth = Integer.parseInt(linkedNodeCoordinates[1]);

            int nodeH1 = currentNodeHeight * 2 + 1;
            int nodeW1 = currentNodeWidth * 2 + 1;
            int nodeH2 = linkedNodeHeight * 2 + 1;
            int nodeW2 = linkedNodeWidth * 2 + 1;

            int nodeH3 = (nodeH1 + nodeH2) / 2;
            int nodeW3 = (nodeW1 + nodeW2) / 2;

            //create paths between two connected nodes
            mazeWithPath[nodeH1][nodeW1] = "//";
            mazeWithPath[nodeH2][nodeW2] = "//";
            mazeWithPath[nodeH3][nodeW3] = "//";

            currentNode = linkedNode;

        }

        //cover entrance and exit of the maze with "//"
        String[] entryNodeCoordinates = nodeToCoordinatesMap.get(dijkstraEntryNode).split(" ");
        int entryNodeHeight = Integer.parseInt(entryNodeCoordinates[0]);
        int entryNodeWidth = Integer.parseInt(entryNodeCoordinates[1]);
        int nodeH1 = entryNodeHeight * 2 + 1;
        int nodeW1 = entryNodeWidth * 2 + 1;

        String[] exitNodeCoordinates = nodeToCoordinatesMap.get(dijkstraExitNode).split(" ");
        int exitNodeHeight = Integer.parseInt(exitNodeCoordinates[0]);
        int exitNodeWidth = Integer.parseInt(exitNodeCoordinates[1]);
        int nodeH2 = exitNodeHeight * 2 + 1;
        int nodeW2 = exitNodeWidth * 2 + 1;

        if (isHorizontalEntranceExit) {
            mazeWithPath[nodeH1][nodeW1 - 1] = "//";
            mazeWithPath[nodeH2][nodeW2 + 1] = "//";
            if (mazeWidth % 2 == 0) {
                mazeWithPath[nodeH2][nodeW2 + 2] = "//";
            }
        } else {
            mazeWithPath[nodeH1 - 1][nodeW1] = "//";
            mazeWithPath[nodeH2 + 1][nodeW2] = "//";
            if (mazeHeight % 2 == 0) {
                mazeWithPath[nodeH2 + 2][nodeW2] = "//";
            }
        }

        return mazeWithPath;
    }

    public static int getDijkstraEntryNode() {
        return dijkstraEntryNode;
    }

    public static void setDijkstraEntryNode(int node) {
        dijkstraEntryNode = node;
    }

    public static int getDijkstraExitNode() {
        return dijkstraExitNode;
    }

    public static void setDijkstraExitNode(int node) {
        dijkstraExitNode = node;
    }

    public static Map<Integer, String> getNodeToCoordinatesMap() {
        return nodeToCoordinatesMap;
    }

    public static void setNodeToCoordinatesMap(Map<Integer, String> map) {
        nodeToCoordinatesMap = map;
    }

    public static Map<Integer, Integer> getVisitMap() {
        return visitMap;
    }

    public static void setVisitMap(Map<Integer, Integer> map) {
        visitMap = map;
    }

    public static String[][] getMazeWithPath() {
        return mazeWithPath;
    }

    public static void setMazeHeight(int height) {
        mazeHeight = height;
    }


    public static void setMazeWidth(int width) {
        mazeWidth = width;
    }

    public static void setMaze(String[][] mazeTemp) {
        maze = mazeTemp;
    }

    public static void setIsHorizontalEntranceExit(boolean flag) {
        isHorizontalEntranceExit = flag;
    }

    public static boolean getIsHorizontalEntranceExit() {
        return isHorizontalEntranceExit;
    }
}