
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    public void testAddPersonValid() {
        boolean result = Person.addPerson("56s_d%&fAB", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990", TEST_FILE);
        assertTrue(result, "Should add valid person");
    }

    @Test
    public void testAddPersonInvalidIDFormat() {
        boolean result = Person.addPerson("12abcdE123", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990", TEST_FILE);
        assertFalse(result, "ID is not valid format");
    }

    @Test
    public void testAddPersonInvalidAddressState() {
        boolean result = Person.addPerson("56s_d%&fAB", "32|Highland Street|Melbourne|NSW|Australia", "15-11-1990", TEST_FILE);
        assertFalse(result, "State must be Victoria");
    }

    @Test
    public void testAddPersonInvalidBirthdateFormat() {
        boolean result = Person.addPerson("56s_d%&fAB", "32|Highland Street|Melbourne|Victoria|Australia", "1990-11-15", TEST_FILE);
        assertFalse(result, "Birthdate format must be DD-MM-YYYY");
    }

    @Test
    public void testAddPersonMissingSpecialCharactersInID() {
        boolean result = Person.addPerson("56abcdefAB", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990", TEST_FILE);
        assertFalse(result, "ID must have at least two special characters");
    }

}
