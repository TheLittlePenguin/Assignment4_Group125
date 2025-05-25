import java.util.HashMap;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Person {

private String personID;
private String firstName;
private String lastName;
private String address;
private String birthdate;
private HashMap<Date, Integer> demeritPoints;
private boolean isSuspended;
private String filePath = "person.txt";


public boolean addPerson(String personID) {
    
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


    return true;
}

public boolean updatePersonalDetails() {

    return true;
}

public String addDemeritPoints() {

    return "Success";
}


















}