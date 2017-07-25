(function() {

	var module = angular.module("couponSystem");

	module.controller("GetAllCustomersCtrl", GetAllCustomersCtrlCtor);
	/**
	 * get all Customers controller with ngConfirm - A controller that allows to get
	 * all of Customers from the database, under administrator permissions
	 */
	function GetAllCustomersCtrlCtor(mainAdminServiceHTTP, $ngConfirm, $state,
			$scope) {
		this.customers = [];
		var self = this;
		/**
		 * Get all customers function. The method receives the answer from the
		 * service into the Promise, using the mainAdminService. If the
		 * answer from Servis succeeded, the first function(resp) will run
		 * and return the answer to the client. If the answer from the
		 * service was unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		var promise = mainAdminServiceHTTP.getAllCustomers();
		promise.then(function(resp) {
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
							 * The client will be transferred to Logain
							 * */ 
							action : function(button) {
							window.location.href="http://localhost:8080/CouponWebService/Login/Login.html";

							}
						},
						/**
						 * The client will remain a logout and will not be able
						 * to perform operations
						 */
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