(function() {

	var module = angular.module("couponSystem");
	module.controller("CouponsByPriceCtrl", CouponsByPriceCtrlCtor);

	/**
	 * coupons By Price controller with ngConfirm - A controller that allows the
	 * deletion of coupons from the database, under customer permissions
	 */
	function CouponsByPriceCtrlCtor(mainCustomerServiceHTTP, $ngConfirm,
			$state, $scope) {

		this.coupons = [];

		/**
		 * onSubmit method To prevent execution by clicking on the form button
		 * and allow validation on the form by the required property in the form
		 * fields
		 */
		this.onSubmit = function(state) {
			if (state == true) {
				this.getPrice();
			}
		}
		var self = this;
		/**
		 * get Price function. The method receives the answer from the service
		 * into the Promise, using the mainCustomerService. If the answer from
		 * Servis succeeded, the first function(resp) will run and return the
		 * answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.
		 */
		this.getPrice = function() {
			var promise = mainCustomerServiceHTTP
					.getAllPurchasedCouponsByMaxPrice(this.price);
			promise
					.then(
							function(resp) {
								// alert(resp.data);
								self.coupons = resp.data;
							},
							function(err) {
								self.error = (err.data);
								// check if the client is loged to perform
								// actions
								if ((err.status) == 401) {
									$ngConfirm({
										theme : 'light',
										animation : 'rotateYR',
										closeAnimation : 'scale',
										animationSpeed : 500,
										boxWidth : '30%',
										useBootstrap : false,
										title : 'Login failed!',
										content : 'You must perform a login to perform actions',
										scope : $scope,
										buttons : {
											Yes : {
												text : 'Log Me In',
												btnClass : 'btn-purple',
												/**
												 * The client will be
												 * transferred to Logain
												 */
												action : function(button) {
													window.location.href = "http://localhost:8080/CouponWebService/Login/Login.html";

												}
											},
											/**
											 * The client will remain a logout
											 * and will not be able to perform
											 * operations
											 */
											No : function(button) {
											},
										}
									});

								} else {
									self.error = err.data;
									$ngConfirm({
										theme : 'light',
										animation : 'rotateYR',
										closeAnimation : 'scale',
										animationSpeed : 500,
										boxWidth : '30%',
										useBootstrap : false,
										title : 'We Are Sorry!',
										content : err.data
												+ '<br>The Coupon May Not Exist, Or You May Have Already Purchased This Coupon',
										scope : $scope,
										buttons : {
											Yes : {
												text : 'OK',
												btnClass : 'btn-purple',
												action : function(button) {
													/**
													 * if the client press YES,
													 * he will redorect to all
													 * coupons page
													 */
													location.reload();
													$state.go("purchase");
												}
											},
										}
									});
								}

							});

		}
		/**
		 * setOrder function Allows us to change the table order from large to
		 * small according to the parameters given in the class html
		 */
		this.orderB = "";
		this.goUp = false;

		this.setOrder = function(field) {
			this.goUp = (this.orderB != field) ? false : !this.goUp;
			this.orderB = field;
		}

	}
})();