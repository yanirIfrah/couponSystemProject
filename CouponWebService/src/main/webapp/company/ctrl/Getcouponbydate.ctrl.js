(function () {

	var module = angular.module("couponSystem");
	/**
	 * Get Coupon By Date controller with ngConfirm - A controller that allows
	 * the updated of Customers in the database, under company permissions
	 */
	module.controller("GetCouponByDateCtrl", GetCouponByDateCtrlCtor);
	/**
	 * Get Coupon By Date CTOR. The constructor receives the answer from the
	 * service into the Promise, using the mainCompanyService. If the answer
	 * from Servis succeeded, the first function(resp) will run and return the
	 * answer to the client. If the answer from the service was unsuccessful,
	 * the error function will run.
	 */
	function GetCouponByDateCtrlCtor(mainCompanyService,$ngConfirm, $state,
			$scope) {
		this.couponsDate = [];
		var self = this;
		// ************************************************************************
		/**
		 * var dates the contain ths convert and compare functions for dates!
		 */
		var dates = {
			/**
			 * Converts the date in d to a date-object. The input can be: a date
			 * object: returned without modification an array : Interpreted as
			 * [year,month,day]. NOTE: month is 0-11. a number : Interpreted as
			 * number of milliseconds since 1 Jan 1970 (a timestamp) a string :
			 * Any format supported by the javascript engine, like "YYYY/MM/DD",
			 * "MM/DD/YYYY", "Jan 31 2009" etc. an object : Interpreted as an
			 * object with year, month and date attributes. **NOTE** month is
			 * 0-11.
			 */
			convert: function(d) {
		        return (
		            d.constructor === Date ? d :
		            d.constructor === Array ? new Date(d[0],d[1],d[2]) :
		            d.constructor === Number ? new Date(d) :
		            d.constructor === String ? new Date(d) :
		            typeof d === "object" ? new Date(d.year,d.month,d.date) :
		            NaN
		        );
		    },
		    /**
			 * Compare two dates (could be of any type supported by the convert
			 * function above) and returns: -1 : if a < b, 0 : if a = b, 1 : if
			 * a > b and NaN : if a or b is an illegal date NOTE: The code
			 * inside isFinite does an assignment (=).
			 */
		    compare: function(a,b) {
		        return (
		            isFinite(a=this.convert(a).valueOf()) &&
		            isFinite(b=this.convert(b).valueOf()) ?
		            (a>b)-(a<b) :
		            NaN
		        );
		    }
	    }
		/**
		 * $watch on the end date and respond to the client, based on the
		 * endDate of the coupon. create A date as today date for comparing to
		 * endDate,using the compare method, check if the endDate is Earlier
		 * then today. The system will not allow you to select a date that is
		 * earlier than today, and it will prevent entering such a date and
		 * reset the endDate field in the form html
		 * 
		 */
		$scope.$watch('[gd.endDate]', () => {
			var eDate = $('#eDate').val();
			if(!(eDate == "")) {
				var today = new Date();
				var day = today.getDate() < 10 ? '0'+today.getDate()  : today.getDate() ;
				var month = (today.getMonth()+1) < 10 ? '0'+ (today.getMonth()+1) :  today.getMonth(); 
				today = today.getFullYear() + "-" + month + "-" + day;
				// compare eDate and today for checking if eDate is
				// erlier from today
				if(dates.compare(eDate, today) == -1) {
					$('#eDate').val("");
					$ngConfirm({
						theme : 'light',
						animation : 'rotateYR',
						closeAnimation : 'scale',
						animationSpeed : 500,
						boxWidth : '30%',
						useBootstrap : false,
						title : 'Choose A New Date!',
						content : 'The Coupon date Must Start From Today<br>Please enter a valid date',
						scope : $scope,
						buttons : {
							Yes : {
								text : 'OK',
								btnClass : 'btn-purple',
							},
						}
					});
					return;
				}
			}
			else {
				if(eDate == "")
					$('#eDate').val("");
				this.success = false;
				this.failure = false;
				return;
			}
		});
		// ************************************************************************
		/**
		 * get Date function. The method get the endDate as a parameter in sand
		 * it to the service and receives the answer from the service into the
		 * Promise, using the mainCompanyService. If the answer from Servis
		 * succeeded, the first function(resp) will run and return the answer to
		 * the client. If the answer from the service was unsuccessful, the
		 * error function will run.<br>
		 */
		this.getDate = function () {
			var promiseGet = mainCompanyService.getCouponsByDate(this.endDate.getTime());

				promiseGet.then(
					function (resp) {
						
						self.couponsDate = resp.data;
					},
	
					function (err) {
						self.error = (err.data);
						// check if the client is loged to perform actions
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
										text : 'Log Me In',
										btnClass : 'btn-purple',
										/**
										 * The client will be transferred to
										 * Logain
										 */ 
										action : function(button) {
										window.location.href="http://localhost:8080/CouponWebService/Login/Login.html";

										}
									},
									/**
									 * The client will remain a logout and will
									 * not be able to perform operations
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
				 * setOrder function Allows us to change the table order from
				 * large to small according to the parameters given in the class
				 * html
				 */	
				this.orderB = "";
				this.goUp = false;
	
				this.setOrder = function (field) {
					this.goUp = (this.orderB != field) ? false
						: !this.goUp;
					this.orderB = field;
				}

		}
	}

})();