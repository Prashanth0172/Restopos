// var taskManagerModule = angular.module('taskManagerApp', ['ngRoute','ngAnimate']);
//

var app = angular.module('myApp',['ngRoute', 'ngAnimate',
    'ngSanitize','ui-notification','ngTable','chart.js',
    'ngCookies', 'angular.filter','ui.bootstrap']);


app.config(['$routeProvider', function($routeProvider){
    $routeProvider


        .when("/restdashboard", {
            templateUrl: "partials/restdashboard.html",
            controller: "restdashCtrl as rest"
        })
        .when("/masters", {
            templateUrl: "partials/masters.html"
        })
        .when("/restaurantReports", {
            templateUrl: "partials/restaurantReports.html"
        })
        .when("/monthendReport", {
            templateUrl: "partials/report/monthendreport.html",
            controller: "monthendcontroller as rest"
        })
        .when("/shiftReport", {
            templateUrl: "partials/shiftReport.html",
            controller: "shiftController as rest"
        })
        .when("/dayEndinvoice", {
            templateUrl: "partials/dayEndListing.html",
            controller: "salesdayendcontroller as rest"
        })
        .when("/freeMealReport", {
            templateUrl: "partials/freeMeal.html",
            controller: "freemealreportcontroller as rest"
        })
        .when("/discountReport", {
            templateUrl: "partials/discountReport.html",
            controller: "discountreport as rest"
        })
        .when("/tableWiseReport", {
            templateUrl: "partials/tableWiseReport.html",
            controller: "tableWiseReportcontroller as rest"
        })
        .when("/itemSalesReport", {
            templateUrl : "partials/report/itemSalesReport.html",
            controller : "itemSalesCtrl as rest"
        })

        .when("/company", {
            templateUrl: "partials/company.html",
            controller: "companyController as rest"
        })
        .when("/cancelledItemReport", {
            templateUrl: "partials/report/cancelledItemReport.html",
            controller: "cancelledItemReportCtrl as rest"
        })
        .when("/onlineInvoiceListing", {
            templateUrl: "partials/report/onlineInvoiceListing.html",
            controller: "onlineInvoicecontroller as rest"
        })
        .when("/waiterReport", {
            templateUrl: "partials/report/waiterReport.html",
            controller: "waiterreportcontroller as rest"
        })
        .when("/voucher", {
            templateUrl: "partials/paymentVoucher.html",
            controller: "VoucherController as rest"
        })
        .when("/shift", {
            templateUrl: "partials/shift.html",
            controller: "shiftCtrl as rest"
        })
        .when("/state", {
            templateUrl: "partials/state.html",
            controller: "stateController"
        })
        .when("/item", {
            templateUrl: "partials/iteminv.html",
            controller: "iteminvCtrl"
        })
        .when("/country", {
            templateUrl: "partials/country.html",
            controller: "countryCtrl"
        })

        .when("/currency", {
            templateUrl: "partials/currency.html",
            controller: "currencyController"
        })
        .when("/accountSetup", {
            templateUrl: "partials/accountSetup.html",
            controller: "accountSetupController"
        })
        .when("/tableReserve", {
            templateUrl: "partials/tableReservation.html",
            controller: "restaurantCtrl as rest"
        })
        .when("/digiOrders", {
            templateUrl: "partials/digiOrders.html",
            controller: "restaurantCtrl as rest"
        })
        .when("/OnlineDelivery", {
            templateUrl: "partials/onlineDeliveryRecords.html",
            controller: "restaurantCtrl as rest"
        })
        .when("/useraccountSetup", {
            templateUrl: "partials/useraccountsetupwithpermissions.html",
            controller: "useraccountsetupCtrl"
        })
        .when("/table", {
            templateUrl: "partials/table.html",
            controller: "tableCtrl"
        })
        .when("/customer", {
            templateUrl: "partials/customer.html",
            controller: "customerCtrl"
        })
        .when("/tableconfig", {
            templateUrl: "partials/tableconfiguration.html",
            controller: "tableconfigurationCtrl"
        })
        .when("/agent", {
            templateUrl: "partials/agent.html",
            controller: "agentCtrl"
        })
        .when("/categoryinv", {
            templateUrl: "partials/categoryinv.html",
            controller: "categoryinvController"
        })
        .when("/restaurantZone", {
            templateUrl: "partials/zone.html",
            controller: "zoneCtrl"
        })
        .when("/restaurant", {
            templateUrl: "partials/restaurant.html",
            controller: "restaurantCtrl"
        })

        .when("/paymentmethod", {
            templateUrl: "partials/paymentmethod.html",
            controller: "paymentmethodCtrl"
        })
        .when("/employee", {
            templateUrl: "partials/employee.html",
            controller: "employeeCtrl"
        })
        .when("/accountType", {
            templateUrl: "partials/accountType.html",
            controller: "accTypeCntrl"
        })
        .when("/accountGroup", {
            templateUrl: "partials/accountGroup.html",
            controller: "accGroupCntrl"
        })
        .when("/createchartofaccount", {
            templateUrl: "partials/createchartofaccount.html",
            controller: "createchartofaccountCtrl"
        })
        .when("/journalentry", {
            templateUrl: "partials/journalentry.html",
            controller: "journalentryCtrl as rest"
        })
        .when("/plreport", {
            templateUrl: "partials/plreport.html",
            controller: "plreportCtrl as rest"
        })
        .when("/balanceSheetReport", {
            templateUrl: "partials/balanceSheetReport.html",
            controller: "balanceSheetReportCtrl"
        })
        .when("/gtforpurchase", {
            templateUrl: "partials/gtforpurchase.html",
            controller: "gtforpurchaseCtrl as rest"
        })
        .when("/gtforsales", {
            templateUrl: "partials/gtforsales.html",
            controller: "gtforsalesCtrl as rest"
        })
        .when("/contact", {
            templateUrl: "partials/otherContacts.html",
            controller: "contactController"
        })
        .when("/configuration", {
            templateUrl: "partials/configuration.html",
            controller: "configurationCtrl as rest"
        })

        .when("/kitchenTokenRecord", {
            templateUrl: "partials/kitchenTokenRecord.html",
            controller: "kitchenTokenRecordCtrl as rest"
        })

        .when("/waiterTokenRecord", {
            templateUrl: "partials/waiterTokenRecord.html",
            controller: "waiterTokenRecordCtrl as rest"
        })
       .when("/restaurantInvoiceListing", {
            templateUrl: "partials/report/restaurantInvoiceListing.html",
            controller: "restaurantinvoicecontroller as rest"
        })
        .when("/dayendreport", {
            templateUrl: "partials/report/dayendreport.html",
            controller: "dayendreportCtrl as rest"
        })
        .when("/cancelledsalesinvoice", {
            templateUrl: "partials/report/cancelledsalesinvoice.html",
            controller: "cancellessalesinvoicectrl as rest"
        })

        .when("/agentReport", {
            templateUrl: "partials/report/agentReport.html",
            controller: "agentlistingCtrl as rest"
        })
        .when("/smsServer", {
            templateUrl: "partials/smsServer.html",
            controller: "smsServerCtrl as rest"
        })
        .when("/kitchen", {
            templateUrl: "partials/KitchenMaster.html",
            controller: "kitchenCtrl as rest"
        })

        .when("/login", {
            templateUrl: "partials/login.html",
            controller: "loginController as rest"
        })
        .otherwise({redirectTo:'/login'});
}]);

