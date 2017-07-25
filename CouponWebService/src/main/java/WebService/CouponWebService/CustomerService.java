package WebService.CouponWebService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import Facade.CustomerFacade;
import ObjectBeans.WebCoupon;

@Path("customerservice")
public class CustomerService {

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
	 * private CustomerFacade getCustomerFacade(), method serving all other
	 * methods in this class for obtaining the CustomerFacade from the session.
	 * 
	 * @throws LoginException.
	 * @return AdminFacade, unless there is no such facade on the session.
	 * @throws LoginException
	 */
	private CustomerFacade getCustomerFacade() throws LoginException {
		CustomerFacade customerFacade = (CustomerFacade) request.getSession().getAttribute("couponClientFacade");
		if (customerFacade == null) {
			MyLogger.logger.log(Level.INFO, "Checking for the CustomerFacade on the session in CustomerService");
			throw new LoginException("You are attempting to connect without authorization!");
		}
		MyLogger.logger.log(Level.INFO, "exiting the getCustomerFacade method");
		return customerFacade;
	}

	// ***************************************
	/**
	 * public Response purchaseCoupon(@PathParam("id") long id), method handling
	 * HTTP POST request.exposed at "purchasecoupon/{id}" path.
	 * 
	 * @param id,
	 *            the id of the coupon.
	 * @return Response, consisting the status code, the appropriate return
	 *         object (i.e. entity).
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@POST
	@Path("purchasecoupon/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response purchaseCoupon(@PathParam("id") long id) {
		MyLogger.logger.info("Entering To *** purchaseCoupon() *** Method");
		Coupon coupon = new Coupon();
		coupon.setId(id);
		System.out.println(id);
		try {
			System.out.println(coupon);
			getCustomerFacade().purchaseCoupon(coupon);
			MyLogger.logger.info("purchaseCoupon() Execute Seccesfully");
			System.out.println("CustomerService purchaseCoupon ");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With getAllPurchasedCoupons() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getAllPurchasedCoupons() Method with no exception");
		return Response.status(200).entity(coupon).type(MediaType.APPLICATION_JSON).build();
	}

	// ***************************************
	/**
	 * public Response getAllPurchasedCoupons(), method handling HTTP GET
	 * request.exposed at "getallpurchasedcoupons" path.
	 * 
	 * @throws CouponSystemException
	 * @throws LoginException
	 * @throws Exception
	 */
	@GET
	@Path("getallpurchasedcoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCoupons() {
		MyLogger.logger.info("Entering To *** getAllPurchasedCoupons() *** Method");

		Collection<WebCoupon> webCoupons = new ArrayList<>();
		try {
			Collection<Coupon> coupons = getCustomerFacade().getAllPurchasedCoupon();
			MyLogger.logger.info("getAllPurchasedCoupons() Execute Seccesfully");
			for (Coupon coupon : coupons) {
				webCoupons.add(new WebCoupon(coupon));
			}
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With getAllPurchasedCoupons() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getAllPurchasedCoupons() Method with no exception");
		GenericEntity<Collection<WebCoupon>> genericWevCoupon = new GenericEntity<Collection<WebCoupon>>(webCoupons) {
		};
		return Response.status(200).entity(genericWevCoupon).type(MediaType.APPLICATION_JSON).build();
	}

	// ***************************************
	/**
	 * public Response getAllPurchasedCouponsByType(@PathParam("type")
	 * CouponType type), method handling HTTP GET request.exposed at
	 * "getallpurchasedcouponsbytype/{type}" path.
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
	@Path("getallpurchasedcouponsbytype/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByType(@PathParam("type") CouponType type) {
		MyLogger.logger.info("Entering To *** getAllPurchasedCouponsByType() *** Method");
		Collection<WebCoupon> webCoupons = new ArrayList<>();
		try {
			Collection<Coupon> coupons = getCustomerFacade().getAllPurchasedCouponByType(type);
			for (Coupon coupon : coupons) {
				webCoupons.add(new WebCoupon(coupon));
			}
			MyLogger.logger.info("getAllPurchasedCouponsByType() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With getAllPurchasedCouponsByType() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getAllPurchasedCouponsByType() Method with no exception");
		GenericEntity<Collection<WebCoupon>> genericWevCoupon = new GenericEntity<Collection<WebCoupon>>(webCoupons) {
		};
		return Response.status(200).entity(genericWevCoupon).type(MediaType.APPLICATION_JSON).build();
	}

	// ***************************************
	/**
	 * public Response getAllPurchasedCouponByPrice(@PathParam("maxprice")
	 * double maxPrice), method handling HTTP GET request.exposed at
	 * "getallpurchasedcouponbyprice/{maxprice}" path.
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
	@Path("getallpurchasedcouponbyprice/{maxprice}")
	@Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponByPrice(@PathParam("maxprice") double maxPrice) {
		MyLogger.logger.info("Entering To *** getAllPurchasedCouponByPrice() *** Method");
		Collection<WebCoupon> webCoupons = new ArrayList<>();
		try {
			Collection<Coupon> coupons = getCustomerFacade().getAllPurchasedCouponByPrice(maxPrice);
			for (Coupon coupon : coupons) {
				webCoupons.add(new WebCoupon(coupon));
			}
			MyLogger.logger.info("getAllPurchasedCouponByPrice() Execute Seccesfully");
		} catch (CouponSystemException e) {
			MyLogger.logger.severe("There Is A Problem With getAllPurchasedCouponByPrice() Execute" + e.toString());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			MyLogger.logger.log(Level.SEVERE, e.getMessage(), e.toString());
			return Response.status(401).build();
		} catch (Exception e) {
			MyLogger.logger.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		MyLogger.logger.info("Exiting to getAllPurchasedCouponByPrice() Method with no exception");
		GenericEntity<Collection<WebCoupon>> genericWevCoupon = new GenericEntity<Collection<WebCoupon>>(webCoupons) {
		};
		return Response.status(200).entity(genericWevCoupon).type(MediaType.APPLICATION_JSON).build();
	}

}
