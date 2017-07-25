(function () {
 
    var module = angular.module("couponSystem");

        // http://stackoverflow.com/questions/41211875/angularjs-1-6-0-latest-now-routes-not-working
        module.config(['$locationProvider', function ($locationProvider) {
                $locationProvider.hashPrefix('');
            }]);

        // router config
        module.config(function ($stateProvider, $urlRouterProvider) {

            $stateProvider
            
            .state("/getAllPurchasedCoupons", {
                url: "/get",
                templateUrl: 'html/getallpurchasedcoupons.html',
                controller: "GetAllCouponsCtrl as g"
            })

            .state("couponbytype", {
                url: "/gettype",
                templateUrl: "html/couponbytype.html",
                controller: "CouponByTypeCtrl as t"
            })
            .state("purchasedbyprice", {
                url: "/getprice",
                templateUrl: "html/couponbyprice.html",
                controller: "CouponsByPriceCtrl as pr"
            })
            .state("purchaseCoupon", {
                url: "/purchase",
                templateUrl: "html/purchasecoupon.html",
                controller: "PurchaseCouponCtrl as pu"
            })
            .state("about", {
                url: "/about",
                templateUrl: "html/about.html",
                controller: "AboutCtrl as ab"
            })
              .state("contact", {
                url: "/contact",
                templateUrl: "html/contact.html",
                controller: "ContactCtrl as con"
            })
            .state("home", {
            	url: "/home",
            	templateUrl: "html/home.html",
            })
            ;

        $urlRouterProvider.when("", "/home"); // first browsing postfix is empty --> route it to /main
        // $urlRouterProvider.otherwise('/404'); // when no switch case matches --> route to /404
    });
 
})();