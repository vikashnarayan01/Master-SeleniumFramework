package org.selenium.base;

import io.restassured.http.Cookies;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.selenium.driver.DriverManager;
import org.selenium.driver.DriverManagerFactory;
import org.selenium.enums.DriverType;
import org.selenium.listeners.AnnotationTransformer;
import org.selenium.listeners.ListenerClass;
import org.selenium.listeners.MethodInterceptor;
import org.selenium.reports.ExtentLogger;
import org.selenium.utils.CookieUtils;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Listeners({ AnnotationTransformer.class, ListenerClass.class, MethodInterceptor.class })
public class BaseTest {

	protected WebDriver getDriver() {
		return DriverManager.getDriver();
	}

	private void setDriver(WebDriver driver) {
		DriverManager.setDriver(driver);
	}

	/*
	 * @Optional -> You can run the test case individually directly from Java class
	 */
	@SuppressWarnings("deprecation")
	@Parameters("browser")
	@BeforeMethod
	public synchronized void startDriver(@Optional String browser) {
		browser = setBrowserValue(browser);
		setDriver(DriverManagerFactory.getManager(DriverType.valueOf(browser)).createDriver());
		System.out.println("Current Thread info = " + Thread.currentThread().getId() + ", Driver = " + getDriver());
	}

	@Parameters("browser")
	@AfterMethod
	public synchronized void quitDriver(@Optional String browser, ITestResult result) throws IOException {

		takeScreenshotOnTestFailure(browser, result);
		getDriver().quit();

	}

	@SuppressWarnings("deprecation")
	private void takeScreenshotOnTestFailure(String browser, ITestResult result) throws IOException {
		browser = setBrowserValue(browser);
		System.out.println("Current Thread info = " + Thread.currentThread().getId() + ", Driver = " + getDriver());

		if (result.getStatus() == ITestResult.FAILURE) {
			File destFile = new File("Screenshots" + File.separator + browser + File.separator
					+ result.getTestClass().getRealClass().getSimpleName() + "_" + result.getMethod().getMethodName()
					+ ".png");
			takeScreenshot(destFile);
		}
	}

	public void injectCookiesToBrowser(Cookies restAssuredCookies) {
		List<Cookie> seleniumCookies = new CookieUtils().convertRestAssuredCookiesToSeleniumCookies(restAssuredCookies);
		for (Cookie seleniumCookie : seleniumCookies) {
			getDriver().manage().addCookie(seleniumCookie);
		}
		ExtentLogger.info(
				"<i> <b>Injecting Selenium Cookies to Browser </b>(by converting RestAssured Cookies to Selenium Cookies) </i>");

		ExtentLogger.info("<details><summary><i><font color=black> RestAssured Cookies: </font></i>" + "</summary>"
				+ "<pre>" + restAssuredCookies + "</pre>" + "</details> \n");
		ExtentLogger.info("<details><summary><i><font color=black> Selenium Cookies: </font></i>" + "</summary>"
				+ "<pre>" + seleniumCookies + "</pre>" + "</details> \n");

	}

	private void takeScreenshot(File destFile) throws IOException {
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFile, destFile);
	}

	@SuppressWarnings("unused")
	private void takeScreenshotUsingAshot(File destFile) throws IOException {
		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
				.takeScreenshot(getDriver());
		try {
			ImageIO.write(screenshot.getImage(), "PNG", destFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private String setBrowserValue(String browser) {
		/*
		 * System.getProperty("browser" -> This is for test execution using Maven
		 * Command Line file
		 */
		note();

		/* This is for test case execution individually from Java class */
		if (browser == null) {
			browser = "EDGE";
			System.out.println(
					"Test execution not done by Maven cmd or TestNG.xml file ->  setting the value: " + "EDGE");
		}

		/* This is for test case execution from Maven command line or testng.xml file */
		browser = System.getProperty("browser", browser);
		return browser;
	}

	private void note() {
		// String browser = System.getProperty("browser");
		/* DriverType.CHROME.toString() -> Default Browser */
		/*
		 * We can run the test cases even as TestNG suite - not mandatory to run from
		 * Maven command line
		 */
		/** This is for test execution using Maven command line */
		// String browser = System.getProperty("browser", DriverType.CHROME.toString());

		/** This is for test execution using testng.xml file */
		// String browser = browserFromTestNG;
	}

}