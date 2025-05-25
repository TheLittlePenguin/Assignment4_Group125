import java.util.HashMap;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.*;

public class Person {

private String personID;
private String firstName;
private String lastName;
private String address;
private String birthdate;
private HashMap<Date, Integer> demeritPoints;
private boolean isSuspended;
private String filePath = "person.txt";
private String newPersonID;


public boolean addPerson(String personID, String address, String birthdate, String filePath) {
    
    // condition 1 start
    if (personID.length() != 10) return false;

        
    if (!Character.isDigit(personID.charAt(0)) || !Character.isDigit(personID.charAt(1))) return false;
    int firstDigit = Character.getNumericValue(personID.charAt(0));
    int secondDigit = Character.getNumericValue(personID.charAt(1));
    if (firstDigit < 2 || firstDigit > 9 || secondDigit < 2 || secondDigit > 9) return false;

       
    char secondLast = personID.charAt(8);
    char last = personID.charAt(9);
    if (!Character.isUpperCase(secondLast) || !Character.isUpperCase(last)) return false;

    int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char ch = personID.charAt(i);
            if (!Character.isLetterOrDigit(ch)) {
                specialCount++;
            }
        }
        if (specialCount < 2) return false;
    //condition 1 end

    //conditon 2 start 
    String[] parts = address.split("\\|");
        if (parts.length != 5) return false;
        if (!parts[3].trim().equalsIgnoreCase("Victoria")) return false;

    //condition 2 end

    //conditon 3 start

    if (!birthdate.matches("\\d{2}-\\d{2}-\\d{4}")) return false;
        String[] dateParts = birthdate.split("-");
        try {
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900) return false;
        } catch (NumberFormatException e) {
            return false;
        }

    //conditon 3 end

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(personID + "," + address + "," + birthdate);
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }


    return true;
}

public boolean updatePersonalDetails(String personID, String newPersonID, String firstName, String lastName,
                                                String address, String birthdate, String filePath) {
    List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 6) {
                    lines.add(line);
                    continue;
                }

                String existingID = data[0];
                String existingFirstName = data[1];
                String existingLastName = data[2];
                String existingAddress = data[3];
                String existingBirthdate = data[4];

                if (!existingID.equals(personID)) {
                    lines.add(line);
                    continue;
                }

                //condition 1
                if (!existingBirthdate.equals(birthdate)) {
                    if (!newID.equals(originalID) || !firstName.equals(existingFirstName) ||
                        !lastName.equals(existingLastName) || !address.equals(existingAddress)) {
                        return false;
                    }
                }

                //condition 2
                if (existingBirthdate.equals(birthdate)) {
                    int age = calculateAge(birthdate);
                    if (age < 18 && !address.equals(existingAddress)) {
                        return false;
                    }
                }

    return true;
}

public String addDemeritPoints() {

    return "Success";
}


















}