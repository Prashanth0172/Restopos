app.controller('restdashCtrl',
    function ($scope, $rootScope, $http, $interval, Notification) {
        $scope.colors = ['#46BFBD', '#803690', '#009933', '#FDB45C', '#00ADF9', '#0066cc', '#990000'];
        $scope.showLine = false;
        $scope.purchaseRemaining = 0;
        $scope.salesRemaining = 0;
        $scope.hiposServerURL1 = "/hipos/";
        $scope.customer1 = 1;

        $("#restdashboard").addClass('active');
        $('#restdashboard').siblings().removeClass('active');
        $('#sidebar-menu ul li ul li').removeClass('active');
        $('#sidebar-menu ul li ul').css('display', 'none');

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
        $scope.popup2 = {
            opened: false
        };

        // $scope.dateOptions = {
        //     minDate : new Date()
        // };
        $scope.dt = new Date();
        $scope.dt1 = new Date();
        $scope.disable=false;

        $scope.restdashboard = function () {
            $scope.dt.setHours(10);
            $scope.dt1.setHours(10);
            $scope.disable=true;
            $scope.doughnutTotalAmountLabel = [];
            $scope.doughnutTotalAmountData = [];
            $scope.doughnutTotalAmountLabel1 = [];
            $scope.doughnutTotalAmountData1 = [];
            $scope.doughnutTotalAmountLabel2 = [];
            $scope.doughnutTotalAmountData2 = [];
            if($scope.inventoryLocation==undefined){
                $scope.inventoryLocation="";
            }
            $http.post("/hipos/restdashboard?fromdate=" + $scope.dt + '&todate=' + $scope.dt1+'&locationId='+$scope.inventoryLocation).then(function (response) {
                var data = response.data;
                $scope.totalSales=angular.fromJson(data.totalSales);
                $scope.subtotal = $scope.totalSales.amount + $scope.totalSales.discount;
                $scope.result = $scope.totalSales;
                $scope.total = $scope.totalSales.amount + $scope.totalSales.taxamount;
                $scope.itemList = angular.fromJson(data.top5Items);
                $scope.paymentList = angular.fromJson(data.paymentType);
                angular.forEach($scope.paymentList, function (val, key) {
                    if (val > 0) {
                        $scope.doughnutTotalAmountData.push(Math.abs(val).toFixed(2));
                        $scope.doughnutTotalAmountLabel.push(key + " ( " + val + " )");
                    }
                });
                $scope.orderType = angular.fromJson(data.orderType);
                angular.forEach($scope.orderType, function (val, key) {
                    if (val.totalAmt > 0) {
                        $scope.doughnutTotalAmountData1.push(Math.abs(val.totalAmt).toFixed(2));
                        $scope.doughnutTotalAmountLabel1.push(val.invoiceType + " ( " + val.totalAmt + " ) ");
                    }
                });
                $scope.runningSalesList = angular.fromJson(data.runningSales);
                $scope.runningSalesTotal = $scope.runningSalesList.amount + $scope.runningSalesList.taxamount - $scope.runningSalesList.discount;
                $scope.shiftList = angular.fromJson(data.shiftList);
                angular.forEach($scope.shiftList, function (val, key) {
                    if (val > 0) {
                        $scope.doughnutTotalAmountData2.push(Math.abs(val).toFixed(2));
                        $scope.doughnutTotalAmountLabel2.push(key + "(" + val + " )");
                    }
                });
                $scope.doughnutTotalAmountOption = {
                    legend: {
                        display: true
                    }
                }
                $scope.doughnutTotalAmountOption = {
                    legend: {
                        display: true
                    }
                }
                $scope.doughnutTotalAmountOption = {
                    legend: {
                        display: true
                    }
                }
                $scope.disable=false;
            })
        };
        $scope.restdashboard();

    });