(function() {

	var module = angular.module("couponSystem");

	module.controller("DeleteCustomerCtrl", DeleteCustomerCtrlCtor);
	/**
	 * delete Customer controller with ngConfirm - A controller that allows the
	 * deletion of Customers from the database, under administrator permissions
	 */
	function DeleteCustomerCtrlCtor(mainAdminServiceHTTP, $ngConfirm, $state,
			$scope) {
		this.customers = [];
		var self = this;
		this.success = false;
		this.failure = false;
		/**
		 * get all Customers from the DB using adminServiceHTTP service.
		 */
		var promiseGet = mainAdminServiceHTTP.getAllCustomers();
		promiseGet.then(function(resp) {
			self.customers = resp.data;

		}, function(err) {
			self.error = (err.data);
			//check if the client is loged to perform actions
			if((err.status)==401){
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
							text : 'Take me to the login form',
							btnClass : 'btn-purple',
							/**
							 * if the client press YES,  he will redorect to all companies page
							 * */ 
							action : function(button) {
							window.location.href="http://localhost:8080/CouponWebService/Login/Login.html";

							}
						},
						No : function(button) {
							/**
							 * if the client press NO,  he will stay in tje same page (create company)
							 * */
						},
					}
				});

			 }else{
				 self.error=err.data;
			 }
		});

		/**
		 * Delete customer function. The method receives the answer from the
		 * service into the Promise, using the mainAdminService. If the
		 * answer from Servis succeeded, the first function(resp) will run
		 * and return the answer to the client. If the answer from the
		 * service was unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.deleteCustomer = function(c) {
			$ngConfirm({
				autoClose : 'No|12000',
				theme : 'dark',
				animation : 'rotateYR',
				closeAnimation : 'scale',
				animationSpeed : 500,
				boxWidth : '30%',
				useBootstrap : false,
				title : 'Warning!',
				content : 'Are you sure you want to permanently delete the selected customer?',
				buttons : {
					Yes : {
						text : 'Delete',
						btnClass : 'btn-purple',
						action : function(button) {
							mainAdminServiceHTTP.deleteCustomer(c.id)
							$ngConfirm({
								autoClose : 'No|6000',
								theme : 'bootstrap',
								animation : 'top',
								closeAnimation : 'scale',
								title : 'Customer Deleted!',
								content : '',
								boxWidth : '30%',
								useBootstrap : false,
								content : 'By clicking Yes, you will be taken directly to the All Customers page to see the list of companies',
								scope : $scope,
								buttons : {
									Yes : {
										text : 'Yes',
										btnClass : 'btn-purple',
										action : function(button) {
											/**
											 * if the client press YES, he will
											 * redirect to all customers page
											 */
											location.reload();
											$state.go("Getallcustomers");
										}
									},
									No : function(button) {
										/**
										 * if the client press NO, he will stay
										 * in the same page (create cutomer)
										 */
										location.reload();
										$state.go("Deletecustomer");
									},
								}
							});

						}
					},
					No : function(button) {
						location.reload();
						$state.go("Deletecustomer");

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
