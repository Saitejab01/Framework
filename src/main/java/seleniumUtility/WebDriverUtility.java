package seleniumUtility;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * this class consists of generic menthods related to WebDriver
 * @author Bandi Saiteja
 * 
 */
public class WebDriverUtility {
	/**
	 * this method will maximize the window
	 * @param driver
	 */
	public void maximizeWindow(WebDriver driver) {
		driver.manage().window().maximize();
	}
	/**
	 * this method will minimize the window
	 * @param driver
	 */
	public void minimizeWindow(WebDriver driver) {
		driver.manage().window().minimize();
	}
	/**
	 * this method will fullscreen the window
	 * @param driver
	 */
	public void fullscreenWindow(WebDriver driver) {
		driver.manage().window().fullscreen();
	}
	/**
	 * this method will add implicitly wait of 10sec
	 * @param driver
	 */
	public void provideImplicitlyWait(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
	/**
	 * this method will add explicitly wait of 10sec till the element is clickable
	 * @param driver
	 * @param ele
	 */
	public void provideExplicitlyWaitForElementToBeClickable(WebDriver driver,WebElement ele) {
		ele.click();
		WebDriverWait wdw = new WebDriverWait(driver, Duration.ofSeconds(10));
		wdw.until(ExpectedConditions.elementToBeClickable(ele));
	}
	/**
	 * this method will add explicitly wait of 10sec till the element is visible
	 * @param driver
	 * @param ele
	 */
	public void provideExplicitlyWaitForElementToBeVisible(WebDriver driver,WebElement ele) {
		WebDriverWait wdw = new WebDriverWait(driver, Duration.ofSeconds(10));
		wdw.until(ExpectedConditions.visibilityOf(ele));
	}
	/**
	 * this method is used to select the option from DropDown by index
	 * @param ele
	 * @param index
	 */
	public void handleDropdowns(WebElement ele,int index) {
		Select sel = new Select(ele);
		sel.selectByIndex(index);
	}
	/**
	 * this method is used to select the option from DropDown by Visible Text
	 * @param ele
	 * @param visibleText
	 */
	public void handleDropdowns(WebElement ele,String visibleText) {
		Select sel = new Select(ele);
		sel.selectByVisibleText(visibleText);
	}
	/**
	 * this method is used to select the option from DropDown by Value
	 * @param value
	 * @param ele
	 */
	public void handleDropdowns(String value,WebElement ele) {
		Select sel = new Select(ele);
		sel.selectByValue(value);
	}
	/**
	 * this method will perform mouse overing action
	 * @param driver
	 * @param x
	 * @param y
	 */
	
	public void toPreformMouseOveringAction(WebDriver driver,int x,int y) {
		Actions act = new Actions(driver);
		act.moveByOffset(x, y).perform();
	}
	/**
	 * this method will perform mouse overing action on to a element
	 * @param driver
	 * @param ele
	 */
	public void toPreformMouseOveringOnToElementAction(WebDriver driver,WebElement ele) {
		Actions act = new Actions(driver);
		act.moveToElement(ele).perform();
	}
	/**
	 * this method will perform drag and drop action from src element to des element
	 * @param driver
	 * @param dragable
	 * @param dropable
	 */
	public void toPreformDragAndDropAction(WebDriver driver,WebElement dragable,WebElement dropable) {
		Actions act = new Actions(driver);
		act.dragAndDrop(dragable, dropable).perform();
	}
	/**
	 * this method will perform drag and drop action from src element to (x,y) coordinates in window
	 * @param driver
	 * @param dragable
	 * @param dropable
	 */
	public void toPreformDragAndDropAction(WebDriver driver,WebElement dragable,int x,int y) {
		Actions act = new Actions(driver);
		act.dragAndDropBy(dragable, x,y).perform();
	}
	/**
	 * to perform click and hold operation
	 * @param driver
	 */
	public void toPreformClickAndHoldAction(WebDriver driver) {
		Actions act = new Actions(driver);
		act.clickAndHold().perform();
	}
	/**
	 * to perform click and hold operation on a element
	 * @param driver
	 * @param ele
	 */
	public void toPreformClickAndHoldAction(WebDriver driver,WebElement ele) {
		Actions act = new Actions(driver);
		act.clickAndHold(ele).perform();
	}
	/**
	 * to perform release action on a element
	 * @param driver
	 * @param ele
	 */
	public void toPreformReleaseAction(WebDriver driver,WebElement ele) {
		Actions act = new Actions(driver);
		act.release(ele).perform();
	}
	/**
	 * to perform release action
	 * @param driver
	 */
	public void toPreformReleaseAction(WebDriver driver) {
		Actions act = new Actions(driver);
		act.release().perform();
	}
	/**
	 * to perform double click action on a element
	 * @param driver
	 * @param ele
	 */
	public void toPreformDoubleClickAction(WebDriver driver,WebElement ele) {
		Actions act = new Actions(driver);
		act.doubleClick(ele).perform();
	}
	/**
	 * to perform double click action
	 * @param driver
	 */
	public void toPreformDoubleClickAction(WebDriver driver) {
		Actions act = new Actions(driver);
		act.doubleClick().perform();
	}
	/**
	 * to perform context click action
	 * @param driver
	 */
	public void toPreformContextClickAction(WebDriver driver) {
		Actions act = new Actions(driver);
		act.contextClick().perform();
	}
	/**
	 * to perform context click action on a element
	 * @param driver
	 * @param ele
	 */
	public void toPreformContextClickAction(WebDriver driver,WebElement ele) {
		Actions act = new Actions(driver);
		act.contextClick(ele).perform();
	}
	
	//handle Frames
	/**
	 * this method is used to switch to a frame by index
	 * @param driver
	 * @param index
	 */
	public void switchToFrame(WebDriver driver,int index) {
		driver.switchTo().frame(index);
	}
	/**
	 * this method is used to switch to a frame by name
	 * @param driver
	 * @param name
	 */
	public void switchToFrame(WebDriver driver,String name) {
		driver.switchTo().frame(name);
	}
	/**
	 * this method is used to switch to a frame by WebElement
	 * @param driver
	 * @param ele
	 */
	public void switchToFrame(WebDriver driver,WebElement ele) {
		driver.switchTo().frame(ele);
	}
	/**
	 * this method is used to switch to a immediate parent frame
	 * @param driver
	 */
	public void switchToParentFrame(WebDriver driver) {
		driver.switchTo().parentFrame();
	}
	/**
	 * this method is used to switch to main frame
	 * @param driver
	 */
	public void switchToMainFrame(WebDriver driver) {
		driver.switchTo().defaultContent();
	}
	/**
	 * this method is used to accept the alert
	 * @param driver
	 */
	public void toAcceptTheAlert(WebDriver driver) {
		driver.switchTo().alert().accept();
	}
	/**
	 * this method is used to dismiss the alert
	 * @param driver
	 */
	public void toDismissTheAlert(WebDriver driver) {
		driver.switchTo().alert().dismiss();
	}
	/**
	 * this method is used to get the text of alert
	 * @param driver
	 * @return
	 */
	public String getTextFromAlert(WebDriver driver) {
		return driver.switchTo().alert().getText();
	}
	/**
	 * this method is used to send message to alert
	 * @param driver
	 * @param message
	 */
	public void getTextFromAlert(WebDriver driver,String message) {
		driver.switchTo().alert().sendKeys(message);
	}
	
	//Handle Window
	/**
	 * this method is used to switch to child window
	 * @param driver
	 */
	public void switchToWindow(WebDriver driver) {
		String mainWin = driver.getWindowHandle();
		Set<String> childWins = driver.getWindowHandles();
		for(String child :childWins) {
			if (!child.equals(mainWin)) {
				driver.switchTo().window(child);
			}
		}
	}
	/**
	 * this method is used to switch to child window based on title
	 * @param driver
	 */
	public void switchToWindow(WebDriver driver,String title) {
		Set<String> childWins = driver.getWindowHandles();
		for(String child :childWins) {
			String ChildTitle = driver.switchTo().window(child).getTitle();
			if (ChildTitle.contains(title)) {
				break;
			}
		}
	}
	/**
	 * this method will perform sendkeys operation
	 * @param ele
	 * @param data
	 */
	public void toPerformSendKeysOperation(WebElement ele,String data) {
		ele.sendKeys(data);
	}
	
	public void toPerformClickOperation(WebElement ele) {
		ele.click();
	}
	
	/**
	 * 
	 * @param driver
	 * @param screenShotName
	 * @return
	 * @throws IOException
	 */
	public String captureScreenShot(WebDriver driver,String screenShotName) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src=ts.getScreenshotAs(OutputType.FILE);
		File des = new File (".\\ScreenShots\\"+screenShotName+".png");
		FileHandler.copy(src, des);
		return des.getAbsolutePath();
	}
}
