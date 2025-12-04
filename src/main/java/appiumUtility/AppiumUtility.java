package appiumUtility;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;

/**
 * This class consists of generic utility methods related to Appium operations
 * such as app installation, app management, screen orientation, clipboard handling,
 * WebView switching, and element interactions.
 * 
 * It is designed to simplify common Appium operations on Android devices.
 * 
 * @author Bandi Saiteja
 */
public class AppiumUtility {

    private final AndroidDriver driver;

    /**
     * Constructor to initialize AppiumUtility with an AndroidDriver instance.
     * 
     * @param driver the AndroidDriver instance for mobile automation
     */
    public AppiumUtility(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Installs the specified application on the connected mobile device.
     * 
     * @param applicationPath the file path of the APK to install
     */
    public void installAppIntoMobile(String applicationPath) {
        driver.installApp(applicationPath);
    }

    /**
     * Checks whether the specified app is installed on the device.
     * 
     * @param appPackage the package name of the app
     * @return true if the app is installed, false otherwise
     */
    public boolean isAppInstalled(String appPackage) {
        return driver.isAppInstalled(appPackage);
    }

    /**
     * Activates (launches) the specified application on the device.
     * 
     * @param appPackage the package name of the app to open
     */
    public void openApplication(String appPackage) {
        driver.activateApp(appPackage);
    }

    /**
     * Closes the specified application on the device.
     * 
     * @param appPackage the package name of the app to close
     */
    public void closeApp(String appPackage) {
        driver.terminateApp(appPackage);
    }

    /**
     * Removes the specified application from the device.
     * 
     * @param appPackage the package name of the app to delete
     * @return true if the app was successfully removed, false otherwise
     */
    public boolean deleteApp(String appPackage) {
        return driver.removeApp(appPackage);
    }

    /**
     * Runs the application in the background for a specified duration.
     * 
     * @param seconds the duration in seconds to run the app in the background
     */
    public void runApplicationInBackgroundForSpecifiedTime(int seconds) {
        driver.runAppInBackground(Duration.ofSeconds(seconds));
    }

    /**
     * Runs the application in the background indefinitely until it is activated.
     */
    public void runApplicationInBackgroundTillActivate() {
        driver.runAppInBackground(Duration.ofSeconds(-1));
    }

    /**
     * Retrieves the current state of the specified application.
     * 
     * @param appPackage the package name of the app
     * @return the status of the application as a string
     */
    public String statusOfApplication(String appPackage) {
        ApplicationState status = driver.queryAppState(appPackage);
        return status.toString();
    }

    /**
     * Returns the current activity running on the device.
     * 
     * @return the current activity name
     */
    public String getCurrentActivity() {
        return driver.currentActivity();
    }

    /**
     * Opens the device's notification panel.
     */
    public void openNotifications() {
        driver.openNotifications();
    }

    /**
     * Hides the device's keyboard if visible.
     */
    public void hideKeyboard() {
        driver.hideKeyboard();
    }

    /**
     * Rotates the device screen to the specified orientation.
     * 
     * @param orientation the desired screen orientation ("portrait" or "landscape")
     * @throws IllegalArgumentException if the orientation value is invalid
     */
    public void rotateScreen(String orientation) {
        if (orientation == null) {
            throw new IllegalArgumentException("Orientation value cannot be null");
        }

        orientation = orientation.trim().toLowerCase();

        switch (orientation) {
            case "landscape":
                driver.rotate(ScreenOrientation.LANDSCAPE);
                break;
            case "portrait":
                driver.rotate(ScreenOrientation.PORTRAIT);
                break;
            default:
                throw new IllegalArgumentException(
                    "Invalid orientation: " + orientation + ". Allowed values: portrait, landscape."
                );
        }
    }

    /**
     * Returns the first Toast message element displayed on the device.
     * 
     * @return WebElement representing the Toast message
     */
    public WebElement getTosterMessage() {
        return driver.findElement(AppiumBy.xpath("//android.widget.Toast[1]"));
    }

    /**
     * Switches the driver context to a WebView if available.
     * 
     * @throws InterruptedException if the thread sleep is interrupted
     */
    public void switchToWebView() throws InterruptedException {
        String mainContext = driver.getContext();
        Thread.sleep(5000); // wait for WebView context to load
        Set<String> contexts = driver.getContextHandles();
        for (String ctx : contexts) {
            if (!ctx.equals(mainContext) && ctx.contains("WEBVIEW_chrome")) {
                driver.context(ctx);
                break;
            }
        }
    }

    /**
     * Sets the device clipboard text to the specified value.
     * 
     * @param text the text to set in the clipboard
     */
    public void setClipboardText(String text) {
        driver.setClipboardText(text);
    }

    /**
     * Retrieves the current text from the device clipboard.
     * 
     * @return the clipboard text
     */
    public String getClipboardText() {
        return driver.getClipboardText();
    }

    /**
     * Clicks on the specified WebElement.
     * 
     * @param ele the element to click
     */
    public void clickOnElement(WebElement ele) {
        ele.click();
    }

    /**
     * Enters the specified text into the WebElement.
     * 
     * @param ele  the element to enter text into
     * @param text the text to enter
     */
    public void enterDataIntoElement(WebElement ele, CharSequence text) {
        ele.sendKeys(text);
    }
}
