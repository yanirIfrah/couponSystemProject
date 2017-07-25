(function() {

	var module = angular.module("couponSystem");
	/**
	 * Purchase coupon controller with ngConfirm - A controller that allows the
	 * deletion of coupons from the database, under customer permissions
	 */
	module.controller("PurchaseCouponCtrl", PurchaseCouponCtrlCtor);

	/**
	 * Purchase Coupon function. The method receives the answer from the service
	 * into the Promise, using the mainCustomerService. If the answer from
	 * Servis succeeded, the first function(resp) will run and return the answer
	 * to the client. If the answer from the service was unsuccessful, the error
	 * function will run.
	 */
	function PurchaseCouponCtrlCtor(mainCustomerServiceHTTP, $ngConfirm, $scope) {
		this.coupons = [];
		this.mycoupons = [];

		var promiseGet = mainCustomerServiceHTTP.getAllPurchasedCoupons();
		promiseGet
				.then(
						function(resp) {

							self.mycoupons = resp.data;
						},
						function(err) {
							self.error = (err.data);
							// check if the client is loged to perform actions
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
											 * The client will be transferred to
											 * Logain
											 */
											action : function(button) {
												window.location.href = "http://localhost:8080/CouponWebService/Login/Login.html";

											}
										},
										/**
										 * The client will remain a logout and
										 * will not be able to perform
										 * operations
										 */
										No : function(button) {
										},
									}
								});

							} else {
								self.error = err.data;
							}
						});
		/**
		 * onSubmit method To prevent execution by clicking on the form button
		 * and allow validation on the form by the required property in the form
		 * fields
		 */
		this.onSubmit = function(state) {
			// alert(state);
			if (state == true) {
				this.purchaseCoupon();
			}
		}
		/**
		 * Purchase Coupon function. The method get the id as a parameter in
		 * sand it to the service and receives the answer from the service into
		 * the Promise, using the mainCustomerService. If the answer from Servis
		 * succeeded, the first function(resp) will run and return the answer to
		 * the client. If the answer from the service was unsuccessful, the
		 * error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.purchaseCoupon = function(id) {

			var self = this;

			var promisePost = mainCustomerServiceHTTP.purchaseCoupon(this.id);

			promisePost
					.then(
							function(resp) {
								// console.log(this.id);
								$ngConfirm({
									theme : 'light',
									animation : 'rotateYR',
									closeAnimation : 'scale',
									animationSpeed : 500,
									boxWidth : '30%',
									useBootstrap : false,
									title : 'Coupon Was Seccesfully Purchased!',
									content : '<strong>Thank you very much for your purchase.</strong><br>We hope you enjoyed your purchase experience !<br>Hope to see you again ..',
									scope : $scope,
									buttons : {
										Yes : {
											text : 'OK',
											btnClass : 'btn-purple',
											action : function(button) {
												/**
												 * if the client press YES, he
												 * will redorect to all coupons
												 * page
												 */
												location.reload();
												$state.go("purchase");
											}
										},
									}
								});
							},
							function(err) {
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
												 * if the client press YES, he
												 * will redorect to all coupons
												 * page
												 */
												location.reload();
												$state.go("purchase");
											}
										},
									}
								});
							});
		}
	}
})();