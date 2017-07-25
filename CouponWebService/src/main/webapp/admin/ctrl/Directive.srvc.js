
(function() {
	'use strict';
	angular.module('couponSystem')
	/**
	 * A directive who is responsible for showing an html template from
	 * Footer.html page into the admin's main page and set to display on the
	 * admin page as an <strong>element<strong>.
	 */
	.directive('myDirective', function() {
		return {
			'restrict' : 'E',
			'templateUrl' : 'html/Footer.html',
		};
	});

})();
