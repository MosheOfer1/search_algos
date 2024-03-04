import java.util.List;

public class BordGame {
    private final Tile[][] tiles;
    private final Algorithm algorithm;
    private final boolean time;
    private final boolean open;

    public BordGame(Algorithm algorithm,boolean time, boolean open, int n, int m, List<String> whiteBlocksList, int[][] initState) {
        this.algorithm = algorithm;
        this.time = time;
        this.open = open;
        this.tiles = new Tile[n][m];
        Tile[] temp_tiles = new Tile[n * m];
        // Put the white tiles in their goal state positions
        for (int i = 0; i < whiteBlocksList.size(); i += 2) {
            int blockNum = Integer.parseInt(whiteBlocksList.get(i));
            int moves = Integer.parseInt(whiteBlocksList.get(i + 1));
            temp_tiles[blockNum] = new Tile(blockNum, moves);
        }

        // arrange the bord to is initial state
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // The tile is red
                if (temp_tiles[initState[i][j]] == null) {
                    tiles[i][j] = new Tile(initState[i][j]);
                }
                // The tile is White
                else {
                    tiles[i][j] = temp_tiles[initState[i][j]];
                }
            }
        }
    }

    public void solve() {
        Node start_node = new Node(tiles);
        Solution solution = algorithm.solve_puzzle(start_node, open);
        if (solution == null)
            solution = new Solution(null,0,0,0);
        solution.generateTextFile("output.txt", time);
    }

}
