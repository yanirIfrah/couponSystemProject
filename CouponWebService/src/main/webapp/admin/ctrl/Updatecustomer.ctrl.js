(function() {

	var module = angular.module("couponSystem");

	module.controller("UpdateCustomerCtrl", UpdateCustomerCtrlCtor);
	/**
	 * update Customer controller with ngConfirm - A controller that allows the
	 * updated of Customers in the database, under Company permissions
	 */
	function UpdateCustomerCtrlCtor(mainAdminServiceHTTP, $ngConfirm, $state,
			$scope) {
		this.customers = [];
		var self = this;
		/**
		 * Get all customers function. The method receives the answer from the
		 * service into the Promise, using the mainCompanyService. If the answer
		 * from Servis succeeded, the first function(resp) will run and return
		 * the answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.
		 */
		var promiseGet = mainAdminServiceHTTP.getAllCustomers();
		promiseGet
				.then(
						function(resp) {
							self.customers = resp.data;

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
											text : 'Take me to the login form',
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
			console.log(state);
			if (state == true) {
				console.log(state);

				this.updateCustomer();
			}
		}
		/**
		 * update customer function. The method receives the answer from the
		 * service into the Promise, using the mainCompanyService. If the answer
		 * from Servis succeeded, the first function(resp) will run and return
		 * the answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.updateCustomer = function() {
			var promisePost = mainAdminServiceHTTP
					.updateCustomer(this.updatedCustomer);
			promisePost
					.then(
							function(resp) {
								$ngConfirm({
									autoClose : 'No | 12000',
									theme : 'light',
									animation : 'rotateYR',
									closeAnimation : 'scale',
									animationSpeed : 500,
									boxWidth : '30%',
									useBootstrap : false,
									title : 'Update Company Succeeded!',
									content : 'By clicking YES, you will be redirect to the All Customers list !<br>Whuld You Like That?',
									scope : $scope,
									buttons : {
										Yes : {
											text : 'Yes',
											btnClass : 'btn-purple',
											action : function(button) {
												/**
												 * if the client press YES, he
												 * will redirect to all
												 * customers page
												 */
												location.reload();
												$state.go("Getallcustomers");

											}
										},
										No : function(button) {
											/**
											 * if the client press NO, he will
											 * stay in the same page (update
											 * customers)
											 */
											location.reload();
											$state.go("Updatecustomer");
										},
									}
								});
							}, function(err) {
								self.error = (err.data);
								self.success = false;
								self.failure = true;
							});
		}
		/**
		 * updateMode Allows us to switch to the status of updating details in
		 * the table of all customers on the html page.(updateCusromer.html)
		 */

		this.updateMode = function(c) {
			for (var i = 0; i < this.customers.length; i++) {
				this.customers[i].edit = false;
			}
			c.edit = true;
			this.updatedCustomer = {};
			this.updatedCustomer.id = c.id;
			this.updatedCustomer.custName = c.custName;
			console.log(this.updatedCustomer);

		}
		/**
		 * closeUpdateMode closing updateMode in the table of all customers on
		 * the html page
		 */
		this.closeUpdateMode = function(c) {
			for (var i = 0; i < this.customers.length; i++) {
				this.customers[i].edit = false;
			}
			c.edit = false;

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
