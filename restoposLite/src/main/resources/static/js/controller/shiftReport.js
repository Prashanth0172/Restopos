/**
 * Created by sneharika on 10/5/2017.
 */
angular.module('ui.bootstrap.demo', ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);
app.controller('shiftController', function($scope, $http,$timeout,$rootScope,Notification) {
    $scope.hiposServerURL = "/hipos/";
    $scope.customer = 1;
    $scope.firstPage=true;
    $scope.lastPage=false;
    $scope.pageNo=1;
    $scope.prevPage=false;
    $scope.isPrev=false;
    $scope.isNext=false;
    $scope.today = function() {
        $scope.dt = new Date();
        $scope.dt1 = new Date();
        $scope.fromDate = new Date();
        $scope.toDate = new Date();
    };


    $scope.today = function() {
        $scope.fromDate = new Date();
        $scope.toDate = new Date();
    };
    $scope.today();
    $scope.format = 'dd/MM/yyyy';

    $scope.open1 = function() {
        $scope.popup1.opened = true;
    };

    $scope.popup1 = {
        opened: false
    };
    $scope.printDiv4 = function (divName) {
        var printContents = document.getElementById(divName).innerHTML;
        $("#print_shiftReport").modal('hide');
        //Logic for Browser Print
        var popupWin = window.open('', '_blank', 'width=300,height=300');
        popupWin.document.open();
        popupWin.document.write('<html><head></head><body onload="window.print()">' + printContents + '</body></html>');
        popupWin.document.close();
    };
    $scope.getCompanyInfo = function () {
        var getRequest = {
            url: '/company/getCompany',
            method: 'GET',
            params: {}
        };
        $http(getRequest).then(function (successResponse) {
            $scope.printData4 = successResponse.data;
            $scope.printData4.date = new Date();
            $scope.dt1=new Date();
            console.log($scope.printData4);
        }, function (failureResponse) {
        });
    };

    $scope.getCompanyInfo();
    $scope.getShiftEndReport = function (date,id) {
        var getRequest = {
            url:'/reports/getShiftEndReport',
            method:'GET',
            params:{'fromDate':date,'shiftId':id}
        };
        $http(getRequest).then(function (successResponse){
            console.log(successResponse.data);
            $scope.shiftReportList = successResponse.data;
            if($scope.shiftReportList['Total Amount']>0){
                console.log(successResponse.data);
                $scope.total = 0;
                $('#print_shiftReport').modal('show');
            }else {
                Notification.error({message: 'Their is no Day End For this Date', positionX: 'center', delay: 2000});
            }
        }, function (failureResponse) {
        });
    };
    $scope.open2 = function() {
        $scope.popup2.opened = true;
    };

    $scope.open1 = function() {
        $scope.popup1.opened = true;
    };
    $scope.getPageLoadDataRest = function () {
        $http.get($scope.restaurantServerURL + '/getPageOnloadData').then(function (response) {
            var data = response.data;
            console.log(data);
            if (!angular.isUndefined(data) && data !== null) {
                if (data.hiPosServiceCharge !== null) {
                    $scope.hiposServiceCharge = data.hiPosServiceCharge.servicePercentage;
                    $scope.serviceChargeName = data.hiPosServiceCharge.serviceChargeName;
                }
            }
        }), function (error) {
            Notification.error({
                message: 'Something went wrong, please try again',
                positionX: 'center',
                delay: 2000
            });
        }
    };
    $scope.getPageLoadDataRest();
    $scope.popup1 = {
        opened: false
    };

    $scope.open2 = function() {
        $scope.popup2.opened = true;
    };

    $scope.isFilterApplied = false;
    $scope.popup2 = {
        opened: false
    };
    $scope.getShiftList = function () {
        $http.get("/reports/getShiftListReport?fromdate="+$scope.fromDate + '&todate='+ $scope.toDate).then(function (response) {
            var data = response.data;
            $scope.reportList = data;
        }, function (failureResponse) {
        });
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
                $scope.getShiftList();
            });
    }
    $scope.getFinStartDate();

    $scope.totalAmt=0;
    $scope.totalValues = function (val) {
        angular.forEach(val , function (value, key) {
            $scope.totalAmt =$scope.totalAmt + value.amount;
        })
    }

    $scope.updateTotals = function(categoryTotal){
        if(categoryTotal !== undefined) {
            $scope.total += parseFloat(categoryTotal);
        }
    };

});
