package WebService.CouponWebService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import DataTypes.Company;
import DataTypes.Customer;
import Exceptions.CouponSystemException;
import Facade.AdminFacade;
import ObjectBeans.WebCompany;
import ObjectBeans.WebCustomer;

/**
 * Root resource (exposed at "adminservice" path) This class invokes the methods
 * related to the administrator permissions, after attaining the facade instance
 * from the session HttpServletRequest.
 */
@Path("adminservice")
public class AdminService {

	@Context
	HttpServletRequest request;

	@Context
	private HttpServletResponse response;
	/**
	 * get an instance of logger
	 */
	Logger looger = MyLogger.getInstance().getLogger();

	// ***************************************************************************************
	/**
	 * private AdminFacade getAdminFacade(), method serving all other methods in
	 * this class for obtaining the AdminFacade from the session.
	 * 
	 * @throws LoginException.
	 * @return AdminFacade
	 */
	private AdminFacade getAdminFacade() throws LoginException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			MyLogger.logger.log(Level.INFO, "Checking for the AdminFacade on the session in AdminService");
			AdminFacade adminFacade = (AdminFacade) request.getSession().getAttribute("couponClientFacade");
			if (adminFacade != null) {
				MyLogger.logger.log(Level.INFO, "Exiting the getAdminFacade method in AdminService");
				return adminFacade;
			}
		}
		MyLogger.logger.log(Level.SEVERE, "please check your credentials - UNAUTHORIZED error ");
		throw new LoginException("You are attempting to connect without authorization!");
	}

	// ***************************************************************************************
	/**
	 * public Response createCompany(WebCompany webCompany), method handling
	 * HTTP POST request.exposed at "createcompany" path.
	 * 
	 * @param WebCompany,
	 *            sand in the format of a JSON.
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@POST
	@Path("createcompany")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCompany(WebCompany webCompany) {
		MyLogger.logger.log(Level.INFO, "Entering To *** createCompany() *** Method in AdminFacade");
		try {
			MyLogger.logger.log(Level.INFO, "converting the web conpany to a company");
			this.getAdminFacade().createCompany(webCompany.convertToCompany());
			MyLogger.logger.log(Level.INFO, "createCompany() method execute seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With createCompany() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to createCompany() Method with no exception");
		return Response.status(201).build();
	}

	// ***************************************************************************************
	/**
	 * public Respons deleteCompany() (@PathParam("companyId") long id), Method
	 * handling HTTP DELETE requests. exposed at "deletecompany/{id}" path.
	 * 
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@DELETE
	@Path("deletecompany/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCompany(@PathParam("id") long id) {
		MyLogger.logger.info("Entering To *** deleteCompany() *** Method");
		WebCompany webCompany = new WebCompany();
		webCompany.setId(id);
		try {
			getAdminFacade().deleteCompany(webCompany.convertToCompany());
			MyLogger.logger.info("deleteCompany() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.log(Level.SEVERE, "There Is A Problem With deleteCompany() Execute" + e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to deleteCompany() Method with no exception");
		return Response.status(201).build();
	}

	// ***************************************************************************************
	/**
	 * public Respons updateCompany(@PathParam("id") long id, WebCompany
	 * webCompany) Method handling HTTP PUT requests.exposed at
	 * "updatecompany/{id}" path.
	 *
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@PUT
	@Path("updatecompany/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCompany(@PathParam("id") long id, WebCompany webCompany) {
		MyLogger.logger.info("Entering To *** updateCompany() *** Method");
		webCompany.setId(id);
		try {
			MyLogger.logger.info("converting company to a web company");
			getAdminFacade().updateCompany(webCompany.convertToCompany());
			MyLogger.logger.info("updateCompany() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.log(Level.SEVERE, "There Is A Problem With updateCompany() Execution" + e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to updateCompany() Method with no exception");
		return Response.status(200).build();
	}

	// ***************************************************************************************
	/**
	 * public Respons getAllCompanies() Method handling HTTP GET
	 * requests.exposed at "getallcompanies" path.
	 *
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	// @GET
	// @Path("getallcompanies")
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response getAllCompanies() {
	// MyLogger.logger.info("Entering To *** getAllCompanies() *** Method");
	// Collection<Company> dbCompanies = null;
	// try {
	// dbCompanies = getAdminFacade().getAllCompanies();
	// MyLogger.logger.info("Convert To WebCompanies Execute Seccesfully");
	// Collection<WebCompany> webCompanies =
	// WebCompany.convertToWebCompanies(dbCompanies);
	// MyLogger.logger.info("getAllCompanies() Execute Seccesfully ");
	// } catch (CouponSystemException e) {
	// MyLogger.logger.log(Level.SEVERE, "There Is A Problem With
	// updateCompany() Execution" + e.toString(), e);
	// return Response.status(404).entity(e.getMessage()).build();
	// } catch (LoginException e) {
	// MyLogger.logger.log(Level.SEVERE, e.toString(), e);
	// return Response.status(401).build();
	// } catch (Exception e) {
	// MyLogger.logger.log(Level.SEVERE, e.toString(), e);
	// return Response.status(500).entity(e.getMessage()).build();
	// }
	// GenericEntity<Collection<Company>> genericWebCompanies = new
	// GenericEntity<Collection<Company>>(webCompanies) {
	// };
	// MyLogger.logger.info("Exiting to updateCompany() Method");
	// return
	// Response.status(200).entity(genericWebCompanies).type(MediaType.APPLICATION_JSON).build();
	// }

	@GET
	@Path("getallcompanies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCompanies() {
		MyLogger.logger.info("Entering To *** getAllCompanies() *** Method");
		Collection<WebCompany> webCompanies = new ArrayList<>();
		try {
			Collection<Company> companies = getAdminFacade().getAllCompanies();
			MyLogger.logger.info("Convert To WebCompanies Execute Seccesfully");
			for (Company company : companies) {
				webCompanies.add(new WebCompany(company));
			}
			MyLogger.logger.info("getAllCompanies() Execute Seccesfully ");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With getAllCompanies() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getAllCompanies() Method with no exception");
		GenericEntity<Collection<WebCompany>> genericWebCompany = new GenericEntity<Collection<WebCompany>>(
				webCompanies) {
		};
		return Response.status(200).entity(genericWebCompany).type(MediaType.APPLICATION_JSON).build();
	}

	// ***************************************************************************************
	/**
	 * public Respons getCompany(@PathParam("id") long id) Method handling HTTP
	 * GET requests.exposed at "getcompany/{id}" path.
	 *
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("getcompany/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompany(@PathParam("id") long id) {
		MyLogger.logger.info("Entering To *** getCompany() *** Method");
		Company company = null;
		WebCompany webCompany = null;
		try {
			company = getAdminFacade().getCompany(id);
			MyLogger.logger.info("converting the company to web company");
			webCompany = new WebCompany(company);
			MyLogger.logger.info("getCompany() Execute Seccesfully -  now converting to webCompany.");
		} catch (CouponSystemException e) {
			MyLogger.logger.log(Level.SEVERE, "There Is A Problem With getCompany () Execution" + e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getCompany() Method with no exception");
		return Response.status(200).entity(webCompany).build();
	}

	// ***************************************************************************************
	/**
	 * public Respons createCustomer Method handling HTTP POST requests.exposed
	 * at "createcustomer" path.
	 *
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@POST
	@Path("createcustomer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(WebCustomer webCustomer) {
		MyLogger.logger.info("Entering To *** createCustomer() *** Method");

		try {
			MyLogger.logger.info("converting customer to web customer");
			getAdminFacade().createCustomer(webCustomer.convertToCustomer());
			MyLogger.logger.info("createCustomer() Execute Seccesfully ");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With createCustomer() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to createCustomer() Method with no exception");
		return Response.status(201).build();
	}

	// ***************************************************************************************
	/**
	 * public Respons deleteCustomer(@PathParam("id") long id) Method handling
	 * HTTP DELETE requests.exposed at "deletecustomer/{id}" path.
	 *
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@DELETE
	@Path("deletecustomer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteCustomer(@PathParam("id") long id) {
		MyLogger.logger.info("Entering To *** deleteCustomer() *** Method");

		WebCustomer webCustomer = new WebCustomer();
		webCustomer.setId(id);
		try {
			getAdminFacade().deleteCustomer(webCustomer.convertToCustomer());
			MyLogger.logger.info("deleteCustomer() Execute Seccesfully ");
		} catch (CouponSystemException e) {
			MyLogger.logger.log(Level.SEVERE, "There Is A Problem With deleteCustomer() Execute" + e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to deleteCustomer() Method with no exception");
		return Response.status(201).build();

	}

	// ***************************************************************************************
	/**
	 * public Respons updetCustomer(@PathParam("id") long id, WebCustomer
	 * webCustomer) Method handling HTTP PUT requests.exposed at
	 * "updatecustomer/{id}" path.
	 *
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@PUT
	@Path("updatecustomer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updetCustomer(@PathParam("id") long id, WebCustomer webCustomer) {
		MyLogger.logger.info("Entering To *** updetCustomer() *** Method");
		webCustomer.setId(id);
		try {
			MyLogger.logger.info("converting to web customer");
			getAdminFacade().updetCustomer(webCustomer.convertToCustomer());
			MyLogger.logger.info("updetCustomer() Execute Seccesfully ");
		} catch (CouponSystemException e) {
			MyLogger.logger.log(Level.SEVERE, "There Is A Problem With updetCustomer() Execution" + e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to updetCustomer() Method with no exception");
		return Response.status(200).build();

	}

	// ***************************************************************************************
	/**
	 * public Respons getCustomer(@PathParam("id") long id) Method handling HTTP
	 * GET requests.exposed at "getcustomer/{id}" path.
	 *
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("getcustomer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustomer(@PathParam("id") long id) throws LoginException {
		Customer customer = null;
		WebCustomer webCustomer = null;
		MyLogger.logger.info("Entering To *** getCustomer() *** Method");
		try {
			customer = getAdminFacade().getCustomer(id);
			MyLogger.logger.info("getCustomer() Execute Seccesfully ");
			MyLogger.logger.info("converting to webCustomer");
			webCustomer = new WebCustomer(customer);
		} catch (CouponSystemException e) {
			MyLogger.logger.log(Level.SEVERE, "There Is A Problem With getCustomer () Execution" + e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getCustomer() Method with no exception");
		return Response.status(200).entity(webCustomer).build();
	}

	// ***************************************************************************************
	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("getallcustomers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCustomers() throws LoginException {
		Collection<WebCustomer> webCustomers = new ArrayList<>();
		MyLogger.logger.info("Entering to getAllCustomers() Mehod");
		try {
			Collection<Customer> customers = getAdminFacade().getAllCustomers();
			MyLogger.logger.info("called To getAllCustomers() Method In AdminServis");
			for (Customer customer : customers) {
				webCustomers.add(new WebCustomer(customer));
			}
			MyLogger.logger.info("Convert To webCustomer Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With getAllCustomers() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getAllCustomers() Method with no exception");
		GenericEntity<Collection<WebCustomer>> genericWebCustomer = new GenericEntity<Collection<WebCustomer>>(
				webCustomers) {
		};
		return Response.status(200).entity(genericWebCustomer).type(MediaType.APPLICATION_JSON).build();
	}

}
