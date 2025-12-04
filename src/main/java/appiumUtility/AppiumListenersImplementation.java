package appiumUtility;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import genericUtilities.JavaUtility;
import seleniumUtility.WebDriverUtility;

public class AppiumListenersImplementation implements ITestListener{
	ExtentReports report;
	ExtentSparkReporter repoter;
	ExtentTest test;
	@Override
	public void onTestStart(ITestResult result) {	
		String methodName = result.getMethod().getMethodName();
		test=report.createTest(methodName);
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		test.log(Status.PASS, methodName+" " +" --> Test Execution is Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		test.log(Status.FAIL, methodName+" "+" --> Test Execution is Failed");
		test.log(Status.INFO, "Test Failed Due to "+result.getThrowable());
		WebDriverUtility wUtil = new WebDriverUtility();
		try {
			String path = wUtil.captureScreenShot(AppiumBaseClass.sdriver, methodName);
			test.addScreenCaptureFromPath(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		test.log(Status.SKIP, methodName+" "+" --> Test Execution is Skipped");
	}
	

	@Override
	public void onStart(ITestContext context) {
		repoter = new ExtentSparkReporter(".\\ExtentReport\\ExtentReport Mobile"+new JavaUtility().getCurrentDateAndTime()+".html");
		repoter.config().setTheme(Theme.DARK);
		repoter.config().setDocumentTitle("Automation Report");
		repoter.config().setReportName("Automation Execution Report");

		report = new ExtentReports();
		report.attachReporter(repoter);
		report.setSystemInfo("Base Platform", "Android");
		report.setSystemInfo("Base URL", "Test Env");
		report.setSystemInfo("Reporter Name", "Saiteja");
	}

	@Override
	public void onFinish(ITestContext context) {
		report.flush();
	}

}

