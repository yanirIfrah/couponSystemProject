(function() {

	var module = angular.module("couponSystem");

	module.controller("CreateCompanyCtrl", CreateCompanyCtrlCtor);

	/**
	 * create conpany controller CTRL with ngConfirm - A controller that allows the
	 * create a new companies in the database, under administrator permissions
	 */
	function CreateCompanyCtrlCtor(mainAdminServiceHTTP, $ngConfirm, $state,
			$scope) {

		this.success = false;
		this.failure = false;

		this.createCompany = function() {

			if (this.newCompany == undefined || this.newCompany.id == undefined
					|| this.newCompany.compName == undefined
					|| this.newCompany.password == undefined
					|| this.newCompany.email == undefined) {
				this.success = false;
				this.failure = true;
				return;
			}

			this.success = false;
			this.failure = false;
			var self = this;
			/**
			 * Create company function. The method receives the answer from the
			 * service into the Promise, using the mainAdminService. If the
			 * answer from Servis succeeded, the first function(resp) will run
			 * and return the answer to the client. If the answer from the
			 * service was unsuccessful, the error function will run.<br>
			 * for dialog with the client i use ngConfirm
			 */
			var promisePost = mainAdminServiceHTTP
					.createCompany(this.newCompany);
			promisePost
					.then(
							function(resp) {
								// alert(resp.data);
								self.companies = resp.data;
								self.newCompany = {};
								/**
								 * confirm the creation of the company with $ngConfirm
								 * */ 
								$ngConfirm({
									autoClose : 'No|12000',
									theme : 'light',
									animation : 'rotateYR',
									closeAnimation : 'scale',
									animationSpeed : 500,
									boxWidth : '30%',
									useBootstrap : false,
									title : 'Create Company Succeeded!',
									content : 'By clicking Yes, you will be taken directly to the All Companies page to see the list of all companies',
									scope : $scope,
									buttons : {
										Yes : {
											text : 'Yes',
											btnClass : 'btn-purple',
											/**
											 * if the client press YES,  he will redirect to all companies page
											 * */ 
											action : function(button) {
												$state.go("Getallcompanies");

											}
										},
										No : function(button) {
											/**
											 * if the client press NO,  he will stay in tje same page (create company)
											 * */
											location.reload();
											$state.go("Createcompany");
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
												 * The client will be transferred to Logain
												 * */ 
												action : function(button) {
												window.location.href="http://localhost:8080/CouponWebService/Login/Login.html";

												}
											},
											No : function(button) {
												/**
												 * The client will remain a logout and will not be able to perform operations
												 * */
											},
										}
									});

								 }else{
									 self.error=err.data;
								 }
								self.success = false;
								self.failure = true;
							});

		}

	}

})();