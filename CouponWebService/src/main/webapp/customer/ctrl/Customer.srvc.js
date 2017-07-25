(function() {

	var module = angular.module("couponSystem");
	/**
	 * Main Service for Customer, includes all the relevant methods that
	 * return $ http / GET / POST / DELETE / PUT from a server. Each method is
	 * called by the relevant controller and receives the answer that returns to the
	 * Promise and excute the promise function's
	 */
	module.service("mainCustomerServiceHTTP", mainCustomerServiceCtor);

	function mainCustomerServiceCtor($http) {
		
		// getAllPurchasedCoupons //************************* */
		this.getAllPurchasedCoupons = function(idcoup) {
			return $http.get('http://localhost:8080/CouponWebService/webapi/customerservice/getallpurchasedcoupons');
		}

		// purchaseCoupon //****************************************** */
		this.purchaseCoupon = function(id) {
			return $http.post('http://localhost:8080/CouponWebService/webapi/customerservice/purchasecoupon/'+id);
		}

		// get Coupons By Type //**************************************** */
		this.getCouponsByType = function(type) {
			return $http.get('http://localhost:8080/CouponWebService/webapi/customerservice/getallpurchasedcouponsbytype/'+type);
		}
		
		// get Coupons By Price //*************************************** */
		this.getAllPurchasedCouponsByMaxPrice = function(price) {
			return $http.get('http://localhost:8080/CouponWebService/webapi/customerservice/getallpurchasedcouponbyprice/'+price);
		}
	}

})();