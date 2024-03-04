import java.util.Objects;

public class Tile {
    private final boolean isWhite;
    private int moves;
    private final int number;
    // White Tile constructor
    public Tile(int number,int moves) {
        this.number = number;
        this.isWhite = true;
        this.moves = moves;
    }
    // Red Tile constructor
    public Tile(int number) {
        this.number = number;
        this.isWhite = false;
        this.moves = -1;
    }

    public Tile(Tile tile) {
        this.moves = tile.getMoves();
        this.isWhite = tile.isWhite();
        this.number = tile.getNumber();
    }

    public int getNumber() {
        return number;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return number + " ";
    }

    // Critical for the algorithms to equal 2 tiles by their number
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return number == tile.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    public void reduceMove() {
        this.moves--;
    }
}
