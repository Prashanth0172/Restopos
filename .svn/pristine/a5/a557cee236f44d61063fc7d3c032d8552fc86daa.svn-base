app.controller('monthendcontroller', function ($scope, $http, $rootScope,Notification) {
    $scope.hiposServerURL = "/hipos/";
    $scope.customer = 1;
    $scope.retailServerURL = "/retail/";
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

    $scope.dateTypeFilter =null;
    $scope.setDates1 = function(){
        $scope.dateMnth = $scope.monthName;
        angular.forEach($scope.monthNames,function (val,key) {
            if($scope.dateMnth==val){
                $scope.month=key;
            }
        })
        var firstDay = new Date(new Date().getFullYear(), $scope.month, 1);
        var lastDay = new Date(new Date().getFullYear(), parseInt($scope.month) + 1, 0);
            $scope.fromDate= firstDay;
            $scope.toDate = lastDay;
        }

    // $scope.setDates1();


    angular.isUndefinedOrNull = function(val) {
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
                $scope.startyear =new Date(response.data.startyear);
                $scope.endyear =new Date(response.data.endyear);
                $scope.setDates();
                $scope.dateOptions = {
                    minDate : response.data.startyear,
                    maxDate : response.data.endyear
                };
                console.log($scope.toDate);
                console.log($scope.fromDate);
            });
    }

    $scope.monthNames=[];
    $scope.monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];
    $scope.setDates = function () {
        $scope.monthsList=[];
        var monthNo=0;
        for (var i = new Date().getMonth(); i <= 12 + new Date().getMonth()+5; i++) {
            if (i >=12) {
                monthNo=i-12;
                $scope.monthsList.push($scope.monthNames[monthNo]+'-'+$scope.endyear.getFullYear());
            } else {
                monthNo =i;
                $scope.monthsList.push($scope.monthNames[monthNo]+'-'+$scope.startyear.getFullYear());
            }
        }
    };

    // $scope.getFinStartDate();
    $http.get("/reports/sales/invoice/onLoadPageData").then(function(data) {
        $scope.currencyList = data.data.currencyList;
        $scope.customerList = data.data.customerList;
        $scope.salesList = data.data.salesList;
        $scope.fromsalesinvoice= $scope.salesList[$scope.salesList.length - 1];
        $scope.toSalesinvoice= $scope.salesList[0];
        $scope.locationList = data.data.locationList;
        $scope.itemList = data.data.itemList;
        $scope.currencyId = parseInt(data.data.cmpyCurrency);
        $scope.customerPage = 2;
    })

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
    $scope.exportFullData = function () {
        $http.get("/reports/sales/salesInvoiceExport").then(function (response) {
            var data = response.data;
            $scope.exportList = angular.copy(data);
            console.log(data);
            $rootScope.exportAction($scope.exportType);
        });
    };

    $scope.isUndefinedOrNull = function(val) {
        return !angular.isDefined(val) || val === null;
    }
    $scope.getSalesInvoicewithFilter = function(page) {
        $scope.isFilterApplied = true;
        $scope.getPaginatedSalesInvoice(page);
    }


    $scope.getPaginatedSalesInvoice = function (page) {
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
            $http.get("/reports/salesMonthEndInvoice", {
                params: {
                    "fromDate": $scope.fromDate,
                    "toDate": $scope.toDate,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
                    "pageNo": $scope.pageNo,
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
            custId = 0;
            toSalesinvoice = 0;
            fromsalesinvoice = 0;
            $scope.invoiceTotal = 0;
            if (!$scope.isUndefinedOrNull($scope.fromsalesinvoice) && !$scope.isUndefinedOrNull($scope.toSalesinvoice)) {
                toSalesinvoice = $scope.toSalesinvoice.sqid;
                fromsalesinvoice = $scope.fromsalesinvoice.sqid;
            }
            $http.get("/reports/salesMonthEndInvoice", {
                params: {
                    "fromDate": $scope.fromDate,
                    "toDate": $scope.toDate,
                    "fromSID": fromsalesinvoice,
                    "toSID": toSalesinvoice,
                    "selectedFilter": $scope.selectedFilter,
                    "customerId":  $scope.customerId,
                    "employeeId":  $scope.employeeId ,
                    "selectedList":  $scope.selectedList ,
                    "itemCategoryId":$scope.itemCategoryId,
                    "itemId":$scope.itemId,
                    "status":$scope.statusText,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
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
        };
    }
    $scope.getPaginatedSalesInvoice('firstPage');
    $scope.selectedList=[];
    $scope.selectedEmployeeList=function(employeeName) {
        $scope.selectedList.push(employeeName);
    }

    $scope.getEmployeeList = function (val) {
        $scope.selectedList=[];
        $(".loader").css("display", "block");
        if (angular.isUndefined(val)) {
        }
        $http.get($scope.hiposServerURL + "/" + $scope.customer + '/getEmployeeList?employeeSearchText=' + val).then(function (response) {
            var data = response.data;
            console.log(data);
            $scope.employeeList = angular.copy(data);
            $("#selectEmployee").modal('show');
            $scope.employeeSearchText = val;
            $scope.searchText = val;
        }, function (error) {
            Notification.error({
                message: 'Something went wrong, please try again',
                positionX: 'center',
                delay: 2000
            });
        })
    };
    $scope.appendEmployee = function (employeeId) {
        $scope.employeeSearchText = employeeId.employeeName;
        $scope.employeeId = employeeId.employeeId;
        $scope.employee = $scope.employeeId;
        $scope.showEmailBox = false;
        $("#selectEmployee").modal('hide');

    }
    $scope.updateEmployeeId = function (newItemVal) {
        $scope.employee = newItemVal.employeeId;
    }
});




