app.controller('shiftCtrl',
    function ($scope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window, $cookies, $httpParamSerializerJQLike) {
        $scope.hiposServerURL =  "/hipos/";
        $scope.retailServerURL = "/retail/";
        $scope.posPurchaseServerURL = "/purchase/";
        $scope.customerId = 1;
        $scope.customer = 1;
        $scope.countVal = 0;
        $scope.customerEmail = "";
        $scope.userRights = [];
        $scope.operation = 'Create';
        $scope.inactiveStatus="Active";
        $scope.number =/^[0-9]/;
        //added for pagination purpose @rahul
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=0;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;

        (function(){
            var setDateRestriction = function(){
                $scope.options = {
                    maxDate : new Date()
                };
            };
            $timeout(setDateRestriction, 0);
        })();
        $scope.clicked = false;
        $scope.inactiveShift = function (){
            if ($scope.clicked == false) {
                $scope.inactiveStatus = "InActive";
                $scope.ButtonStatus = "Active";
                var page = "Page";
            }
            else {
                $scope.inactiveStatus = "Active";
                $scope.ButtonStatus = "InActive";
                var page = "";
            }
            $scope.clicked = !$scope.clicked;
            $scope.getPaginatedShiftList();

        };


        $scope.ButtonStatus = "InActive";
        // $scope.inactiveShift = function(val) {
        //
        //     if($scope.clicked == false){
        //         $scope.inactiveStatus = "InActive";
        //         $scope.ButtonStatus = "Active";
        //         var page="Page";
        //     }
        //     else{
        //         $scope.inactiveStatus = "Active";
        //         $scope.ButtonStatus = "InActive";
        //         var page="";
        //     }
        //     $scope.clicked = !$scope.clicked;
        //     if (angular.isUndefined(val)) {
        //         val = "";
        //     }
        //     $http.get($scope.hiposServerURL + "/" + $scope.customer + "/getPaginatedShiftList?shiftSearchText=" + val+"&type="+  $scope.inactiveStatus).then(function (response) {
        //         var data = response.data;
        //         $scope.shiftList = data.data;
        //         $scope.listStatus=false;
        //
        //     })
        // };

        $scope.removeShiftDetails = function () {
            $scope.shiftId = "";
            $scope.shiftName = "";
            $scope.fromtime = "";
            $scope.totime = "";
        };
        $scope.captalize = function(str){
            var words = str.toLowerCase().split(' ');
            for (var i = 0; i < words.length; i++) {
                var letters = words[i].split('');
                letters[0] = letters[0].toUpperCase();
                words[i] = letters.join('');
            }
            return words.join(' ');
        }
        $scope.addShift = function () {
            $scope.status="Active";
            $('#shift-title').text("Add Shift");
            $("#add_new_Shift_modal").modal('show');
        };

        $scope.saveNewShift = function () {
            if (angular.isUndefined($scope.shiftName) || $scope.shiftName == '' || $scope.shiftName == null) {
                Notification.warning({message: 'Shift Name can not be empty', positionX: 'center', delay: 2000});}

            else if (angular.isUndefined($scope.fromtime) || $scope.fromtime == '' || $scope.fromtime == null) {
                Notification.warning({message: 'From Time can not be empty', positionX: 'center', delay: 2000});
            }

            else if (angular.isUndefined($scope.totime) || $scope.totime == '' || $scope.totime == null) {
                Notification.warning({message: 'To Time can not be empty', positionX: 'center', delay: 2000});
            }

            else {
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000)
                var saveShiftDetails;
                saveShiftDetails = {
                    shiftId: $scope.shiftId,
                    shiftName: $scope.captalize($scope.shiftName),
                    status: $scope.status,
                    fromTime:$scope.fromtime,
                    toTime:$scope.totime
                };
                $http.post($scope.hiposServerURL   + '/saveShift', angular.toJson(saveShiftDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if(data==""){
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $scope.removeShiftDetails();
                        $scope.getPaginatedShiftList();
                        $("#add_new_Shift_modal").modal('hide');
                        Notification.success({message: 'Shift Created  successfully', positionX: 'center', delay: 2000});
                    }

                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                });
            };
        };

        $scope.editshift = function (data) {
            $scope.shiftId = data.shiftId;
            $scope.shiftName=data.shiftName;
            $scope.status=data.status;
            $scope.totime=data.toTime;
            $scope.fromtime=data.fromTime;
            $scope.operation='Edit';
            $('#shift-title').text("Edit Shift");
            $("#add_new_Shift_modal").modal('show');
        },function (error) {
            Notification.error({message: 'Something went wrong, please try again',positionX: 'center',delay: 2000});

        };
        $scope.getPaginatedShiftList = function (page){
            switch (page){
                case 'firstPage':
                    $scope.firstPage = true;
                    $scope.lastPage = false;
                    $scope.isNext = false;
                    $scope.isPrev = false;
                    $scope.pageNo = 0;
                    break;
                case 'lastPage':
                    $scope.lastPage = true;
                    $scope.firstPage = false;
                    $scope.isNext = false;
                    $scope.isPrev = false;
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
            var paginationDetails;
            paginationDetails = {
                firstPage: $scope.firstPage,
                lastPage: $scope.lastPage,
                pageNo: $scope.pageNo,
                prevPage: $scope.prevPage,
                prevPage: $scope.isPrev,
                nextPage: $scope.isNext
            }
            if (angular.isUndefined($scope.shiftSearchText)) {
                $scope.shiftSearchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginatedShiftList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.shiftSearchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.shiftList = data.list;
                $scope.first = data.firstPage;
                $scope.last = data.lastPage;
                $scope.prev = data.prevPage;
                $scope.next = data.nextPage;
                $scope.pageNo = data.pageNo;
                $scope.listStatus = true;

            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        };
        $scope.getPaginatedShiftList();
        $scope.deleteShift = function (data) {
            bootbox.confirm({
                title: "Alert",
                message: "Do you want to Continue ?",
                buttons: {
                    confirm: {
                        label: 'OK'
                    },
                    cancel: {
                        label: 'Cancel'
                    }
                },
                callback: function (result) {
                    if (result == true) {
                        $http.post($scope.hiposServerURL + '/deleteShift?shiftId='+ data).then(function (response) {
                            var data = response.data;
                            if ($scope.status == "InActive") {
                                Notification.success({
                                    message: 'It is Already in use So Status Changes to Inactive',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            } else {
                                Notification.success({
                                    message: 'Successfully Deleted',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            }
                            $scope.getPaginatedShiftList();
                        }, function (error) {
                            Notification.error({
                                message: 'Something went wrong, please try again',
                                positionX: 'center',
                                delay: 2000
                            });
                        });
                    }
                }
            });
        };


    });
