app.controller('trialbalancereportcontroller', function($scope, $http,$rootScope) {
    $scope.bshimServerURL = "/bs";
    $scope.trailBalance=function () {
        $http.get("/bs/trialBalance").then(function(data) {
            $scope.trialbalancepojo= data.data;
        });
    }

    $scope.format = 'dd/MM/yyyy';
    $scope.open1 = function() {
        $scope.popup1.opened = true;
    };
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
                $scope.dt1 = new Date(response.data.endyear);
                $scope.fiscalyearId=response.data;
                $scope.dateOptions = {
                    minDate : response.data.startyear,
                    maxDate : response.data.endyear
                };
                $scope.getprofitList($scope.dt,$scope.dt1);
            });
    }
    $scope.getFinStartDate();

    $scope.trailBalance();
    $scope.getInventoryLocationList = function () {
        $http.get('/hipos' + "/" + 1 + '/addUserAccountSetup').then(function (response) {
            var data = response.data;
            $scope.inventoryLocationList = data;
            $scope.fromLocation=$scope.fromLocation = parseInt($scope.inventoryLocationList[0].userLocationId);
        }, function (error) {
            Notification.error({
                message: 'Something went wrong, please try again',
                positionX: 'center',
                delay: 2000
            });
        })
    };
    $scope.getInventoryLocationList();
    $scope.printDiv = function (printablediv) {
        var printContents = document.getElementById(printablediv).innerHTML;
        var popupWin = window.open('', '_blank', 'width=300,height=300');
        popupWin.document.open();
        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" media="print" href="../poscss/recept_print.css"></head><body onload="window.print()">' + printContents + '</body></html>');
        popupWin.document.close();
    };
    $scope.getAccountName=function (val) {
        $rootScope.accountName=val;
        $rootScope.sheetName='TB';
        $window.location.href="/home#!/statementofcomprehensiveincomedetails?search=" + val;
    }
    $scope.updateTrailBalance = function () {
        if($scope.inventoryLocation==undefined){
            $scope.inventoryLocation=0;
        }
        $scope.trialbalancepojo.fromDate=$scope.dt;
        $scope.trialbalancepojo.toDate=$scope.dt1;
        var getRequest = {
            url: '/reports/finance/updatedTrailBalance?locationId='+$scope.inventoryLocation,
            method: 'POST',
            data: $scope.trialbalancepojo
        }
        $http(getRequest).then(function (successReponse) {
            console.log(successReponse);
            $scope.trialbalancepojo = successReponse.data;
        }, function (failureResponse) {
        })
    }
    //According to Shahid Sir
    // $timeout(function () {
    //     console.log("Trail Balance Data");
    //     console.log($scope.trialbalancepojo);
    //     $scope.updateTrailBalance();
    // }, 1000);
});