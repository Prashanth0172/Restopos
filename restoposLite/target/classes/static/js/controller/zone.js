/**
 * Created by hyvad067 on 01-08-2018.
 */
app.controller('zoneCtrl',
    function ($scope, $rootScope,$window, $http,$interval, $location, $filter, Notification) {
        $scope.hiposServerURL = "/hipos";
        $scope.customer = 1;
        $scope.restaurantServerURL = "/restaurant";
        $scope.restrictLoc = "true";
        $scope.getAllTables = function (tableConfiguratorName,tableName,flag,locationId) {
            if(flag=='config'){
                $scope.tableSearchText=null;
            }
            if (angular.isUndefined(tableConfiguratorName)||tableConfiguratorName==null) {
                tableConfiguratorName = "";
            }
            if (angular.isUndefined(tableName)||tableName==null) {
                tableName = "";
            }
            if($scope.configurationname==null){
                $rootScope.getTableList();
            }else {
                $http.get("/hipos/getTableListOnConfig?configName=" + tableConfiguratorName + "&tableSearchText=" + tableName + "&locationId=" + locationId).then(function (successResponse) {
                    $scope.tableList = successResponse.data;
                    if (tableName == "") {
                        $scope.completeTableList = successResponse.data;
                    }
                })
            }

        };
        $rootScope.getTableList = function() {
            var getRequest = {
                url: '/hipos/completeTableList',
                method: 'GET',
                params: {type:'Active'}

            };
            $http(getRequest).then(function(successResponse){
                console.log(successResponse.data);
                var data = successResponse.data;
                $scope.completeTableList = data;
                $scope.tableList = data;
            });
        };
        $rootScope.getTableList();
        $interval(function () {
            if($scope.configurationname!=null){
                $scope.getAllTables($scope.configurationname,$scope.tableSearchText,'',$scope.fromLocation);
            }else {
                $rootScope.getTableList();
            }
        }, 30000);
        $scope.guestArrived=function (table) {
            $rootScope.selectedTableId=table.tableid;
            $rootScope.selectedTableName=table.tableName;
            $rootScope.guestName=table.guestName;
            $rootScope.selectedLocation =$scope.fromLocation;
            $rootScope.orderTypeName='DineIn';
            $window.location.href = '#!/restaurant';
        }
        $scope.customerBill=function (table) {
            $scope.currTableName=table.tableName;
            $scope.currTableId=table.tableid;
            $scope.disAmtInPer=0;
            $scope.hiposServiceCharge=0;
            $scope.hiposServiceChargeAmt=0;
            $http.get($scope.restaurantServerURL + '/getTempData?currTableName=' + $scope.currTableName + "&currTableId=" + $scope.currTableId).then(function (response) {
                var data = response.data;
                $scope.selectedItemsList = [];
                if (!angular.isUndefined(data.selectedItemsList) && data.selectedItemsList !== null) {
                    $scope.previousOrdersConfirmed = angular.fromJson(data.selectedItemsList);
                    $scope.selectedItemsList = angular.fromJson(data.selectedItemsList);
                }
                $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                if (angular.isUndefined($scope.selectedItemsList) || $scope.selectedItemsList.length <= 0) {
                    Notification.error({
                        message: 'At least One item has to be selected',
                        positionX: 'center',
                        delay: 2000
                    });
                    return false;
                } else if (!$scope.isValidatedData()) {
                    Notification.error({
                        message: 'Qty should not be Zero',
                        positionX: 'center',
                        delay: 2000
                    });
                    return false;
                } else {
                    if($scope.discType==undefined){
                        $scope.discType="";
                    }
                    $scope.roundingOffValue = $scope.totalCheckOutamt - Math.round($scope.totalCheckOutamt);
                    var jsonString = {
                        tableName: $scope.currTableName,
                        tableNo: $scope.currTableId,
                        roundingOffValue: $scope.roundingOffValue,
                        selectedItemList: $scope.selectedItemsList,
                        discount: $scope.disAmtInPer,
                        discountType: $scope.discType,
                        discountAmt: $scope.totalVPDiscount,
                        serviceCharge: $scope.hiposServiceCharge,
                        totalTax: $scope.totalTaxAmt,
                        subtotal: $scope.totalAmtExamt,
                        grandtotal: $scope.totalCheckOutamt,
                        hiposServiceChargeAmt: $scope.hiposServiceChargeAmt,
                        type: 'Desktop'
                    };
                    var postRequest = {
                        url: '/restaurant/customerBill',
                        method: 'POST',
                        data: jsonString
                    };
                    $http(postRequest).then(function (successResponse) {
                        console.log(successResponse);
                        $rootScope.getTableList();
                        if ($scope.configurationData.buildTypeCloud) {
                            var postRequest = {
                                url: 'http://localhost:9001/hiAccounts/sendDatatoLocalPrinter',
                                data: successResponse.data,
                                method: 'POST',
                                params: {}
                            };
                            $http(postRequest);
                        }
                    }, function (failureResponse) {
                        console.log(failureResponse);
                    })
                }
            });

        }
        $scope.smsType=false;
        $scope.invoiceBill=function (table,type) {
            $scope.currTableName=table.tableName;
            $scope.currTableId=table.tableId;
            $http.get($scope.restaurantServerURL + '/getTempData?currTableName=' + $scope.currTableName + "&currTableId=" + $scope.currTableId +"&locationId="+$rootScope.selectedLocation).then(function (response) {
                var data = response.data;
                var token=data;
                $scope.selectedItemsList = JSON.parse(token.selectedItemsList);
                $scope.currTableName = token.tableName;
                $scope.tableSearchText = token.tableName;
                $scope.currTableId = token.tableId;
                $scope.orderNo = token.orderNo;
                $scope.waiterSearchText = token.useraccount_id;
                $scope.employeeSearchText = token.useraccount_id;
                $scope.waiterName = token.useraccount_id;
                $scope.customerId = token.customerId;
                $scope.customerMobileNo = token.customerNo;
                $scope.getSelectedCustomer($scope.customerId);
                $scope.agentSearchText = token.agentId;
                $scope.invoiceType="DineIn";
                $scope.orderToKitchenBuffer=[];
                $scope.splitTable2List=[];
                $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];
                $scope.paymentMethodType="other";
                $scope.pageType="other";
                $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                $scope.openSettlePayment(false,"",type);
            }, function (failureResponse) {
                console.log(failureResponse)
            });
        }

        $scope.getTableConfigurationList = function () {
            $http.post("/hipos/getTableConfigList").then(function (successResponse) {
                    $scope.tableConfigurationList = angular.copy(successResponse.data);
                }, function () {
                    Notification.error('Something went wrong');
                }
            )
        };
        $scope.getTableConfigurationList();
        $scope.appendTable=function (table) {
            $rootScope.selectedTableId=table.tableid;
            $rootScope.selectedTableName=table.tableName;
            $rootScope.orderTypeName='DineIn';
            $rootScope.guestName="";
            $window.location.href = '#!/restaurant';
        };
        $scope.setOrderType=function (type) {
            $rootScope.orderTypeName=type;
            $rootScope.selectedTableId=null;
            $rootScope.selectedTableName=null;
            $window.location.href = '#!/restaurant';
        };

        $scope.openOnlinePage = function (type) {
            $rootScope.orderTypeName=type;
            $rootScope.selectedTableId=null;
            $rootScope.selectedTableName=null;
            $window.location.href="#!/OnlineDelivery";
        };
        $scope.openTableReservation = function (type) {
            $rootScope.orderTypeName=type;
            $rootScope.selectedTableId=null;
            $rootScope.selectedTableName=null;
            $window.location.href="#!/tableReserve";
        };
        $scope.openDigiOrders = function (type) {
            $rootScope.orderTypeName=type;
            $rootScope.selectedTableId=null;
            $rootScope.selectedTableName=null;
            $window.location.href="#!/digiOrders";
        }

    });

