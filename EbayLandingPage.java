package com.fis.global.ebayPage;

import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.fis.global.testBase.TestBase;

public class EbayLandingPage extends TestBase {

	WebDriver driver;

	public EbayLandingPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[@id = 'gh-ac']")
	public WebElement searchTextBox;

	@FindBy(xpath = "//input[@id = 'gh-btn']")
	public WebElement searchButton;

	@FindBy(xpath = "(//ul[@class='srp-results srp-list clearfix']//span[@role='heading'])[1]")
	public WebElement firstBook;

	@FindBy(xpath = "//span[text() = 'Add to cart']")
	public WebElement addToCart;

	@FindBy(xpath = "//i[text()='1']")
	public WebElement cartValue;

	public void launchAndValidate() throws InterruptedException {
		driver.get("https://ebay.com");
		Thread.sleep(3000);
		this.searchTextBox.click();
		this.searchTextBox.sendKeys("book");
		this.searchButton.click();
		String mainTabHandle = driver.getWindowHandle();
		this.firstBook.click();

		Set<String> windowHandles = driver.getWindowHandles();
		for (String s : windowHandles) {
			if (!s.equals(mainTabHandle)) {
				driver.switchTo().window(s); // Switch to the new tab
				break;
			}
		}
		
		this.addToCart.click();
		String value = this.cartValue.getText();
		Assert.assertEquals(value, "1");
	}

}
