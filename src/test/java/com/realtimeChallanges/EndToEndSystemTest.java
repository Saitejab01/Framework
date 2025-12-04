package com.realtimeChallanges;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.windows.WindowsDriver;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class EndToEndSystemTest {
	 ProcessBuilder pb = null;
	 Process process =null;
    String invoiceID = null;
    
//    @BeforeSuite
//    public void bsConfig() throws IOException, InterruptedException {
//        pb = new ProcessBuilder("cmd.exe", "/k", "start", "appium", "--port", "4723");
//        process = pb.start();
//        Thread.sleep(5000);
//    }
//    
//    @AfterSuite
//    public void asConfig() throws Exception {
//        // Kill Appium process
//        if (process != null) {
//            process.destroy();
//        }
//        
//        // Kill anything running on port 4723
//        Process kill = Runtime.getRuntime().exec("cmd.exe /c for /f \"tokens=5\" %a in ('netstat -ano ^| findstr :4723') do taskkill /F /PID %a");
//        kill.waitFor();
//    }

    @Test(priority = 0)
    public void crmFullFlow() throws Throwable {

        // ====================== EXCEL SETUP ======================
        File f = new File("C:\\Users\\User\\FireFlinkProjects\\unifiedFramework\\src\\test\\resources\\MasterExcel.xlsx");
        FileInputStream fis = new FileInputStream(f);
        Workbook wb = WorkbookFactory.create(fis);
        fis.close();
        Sheet sheet = wb.getSheet("Sheet1");
        Row row = sheet.createRow(1);

        Random rand = new Random();
        int randNum = rand.nextInt(1000, 9999);

        String venderName = "LokeshGangineni" + randNum;
        String productName = venderName + "Product";

        row.createCell(0).setCellValue(venderName);
        row.createCell(1).setCellValue(productName);

        // ====================== WEB AUTOMATION ======================
        WebDriver driver = new EdgeDriver();
        driver.get("http://49.249.28.218:8097/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.findElement(By.id("username")).sendKeys("rmgyantra");
        driver.findElement(By.id("inputPassword")).sendKeys("rmgy@9999");
        driver.findElement(By.tagName("button")).click();

        driver.findElement(By.xpath("//li[.='Vendors']")).click();
        driver.findElement(By.xpath("//span[.='Add New Vendor']")).click();
        driver.findElement(By.xpath("//label[contains(.,'Name*')]/..//input")).sendKeys(venderName);
        driver.findElement(By.xpath("//label[contains(.,'Email*')]/..//input")).sendKeys(venderName + "@gmail.com");
        driver.findElement(By.xpath("//label[contains(.,'Phone*')]/..//input")).sendKeys("963253" + randNum);
        Select sel1 = new Select(driver.findElement(By.xpath("//label[contains(.,'Category*')]/..//select")));
        sel1.selectByValue("Electronics");
        driver.findElement(By.xpath("//label[contains(.,'Website')]/..//input")).sendKeys(venderName + ".com");
        driver.findElement(By.xpath("//label[contains(.,'Street')]/..//input")).sendKeys(venderName + "Street");
        driver.findElement(By.xpath("//label[contains(.,'City')]/..//input")).sendKeys(venderName + "City");
        driver.findElement(By.xpath("//label[contains(.,'State')]/..//input")).sendKeys(venderName + "State");
        driver.findElement(By.xpath("//label[contains(.,'Postal Code')]/..//input")).sendKeys("582147");
        driver.findElement(By.xpath("//label[contains(.,'Country')]/..//input")).sendKeys("india");
        driver.findElement(By.xpath("//input[@value=\"Add\"]")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.findElement(By.xpath("//input[@placeholder=\"Search by Vendor Name\"]")).sendKeys(venderName);
        WebElement vID = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//td[contains(.,'" + venderName + "')]/preceding-sibling::td[contains(.,'VID')]"))));
        String venderID = vID.getText();
        row.createCell(2).setCellValue(venderID);

        driver.findElement(By.xpath("//li[.='Products']")).click();
        driver.findElement(By.xpath("//span[.='Create Product']")).click();
        driver.findElement(By.xpath("//label[contains(.,'Product Name*')]/..//input")).sendKeys(productName);
        Select sel2 = new Select(driver.findElement(By.xpath("//label[contains(.,'Product Category')]/..//select")));
        sel2.selectByValue("Electronics");
        driver.findElement(By.xpath("//label[contains(.,'Quantity*')]/..//input")).sendKeys("100");
        driver.findElement(By.xpath("//label[contains(.,'Price Per Unit($)')]/..//input")).sendKeys("1000");
        Select sel3 = new Select(driver.findElement(By.xpath("//select[@name=\"vendorId\"]")));
        sel3.selectByVisibleText(venderName + " - (Electronics)");
        driver.findElement(By.xpath("//input[@value=\"Add product\"]")).click();
        driver.findElement(By.xpath("//input[@placeholder=\"Search by product Id\"]")).sendKeys(productName);
        WebElement pID = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//td[contains(.,'" + productName + "')]/preceding-sibling::td[contains(.,'IM')]"))));
        String productID = pID.getText();
        row.createCell(3).setCellValue(productID);

        // ====================== API AUTOMATION ======================
        baseURI="http://49.249.28.218:8098";
		String campaignName="campaignName"+randNum;
		String targetSize="10";

		String campaignJsonBody ="		{\r\n"
				+ "			  \"campaignName\": \""+campaignName+"\",\r\n"
				+ "			  \"targetSize\": "+targetSize+"\r\n"
				+ "			}";
	Response resp=given()
		.contentType(ContentType.JSON)
		.body(campaignJsonBody)
		.when()
		.post("/campaign");
		
		String campaignId=resp.jsonPath().get("campaignId");
		row.createCell(4).setCellValue(campaignId);
		
		
		//contact Creation
		String contactName="ContactName"+randNum;
		String mobile = "852314"+randNum;
		
		String contactJsonBody="{\r\n"
				+ "  \"contactName\": \""+contactName+"\",\r\n"
				+ "  \"email\": \""+contactName+"@gmail.com\",\r\n"
				+ "  \"mobile\": \""+mobile+"\"\r\n"
				+ "}";
		Response contactResp = given()
		.contentType(ContentType.JSON)
		.queryParam("campaignId",campaignId )
		.body(contactJsonBody)
		.when()
		.post("/contact");
		contactResp.then().log().all();
		
		String contactID = contactResp.jsonPath().get("contactId");
		row.createCell(5).setCellValue(contactID);
		
		//lead creation
		String company="FireFLink"+randNum;
		String industry="FireFLink industries"+randNum;
		String leadSource="FireFlink lead"+randNum;
		String leadStatus="FireFlink Master";
		String name="Name"+randNum;
		String phone="795648"+randNum;
		String leadJsonBody="{\r\n"
				+ "  \"company\": \""+company+"\",\r\n"
				+ "  \"industry\": \""+industry+"\",\r\n"
				+ "  \"leadSource\": \""+leadSource+"\",\r\n"
				+ "  \"leadStatus\": \""+leadStatus+"\",\r\n"
				+ "  \"name\": \""+name+"\",\r\n"
				+ "  \"phone\": \""+phone+"\"\r\n"
				+ "}";
		
		
		Response leadResp = given()
		.contentType(ContentType.JSON)
		.queryParam("campaignId",campaignId )
		.body(leadJsonBody)
		.when()
		.post("/lead");
		leadResp.then().log().all();
		String leadID=leadResp.jsonPath().get("leadId");
		row.createCell(6).setCellValue(leadID);
		
		
		String opportunityJsonBody="{\r\n"
				+ "  \"nextStep\": \"Checking\",\r\n"
				+ "  \"opportunityName\": \"OPPO\",\r\n"
				+ "  \"probability\": \"50\",\r\n"
				+ "  \"salesStage\": \"JaiBavani\"\r\n"
				+ "}";
		
		Response opportunityResp = given()
		.contentType(ContentType.JSON)
		.queryParam("leadId",leadID )
		.queryParam("campaignId",campaignId )
		.body(opportunityJsonBody)
		.when()
		.post("/opportunity");
		opportunityResp.then().log().all();
		String opportunityID=opportunityResp.jsonPath().get("opportunityId");
		row.createCell(7).setCellValue(opportunityID);
		
		String quoteJsonBody= "{\r\n"
				+ "    \"opportunityId\": \""+opportunityID+"\",\r\n"
				+ "    \"contactId\": \""+contactID+"\",\r\n"
				+ "    \"quotes\": {\r\n"
				+ "        \"quoteId\": \"\",\r\n"
				+ "        \"quoteStage\": \"sfd\",\r\n"
				+ "        \"netTotal\": 99,\r\n"
				+ "        \"shippingAndHandlingCharges\": \"\",\r\n"
				+ "        \"discount\": \"\",\r\n"
				+ "        \"grandTotal\": 99\r\n"
				+ "    },\r\n"
				+ "    \"products\": [\r\n"
				+ "        {\r\n"
				+ "            \"productId\": \""+productID+"\",\r\n"
				+ "            \"productName\": \""+productName+"\",\r\n"
				+ "            \"price\": \"99\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"billingAddress\": {\r\n"
				+ "        \"address\": \"Jayanagar\",\r\n"
				+ "        \"poBox\": \"Jayanagar\",\r\n"
				+ "        \"city\": \"Bangalore\",\r\n"
				+ "        \"state\": \"Karnataka\",\r\n"
				+ "        \"postalCode\": \"560007\",\r\n"
				+ "        \"country\": \"India\"\r\n"
				+ "    },\r\n"
				+ "    \"shippingAddress\": {\r\n"
				+ "        \"address\": \"Jayanagar\",\r\n"
				+ "        \"poBox\": \"Jayanagar\",\r\n"
				+ "        \"city\": \"bangalore\",\r\n"
				+ "        \"state\": \"Karnataka\",\r\n"
				+ "        \"postalCode\": \"560007\",\r\n"
				+ "        \"country\": \"India\"\r\n"
				+ "    },\r\n"
				+ "    \"productQuantities\": {\r\n"
				+ "        \""+productID+"\": 1\r\n"
				+ "    }\r\n"
				+ "}";
		
		Response quoteResp = given()
		.contentType(ContentType.JSON)
		.body(quoteJsonBody)
		.when()
		.post("/quote");
		quoteResp.then().log().all();
		String quoteID=quoteResp.jsonPath().get("quoteId");
		row.createCell(8).setCellValue(quoteID);
		
		
		String purchaseJsonBody="{\r\n"
				+ "    \"contactId\": \""+contactID+"\",\r\n"
				+ "    \"purchaseOrder\": {\r\n"
				+ "        \"orderId\": \"\",\r\n"
				+ "        \"status\": \"Available\",\r\n"
				+ "        \"netTotal\": 99,\r\n"
				+ "        \"shippingAndHandlingCharges\": \"\",\r\n"
				+ "        \"discount\": \"\",\r\n"
				+ "        \"grandTotal\": 99\r\n"
				+ "    },\r\n"
				+ "    \"products\": [\r\n"
				+ "        {\r\n"
				+ "            \"productId\": \""+productID+"\",\r\n"
				+ "            \"productName\": \""+productName+"\",\r\n"
				+ "            \"price\": \"99\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"billingAddress\": {\r\n"
				+ "        \"address\": \"Jayanagar\",\r\n"
				+ "        \"poBox\": \"Jayanagar\",\r\n"
				+ "        \"city\": \"Kolar\",\r\n"
				+ "        \"state\": \"Karnataka\",\r\n"
				+ "        \"postalCode\": \"560085\",\r\n"
				+ "        \"country\": \"India\"\r\n"
				+ "    },\r\n"
				+ "    \"shippingAddress\": {\r\n"
				+ "        \"address\": \"Jayanagar\",\r\n"
				+ "        \"poBox\": \"Jayanagar\",\r\n"
				+ "        \"city\": \"Kolar\",\r\n"
				+ "        \"state\": \"Karnataka\",\r\n"
				+ "        \"postalCode\": \"560022\",\r\n"
				+ "        \"country\": \"India\"\r\n"
				+ "    },\r\n"
				+ "    \"productQuantities\": {\r\n"
				+ "        \""+productID+"\": 1\r\n"
				+ "    }\r\n"
				+ "}";
		
		Response purchaseResp = given()
		.contentType(ContentType.JSON)
		.body(purchaseJsonBody)
		.when()
		.post("/purchase-order");
		purchaseResp.then().log().all();
		String orderID=purchaseResp.jsonPath().get("orderId");
		row.createCell(9).setCellValue(orderID);
		
		
		String salesJsonBody="{\r\n"
				+ "    \"opportunityId\": \""+opportunityID+"\",\r\n"
				+ "    \"contactId\": \""+contactID+"\",\r\n"
				+ "    \"quoteId\": \""+quoteID+"\",\r\n"
				+ "    \"salesOrder\": {\r\n"
				+ "        \"quoteId\": \""+quoteID+"\",\r\n"
				+ "        \"status\": \"InProgress\",\r\n"
				+ "        \"netTotal\": 99,\r\n"
				+ "        \"shippingAndHandlingCharges\": \"\",\r\n"
				+ "        \"discount\": \"\",\r\n"
				+ "        \"grandTotal\": 99\r\n"
				+ "    },\r\n"
				+ "    \"products\": [\r\n"
				+ "        {\r\n"
				+ "            \"productId\": \""+productID+"\",\r\n"
				+ "            \"productName\": \""+productName+"\",\r\n"
				+ "            \"price\": \"99\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"billingAddress\": {\r\n"
				+ "        \"address\": \"Kolar\",\r\n"
				+ "        \"poBox\": \"Jayanagar\",\r\n"
				+ "        \"city\": \"Kolar\",\r\n"
				+ "        \"state\": \"Karnataka\",\r\n"
				+ "        \"postalCode\": \"560085\",\r\n"
				+ "        \"country\": \"India\"\r\n"
				+ "    },\r\n"
				+ "    \"shippingAddress\": {\r\n"
				+ "        \"address\": \"Kolar\",\r\n"
				+ "        \"poBox\": \"Jayanagar\",\r\n"
				+ "        \"city\": \"Kolar\",\r\n"
				+ "        \"state\": \"Karnataka\",\r\n"
				+ "        \"postalCode\": \"560085\",\r\n"
				+ "        \"country\": \"India\"\r\n"
				+ "    },\r\n"
				+ "    \"productQuantities\": {\r\n"
				+ "        \""+productID+"\": 1\r\n"
				+ "    }\r\n"
				+ "}";
		Response salesResp = given()
		.contentType(ContentType.JSON)
		.body(salesJsonBody)
		.when()
		.post("/sales-order");
		salesResp.then().log().all();
		String salesID=salesResp.jsonPath().get("orderId");
		row.createCell(10).setCellValue(salesID);
		
		String invoiceJsonBody="{\r\n"
				+ "    \"billingAddress\": {\r\n"
				+ "        \"addressId\": \"ADD04770\"\r\n"
				+ "    },\r\n"
				+ "    \"contactId\": \""+contactID+"\",\r\n"
				+ "    \"invoice\": {\r\n"
				+ "        \"billingAddress\": {\r\n"
				+ "            \"addressId\": \"ADD04770\"\r\n"
				+ "        },\r\n"
				+ "        \"contact\": {\r\n"
				+ "            \"campaign\": {\r\n"
				+ "                \"campaignId\": \""+campaignId+"\"\r\n"
				+ "            },\r\n"
				+ "            \"contactId\": \""+contactID+"\"\r\n"
				+ "        },\r\n"
				+ "        \"invoiceId\": \"string\",\r\n"
				+ "        \"products\": [\r\n"
				+ "            {\r\n"
				+ "                \"productId\": \""+productID+"\"\r\n"
				+ "      }\r\n"
				+ "    ],\r\n"
				+ "        \"salesOrder\": {\r\n"
				+ "            \"billingAddress\": {\r\n"
				+ "                \"addressId\": \"ADD04770\"\r\n"
				+ "            },\r\n"
				+ "            \"contact\": {\r\n"
				+ "                \"campaign\": {\r\n"
				+ "                    \"campaignId\": \""+campaignId+"\"\r\n"
				+ "                },\r\n"
				+ "                \"contactId\": \""+contactID+"\"\r\n"
				+ "            },\r\n"
				+ "            \"opportunity\": {\r\n"
				+ "                \"lead\": {\r\n"
				+ "                    \"campaign\": {\r\n"
				+ "                        \"campaignId\": \""+campaignId+"\"\r\n"
				+ "                    },\r\n"
				+ "                    \"leadId\": \""+leadID+"\"\r\n"
				+ "                },\r\n"
				+ "                \"opportunityId\": \""+opportunityID+"\"\r\n"
				+ "            },\r\n"
				+ "            \"orderId\": \""+salesID+"\",\r\n"
				+ "            \"products\": [\r\n"
				+ "                {\r\n"
				+ "                    \"productId\": \""+productID+"\"\r\n"
				+ "        }\r\n"
				+ "      ],\r\n"
				+ "            \"quote\": {\r\n"
				+ "                \"billingAddress\": {\r\n"
				+ "                    \"addressId\": \"ADD04770\"\r\n"
				+ "                },\r\n"
				+ "                \"contact\": {\r\n"
				+ "                    \"campaign\": {\r\n"
				+ "                        \"campaignId\": \""+campaignId+"\"\r\n"
				+ "                    },\r\n"
				+ "                    \"contactId\": \""+contactID+"\"\r\n"
				+ "                },\r\n"
				+ "                \"opportunity\": {\r\n"
				+ "                    \"lead\": {\r\n"
				+ "                        \"campaign\": {\r\n"
				+ "                            \"campaignId\": \""+campaignId+"\"\r\n"
				+ "                        },\r\n"
				+ "                        \"leadId\": \""+leadID+"\"\r\n"
				+ "                    },\r\n"
				+ "                    \"opportunityId\": \""+opportunityID+"\"\r\n"
				+ "                },\r\n"
				+ "                \"products\": [\r\n"
				+ "                    {\r\n"
				+ "                        \"productId\": \""+productID+"\"\r\n"
				+ "          }\r\n"
				+ "        ],\r\n"
				+ "                \"quoteId\": \""+quoteID+"\",\r\n"
				+ "                \"shippingAddress\": {\r\n"
				+ "                    \"addressId\": \"ADD04770\"\r\n"
				+ "                }\r\n"
				+ "            },\r\n"
				+ "            \"shippingAddress\": {\r\n"
				+ "                \"addressId\": \"ADD04770\"\r\n"
				+ "            }\r\n"
				+ "        },\r\n"
				+ "        \"shippingAddress\": {\r\n"
				+ "            \"addressId\": \"ADD04770\"\r\n"
				+ "        }\r\n"
				+ "    },\r\n"
				+ "    \"orderId\": \""+salesID+"\",\r\n"
				+ "    \"productQuantities\": {\r\n"
				+ "        \""+productID+"\": 1\r\n"
				+ "    },\r\n"
				+ "    \"products\": [\r\n"
				+ "        {\r\n"
				+ "            \"productId\": \""+productID+"\"\r\n"
				+ "    }\r\n"
				+ "  ],\r\n"
				+ "    \"shippingAddress\": {\r\n"
				+ "        \"addressId\": \"ADD04770\"\r\n"
				+ "    }\r\n"
				+ "}";
		Response invoiceResp = given()
		.contentType(ContentType.JSON)
		.body(invoiceJsonBody)
		.when()
		.post("/invoice");
		invoiceResp.then().log().all();
		invoiceID=invoiceResp.jsonPath().get("invoiceId");
		row.createCell(11).setCellValue(invoiceID);
        // ====================== WRITE EXCEL ======================
        FileOutputStream fout = new FileOutputStream(f);
        wb.write(fout);
        fout.close();
        wb.close();
        driver.quit();
        

    }
    // ====================== MOBILE AUTOMATION ======================
    @Test(dependsOnMethods = "crmFullFlow")
    public void CRM_MobileAutomation() throws IOException, InterruptedException {
        DesiredCapabilities mobileCaps = new DesiredCapabilities();
        mobileCaps.setCapability("platformName", "Android");
        mobileCaps.setCapability("deviceName", "emulator-5554");
        mobileCaps.setCapability("automationName", "UiAutomator2");
        mobileCaps.setCapability("appium:appPackage", "com.acoe.logistics_app");
        mobileCaps.setCapability("appium:appActivity", "com.acoe.logistics_app.MainActivity");
        mobileCaps.setCapability("appium:noReset", true);
        mobileCaps.setCapability("appium:forceAppLaunch", true);

        AndroidDriver mobileDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), mobileCaps);
        mobileDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

        mobileDriver.findElement(By.id("com.acoe.logistics_app:id/usernameEditText")).sendKeys("rmgyantra");
        mobileDriver.findElement(By.id("com.acoe.logistics_app:id/passwordEditText")).sendKeys("rmgy@9999");
        mobileDriver.findElement(By.id("com.acoe.logistics_app:id/loginButton")).click();

        mobileDriver.findElement(By.id("com.acoe.logistics_app:id/searchInput")).sendKeys(invoiceID);
        String mobileText = mobileDriver.findElement(By.id("com.acoe.logistics_app:id/invoice_id")).getText();
        Assert.assertEquals(mobileText, invoiceID);
        mobileDriver.quit();
    }
    // ====================== DESKTOP AUTOMATION ======================
    
    @Test(dependsOnMethods = "CRM_MobileAutomation")
    public void CRM_DesktopAutomation() throws InterruptedException, MalformedURLException {
        DesiredCapabilities desktopCaps = new DesiredCapabilities();
        desktopCaps.setCapability("platformName", "Windows");
        desktopCaps.setCapability("deviceName", "WindowsPC");
        desktopCaps.setCapability("app", "C:\\Program Files\\brm-app\\brm-app.exe");
        desktopCaps.setCapability("newCommandTimeout", 300);

        WindowsDriver desktopDriver = new WindowsDriver(new URL("http://127.0.0.1:4723/"), desktopCaps);
        Thread.sleep(3000);
        
        String un = "rmgyantra";
        for(int i= 0;i<=un.length()-1;i++)
        {
            String username = ""+un.charAt(i);
            desktopDriver.findElement(AppiumBy.accessibilityId("username")).sendKeys(username);
        }
        String pw = "rmgy@9999";
        for(int i= 0;i<=pw.length()-1;i++)
        {
            String password = ""+pw.charAt(i);
            desktopDriver.findElement(AppiumBy.accessibilityId("password")).sendKeys(password);             
        }

        desktopDriver.findElement(AppiumBy.accessibilityId("loginButton")).click();
        Thread.sleep(4000);

        desktopDriver.findElement(AppiumBy.name("Invoices")).click();
        Thread.sleep(3000);
        desktopDriver.findElement(AppiumBy.accessibilityId("refreshInvoices")).click();
        Thread.sleep(3000);

        String desktopText = desktopDriver.findElement(By.name(invoiceID)).getText();
        System.out.println("Desktop Invoice: " + desktopText);

        desktopDriver.quit();
    }
}

