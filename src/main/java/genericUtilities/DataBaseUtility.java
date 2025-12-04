package genericUtilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides generic utility methods for interacting with a MySQL database.
 * <p>
 * This class supports connecting to the database, verifying data, and
 * closing connections. Database credentials are read from a properties file.
 * </p>
 * 
 * @author: Bandi Saiteja
 */
public class DataBaseUtility {

    private FileUtility fUtil = new FileUtility(".\\src\\main\\resources\\TestData\\commonData.properties");
    private Connection con = null;

    /**
     * Establishes a connection to the MySQL database using credentials from the properties file.
     * 
     * @throws ClassNotFoundException if the JDBC driver class is not found
     * @throws SQLException if database connection fails
     * @throws IOException if properties file cannot be read
     */
    public void connectToDB() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String dBUrl = fUtil.readDataFromPropertiesFile("DBUrl");
        String dBuserName = fUtil.readDataFromPropertiesFile("DBUsername");
        String dBPassword = fUtil.readDataFromPropertiesFile("DBPassword");

        con = DriverManager.getConnection(dBUrl, dBuserName, dBPassword);
        System.out.println("<-------Database connection established successfully--------->");
    }

    /**
     * Executes the given SQL query and verifies if the expected text exists in the specified column.
     * 
     * @param query SQL query to execute
     * @param colNum Column number to check (1-based index)
     * @param expectedText The text expected to be present in the column
     * @return {@code true} if the expected text is found, otherwise {@code false}
     * @throws SQLException if query execution fails
     */
    public boolean getDataFromDBAndVerify(String query, int colNum, String expectedText) throws SQLException {
        try (ResultSet result = con.createStatement().executeQuery(query)) {
            while (result.next()) {
                if (result.getString(colNum).equals(expectedText)) {
                    System.out.println(expectedText + " is verified in DB");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println(expectedText + " is not present in DB");
        return false;
    }

    /**
     * Closes the database connection if it is open.
     * 
     * @throws SQLException if closing the connection fails
     */
    public void closeConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
            System.out.println("<-------Database connection closed successfully--------->");
        }
    }
}
