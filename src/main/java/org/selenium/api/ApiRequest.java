package org.selenium.api;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.commons.io.output.WriterOutputStream;
import org.selenium.reports.ExtentLogger;
import org.selenium.utils.ConfigLoader;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.selenium.constants.FrameworkConstants.YES;

public class ApiRequest extends SpecBuilder {

	@SuppressWarnings("deprecation")
	public static Response post(String endPoint, Headers headers, HashMap<String, Object> formParams, Cookies cookies) {

		StringWriter writerRequest;
		PrintStream captor;
		writerRequest = new StringWriter();
		captor = new PrintStream(new WriterOutputStream(writerRequest), true);

		Response response = given(getRequestSpec()).filter(new RequestLoggingFilter(captor)).headers(headers)
				.formParams(formParams).cookies(cookies).when().post(endPoint).then().spec(getResponseSpec()).extract()
				.response();

		printDetailsInExtentReport(writerRequest, response);
		return response;
	}

	private static void printDetailsInExtentReport(StringWriter writer, Response response) {
		if (ConfigLoader.getInstance().getRequestDetailsInReports().equalsIgnoreCase(YES)) {
			ExtentLogger.info("<details><summary><i><font color=black> Request details: </font></i>" + "</summary>"
					+ "<pre>" + writer.toString() + "</pre>" + "</details> \n");
		}
	}

	@SuppressWarnings("deprecation")
	public static Response get(String endPoint, Cookies cookies) {

		StringWriter writerRequest;
		PrintStream captor;
		writerRequest = new StringWriter();
		captor = new PrintStream(new WriterOutputStream(writerRequest), true);

		Response response = given(getRequestSpec()).filter(new RequestLoggingFilter(captor)).cookies(cookies).when()
				.get(endPoint).then().spec(getResponseSpec()).extract().response();

		printDetailsInExtentReport(writerRequest, response);
		return response;

	}
}
