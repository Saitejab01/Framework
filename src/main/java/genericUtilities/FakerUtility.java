package genericUtilities;

import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Provides utility methods for generating realistic test data such as
 * personal information, identity numbers, contact details, employment
 * information, and financial data.  
 * <p>
 * This class leverages the Java Faker library and custom random generators
 * to produce deterministic yet varied mock data suitable for automated
 * testing, data seeding, and validation frameworks.
 * </p>
 *
 * <p><b>Supported Data Categories:</b></p>
 * <ul>
 *     <li>User Identity (Name, Gender, DOB, Age)</li>
 *     <li>Contact Information (Email, Phone, Address)</li>
 *     <li>Government Identifiers (PAN, Passport, Voter ID, etc.)</li>
 *     <li>Company and Employment Details</li>
 *     <li>Banking and Financial Information</li>
 * </ul>
 *
 * <p>
 * All generated values are randomized for realistic variation while
 * maintaining valid formats for each data type.
 * </p>
 *
 * @author Bandi Saiteja
 */
public class FakerUtility {

    private final Faker faker = new Faker(new Locale("en-IND"));
    private final Random random = new Random();
    private String firstName;
    private String lastName;
    private String fullName;
    private String username;
    private String email;
    private String gender;
    private LocalDate dob;
    private int age;

    /**
     * Initializes a new instance of {@code FakerUtility} and generates
     * a complete base identity including name, gender, username, email,
     * date of birth, and age.
     */
    public FakerUtility() {
        createIdentity();
    }

    /**
     * Generates the core identity attributes for the user such as name,
     * email, username, gender, and date of birth.
     */
    private void createIdentity() {
        gender = random.nextBoolean() ? "Male" : "Female";

        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        fullName = firstName + " " + lastName;

        int num = 10 + random.nextInt(89);

        username = firstName.toLowerCase() + "." + lastName.toLowerCase() + num;
        email = firstName.toLowerCase() + num + "@gmail.com";

        dob = LocalDate.now()
                .minusYears(18 + random.nextInt(40))
                .minusDays(random.nextInt(365));

        age = Period.between(dob, LocalDate.now()).getYears();
    }

    // =========================================================================
    // PERSON IDENTITY
    // =========================================================================

    /**
     * @return The full name of the generated user.
     */
    public String getFullName() { return fullName; }

    /**
     * @return The first name of the generated user.
     */
    public String getFirstName() { return firstName; }

    /**
     * @return The last name of the generated user.
     */
    public String getLastName() { return lastName; }

    /**
     * @return The generated username based on first and last name.
     */
    public String getUsername() { return username; }

    /**
     * @return The generated email address.
     */
    public String getEmail() { return email; }

    /**
     * @return The gender assigned during identity creation.
     */
    public String getGender() { return gender; }

