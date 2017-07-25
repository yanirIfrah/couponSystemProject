package WebService.CouponWebService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import DataTypes.Coupon;
import DataTypes.CouponType;
import Exceptions.CouponSystemException;
import Facade.CompanyFacade;
import ObjectBeans.WebCompany;
import ObjectBeans.WebCoupon;
import Utiles.DateConvertUtils;

/**
 * Root resource (exposed at "companyService" path) This class invokes the
 * methods related to the companyService permissions, after attaining the facade
 * instance from the session HttpServletRequest.
 */
@Path("companyservice")
public class CompanyService {

	@Context
	HttpServletRequest request;

	@Context
	private HttpServletResponse response;
	/**
	 * get an instance of MyLogger
	 */
	Logger looger = MyLogger.getInstance().getLogger();

	// ***************************************************************************************
	/**
	 * private CompanyFacade getCompanyFacade(), method serving all other
	 * methods in this class for obtaining the CompanyFacade from the session.
	 * 
	 * @throws LoginException.
	 * @return CompanyFacade
	 */
	private CompanyFacade getCompanyFacade() throws LoginException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			MyLogger.logger.log(Level.INFO, "Checking for the CompanyFacade on the session in CompanyService");
			CompanyFacade companyFacade = (CompanyFacade) request.getSession().getAttribute("couponClientFacade");
			if (companyFacade != null) {
				MyLogger.logger.log(Level.INFO,
						"Exiting the getCompanyFacade method in CompanyService without Exceptions");
				return companyFacade;

			}
		}
		MyLogger.logger.log(Level.SEVERE, "please check your credentials - UNAUTHORIZED error ");
		throw new LoginException("You are attempting to connect without authorization!");
	}

	// ***************************************************************************************
	/**
	 * public Response createCoupon(WebCoupon webCoupon), method handling HTTP
	 * POST request.exposed at "createcoupon" path.
	 * 
	 * @param WebCoupon,
	 *            sand in the format of a JSON.
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@POST
	@Path("createcoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCoupon(WebCoupon webCoupon) {
		MyLogger.logger.info("Entering To *** createCoupon() *** Method");
		Coupon coupon = new Coupon(webCoupon.getId(), webCoupon.getTitle(), webCoupon.getStartDate(),
				webCoupon.getEndDate(), webCoupon.getAmount(), CouponType.valueOf(webCoupon.getType().toUpperCase()),
				webCoupon.getMessage(), webCoupon.getPrice(), webCoupon.getImage());
		try {
			getCompanyFacade().createCoupon(coupon);
			MyLogger.logger.info("createCoupon() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With createCoupon() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to createCoupon() Method with no exception");
		return Response.status(201).build();

	}

	// ***************************************************************************************
	/**
	 * public Response deleteCoupon(@PathParam("id") long id), method handling
	 * HTTP DELETE request.exposed at "deletecoupon/{id}" path.
	 * 
	 * @param id,
	 *            ths coupon id for deleation.
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@DELETE
	@Path("deletecoupon/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteCoupon(@PathParam("id") long id) {
		MyLogger.logger.info("Entering To *** deleteCoupon() *** Method");
		Collection<Coupon> coupons = new ArrayList<>();
		Coupon coupon = new Coupon();
		coupon.setId(id);
		try {
			getCompanyFacade().deleteCoupon(coupon);
			MyLogger.logger.info("deleteCoupon() Execute Seccesfully");
			coupons = getCompanyFacade().getAllCoupons();
			MyLogger.logger.info("getAllCoupons() In deleteCoupon() Method Execute Seccesfully");

			System.out.println("CompanyService deleteCoupon ");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With deleteCoupon() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to deleteCoupon() Method with no exception");
		return Response.status(201).entity(coupons).build();
	}

	// ***************************************************************************************
	/**
	 * public Response updateCoupon(@PathParam("id") long id, WebCoupon
	 * webCoupon), method handling HTTP PUT request.exposed at
	 * "updatecoupon/{id}" path.
	 * 
	 * @param id,
	 *            ths coupon id for updating.
	 * @return Response, consisting the status code
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@PUT
	@Path("updatecoupon/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCoupon(@PathParam("id") long id, WebCoupon webCoupon) {
		MyLogger.logger.info("Entering To *** updateCoupon() *** Method");
		webCoupon.setId(id);
		try {
			getCompanyFacade().updateCoupon(webCoupon.convertToCoupon());
			MyLogger.logger.info("updateCoupon()() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.log(Level.SEVERE, "There Is A Problem With updateCoupon() Execution" + e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to updateCoupon() Method with no exception");
		return Response.status(200).build();
	}

	// ***************************************************************************************
	/**
	 * public Response getCoupon(@PathParam("id") long id), method handling HTTP
	 * GET request.exposed at "getcoupon/{id}" path.
	 * 
	 * @param id,
	 *            ths coupon id .
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("getcoupon/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCoupon(@PathParam("id") long id) throws LoginException {
		MyLogger.logger.info("Entering To *** getCoupon() *** Method");
		Coupon coupon = null;
		WebCoupon webCoupon = null;
		try {
			coupon = getCompanyFacade().getCoupon(id);
			MyLogger.logger.info("getCoupon() Execute Seccesfully");
			webCoupon = new WebCoupon(coupon);
		} catch (CouponSystemException e) {
			MyLogger.logger.log(Level.SEVERE, "There Is A Problem With getCoupon () Execution" + e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getCoupon() Method with no exception");
		return Response.status(200).entity(webCoupon).build();
	}

	// ***************************************************************************************
	/**
	 * public Response getAllCoupons(), method handling HTTP GET request.exposed
	 * at "getallcoupons" path.
	 * 
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("getallcoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCoupons() {
		MyLogger.logger.info("Entering To *** getAllCoupons() *** Method");
		Collection<WebCoupon> webCoupons = new ArrayList<>();
		try {
			Collection<Coupon> coupons = getCompanyFacade().getAllCoupons();
			// convert coupons to webCoupons
			for (Coupon coupon : coupons) {
				webCoupons.add(new WebCoupon(coupon));
			}
			MyLogger.logger.info("getAllCoupons() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With getAllCoupons() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getAllCoupons() Method with no exception");
		GenericEntity<Collection<WebCoupon>> genericWevCoupon = new GenericEntity<Collection<WebCoupon>>(webCoupons) {
		};
		return Response.status(200).entity(genericWevCoupon).type(MediaType.APPLICATION_JSON).build();
	}

	// ***************************************************************************************
	/**
	 * public Response getCouponsByType(@PathParam("type") CouponType type),
	 * method handling HTTP GET request.exposed at "getcouponsbytype/{type}"
	 * path.
	 * 
	 * @param CouponType,
	 *            the type of the coupon.
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("getcouponsbytype/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCouponsByType(@PathParam("type") CouponType type) throws LoginException {
		MyLogger.logger.info("Entering To *** getCouponsByType() *** Method");
		Collection<WebCoupon> webCoupons = new ArrayList<>();
		try {
			Collection<Coupon> coupons = getCompanyFacade().getCouponsByType(type);
			for (Coupon coupon : coupons) {
				webCoupons.add(new WebCoupon(coupon));
			}
			MyLogger.logger.info("getCouponsByType() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With getCouponsByType() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getCouponsByType() Method with no exception");
		GenericEntity<Collection<WebCoupon>> genericWevCoupon = new GenericEntity<Collection<WebCoupon>>(webCoupons) {
		};
		return Response.status(200).entity(genericWevCoupon).type(MediaType.APPLICATION_JSON).build();
	}

	// ***************************************************************************************
	/**
	 * public Response getCurrentCompany(), method handling HTTP GET
	 * request.exposed at "currentcompany" path.
	 * 
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("currentcompany")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentCompany() {
		MyLogger.logger.info("Entering To *** getCurrentCompany() *** Method");
		WebCompany webcompany = null;
		try {
			webcompany = new WebCompany(getCompanyFacade().currentCompany());
			MyLogger.logger.info("converting the company to web company");
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getCurrentCompany() Method with no exception");
		return Response.status(200).entity(webcompany).build();
	}

	// ***************************************************************************************
	/**
	 * public Response getCouponsByMaxPrice(@PathParam("maxprice") double
	 * maxPrice)), method handling HTTP GET request.exposed at
	 * "getcouponsbytype/{type}" path.
	 * 
	 * @param maxprice,
	 *            the maxprice of the coupon.
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("getcouponsbymaxprice/{maxprice}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCouponsByMaxPrice(@PathParam("maxprice") double maxPrice) {
		MyLogger.logger.info("Entering To *** getCouponsByMaxPrice() *** Method");
		Collection<WebCoupon> webCoupons = new ArrayList<>();
		try {
			Collection<Coupon> coupons = getCompanyFacade().getCouponsByMaxPrice(maxPrice);
			for (Coupon coupon : coupons) {
				webCoupons.add(new WebCoupon(coupon));
			}
			MyLogger.logger.info("getCouponsByMaxPrice() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With deleteCoupon() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getCouponsByMaxPrice() Method with no exception");
		GenericEntity<Collection<WebCoupon>> genericWevCoupon = new GenericEntity<Collection<WebCoupon>>(webCoupons) {
		};
		return Response.status(200).entity(genericWevCoupon).type(MediaType.APPLICATION_JSON).build();
	}

	// ***************************************************************************************
	/**
	 * public Response getCouponsByEndDate(@PathParam("date") String date),
	 * method handling HTTP GET request.exposed at "getcouponsbytype/{type}"
	 * path.
	 * 
	 * @param date,
	 *            the maxprice of the coupon.
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("getcouponsbyenddate/{date}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCouponsByEndDate(@PathParam("date") String date) throws LoginException {
		MyLogger.logger.info("Entering To *** getCouponsByEndDate() *** Method");
		java.util.Date maxDate = new Date(Long.parseLong(date));
		Collection<WebCoupon> webCoupons = new ArrayList<>();
		try {
			Collection<Coupon> coupons = getCompanyFacade()
					.getCouponsByEndDate(DateConvertUtils.dateToSqlDate(maxDate));
			for (Coupon coupon : coupons) {
				webCoupons.add(new WebCoupon(coupon));
			}
			MyLogger.logger.info("getCouponsByEndDate() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With deleteCoupon() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getCouponsByEndDate() Method with no exception");
		GenericEntity<Collection<WebCoupon>> genericWevCoupon = new GenericEntity<Collection<WebCoupon>>(webCoupons) {
		};
		return Response.status(200).entity(genericWevCoupon).type(MediaType.APPLICATION_JSON).build();
	}

}
