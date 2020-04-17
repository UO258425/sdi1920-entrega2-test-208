package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.runners.MethodSorters;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Sleeper;

import test.pageobjects.PO_LoginView;
import test.pageobjects.PO_PrivateView;
import test.pageobjects.PO_RegisterView;
import test.pageobjects.PO_View;
import test.utils.SeleniumUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Entrega2Tests {

	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver024 = new File("drivers/geckodriver024win64.exe").getAbsolutePath();

	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "https://localhost:8081";

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
		driver.findElement(By.xpath("//a[@href='/signin']")).click();
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "prueba12@prueba12", "nombre12", "apellidos12", "prueba12", "prueba12");
		// PO_View.checkElement(driver, "text", "Nuevo usuario registrado");
		SeleniumUtils.textoPresentePagina(driver, "Nuevo usuario registrado");
	}

	/**
	 * Registro de usuario con datos inválidos email vacio, nombre vacio, apellidos
	 * vacios
	 */
	@Test
	public void PR02() {
		// Vamos al formulario de registro
		driver.findElement(By.xpath("//a[@href='/signin']")).click();

		PO_RegisterView.fillForm(driver, "", "TestName", "TestSurname", "123456", "123456");
		// COmprobamos el error de email vacio.
		// PO_RegisterView.checkKey(driver, "Error al registrar usuario: email vacio");
		SeleniumUtils.textoPresentePagina(driver, "Error al registrar usuario: email vacio");

		PO_RegisterView.fillForm(driver, "user123123@email.com", "", "Perez", "77777", "77777");
		// COmprobamos el error de Nombre corto .
		// PO_RegisterView.checkKey(driver, "Error al registrar usuario: nombre vacio");
		SeleniumUtils.textoPresentePagina(driver, "Error al registrar usuario: nombre vacio");

		PO_RegisterView.fillForm(driver, "user123123@email.com", "nombre", "", "77777", "77777");
		// COmprobamos el error de Nombre corto .
		// PO_RegisterView.checkKey(driver, "Error al registrar usuario: apellidos
		// vacios");
		SeleniumUtils.textoPresentePagina(driver, "Error al registrar usuario: apellidos vacios");
	}

	/**
	 * Registro de usuario con datos inválidos repeticion de contraseña inválida
	 */
	@Test
	public void PR03() {
		// Vamos al formulario de registro
		driver.findElement(By.xpath("//a[@href='/signin']")).click();
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "user123@email.com", "TestName", "TestSurname", "123456", "1");
		// COmprobamos el error de contraseña invalida
		// PO_RegisterView.checkKey(driver, "Error al registrar usuario: las contraseñas
		// no coinciden");
		SeleniumUtils.textoPresentePagina(driver, "Error al registrar usuario: las contraseñas no coinciden");

	}

	/**
	 * Registro de usuario con datos inválidos email existente
	 */
	@Test
	public void PR04() {
		// Vamos al formulario de registro
		driver.findElement(By.xpath("//a[@href='/signin']")).click();
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "prueba1@prueba1", "TestName", "TestSurname", "123456", "123456");
		// COmprobamos el error de DNI repetido.
		// PO_RegisterView.checkKey(driver, "Error al registrar usuario: No puede
		// registrarse con ese email");
		SeleniumUtils.textoPresentePagina(driver, "Error al registrar usuario: No puede registrarse con ese email");

	}

	/**
	 * Inicio de sesion con datos validos (usuario estandar)
	 */
	@Test
	public void PR05() {
		loginAs("prueba1@prueba1", "prueba1");
	}

	/**
	 * Inicio de sesion con datos invalidos usuario estandar, campo email y
	 * contraseña vacios
	 */
	@Test
	public void PR06() {
		driver.findElement(By.xpath("//a[@href='/login']")).click();

		PO_LoginView.fillForm(driver, "", "123456");
		// Comprobamos que se muestra el error
		// PO_RegisterView.checkKey(driver, "Error al identificar usuario: email
		// vacio");
		SeleniumUtils.textoPresentePagina(driver, "Error al identificar usuario: email vacio");

		PO_LoginView.fillForm(driver, "user1@email.com", "");
		// Comprobamos que se muestra el error
		// PO_RegisterView.checkKey(driver, "Error al identificar usuario: contraseña
		// vacia");
		SeleniumUtils.textoPresentePagina(driver, "Error al identificar usuario: contraseña vacia");

	}

	/**
	 * Inicio de sesion con datos validos usuario estandar, email existente, pero
	 * contraseña incorrecta
	 */
	@Test
	public void PR07() {
		driver.findElement(By.xpath("//a[@href='/login']")).click();
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "prueba1@prueba1", "123456234234");
		// Comprobamos que se muestra el error
		// PO_RegisterView.checkKey(driver, "Email o password incorrecto");
		SeleniumUtils.textoPresentePagina(driver, "Email o password incorrecto");

	}

	/**
	 * Inicio de sesión con datos inválidos (usuario estándar, email no existentey
	 * contraseña no vacía).
	 * 
	 */
	@Test
	public void PR08() {
		driver.findElement(By.xpath("//a[@href='/login']")).click();
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "user1@email.com", "123456234234");
		// Comprobamos que se muestra el error
		// PO_RegisterView.checkKey(driver, "Email o password incorrecto");
		SeleniumUtils.textoPresentePagina(driver, "Email o password incorrecto");

	}

	/**
	 * Fin de sesion hacer click en la opcion de salir de sesion y comprobar que se
	 * redirige a la pagina de inicio de sesion
	 */
	@Test
	public void PR09() {
		loginAs("prueba1@prueba1", "prueba1");

		disconect();

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
		loginAs("prueba1@prueba1", "prueba1");

		goToListUsers();

		int numOfUsers = PO_PrivateView.getTotalNumOfElements(driver);
		assertEquals(12, numOfUsers);

//		List<WebElement> usuarios = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertEquals(usuarios.size(), 4);
//		driver.navigate().to(URL + "/usuarios?pg=2");
//		usuarios = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertEquals(usuarios.size(), 4);
//		driver.navigate().to(URL + "/usuarios?pg=3");
//		usuarios = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr", PO_View.getTimeout());
//		assertEquals(usuarios.size(), 4);

		disconect();
	}

	/**
	 * Hacer una búsqueda con el campo vacío y comprobar que se muestra la página
	 * que corresponde con el listado usuarios existentes en el sistema.
	 */
	@Test
	public void PR12() {
		loginAs("prueba1@prueba1", "prueba1");
		goToListUsers();
		// search without any text
		PO_PrivateView.searchInUsersList(driver, "");
		// get the number of users
		int numOfUsers = PO_PrivateView.getTotalNumOfElements(driver);
		assertEquals(12, numOfUsers);

		disconect();

	}

	/**
	 * Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar
	 * que se muestra la página que corresponde, con la lista de usuarios vacía
	 */
	@Test
	public void PR13() {
		loginAs("prueba1@prueba1", "prueba1");
		goToListUsers();
		PO_PrivateView.searchInUsersList(driver, "noexisto");
		// check no user shows up
		List<WebElement> users = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(0, users.size());

		disconect();
	}

	/**
	 * Hacer una búsquedacon un texto específico y comprobar que se muestra la
	 * página que corresponde, con la lista de usuarios en los que el
	 * textoespecificados sea parte de su nombre, apellidos o de su email.
	 */
	@Test
	public void PR14() {
		loginAs("prueba1@prueba1", "prueba1");
		goToListUsers();

		// search a name that exists
		PO_PrivateView.searchInUsersList(driver, "nombre11");
		List<WebElement> users = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(1, users.size());

		// search a surname that exists
		PO_PrivateView.searchInUsersList(driver, "apellidos11");
		users = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(1, users.size());

		// search valid email
		PO_PrivateView.searchInUsersList(driver, "prueba11@prueba11");
		users = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(1, users.size());

		disconect();

	}

	/**
	 * Desde el listado de usuarios de la aplicación, enviar una invitación de
	 * amistad a un usuario. Comprobar que la solicitud de amistad aparece en el
	 * listado de invitaciones (punto siguiente).
	 */
	@Test
	public void PR15() {
		loginAs("prueba1@prueba1", "prueba1");

		goToListUsers();
		PO_PrivateView.goToPage(driver, 2);

		// envia invitacion a nombre7
		driver.findElement(By.id("nombre7")).click();
		SeleniumUtils.textoPresentePagina(driver, "Invitacion enviada");

		gotoListInvitaciones();

		SeleniumUtils.textoPresentePagina(driver, "nombre7");

		disconect();
	}

	/**
	 * Desde el listado de usuarios de la aplicación, enviar una invitación de
	 * amistad a un usuario al que ya le habíamos enviado la invitación previamente.
	 * No debería dejarnos enviar la invitación, se podría ocultar el botón de
	 * enviar invitación onotificar que ya había sido enviada previamente
	 */
	@Test
	public void PR16() {
		loginAs("prueba1@prueba1", "prueba1");

		goToListUsers();
		PO_PrivateView.goToPage(driver, 2);

		// envia invitacion a nombre7
		driver.findElement(By.id("nombre7")).click();
		SeleniumUtils.textoPresentePagina(driver, "Error: ya hay una petición pendiente");

		disconect();
	}

	/**
	 * Mostrar el listado de invitaciones de amistad recibidas. Comprobar con un
	 * listado que contenga varias invitaciones recibidas
	 */
	@Test
	public void PR17() {
		loginAs("prueba1@prueba1", "prueba1");

		gotoListInvitaciones();

		List<WebElement> invitaciones = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(4, invitaciones.size());

		disconect();
	}

	/**
	 * Sobre el listado de invitaciones recibidas. Hacer click en el botón/enlace de
	 * una de ellas y comprobar que dicha solicitud desaparece del listado de
	 * invitaciones
	 */
	@Test
	public void PR18() {
		loginAs("prueba1@prueba1", "prueba1");

		gotoListInvitaciones();
		driver.findElement(By.id("nombre7")).click();
		SeleniumUtils.textoPresentePagina(driver, "Invitación aceptada");
		List<WebElement> invitaciones = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(3, invitaciones.size());

		disconect();
	}

	/**
	 * Mostrar el listado de amigos de un usuario. Comprobar que el listado contiene
	 * los amigos que deben ser.
	 */
	@Test
	public void PR19() {
		loginAs("prueba1@prueba1", "prueba1");

		goToListFriends();

		List<WebElement> invitaciones = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(6, invitaciones.size());

		disconect();
	}

	/**
	 * Intentar acceder sin estar autenticado a la opcion de listado de usuarios Se
	 * debera volver al formulario de login
	 *
	 */
	@Test
	public void PR20() {
		driver.navigate().to("https://localhost:8081/usuarios");
		PO_View.checkElement(driver, "text", "Identificación de usuario");
		String currentURL = driver.getCurrentUrl();
		assertEquals(currentURL, "https://localhost:8081/login");
	}

	/**
	 * Intentar acceder sin estar autenticado a la opción de listado de invitaciones
	 * de amistad recibida de un usuario estándar. Se deberá volver al formulario de
	 * login.
	 */
	@Test
	public void PR21() {
		driver.navigate().to("https://localhost:8081/invitaciones");
		PO_View.checkElement(driver, "text", "Identificación de usuario");
		String currentURL = driver.getCurrentUrl();
		assertEquals(currentURL, "https://localhost:8081/login");
	}

	/**
	 * Intentar acceder estando autenticado como usuario standard a la lista de
	 * amigos de otro usuario. Se deberá mostrar un mensaje de acción indebida.
	 *
	 */
	@Test
	public void PR22() {
		driver.navigate().to("https://localhost:8081/usuarios");
		PO_View.checkElement(driver, "text", "Identificación de usuario");
		String currentURL = driver.getCurrentUrl();
		assertEquals(currentURL, "https://localhost:8081/login");
	}

	/**
	 * Inicio de sesión con datos válidos.
	 * 
	 * @throws InterruptedException
	 *
	 */
	@Test
	public void PR23() throws InterruptedException {
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario.
		PO_LoginView.fillClienteForm(driver, "prueba1@prueba1", "prueba1");
		// TimeUnit.MILLISECONDS.sleep(500);
		PO_View.checkElement(driver, "text", "nombre2");

		// SeleniumUtils.textoPresentePagina(driver, "nombre2");

	}

	/**
	 * Inicio de sesión con datos inválidos (usuario no existente en la aplicación).
	 *
	 */
	@Test
	public void PR24() {
		driver.navigate().to("https://localhost:8081/cliente.html");
		// Rellenamos el formulario.
		PO_LoginView.fillClienteForm(driver, "prueba1@prueba1", "asdfasdf");
		SeleniumUtils.textoPresentePagina(driver, "Autenticación fallida");
	}

	/**
	 * Acceder a la lista de amigos de un usuario, que al menos tenga tres amigos.
	 * 
	 * @throws InterruptedException
	 *
	 */
	@Test
	public void PR25() throws InterruptedException {
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillClienteForm(driver, "prueba1@prueba1", "prueba1");
		TimeUnit.MILLISECONDS.sleep(500);

		// List<WebElement> users = PO_PrivateView.getAllElementsInList(driver);
		List<WebElement> elements = SeleniumUtils.EsperaCargaPaginaxpath(driver, "//tbody/tr", 3);

		assertEquals(6, elements.size());

	}

	/**
	 * Acceder a la lista de amigos de un usuario, y realizar un filtrado para
	 * encontrar a un amigo concreto, el nombre a buscar debe coincidir con el de un
	 * amigo.
	 * @throws InterruptedException 
	 *
	 */
	@Test
	public void PR26() throws InterruptedException {
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillClienteForm(driver, "prueba1@prueba1", "prueba1");

		// PO_PrivateView.searchInUsersListCliente(driver, "nombre2");

		// WebElement toSearch = SeleniumUtils.EsperaCargaPaginaxpathUnElemento(driver,
		// "//input", 5);
		List<WebElement> elements = SeleniumUtils.EsperaCargaPaginaxpath(driver, "//input", 3);
		WebElement toSearch = elements.get(0);
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		toSearch.click();
		toSearch = SeleniumUtils.EsperaCargaPaginaxpath(driver, "//input", 3).get(0);
		TimeUnit.SECONDS.sleep(1);
		toSearch.sendKeys("nombre2");
		toSearch.sendKeys(Keys.ENTER);
		
		
		List<WebElement> users = driver.findElements(By.xpath("//tbody/tr"));
		assertEquals(1, users.size());

	}

	/**
	 * Acceder a la lista de mensajes de un amigo “chat”, la lista debe contener
	 * almenos tres mensajes.
	 * 
	 * @throws InterruptedException
	 *
	 */
	@Test
	public void PR27() throws InterruptedException {
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillClienteForm(driver, "prueba1@prueba1", "prueba1");

		driver.findElement(By.xpath("/html/body/div/div/table/tbody/tr[1]/td[1]/a")).click();

		TimeUnit.SECONDS.sleep(2); // messages take a while to load

		List<WebElement> msg = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(14, msg.size());

	}

	/**
	 * Acceder a la lista de mensajes de un amigo“chat” y crear un nuevo mensaje,
	 * validar que el mensaje aparece en la lista de mensajes
	 *
	 */
	@Test
	public void PR28() throws InterruptedException {
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillClienteForm(driver, "prueba1@prueba1", "prueba1");

		driver.findElement(By.xpath("/html/body/div/div/table/tbody/tr[1]/td[1]/a")).click();
		TimeUnit.SECONDS.sleep(1); // messages take a while to load

		PO_PrivateView.sendMessage(driver, "nuevo mensaje de prueba");
		TimeUnit.SECONDS.sleep(1); // messages take a while to load

		SeleniumUtils.textoPresentePagina(driver, "nuevo mensaje de prueba");

		List<WebElement> msg = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(15, msg.size());

	}

	/**
	 * Acceder a la lista de mensajes de un amigo“chat” y crear un nuevo mensaje,
	 * validar que el mensaje aparece en la lista de mensajes
	 *
	 * Se comprueba en el otro participante de la conversacion que el mensaje
	 * aparece
	 *
	 */
	@Test
	public void PR32() throws InterruptedException {
		driver.navigate().to("https://localhost:8081/cliente.html");
		PO_LoginView.fillClienteForm(driver, "prueba1@prueba1", "prueba1");

		driver.findElement(By.xpath("/html/body/div/div/table/tbody/tr[1]/td[1]/a")).click();
		TimeUnit.SECONDS.sleep(2); // messages take a while to load

		SeleniumUtils.textoPresentePagina(driver, "nuevo mensaje de prueba");

		List<WebElement> msg = PO_PrivateView.getAllElementsInList(driver);
		assertEquals(15, msg.size());

	}

	private void loginAs(String email, String password) {
		driver.findElement(By.xpath("//a[@href='/login']")).click();
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, email, password);
		// COmprobamos que entramos en la pagina privada de Alumno
		// PO_View.checkElement(driver, "text", "Bienvenido a My SocialNetwork!");
		SeleniumUtils.textoPresentePagina(driver, "Bienvenido a My SocialNetwork!");

	}

	private void goToListUsers() {
		driver.findElement(By.xpath("//a[@href='/usuarios']")).click();
	}

	private void gotoListInvitaciones() {
		driver.findElement(By.xpath("//a[@href='/invitaciones']")).click();
	}

	private void goToListFriends() {
		driver.findElement(By.xpath("//a[@href='/amigos']")).click();
	}

	private void disconect() {
		driver.findElement(By.xpath("//a[@href='/logout']")).click();
		// PO_View.checkKey(driver, "Usuario desconectado");
		SeleniumUtils.textoPresentePagina(driver, "Usuario desconectado");

		// PO_View.checkKey(driver, "Identificación de usuario");
		SeleniumUtils.textoPresentePagina(driver, "Identificación de usuario");

	}
}
