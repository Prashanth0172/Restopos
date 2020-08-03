
/**
 * Created by sahera on 10/26/2017.
 */
angular.module('ui.bootstrap.demo', ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);
app.controller('agentlistingCtrl', function($scope, $http, $timeout, $rootScope,Notification) {
    $scope.hiposServerURL =  "/hipos/";
    $scope.customer=1;
    // $scope.isFilterApplied = false;
    $scope.today = function () {
        $scope.fromDate = new Date();
        $scope.toDate = new Date();
    };
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

    $scope.isFilterApplied = false;
    $scope.popup2 = {
        opened: false
    };
    $scope.getFinStartDate = function () {
        var url = "company/getCompany";
        $http.get(url)
            .then(function mySuccess(response) {
                $scope.fromDate = new Date(response.data.startyear);
                $scope.fromDate.setHours(10);
                $scope.toDate = new Date();
                $scope.dateOptions = {
                    minDate : response.data.startyear,
                    maxDate : response.data.endyear
                };
                console.log($scope.toDate);
                console.log($scope.fromDate);
            });
    }
    $scope.getFinStartDate();
    $scope.exportFullData = function () {
        $http.get("/reports/purchase/purchaseOrder", {
            params: {
                "fromDate": $scope.fromDate

                // "toDate": $scope.toDate
            }
        }).then(function (data) {
            $scope.exportList = data.data.data;
            $timeout(function () {
                $rootScope.exportAction($scope.exportType)
            }, 1000);
        })
    }
    // angular.isUndefinedOrNull = function(val) {
    //     return angular.isUndefined(val) || val === null
    // }
    // $scope.updateagentId = function (newCustVal) {
    //     $scope.agent = newCustVal.agentId;
    //     $scope.removeAllItems();
    // }


    // $scope.getAgentListSearch = function () {
    //     $http.get($scope.hiposServerURL + '/getAgentList').then(function (response) {
    //         var data = response.data;
    //         $scope.agentList = angular.copy(data);
    //     }, function (error) {
    //         Notification.error({
    //             message: 'Something went wrong, please try again',
    //             positionX: 'center',
    //             delay: 2000
    //         });
    //     })
    // };
    // $scope.getAgentListSearch();

    $scope.getAgentListSearch = function (val,type) {
        if (angular.isUndefined(val)) {
            val = "";
        }
        if (angular.isUndefined(type)) {
            type = "";
        }
        $http.post('reports/getAgentList?agentSearchText=' + val+"&type="+ type).then(function (response) {
                var data = response.data;
                $scope.agentList = angular.copy(data);
                $("#selectAgent").modal('show');
                $scope.agentSearchText = val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            }
        )
    };
    $scope.appendAgent = function (agentId) {
        $scope.agentSearchText = agentId.agentName;
        $scope.agentId = agentId.agentId;
        $scope.agent = $scope.agentId;
        $scope.showEmailBox = false;
        $("#selectAgent").modal('hide');

    }

    // $http.get("/reports/sales/agentListing/onLoadPageData").then(function(data) {
    //     $scope.agentList=data.data.agentList;
    //     $scope.salesList =data.data.salesInvoiceList;
    //     $scope.customerPage = 2;
    // })
    // $scope.onPageLoadData = function (searchText) {
    //     $http.get("/reports/agentReport", {
    //         params: {
    //             "fromDate": $scope.fromdate,
    //             "toDate": $scope.toDate,
    //             "searchText":searchText
    //         }
    //     }).then(function(data) {
    //         $scope.agentReportList = data.data.data;
    //         $scope.totalValues($scope.agentReportList);
    //         $scope.first = data.data.first;
    //         $scope.last = data.data.last;
    //         $scope.prev = data.data.prev;
    //         $scope.next = data.data.next;
    //         $scope.pageNo = data.data.pageNo;
    //     })
    //
    // }
    // $scope.onPageLoadData();
    $scope.isUndefinedOrNull = function(val) {
        return !angular.isDefined(val) || val === null;
    }
    $scope.getAgentListingwithFilter = function(page,SIfilter) {
        $scope.isFilterApplied = true
        $scope.getPaginatedAgentListing(page,SIfilter);

    }
    $scope.invoiceTotal = 0;
    $scope.comissionTotal =0;
    $scope.totalValues = function (val) {
        angular.forEach(val , function (value, key) {
            $scope.invoiceTotal =$scope.invoiceTotal + value.totalAmount;
            $scope.comissionTotal =$scope.comissionTotal +value.totalCommission;

        })
    }

    $scope.getPaginatedAgentListing = function(page,SIfilter) {
        $scope.invoiceTotal = 0;
        $scope.comissionTotal =0;
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
            // default:
            //     $scope.firstPage = true;



        }


        if (!$scope.isFilterApplied) {
            $http.get("/reports/agentReport", {
                params: {
                    "fromDate": $scope.fromDate,
                    "toDate":$scope.toDate,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
                    "pageNo": $scope.pageNo,
                    "prevPage": $scope.prevPage,
                    "prevPage": $scope.isPrev,
                    "nextPage": $scope.isNext
                }
            }).then(function(data) {
                $scope.agentReportList = data.data.data;
                $scope.totalValues($scope.agentReportList);
                $scope.first = data.data.first;
                $scope.last = data.data.last;
                $scope.prev = data.data.prev;
                $scope.next = data.data.next;
                $scope.pageNo = data.data.pageNo;
            })




        } else {

            agentId = 0;
            $scope.invoiceTotal = 0;
            $scope.comissionTotal =0;
            if (!$scope.isUndefinedOrNull($scope.agentId)) {

                agentId= $scope.agentId;
            }
            $http.get("/reports/agentReport", {
                params: {
                    "fromDate": $scope.fromDate,
                    "toDate":$scope.toDate,
                    "agentId":  agentId,
                    // "selectedFilter": $scope.selectedFilter,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
                    "pageNo": $scope.pageNo,
                    "prevPage": $scope.prevPage,
                    "prevPage": $scope.isPrev,
                    "nextPage": $scope.isNext,
                    "filterApplied": $scope.isFilterApplied
                }
            }).then(function(data) {
                $scope.agentReportList = data.data.data;
                $scope.totalValues($scope.agentReportList);
                $scope.first = data.data.first;
                $scope.last = data.data.last;
                $scope.prev = data.data.prev;
                $scope.next = data.data.next;
                $scope.pageNo = data.data.pageNo;
            })
        };

    }
     $scope.getPaginatedAgentListing();





});
