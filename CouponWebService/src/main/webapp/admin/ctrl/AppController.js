(function() {

	var module = angular.module("couponSystem");

'use strict';
/**
 * appController for star rating in admin page.
 * */
module.controller('appController', ['$scope', function ($scope) {
    $scope.starRating1 = 4;
    $scope.starRating2 = 5;
    $scope.starRating3 = 2;
    $scope.hoverRating1 = $scope.hoverRating2 = $scope.hoverRating3 = 0;

    $scope.click1 = function (param) {
    };

    $scope.mouseHover1 = function (param) {
        $scope.hoverRating1 = param;
    };

    $scope.mouseLeave1 = function (param) {
        $scope.hoverRating1 = param + '*';
    };

    $scope.click2 = function (param) {
    };

    $scope.mouseHover2 = function (param) {
        $scope.hoverRating1 = param;
    };

    $scope.mouseLeave2 = function (param) {
        $scope.hoverRating2 = param + '*';
    };

    $scope.click3 = function (param) {
    };

    $scope.mouseHover3 = function (param) {
        $scope.hoverRating3 = param;
    };

    $scope.mouseLeave3 = function (param) {
        $scope.hoverRating3 = param + '*';
    };
}]);
})();