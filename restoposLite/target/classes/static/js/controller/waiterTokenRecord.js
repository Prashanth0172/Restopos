app.controller('waiterTokenRecordCtrl',
    function ($scope, $http, $location, $filter, Notification) {
        $scope.waiterName="";
        $scope.getEmployeeList = function () {
            $http.post('/hipos' + '/getEmployeeList?employeeSearchText=' + "").then(function (response) {
                var data = response.data;
                $scope.employeeList = data;
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            })
        };
        $scope.getEmployeeList();
        // $scope.getTokenRecordsList=function(){
        //     $http.get('/hipos/getKitchenOrders?requestFrom=waiter&waiterName='+$scope.waiterName).then(function (response) {
        //         var data = response.data;
        //         $scope.WaiterList = data;
        //         angular.forEach(data,function(value,key){
        //             if(value.tokenItemDetailsList !==null){
        //                 value.tokenItemDetailsList = JSON.parse(value.tokenItemDetailsList);
        //                 angular.forEach(value.tokenItemDetailsList,function(item,k){
        //                     item.numberList =  $.map($(Array(item.qtyCompleted + 1)),function(val, i) { return i; })
        //                 })
        //             }
        //         });
        //         $scope.tokenRecordsList = data;
        //         $("#tokenRecordsList").modal('show');
        //     });
        // }
        $scope.getTokenRecordsList=function(){
            if($scope.waiterName==null){
                $scope.waiterName ="";
            }
            $http.get('/hipos/getKitchenOrders?requestFrom=waiter&waiterName='+$scope.waiterName).then(function (response) {
                var data = response.data;
                $scope.WaiterList = JSON.parse(data.kitchenList);
                $scope.ItemsList=[];
                angular.forEach($scope.WaiterList, function (value, key) {
                    if (value.tokenItemDetailsList !== null) {
                        value.tokenItemDetailsList = JSON.parse(value.tokenItemDetailsList);
                        angular.forEach(value.tokenItemDetailsList,function (val,key) {
                            // $scope.ItemsList.push($scope.KitchenOrderList);
                            if(val.status=='Completed') {
                                $scope.ItemsList.push({
                                    tokenNo: value.tokenNo,
                                    tableName: value.tableName,
                                    waiter: value.waiter,
                                    kitchenTokenStart: value.kitchenTokenStart,
                                    itemName: val.itemName,
                                    orderNo:value.orderNo,
                                    qtyCompleted: val.qtyOrdered
                                });
                            }
                        })

                    }
                });
                $("#tokenRecordsList").modal('show');
            });
        }
        $scope.ReadyOrder = function (token) {
            angular.forEach($scope.WaiterList, function (val, key) {
                if (token.tokenNo == val.tokenNo) {
                    angular.forEach(val.tokenItemDetailsList, function (value, key) {
                        if (token.itemName == value.itemName) {
                            value.qtyDelivered=value.qtyOrdered;
                            value.status ="Ready";
                            $scope.checkQty(value.qtyDelivered,val);
                            $scope.updateStockStatus(val);
                        }

                    })
                }
            });
            if(token.orderNo!=null&&!angular.isUndefined(token.orderNo)) {
                $http.post("/hipos/getStatusChange?status=" + "Ready" + "&item=" + token.itemName + "&order=" + token.orderNo).then(function (response) {
                    var data = response.data;
                });
            }
        };
        $scope.servedOrder = function (token) {
            angular.forEach($scope.WaiterList, function (val, key) {
                if (token.tokenNo == val.tokenNo) {
                    angular.forEach(val.tokenItemDetailsList, function (value, key) {
                        if (token.itemName == value.itemName) {
                            value.qtyDelivered=value.qtyOrdered;
                            value.status ="Served";
                            $scope.checkQty(value.qtyDelivered,val);
                            $scope.updateStockStatus(val);
                        }

                    })
                }
            });
            if(token.orderNo!=null&&!angular.isUndefined(token.orderNo)) {
                $http.post("/hipos/getStatusChange?status=" + "Served" + "&item=" + token.itemName + "&order=" + token.orderNo).then(function (response) {
                    var data = response.data;
                });
            }
        };
        $scope.checkQty=function(deliveredQty,token){
            var completedOrder = [];
            angular.forEach(token.tokenItemDetailsList,function(value,key){
                var diff = value.qtyOrdered - value.qtyDelivered;
                if(diff === 0){
                    completedOrder.push("true")
                }else {
                    completedOrder.push("false")
                }
            });

            if(completedOrder.indexOf("false") !== -1)
                token.status = "Partially Delivered";
            else
                token.status = "Completed";
        }
        //$timeout($route.reload, 300000);
        $scope.getTokenRecordsList();
        $scope.updateStockStatus=function(tokenRecord){
            var i=true,j=false;
            angular.forEach(tokenRecord.tokenItemDetailsList,function (val,key) {
                if(val.qtyDelivered>0){
                    if(val.qtyDelivered!=val.qtyOrdered){
                        i=false;
                    }
                }else {
                    i=false;
                }
                if(val.qtyDelivered>0){
                    j=true;
                }
            })
            if(i==true){
                tokenRecord.status='Delivered';
            }
            if(i==false){
                if(tokenRecord.status=='Delivered'){
                    i=false;
                }else {
                    i=true;
                }
            }
            if(i==false||j==false){
                Notification.warning({
                    message: 'Update Qty Correctly',
                    positionX: 'center',
                    delay: 2000
                });
            }
            if(i=true&&j==true){
                $http.post('/hipos' + '/updateTokenDetails',angular.toJson(tokenRecord)).then(function (response) {
                    var data = response.data;
                    $scope.tokenRecordsList=data;
                    $scope.getTokenRecordsList();
                    Notification.warning({
                        message: 'Updated successfully',
                        positionX: 'center',
                        delay: 2000
                    });
                });
            }
        }
});