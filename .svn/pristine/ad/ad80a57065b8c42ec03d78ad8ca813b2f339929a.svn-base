angular.module('ui.bootstrap.demo', ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);
app.controller('itemSalesCtrl', function ($scope, $http, $rootScope,Notification) {
    $scope.hiposServerURL = "/hipos/";
    $scope.isFilterApplied = false;
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

    $scope.open2 = function() {
        $scope.popup2.opened = true;
    };

    $scope.isFilterApplied = false;
    $scope.popup2 = {
        opened: false
    };


    angular.isUndefinedOrNull = function(val) {
        return angular.isUndefined(val) || val === null
    }

    $scope.invoiceTotal = 0;
    $scope.totalValues = function (val) {
        angular.forEach(val , function (value, key) {
            $scope.invoiceTotal =$scope.invoiceTotal + value.invoiceAmount;
        })
    }
    $scope.getItemCategoryList = function (val) {
        if (angular.isUndefined(val)) {
            val = "";
        }
        $http.post($scope.hiposServerURL+"/getItemCategoryList").then(function (response) {
            var data = response.data;
            $scope.itemCategoryList = data;
            $("#selectItemCategory").modal('show');

        })
    };
    $scope.appendItemCategory = function (itemCategoryId) {
        $scope.itemCategorySearchText = itemCategoryId.itemCategoryName;
        $scope.itemCategoryId = itemCategoryId.itemCategoryId;
        $scope.itemCategory = $scope.itemCategoryId;
        $scope.showEmailBox = false;
        $scope.itemText=null;
        $("#selectItemCategory").modal('hide');

    }
    $scope.getItemLists = function (val) {
        $(".loader").css("display", "block");
        if (angular.isUndefined(val)) {
            val = "";
        }
        if (angular.isUndefined($scope.itemCategoryId)) {
            $scope.itemCategoryId = 0;
        }
        $http.get('/hipos/' + '/getItemListOnCategory?itemCategoryId=' + $scope.itemCategoryId + '&searchText=' + ''+'&locationId='+$rootScope.selectedLocation).then(function (response) {
            var data = response.data;
            $scope.itemList = angular.copy(data);
            $("#selectItems").modal('show');
        }), function (error) {
            Notification.error({
                message: 'Somthing went wrong, please try again',
                positionX: 'center',
                delay: 2000
            });
        };
    };
    $scope.appendItem = function (itemId) {
        $scope.itemText = itemId.itemName;
        $scope.itemId = itemId.itemId;
        $scope.item = $scope.itemId;
        $scope.showEmailBox = false;
        $("#selectItems").modal('hide');

    }
    $scope.isUndefinedOrNull = function(val) {
        return !angular.isDefined(val) || val === null;
    }

    $scope.getSalesInvoicewithFilter = function(page,SIfilter) {
        $scope.isFilterApplied = true;
        $scope.getPaginatedSalesInvoice(page,SIfilter);

    }


    $scope.getPaginatedSalesInvoice = function (page,SIfilter) {
        $scope.invoiceTotal = 0;
        $scope.fromDate.setHours(10);
        $scope.toDate.setHours(10);

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
            if($scope.fromDate > $scope.toDate){
                Notification.info({
                    message: 'FromDate should be less than ToDate ',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            $http.get("/reports/restItemWise", {
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
            if($scope.fromDate > $scope.toDate){
                Notification.info({
                    message: 'FromDate should be less than ToDate ',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            $scope.invoiceTotal = 0;
            $http.get("/reports/restItemWise", {
                params: {
                    "fromDate": $scope.fromDate,
                    "toDate": $scope.toDate,
                    "itemCategoryId":$scope.itemCategoryId,
                    "itemId":$scope.itemId,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
                    "prevPage": $scope.prevPage,
                    "prevPage": $scope.isPrev,
                    "nextPage": $scope.isNext,
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




