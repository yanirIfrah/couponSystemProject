package DataTypes;

/**
 * ENUM CouponType class that define a different categories of coupons<br>
 * a coupon object has an attribute that include one of the categories that
 * define in that class<br>
 * <b>Values are:</b><br>
 * <blockquote> RESTURANT<br>
 * ELECTRICITY<br>
 * FOOD<br>
 * HEALTH<br>
 * SPORTS<br>
 * CAMPING<br>
 * TRAVELING </blockquote>
 */
public enum CouponType {
	RESTURANT("resturant"), ELECTRICITY("electricity"), FOOD("food"), HEALTH("health"), SPORTS("sports"), CAMPING(
			"camping"), TRAVELING("traveling");

	private final String name;

	CouponType(String name) {
		this.name = name();
	}

	public String getName() {
		return name;
	}
}
