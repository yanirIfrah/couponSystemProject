(function() {

	var module = angular.module("couponSystem");

	module.controller("DeleteCouponCtrl", DeleteCouponCtrlCtor);
	/**
	 * delete coupon controller with ngConfirm - A controller that allows the
	 * deletion of coupons from the database, under mainCompanyService
	 * permissions
	 */
	function DeleteCouponCtrlCtor(mainCompanyService, $ngConfirm, $state,
			$scope) {
		this.coupons = [];
		var self = this;
		/**
		 * get all coupons from the DB using adminServiceHTTP service.
		 */
		var promiseGet = mainCompanyService.getAllCoupons();
		promiseGet
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
		 * delete coupon function. The method receives the answer from the
		 * service into the Promise, using the mainCompanyService. If the answer
		 * from Servis succeeded, the first function(resp) will run and return
		 * the answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.deleteCoupon = function(c) {
			$ngConfirm({
				autoClose : 'No|12000',
				theme : 'light',
				animation : 'rotateYR',
				closeAnimation : 'scale',
				animationSpeed : 500,
				boxWidth : '30%',
				useBootstrap : false,
				title : 'Warning!',
				content : 'Are you sure you want to permanently delete the selected Coupon?',
				buttons : {
					Yes : {
						text : 'Delete',
						btnClass : 'btn-purple',
						action : function(button) {
							mainCompanyService.deleteCoupon(c.id) // Delete
							// the
							// coupon by
							// pressing
							// the
							// button
							$ngConfirm({
								autoClose : 'No|6000',
								theme : 'bootstrap',
								animation : 'top',
								closeAnimation : 'scale',
								title : 'Coupon Deleted!',
								content : '',
								boxWidth : '30%',
								useBootstrap : false,
								content : 'By clicking <strong>Yes</strong>, you will be taken directly to the All Coupons page to see the list of your coupons',
								scope : $scope,
								buttons : {
									Yes : {
										text : 'Yes',
										btnClass : 'btn-purple',
										action : function(button) {
											/**
											 * if the client press YES, he will
											 * redirect to all coupons page
											 */
											location.reload();
											$state.go("getallcoupons");
										}
									},
									No : function(button) {
										/**
										 * if the client press NO, he will stay
										 * in the same page (delete coupon)
										 */
										location.reload();
										$state.go("deletecoupon");
									},
								}
							});

						}
					},
					No : function(button) {

					},
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