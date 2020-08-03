app.controller('categoryinvController',
    function ($scope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window, $cookies, $httpParamSerializerJQLike) {
        // $scope.shortCutRestrict=keyPressFactory.shortCutRestrict();
        // body...\
        $scope.hiposServerURL = "/hipos";
        $scope.customer = 1;
        // added code for pagination on 19/08/2017
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=0;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;
        $scope.inactiveStatus="Active";
        $scope.operation = 'Create';

        $( ".modal" ).keydown(function(event){
            //alert("I want this to appear after the modal has opened!");
            var charKey = event.which || event.keyCode;
            if(charKey != 9 ) {
                // e.preventDefault();
                event.stopPropagation();
            }
        });
        $scope.importPopup = function(){
            $("#import_category").modal('show');
        }
        $scope.removeCategoryDetails = function () {
            $scope.CategoryNameText = "";
            $scope.ctgryStatus =null;
            $scope.CategoryDescriptionText="";
            $scope.operation = "";
        };
        $scope.saveCategoryImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("categoryDetails");
            var categoryDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL  + '/saveCategoryImport',categoryDetails,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#import_category").modal('hide');
                    $scope.getPaginatedCategoryList();
                    $scope.isDisabled= false;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                }
            )
        }
        $scope.addNewItemCategoryPopulate = function () {
            $(".loader").css("display", "block");
                $scope.itemCategoryId="";
                $scope.CategoryNameText="";
                $scope.CategoryDescriptionText="";
                $scope.Kitchen=null;
                $scope.defaultType=true;
                $scope.ctgryStatusText="Active";
                $scope.operation='Add';
                $('#category-title').text("Add Category");
            $("#submit").text("Save");
                $("#add_new_ItemCategory_modal").modal('show');
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
        };
        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";
        $scope.inactiveCategory = function (){
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
            $scope.getPaginatedCategoryList();

        };

        $scope.clicked = false;
        $scope.Button = "SelectAll";
        $scope.selectAll = function () {

            if ($scope.clicked == false) {
                $scope.select = "SelectAll";
                $scope.Button = "UnSelectAll";
                angular.forEach($scope.itemCategoryList, function (val, key) {
                    val.checkbox = true;
                })
            }
            else {
                $scope.select = "UnSelectAll";
                $scope.Button = "SelectAll";
                angular.forEach($scope.itemCategoryList, function (val, key) {
                    val.checkbox = false;
                })
            }
            $scope.clicked = !$scope.clicked;
        };
        $scope.synchronize = function(){
                $scope.checkboxlist = [];
                angular.forEach($scope.itemCategoryList, function (val, key) {
                    if (val.checkbox == true) {
                        $scope.checkboxlist.push(val);
                    }
                });
                if($scope.checkboxlist.length>0){
                $http.post($scope.hiposServerURL + '/synchronizeCategory', angular.toJson($scope.checkboxlist)).then(function (response) {
                    var data = response.data;
                    if (data != "") {
                        Notification.success({
                            message: 'Successfully Syncronized',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                    angular.forEach($scope.itemCategoryList, function (val, key) {
                        if (val.checkbox == true) {
                            val.checkbox = false;
                        }
                    })
                })
            }else {
                    Notification.error({
                        message: 'Please Select Check Box',
                        positionX: 'center',
                        delay: 2000
                    })
                };
        };

        $scope.editNewItemCategoryPopulate = function(data) {
            $scope.itemObj=data;
            $scope.itemCategoryId = data.itemCategoryId;
            $scope.CategoryNameText = data.itemCategoryName;
            $scope.CategoryDescriptionText=data.itemCategoryDesc;
            $scope.ctgryStatusText=data.status;
            $scope.Kitchen =parseInt(data.kitchen);
            $scope.defaultType=data.defaultType== "true";
            $scope.operation='Edit';
            $scope.getKitchenList();
            $('#category-title').text("Edit Category");
            $("#submit").text("Update");
            $("#add_new_ItemCategory_modal").modal('show');
        },function (error) {
            Notification.error({message: 'Something went wrong, please try again',positionX: 'center',delay: 2000});
        };

        $scope.isEmpty = function(card){
            return card == '';
        }
        $scope.deleteCategory = function (data) {
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
                        $http.post($scope.hiposServerURL + '/deleteCategory?itemCategoryId='+ data).then(function (response) {
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
                            $scope.getPaginatedCategoryList();
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



        $scope.getPaginatedCategoryList = function (page){
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
                prevPage: $scope.isPrev,
                nextPage: $scope.isNext
            }
            if (angular.isUndefined($scope.searchText)) {
                $scope.searchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginatedCategoryList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.searchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                $scope.itemCategoryList = data.list;
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
        $scope.getPaginatedCategoryList();

        $scope.saveNewItemCategory = function () {
            if ($scope.isEmpty($scope.CategoryNameText)) {
                Notification.warning({message: 'Category Name can not be empty', positionX: 'center', delay: 2000});
                return false;
            }

            else {
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000)
            var saveItemCategoryDetails;
            saveItemCategoryDetails = {
                itemCategoryId:$scope.itemCategoryId,
                itemCategoryName: $scope.CategoryNameText,
                itemCategoryDesc: $scope.CategoryDescriptionText,
                defaultType:$scope.defaultType,
                kitchen:$scope.Kitchen,
                status:$scope.ctgryStatusText
            };

            $http.post($scope.hiposServerURL + '/saveNewItemCategory', angular.toJson(saveItemCategoryDetails, "Create")).then(function (response, status, headers, config) {
                var data = response.data;
                if(data ===""){
                    Notification.error({
                        message: 'Item Category Already Created',
                        positionX: 'center',
                        delay: 2000
                    });
                }
                else {
                    $scope.getPaginatedCategoryList();
                    $scope.removeCategoryDetails();
                    $("#add_new_ItemCategory_modal").modal('hide');
                    Notification.success({
                        message: 'Item Category Created  successfully',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.getPaginatedCategoryList();
                }

            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });

            });
        };
    }

        $scope.getKitchenList = function () {
            $http.post($scope.hiposServerURL+"/getKitchenList").then(function (response) {
                var data = response.data;
                $scope.kitchenMasterList = data;
            })
        };
        $scope.getKitchenList();


        // $scope.GetValue = function () {
        //     $scope.exportsVlues = $scope.exportsType;
        // }

});