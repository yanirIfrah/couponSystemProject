(function () {
 
    var module = angular.module("couponSystem");

        // http://stackoverflow.com/questions/41211875/angularjs-1-6-0-latest-now-routes-not-working
        module.config(['$locationProvider', function ($locationProvider) {
                $locationProvider.hashPrefix('');
            }]);

        // router config
        module.config(function ($stateProvider, $urlRouterProvider) {

            $stateProvider
            
            .state("/Createcompany", {
                url: "/Createcompany",
                templateUrl: 'html/Createcompany.html',
                controller: "CreateCompanyCtrl as cco"
            })
             .state("/Updatecompany", {
                url: "/Updatecompany",
                templateUrl: 'html/Updatecompany.html',
                controller: "UpdateCompanyCtrl as upco"
            })

            .state("Deletecompany", {
                url: "/Deletecompany",
                templateUrl: "html/Deletecompany.html",
                controller: "DeleteCompanyCtrl as dco"
            })
            .state("Getcompany", {
                url: "/Getcompany",
                templateUrl: "html/Getcompany.html",
                controller: "GetCompanyCtrl as gco"
            })
            .state("Getallcompanies", {
                url: "/Getallcompanies",
                templateUrl: "html/Getallcompanies.html",
                controller: "GetAllCompaniesCtrl as gaco"
            })
            .state("Createcustomer", {
                url: "/Createcustomer",
                templateUrl: "html/Createcustomer.html",
                controller: "CreateCustomerCtrl as ccu"
            })
             .state("Updatecustomer", {
                url: "/Updatecustomer",
                templateUrl: "html/Updatecustomer.html",
                controller: "UpdateCustomerCtrl as upcu"
            })
              .state("Deletecustomer", {
                url: "/Deletecustomer",
                templateUrl: "html/Deletecustomer.html",
                controller: "DeleteCustomerCtrl as dcu"
            })
             .state("Getcustomer", {
                url: "/Getcustomer",
                templateUrl: "html/Getcustomer.html",
                controller: "GetCustomerCtrl as gcu"
            })
             .state("Getallcustomers", {
                url: "/Getallcustomers",
                templateUrl: "html/Getallcustomers.html",
                controller: "GetAllCustomersCtrl as gacu"
            })
             .state("Contact", {
                url: "/Contact",
                templateUrl: "html/Contact.html",
                controller: "ContactCtrl as con"
            })
            .state("Home", {
            	url: "/Home",
            	templateUrl: "html/Home.html",
            })
            ;

        $urlRouterProvider.when("", "/Home"); // first browsing postfix is empty --> route it to /main
         $urlRouterProvider.otherwise('/404'); // when no switch case matches --> route to /404
    });
 
})();