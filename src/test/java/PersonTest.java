
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void testUpdateValidDetails() {
        boolean result = Person.updatePersonalDetails("56s_d%&fAB", "56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990", TEST_FILE);
        assertTrue(result, "Valid update with same birthday");
    }

    @Test
    public void testUpdateFailsOnUnder18AddressChange() {
        boolean result = Person.updatePersonalDetails("56s_d%&fAB", "56s_d%&fAB", "John", "Doe", "33|New Street|Melbourne|Victoria|Australia", "25-05-2010", TEST_FILE);
        assertFalse(result, "Under 18 cannot change address");
    }

    @Test
    public void testUpdateFailsOnBirthdayChangeWithOtherChanges() {
        boolean result = Person.updatePersonalDetails("56s_d%&fAB", "56s_d%&fAB", "John", "New", "32|Highland Street|Melbourne|Victoria|Australia", "16-11-1990", TEST_FILE);
        assertFalse(result, "Cannot change birthday and other fields together");
    }

    @Test
    public void testUpdateFailsIfIDStartsWithEvenAndChanges() {
        boolean result = Person.updatePersonalDetails("56s_d%&fAB", "76s_d%&fAB", "John", "Doe", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990", TEST_FILE);
        assertFalse(result, "Even-starting ID cannot be changed");
    }

    @Test
    public void testUpdateFailsWithInvalidNewAddress() {
        boolean result = Person.updatePersonalDetails("56s_d%&fAB", "56s_d%&fAB", "John", "Doe", "123|Main|Melbourne|QLD|Australia", "15-11-1990", TEST_FILE);
        assertFalse(result, "Invalid address (wrong state)");
    }

        @Test
    public void testAddValidDemeritPointsUnderLimit() {
        String result = Person.addDemeritPoints("56s_d%&fAB", "20-06-2024", 3, TEST_FILE);
        assertEquals("Success", result, "Should add valid points");
    }

    @Test
    public void testAddDemeritPointsInvalidDateFormat() {
        String result = Person.addDemeritPoints("56s_d%&fAB", "2024/06/20", 3, TEST_FILE);
        assertEquals("Failed", result, "Invalid date format");
    }

    @Test
    public void testAddDemeritPointsInvalidRange() {
        String result = Person.addDemeritPoints("56s_d%&fAB", "20-06-2024", 10, TEST_FILE);
        assertEquals("Failed", result, "Points must be 1-6");
    }

    @Test
    public void testSuspensionForUnder21ExceedingLimit() {
        Person.addDemeritPoints("56s_d%&fAB", "15-01-2024", 4, TEST_FILE);
        String result = Person.addDemeritPoints("56s_d%&fAB", "20-01-2024", 3, TEST_FILE);
        assertEquals("Success", result, "Should add and suspend under-21");
    }

    @Test
    public void testAddDemeritPointsPersonNotFound() {
        String result = Person.addDemeritPoints("XX1_INVALID", "20-06-2024", 3, TEST_FILE);
        assertEquals("Failed", result, "Person does not exist");
    }



}
