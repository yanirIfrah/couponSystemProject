package Servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import CouponSystem.CouponSystem;
import Exceptions.CouponSystemException;
import Exceptions.DAOException;
import Facade.ClientType;
import Facade.CouponClientFacade;
import WebService.CouponWebService.MyLogger;

/**
 * Servlet implementation class LoginServlet. This class handles the login
 * attempted by the user, and operates according to the details provided by the
 * latter (or to existing client cookies), all the while logging each operation.
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Logger logger = MyLogger.getInstance().getLogger();

	CouponClientFacade couponClientFacade = null;

	//
	@Override
	public void init() throws ServletException {
		try {
			CouponSystem.initialize();
		} catch (CouponSystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * setCookies method
	 */
	private void setCookies(HttpServletRequest request, HttpServletResponse response, String user, String pwd) {
		MyLogger.logger.log(Level.INFO, "Entering To setCookie() Method");

		user = request.getParameter("userName");
		pwd = request.getParameter("password");
		Cookie cookieType = new Cookie("clientType", request.getParameter("clientType"));
		MyLogger.logger.log(Level.INFO, "Create Cookie - clientType - set max age to");
		cookieType.setMaxAge(60 * 60 * 24 * 7);// 60*60*24*7
		MyLogger.logger.log(Level.INFO, "Set Cookie clientType To one week");

		Cookie cookieName = new Cookie("userName", user);
		MyLogger.logger.log(Level.INFO, "Create Cookie - userName");
		cookieName.setMaxAge(60 * 60 * 24 * 7);// 60*60*24*7
		MyLogger.logger.log(Level.INFO, "Set Cookie userName To one wee");

		Cookie cookiePass = new Cookie("password", pwd);
		MyLogger.logger.log(Level.INFO, "Create Cookie - password");
		cookiePass.setMaxAge(60 * 60 * 24 * 7);// 60*60*24*7
		MyLogger.logger.log(Level.INFO, "Set Cookie password To one wee");

		response.addCookie(cookieName);
		response.addCookie(cookiePass);
		response.addCookie(cookieType);

		MyLogger.logger.log(Level.INFO, "Finish Adding Cookie On The Response");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * protected void doPost(HttpServletRequest request, HttpServletResponse
	 * response), method handling HTTP POST request. The method checks for any
	 * client cookies. If found, the login is performed accordingly. Otherwise,
	 * the method checks the login details filled out by the user, and either
	 * sets the appropriate CouponClientFacade on the session, as well as plants
	 * cookies with the login details (in case of a proper login attempt),
	 * thereafter redirecting to the URL of the suitable user; or sends to an
	 * error page, indicating a bad request by the user (in case of an incorrect
	 * login attempt).
	 * 
	 * @param HttpServletRequest;
	 * @param HttpServletResponse.
	 * @throws ServletException;
	 * @throws IOException.
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String type = "";
		String user = "";
		String pwd = "";

		MyLogger.logger.log(Level.INFO, "Checking cookies on client  Brwoser");
		Cookie[] cookie = request.getCookies();
		if (cookie != null) {
			for (int i = 0; i < cookie.length; i++) {
				if (cookie[i].getName().equals("userName")) {
					user = cookie[i].getValue();
				} else if (cookie[i].getName().equals("password")) {
					pwd = cookie[i].getValue();
				} else if (cookie[i].getName().equals("clientType")) {
					type = cookie[i].getValue();
				}
			}
		}

		if (pwd.equals("") || user.equals("") || type.equals("")) {
			MyLogger.logger.log(Level.INFO, "get the parameters From the form in login pafe in client Brwoser");
			type = request.getParameter("clientType");
			user = request.getParameter("userName");
			pwd = request.getParameter("password");
		}
		System.out.println(type);
		System.out.println(user);
		System.out.println(pwd);
		try {
			switch (type) {
			case "ADMIN":
				MyLogger.logger.log(Level.INFO, "Admin Is Trying To Login - Calling To CouponSystem.Login Method!");
				couponClientFacade = CouponSystem.getInstance().login(ClientType.ADMIN, user, pwd);
				MyLogger.logger.log(Level.INFO, "Login Succeeded - Set Admin Attribute On The Session Request");
				request.getSession().setAttribute("couponClientFacade", couponClientFacade);
				MyLogger.logger.log(Level.INFO, "Adding Cookies To Client Browser by calling the setCookies method");
				setCookies(request, response, user, pwd);
				MyLogger.logger.log(Level.INFO, "Redirecting To Admin Home Page");
				response.sendRedirect("http://localhost:8080/CouponWebService/admin/admin.html");
				break;

			case "COMPANY":
				MyLogger.logger.log(Level.INFO,
						"Company :" + user + " Is Trying To Login - Calling To CouponSystem.Login Method!");
				couponClientFacade = CouponSystem.getInstance().login(ClientType.COMPANY, user, pwd);
				MyLogger.logger.log(Level.INFO,
						"Login Succeeded - Set Company :" + user + " Attribute On The Session Request");
				request.getSession().setAttribute("couponClientFacade", couponClientFacade);
				MyLogger.logger.log(Level.INFO, "Adding Cookies To Client Browser by calling the setCookies method");
				setCookies(request, response, user, pwd);
				MyLogger.logger.log(Level.INFO, "Redirecting " + user + " To Company Home Page");
				response.sendRedirect("http://localhost:8080/CouponWebService/company/company.html");
				break;

			case "CUSTOMER":
				MyLogger.logger.log(Level.INFO,
						"Customer :" + user + " Is Trying To Login - Calling To CouponSystem.Login Method!");
				couponClientFacade = CouponSystem.getInstance().login(ClientType.CUSTOMER, user, pwd);
				MyLogger.logger.log(Level.INFO,
						"Login Succeeded - Set Customer :" + user + " Attribute On The Session Request");
				request.getSession().setAttribute("couponClientFacade", couponClientFacade);
				MyLogger.logger.log(Level.INFO, "Adding Cookies To Client Browser by calling the setCookies method");
				setCookies(request, response, user, pwd);
				MyLogger.logger.log(Level.INFO, "Redirecting " + user + " To Customer Home Page");
				response.sendRedirect("http://localhost:8080/CouponWebService/customer/customer.html");
				break;
			}
		} catch (CouponSystemException | DAOException e) {
			MyLogger.logger.log(Level.WARNING, user + " Details Are Incorrect - Sending Error 400 !");
			response.sendError(400, "details are incorrect ! Try again! ");
		}

	}

}
