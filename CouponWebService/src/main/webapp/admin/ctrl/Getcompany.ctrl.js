(function() {

	var module = angular.module("couponSystem");

	module.controller("GetCompanyCtrl", GetCompanyCtrlCtor);
	/**
	 * get Company controller with ngConfirm - A controller that allows the get
	 * Company from the database by id, under administrator permissions, by
	 * using the getId function
	 */
	function GetCompanyCtrlCtor(mainAdminServiceHTTP, $ngConfirm, $scope) {
		var company = {};
		var self = this;
		/**
		 * onSubmit method To prevent execution by clicking on the form button
		 * and allow validation on the form by the required property in the form
		 * fields
		 */
		this.onSubmit = function(state) {
			if (state == true) {
				this.getId();
			}
		}
		/**
		 * Get company by id function. get a specific company from the DB by
		 * given a specific Id as a parameter in the function. The method
		 * receives the answer from the service into the Promise, using the
		 * mainAdminService. If the answer from Servis succeeded, the first
		 * function(resp) will run and return the answer to the client. If the
		 * answer from the service was unsuccessful, the error function will
		 * run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.getId = function() {

			var promiseGet = mainAdminServiceHTTP.getCompanyById(this.id);

			promiseGet
					.then(
							function(resp) {
								self.company = resp.data;
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
											 * if the client press NO, he will
											 * stay in the same page (create
											 * company)
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
										content : 'There may be no company with the ID youre looking for<br>Please try new ID!',
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
	}

})();