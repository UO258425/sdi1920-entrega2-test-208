package test.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import test.utils.SeleniumUtils;

public class PO_PrivateView extends PO_NavView {

	/**
	 * navigates to selected page
	 * 
	 * @param driver
	 * @param page
	 */
	public static void goToPage(WebDriver driver, int page) {
		// find page buttons
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "@href", "?pg=", PO_View.getTimeout());
		
		// click corresponding one
		elements.get(page-1).click();
	}

	/**
	 * logout of the system
	 * 
	 * @param driver
	 */
	public static void logout(WebDriver driver) {
		clickOption(driver, "logout", "class", "btn btn-primary");
	}

	/**
	 * searches in the user list for the input searchText
	 * 
	 * @param driver
	 * @param searchText
	 */
	public static void searchInUsersList(WebDriver driver, String searchText) {
		// find the search input
		WebElement toSearch = driver.findElement(By.name("busqueda"));
		// find the search button
		WebElement btnSearch = driver.findElement(By.className("btn"));
		// fill the search input with the text to search
		toSearch.click();
		toSearch.clear();
		toSearch.sendKeys(searchText);
		// press the search button
		btnSearch.click();
	}

	/**
	 * searches in the user list for the input searchText
	 * 
	 * @param driver
	 * @param searchText
	 */
	public static void searchInUsersListCliente(WebDriver driver, String searchText) {
		// find the search input
		WebElement toSearch = driver.findElement(By.id("filtro-nombre"));
		// fill the search input with the text to search
		toSearch.click();
		toSearch.clear();
		toSearch.sendKeys(searchText);
	}

	/**
	 * writes a text message in a chat
	 * 
	 * @param driver
	 * @param searchText
	 */
	public static void sendMessage(WebDriver driver, String messageText) {
		WebElement text = driver.findElement(By.id("input-mensaje"));
		WebElement btnSend = driver.findElement(By.id("btn-enviar"));
		text.click();
		text.clear();
		text.sendKeys(messageText);
		btnSend.click();
	}

	/**
	 * counts the total number of elements in all pages
	 * 
	 * @param driver
	 * @return
	 */
	public static int getTotalNumOfElements(WebDriver driver) {
		// get the number of pages
		int numOfPages = getNumOfPages(driver);
		int count = 0;
		List<WebElement> elements;
		// for each page count number of users
		for (int i = 0; i < numOfPages; i++) {
			PO_PrivateView.goToPage(driver, i+1);
			elements = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
			count += elements.size();
		}
		return count;
	}

	/**
	 * returns the number of current pages
	 * 
	 * @param driver
	 * @return number of pages
	 */
	public static int getNumOfPages(WebDriver driver) {
		// find page buttons
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "@href", "?pg=", PO_View.getTimeout());
		return elements.size() ; // take into account first and last shouldn't be counted
	}

	/**
	 * returns all elements in the current displayed list
	 * 
	 * @param driver
	 * @return a list of web elements
	 */
	public static List<WebElement> getAllElementsInList(WebDriver driver) {
		By element = By.xpath("//tbody/tr");
		return driver.findElements(element);
	}

}
