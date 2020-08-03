/**
 * Created by prasad on 10/30/2017.
 */


app.controller('tableCtrl',[
    '$scope','$http','$window','$timeout','Notification',
    function ($scope,$http,$window,$timeout,Notification) {

        $scope.posPurchaseServerURL = "/purchase";
        $scope.hiposServerURL = "/hipos";
        $scope.inactiveStatus="Active";
        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";
        $scope.firstPage = true;
        $scope.lastPage = false;
        $scope.isNext = false;
        $scope.isPrev = false;
        $scope.pageNo=0;


        $scope.inactiveTable = function (){
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
        
        $scope.listStatus="";
        $scope.location=0;

        $scope.configuredSeats = function() {

            $scope.duplicateTableConfig = false;
            $scope.duplicateTableName = false;
            var tableid  = 0;
            if($scope.tableObj !== undefined)
                tableid = $scope.tableObj.tableid;

            var tableName = $scope.tablenameText;
            var configname = $scope.configurationName;
            var row = $scope.rownoText;
            var col = $scope.columnnoText;
            var list = $scope.tableList;

            //search for duplicate table name
            // var filteredArray = list.filter(function(table) {
            //     return(tableName && (table.tableName.toUpperCase() === tableName.toUpperCase()) );
            // });
            // if(filteredArray.length !== 0 && filteredArray[0].tableid !== tableid) {
            //     $scope.duplicateTableName = true;
            //     return false;
            // }
            //search for duplicate configuration
            filteredArray = list.filter(function (table) {
                return ((table.configurationname === configname) && (table.gridLocationH === row) && (table.gridLocationV === col));
            });
            if(filteredArray.length !== 0 && filteredArray[0].tableid !== tableid){
                $scope.duplicateTableConfig = true;
                return false;
            }
            $scope.tableObj = undefined;
            return true;
        };
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
            $http.post($scope.hiposServerURL + "/getpaginatedtable?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.searchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.tableList = data.list;
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
        $scope.synchronize = function(){
            $scope.checkboxlist =[];
            angular.forEach($scope.tableList,function (val,key) {
                if (val.checkbox == true) {
                    $scope.checkboxlist.push(val);
                }
            });
            if($scope.checkboxlist.length>0) {
                $http.post("/hipos" + '/synchronizeTable', angular.toJson($scope.checkboxlist)).then(function (response) {
                    var data = response.data;
                    if (data != "") {
                    Notification.success({
                        message: 'Successfully Syncronized',
                        positionX: 'center',
                        delay: 2000
                    });
                }
                    angular.forEach($scope.tableList, function (val, key) {
                        if (val.checkbox == true) {
                            val.checkbox = false;
                        }
                    })
                })
            }else {
                Notification.error({message:'Please Select  Check Box',
                positionX:'center',
                delay:2000})
            }
        };



        $scope.addtable = function (val) {
            $scope.tablenameText = "";
            $scope.noOfChairsText = "";
            $scope.configurationName = null;
            $scope.rownoText = null;
            $scope.columnnoText =null;
            $scope.mincapacity = "1";
            $scope.maxcapacity = "2";
            $scope.statusText="Active";
            $scope.tableid=null;

            if (angular.isUndefined(val)) {
                val = "";
            }
            $('#table-title').text("Add Table");
            $("#submit").text("Save");
            $("#table_modal").modal('show');
        };
        // $scope.getInventoryLocationList = function () {
        //     $http.get($scope.hiposServerURL + "/" + $scope.customer + '/addUserAccountSetup').then(function (response) {
        //         var data = response.data;
        //         $scope.inventoryLocationList = data;
        //         $scope.fromLocation=$scope.fromLocation = parseInt($scope.inventoryLocationList[0].userLocationId);
        //     }, function (error) {
        //         Notification.error({
        //             message: 'Something went wrong, please try again',
        //             positionX: 'center',
        //             delay: 2000
        //         });
        //     })
        // };
        // $scope.getInventoryLocationList();
        $scope.tableconfig = function () {
            var data = $scope.configurationName;
            $http.get($scope.hiposServerURL +  '/getTableConfig/'+data).then(function (response) {
                var data = response.data;
                //add +1 to list
                $scope.horizontalList = data[0].map(function(currentValue,index,array){return currentValue+1});
                $scope.verticalList = data[1];
            })
        };

        $scope.importPopup = function(){
            $("#import_table").modal('show');
        };

        $scope.saveTableImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("itemDetails");
            var itemDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL +  '/saveTableImport',itemDetails,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $scope.getPaginationList();
                    $("#import_table").modal('hide');
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
        };

        $scope.getList = function () {
            $http.post($scope.hiposServerURL+"/getTableConfigList").then(function (response) {
                var data = response.data;
                $scope.getConfigList = data;
            })
        };
        $scope.getList();

        $scope.removeTable = function (){
            $scope.tablenameText = "";
            $scope.configurationName = "";
            $scope.rownoText = "";
            $scope.columnnoText = "";
            $scope.noOfChairsText = "";
            $scope.mincapacity = "";
            $scope.maxcapacity = "";
            $scope.StatusText =null;

        };
        $scope.saveTable = function () {
         if($scope.tablenameText==''||$scope.tablenameText==null||angular.isUndefined($scope.tablenameText)){
                         Notification.warning({message:'Table Name cannot be empty', positionX: 'center', delay: 2000})
                     }
                     else if($scope.configurationName==''||$scope.configurationName==null||angular.isUndefined($scope.configurationName)){
                         Notification.warning({message:'Zone Name cannot be empty', positionX: 'center', delay: 2000})
                     }
                    else if($scope.rownoText==''||$scope.rownoText==null||angular.isUndefined($scope.rownoText)){
                         Notification.warning({message:'No. Of Rows cannot be empty', positionX: 'center', delay: 2000})
                     }
            var noOfRows = $scope.rownoText;
            var noOfColumns = $scope.columnnoText;
            var savetablepos;
            savetablepos = {
                tableid:$scope.tableid,
                tableName:$scope.tablenameText,
                configurationname:$scope.configurationName,
                gridLocationH:noOfRows,
                gridLocationV:noOfColumns,
                status:$scope.statusText,
                chair:$scope.noOfChairsText,
                minCapacity:$scope.mincapacity,
                maxCapacity:$scope.maxcapacity
            };
            $http.post($scope.hiposServerURL + '/saveTable', angular.toJson(savetablepos, "Create")).then(function (response) {
                var data = response.data;
                if(data==""){
                    Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                }
                else {
                    $scope.getPaginationList();
                    $("#table_modal").modal('hide');
                    Notification.success({
                        message: 'Table  Created  successfully',
                        positionX: 'center',
                        delay: 2000
                    });
                }
            });

        };


        $scope.Button = "SelectAll";
        $scope.selectAll = function () {

            if ($scope.clicked == false) {
                $scope.select = "SelectAll";
                $scope.Button = "UnSelectAll";
                angular.forEach($scope.tableList, function (val, key) {
                    val.checkbox = true;
                })
            }
            else {
                $scope.select = "UnSelectAll";
                $scope.Button = "SelectAll";
                angular.forEach($scope.tableList, function (val, key) {
                    val.checkbox = false;
                })
            }
            $scope.clicked = !$scope.clicked;
        };

        // $scope.synchronize = function(){
        //     $scope.checkboxlist =[];
        //     angular.forEach($scope.tableList,function (val,key) {
        //         if (val.checkbox == true) {
        //             $scope.checkboxlist.push(val);
        //         }
        //     });
        //     $http.post($scope.hiposServerURL + "/" + "1" + '/synchronizeTable', angular.toJson($scope.checkboxlist)).then(function (response) {
        //         var data = response.data;
        //         Notification.success({
        //             message: 'Successfully Syncronized',
        //             positionX: 'center',
        //             delay: 2000
        //         });
        //         angular.forEach($scope.tableList,function (val,key) {
        //             if(val.checkbox==true){
        //                 val.checkbox=false;
        //             }
        //         })
        //     })
        // }
        $scope.Button = "SelectAll";
        $scope.selectAll = function () {

            if ($scope.clicked == false) {
                $scope.select = "SelectAll";
                $scope.Button = "UnSelectAll";
                angular.forEach($scope.tableList, function (val, key) {
                    val.checkbox = true;
                })
            }
            else {
                $scope.select = "UnSelectAll";
                $scope.Button = "SelectAll";
                angular.forEach($scope.tableList, function (val, key) {
                    val.checkbox = false;
                })
            }
            $scope.clicked = !$scope.clicked;
        };


        $scope.editpopup = function(data,val){
            if (angular.isUndefined(val)) {
                val = "";
            }
            $scope.tableObj = data;
            console.log('*');
            console.log(data);
            $scope.tableid = data.tableid;
            var promise = $http.get($scope.hiposServerURL + '/getTableConfigurationList?tableConfigSearchText='+val +'&status='+"Active").then(function(response){return response});
            promise.then(function (response) {
                var data = response.data;
                $scope.tableConfig = data;
                //populate list
                $scope.horizontalList = data.rowtableconfig.map(function(currentValue,index,array) {
                    return currentValue+1
                });
                $scope.verticalList = data.columntableconfig;

            },function(failure){console.log(failure)});
            $scope.tablenameText = data.tableName;
            $scope.statusText = data.status;
            $scope.configurationName = data.configurationname;
            $scope.mincapacity = data.minCapacity;
            $scope.maxcapacity = data.maxCapacity;
            $scope.noOfChairsText = data.chair;
            $("#submit").text("update");
            $('#table-title').text("Edit Table");
            $("#table_modal").modal('show');
            var cfg = $scope.tableObj.configurationname;
            $scope.configurationName = cfg;
            $scope.tableconfig();
            $timeout( function(){
                $scope.rownoText = data.gridLocationH;
                $scope.columnnoText = data.gridLocationV;
                console.log('updated');
            }, 1000 );

        };

        $scope.deleteTable = function (data) {
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
                            tableid:data.tableid

                        };
                        $http.post($scope.hiposServerURL +"/deleteTable", angular.toJson(deleteDetails, "Create")).then(function (response, status, headers, config) {
                            var data = response.data;
                            $scope.getPaginationList();
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
