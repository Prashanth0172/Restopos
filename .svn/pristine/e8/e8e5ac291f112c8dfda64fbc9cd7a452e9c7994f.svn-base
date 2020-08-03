app.controller('tableconfigurationCtrl',[
    '$scope','$http','Notification',
    function ($scope,$http,Notification) {

        $scope.posPurchaseServerURL = "/purchase";
        $scope.hiposServerURL = "/hipos/";
        $scope.supplier = 1;
        $scope.customer = 1;
        $scope.firstPage = true;
        $scope.lastPage = false;
        $scope.isNext = false;
        $scope.isPrev = false;
        $scope.pageNo=0;
        $scope.inactiveStatus="Active";

        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";

        // $scope.getInventoryLocationList = function () {
        //     $http.get($scope.hiposServerURL + "/" + $scope.customer + '/addUserAccountSetup').then(function (response) {
        //         var data = response.data;
        //         $scope.inventoryLocationList = data;
        //         $scope.fromLocation=$scope.fromLocation = parseInt($scope.inventoryLocationList[0].userLocationId);
        //         $scope.prefix=$scope.fromLocation = parseInt($scope.inventoryLocationList[0].prefix);
        //     }, function (error) {
        //         Notification.error({
        //             message: 'Something went wrong, please try again',
        //             positionX: 'center',
        //             delay: 2000
        //         });
        //     })
        // };
        // $scope.getInventoryLocationList();
        $scope.checkDuplicate = function (){
            $scope.duplicate = false;
            var enteredValue = $scope.configurationnametext;
            angular.forEach($scope.tableConfig,function (value,key) {
                if(enteredValue && value.configurationname.toUpperCase() === $scope.prefix+'-'+enteredValue.toUpperCase()&&parseInt(value.location)==parseInt($scope.fromLocation)){
                    $scope.duplicate ="Already Added";
                    $scope.configurationnametext = "";
                }
            });

            return $scope.duplicate;
        };
        $scope.importPopup = function(){
            $("#import_tableconfig").modal('show');
        }


        $scope.removeTableConfig = function (){
            $scope.configurationnametext = "";
            $scope.noofrowsText = "";
            $scope.noofcolumnsText = "";
            $scope.StatusText =null;

        };

        $scope.saveTableconfigImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("itemDetails");
            var itemDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL + '/TableConfigImportSave',itemDetails,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $scope.getPaginatedTableconfiList();
                    $("#import_tableconfig").modal('hide');
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
            $scope.getPaginatedTableconfiList();

        };

        $scope.AddTableConfiguration = function () {
            $scope.configurationnametext = "";
            $scope.noofrowsText = "";
            $scope.noofcolumnsText = "";
            $scope.status = "Active";
            $("#submit").text("Save");
            $("#tableCongiguration_modal").modal('show');
        };
        $scope.getPaginatedTableconfiList = function(page) {

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
            if (angular.isUndefined($scope.tableConfigSearchText)) {
                $scope.tableConfigSearchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginatedTableconfiList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.tableConfigSearchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.tableConfigList = data.list;
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
        $scope.getPaginatedTableconfiList();

        $scope.saveTableConfiguration = function () {
             if($scope.configurationnametext==''||$scope.configurationnametext==null||angular.isUndefined($scope.configurationnametext)){
                 Notification.warning({message:'Zone Name cannot be empty', positionX: 'center', delay: 2000})
             }
             else if($scope.noofrowsText==''||$scope.noofrowsText==null||angular.isUndefined($scope.noofrowsText)){
                 Notification.warning({message:'No.Of Rows cannot be empty', positionX: 'center', delay: 2000})
             }
            else if($scope.noofcolumnsText==''||$scope.noofcolumnsText==null||angular.isUndefined($scope.noofcolumnsText)){
                 Notification.warning({message:'No. Of Columns cannot be empty', positionX: 'center', delay: 2000})
             }
             else {
                var saveTableConfig;
                //for edit
                var id = undefined;
                if ($scope.tableObj !== undefined)
                    id = $scope.tableObj.tableconfigid;
                saveTableConfig = {
                    tableconfigid: id,
                    configurationname: $scope.configurationnametext,
                    rowtableconfig: $scope.noofrowsText,
                    columntableconfig: $scope.noofcolumnsText,
                    status: $scope.status
                }
                $http.post($scope.hiposServerURL + '/savetableconfiguration', angular.toJson(saveTableConfig, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if (data == "") {
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $("#tableCongiguration_modal").modal('hide');
                        $scope.getPaginatedTableconfiList();
                        Notification.success({
                            message: 'Table Zone  Created  successfully',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                })
                $scope.tableObj = undefined;
             }
        };

        $scope.editpopup = function(data){
            $scope.tableObj = data;
            $scope.tableObj.tableid = data.tableid,
                $scope.tableconfigid = data.tableconfigid,
                $scope.configurationnametext = data.configurationname,
                $scope.noofrowsText = data.rowtableconfig,
                $scope.noofcolumnsText = data.columntableconfig,
                $scope.status = data.status;
            $("#submit").text("Update");
            $("#tableCongiguration_modal").modal('show');

        };

        $scope.deleteTableConfig = function (data) {
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
                            tableconfigid:data.tableconfigid

                        };
                        $http.post($scope.hiposServerURL +"/deleteTableConfig", angular.toJson(deleteDetails, "Create")).then(function (response, status, headers, config) {
                            var data = response.data;
                            $scope.getPaginatedTableconfiList();
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

    }
]);

