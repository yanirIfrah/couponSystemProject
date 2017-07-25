(function() {

	var module = angular.module("couponSystem");
	'use strict';
	/**
	 * Directive starRating
	 */
	module
			.directive(
					'starRating',
					function() {
						return {
							scope : {
								rating : '=',
								maxRating : '@',
								readOnly : '@',
								click : "&",
								mouseHover : "&",
								mouseLeave : "&"
							},
							template : "<div style='display: inline-block; margin: 0px; padding: 0px; cursor:pointer; font-size:1.5em' ng-repeat='idx in maxRatings track by $index'> \
                    <img ng-src='{{((hoverValue + _rating) <= $index) && \"image/star-empty-lg.png\" || \"image/star-fill-lg.png\"}}' \
                    ng-Click='isolatedClick($index + 1)' \
                    ng-mouseenter='isolatedMouseHover($index + 1)' \
                    ng-mouseleave='isolatedMouseLeave($index + 1)'></img> \
            </div>",
							compile : function(element, attrs) {
								if (!attrs.maxRating
										|| (Number(attrs.maxRating) <= 0)) {
									attrs.maxRating = '5';
								}
								;
							},
							controller : function($scope) {
								$scope.maxRatings = [];

								for (var i = 1; i <= $scope.maxRating; i++) {
									$scope.maxRatings.push({});
								}
								;

								$scope._rating = $scope.rating;
								// on click, the parameter is passed to the
								// fixed star rating and redefines the rating
								// given by the user
								$scope.isolatedClick = function(param) {
									if ($scope.readOnly == 'true')
										return;

									$scope.rating = $scope._rating = param;
									$scope.hoverValue = 0;
									$scope.click({
										param : param
									});
								};
								// Allows the mouse to go through the stars and
								// mark them one by one
								$scope.isolatedMouseHover = function(param) {
									if ($scope.readOnly == 'true')
										return;

									$scope._rating = 0;
									$scope.hoverValue = param;
									$scope.mouseHover({
										param : param
									});
								};

								$scope.isolatedMouseLeave = function(param) {
									if ($scope.readOnly == 'true')
										return;

									$scope._rating = $scope.rating;
									$scope.hoverValue = 0;
									$scope.mouseLeave({
										param : param
									});
								};
							}
						};
					})
})();
