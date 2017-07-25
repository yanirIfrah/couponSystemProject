(function() {

	var module = angular.module("couponSystem");

	module.controller("GetAllCompaniesCtrl", GetAllCompaniesCtrlCtor);
	/**
	 * get all Companies controller CTRL with ngConfirm - A controller that
	 * allows the get all of Companies from the database, under administrator
	 * permissions
	 */
	function GetAllCompaniesCtrlCtor(mainAdminServiceHTTP, $ngConfirm, $state,
			$scope) {
		this.companies = [];
		var self = this;
		/**
		 * Get all companies function. The method receives the answer from the
		 * service into the Promise, using the mainAdminService. If the
		 * answer from Servis succeeded, the first function(resp) will run
		 * and return the answer to the client. If the answer from the
		 * service was unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		var promise = mainAdminServiceHTTP.getAllCompanies();
		promise.then(function(resp) {

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