import java.io.FileNotFoundException;
import java.io.FileReader;

public class Ex1 {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader = new FileReader("input.txt");
        BordGame bordGame = UtilityClass.createBord(fileReader);
        bordGame.solve();
    }
}
