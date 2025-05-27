
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String offenseDate;
    private int points;

    public boolean addPerson(String personID, String address, String birthdate, String filePath) {

        // condition 1 start
        if (personID.length() != 10) {
            return false;
        }

        if (!Character.isDigit(personID.charAt(0)) || !Character.isDigit(personID.charAt(1))) {
            return false;
        }
        int firstDigit = Character.getNumericValue(personID.charAt(0));
        int secondDigit = Character.getNumericValue(personID.charAt(1));
        if (firstDigit < 2 || firstDigit > 9 || secondDigit < 2 || secondDigit > 9) {
            return false;
        }

        char secondLast = personID.charAt(8);
        char last = personID.charAt(9);
        if (!Character.isUpperCase(secondLast) || !Character.isUpperCase(last)) {
            return false;
        }

        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char ch = personID.charAt(i);
            if (!Character.isLetterOrDigit(ch)) {
                specialCount++;
            }
        }
        if (specialCount < 2) {
            return false;
        }
        //condition 1 end

        //conditon 2 start 
        String[] parts = address.split("\\|");
        if (parts.length != 5) {
            return false;
        }
        if (!parts[3].trim().equalsIgnoreCase("Victoria")) {
            return false;
        }

        //condition 2 end
        //conditon 3 start
        if (!birthdate.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return false;
        }
        String[] dateParts = birthdate.split("-");
        try {
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900) {
                return false;
            }
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

    }

    //function two
    public boolean updatePersonalDetails(String personID, String newPersonID, String firstName, String lastName, String address, String birthdate, String filePath) {

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
                    if (!newPersonID.equals(personID) || !firstName.equals(existingFirstName)
                            || !lastName.equals(existingLastName) || !address.equals(existingAddress)) {
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

                //condition 3
                char firstChar = existingID.charAt(0);
                if (Character.isDigit(firstChar) && (firstChar - '0') % 2 == 0 && !newPersonID.equals(personID)) {
                    return false;
                }

                //validation
                if (!isValidPersonID(newPersonID) || !isValidAddress(address) || !isValidBirthdate(birthdate)) {
                    return false;
                }
                String updatedLine = String.join(",", newPersonID, firstName, lastName, address, birthdate, data[5]); // keep demerits/suspension
                lines.add(updatedLine);
                updated = true;

                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //validate personID
    private static boolean isValidPersonID(String personID) {
        if (personID.length() != 10) {
            return false;
        }

        if (!Character.isDigit(personID.charAt(0)) || !Character.isDigit(personID.charAt(1))) {
            return false;
        }
        int firstDigit = Character.getNumericValue(personID.charAt(0));
        int secondDigit = Character.getNumericValue(personID.charAt(1));
        if (firstDigit < 2 || firstDigit > 9 || secondDigit < 2 || secondDigit > 9) {
            return false;
        }

        if (!Character.isUpperCase(personID.charAt(8)) || !Character.isUpperCase(personID.charAt(9))) {
            return false;
        }

        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char ch = personID.charAt(i);
            if (!Character.isLetterOrDigit(ch)) {
                specialCount++;
            }
        }
        return specialCount >= 2;
    }

    private static boolean isValidAddress(String address) {
        String[] parts = address.split("\\|");
        return parts.length == 5 && parts[3].trim().equalsIgnoreCase("Victoria");
    }

    //validating birthdate
    private static boolean isValidBirthdate(String birthdate) {
        if (!birthdate.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return false;
        }

        try {
            String[] parts = birthdate.split("-");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            return (day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= 1900);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    //calculating age
    private static int calculateAge(String birthdate) {
        try {
            String[] parts = birthdate.split("-");
            int birthYear = Integer.parseInt(parts[2]);
            int birthMonth = Integer.parseInt(parts[1]);
            int birthDay = Integer.parseInt(parts[0]);

            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - birthYear;

            if (today.get(Calendar.MONTH) + 1 < birthMonth
                    || (today.get(Calendar.MONTH) + 1 == birthMonth && today.get(Calendar.DAY_OF_MONTH) < birthDay)) {
                age--;
            }
            return age;
        } catch (Exception e) {
            return 0;
        }
    }

    private static Map<String, Integer> parseOffenseData(String data) {
        Map<String, Integer> offenses = new HashMap<>();
        if (data == null || data.trim().isEmpty()) {
            return offenses;
        }

        String[] entries = data.split("\\|");
        for (String entry : entries) {
            String[] pair = entry.split(":");
            if (pair.length == 2) {
                offenses.put(pair[0], Integer.parseInt(pair[1]));
            }
        }
        return offenses;
    }

    //calculate demeritpoints for within two years
    private static boolean isWithinLastTwoYears(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date offenseDate = sdf.parse(dateStr);

            Calendar twoYearsAgo = Calendar.getInstance();
            twoYearsAgo.add(Calendar.YEAR, -2);
            return !offenseDate.before(twoYearsAgo.getTime());
        } catch (ParseException e) {
            return false;
        }
    }

    public String addDemeritPoints(String personID, String offenseDate, int points, String filePath) {

        //validating input data
        if (!offenseDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return "Failed";
        }

        if (points < 1 || points > 6) {
            return "Failed";
        }

        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 6 || !parts[0].equals(personID)) {
                    lines.add(line);
                    continue;
                }

                String birthdate = parts[4];
                String offenseData = parts[5];
                boolean isSuspended = parts.length > 6 && parts[6].equalsIgnoreCase("true");

                Map<String, Integer> offenses = parseOffenseData(offenseData);
                offenses.put(offenseDate, points);

                int recentPoints = 0;
                for (Map.Entry<String, Integer> entry : offenses.entrySet()) {
                    if (isWithinLastTwoYears(entry.getKey())) {
                        recentPoints += entry.getValue();
                    }
                }
                
                //calculating age against current demerit points
                int age = calculateAge(birthdate);
                if ((age < 21 && recentPoints > 6) || (age >= 21 && recentPoints > 12)) {
                    isSuspended = true;
                }

                //updating offenses
                StringBuilder updatedOffenses = new StringBuilder();
                for (Map.Entry<String, Integer> entry : offenses.entrySet()) {
                    if (updatedOffenses.length() > 0) {
                        updatedOffenses.append("|");
                    }
                    updatedOffenses.append(entry.getKey()).append(":").append(entry.getValue());
                }

                String updatedLine = String.join(",", parts[0], parts[1], parts[2], parts[3], parts[4], updatedOffenses.toString(), String.valueOf(isSuspended));
                lines.add(updatedLine);
                updated = true;
            }

        } catch (IOException e) {
            return "Failed";
        }

        return "Success";
    }

}
