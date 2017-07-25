package Servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import WebService.CouponWebService.MyLogger;

/**
 * Servlet implementation class LogoutServlet. This class handles the logout
 * attempted by the user, performs an orderly exit by deleting all the cookies
 * in the client browser and ending the client SESSION.
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LogoutServlet() {
		super();
	}

	/**
	 * create an instence fo MyLogger
	 */
	Logger logger = MyLogger.getInstance().getLogger();

	/**
	 * protected void doPost(HttpServletRequest request, HttpServletResponse
	 * response), method handling HTTP POST request.The method takes the cookies
	 * from the client browser and deletes them by resetting their time to 0,
	 * and then invalid the session.
	 * 
	 * @param HttpServletRequest;
	 * @param HttpServletResponse.
	 * @throws ServletException;
	 * @throws IOException.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MyLogger.logger.log(Level.INFO, "Creae Cookie Before Logout");

		Cookie[] cookies = request.getCookies();
		MyLogger.logger.log(Level.INFO, "Set Cookie Max Age To : 0 ");
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}

		MyLogger.logger.log(Level.INFO, "Invalidate Session");
		request.getSession().invalidate();
		MyLogger.logger.log(Level.INFO, "Redirecting To Login.html Page");
		response.sendRedirect("http://localhost:8080/CouponWebService/Login/Login.html");
	}

}
