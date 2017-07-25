(function() {

	var module = angular.module("couponSystem");

	module.controller("CreateCouponCtrl", CreateCouponCtrlCtor);
	/**
	 * create coupon controller CTRL with ngConfirm - A controller that allows
	 * the create a new coupon in the database, under company permissions
	 */
	function CreateCouponCtrlCtor(mainCompanyService, $ngConfirm, $state,
			$scope) {
		
		this.success = false;
		this.failure = false;
		// *****************************************************************
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
		 * $watch on the start date and end date and respond to the client,
		 * based on the start and end date of the coupon. create A date as today
		 * date for comparing to startDate,using the compare method, check if
		 * the startDate is Earlier then today. The system will not allow you to
		 * select a date that is earlier than today, and it will prevent
		 * entering the such a date and reset the start date fiel in the form
		 * html
		 * 
		 */
		$scope.$watch('[c.newCoupon.startDate, c.newCoupon.endDate]', () => {
			var sDate = $('#sDate').val(),
				eDate = $('#eDate').val();
			if(!(sDate == "")  &&  !(eDate == "")) {
				// create a new date to compare with start date
				var today = new Date();
				var day = today.getDate() < 10 ? '0'+today.getDate()  : today.getDate() ;
				var month = (today.getMonth()+1) < 10 ? '0'+ (today.getMonth()+1) :  today.getMonth(); 
				today = today.getFullYear() + "-" + month + "-" + day;
				// compare start date and today for checking if start date is
				// erlier from today
				if(dates.compare(sDate, today) == -1) {
					$('#sDate').val("");
					$ngConfirm({
						theme : 'light',
						animation : 'rotateYR',
						closeAnimation : 'scale',
						animationSpeed : 500,
						boxWidth : '30%',
						useBootstrap : false,
						title : 'Choose A New Date!',
						content : 'The Coupon Must Start From Today',
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
				// compare start date and end date
				var datesAnswer = dates.compare(sDate, eDate);
				
				if(datesAnswer == 1) {// start date is bigger then end date
					// clear the start date field in the form
					$('#sDate').val("");
					$ngConfirm({
						theme : 'light',
						animation : 'rotateYR',
						closeAnimation : 'scale',
						animationSpeed : 500,
						boxWidth : '30%',
						useBootstrap : false,
						title : 'Coupon Start date is invalid!',
						content : 'The start date of the coupon MUST be before the end date of the coupon.<br>Please select a valid start date !',
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
				// start date and end date rae equals.
				else if(datesAnswer == 0) {// both dates are equal
					$('#eDate').val("");
					$ngConfirm({
						theme : 'light',
						animation : 'rotateYR',
						closeAnimation : 'scale',
						animationSpeed : 500,
						boxWidth : '30%',
						useBootstrap : false,
						title : 'NOTE!',
						content : 'You can not create a "one day" Coupon ! ! !<br>Please select a valid start date and end date',
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
				else {				
					return;
				}
			}
			else {
				if(sDate == "")
					$('#sDate').val("");
				if(eDate == "")
					$('#eDate').val("");
				
				this.success = false;
				this.failure = false;
				return;
			}
		});
		// *****************************************************************
		/**
		 * onSubmit method To prevent execution by clicking on the form button
		 * and allow validation on the form by the required property in the form
		 * fields
		 */
		this.onSubmit = function(state) {
			if (state == true) {
				this.createCoupon();
			}
		}
		/**
		 * create coupon function. The method receives the answer from the
		 * service into the Promise, using the mainCompanyService. If the answer
		 * from Servis succeeded, the first function(resp) will run and return
		 * the answer to the client. If the answer from the service was
		 * unsuccessful, the error function will run.<br>
		 * for dialog with the client i use ngConfirm
		 */
		this.createCoupon = function() {
			if (this.newCoupon == undefined || this.newCoupon.id == undefined
					|| this.newCoupon.title == undefined
					|| this.newCoupon.startDate == undefined
					|| this.newCoupon.endDate == undefined
					|| this.newCoupon.amount == undefined
					|| this.newCoupon.type == undefined
					|| this.newCoupon.message == undefined
					|| this.newCoupon.price == undefined
					|| this.newCoupon.image == undefined) {
				return;
			}
			
			this.success = false;
			this.failure = false;
			var self = this;
			
			var promisePost = mainCompanyService.createCoupon(this.newCoupon);
			
			promisePost.then(function(resp) {
				
				self.coupons = resp.data;
				self.newCoupon = {};
				
				/**
				 * confirm the creation of the company with $ngConfirm
				 */ 
				$ngConfirm({
					autoClose : 'No|12000',
					theme : 'light',
					animation : 'rotateYR',
					closeAnimation : 'scale',
					animationSpeed : 500,
					boxWidth : '30%',
					useBootstrap : false,
					title : 'Create Company Succeeded!',
					content : 'By clicking Yes, you will be taken directly to the All Coupons page to see the list of your Coupns<br>Would you like that?',
					scope : $scope,
					buttons : {
						Yes : {
							text : 'Yes',
							btnClass : 'btn-purple',
							/**
							 * if the client press YES, he will redirect to all
							 * coupons page
							 */ 
							action : function(button) {
								$state.go("getallcoupons");

							}
						},
						No : function(button) {
							/**
							 * if the client press NO, he will stay in the same
							 * page (create coupon)
							 */
							location.reload();
							$state.go("createcoupon");
						},
					}
				});


			}, function(err) {
				self.error = (err.data);
				// check if the client is loged to perform
				// actions
				if((err.status)==401){
					$ngConfirm({
						theme : 'light',
						animation : 'rotateYR',
						closeAnimation : 'scale',
						animationSpeed : 500,
						boxWidth : '30%',
						useBootstrap : false,
						title : 'Login failed!',
						content : 'You must be login to perform actions',
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
							 * The client will remain a logout and will not be
							 * able to perform operations
							 */
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