app.controller('accGroupCntrl',
    function ($scope, $timeout, $http, $location, $filter, Notification) {
        $scope.bshimServerURL = "/hipos";
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
            $scope.accountType = null;
            $scope.accountCode = "";
        };
        $scope.addNewAccountTypePopulate = function () {
            $('#accountGroup').text("Add Account Type");
            $scope.StatusText = "Active";
            $scope.operation ='Add';
            $("#submit").text("Save");
            $("#add_new_accountType_modal").modal('show');
        };
        //
        $scope.getAccountTypeList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }

            $(".loader").css("display", "block");
            $http.post($scope.bshimServerURL  + '/getAccountTypeList?searchText=' + val).then(function (response) {
                var data = response.data;
                $scope.accountTypeList= data;
                $scope.searchText = val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })

        };
        $scope.getAccountTypeList();
        // $scope.getAccountGroupList = function (val) {
        //     if (angular.isUndefined(val)) {
        //         val = "";
        //     }
        //
        //     $(".loader").css("display", "block");
        //     $http.post($scope.bshimServerURL  + '/getAccountGroupList?searchText=' + val).then(function (response) {
        //         var data = response.data;
        //         $scope.accountGroupList= data;
        //         $scope.searchText = val;
        //     }, function (error) {
        //         Notification.error({
        //             message: 'Something went wrong, please try again',
        //             positionX: 'center',
        //             delay: 2000
        //         });
        //     })
        //
        // };
        //
        // $scope.getAccountGroupList();


        $scope.getAccountGroupList = function (page) {
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
            $http.post($scope.bshimServerURL + "/getPaginatedAcctGrpList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.searchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                var i = 0;
                $scope.accountGroupList = data.list;
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
        $scope.getAccountGroupList();


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
            $scope.getAccountGroupList();

        };



        $scope.editAccountGroup  = function(data) {
            $scope.accountId = data.accountId;
            $scope.accountName = data.accountName;
            $scope.StatusText = data.status;
            $scope.accountDesc = data.accountDescription;
            $scope.accountCode = data.accountCode;
            $scope.accountType= parseInt(data.accountType);
            $scope.operation = 'Edit';
            $("#submit").text("Update");
            $('#accountGroup').text("Edit Account Group");
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
                Notification.error({message: 'Enter Account Name', positionX: 'center', delay: 2000});
            }
            else if ($scope.accountType === ''||$scope.accountType==null||angular.isUndefined($scope.accountType)) {
                Notification.error({message: 'Enter Account Type', positionX: 'center', delay: 2000});
            }
            else if ($scope.accountCode === ''||$scope.accountCode==null||angular.isUndefined($scope.accountCode)) {
                Notification.error({message: 'Enter Account Code', positionX: 'center', delay: 2000});
            }

            else {
                var saveAccTypeDetails;
                saveAccTypeDetails = {
                    accountId: $scope.accountId,
                    accountName: $scope.accountName,
                    accountDescription: $scope.accountDesc,
                    accountType:$scope.accountType,
                    accountCode:$scope.accountCode,
                    status: $scope.StatusText
                };
                $http.post($scope.bshimServerURL + '/saveAccountGroup', angular.toJson(saveAccTypeDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if (data == "") {
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $scope.removeaccountTypeDetails();
                        $scope.getAccountGroupList();
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

            }
            ;
        };

        $scope.deleteAccountGroup = function (data) {
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
                            accountCode : data.accountCode

                        };
                        $http.post($scope.bshimServerURL +"/deleteAccountGroup", angular.toJson(deleteDetails, "Create")).then(function (response, status, headers, config) {
                            var data = response.data;
                            $scope.getAccountGroupList();
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