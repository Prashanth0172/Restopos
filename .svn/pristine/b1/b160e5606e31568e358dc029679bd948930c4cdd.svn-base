/**
 * Created by sneharika on 10/5/2017.
 */
angular.module('ui.bootstrap.demo', ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);
app.controller('dayendreportCtrl', function($scope, $http,$timeout,$rootScope,Notification) {
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
    $scope.today();
    $scope.format = 'dd/MM/yyyy';

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

    $scope.getFinStartDate = function () {
        var url = "company/getCompany";
        $http.get(url)
            .then(function mySuccess(response) {
                $scope.dt = new Date(response.data.startyear);
                $scope.dt.setHours(10);
                $scope.dt1 = new Date();
                $scope.dateOptions = {
                    minDate : response.data.startyear,
                    maxDate : response.data.endyear
                };
                $scope.getDayEndReportList();
            });
    }
    $scope.getFinStartDate();

    $scope.totalAmt=0;
    $scope.totalValues = function (val) {
        angular.forEach(val , function (value, key) {
            $scope.totalAmt =$scope.totalAmt + value.amount;
        })
    }

    $scope.getDayEndList = function (date) {
        var getRequest = {
            url:'/reports/getDayEndReport',
            method:'GET',
            params:{'fromDate':new Date(date).getTime()}
        };
        $http(getRequest).then(function (successResponse){
            console.log(successResponse.data);
            $scope.dailyReportList = successResponse.data;
            if($scope.dailyReportList['Total Amount']>0){
                console.log(successResponse.data);
                $scope.total = 0;
                $('#print_dailyReport').modal('show');
            }else {
                Notification.error({message: 'Their is no Day End For this Date', positionX: 'center', delay: 2000});
            }
        }, function (failureResponse) {
        });
    };
    $scope.getDayEndReportList = function (page) {
        $scope.totalAmt=0;
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
        $http.get("/reports/getDayEndReportList", {
            params: {
                "fromDates": $scope.dt,
                "toDate": $scope.dt1,
                "firstPage": $scope.firstPage,
                "lastPage": $scope.lastPage,
                "pageNo": $scope.pageNo,
                "prevPage": $scope.prevPage,
                "prevPage": $scope.isPrev,
                "nextPage": $scope.isNext
            }
        }).then(function (data) {
            $scope.dailyEndReportList = data.data.data;
            angular.forEach($scope.dailyEndReportList,function (val,key) {
                val.date=new Date(val.date).getTime();
            })
            $scope.totalValues($scope.dailyEndReportList);
            $scope.first = data.data.first;
            $scope.last = data.data.last;
            $scope.prev = data.data.prev;
            $scope.next = data.data.next;
            $scope.pageNo = data.data.pageNo;
        });

    }
    $scope.updateTotals = function(categoryTotal){
        if(categoryTotal !== undefined) {
            $scope.total += parseFloat(categoryTotal);
        }
    };

    $scope.printDiv4 = function (divName) {
        var printContents = document.getElementById(divName).innerHTML;
        $("#print_dailyReport").modal('hide');
        //Logic for Browser Print
        var popupWin = window.open('', '_blank', 'width=300,height=300');
        popupWin.document.open();
        popupWin.document.write('<html><head></head><body onload="window.print()">' + printContents + '</body></html>');
        popupWin.document.close();
    };


    $scope.getCompanyInfo = function(){
        var getRequest = {
            url:'/hipos/getCompanyList',
            method:'POST',
            params:{}
        };
        $http(getRequest).then(function (successResponse){
            $scope.printData4 = successResponse.data;
            $scope.printData4.date = new Date();
            console.log($scope.printData4);
        },function (failureResponse){});
    };

    $scope.getCompanyInfo();
    $scope.restaurantServerURL = "/restaurant";
    $http.get($scope.restaurantServerURL + '/getPageOnloadData').then(function (response) {
        var data = response.data;
        if (data.hiPosServiceCharge !== null) {
            $scope.hiposServiceCharge = data.hiPosServiceCharge.servicePercentage;
            $scope.serviceChargeName = data.hiPosServiceCharge.serviceChargeName;
        }
    })


});
