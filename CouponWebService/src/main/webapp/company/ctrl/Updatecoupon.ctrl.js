(function() {

	var module = angular.module("couponSystem");
	/**
	 * update Coupon controller with ngConfirm - A controller that allows to get
	 * of coupons from the database, under company permissions
	 */
	module.controller("updateCouponCtrl", updateCouponCtrlCtor);

	function updateCouponCtrlCtor(mainCompanyService, $ngConfirm, $state,
			$scope) {
		this.coupons = [];
		var self = this;
		// *******************************************************************
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
		$scope.$watch('[up.updatedCoupon.endDate]', () => {
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
		// **********************************************************************
		/**
		 * Get all coupons function. The method receives the answer from the
		 * service into the Promise, using the mainCompanyService. If the answer
		 * from Servis succeeded, the first function(resp) will run and return
		 * the answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.
		 */
		var promiseGet = mainCompanyService.getAllCoupons();
		promiseGet.then(function(resp) {

			self.coupons = resp.data;

		}, function(err) {
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
							 * The client will be transferred to Logain
							 */ 
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
		 * onSubmit method To prevent execution by clicking on the form button
		 * and allow validation on the form by the required property in the form
		 * fields
		 */
		this.onSubmit = function(state) {
			if (state == true) {
				this.updateTheCoupon();
			}
		}
		/**
		 * update coupon function. The method receives the answer from the
		 * service into the Promise, using the mainAdminService. If the answer
		 * from Servis succeeded, the first function(resp) will run and return
		 * the answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.updateTheCoupon = function() {

			var promisePost = mainCompanyService
					.updateCoupon(this.updatedCoupon);
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
									title : 'Update Coupon Succeeded!',
									content : 'By clicking YES, you will be taken directly to the All Coupons page to see the list of all your Coupons.<br>Would you like that?',
									scope : $scope,
									buttons : {
										Yes : {
											text : 'Yes',
											btnClass : 'btn-purple',
											action : function(button) {
												/**
												 * if the client press YES, he
												 * will redirect to all get all
												 * coupons page
												 */
												$state.go("getallcoupons");

											}
										},
										No : function(button) {
											/**
											 * if the client press NO, he will
											 * stay in the same page - update
											 * coupons
											 */
											location.reload();
											$state.go("updatecoupons");
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
			for (var i = 0; i < this.coupons.length; i++) {
				this.coupons[i].edit = false;
			}
			c.edit = true;
			this.updatedCoupon = {};
			this.updatedCoupon.id = c.id;
			this.updatedCoupon.title = c.title;
			this.updatedCoupon.startDate = c.startDate;
			this.updatedCoupon.amount = c.amount;
			this.updatedCoupon.type = c.type;
			this.updatedCoupon.message = c.message;
			this.updatedCoupon.image = c.image;
			console.log(this.updatedCoupon);

		}
		/**
		 * closeUpdateMode closing updateMode in the table of all customers on
		 * the html page
		 */
		this.closeUpdateMode = function(c) {
			for (var i = 0; i < this.coupons.length; i++) {
				this.coupons[i].edit = false;
			}
			c.edit = false;

		}

		/**
		 * setOrder function Allows us to change the table order from large to
		 * small according to the parameters given in the html file
		 */
		this.orderB = "";
		this.goUp = false;

		this.setOrder = function(field) {
			this.goUp = (this.orderB != field) ? false : !this.goUp;
			this.orderB = field;
		}

	}

})();