app.controller('accTypeCntrl',
    function ($scope, $timeout, $http, $location, $filter, Notification) {
        $scope.bshimServerURL = "/hipos/";
        $scope.operation = 'Create';
        // $scope.inactiveStatus = "Active";
        $scope.firstPage = true;
        $scope.lastPage = false;
        $scope.pageNo = 0;
        $scope.prevPage = false;
        $scope.isPrev = false;
        $scope.isNext = false;
        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";

        $scope.inactiveStatus = "Active";
        $scope.removeaccountTypeDetails = function () {
            $scope.accountDesc="";
            $scope.accountId = "";
            $scope.StatusText = "";
            $scope.accountName = "";
        };
        $scope.addNewAccountTypePopulate = function () {
            $('#accountType-title').text("Add Account Type");
            $scope.StatusText = "Active";
            $("#submit").text("Save");
            $("#add_new_accountType_modal").modal('show');
        };

        $scope.getAccountTypeList = function (page) {
            switch (page) {
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
            $http.post($scope.bshimServerURL + "/getPaginatedAcctTypeList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.searchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                var i = 0;
                $scope.accountTypeList = data.list;
                $scope.first = data.firstPage;
                $scope.last = data.lastPage;
                $scope.prev = data.prevPage;
                $scope.next = data.nextPage;
                $scope.pageNo = data.pageNo;
                $scope.listStatus = true;
                // $scope.removeState();

            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        };
        $scope.getAccountTypeList();


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
            $scope.getAccountTypeList();

        };




        $scope.editAccountType  = function(data) {
            $scope.accountId = data.accountId;
            $scope.accountName = data.accountName;
            $scope.StatusText = data.status;
            $scope.accountDesc = data.accountDescription;
            $scope.operation = 'Edit';
            $("#submit").text("Update");
            $('#accountType-title').text("Edit Account Type");
            $("#add_new_accountType_modal").modal('show');
        }, function (error) {
            Notification.error({
                message: 'Something went wrong, please try again',
                positionX: 'center',
                delay: 2000
            });
        };


        $scope.saveAccountType = function () {
            if ($scope.accountName === ''||$scope.accountName==null||angular.isUndefined($scope.accountName)) {
                Notification.warning({message: 'Enter Account Name', positionX: 'center', delay: 2000});
            }
            else {
                var saveAccTypeDetails;
                saveAccTypeDetails = {
                    accountId: $scope.accountId,
                    accountName: $scope.accountName,
                    accountDescription: $scope.accountDesc,
                    status: $scope.StatusText
                };
                $http.post($scope.bshimServerURL + '/saveAccountType', angular.toJson(saveAccTypeDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if (data == "") {
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $scope.removeaccountTypeDetails();
                        $scope.getAccountTypeList();
                        $("#add_new_accountType_modal").modal('hide');
                        if (!angular.isUndefined(data) && data !== null) {
                            $scope.SearchText = "";
                        }
                        Notification.success({
                            message: 'Account Created  successfully',
                            positionX: 'center',
                            delay: 2000
                        });
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

        $scope.deleteAccountType = function (data) {
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
                    if(result == true){
                        var deleteDetails = {
                            accountId :data.accountId,
                            accountName:data.accountName,
                            status : data.status,
                            accountDescription : data.accountDescription,

                        };
                        $http.post($scope.bshimServerURL +"/deleteAccountType", angular.toJson(deleteDetails, "Create")).then(function (response, status, headers, config) {
                            var data = response.data;
                            $scope.getAccountTypeList();
                            if(data==true){
                                Notification.success({
                                    message: 'Successfully Deleted',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            }else {
                                Notification.warning({
                                    message: 'Cannot delete Already in Use',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            }
                        }, function (error) {
                            Notification.warning({
                                message: 'Cannot be delete,already it is using',
                                positionX: 'center',
                                delay: 2000
                            });
                        });
                    }
                }
            });
        };

    });