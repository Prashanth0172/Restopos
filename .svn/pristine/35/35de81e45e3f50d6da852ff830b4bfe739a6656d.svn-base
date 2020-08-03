/**
 * Created by sowmya on 10/30/2017.
 */
angular.module('ui.bootstrap.demo', ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);
app.controller('cancellessalesinvoicectrl', function ($scope, $http, $rootScope, Notification) {
    $scope.hiposServerURL = "/hipos/";
    $scope.customer = 1;
    $scope.today = function () {
        $scope.fromDate = new Date();
        $scope.toDate = new Date();
    };
    $scope.firstPage=true;
    $scope.lastPage=false;
    $scope.pageNo=1;
    $scope.prevPage=false;
    $scope.isPrev=false;
    $scope.isNext=false;
    $scope.today();
    $scope.format = 'dd/MM/yyyy';

    $scope.open1 = function () {
        $scope.popup1.opened = true;
    };

    $scope.popup1 = {
        opened: false
    };

    $scope.open2 = function () {
        $scope.popup2.opened = true;
    };

    $scope.isFilterApplied = false;
    $scope.popup2 = {
        opened: false
    };


    angular.isUndefinedOrNull = function (val) {
        return angular.isUndefined(val) || val === null
    }
    /*
     *adding for date on 12/09/2017 @rahul
     */
    $scope.getFinStartDate = function () {
        var url = "company/getCompany";
        $http.get(url)
            .then(function mySuccess(response) {
                $scope.fromDate = new Date(response.data.startyear);
                $scope.fromDate.setHours(10);
                $scope.toDate = new Date();
                $scope.dateOptions = {
                    minDate: response.data.startyear,
                    maxDate: response.data.endyear
                };
                $scope.getPageLoadDate();
            });
    }
    $scope.getFinStartDate();
    $http.get("/reports/invoice/onLoadPageData").then(function (data) {
        $scope.currencyList = data.data.currencyList;
        $scope.customerList = data.data.customerList;
        $scope.salesCancelList = data.data.salesCancelList;
        $scope.fromsalesinvoice= $scope.salesCancelList[0].sqid;
        $scope.toSalesinvoice= $scope.salesCancelList[$scope.salesCancelList.length - 1].sqid;
        $scope.locationList = data.data.locationList;
        $scope.itemList = data.data.itemList;
        $scope.customerPage = 2;
    })
    $scope.updateCustomerId = function (newCustVal) {
        $scope.customer = newCustVal.customerId;
        $scope.removeAllItems();
    }
    $scope.getCustomerListSearch = function (val) {
        $(".loader").css("display", "block");
        if (angular.isUndefined(val)) {
            val = "";
        }
        $http.post($scope.hiposServerURL + "/" + '/getCustomerListSearch?searchCustomerText=' + val).then(function (response) {
                var data = response.data;
                $scope.customerList = angular.copy(data);
                $("#selectCustomer").modal('show');
                $scope.searchCustomerText = val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            }
        )
    };
    $scope.appendCustomer = function (customerId) {
        $scope.customerSearchText = customerId.customerName;
        $scope.customerId = customerId.customerId;
        $scope.customer = $scope.customerId;
        $scope.showEmailBox = false;
        $("#selectCustomer").modal('hide');

    }

    $scope.getPageLoadDate=function () {

        var cancelReportDetails;
        cancelReportDetails = {
            "firstPage": $scope.firstPage,
            "lastPage": $scope.lastPage,
            "pageNo": $scope.pageNo,
            "prevPage": $scope.prevPage,
            "prevPage": $scope.isPrev,
            "nextPage": $scope.isNext,
            "fromDate": $scope.fromDate,
            "toDate": $scope.toDate

        };
        $http.post("/reports/cancelSalesInvoice", angular.toJson(cancelReportDetails, "Create"))
            .then(function (data) {
                $scope.exportList = data.data.data;
                $scope.totalValues($scope.exportList);
                $scope.first = data.data.first;
                $scope.last = data.data.last;
                $scope.prev = data.data.prev;
                $scope.next = data.data.next;
                $scope.pageNo = data.data.pageNo;
            })
    }
    $scope.invoiceTotal = 0;
    $scope.receivedTotal =0;
    $scope.totalValues = function (val) {
        angular.forEach(val , function (value, key) {
            $scope.invoiceTotal =$scope.invoiceTotal + value.totalAmount;
            $scope.receivedTotal =$scope.receivedTotal +value.totalReceivable;

        })
    }

    $scope.isUndefinedOrNull = function (val) {
        return !angular.isDefined(val) || val === null;
    }
    $scope.getSalesInvoicewithFilter = function (page, SIfilter) {
        $scope.isFilterApplied = true;
        $scope.getPaginatedSalesInvoice(page, SIfilter);

    }
    $scope.getPaginatedSalesInvoice = function (page, SIfilter) {
        $scope.invoiceTotal = 0;
        $scope.receivedTotal = 0;

        switch (page) {
            case 'firstPage':
                $scope.firstPage = true;
                $scope.lastPage = false;
                $scope.pageNo = 0;
                break;
            case 'lastPage':
                $scope.lastPage = true;
                $scope.firstPage = false;
                $scope.pageNo = 0;
                break;
            case 'nextPage':
                $scope.isNext = true;
                $scope.isPrev = false;
                $scope.lastPage = false;
                $scope.firstPage = false;
                $scope.pageNo = $scope.pageNo + 1;
                break;
            case 'prevPage':
                $scope.isPrev = true;
                $scope.lastPage = false;
                $scope.firstPage = false;
                $scope.isNext = false;
                $scope.pageNo = $scope.pageNo - 1;
                break;
            default:
                $scope.firstPage = true;
        }
        if (!$scope.isFilterApplied) {
            if ($scope.fromDate > $scope.toDate) {
                Notification.info({
                    message: 'FromDate should be less than ToDate ',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            $http.get("/reports/cancelSalesInvoice", {
                params: {
                    "fromDate": $scope.fromDate,
                    "toDate": $scope.toDate,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
                    "pageNo": $scope.pageNo,
                    "prevPage": $scope.prevPage,
                    "prevPage": $scope.isPrev,
                    "nextPage": $scope.isNext
                }
            }).then(function (data) {
                $scope.exportList = data.data.data;
                $scope.totalValues($scope.exportList);
                $scope.first = data.data.first;
                $scope.last = data.data.last;
                $scope.prev = data.data.prev;
                $scope.next = data.data.next;
                $scope.pageNo = data.data.pageNo;
            })
        } else {
            if ($scope.fromDate > $scope.toDate) {
                Notification.info({
                    message: 'FromDate should be less than ToDate ',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            toSalesinvoice = 0;
            fromsalesinvoice = 0;
            if (!$scope.isUndefinedOrNull($scope.fromsalesinvoice) && !$scope.isUndefinedOrNull($scope.toSalesinvoice)) {
                toSalesinvoice = $scope.toSalesinvoice.sqno;
                fromsalesinvoice = $scope.fromsalesinvoice.sqno;
            }
            $http.get("/reports/cancelSalesInvoice", {
                params: {
                    "fromDate": $scope.fromDate,
                    "toDate": $scope.toDate,
                    "fromSINo":fromsalesinvoice,
                    "toSINo": toSalesinvoice,
                    "selectedFilter": $scope.selectedFilter,
                    "customerId": $scope.customerId,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
                    "prevPage": $scope.prevPage,
                    "prevPage": $scope.isPrev,
                    "nextPage": $scope.isNext,
                    "pageNo": $scope.pageNo,
                    "filterApplied": $scope.isFilterApplied

                }
            }).then(function (data) {
                $scope.exportList = data.data.data;
                $scope.totalValues($scope.exportList);
                $scope.first = data.data.first;
                $scope.last = data.data.last;
                $scope.prev = data.data.prev;
                $scope.next = data.data.next;
                $scope.pageNo = data.data.pageNo;
            })
        }
        ;
    }
 $scope.getPaginatedSalesInvoice();
});




