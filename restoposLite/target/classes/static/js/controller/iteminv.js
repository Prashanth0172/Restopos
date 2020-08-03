app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

app.controller('iteminvCtrl',
    function ($scope,$rootScope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window) {
        window.$scope = $scope;
        $("#inventory").addClass('active');
        $('#inventory').siblings().removeClass('active');
        $('#sidebar-menu ul li ul li').removeClass('active');
        $('#sidebar-menu ul li ul').css('display','none');
        // body...\
        $scope.hiposServerURL = "/hipos/";
        $scope.retailServerURL = "/retail/";
        $scope.posPurchaseServerURL = "hiposrestapi/purchase";
        $scope.returnQty = parseFloat('0.00');
        $scope.totalBeforDiscount = parseFloat('0.00');
        $scope.totalDiscount = parseFloat('0.00');
        $scope.totalAfterDiscount = parseFloat('0.00');
        $scope.totalTaxAmt = parseFloat('0.00');
        $scope.customerId = 1;
        $scope.countVal = 0;
        $scope.customerEmail = "";
        $scope.userRights = [];
        $scope.operation = 'Create';
        $scope.cursorPosVal = 0;
        $scope.customer = 1;
        $scope.SIId = 0;
        $scope.taxList = [];
        $scope.serializableItemsList = [];
        $scope.itemList = [];
        $scope.posSalesList = [];
        $scope.posAdvanceSalesList = [];
        $scope.SIList = [];
        $scope.invokeOrderId = '';
        $scope.InvokeOrderList = [];
        $scope.selectedItemsList = [];
        $scope.retailPostData = [];
        $scope.customerSearchText = "";
        $scope.cardPayementList = [];
        $scope.voucherPayementList = [];
        $scope.removePayments = "";
        $scope.checkbox = "";
        $scope.totaldupltax = parseFloat(0);
        $scope.totaltaxdupltax = parseFloat(0);
        $scope.serialNo = "";
        $scope.today = new Date();
        $scope.companyName = "";
        $scope.cashPayment = parseFloat(0);
        $scope.taxSummaryList = [];
        $scope.companyAddress = "";
        $scope.companyPhoneNo = "";
        $scope.companyFax = "";
        $scope.companyGstNo = "";
        $scope.cutomerName = "";
        $scope.balanceAmt = parseFloat(0);
        $scope.inventoryadress = "";
        $scope.inventoryphone = "";
        $scope.inventoryfax = "";
        $scope.formNo = "";
        $scope.returnType = "";
        $scope.salesOrderList = [];
        $scope.receiptPaymentList = [];
        $scope.disableButtons = false;
        $scope.customerDetails = [];
        $scope.customerNameText = "";
        $scope.hiConnectNotificationList = [];
        $scope.fullUserName = "";
        //added for pagination
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=0;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;
        $scope.inactiveStatus="Active";
        var myArr = [];
        $('#body_info').on('keyup', function(e) {
                var keyCode = e.keyCode || e.which;
                if (keyCode == 9) {
                    var triggerElement = document.activeElement.id;
                    if(myArr.length>0){
                        lastFocus=myArr[0];
                        $('#' + lastFocus).parent().css({
                            "background-color": ""
                        });
                    }
                    if($('#'+triggerElement).focus())
                    {
                        myArr[0]=triggerElement;
                        $('#' + triggerElement).parent().css({
                            "background-color": "dimgray"
                        });
                    }
                }

        });


        $scope.taxTypes = [
            {name: 'FullTax', value: 'FullTax'},
            {name: 'SimplifiedTax', value: 'SimplifiedTax'},
        ];
        $scope.updatesimplifiedTax = function (newCustVal) {
            $scope.simplifiedTax = newCustVal.fullSimplTax;
        }
        $scope.updateCustomerId = function (newCustVal) {
            $scope.customer = newCustVal.customerId;
            $scope.removeAllItems();
        }
        $scope.barCodeInputfocus = function () {
            $("#barcodeInput").focus();
        };
        $scope.barCodeInputfocus();
        $scope.serialBarCodeInputfocus = function () {
            $("#serialBarcodeInput").focus();
        };
        $scope.importIntegrationPopup = function(){
            $("#import_integration_modal").modal('show');
        }
        $scope.reOrderLevelPopup = function(){
            $("#reorder_level_modal").modal('show');
        }

        $scope.saveItemImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("itemDetails");
            var itemDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL  + '/itemImport',itemDetails,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#import_item").modal('hide');
                    $scope.getPaginatedItemList();
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
        $scope.saveItemIntegrationImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("itemDetailsIntegration");
            var itemDetailsIntegration = new FormData(formElement);
            $http.post($scope.hiposServerURL + "/" + $scope.customer + '/itemImportEdit',itemDetailsIntegration,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#import_integration_modal").modal('hide');
                    $scope.getPaginatedItemList();
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
        $scope.saveReOrderLevelImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("reOrderLevelIntegration");
            var reOrderLevelIntegration = new FormData(formElement);
            $http.post($scope.hiposServerURL + "/" + $scope.customer + '/reOrderLevelImport',reOrderLevelIntegration,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#reorder_level_modal").modal('hide');
                    $scope.getPaginatedItemList();
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
            $scope.getPaginatedItemList();

        };

        $scope.editItem  = function(data) {
            $scope.productionItem = "";
            $scope.itemId = data.itemId;
            $scope.itemCodeText = data.itemCode;
            $scope.itemNameText = data.itemName;
            $scope.itemDiscText = data.itemDesc;
            $scope.itemCategoryId = data.itemCategory;
            $scope.fileName = data.itemImage;
            angular.forEach($scope.itemCategoryDTOList, function (val, key) {
                if (val.itemCategoryId == data.itemCategoryId) {
                    $scope.itemCategoryId = val;
                    }
                    })
            $scope.itemTypeId = data.itemType;
            $scope.hsnCode = data.hsnCode;
            $scope.foodtype = data.foodtype;
            $scope.itemStatusText = data.itemStatus;
            $scope.purchasePricingText = data.purchasePrice;
            $scope.salesPricingText = data.salesPrice;
            var itemProductStatus = data.productionItem;
            if (itemProductStatus == "productionName") {
                $scope.productionItem = true;
            }
            $("#submit").text("Update");
            $('#modal-title').text("Edit Item");
            $("#add_new_item_modal").modal('show');
        };

        $scope.synchronize = function () {
            $scope.ItemCheckboxList = [];
            angular.forEach($scope.itemList, function (val, key) {
                if (val.checkbox == true) {
                    $scope.ItemCheckboxList.push(val);
                }
            })
            if ($scope.ItemCheckboxList.length > 0) {
                $http.post($scope.hiposServerURL + '/synchronizeItem', angular.toJson($scope.ItemCheckboxList)).then(function (response) {
                    var data = response.data;
                    if(data!="") {
                        Notification.success({
                            message: 'Successfully Syncronized',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                    angular.forEach($scope.itemList, function (val, key) {
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
                });
            }
        }


        $scope.clicked = false;
        $scope.Button = "SelectAll";
        $scope.selectAll = function () {

            if ($scope.clicked == false) {
                $scope.select = "SelectAll";
                $scope.Button = "UnSelectAll";
                angular.forEach($scope.itemList, function (val, key) {
                    val.checkbox = true;
                })
            }
            else {
                $scope.select = "UnSelectAll";
                $scope.Button = "SelectAll";
                angular.forEach($scope.itemList, function (val, key) {
                    val.checkbox = false;
                })
            }
            $scope.clicked = !$scope.clicked;
        };

        $scope.updateItemsinLocation = function (code,location,status) {
            $scope.itemList=[];
            $scope.itemList.push(code);
            $scope.locationList=[];
            $scope.locationList.push(location.toLowerCase());
            $http.post($scope.hiposServerURL + "/" + $scope.customer + '/updateItemsinLocation?status='+status+'&items='+angular.toJson($scope.itemList)+'&locations='+angular.toJson($scope.locationList)).then(function (response) {
                $scope.getPaginatedItemList();
            }).error(function () {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            });
        }
        $scope.getPaginatedItemList = function (page){
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
            if (angular.isUndefined($scope.itemSearchText)) {
                $scope.itemSearchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginatedItemList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.itemSearchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.itemList = data.list;
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
        $scope.getPaginatedItemList();
        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";
        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=300,height=300');
            popupWin.document.open();
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" media="print" href="../poscss/recept_print.css"></head><body onload="window.print()">' + printContents + '</body></html>');
            popupWin.document.close();
        };

        $scope.EditItemPopulateList = function () {
            $http.post($scope.hiposServerURL + "/" + $scope.customer + '/getNewItemDetails?itemSearchText=').then(function (response) {
                    var data = response.data;
                    $scope.itemCategoryDTOList = data.itemCategoryDTOList;
                    $scope.itemTypeDTOList = data.itemTypeDTOList;
                    $scope.itemUOMTypeDTOList = data.itemUOMTypeDTOList;
                    $scope.itemMSICDTOList = data.itemMSICDTOList;
                    $scope.itemBrandDTOList = data.itemBrandDTOList;
                    $scope.itemCountTypeDTOList = data.itemCountTypeDTOList;
                    $scope.itemIPTaxDTOList = data.itemIPTaxDTOList;
                    $scope.itemOPTaxDTOList = data.itemOPTaxDTOList;
                    $scope.productionItem=data.productionItem;
                    $scope.getPaginatedItemList();
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                }
            )
        };

        /*
            Code for split the add new item model as show more and show less using Jquery.
         */
        $scope.next_wizard = function(){
            $("#sub_step1").removeClass("in active");
            $("#sub_step2").addClass("tab-pane fade in active");
        }
        $scope.back_wizard = function(){
            $("#sub_step2").removeClass("in active");
            $("#sub_step1").addClass("tab-pane fade in active");
        }

        $scope.exportItemsToFile = function(){
            var getRequest = {
                url:'/hipos/1//writeItemstoExcel',
                method:'GET',
                params:{}
            };
            $http(getRequest).then(function(successResponse){
                Notification.warning({message: 'Items Exported to Excel File', positionX: 'center', delay: 2000});
                },function(failureResponse){})
        }

        // $scope.synchronize = function () {
        //     $scope.ItemCheckboxList=[];
        //     angular.forEach($scope.itemList,function (val,key) {
        //         if(val.checkbox==true){
        //             $scope.ItemCheckboxList.push(val);
        //         }
        //     })
        //     $http.post($scope.hiposServerURL + "/" +$scope.customer+ '/synchronizeItem', angular.toJson($scope.ItemCheckboxList)).then(function (response, status, headers, config) {
        //         Notification.success({
        //             message: 'Successfully Syncronized',
        //             positionX: 'center',
        //             delay: 2000
        //         });
        //         angular.forEach($scope.itemList,function (val,key) {
        //             if(val.checkbox==true){
        //                 val.checkbox=false;
        //             }
        //         })
        //     })
        // }


        $scope.clicked = false;
        $scope.Button = "SelectAll";
        $scope.selectAll = function () {

            if ($scope.clicked == false) {
                $scope.select = "SelectAll";
                $scope.Button = "UnSelectAll";
                angular.forEach($scope.itemList, function (val, key) {
                    val.checkbox = true;
                })
            }
            else {
                $scope.select = "UnSelectAll";
                $scope.Button = "SelectAll";
                angular.forEach($scope.itemList, function (val, key) {
                    val.checkbox = false;
                })
            }
            $scope.clicked = !$scope.clicked;
        };
        $scope.deleteItem = function (data) {
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
                        $http.post($scope.hiposServerURL + '/deleteItem?itemId='+ data).then(function (response) {
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
                            $scope.getPaginatedItemList();
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