import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Solution {
    private final String path;
    private final int num;
    private final int cost;
    private final double timeInSecond;

    public Solution(String path, int num, int cost, double timeInSecond) {
        this.path = path;
        this.num = num;
        this.cost = cost;
        this.timeInSecond = timeInSecond;
    }

    public void generateTextFile(String filename,boolean time) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(path != null ? path : "no path");
            writer.newLine();
            writer.write("Num: ");
            writer.write(num != 0 ? String.valueOf(num) : "");
            writer.newLine();
            writer.write("Cost: ");
            writer.write(cost != 0 ? String.valueOf(cost) : "");
            writer.newLine();
            if (time)
                writer.write(String.format("%.3f seconds", timeInSecond));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        if (path == null)
            return "no path";
        else
            return path + "\n" +
                "Num: " + num + "\n" +
                "Cost: " + cost + "\n" +
                String.format("%.3f seconds", timeInSecond);
    }

}
