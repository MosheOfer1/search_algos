import java.util.Arrays;

public class Node implements Comparable<Node> {
    public Tile[][] tiles;
    public Node parent;
    private final int n;
    private final int m;
    private int g;
    private int h;
    private int cost;
    private int f = -1;
    private final int iteration_num;
    private int x_empty_cell;
    private int y_empty_cell;
    private final UtilityClass.Direction direction_priority;
    private boolean out = false;

    // The root in the search tree's constructor
    public Node(Tile[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
        this.m = tiles[0].length;
        this.parent = null;
        this.g = 0;
        this.h = -1;
        this.iteration_num = 0;
        this.direction_priority = null;
        findTheEmptyCell();
    }

    // The rest of the nodes in the search tree's constructor
    public Node(Tile[][] tiles, Node parent, int cost, int iterationNumb, UtilityClass.Direction direction_priority) {
        deepCopy(tiles);
        this.n = tiles.length;
        this.m = tiles[0].length;
        this.parent = parent;
        this.g = this.h = -1;
        this.cost = cost;
        this.iteration_num = iterationNumb;
        this.direction_priority = direction_priority;
        findTheEmptyCell();
    }


    // Copy constructor
    public Node(Node otherNode) {
        deepCopy(otherNode.tiles);
        this.parent = otherNode.parent;
        this.x_empty_cell = otherNode.x_empty_cell;
        this.y_empty_cell = otherNode.y_empty_cell;
        this.n = otherNode.n;
        this.m = otherNode.m;
        this.g = otherNode.g;
        this.h = otherNode.h;
        this.cost = otherNode.cost;
        this.f = otherNode.f;
        this.iteration_num = otherNode.iteration_num;
        this.direction_priority = otherNode.direction_priority;
    }
    private void deepCopy(Tile[][] tiles) {
        this.tiles = new Tile[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].isWhite())
                    this.tiles[i][j] = new Tile(tiles[i][j].getNumber(), tiles[i][j].getMoves());
                else
                    this.tiles[i][j] = new Tile(tiles[i][j].getNumber());
            }
        }
    }
    public int G() {
        if (g != -1)
            return g;
        this.g = this.parent.G() + cost;
        return g;
    }
    // The Heuristic Function
    public int ManhattanDistance(Tile[][] tiles) {
        int distance = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                if (tile != null) {
                    int tile_number = tile.getNumber() - 1;
                    if (tile_number == -1)
                        tile_number = n * m - 1;
                    int desiredRow = tile_number / m;
                    int desiredCol = tile_number % m;
                    int manhattanDistance = Math.abs(desiredRow - i) + Math.abs(desiredCol - j);
                    // Check if the tile is white or red and give it the proper cost
                    // Red tile
                    if (!tiles[i][j].isWhite())
                        manhattanDistance *= 30;
                    // White tile
                    else {
                        // If the Manhattan distance is greater than the allowed moves
                        if (manhattanDistance > tiles[i][j].getMoves())
                            return Integer.MAX_VALUE;
                    }
                    distance += manhattanDistance;
                }
            }
        }
        return distance;
    }

    public int H() {
        if (h != -1)
            return h;
        this.h = ManhattanDistance(tiles);
        return h;
    }
    public int F() {
        if (f != -1)
            return f;

        this.f = G() + H();
        if (h == Integer.MAX_VALUE) {
            this.f = h;
        }
        return f;
    }

    public boolean isGoal() {
        int counter = 1;
        int n = tiles.length;
        int m = tiles[0].length;
        for (Tile[] tiles_row : tiles) {
            for (Tile tile : tiles_row) {
                if (tile.getNumber() % (n * m) != counter  % (n * m))
                    return false;
                counter++;
            }
        }
        return true;
    }
    private void findTheEmptyCell() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j].getNumber() == 0) {
                    x_empty_cell = i;
                    y_empty_cell = j;
                    return;
                }
            }
        }
    }

    public int getX_empty_cell() {
        return x_empty_cell;
    }

    public int getY_empty_cell() {
        return y_empty_cell;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    // Critical for the algorithms to equal 2 nodes by their tiles configuration
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return Arrays.deepEquals(tiles, node.tiles);
    }
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }


    @Override
    public int compareTo(Node o) {
        if (this.F() != o.F())
            return Integer.compare(this.F(), o.F());
        if (this.iteration_num != o.iteration_num)
            return Integer.compare(this.iteration_num, o.iteration_num);
        return Integer.compare(this.direction_priority.ordinal(), o.direction_priority.ordinal());
    }

    public UtilityClass.Direction getDirection_priority() {
        return direction_priority;
    }

    public boolean isMarkAsOut() {
        return this.out;
    }

    public void markAsOut() {
        this.out = true;
    }
}