    /**
     * @return The date of birth in {@code dd-MM-yyyy} format.
     */
    public String getDOB() {
        return dob.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    /**
     * @return The calculated age in years.
     */
    public int getAge() { return age; }

    /**
     * @return A randomly selected blood group in the correct medical format.
     */
    public String getBloodGroup() {
        String[] groups = {"A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"};
        return groups[random.nextInt(groups.length)];
    }

    /**
     * Determines marital status based on age.
     *
     * @return {@code "Married"} or {@code "Single"}.
     */
    public String getMaritalStatus() {
        return age > 25 ? (random.nextBoolean() ? "Married" : "Single") : "Single";
    }

    // =========================================================================
    // CONTACT INFORMATION
    // =========================================================================

    /**
     * Generates a valid Indian mobile number beginning with digits 6–9.
     *
     * @return A 10-digit mobile number as a string.
     */
    public String getMobile() {
        int start = 6 + random.nextInt(4);
        long remaining = 100000000L + random.nextInt(900000000);
        return start + "" + remaining;
    }

    /**
     * Produces a complete formatted address including street, city, state,
     * and postal code.
     *
     * @return A full postal address.
     */
    public String getAddress() {
        return faker.address().streetAddress() + ", "
                + faker.address().cityName() + ", "
                + faker.address().state() + " - "
                + faker.address().zipCode();
    }

    /**
     * @return A randomly generated Indian state name.
     */
    public String getState() { return faker.address().state(); }

    /**
     * @return A randomly generated city name.
     */
    public String getCity() { return faker.address().city(); }

    /**
     * @return The country name representing the locale. Always returns "India".
     */
    public String getCountry() { return "India"; }

    /**
     * @return Randomly generated latitude in decimal format.
     */
    public String getLatitude() { return faker.address().latitude(); }

    /**
     * @return Randomly generated longitude in decimal format.
     */
    public String getLongitude() { return faker.address().longitude(); }

    // =========================================================================
    // GOVERNMENT IDENTIFIERS
    // =========================================================================

    /**
     * Generates a PAN-like identifier following the format:
     * <pre>AAAAA9999A</pre>
     *
     * @return A PAN-format identifier.
     */
    public String getPAN() {
        return randomString(5).toUpperCase()
                + randomNumber(4)
                + randomString(1).toUpperCase();
    }

    /**
     * Generates an Aadhaar-like 12-digit number grouped into three blocks.
     *
     * @return A formatted Aadhaar-like number.
     */
    public String getAadhaarLike() {
        return randomNumber(4) + " " + randomNumber(4) + " " + randomNumber(4);
    }

    /**
     * Generates a passport-like identifier beginning with a letter
     * followed by seven digits.
     *
     * @return A passport-format identifier.
     */
    public String getPassport() {
        return randomString(1).toUpperCase() + randomNumber(7);
    }

    /**
     * Generates a driving license-like identifier.
     *
     * @return A formatted driving license number.
     */
    public String getDrivingLicense() {
        return randomString(2).toUpperCase() + "-" + randomNumber(13);
    }

    /**
     * Generates a voter ID-like identifier.
     *
     * @return A voter ID-style value.
     */
    public String getVoterID() {
        return randomString(3).toUpperCase() + randomNumber(7);
    }

    /**
     * Generates a ration card-like identifier.
     *
     * @return A ration card-style value.
     */
    public String getRationCard() {
        return "RC-" + randomNumber(10);
    }

    // =========================================================================
    // EMPLOYMENT INFORMATION
    // =========================================================================

    /**
     * @return A random company name.
     */
    public String getCompanyName() { return faker.company().name(); }

    /**
     * @return A random job title.
     */
    public String getJobTitle() { return faker.job().title(); }

    /**
     * Selects a department from a predefined list.
     *
     * @return Department name.
     */
    public String getDepartment() {
        String[] dept = {"IT", "HR", "Finance", "QA", "Support", "Operations"};
        return dept[random.nextInt(dept.length)];
    }

    /**
     * @return An employee code formatted as EMP####.
     */
    public String getEmployeeCode() {
        return "EMP" + randomNumber(4);
    }

    /**
     * @return A company email address for the generated user.
     */
    public String getCompanyEmail() {
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@company.in";
    }

    /**
     * Generates a salary range in INR.
     *
     * @return Salary range in the format "xxxxx - yyyyy INR".
     */
    public String getSalary() {
        int base = 3 + random.nextInt(12);
        return (base * 10000) + " - " + (base * 15000) + " INR";
    }

    // =========================================================================
    // BANKING INFORMATION
    // =========================================================================

    /**
     * @return A random Indian bank name.
     */
    public String getBankName() {
        String[] banks = {"HDFC Bank", "ICICI Bank", "SBI", "Axis Bank", "Kotak Mahindra"};
        return banks[random.nextInt(banks.length)];
    }

    /**
     * @return A random 12-digit bank account number.
     */
    public String getAccountNumber() {
        return randomNumber(12);
    }

    /**
     * @return An IFSC code in the format HDFC0######.
     */
    public String getIFSC() {
        return "HDFC0" + randomNumber(6);
    }

    /**
     * @return A UPI ID for the generated identity.
     */
    public String getUPI() {
        return firstName.toLowerCase() + lastName.toLowerCase() + "@upi";
    }

    /**
     * @return A generated credit card number without hyphens.
     */
    public String getCreditCard() {
        return faker.finance().creditCard().replace("-", "");
    }

    /**
     * @return Credit card expiry date in MM/YY format.
     */
    public String getCardExpiry() {
        int month = 1 + random.nextInt(12);
        int year = 25 + random.nextInt(6);
        return String.format("%02d/%d", month, year);
    }

    /**
     * @return A 3-digit CVV.
     */
    public String getCVV() { return randomNumber(3); }

    /**
     * @return A 6-digit OTP.
     */
    public String getOTP() { return randomNumber(6); }

    /**
     * Generates a password using first initial, last name, and random digits.
     *
     * @return A strong password.
     */
    public String getStrongPassword() {
        return firstName.substring(0, 1).toUpperCase()
                + lastName.toLowerCase()
                + "@"
                + randomNumber(3);
    }

    /**
     * @return A UUID identifier.
     */
    public String getUUID() { return UUID.randomUUID().toString(); }

    /**
     * @return A random boolean value.
     */
    public boolean randomBoolean() { return random.nextBoolean(); }

    // =========================================================================
    // INTERNAL GENERATORS
    // =========================================================================

    /**
     * Generates a random alphabetic string.
     *
     * @param len Number of characters required.
     * @return String containing uppercase alphabetic characters.
     */
    public String randomString(int len) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return builder.toString();
    }

    /**
     * Generates a random numeric string.
     *
     * @param len Number of digits required.
     * @return A string containing only digits 0–9.
     */
    public String randomNumber(int len) {
        String digits = "0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(digits.charAt(random.nextInt(digits.length())));
        }
        return builder.toString();
    }
}
