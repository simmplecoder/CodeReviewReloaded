import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MySQLDatabaseTestSetup {
    public MySQLDatabaseTestSetup() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("CodeReviewToolTest.sql").getFile());
        try {
            Scanner scanner = new Scanner(file);
            String result = "";
            while (scanner.hasNextLine()) {
                result += scanner.nextLine();
            }
            System.out.println(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
