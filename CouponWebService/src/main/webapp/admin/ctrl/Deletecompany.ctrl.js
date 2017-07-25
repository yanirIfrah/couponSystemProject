(function() {

	var module = angular.module("couponSystem");

	module.controller("DeleteCompanyCtrl", DeleteCompanyCtrlCtor);
	/**
	 * delete company controller with ngConfirm - A controller that allows the
	 * deletion of companies from the database, under administrator permissions
	 */
	function DeleteCompanyCtrlCtor(mainAdminServiceHTTP, $ngConfirm, $state,
			$scope) {
		this.companies = [];
		var self = this;
		this.success = false;
		this.failure = false;
		/**
		 * get all companies from the DB using adminServiceHTTP service.
		 */
		var promiseGet = mainAdminServiceHTTP.getAllCompanies();
		promiseGet.then(function(resp) {
			self.companies = resp.data;

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

		/**
		 * Delete company function. The method receives the answer from the
		 * service into the Promise, using the mainAdminService. If the
		 * answer from Servis succeeded, the first function(resp) will run
		 * and return the answer to the client. If the answer from the
		 * service was unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.deleteCompany = function(c) {

			$ngConfirm({
				autoClose : 'No|12000',
				theme : 'light',
				animation : 'rotateYR',
				closeAnimation : 'scale',
				animationSpeed : 500,
				boxWidth : '30%',
				useBootstrap : false,
				title : 'Warning!',
				content : 'Are you sure you want to permanently delete the selected Company?',
				buttons : {
					Yes : {
						text : 'Delete',
						btnClass : 'btn-purple',
						action : function(button) {
							mainAdminServiceHTTP.deleteCompany(c.id)
							$ngConfirm({
								autoClose : 'No|6000',
								theme : 'bootstrap',
								animation : 'top',
								closeAnimation : 'scale',
								title : 'Company Deleted!',
								content : '',
								boxWidth : '30%',
								useBootstrap : false,
								content : 'By clicking <strong>Yes</strong>, you will be taken directly to the All Companies  list',
								scope : $scope,
								buttons : {
									Yes : {
										text : 'Yes',
										btnClass : 'btn-purple',
										action : function(button) {
											/**
											 * if the client press YES, he will
											 * redirect to all companies page
											 */
											location.reload();
											$state.go("Getallcompanies");
										}
									},
									No : function(button) {
										/**
										 * if the client press NO, he will stay
										 * in the same page (delete company)
										 */
										location.reload();
										$state.go("Deletecompany");
									},
								}
							});

						}
					},
					No : function(button) {
						location.reload();
						$state.go("Deletecompany");

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