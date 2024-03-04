import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UtilityClass {
    public enum Direction {
        LEFT, UP, RIGHT, DOWN
    }

    public static Solution path(Node n ,int num ,int cost ,long start_time) {
        StringBuilder path = new StringBuilder();

        // Back tracking for finding the path
        Node current_node = n;
        while (current_node.parent != null) {
            int x_empty = current_node.getX_empty_cell();
            int y_empty = current_node.getY_empty_cell();
            int x_parent_empty = current_node.parent.getX_empty_cell();
            int y_parent_empty = current_node.parent.getY_empty_cell();
            int number = current_node.tiles[x_parent_empty][y_parent_empty].getNumber();

            // Up
            if (x_empty == x_parent_empty + 1)
                path.append("U").append(number);
            // Down
            else if (x_empty == x_parent_empty - 1)
                path.append("D").append(number);
            // Left
            else if (y_empty == y_parent_empty + 1)
                path.append("L").append(number);
            // Right
            else if (y_empty == y_parent_empty - 1)
                path.append("R").append(number);

            if (number == 10) {
                path.delete(path.length() - 2, path.length());
                path.append("01");
            }

            path.append("-");
            current_node = current_node.parent;
        }
        path.reverse();
        path.deleteCharAt(0);

        long end_time = System.currentTimeMillis();
        long elapsedTime = end_time - start_time;
        double elapsedTimeInSeconds = elapsedTime / 1000.0; // Convert milliseconds to seconds

        return new Solution(path.toString(), num, cost, elapsedTimeInSeconds);
    }
    public static void printOpenListConfiguration(Collection<Node> openList) {
        System.out.println("Open List Configuration:");
        System.out.println("Total Nodes in Open List: " + openList.size());
        int index = 1;
        for (Node node : openList) {
            System.out.println("Node " + index + ": " + node);
            System.out.println("Board Configuration:");
            printBoard(node.tiles);
            System.out.println("\ng: " + node.G() + ", h: " + node.H() + ", f: " + node.F() + (node.parent == null ? "" : ", Parent: " +  node.parent));
            System.out.println("---------------------------------");
            index++;
        }
        System.out.println("\n\n\n");
    }

    public static void printBoard(Tile[][] tiles) {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                System.out.print(tile + " ");
            }
            System.out.println();
        }
    }

    public static BordGame createBord(FileReader fileReader){
        BordGame bordGame = null;
        try {
            BufferedReader reader = new BufferedReader(fileReader);

            // Read the data from the input file
            String algorithm = reader.readLine().trim();
            String timingPreference = reader.readLine().trim();
            String openPreference = reader.readLine().trim();
            String boardSize = reader.readLine().trim();

            boolean show_time = timingPreference.equals("with time");
            boolean show_open_list = openPreference.equals("with open");

            String[] sizeTokens = boardSize.split("x");
            int N = Integer.parseInt(sizeTokens[0]);
            int M = Integer.parseInt(sizeTokens[1]);

            // Read white blocks
            String whiteBlocksLine = reader.readLine().trim();
            List<String> whiteBlocksList = new ArrayList<>();
            if (whiteBlocksLine.startsWith("White: (")) {
                whiteBlocksLine = whiteBlocksLine.split(":")[1].trim();
                String[] whiteBlocksArray = whiteBlocksLine.replaceAll("[()]", "").split(",");
                whiteBlocksList.addAll(Arrays.asList(whiteBlocksArray));
            }

            // Parse board arrangement
            int[][] initState = new int[N][M];
            for (int i = 0; i < N; i++) {
                String[] lineTokens = reader.readLine().trim().split(",");
                for (int j = 0; j < M; j++) {
                    if (!lineTokens[j].equals("_")) {
                        initState[i][j] = Integer.parseInt(lineTokens[j]);
                    } else {
                        // Handle empty block (marked as "_")
                        initState[i][j] = 0;
                    }
                }
            }
            reader.close();

            // create the right algo
            Algo_factory algo_factory = new Algo_factory();
            Algorithm algo = algo_factory.createAlgo(algorithm);

            // Create the right game bord
            bordGame = new BordGame(algo,show_time, show_open_list, N, M, whiteBlocksList, initState);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }

        return bordGame;
    }

    public static Node expand_node(Node n,Direction current_direction, int iterationNum) {
        // The order of creating new nodes is: Left, Up, Right, Down
        Node child = null;

        switch (current_direction) {
            case LEFT :
                if (n.getY_empty_cell() < n.getM() - 1
                        && n.tiles[n.getX_empty_cell()][n.getY_empty_cell() + 1].getMoves() != 0
                        && n.getDirection_priority() != Direction.RIGHT) {
                    child = createNewNode(n,0, 1, iterationNum, Direction.LEFT);
                    child.tiles[n.getX_empty_cell()][n.getY_empty_cell()].reduceMove();
                }
                break;

                case UP :
                if (n.getX_empty_cell() < n.getN() - 1 && n.tiles[n.getX_empty_cell() + 1][n.getY_empty_cell()].getMoves() != 0 && n.getDirection_priority() != Direction.DOWN) {
                    child = createNewNode(n, 1, 0, iterationNum, Direction.UP);
                    child.tiles[n.getX_empty_cell()][n.getY_empty_cell()].reduceMove();
                }

                break;

                case RIGHT :
                if (n.getY_empty_cell() > 0 && n.tiles[n.getX_empty_cell()][n.getY_empty_cell() - 1].getMoves() != 0 && n.getDirection_priority() != Direction.LEFT) {
                    child = createNewNode(n, 0, -1, iterationNum, Direction.RIGHT);
                    child.tiles[n.getX_empty_cell()][n.getY_empty_cell()].reduceMove();
                }

                break;

                case DOWN :
                if (n.getX_empty_cell() > 0 && n.tiles[n.getX_empty_cell() - 1][n.getY_empty_cell()].getMoves() != 0&& n.getDirection_priority() != Direction.UP) {
                    child = createNewNode(n, -1, 0, iterationNum, Direction.DOWN);
                    child.tiles[n.getX_empty_cell()][n.getY_empty_cell()].reduceMove();
                }

                break;
        }
        return child;
    }

    // Helper method to create a new node and add it to the children list
    private static Node createNewNode (Node n, int xOffset, int yOffset, int iterationNum, Direction direction_priority) {
        // Deep copy of each Tile
        Tile[][] newTiles = new Tile[n.tiles.length][];
        for (int i = 0; i < n.tiles.length; i++) {
            newTiles[i] = new Tile[n.tiles[i].length];
            for (int j = 0; j < n.tiles[i].length; j++) {
                newTiles[i][j] = new Tile(n.tiles[i][j]);
            }
        }

        Tile temp = newTiles[n.getX_empty_cell()][n.getY_empty_cell()];
        newTiles[n.getX_empty_cell()][n.getY_empty_cell()] = newTiles[n.getX_empty_cell() + xOffset][n.getY_empty_cell() + yOffset];
        newTiles[n.getX_empty_cell() + xOffset][n.getY_empty_cell() + yOffset] = temp;

        return new Node(newTiles, n, newTiles[n.getX_empty_cell()][n.getY_empty_cell()].isWhite() ? 1 : 30, iterationNum, direction_priority);
    }
    public static int factorial(int number) {
        int result = 1;

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }
}
