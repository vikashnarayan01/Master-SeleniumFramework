package org.selenium.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.enums.WaitStrategy;
import org.selenium.pages.BasePage;
import org.selenium.pages.StorePage;

public class MyHeader extends BasePage {

	public MyHeader(WebDriver driver) {
		super(driver);
	}

	private final By storeMenuLink = By.xpath("//li[@id='menu-item-1227']//a");

	/* Fluent Interface */
	public StorePage navigateToStoreUsingMenu() {
		click(storeMenuLink, WaitStrategy.CLICKABLE, "Store Menu Link");
		return new StorePage(driver);
	}

}
