(function() {

	var module = angular.module("couponSystem");

	module.controller("UpdateCompanyCtrl", UpdateCompanyCtrlCtor);
	/**
	 * update conpany controller with ngConfirm - A controller that allows the
	 * updated of companies in the database, under administrator permissions
	 */
	function UpdateCompanyCtrlCtor(mainAdminServiceHTTP, $ngConfirm, $state,
			$scope) {
		this.companies = [];
		var self = this;

		/**
		 * Get all companies function. The method receives the answer from the
		 * service into the Promise, using the mainAdminService. If the answer
		 * from Servis succeeded, the first function(resp) will run and return
		 * the answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.<br>
		 */
		var promiseGet = mainAdminServiceHTTP.getAllCompanies();
		promiseGet
				.then(
						function(resp) {
							self.companies = resp.data;

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
											 * The client will be
											 * transferred to Logain
											 */
											action : function(button) {
												window.location.href = "http://localhost:8080/CouponWebService/Login/Login.html";

											}
										},
										/**
										 * if the client press NO, he will stay
										 * in the same page (create company)
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

				this.updateCompany();
			}
		}
		/**
		 * update company function. The method receives the answer from the
		 * service into the Promise, using the mainAdminService. If the answer
		 * from Servis succeeded, the first function(resp) will run and return
		 * the answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.updateCompany = function() {
			var promiseGet = mainAdminServiceHTTP
					.updateCompany(this.updatedCompany);
			promiseGet
					.then(
							function(resp) {

								$ngConfirm({
									autoClose : 'No | 12000',
									theme : 'dark',
									animation : 'rotateYR',
									closeAnimation : 'scale',
									animationSpeed : 500,
									boxWidth : '30%',
									useBootstrap : false,
									title : 'Update Company Succeeded!',
									content : 'By clicking Yes, you will be taken directly to the All Companies list!<br>Whuld Tou Like That?',
									scope : $scope,
									buttons : {
										Yes : {
											text : 'Yes',
											btnClass : 'btn-purple',
											action : function(button) {
												/**
												 * if the client press YES, he
												 * will redirect to all
												 * companies page
												 */
												location.reload();
												$state.go("Getallcompanies");

											}
										},
										No : function(button) {
											/**
											 * if the client press NO, he will
											 * stay in the same page (update
											 * company)
											 */
											location.reload();
											$state.go("Updatecompanies");
										},
									}
								});
							}, function(err) {
								self.error = (err.data);
							});
		}
		/**
		 * updateMode Allows us to switch to the status of updating details in
		 * the table of all companies on the html page.(updateCompany.html)
		 */

		this.updateMode = function(c) {
			for (var i = 0; i < this.companies.length; i++) {
				this.companies[i].edit = false;

			}
			c.edit = true;
			this.updatedCompany = {};
			this.updatedCompany.id = c.id;

			this.updatedCompany.compName = c.compName;
			console.log(this.updatedCompany);

		}
		/**
		 * closeUpdateMode closing updateMode in the table of all customers on
		 * the html page
		 */
		this.closeUpdateMode = function(c) {
			for (var i = 0; i < this.companies.length; i++) {
				this.companies[i].edit = false;
			}
			c.edit = false;

		}

		/**
		 * setOrder function Allows user to change the table order from large to
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