package com.fis.global.testcase;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fis.global.ebayPage.EbayLandingPage;
import com.fis.global.testBase.TestBase;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class EbayTest extends TestBase {

	WebDriver driver;

	@BeforeMethod
	public void launchBrowser() {
		try {
			this.driver = instatiateWebdriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void verifyCartValue() throws InterruptedException {
		EbayLandingPage ebayLandingPage = new EbayLandingPage(driver);
		ebayLandingPage.launchAndValidate();
	}

	@Test
	public void verifyResponse() {
		Response res = RestAssured.given().when().get("https://api.coindesk.com/v1/bpi/currentprice.json");
		 JsonPath jsonPath = res	.jsonPath();
		 Map<String, Object> bpi = jsonPath.get("bpi");

		 Iterator<String> keys = bpi.keySet().iterator();
		boolean hasUSD = false, hasGBP = false, hasEUR = false;
		boolean isGBPDescriptionCorrect = false;

		// Step 2: Iterate over the keys and check the conditions
		while (keys.hasNext()) {
            String key = keys.next();
            Map<String, Object> currencyNode = (Map<String, Object>) bpi.get(key);

            // Check for USD, GBP, and EUR
            if ("USD".equals(key)) {
                hasUSD = true;
            } else if ("GBP".equals(key)) {
                hasGBP = true;
                // Check if the GBP description is correct
                String gbpDescription = (String) currencyNode.get("description");
                if ("British Pound Sterling".equals(gbpDescription)) {
                    isGBPDescriptionCorrect = true;
                }
            } else if ("EUR".equals(key)) {
                hasEUR = true;
            }
        }

        // Step 3: Final validation
        if (hasUSD && hasGBP && hasEUR) {
            System.out.println("Validation passed: 'bpi' contains 3 nodes (USD, GBP, EUR).");
        } else {
            System.out.println("Validation failed: 'bpi' does not contain all required keys (USD, GBP, EUR).");
        }

        if (isGBPDescriptionCorrect) {
            System.out.println("GBP description is correct: British Pound Sterling");
        } else {
            System.out.println("Validation failed: GBP description is incorrect.");
        }

	}

}
