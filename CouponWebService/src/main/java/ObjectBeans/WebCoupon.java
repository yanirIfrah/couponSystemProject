package ObjectBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import DataTypes.Coupon;
import DataTypes.CouponType;

@XmlRootElement
public class WebCoupon implements Serializable {

	private static final long serialVersionUID = 7704978447152925823L;

	// Attribute
	private long id;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private String type;
	private String message;
	private double price;
	private String image;

	/**
	 * CTOR
	 */
	public WebCoupon(Coupon coupon) {
		super();
		this.id = coupon.getId();
		this.title = coupon.getTitle();
		this.startDate = coupon.getStartDate();
		this.endDate = coupon.getEndDate();
		this.amount = coupon.getAmount();
		this.type = coupon.getType().toString();
		this.message = coupon.getMessage();
		this.price = coupon.getPrice();
		this.image = coupon.getImage();

	}

	public WebCoupon(long id, String title, Date startDate, Date endDate, int amount, String type, String message,
			double price, String image) {
		super();
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.type = type.toString();
		this.message = message;
		this.price = price;
		this.image = image;
	}

	/**
	 * CTOR
	 */
	public WebCoupon() {
	}

	// ******************************************************************************
	/**
	 * convertToCoupon method are converting the coupon to a WebCoupon that can
	 * run in the Web.
	 */
	public Coupon convertToCoupon() {
		return new Coupon(id, title, startDate, endDate, amount, CouponType.valueOf(type.toUpperCase()), message, price,
				image);
	}

	public WebCoupon convertToWebCoupon(Coupon coupon) {
		return new WebCoupon(coupon);
	}

	// ******************************************************************************
	/**
	 * convertToCoupon method are converting the coupon to a WebCoupon that can
	 * run in the Web.
	 */
	public static Collection<Coupon> convertToCoupons(Collection<WebCoupon> webCoupons) {
		Collection<Coupon> coupons = new ArrayList<>();
		for (WebCoupon webCoupon : webCoupons) {
			coupons.add(webCoupon.convertToCoupon());
		}
		return coupons;
	}
	// ******************************************************************************

	/**
	 * convertToCoupon method are converting the coupon from a WebCoupon that
	 * can run in the Web to Coupon as in the DB
	 */
	public static Collection<WebCoupon> convertToWebCoupons(Collection<Coupon> coupons) {
		Collection<WebCoupon> webCoupons = new ArrayList<>();
		for (Coupon coupon : coupons) {
			webCoupons.add(new WebCoupon(coupon));
		}
		return webCoupons;
	}
	// ******************************************************************************
	// Getter / Setter

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
