package seleniumUtility;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import genericUtilities.DataBaseUtility;
import genericUtilities.FileUtility;

public class SeleniumBaseClass {
	WebDriver driver;
	public static WebDriver sdriver;
	public DataBaseUtility dbUtil = new DataBaseUtility();
	public WebDriverUtility wUtil = new WebDriverUtility();
	public FileUtility fUtil = new FileUtility(".\\src\\main\\resources\\TestData\\commonData.properties");
	@BeforeSuite
	public void bsConfig() throws ClassNotFoundException, SQLException, IOException {
		//--------------DataBase Connection----------------
		dbUtil.connectToDB();
	}
	@BeforeTest
	public void btConfig() {
		
	}
	@BeforeClass
	public void bcConfig() throws IOException {
		//------------------open Browser-------------------
		String BROWSER = fUtil.readDataFromPropertiesFile("browser").toLowerCase();
		String URL = fUtil.readDataFromPropertiesFile("url");
		switch(BROWSER) {
		case "chrome" : 
			driver = new ChromeDriver();
			break;
		case "firefox" :
			 driver = new FirefoxDriver();
			 break;
		case "edge" :
			driver= new EdgeDriver();
			break;
		default :
			driver= new EdgeDriver();
		}
		
		wUtil.maximizeWindow(driver);
		wUtil.provideImplicitlyWait(driver);
		driver.get(URL);
		sdriver=driver;
	}
	@BeforeMethod
	public void bmConfig() throws IOException {
		//-----------------Login into Application---------------------
		String USERNAME = fUtil.readDataFromPropertiesFile("username");
		String PASSWORD = fUtil.readDataFromPropertiesFile("password");
		
	}
	@AfterMethod
	public void amConfig() {
		//-----------------Logout from Application---------------------
		
	}
	@AfterClass
	public void acConfig() {
		//---------------------Close Browser--------------------------
		driver.quit();
	}
	@AfterTest
	public void atConfig() {
		
	}
	@AfterSuite
	public void asConfig() throws SQLException {
		//--------------DataBase Disconnection----------------
		dbUtil.closeConnection();
	}
}
