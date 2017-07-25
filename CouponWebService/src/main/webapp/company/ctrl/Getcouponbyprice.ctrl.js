(function() {

	var module = angular.module("couponSystem");

	module.controller("GetCouponsByPriceCtrl", GetCouponsByPriceCtrlCtor);

	function GetCouponsByPriceCtrlCtor(mainCompanyService, $ngConfirm, $state,
			$scope) {
		this.coupons = [];
		var self = this;
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
		/**
		 * get Price function. The method The method get the price as a
		 * parameter in sand it to the service and receives the answer from the
		 * service into the Promise, using the mainCompanyService. If the answer
		 * from Servis succeeded, the first function(resp) will run and return
		 * the answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.getPrice = function() {

			var promise = mainCompanyService.getCouponsByPrice(this.price);
			promise
					.then(
							function(resp) {
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