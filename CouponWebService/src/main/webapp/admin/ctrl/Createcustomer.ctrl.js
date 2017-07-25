(function() {

	var module = angular.module("couponSystem");

	module.controller("CreateCustomerCtrl", CreateCustomerCtrlCtor);

	/**
	 * create Customer controller CTRL with ngConfirm - A controller that allows
	 * the create a new Customers in the database, under administrator
	 * permissions
	 */
	function CreateCustomerCtrlCtor(mainAdminServiceHTTP, $ngConfirm, $state,
			$scope) {

		this.success = false;
		this.failure = false;

		this.createCustomer = function() {
			/**
			 * Check if the client is trying to create a customer without typing the
			 * company information
			 */
			if (this.newCustomer == undefined
					|| this.newCustomer.id == undefined
					|| this.newCustomer.custName == undefined
					|| this.newCustomer.password == undefined) {
				this.success = false;
				this.failure = true;
				return;
			}
			this.success = false;
			this.failure = false;
			var self = this;
			/**
			 * create customer function. The method receives the answer from the
			 * service into the Promise, using the mainAdminService. If the
			 * answer from Servis succeeded, the first function(resp) will run
			 * and return the answer to the client. If the answer from the
			 * service was unsuccessful, the error function will run.<br>
			 * for dialog with the client i use ngConfirm
			 */
			var promisePost = mainAdminServiceHTTP
					.createCustomer(this.newCustomer);
			promisePost
					.then(
							function(resp) {
								// alert(resp.data);
								self.customers = resp.data;
								self.newCustomer = {};
								/**
								 * confirm the creation of the new customer with
								 * $ngConfirm
								 */
								$ngConfirm({
									autoClose : 'No|12000',
									theme : 'light',
									animation : 'rotateYR',
									closeAnimation : 'scale',
									animationSpeed : 500,
									boxWidth : '30%',
									useBootstrap : false,
									title : 'Create Customer Succeeded!',
									content : 'By clicking Yes, you will be taken directly to the All Customers page to see the list of Customers',
									scope : $scope,
									buttons : {
										Yes : {
											text : 'Yes',
											btnClass : 'btn-purple',
											/**
											 * if the client press YES, he will
											 * redirect to all customres page
											 */
											action : function(button) {
												$state.go("Getallcustomers");

											}
										},
										No : function(button) {
											/**
											 * if the client press NO, he will
											 * stay in the same page (create
											 * customer)
											 */
											location.reload();
											$state.go("Createcustomers");
										},
									}
								});

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
												 * The client will be transferred to Login
												 * */ 
												action : function(button) {
												window.location.href="http://localhost:8080/CouponWebService/Login/Login.html";

												}
											},
											/**
											 * The client will remain a logout and will not be able to perform operations
											 * */
											No : function(button) {
											},
										}
									});

								 }else{
									 self.error=err.data;
								 }
							});

		}

	}

})();