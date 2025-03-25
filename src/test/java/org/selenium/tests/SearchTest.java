package org.selenium.tests;

import org.selenium.base.BaseTest;
import org.selenium.pages.StorePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchTest extends BaseTest {

	@Test(groups = { "SANITY", "BVT", "REGRESSION" })
	public void searchWithPartialMatch() {

		String searchFor = "Blue";
		StorePage storePage = new StorePage(getDriver()).load().enterTextInSearchField(searchFor).clickSearchBtn();

		Assert.assertEquals(storePage.getTitle(), "Search results: “" + searchFor + "”");
	}

}
