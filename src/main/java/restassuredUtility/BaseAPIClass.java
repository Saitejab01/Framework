package restassuredUtility;

import java.io.IOException;
import java.sql.SQLException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import genericUtilities.DataBaseUtility;
import genericUtilities.FileUtility;
import genericUtilities.JavaUtility;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * Base class for REST API automation tests.
 * <p>
 * Provides setup and teardown methods for TestNG suite execution,
 * including database connection management, and REST Assured request
 * and response specifications.
 * </p>
 * 
 * @author: Bandi Saiteja
 */
public class BaseAPIClass {

    public JavaUtility jLib = new JavaUtility();
    public FileUtility fLib = new FileUtility(".\\src\\main\\resources\\TestData\\commonData.properties");
    public DataBaseUtility dbLib = new DataBaseUtility();

    public static RequestSpecification reqSpecObject;
    public static ResponseSpecification resSpecObject;

    /**
     * Initializes the database connection and configures REST Assured request
     * and response specifications before the TestNG suite starts.
     * 
     * @throws ClassNotFoundException if JDBC driver class is not found
     * @throws SQLException if database connection fails
     * @throws IOException if properties file cannot be read
     */
    @BeforeSuite
    public void configBeforeSuite() throws ClassNotFoundException, SQLException, IOException {
        dbLib.connectToDB();

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(ContentType.JSON);
        reqBuilder.setBaseUri(fLib.readDataFromPropertiesFile("BASEUri"));
        reqSpecObject = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectContentType(ContentType.JSON);
        resSpecObject = resBuilder.build();

        System.out.println("<-------API Base Setup Completed--------->");
    }

    /**
     * Closes the database connection after the TestNG suite finishes.
     * 
     * @throws SQLException if closing the connection fails
     */
    @AfterSuite
    public void configAfterSuite() throws SQLException {
        dbLib.closeConnection();
        System.out.println("<-------API Database Connection Closed--------->");
    }
}
