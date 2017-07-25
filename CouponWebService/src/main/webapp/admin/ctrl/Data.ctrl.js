(function() {

	var module = angular.module("couponSystem");
	/**
	 * MyController, Which is responsible for drawing the chart inside the admin page
	 */
	module.controller('MyController', function($scope) {
		this.companies = [];
		var self = this;

		// chart data source
		$scope.dataSource = {
				"chart": {
			        "caption": "USER CHART",
			        "subCaption": "properties",
			        "dataFormat" :"json",
			        "xAxisName": "USERS",
			        "yAxisName": "NUMBER",
			        "paletteColors": "#0075c2",
			        "bgColor": "#ffffff",
			        "borderAlpha": "20",
			        "plotBorderAlpha": "10",
			        "placevaluesInside": "1",
			        "rotatevalues": "1",
			        "valueFontColor": "#ffffff",
			        "showXAxisLine": "1",
			        "xAxisLineColor": "#999999",
			        "divlineColor": "#999999",
			        "divLineDashed": "1",
			        "showAlternateHGridColor": "0",
			        "subcaptionFontBold": "0",
			        "subcaptionFontSize": "14"
			    },
			"data" : [ {
				"label" : "No' of companies",
				"value" : "15"
			},
			{
				"label" : "No' of customers",
				"value" : "11"
			},
			{
				"label" : "No' of coupons",
				"value" : "18"
			},
			// more chart data
			]
		};
	});
})();

//**********************************************
//(function() {
//
//	var module = angular.module("couponSystem");
//	
//	/**
//	 * MyController, Which is responsible for drawing the chart inside the admin page
//	 */
//	module.controller('MyController', ctrlFunction);
//	
//	function ctrlFunction($scope, mainAdminServiceHTTP) {
//		this.companies = [];
//		var self = this;
//		self.companies = {};
//		var promise = mainAdminServiceHTTP.getAllCompanies();
//		promise.then(function(resp) {
//
//			self.companies = resp.data;
//			var a = self.companies.length;
//			$scope.dataSource = {
//					"chart": {
//				        "caption": "USER CHART",
//				        "subCaption": "properties",
//				        "xAxisName": "USERS",
//				        "yAxisName": "NUMBER",
//				        "paletteColors": "#0075c2",
//				        "bgColor": "#ffffff",
//				        "borderAlpha": "20",
//				        "plotBorderAlpha": "10",
//				        "placevaluesInside": "1",
//				        "rotatevalues": "1",
//				        "valueFontColor": "#ffffff",
//				        "showXAxisLine": "1",
//				        "xAxisLineColor": "#999999",
//				        "divlineColor": "#999999",
//				        "divLineDashed": "1",
//				        "showAlternateHGridColor": "0",
//				        "subcaptionFontBold": "0",
//				        "subcaptionFontSize": "14"
//				    },
//				"data" : [ {
//					"label" : "No' of companies",
//					"value" : a
//				},
//				{
//					"label" : "No' of customers",
//					"value" : "11"
//				},
//				{
//					"label" : "No' of coupons",
//					"value" : "18"
//				},
//				// more chart data
//				]
//			};
//		});
//		
//		console.log("companies: " , self.companies)
//
//	};
//})();





