package appiumUtility;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import genericUtilities.FileUtility;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

/**
 * This is the base class for Appium automation tests.
 * It provides setup and teardown methods for the Appium server, AndroidDriver, and utilities.
 * All test classes should extend this class to inherit these configurations.
 * 
 * Features:
 *  - Starts and stops Appium server
 *  - Initializes AndroidDriver with UiAutomator2Options
 *  - Provides reusable utility objects for Appium operations
 * 
 * Author: Bandi Saiteja
 */
public class AppiumBaseClass {

    private static AppiumDriverLocalService service;
    public AndroidDriver driver;
    public static AndroidDriver sdriver;
    public AppiumUtility aUtil;
    public GestureUtility gUtil;
    public FileUtility fUtil = new FileUtility(".\\src\\main\\resources\\TestData\\commonData.properties");

    /**
     * Sets up the Appium server before the test suite.
     */
    @BeforeSuite
    public void bsConfig() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withArgument(() -> "--allow-insecure", "*:chromedriver_autodownload")
                .withTimeout(Duration.ofSeconds(30));

        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        System.out.println("<-------Appium server started successfully--------->");
    }

    /**
     * Placeholder for configurations before the test tag.
     */
    @BeforeTest
    public void btConfig() {
        // Can be used for TestNG @BeforeTest operations if required
    }

    /**
     * Initializes the AndroidDriver and utility classes before the test class.
     * 
     * @throws Exception if driver initialization fails
     */
    @BeforeClass
    public void bcConfig() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(fUtil.readDataFromPropertiesFile("platformName"));
        options.setUdid(fUtil.readDataFromPropertiesFile("UDID"));
        options.setAppPackage(fUtil.readDataFromPropertiesFile("appPackage"));
        options.setAppActivity(fUtil.readDataFromPropertiesFile("appActivity"));
        options.setChromedriverExecutable(fUtil.readDataFromPropertiesFile("chromedriverExecutable"));
        options.autoGrantPermissions();

        URL url = new URL(fUtil.readDataFromPropertiesFile("url"));
        driver = new AndroidDriver(url, options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        aUtil = new AppiumUtility(driver);
        gUtil = new GestureUtility(driver);
        sdriver=driver;
    }

    /**
     * Placeholder for setup operations before each test method.
     * 
     * @throws IOException if file operations fail
     */
    @BeforeMethod
    public void bmConfig() throws IOException {
        // Can be used for method-level setup operations
    }

    /**
     * Placeholder for cleanup operations after each test method.
     * 
     * @throws IOException if file operations fail
     */
    @AfterMethod
    public void amConfig() throws IOException {
        // Can be used for method-level cleanup operations
    }

    /**
     * Terminates the application after the test class execution.
     * 
     * @throws IOException if file operations fail
     */
    @AfterClass
    public void acConfig() throws IOException {
        driver.terminateApp(fUtil.readDataFromPropertiesFile("appPackage"));
    }

    /**
     * Placeholder for cleanup operations after the test tag.
     */
    @AfterTest
    public void atConfig() {
        // Can be used for TestNG @AfterTest operations if required
    }

    /**
     * Stops the Appium server after the test suite.
     */
    @AfterSuite
    public void asConfig() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("<-------Appium server stopped successfully--------->");
        }
    }
}
