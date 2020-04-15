package test.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import test.utils.SeleniumUtils;

public class PO_PrivateView extends PO_NavView{
	
	/**
	 * navigates to selected page
	 * 
	 * @param driver
	 * @param page
	 */
	public static void goToPage(WebDriver driver, int page) {
		// find page buttons
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "@href", "?page=", PO_View.getTimeout());
		elements.remove(0); // remove "first"
		elements.remove(elements.size() - 1); // remove "last"
		// click corresponding one
		elements.get(page).click();
	}
	

	/**
	 * logout of the system
	 * 
	 * @param driver
	 */
	public static void logout(WebDriver driver) {
		clickOption(driver, "logout", "class", "btn btn-primary");
	}

}
