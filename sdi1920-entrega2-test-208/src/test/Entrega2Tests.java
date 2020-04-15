package test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.runners.MethodSorters;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import test.pageobjects.PO_HomeView;
import test.pageobjects.PO_LoginView;
import test.pageobjects.PO_RegisterView;
import test.pageobjects.PO_View;
import test.utils.SeleniumUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Entrega2Tests {

	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver024 = new File("drivers/geckodriver024win64.exe").getAbsolutePath();

	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "http://localhost:8081";
	
	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}
	
	@Before
	public void setUp() throws Exception {
		// antes de cada prueba navega a la url home de la app
		driver.navigate().to(URL);
	}

	@After
	public void tearDown() throws Exception {
		// despues de cada prueba se borran las cookies del navegador
		driver.manage().deleteAllCookies();
	}
	
	@BeforeClass
	static public void begin() {

	}

	@AfterClass
	static public void end() {
		// al finalizar la ultima prueba
		// cerramos el navegador al finalizar las pruebas
		driver.quit();
	}
	
	/**
	 * Registro de usuario con datos válidos
	 */
	@Test
	public void PR01() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "user8@email.com", "TestName", "TestSurname", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_View.checkElement(driver, "text", "Bienvenidos a la página principal");
	}
	
	

	/**
	 * Registro de usuario con datos inválidos email vacio, nombre vacio, apellidos
	 * vacios
	 */
	@Test
	public void PR02() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");

		PO_RegisterView.fillForm(driver, "", "TestName", "TestSurname", "123456", "123456");
		// COmprobamos el error de email vacio.
		PO_RegisterView.checkKey(driver, "Error.empty", PO_Properties.getSPANISH());

		PO_RegisterView.fillForm(driver, "user123123@email.com", "", "Perez", "77777", "77777");
		// COmprobamos el error de Nombre corto .
		PO_RegisterView.checkKey(driver, "Error.empty", PO_Properties.getSPANISH());

		PO_RegisterView.fillForm(driver, "user123123@email.com", "nombre", "", "77777", "77777");
		// COmprobamos el error de Nombre corto .
		PO_RegisterView.checkKey(driver, "Error.empty", PO_Properties.getSPANISH());

	}
	
	/**
	 * Registro de usuario con datos inválidos repeticion de contraseña inválida
	 */
	@Test
	public void PR03() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "user123@email.com", "TestName", "TestSurname", "123456", "1");
		PO_View.getP();
		// COmprobamos el error de contraseña invalida
		PO_RegisterView.checkKey(driver, "Error.signup.passwordConfirm.coincidence", PO_Properties.getSPANISH());
	}
	

	/**
	 * Registro de usuario con datos inválidos email existente
	 */
	@Test
	public void PR04() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "user1@email.com", "TestName", "TestSurname", "123456", "123456");
		PO_View.getP();
		// COmprobamos el error de DNI repetido.
		PO_RegisterView.checkKey(driver, "Error.signup.email.duplicate", PO_Properties.getSPANISH());
	}
	

	/**
	 * Inicio de sesion con datos validos (usuario estandar)
	 */
	@Test
	public void PR05() {
		loginStandardUser();
	}
	
	/**
	 * Inicio de sesion con datos invalidos usuario estandar, campo email y
	 * contraseña vacios
	 */
	@Test
	public void PR06() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

		PO_LoginView.fillForm(driver, "", "123456");
		// Comprobamos que se muestra el error
		PO_RegisterView.checkKey(driver, "login.error", PO_Properties.getSPANISH());

		PO_LoginView.fillForm(driver, "user1@email.com", "");
		// Comprobamos que se muestra el error
		PO_RegisterView.checkKey(driver, "login.error", PO_Properties.getSPANISH());
	}
	
	/**
	 * Inicio de sesion con datos validos usuario estandar, email existente, pero
	 * contraseña incorrecta
	 */
	@Test
	public void PR07() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "user1@email.com", "123456234234");
		// Comprobamos que se muestra el error
		PO_RegisterView.checkKey(driver, "login.error", PO_Properties.getSPANISH());
	}
	
	/**
	 * Inicio de sesión con datos inválidos (usuario estándar, email no existentey contraseña no vacía).
	 * 
	 */
	@Test
	public void PR08() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "user1@email.com", "123456234234");
		// Comprobamos que se muestra el error
		PO_RegisterView.checkKey(driver, "login.error", PO_Properties.getSPANISH());
	}
	
	/**
	 * Fin de sesion hacer click en la opcion de salir de sesion y comprobar que se
	 * redirige a la pagina de inicio de sesion
	 */
	@Test
	public void PR09() {
		loginStandardUser();

		PO_LoginView.clickOption(driver, "logout", "class", "btn btn-primary");
		PO_View.checkKey(driver, "login.message", PO_Properties.getSPANISH());

	}
	
	/**
	 * Fin de sesion Comprobar que el botón cerrar sesion no está visible si el
	 * usuario no está autenticado
	 */
	@Test
	public void PR10() {
		SeleniumUtils.textoNoPresentePagina(driver, "Desconectar");

	}
	
	/**
	 * Mostrar el listado de usuarios y comprobar que se muestran todos los que
	 * existen en el sistema
	 */
	@Test
	public void PR11() {
		loginStandardUser();

		goToListUsers();

		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
		assertEquals(usuarios.size(), 5);

		PO_PrivateView.clickOption(driver, "logout", "text", "Identifícate");
	}
	
	/**
	 * Hacer  una  búsqueda  con  el  campo  vacío  y  comprobar  que  se  muestra  la  página  
	 * que corresponde con el listado usuarios existentes en el sistema.
    */
	@Test
	public void PR12() {
		// log in as standard user
		loginStandardUser();
		// navigate to users list
		goToListUsers();
		// search without any text
		PO_PrivateView.searchInUsersList(driver, "");
		// get the number of users
		int numOfUsers = PO_PrivateView.getTotalNumOfElements(driver);
		assertEquals(6, numOfUsers);

		// logout
		PO_PrivateView.logout(driver);

		// log in as admin
		loginAdmin();
		// navigate to users list
		goToListUsers();
		// search without any text
		PO_PrivateView.searchInUsersList(driver, "");
		// get the number of users
		numOfUsers = PO_PrivateView.getTotalNumOfElements(driver);
		assertEquals(8, numOfUsers);
	}
	
	/**
	 * Hacer  una  búsqueda  escribiendo  en  el  campo  un  texto  que  no  exista 
	 *  y  comprobar  que  se muestra la página que corresponde, con la lista de usuarios vacía
	 */
	@Test
	public void PR13() {
		// login as standard user
		loginStandardUser();
		// go to list of users
		goToListUsers();
		// search with non-existing text
		PO_PrivateView.searchInUsersList(driver, "noexisto");
		// check no user shows up
		List<WebElement> users = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(0, users.size());

		// logout
		PO_PrivateView.logout(driver);

		// login as standard user
		loginAdmin();
		// go to list of users
		goToListUsers();
		// search with non-existing text
		PO_PrivateView.searchInUsersList(driver, "noexisto");
		// check no user shows up
		users = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(0, users.size());
	}
	
	/**
	 * Hacer  una  búsquedacon  un texto  específico y  comprobar  que  se 
	 *  muestra  la  página  que corresponde, con la lista de usuarios en los
	 *  que el textoespecificados sea parte de su nombre, apellidos o de su email.
	 */
	@Test
	public void PR14() {
		// login as standard user
		loginStandardUser();
		// go to users list
		goToListUsers();
		// search a name that exists
		PO_PrivateView.searchInUsersList(driver, "ed");
		// check pedro was returned
		List<WebElement> users = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(1, users.size());
		// search a surname that exists
		PO_PrivateView.searchInUsersList(driver, "ez");
		// check maria, pedro and lucas were returned
		users = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(3, users.size());
		// search valid email
		PO_PrivateView.searchInUsersList(driver, "6");
		// check that carlos is returned
		users = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(1, users.size());
		
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Intentar acceder sin estar autenticado a la opcion de listado de usuarios Se
	 * debera volver al formulario de login
	 *
	 */
	@Test
	public void PR20() {
		driver.navigate().to("http://localhost:80810/user/list");
		PO_View.checkElement(driver, "text", "Identifícate");
		String currentURL = driver.getCurrentUrl();
		assertEquals(currentURL, "http://localhost:8090/login");
	}
	
	/**
	 * Intentar acceder sin estar autenticado a la opción de listado de invitaciones
	 *  de amistad recibida de un usuario estándar. 
	 *  Se deberá volver al formulario de login.
	 */
	@Test
	public void PR21() {
		driver.navigate().to("http://localhost:8081/invitaciones");
		PO_View.checkElement(driver, "text", "Identifícate");
		String currentURL = driver.getCurrentUrl();
		assertEquals(currentURL, "http://localhost:8081/login");
	}
	
	/**
	 * Intentar  acceder estando  autenticado  como  usuario  standard  a
	 *  la  lista  de  amigos  de  otro usuario. Se deberá mostrar un mensaje de acción indebida.
	 *
	 */
	@Test
	public void PR22() {
		driver.navigate().to("http://localhost:80810/user/list");
		PO_View.checkElement(driver, "text", "Identifícate");
		String currentURL = driver.getCurrentUrl();
		assertEquals(currentURL, "http://localhost:8090/login");
	}
	
	
	
	
	private void loginStandardUser() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "user1@email.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		PO_View.checkElement(driver, "text", "Esta es una zona privada la web");
	}
	

	private void loginAs(int userId) {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "user" + userId + "@email.com", "123456");
		// COmprobamos que entramos en la pagina privada de Alumno
		PO_View.checkElement(driver, "text", "Esta es una zona privada la web");
	}
	

	private void goToListUsers() {
		// Pinchamos en la opción de menu de usuarios: //li[contains(@id,
		// 'marks-menu')]/a
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id, 'users-menu')]/a");
		elementos.get(0).click();
		// Esperamos a aparezca la opción de ver usuarios: //a[contains(@href,
		// 'mark/add')]
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'user/list')]");
		// Pinchamos en agregar Nota.
		elementos.get(0).click();
	}
}
