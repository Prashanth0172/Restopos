app.controller('kitchenTokenRecordCtrl',
    function ($scope, $http, $location, $filter, Notification,$timeout,$window,$rootScope) {
        $("#tokens").addClass('active');
        $('#tokens').siblings().removeClass('active');
        $("#receipt").addClass('active');
        $('#receipt').siblings().removeClass('active');
        $rootScope.tokenRecordsList = []
        // $scope.getTokenRecordsList=function(){
        //     $http.get('/hipos/getKitchenOrders?requestFrom=kitchen&waiterName=').then(function (response) {
        //         var data = response.data;
        //         $scope.kitchenTokenOrderList = data;
        //         angular.forEach(data,function(value,key){
        //            if(value.tokenItemDetailsList !==null){
        //                $scope.cancelList=[];
        //                value.tokenItemDetailsList = JSON.parse(value.tokenItemDetailsList);
        //                angular.forEach(value.tokenItemDetailsList,function(item,k){
        //                   item.numberList =  $.map($(Array(item.qtyOrdered - item.qtyCompleted + 1)),function(val, i) { return i; })
        //                   if(item.status=='Cancel'){
        //                       $scope.cancelList.push(item);
        //                   }
        //                })
        //                value.cancelList=$scope.cancelList;
        //            }
        //         });
        //         $rootScope.tokenRecordsList = data;
        //         $rootScope.tokenKitchenRecordsList = data;
        //         console.log(data);
        //         $("#tokenRecordsList").modal('show');
        //     },function(failureResponse){console.log(failureResponse)});
        // };


        $scope.getTokenRecordsList = function () {
            $http.get('/hipos/getKitchenOrders?requestFrom=kitchen&waiterName=').then(function (response) {
                var data = response.data;
                $scope.ItemsList = JSON.parse(data.itemsList);
                $scope.KitchenOrderList =JSON.parse(data.kitchenList);
                angular.forEach($scope.KitchenOrderList, function (value, key) {
                    value.tokenItemDetailsList = JSON.parse(value.tokenItemDetailsList);
                });
            })
        };
        $timeout(function () {
            $window.location.reload(true);
        }, 300000);
        $scope.getTokenRecordsList();
        $scope.checkQty=function(completedQty,token){
            var completedOrder = [];
            angular.forEach(token.tokenItemDetailsList,function(value,key){
                var diff = value.qtyOrdered - value.qtyCompleted;
                if(diff === 0){
                    completedOrder.push("true")
                }else {
                    completedOrder.push("false")
                }
            });

            if(completedOrder.indexOf("false") !== -1)
                token.status = "Partially Completed";
            else
                token.status = "Partially Delivered";
        }

        $scope.viewAllDetails = function () {
            // $scope.notification = notification;
            $("#viewalldetails").modal('show');
        }

        $scope.AcceptOrder = function (token) {
            angular.forEach($scope.KitchenOrderList, function (val, key) {
                if (token.tokenNo == val.tokenNo) {
                    angular.forEach(val.tokenItemDetailsList, function (value, key) {
                        if (token.itemName == value.itemName) {
                            value.qtyCompleted=value.qtyOrdered;
                            value.status ="Completed";
                            $scope.checkQty(value.qtyCompleted,val);
                            $scope.updateStockStatus(val);
                        }

                    })
                }
            });
            if(token.orderNo!=null&&!angular.isUndefined(token.orderNo)) {
                $http.post("/hipos/getStatusChange?status=" + "Accept" + "&item=" + token.itemName + "&order=" + token.orderNo).then(function (response) {
                    var data = response.data;
                });
            }
        };
        $scope.ReadyOrder = function (token) {
            angular.forEach($scope.KitchenOrderList, function (val, key) {
                if (token.tokenNo == val.tokenNo) {
                    angular.forEach(val.tokenItemDetailsList, function (value, key) {
                        if (token.itemName == value.itemName) {
                            value.qtyDelivered=value.qtyOrdered;
                            value.status ="Ready";
                            $scope.checkQty(value.qtyDelivered,val);
                            $scope.updateStockStatus(val,'ready');
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
            angular.forEach($scope.KitchenOrderList, function (val, key) {
                if (token.tokenNo == val.tokenNo) {
                    angular.forEach(val.tokenItemDetailsList, function (value, key) {
                        if (token.itemName == value.itemName) {
                            value.qtyDelivered=value.qtyOrdered;
                            value.status ="Served";
                            $scope.checkQty(value.qtyDelivered,val);
                            $scope.updateStockStatus(val,'ready');
                        }

                    })
                }
            })
            $http.post("/hipos/getStatusChange?status="+"Served"+"&item="+ token.itemName+"&order="+token.orderNo).then(function (response) {
                var data = response.data;
            });
        };
        $scope.updateStockStatus=function(tokenRecord,type){
            var i=true,j=false;
            if(type=='ready'){
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
            }else {
                angular.forEach(tokenRecord.tokenItemDetailsList, function (val, key) {
                    if (val.qtyCompleted != val.qtyOrdered) {
                        i = false;
                    }
                    if (val.qtyCompleted > 0) {
                        j = true;
                    }

                    //delete key numberList
                    delete val.numberList;
                })
                if (i == true) {
                    tokenRecord.status = 'Partially Delivered';
                }
                if (i == false) {
                    if (tokenRecord.status == 'Completed') {
                        i = false;
                    } else {
                        i = true;
                    }
                }
            }
            if(i==false||j==false){
                Notification.warning({
                    message: 'Update Qty Correctly',
                    positionX: 'center',
                    delay: 2000
                });
            }
            if(i=true&&j==true) {
                $http.post('/hipos/updateTokenDetails', angular.toJson(tokenRecord)).then(function (response) {
                    var data = response.data;
                    $rootScope.tokenRecordsList = data;
                    $rootScope.tokenKitchenRecordsList = data;
                    $scope.getTokenRecordsList();
                    Notification.warning({
                        message: 'Updated successfully',
                        positionX: 'center',
                        delay: 2000
                    });
                });
            }
        }


        $scope.msToTime = function(duration) {
            return ""+(new Date(duration).getHours())+"h :"+new Date(duration).getMinutes()+"m";
        }
    });


app.directive('countdown1', function() {
    return {
        restrict: 'E',
        template: '<div>{{countdownVal}}</div>',
        scope: {
            initVal: '='
        },
        controller: function($scope, $interval) {
            $scope.countdownVal = $scope.initVal;

            $interval(function () {
                if ($scope.countdownVal > 0) {
                    $scope.countdownVal--;
                }
            }, 1000);
        }
    }
});


app.directive('countdown2', function() {
    return {
        restrict: 'E',
        template: '<div>{{currentTimeElapsed}}</div>',
        scope: {
            date: '@',
            token: '@'
        },
        controller: function($scope, $interval,$rootScope,$http) {
            var future;
            var timeInMilli = $scope.date;
            var tokenid = $scope.token;
            future = timeInMilli;
            $http.post('/hipos' + "/" + 1 + '/getConfigureData').then(function (response) {
                if (response.data) {
                    $scope.pompOrderTimeOut =parseInt(response.data.pompOrderMaxTime);
                }
            });
            $interval(function () {
                var diff;
                diff = Math.floor((new Date().getTime() - future) / 1000);
                hours = Math.floor(diff / 3600) % 24;
                diff -= hours * 3600;
                minutes = Math.floor(diff / 60) % 60;
                diff -= minutes * 60;
                seconds = diff % 60;
                //default green
                if(minutes > 10 && minutes < 15)
                    document.getElementById(timeInMilli).style.backgroundColor = "yellow";
                else if(minutes > 15 && minutes < 30)
                    document.getElementById(timeInMilli).style.backgroundColor = "red";
                $scope.currentTimeElapsed = [hours + 'h',minutes + 'm', seconds + 's'].join(' ');
                angular.forEach($rootScope.tokenRecordsList,function(value,key){
                    if(value.restaurantTokenId ==tokenid){
                        value.hours=hours;
                        value.minutes=(hours*60)+minutes;
                        value.countDown=$scope.currentTimeElapsed;
                    }
                });
                $rootScope.tokenKitchenRecordsList=[];
                angular.forEach($rootScope.tokenRecordsList,function(value,key){
                    if(value.minutes < $scope.pompOrderTimeOut){
                        $rootScope.tokenKitchenRecordsList.push(value);
                    }
                });
            }, 1000);
        }
    }
});

app.service('Util', [
    function () {
        return {
            dhms: function (t) {
                var minutes, seconds;
                minutes = Math.floor(t / 60) % 60;
                t -= minutes * 60;
                seconds = t % 60;
                return [minutes + 'm', seconds + 's'].join(' ');
            }
        };
    }
]);


