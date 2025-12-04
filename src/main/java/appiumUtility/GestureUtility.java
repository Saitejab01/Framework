package appiumUtility;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

/**
 * This class provides generic gesture-related utility methods for Appium automation on Android devices.
 * It includes gestures such as click, double-click, long-click, drag-and-drop, swipe, pinch (zoom), 
 * and scroll operations.
 * 
 * @author Bandi Saiteja
 */
public class GestureUtility {

    private final AndroidDriver driver;

    /**
     * Constructor to initialize GestureUtility with an AndroidDriver instance.
     * 
     * @param driver the AndroidDriver instance for mobile automation
     */
    public GestureUtility(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Performs a click gesture at the specified screen coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void clickGestureViaCoordinates(int x, int y) {
        driver.executeScript("mobile:clickGesture", ImmutableMap.of("x", x, "y", y));
    }

    /**
     * Performs a click gesture on the specified element.
     * 
     * @param ele the WebElement to click
     */
    public void clickGestureViaWebElement(WebElement ele) {
        RemoteWebElement rEle = (RemoteWebElement) ele;
        driver.executeScript("mobile:clickGesture", ImmutableMap.of("elementId", rEle.getId()));
    }

    /**
     * Performs a double-click gesture at the specified screen coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void doubleClickGestureViaCoordinates(int x, int y) {
        driver.executeScript("mobile:doubleClickGesture", ImmutableMap.of("x", x, "y", y));
    }

    /**
     * Performs a double-click gesture on the specified element.
     * 
     * @param ele the WebElement to double-click
     */
    public void doubleClickGestureViaWebElement(WebElement ele) {
        RemoteWebElement rEle = (RemoteWebElement) ele;
        driver.executeScript("mobile:doubleClickGesture", ImmutableMap.of("elementId", rEle.getId()));
    }

    /**
     * Performs a long-click gesture at the specified screen coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void longClickGestureViaCoordinates(int x, int y) {
        driver.executeScript("mobile:longClickGesture", ImmutableMap.of("x", x, "y", y));
    }

    /**
     * Performs a long-click gesture on the specified element.
     * 
     * @param ele the WebElement to long-click
     */
    public void longClickGestureViaWebElement(WebElement ele) {
        RemoteWebElement rEle = (RemoteWebElement) ele;
        driver.executeScript("mobile:longClickGesture", ImmutableMap.of("elementId", rEle.getId()));
    }

    /**
     * Performs a drag-and-drop gesture from the specified element to the target coordinates.
     * 
     * @param ele  the element to drag
     * @param endX the target x-coordinate
     * @param endY the target y-coordinate
     */
    public void dragAndDropGestureViaWebElement(WebElement ele, int endX, int endY) {
        RemoteWebElement rEle = (RemoteWebElement) ele;
        driver.executeScript("mobile:dragGesture",
                ImmutableMap.of("elementId", rEle.getId(), "endX", endX, "endY", endY));
    }

    /**
     * Performs a drag-and-drop gesture from start coordinates to end coordinates.
     * 
     * @param startX starting x-coordinate
     * @param startY starting y-coordinate
     * @param endX   target x-coordinate
     * @param endY   target y-coordinate
     */
    public void dragAndDropGestureViaCoordinates(int startX, int startY, int endX, int endY) {
        driver.executeScript("mobile:dragGesture",
                ImmutableMap.of("startX", startX, "startY", startY, "endX", endX, "endY", endY));
    }

    /**
     * Performs a zoom-in gesture on the specified element.
     * 
     * @param ele     the element to zoom in
     * @param percent zoom percentage (0-100)
     */ 
    public void zoomInGesture(WebElement ele, double percent) {
        RemoteWebElement rEle = (RemoteWebElement) ele;
        percent = percent / 100;
        driver.executeScript("mobile:pinchOpenGesture", ImmutableMap.of("elementId", rEle.getId(), "percent", percent));
    }

    /**
     * Performs a zoom-out gesture on the specified element.
     * 
     * @param ele     the element to zoom out
     * @param percent zoom percentage (0-100)
     */
    public void zoomOutGesture(WebElement ele, double percent) {
        RemoteWebElement rEle = (RemoteWebElement) ele;
        percent = percent / 100;
        driver.executeScript("mobile:pinchCloseGesture", ImmutableMap.of("elementId", rEle.getId(), "percent", percent));
    }

    /**
     * Performs a swipe gesture using screen coordinates.
     * 
     * @param left      left coordinate
     * @param width     width of swipe area
     * @param top       top coordinate
     * @param height    height of swipe area
     * @param direction swipe direction ("left", "right", "up", "down")
     * @param percent   swipe distance percentage (0-100)
     */
    public void swipeGestureViaCoordinates(int left, int width, int top, int height, String direction, double percent) {
        direction = direction.trim().toLowerCase();
        percent = percent > 1 ? percent / 100 : percent;

        switch (direction) {
            case "right":
            case "left":
            case "up":
            case "down":
                driver.executeScript("mobile:swipeGesture",
                        ImmutableMap.of("left", left, "width", width, "top", top, "height", height,
                                "direction", direction, "percent", percent));
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction + " (use: left, right, up, down)");
        }
    }

    /**
     * Performs a swipe gesture on the specified element.
     * 
     * @param ele       the element to swipe
     * @param direction swipe direction ("left", "right", "up", "down")
     * @param percent   swipe distance percentage (0-100)
     */
    public void swipeGestureViaWebElement(WebElement ele, String direction, double percent) {
        direction = direction.trim().toLowerCase();
        percent = percent > 1 ? percent / 100 : percent;

        RemoteWebElement rEle = (RemoteWebElement) ele;

        switch (direction) {
            case "right":
            case "left":
            case "up":
            case "down":
                driver.executeScript("mobile:swipeGesture",
                        ImmutableMap.of("elementId", rEle.getId(), "direction", direction, "percent", percent));
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction + " (use: left, right, up, down)");
        }
    }

    /**
     * Scrolls to an element containing the exact visible text.
     * 
     * @param text the visible text to scroll to
     * @return WebElement found after scrolling
     */
    public WebElement scrollGestureViaVisibleText(String text) {
        text = text.replace("\"", "\\\"");
        String uiScrollCmd = "new UiScrollable(new UiSelector().scrollable(true))"
                + ".scrollIntoView(new UiSelector().text(\"" + text + "\"))";
        return driver.findElement(AppiumBy.androidUIAutomator(uiScrollCmd));
    }

    /**
     * Scrolls to an element containing partial text.
     * 
     * @param text partial text to scroll to
     * @return WebElement found after scrolling
     */
    public WebElement scrollByContainsText(String text) {
        text = text.replace("\"", "\\\"");
        String cmd = "new UiScrollable(new UiSelector().scrollable(true))"
                + ".scrollIntoView(new UiSelector().textContains(\"" + text + "\"))";
        return driver.findElement(AppiumBy.androidUIAutomator(cmd));
    }

    /**
     * Scrolls to an element with the specified resource ID.
     * 
     * @param resourceId the resource ID to scroll to
     * @return WebElement found after scrolling
     */
    public WebElement scrollToResourceId(String resourceId) {
        String cmd = "new UiScrollable(new UiSelector().scrollable(true))"
                + ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"))";
        return driver.findElement(AppiumBy.androidUIAutomator(cmd));
    }
}