app.directive("limitTo", [function () {
    return {
        restrict: "A",
        link: function (scope, elem, attrs) {
            var limit = parseInt(attrs.limitTo);
            angular.element(elem).on("keypress", function (e) {
                if (this.value.length === limit)
                    e.preventDefault();
            });
        }
    };
}]);

app.directive('numWithOutSpace', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            if(!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function(val) {
                var clean = val.replace( /[^0-9:]/g,'');
                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });
        }
    };
});
/* for only  Alpha without space function
 */
app.directive('alphaWithoutSpace', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            if(!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function(val) {
                var clean = val.replace( /[^a-zA-Z]/g, '');
                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });

            element.bind('keypress', function(event) {
                if(event.keyCode === 32) {
                    event.preventDefault();
                }
            });
        }
    };
});
/* validation
 * for only number with space function
 */
app.directive('numWithSpace', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            if(!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function(val) {
                var clean = val.replace( /[^\s^0-9]+/g, '');
                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });
        }
    };
});

/* validation
 * It allows number,plus,hypen with space function
 */
app.directive('mobileNumWithSpace', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            if(!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function(val) {
                var clean = val.replace( /[^- ^+^0-9]+/g, '');
                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });
        }
    };
});
// app.directive('numWithOutSpace', function() {
//     return {
//         require: '?ngModel',
//         link: function(scope, element, attrs, ngModelCtrl) {
//             if(!ngModelCtrl) {
//                 return;
//             }
//
//             ngModelCtrl.$parsers.push(function(val) {
//                 var clean = val.replace( /[^0-9]+/g, '');
//                 if (val !== clean) {
//                     ngModelCtrl.$setViewValue(clean);
//                     ngModelCtrl.$render();
//                 }
//                 return clean;
//             });
//         }
//     };
// });
/* validation
 * It allows number,plus,hypen with space function
 */
app.directive('number', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            if(!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function(val) {
                var clean = val.replace( /[^0-9]+/g, '');
                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });
        }
    };
});



/* for only Alpha with space function
 */
