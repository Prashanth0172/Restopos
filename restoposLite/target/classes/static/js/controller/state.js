app.controller('stateController',
    function ($scope, $timeout, $http, $location, $filter, Notification) {
        $scope.hiposServerURL = "/hipos";
        $scope.CountryNameText = "";
        $scope.StatusText = "";
        $scope.operation = 'Create';
        $scope.inactiveStatus = "Active";
        $scope.firstPage = true;
        $scope.lastPage = false;
        $scope.pageNo = 0;
        $scope.prevPage = false;
        $scope.isPrev = false;
        $scope.isNext = false;
        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";

        $scope.removeState = function () {
            $scope.stateId="";
            $scope.countryId = null;
            $scope.stateNameText = "";
            $scope.StatusText = "";
            $scope.stateId = "";
            $scope.operation = "";
        };
        $scope.importState = function(){
            $("#import_State").modal('show');
        }
        $scope.saveStateImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("stateDetails");
            var stateDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL + '/stateImportSave',stateDetails,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#import_State").modal('hide');
                    $scope.getPaginationList();
                    $scope.isDisabled= false;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.isDisabled= false;
                }
            )
        }



        $scope.inactiveButton = function (){
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
            $scope.getPaginationList();

        };

        $scope.getStateList = function () {
            $http.post($scope.hiposServerURL+"/getStateList").then(function (response) {
                var data = response.data;
                $scope.stateList = data;
            })
        };
        $scope.getStateList();
        $scope.getCountryList = function () {
            $http.post($scope.hiposServerURL+"/getCountryList").then(function (response) {
                var data = response.data;
                $scope.getCountryList = data;
            })
        };
        $scope.getCountryList();
        $scope.getPaginationList = function (page){
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
            if (angular.isUndefined($scope.searchText)) {
                $scope.searchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginatedState?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.searchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.stateList = data.list;
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
        $scope.getPaginationList();


        $scope.editState  = function(data) {
            $scope.stateId = data.stateId;
            $scope.countryId = data.countryId;
            $scope.stateNameText = data.stateName;
            $scope.StatusText = data.status;
            $scope.operation = 'Edit';
            $('#state-title').text("Edit State");
            $("#submit").text("update");
            $("#add_new_state_modal").modal('show');
        };
        $scope.addState = function () {
            $('#state-title').text("Add State");
            $scope.StatusText = "Active";
            $("#submit").text("Save");
            $("#add_new_state_modal").modal('show');
        };

        $scope.saveState = function () {
            if (angular.isUndefined($scope.countryId) || $scope.countryId == ''||$scope.countryId==null) {
                Notification.warning({message: ' Please select Country', positionX: 'center', delay: 2000});
            }
            else if (angular.isUndefined($scope.stateNameText) || $scope.stateNameText == ''||$scope.stateNameText==null) {
                Notification.warning({message: ' Please Enter the StateName', positionX: 'center', delay: 2000});
            }
            else {
                var saveStateDetails;
                saveStateDetails = {
                    stateId: $scope.stateId,
                    status: $scope.StatusText,
                    stateName: $scope.stateNameText,
                    countryId: $scope.countryId
                };
                $http.post($scope.hiposServerURL + '/saveState', angular.toJson(saveStateDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if (data == "") {
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $scope.removeState();
                        $scope.getStateList();
                        $("#add_new_state_modal").modal('hide');
                        if (!angular.isUndefined(data) && data !== null) {
                            $scope.searchText = "";
                        }
                        Notification.success({
                            message: 'State Created  successfully',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                }, function (error) {
                   var data = response.data;
                    $scope.getPaginationList();
                   if(data==""){
                       Notification.error({
                           message: 'Something went wrong, please try again',
                           positionX: 'center',
                           delay: 2000
                       });
                   }else {
                       Notification.error({
                           message: 'Something went wrong, please try again',
                           positionX: 'center',
                           delay: 2000
                       });
                   }
                });

            }
            ;
        };
        $scope.deleteState = function (data) {
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
                        $http.post($scope.hiposServerURL + '/deleteState?stateId='+ data).then(function (response) {
                            var data = response.data;
                            $scope.status = data.designationStatus;
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
                            $scope.getStateList();
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