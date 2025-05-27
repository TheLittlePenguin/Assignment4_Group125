import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class PersonTest {

    private static final String TEST_FILE = "test_people.txt";

    @BeforeEach
    public void setup() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            writer.write("56s_d%&fAB,John,Doe,32|Highland Street|Melbourne|Victoria|Australia,15-11-1990,15-06-2023:3, false");
            writer.newLine();
        }
    }

    @AfterEach
    public void cleanup() {
        new File(TEST_FILE).delete();
    }
    
}
