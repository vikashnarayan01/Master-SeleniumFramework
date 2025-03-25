package org.selenium.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.selenium.components.ProductThumbnail;
import org.selenium.enums.WaitStrategy;

public class StorePage extends BasePage {

	private ProductThumbnail productThumbnail;

	public StorePage(WebDriver driver) {
		super(driver);
		productThumbnail = new ProductThumbnail(driver);
	}

	@Step
	public ProductThumbnail getProductThumbnail() {
		return productThumbnail;
	}

	public Boolean isLoaded() {
		return wait.until(ExpectedConditions.urlContains("/store"));
	}

	private final By vaibav = By.id("woocommerce-product-search-field-0");
	private final By searchBtn = By.xpath("//button[normalize-space()='Search']");
	private final By title = By.xpath("//h1[@class='woocommerce-products-header__title page-title']");

	/* Builder Pattern - used for Method Chaining */
	/* Structural Page Object */
	/* 1 method will do 1 operation --> sendKeys() */
	public StorePage enterTextInSearchField(String txt) {
		sendKeys(vaibav, txt, WaitStrategy.PRESENCE, "Search Field");
		return this;
	}

	/* Functional Page Object */
	/* Represents a Functionality */
	/* 1 method will do 2 operations --> sendKeys(), click() */
	public StorePage search(String txt) {
		search(txt).clickSearchBtn();
		return this;
	}

	@Step
	public StorePage load() {
		load("store");
		return this;
	}

	public StorePage clickSearchBtn() {
		click(searchBtn, WaitStrategy.CLICKABLE, "Search Button");
		return this;
	}

	public String getTitle() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText();
	}
}