app.directive('alphaWithSpace', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            if(!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function(val) {
                var clean = val.replace( /[^\s^a-zA-Z]/g, '');
                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });
        }
    };
});


/* for only Alpha and number with space function
 */
app.directive('alphanumWithSpace', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            if(!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function(val) {
                var clean = val.replace( /[^\s^a-zA-Z^0-9]/g, '');
                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });
        }
    };
});


/* for only Alpha and number without space function
 */
app.directive('alphanumWithoutSpace', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            if(!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function(val) {
                var clean = val.replace( /[^a-zA-Z^0-9]/g, '');
                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });

            element.bind('keypress', function(event) {
                if(event.keyCode === 32) {
                    event.preventDefault();
                }
            });
        }
    };
});

/* for only two digit decimal Function
 */

app.directive('twoDigitsDec', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            if(!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function(val) {
                if (angular.isUndefined(val)) {
                    var val = '';
                }

                var clean = val.replace(/[^-0-9\.]/g, '');
                var negativeCheck = clean.split('-');
                var decimalCheck = clean.split('.');
                if(!angular.isUndefined(negativeCheck[1])) {
                    negativeCheck[1] = negativeCheck[1].slice(0, negativeCheck[1].length);
                    clean =negativeCheck[0] + '-' + negativeCheck[1];
                    if(negativeCheck[0].length > 0) {
                        clean =negativeCheck[0];
                    }

                }

                if(!angular.isUndefined(decimalCheck[1])) {
                    decimalCheck[1] = decimalCheck[1].slice(0,2);
                    clean =decimalCheck[0] + '.' + decimalCheck[1];
                }

                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });

            element.bind('keypress', function(event) {
                if(event.keyCode === 32) {
                    event.preventDefault();
                }
            });
        }
    };
});

app.directive('noSpace', function() {
    return {
        require: '?ngModel',
        link: function(scope, element, attrs, ngModelCtrl) {
            element.bind('keypress', function(event) {
                if(event.keyCode === 32) {
                    event.preventDefault();
                }
            });
        }
    };
});

// Change text to uppercase and add dash every 5 char
app.directive('capitalizeWithDash', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, modelCtrl) {
            var capitalize = function(inputValue) {
                var number = 5;
                if (inputValue == undefined) inputValue = '';
                var inputUpper = inputValue.toUpperCase();
                var capitalizedArray = [];
                for(var i=0; i<inputUpper.length; i+= number){
                    if(inputUpper[i] == "-"){
                        capitalizedArray.push(inputUpper.substr(i+1,number))
                        i += 1;
                    }else {
                        capitalizedArray.push(inputUpper.substr(i, number))
                    }
                }
                var capitalized = capitalizedArray.join("-");
                if (capitalized !== inputValue) {
                    modelCtrl.$setViewValue(capitalized);
                    modelCtrl.$render();
                }
                return capitalized;
            }
            modelCtrl.$parsers.push(capitalize);
            capitalize(scope[attrs.ngModel]);
        }
    };
});

// $('.main_container').bind('keypress', function(e) {
//     var code = e.keyCode || e.which;
//     if(code == 13) { //Enter keycode
//         //Do something
//     }
// });




// angular.module("core").directive('hnEnterKey', function() {
//     return function(scope, element, attrs) {
//        ("keydown keypress", function(event) {
//
//             var code = e.keyCode || e.which;
//             if(code == 13) { //Enter keycode
//                 //Do something
//             }
//
//
//             var keyCode = event.which || event.keyCode;
//             if (keyCode === 13) {
//                 scope.$apply(function() {
//                     scope.$eval(attrs.hnEnterKey);
//                 });
//
//                 event.preventDefault();
//             }
//         });
//     };
// });


// app.config(['$httpProvider', function ($httpProvider) {
//     var $cookies;
//     angular.injector(['ngCookies']).invoke(['$cookies', function (_$cookies_) {
//         $cookies = _$cookies_;
//     }]);
//     $httpProvider.defaults.headers.common['Authorization'] = $cookies.get('accessToken');
// }]);


app.filter('setDecimal', function ($filter) {
    return function (input, places) {
        if (isNaN(input))
            return input;
        // If we want 1 decimal place, we want to mult/div by 10
        // If we want 2 decimal places, we want to mult/div by 100, etc
        // So use the following to create that factor
        var factor = "1" + Array(+(places > 0 && places + 1)).join("0");
        return Math.round(input * factor) / factor;
    };
});

app.filter('firstLetter', function () {
    return function (input, key, letter) {
        input = input || [];
        var out = [];
        input.forEach(function (item) {
            console.log('item: ', item[key][0].toLowerCase());
            console.log('letter: ', letter);
            if (item[key][0].toLowerCase() == letter.toLowerCase()) {
                out.push(item);
            }
        });
        return out;
    }
});

