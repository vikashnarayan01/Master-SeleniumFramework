package org.selenium.listeners;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.selenium.reports.ExtentLogger;
import org.selenium.reports.ExtentReport;
import org.selenium.utils.BrowserOSInfoUtils;
import org.selenium.utils.EmailSendUtils;
import org.selenium.utils.IconUtils;
import org.selenium.utils.ZipUtils;
import org.testng.*;

import java.util.Arrays;

import static org.selenium.constants.FrameworkConstants.*;

public class ListenerClass implements ITestListener, ISuiteListener {

	static int count_passedTCs;
	static int count_skippedTCs;
	static int count_failedTCs;
	static int count_totalTCs;

	@Override
	public void onStart(ISuite suite) {
		ExtentReport.initReports();
	}

	@Override
	public void onFinish(ISuite suite) {
		ExtentReport.flushReports();
		ZipUtils.zip();
		EmailSendUtils.sendEmail(count_totalTCs, count_passedTCs, count_failedTCs, count_skippedTCs);
	}

	@Override
	public void onTestStart(ITestResult result) {

		count_totalTCs = count_totalTCs + 1;
		ExtentReport.createTest(result.getMethod().getMethodName());

		ExtentReport.addDevices();
		ExtentLogger.info("<b>" + IconUtils.getOSIcon() + "  &  " + IconUtils.getBrowserIcon() + " --------- "
				+ BrowserOSInfoUtils.getOS_Browser_BrowserVersionInfo() + "</b>");

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		count_passedTCs = count_passedTCs + 1;
		String logText = "<b>" + result.getMethod().getMethodName() + " is passed.</b>" + "  " + ICON_SMILEY_PASS;
		Markup markup_message = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		ExtentLogger.pass(markup_message);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		count_failedTCs = count_failedTCs + 1;
		ExtentLogger.fail(ICON_BUG + "  " + "<b><i>" + result.getThrowable().toString() + "</i></b>");
		String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
		String message = "<details><summary><b><font color=red> Exception occured, click to see details: "
				+ ICON_SMILEY_FAIL + " </font></b>" + "</summary>" + exceptionMessage.replaceAll(",", "<br>")
				+ "</details> \n";

		ExtentLogger.fail(message);

		String logText = "<b>" + result.getMethod().getMethodName() + " is failed.</b>" + "  " + ICON_SMILEY_FAIL;
		Markup markup_message = MarkupHelper.createLabel(logText, ExtentColor.RED);
		ExtentLogger.fail(markup_message, true);

	}

	@Override
	public void onTestSkipped(ITestResult result) {

		count_skippedTCs = count_skippedTCs + 1;

		ExtentLogger.skip(ICON_BUG + "  " + "<b><i>" + result.getThrowable().toString() + "</i></b>");
		String logText = "<b>" + result.getMethod().getMethodName() + " is skipped.</b>" + "  " + ICON_SMILEY_FAIL;
		Markup markup_message = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
		ExtentLogger.skip(markup_message, true);

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		/*
		 * As of now, we are not using it
		 */
	}

	@Override
	public void onStart(ITestContext result) {
		/*
		 * As of now, we are not using it.
		 */
	}

	@Override
	public void onFinish(ITestContext result) {
		/*
		 * As of now, we are not using it.
		 */
	}

}
