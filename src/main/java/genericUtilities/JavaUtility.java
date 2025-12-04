package genericUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Provides generic Java utility methods such as date-time formatting,
 * random number/string generation, and string concatenation.
 * <p>
 * Useful for test data generation, timestamping, and generic operations.
 * </p>
 * 
 * @author: Bandi Saiteja
 */
public class JavaUtility {

    private final Random rand = new Random();

    /**
     * Returns the current date and time in the format "dd-MM-yyyy :: HH:mm:ss".
     * 
     * @return Formatted current date and time as String.
     */
    public String getCurrentDateAndTime() {
        Date date = new Date();
        SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy :: HH:mm:ss");
        return sDate.format(date);
    }

    /**
     * Returns the current date in the format "dd-MM-yyyy".
     * 
     * @return Formatted current date as String.
     */
    public String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
        return sDate.format(date);
    }

    /**
     * Generates a random integer between 0 (inclusive) and 1000 (exclusive).
     * 
     * @return Random integer.
     */
    public int generateRandomNumber() {
        return rand.nextInt(1000);
    }

    /**
     * Generates a random alphanumeric string of the given length.
     * 
     * @param size Length of the string to generate.
     * @return Random alphanumeric string.
     */
    public String generateRandomAlphaNumeric(int size) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder randStr = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            int randNum = rand.nextInt(chars.length());
            randStr.append(chars.charAt(randNum));
        }

        return randStr.toString();
    }

    /**
     * Concatenates multiple strings into a single string.
     * 
     * @param strings Varargs of strings to concatenate.
     * @return Concatenated string.
     */
    public String concatenateStrings(String... strings) {
        StringBuilder conString = new StringBuilder();
        for (String str : strings) {
            conString.append(str);
        }
        return conString.toString();
    }
}
