(function() {

	var module = angular.module("couponSystem");
	/**
	 * Get coupon by type controller with ngConfirm - A controller that allows
	 * the get coupons by type from the database, under customer permissions
	 */
	module.controller("CouponByTypeCtrl", CouponByTypeCtrlCtor);

	function CouponByTypeCtrlCtor(mainCustomerServiceHTTP, $ngConfirm, $scope) {
		this.coupons = [];
		var self = this;
		/**
		 * onSubmit method To prevent execution by clicking on the form button
		 * and allow validation on the form by the required property in the form
		 * fields
		 */
		this.onSubmit = function(state) {
			if (state == true) {
				this.getType();
			}
		}

		/**
		 * get Type function. The method The method get the type as a parameter
		 * in sand it to the service and receives the answer from the service
		 * into the Promise, using the mainCustomerService. If the answer from
		 * Servis succeeded, the first function(resp) will run and return the
		 * answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.getType = function() {

			var promiseGet = mainCustomerServiceHTTP
					.getCouponsByType(this.type);
			promiseGet
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
										title : 'A problem has occurred!',
										content : 'There may be no coupon with the TYPE youre looking for<br>Please try other TYPE!',
										scope : $scope,
										buttons : {
											Yes : {
												text : 'OK',
												btnClass : 'btn-purple',
											},
										}
									});
								}
							});
		}
		/**
		 * setOrder function Allows the user to change the table order from
		 * large to small according to the parameters given in the html file
		 */
		this.orderB = "";
		this.goUp = false;

		this.setOrder = function(field) {
			this.goUp = (this.orderB != field) ? false : !this.goUp;
			this.orderB = field;
		}
	}
})();