

(function() {
	'use strict';
	angular.module('couponSystem')
		/**
	 * A controller who is responsible for showing an html template from
	 * Footer.html page into the customer main page (S.P) and set to display on the
	 * company page as an <strong>element<strong>.
	 */
	.controller('footerController', ['$scope', function($scope) {
		
	}])
	.directive('myDirective', function() {
		return {
			restrict: 'E',
			templateUrl: 'html/Footer.html'
		};
	});
	
})();

