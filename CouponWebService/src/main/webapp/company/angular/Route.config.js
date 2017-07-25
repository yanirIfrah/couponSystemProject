(function () {
 
    var module = angular.module("couponSystem");

        module.config(['$locationProvider', function ($locationProvider) {
                $locationProvider.hashPrefix('');
            }]);

        // router config
        module.config(function ($stateProvider, $urlRouterProvider) {

            $stateProvider
            
            .state("/createcoupon", {
                url: "/create",
                templateUrl: 'html/createcoupon.html',
                controller: "CreateCouponCtrl as c"
            })

            .state("deletecoupon", {
                url: "/delete",
                templateUrl: "html/deletecoupon.html",
                controller: "DeleteCouponCtrl as d"
            })
            .state("updatecoupon", {
                url: "/update",
                templateUrl: "html/updatecoupon.html",
                controller: "updateCouponCtrl as up"
            })
            .state("getcouponbyid", {
                url: "/getid",
                templateUrl: "html/getcouponbyid.html",
                controller: "GetCouponByIdCtrl as gid"
            })
            .state("getallcoupons", {
                url: "/getall",
                templateUrl: "html/getallcoupons.html",
                controller: "GetAllCouponsCtrl as gall"
            })
             .state("getcouponsbytype", {
                url: "/gettype",
                templateUrl: "html/getcouponsbytype.html",
                controller: "GetCouponByTypeCtrl as gt"
            })
              .state("getcouponsbyprice", {
                url: "/getprice",
                templateUrl: "html/getcouponsbyprice.html",
                controller: "GetCouponsByPriceCtrl as gp"
            })
             .state("getcouponsbydate", {
                url: "/getdate",
                templateUrl: "html/getcouponsbydate.html",
                controller: "GetCouponByDateCtrl as gd"
            })
             .state("getcompany", {
                url: "/getcompany",
                templateUrl: "html/getcompany.html",
                controller: "GetCompanyCtrl as gc"
            })
             .state("about", {
                url: "/about",
                templateUrl: "html/about.html",
                controller: "aboutCtrl as ab"
            })
              .state("contact", {
                url: "/contact",
                templateUrl: "html/contact.html",
                controller: "ContactCtrl as con"
            })
            .state("home", {
            	url: "/home",
            	templateUrl: "html/home.html",
//            	controller: "ContactCtrl as con"
            })
            ;

        $urlRouterProvider.when("", "/home"); // first browsing postfix is empty --> route it to /main
        // $urlRouterProvider.otherwise('/404'); // when no switch case matches --> route to /404
    });
 
})();