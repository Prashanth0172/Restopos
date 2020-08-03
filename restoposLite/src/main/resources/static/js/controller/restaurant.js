app.controller('restaurantCtrl', [
    '$scope',
    '$http',
    '$location',
    '$filter',
    'Notification',
    'ngTableParams',
    '$timeout',
    '$window',
    '$cookies',
    '$parse',
    '$rootScope',
    function ($scope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window, $cookies, $parse, $rootScope) {
        $scope.customer = 1;
        $scope.retailServerURL = "/retail/";
        $scope.hiposServerURL = "/hipos";
        $scope.restaurantServerURL = "/restaurant";
        $scope.totalCPDiscount = parseFloat('0.00');
        $scope.totalCPAmountTendered = parseFloat('0.00');
        $scope.totalTaxAmt = parseFloat('0.00');
        $scope.userRights = [];
        $scope.notificationFlag = false;
        $scope.customerId = 1; //'Cash Customer'
        $scope.SIId = 0;
        $scope.taxList = [];
        $scope.itemList = [];
        $scope.SIList = [];
        $scope.agentId = null;
        $scope.InvokeOrderList = [];
        $scope.selectedItemsList = [];
        $scope.retailPostData = [];
        $scope.hiposServiceCharge = 0;
        $scope.companyName = "";
        $scope.fullUserName = "";
        $scope.tax = "SimplifiedTax";
        $scope.fileToUpload = "";
        $scope.tableConfigurationList = [];
        $scope.taxString = "";
        $scope.total = 0;
        $scope.subTotal = 0;
        $scope.noWaiterSelected = "Busser";
        $scope.noTableSelected = "Take Away";
        $scope.disAmtInPer = 0;
        $scope.customerSearchText = 'Cash Customer';
        $scope.agentId = "";
        $scope.date=new Date();
        $scope.time = new Date().getTime();
        $scope.makeAgentReadonly = false;
        $scope.makeItemReadonly = false;
        var location = window.location.origin;
        $scope.taxTypes = [{
            value: 'CGST:SGST/UGST',
            text: 'CGST:SGST/UGST',
            Selected: true
        }
        ];
        $scope.orderToKitchenBuffer = [];
        $scope.previousOrdersConfirmed = [];
        $scope.restuarantType = "";
        $scope.orderToKitchenBufferCategory = "";
        $scope.makeEmployeeSearchTextReadonly = false;
        $scope.removedItemList = [];
        $scope.waiterName = "";
        $scope.orderTypeFlag = "DineIn";
        $scope.selectedItemListCopy = [];

        $scope.updateCustomerId = function (newCustVal) {
            $scope.customer = newCustVal.customerId;
        };
        $scope.paymentMethodType = 'cash';
        $scope.setPaymentType = function () {
            $scope.paymentMethodType = 'cash';
            $scope.tenderedAmount = $scope.totalAfterDiscount;
        }
        $scope.tokenRecordsList = [];


        $scope.getTableRerservationList = function(){
            $http.post($scope.hiposServerURL + "/getTableReservationList").then(function (response) {
                var data = response.data;
                $scope.tableReservationList = data;
            });

        };
        $scope.getTableRerservationList();

        $scope.OpenReservedTablesList = function (data) {
            $scope.tableReservationId = data.id;
            $scope.reservedTime = data.time;
            $scope.reservedDate = data.date;
            $scope.getTablesListForReserved();
            $("#add_new_tablereservation_modal").modal('show');
        };

        $scope.getplaceorder = function () {
            $http.get('/restaurant/getPlaceOrder').then(function (response) {
                var data = response.data;
                $scope.placeorderList = data;

            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        }
        // $scope.getplaceorder();


        $scope.viewAllDetails = function (notification) {
            $scope.notification = notification;
            $("#viewalldetails").modal('show');
        }

        $scope.makeExclusive = function (item) {
            //fetch the value from key
            var value = item.inclusiveJSON;
            if (JSON.parse(value).sales === true) {
                Notification.warning('Changing Unit Price makes it Exclusive Tax');
                //reset the value to false
                value = value.replace("\"sales\":true", "\"sales\":false");
            }
            item.inclusiveJSON = value;
        };
        $scope.openpayment = function () {
            $scope.openStockValidateMultipaymnet();
        };
        $scope.itemsNoStockList = [];
        $scope.imageUpload = function (event) {
            var files = event.target.files;
            var file = files[0];
            var srcString;
            var imageCompressor = new ImageCompressor;
            var compressorSettings = {
                toWidth: 200,
                toHeight: 200,
                mimeType: 'image/png',
                mode: 'strict',
                quality: 1,
                grayScale: false,
                sepia: false,
                threshold: false,
                speed: 'low'
            };
            if (files && file) {
                var reader = new FileReader();
                reader.onload = function (readerEvt) {
                    binaryString = readerEvt.target.result;
                    $('.image-preview').attr('src', binaryString);
                };
                reader.readAsDataURL(file);
                reader.onloadend = function () {
                    srcString = $('.image-preview').attr("src");
                    imageCompressor.run(srcString, compressorSettings, proceedCompressedImage);
                };
            }
        };

        function proceedCompressedImage(compressedSrc) {
            $('#image-preview').attr('src', compressedSrc);
            $scope.fileToUpload = compressedSrc;
        }

        $scope.removeCustomerDetails = function () {
            $scope.customerNameText = "";
            $scope.customerEmailText = "";
            $scope.customerContactText = "";
            $scope.customerContactText = "";
            $scope.customerAddressText = "";
            $scope.fromRegNo = "";
            $scope.companyRegNo = "";
            $scope.UINText = "";
            $scope.Hi_Conn_company_Name = false;
        };

        $scope.removeReservation = function () {
            $scope.tableName = "";
            $scope.noOfPersons = "";
            $scope.time = "";
            $scope.guestName = "";

        };

        $scope.removeAllItemsWithoutCustomer = function () {
            $scope.totalCheckOutamt = parseFloat('0.00');
            $scope.totalBeforDiscount = parseFloat('0.00');
            $scope.totalDiscount = parseFloat('0.00');
            $scope.totalAfterDiscount = parseFloat('0.00');
            $scope.totalTaxAmt = parseFloat('0.00');
            $scope.invokeOrderName = "";
            $scope.invokeOrderId = "";
            $scope.invokeorder = "";
            $scope.selectedItemsList = [];
            $scope.itemSearchText = "";
            $scope.operation = 'Create';
            $scope.customerEmail = "";
            $scope.serialItems = "";
        };

        $scope.open1 = function() {
            $scope.popup1.opened = true;
        };
        $scope.remarklist= [];
        $scope.popup1 = {
            opened: false
        };

        $scope.next_wizard1 = function (grade) {
            $("#sub_steps12").removeClass("in active");
                $("#sub_steps21").addClass("tab-pane fade in active");
                $("#next123").hide();
                $("#back123").show();
                $("#saveIds").show();
                $("#close").show();

        }

        $scope.back_wizard1 = function () {

            $("#sub_steps21").removeClass("in active");
            $("#sub_steps12").addClass("tab-pane fade in active");

            $("#back123").hide();
            $("#next123").show();
            $("#close").show();
            $("#saveIds").hide();
        }

        $scope.removesDetails = function(){
            $("#sub_steps21").removeClass("in active");
            $("#sub_steps12").addClass("tab-pane fade in active");
        }

        $scope.getItemsOnCategory = function (itemCategory) {
            $http.get('/hipos/' + '/getItemListOnCategory?itemCategoryId=' + itemCategory.itemCategoryId + '&searchText=' + ''+'&locationId='+$rootScope.selectedLocation).then(function (response) {
                var data = response.data;
                $scope.itemListBasedonCategory = angular.copy(data);
            }), function (error) {
                Notification.error({
                    message: 'Somthing went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            };
        };
        $scope.totalVPAmountTendered = 0;

        $scope.openStockValidateMultipaymnet = function () {
            if (angular.isUndefined($scope.selectedItemsList) || $scope.selectedItemsList.length <= 0) {
                Notification.error({
                    message: 'At least One item has to be selected',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            } else if (!$scope.isValidatedData()) {
                Notification.warning({
                    message: 'Please Enter Proper Data',
                    positionX: 'center',
                    delay: 2000
                });
            } else {
                //alert('else');
                $scope.invokeOrderId = 1;
                // $http.post($scope.retailServerURL + '/validateCheckout?invokeOrderId=' + $scope.invokeOrderId,
                //     angular.toJson($scope.populateSaveSIData("", ""))).then(function (response, status, headers, config) {
                //     var data = response.data;
                //     $scope.itemsNoStockList = data;
                //     if ($scope.itemsNoStockList !== null && $scope.itemsNoStockList.length > 0 && $scope.advancepayment != true) {
                //         $("#cashpayment").modal('hide');
                //         $("#ItemsNoStockListModel").modal('show');
                //     } else {
                        $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                        $scope.totalVPBeforDiscount = parseFloat($scope.totalBeforDiscount).toFixed(2);
                        if (angular.isUndefined($scope.totalVPBeforDiscount) || $scope.totalVPBeforDiscount <= 0) {
                            $scope.totalVPBeforDiscount = 0;
                        }
                        $scope.amountWithoutDiscount = parseFloat($scope.totalBeforDiscount).toFixed(2);
                        $scope.totalVPDiscount = parseFloat($scope.totalDiscount).toFixed(2);
                        $scope.voucherNo = 0.00;
                        $scope.cardAmount = 0.00;
                        $scope.transactionNo = 0.00;
                        $scope.totalVPAfterDiscount = parseFloat($scope.totalAfterDiscount).toFixed(2);
                        $scope.totalVPAmountTendered = 0.00;
                        $scope.totalVPAmountRefunded = 0.00;
                        $scope.voucherAmt = 0.00;
                        $scope.totalVoucherAmt = 0.00;
                        $scope.totalPaidAmt = 0.00;
                        $scope.operation = "Sales";
                    // }
                // }, function () {
                //     Notification.error({message: data.message, positionX: 'center', delay: 2000});
                // })
            }
        };
        $scope.saveSplitSI = function (paymentType, type) {
            if (parseInt($scope.totalVPBeforDiscount) > parseInt($scope.totalPaidAmt)) {
                Notification.error({
                    message: 'Total Amount is not equal to Amount Tendered',
                    positionX: 'center',
                    delay: 2000
                });
            } else if ((angular.isUndefined($scope.tableSearchText) || $scope.tableSearchText == null || $scope.tableSearchText == "") && $scope.orderTypeFlag == 'DineIn') {
                Notification.error({
                    message: 'Select Table ',
                    positionX: 'center',
                    delay: 2000
                });
            } else if ((angular.isUndefined($scope.waiterSearchText) || $scope.waiterSearchText == null || $scope.waiterSearchText == "") && $scope.orderTypeFlag != 'takeAway') {
                Notification.error({
                    message: 'Select Waiter ',
                    positionX: 'center',
                    delay: 2000
                });
            }
            else {
                $scope.isDisabled = true;
                var restobject = $scope.populateSaveSplitSIData(paymentType);
                if (restobject != false) {
                    $http.post('/hipos' + '/save?tableNo=' + $scope.currTableId + "&tableName=" + $scope.currTableName + "&waiterName=" + $scope.waiterName + "&printVal=" + type ,
                        angular.toJson(restobject)).then(function (response) {
                        $scope.isDisabled = false;
                        var data = response.data.Desktop;
                        $scope.siNo = data.siData.siNo;
                        if ($scope.configurationData.buildTypeCloud && !$scope.printPreview) {
                            console.log('SplitBill - Cloud');
                            console.log(response.data.Cloud);
                            var postRequest = {
                                url: 'http://localhost:9001/hiAccounts/sendDatatoLocalPrinter',
                                data: response.data.Cloud,
                                method: 'POST',
                                params: {}
                            };
                            $http(postRequest);
                        }
                        $scope.TotalAmtAfterDiscount = 0;
                        angular.forEach(data.siData.selectedItemsList, function (value, key) {
                            $scope.TotalAmtAfterDiscount = value.amtexclusivetax + $scope.TotalAmtAfterDiscount;
                        });
                        $("#paymentNew1").modal('hide');
                        $scope.populatePrintData(data, type);
                        $scope.resetSplitPopUp();
                        $scope.removeAllItems();
                        //save first table then second
                        $scope.selectedItemsList = angular.copy($scope.splitTable1List);
                        $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                        if ($scope.splitTable1List.length == 0) {
                            $scope.orderType($scope.orderTypeFlag);
                        }
                        Notification.success({
                            message: 'Order has been saved successfully',
                            positionX: 'center',
                            delay: 2000
                        });
                        $rootScope.getTableList();
                    }, function (error) {
                        $scope.isDisabled = false;
                        Notification.error({
                            message: 'Something went wrong, please try again',
                            positionX: 'center',
                            delay: 2000
                        });
                    });
                }
            }
        };
        $scope.placeOrdersTOKOTNotification=function (response, currTableName) {
            var map={};
            map["json"]=angular.toJson(response);
            map["list"]=angular.toJson($scope.orderToKitchenBuffer);
            $http.post("/hipos" + '/placeOrderNotification?currTableName=' + currTableName +"&currTableId="+$scope.currTableId +"&waiter="+$scope.waiterSearchText +
                 "&customerName=" + $scope.customerSearchText+"&customerNo=" + $scope.customerContactText + "&orderNo=" + $scope.orderNo +"&pax="+$scope.pax+"&instructions="+ $scope.instructions+ "&agentId=" + $scope.agentSearchText + "&locationId="+$rootScope.selectedLocation+"&kotRequired="+$scope.promptKOT+"&duplicatePrint="+$scope.duplicatePrint,angular.toJson(map)).then(function (response) {
            });
        }
        $scope.saveNGetNextTableDetails = function (response, currTableName,
                                                    currTableId, isSplit, customerId) {
            if (angular.isUndefined($scope.waiterSearchText)) {
                $scope.waiterSearchText = "";
            }
            if (angular.isUndefined($scope.agentSearchText)) {
                $scope.agentSearchText = "";
            }
            if ($scope.orderNo == undefined) {
                $scope.orderNo = "";
            }
            if ((angular.isUndefined($scope.tableSearchText) || $scope.tableSearchText == null || $scope.tableSearchText == "") && $scope.orderTypeFlag == 'DineIn') {
                Notification.error({
                    message: 'Select Table ',
                    positionX: 'center',
                    delay: 2000
                });
            } else if ((angular.isUndefined($scope.waiterSearchText) || $scope.waiterSearchText == null || $scope.waiterSearchText == "") && $scope.orderTypeFlag != 'TakeAway'&& ($scope.orderTypeFlag != 'OnlineDelivery'&&$scope.notificationFlag!=true)) {
                Notification.error({
                    message: 'Select Waiter ',
                    positionX: 'center',
                    delay: 2000
                });
            }
            var map={};
            map["json"]=angular.toJson(response);
            map["list"]=angular.toJson($scope.orderToKitchenBuffer);
            //Save the data
            if(angular.isUndefined(currTableId)){
                currTableId='';
            }
            $http.post("/hipos" + '/saveTableDataTemp?currTableName=' + currTableName + "&currTableId=" + currTableId +"&employeeName=" + $scope.waiterName + "&customerId=" + customerId +"&pax="+$scope.pax+"&instructions="+ $scope.instructions+
                 "&orderNo=" + $scope.orderNo + "&agentId=" + $scope.agentSearchText + "&kotRequired="+$scope.promptKOT+"&duplicatePrint="+$scope.duplicatePrint,angular.toJson(map)).then(function (response) {
                //holds the reference of current selected table
                var data = response.data;
                $scope.selectedItemsList = [];
                if (!angular.isUndefined(data.selectedItemsList) && data.selectedItemsList !== null) {
                    //Save Previous Orders only 'Values' not 'References'
                    //angular.copy
                    $scope.previousOrdersConfirmed = angular.fromJson(data.selectedItemsList);
                    //Populate Previous Orders
                    $scope.selectedItemsList = angular.fromJson(data.selectedItemsList);
                    //Can't remove previous Orders - So make readonly
                    angular.forEach($scope.selectedItemsList, function (value, key) {
                        //add extra attribute to json
                        value.savedOrder = true;
                    });
                    console.log($scope.selectedItemsList);
                    $scope.customer = data.customerId;
                    $scope.tokenNumber = data.id;
                    //useraccount_id holds the reference of employee selected, so update
                    if (data.useraccount_id === null || data.useraccount_id.trim() === '') {
                        //No Employee assigned to table
                        $scope.makeEmployeeSearchTextReadonly = false;
                    } else {
                        //Employee assigned to table (don't modify)
                        $scope.makeEmployeeSearchTextReadonly = true;
                        $scope.waiterSearchText = data.useraccount_id;
                        $scope.agentSearchText = data.agentId;
                        $scope.waiterName = data.useraccount_id;
                    }
                    //Calculate Total Amount for selectedItems
                    $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                } else {
                    //No Previous Order found so we can select employee for the table
                    $scope.makeEmployeeSearchTextReadonly = false;
                    $scope.tokenNumber = data.id;
                }
                $("#restaurantTable").modal('hide');
            }), function (error) {
                Notification.error({
                    message: 'Somthing went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            };
        };
        $scope.selectedRow = null;
        $scope.onTableSelection = function (obj, type) {
            $scope.orderToKitchenBuffer = [];
            $scope.previousOrdersConfirmed = [];
            $scope.currTableName = obj.tableName;
            $scope.currTableId = obj.tableid;
            $scope.tableSearchText = obj.tableName;
            if (obj.useraccount_id != null) {
                $scope.waiterSearchText = obj.useraccount_id;
            }
            $scope.tableId = $scope.currTableId;
            $scope.getTempData();
            if (type == 'fromTable') {
                $scope.fromTableId = obj.tableid;
            } else if (type == 'toTable') {
                $scope.toTableId = obj.tableid;
            }
        };
        $scope.smsType=false;
        $scope.addItem = function (data, model) {
            if ($scope.restuarantType == 'qos') {
                $scope.tableSearchText = 'WalkIn';
                $scope.waiterSearchText = 'WalkIn';
            }
            if ((angular.isUndefined($scope.tableSearchText) || $scope.tableSearchText == '' || $scope.tableSearchText == null) && $scope.orderTypeFlag == 'DineIn') {
                Notification.info({message: 'Please Select a Table', positionX: 'center', delay: 5000});
            }
            else if ((angular.isUndefined($scope.waiterSearchText) || $scope.waiterSearchText == '' || $scope.waiterSearchText == null) && $scope.orderTypeFlag != 'TakeAway') {
                Notification.info({message: 'Please Select a Waiter', positionX: 'center', delay: 5000});
            }
            else if (angular.isUndefined($scope.customerSearchText) || $scope.customerSearchText == '' || $scope.customerSearchText == null) {
                Notification.info({message: 'Please Select a Customer', positionX: 'center', delay: 5000});
            }
            else {
                $scope.iteamIndex = $scope.itemIndexOf(model, data.itemName);
                //If item already added
                if (!angular.isUndefined($scope.iteamIndex) && $scope.iteamIndex !== null && $scope.iteamIndex !== -1) {
                    //fetch already added item qty
                    $scope.currentQty = model[$scope.iteamIndex].qty;
                    //update qty
                    model[$scope.iteamIndex].qty = parseFloat($scope.currentQty) + 1;
                    //update qty vs amount
                    $scope.editSelectedItemList(model[$scope.iteamIndex], model);

                } else {
                    $scope.addSelectedItemList(data, model);
                }
            }
            // $timeout(function () {
            //     document.getElementById("qty_" + data.itemId).focus();
            // }, 3000);
        };
        $scope.itemIndexOfItemCode = function (array, searchVal) {
            var itemIndex = -1;
            if ($scope.isUndefinedOrNull(searchVal)) {
                itemIndex = -1;
            } else if (array.length > 0) {
                var foundIndex = $filter('filter')(array, {
                    itemCode: searchVal
                }, true)[0];
                itemIndex = array.indexOf(foundIndex);
            }
            return itemIndex;
        };
        $scope.totalVoucherAmt = 0.00;
        $scope.OnChangeDiscount = function (oldValue) {
            if ($scope.totalCPDiscount < 0) {
                $scope.totalCPDiscount = oldValue;
                return;
            }
            if ($scope.totalCPDiscount > $scope.totalCPBeforDiscount) {

                Notification.info({
                    message: 'Discount cannot be greater than To be paid amount',
                    positionX: 'center',
                    delay: 2000
                });
                $scope.totalCPDiscount = oldValue;
                return;
            }
            $scope.totalCPAfterDiscount = parseFloat($scope.totalCPBeforDiscount - $scope.totalCPDiscount);

            $scope.getTotalAmtToBeReturned();
        };
        $scope.OnChangeVoucherDiscount = function (oldValue) {
            if ($scope.totalVPDiscount + $scope.totalVoucherAmt > $scope.totalVPBeforDiscount) {

                Notification.info({
                    message: 'Discount cannot be greater than To be paid amount',
                    positionX: 'center',
                    delay: 2000
                });
                $scope.totalVPDiscount = oldValue;
                return;
            }
            $scope.totalVPAfterAllDeductions = $scope.totalVPBeforDiscount - $scope.totalVPDiscount - $scope.totalVoucherAmt;
            $scope.getTotalVoucherAmtToBeReturned();
        };
        $scope.OnChangeVoucherAmt = function (oldValue) {
            if ($scope.totalVoucherAmt < 0) {
                $scope.totalVoucherAmt = oldValue;
                return;
            }
            if ($scope.totalVPDiscount + $scope.totalVoucherAmt > $scope.totalVPBeforDiscount) {

                Notification.info({
                    message: 'Voucher Amt cannot be greater than To be paid amount',
                    positionX: 'center',
                    delay: 2000
                });
                $scope.totalVoucherAmt = oldValue;
                return;
            }
            $scope.totalVPAfterAllDeductions = $scope.totalVPBeforDiscount - $scope.totalVPDiscount - $scope.totalVoucherAmt;
            $scope.getTotalVoucherAmtToBeReturned();
        };
        $scope.changeTablePopup = function (type) {
            $scope.fromTableSearchText = null;
            $scope.toTableSearchText = null;
            $scope.fromTableId = null;
            $scope.toTableId = null;
            var getRequest = {
                url: '/hipos/getChangeTableList',
                method: 'GET',
                params: {type: 'occupied'}

            };
            $http(getRequest).then(function (successResponse) {
                $scope.fromTableList = successResponse.data;
            });
            var getRequest = {
                url: '/hipos/getChangeTableList',
                method: 'GET',
                params: {type: 'remaining'}

            };
            $http(getRequest).then(function (successResponse) {
                $scope.toTableList = successResponse.data;
            });
            $("#changeTablePopUp").modal('show');
        };

        $scope.saveMergeToTableFields = function () {
            $scope.checkList = [];
            $scope.NameList = [];
            angular.forEach($scope.merge, function (val, key) {
                if (val.checkbox == true) {
                    $scope.checkList.push(val.tableid);
                    $scope.NameList.push(val.tableName);
                }
            })
            $("#saveMergeToTableFields").modal('hide');
            $("#mergeTablePopUp").modal('show');
            $scope.toTable = JSON.stringify($scope.checkList);
            $scope.toTabl = JSON.stringify($scope.NameList);

        };
        $scope.saveMergeFromTableFields = function () {
            $scope.SelectedList = [];
            $scope.SelectedListName = [];
            angular.forEach($scope.merge, function (val, key) {
                // if (val.checkbox == true) {
                    $scope.SelectedList.push(val.tableid);
                    $scope.SelectedListName.push(val.tableName);
                // }
            })
            $("#selectFromTableToMerge").modal('hide');
            $scope.fromTable = JSON.stringify($scope.SelectedList);
            $scope.fromTabl = JSON.stringify($scope.SelectedListName);

        };

        $scope.mergeTablePopup = function () {
            $scope.fromTabl = '';
            $scope.toTabl = '';
            if(angular.isUndefined($scope.searchText)){
                $scope.searchText="";
            }
            $http.get('/hipos/getMergeTableList?type='+'Empty'+"&searchText="+$scope.searchText).then(function (response) {
                var data = response.data;
                $scope.merge = data;
            });

            $("#mergeTablePopUp").modal('show');
            // $("#selectFromTableToMerge").modal('hide');
        };


        $scope.addTableReservation = function () {
            $scope.tableName ="";
            $scope.noOfPersons ="";
            $scope.guestName ="";
            $scope.Customeremail ="";
            $scope.CustomerPhone ="";
            $scope.date = new Date();
            $scope.back_wizard1();
            $("#sub_steps21").removeClass("in active");
            $("#saveIds").hide();
            $("#back123").hide();
            $("#add_Tablereservation_modal").modal('show');
        }

        $scope.getMergeToTableList = function () {
            $("#mergeTablePopUp").modal('hide');
            $("#selectToTableForMerge").modal('show');

        };
        $scope.getMergeFromTableList = function () {
            $("#mergeTablePopUp").modal('hide');
            $("#selectFromTableToMerge").modal('show');

        };


        $scope.selectedFromTable = function (data) {
            $scope.fromTable = data.tableid;
            $scope.fromTabl = data.tableName;
            $("#selectFromTableToMerge").modal('hide');
            $("#mergeTablePopUp").modal('show');

        };

        $scope.selectedCoupon = function (data) {
            $scope.applyvalue(data.vocherCode);
        $("#selectOffersPage").modal('hide');
        };

        $scope.removeVoucher = function () {
            $scope.couponNameText ="";
            $scope.coupon = "";
            $scope.disAmtInPer = 0;
            $scope.updatePayable();
        };
        $scope.applyvalue = function (coupon) {
            $scope.coupon = coupon.toUpperCase();
            $http.post("/hipos/validateCoupon?coupon=" + $scope.coupon).then(function (response) {
                var data = response.data;
                $scope.validatedCouponsList = data;
                if ($scope.validatedCouponsList.length != 0) {
                    angular.forEach($scope.validatedCouponsList, function (val, key) {
                        if (val.vocherCode == $scope.coupon) {
                            if (parseFloat($scope.totalAmtExamt) >= parseFloat(val.minBill)) {
                                if (val.discountType == "Percentage") {
                                    var dis = parseFloat($scope.totalAmtExamt) / 100 * parseFloat(val.discountAmount);
                                    if (dis <= val.maxDiscount) {
                                        $scope.disAmtInPer = val.discountAmount + "%";
                                    } else {
                                        $scope.disAmtInPer = val.maxDiscount;
                                    }
                                } else {
                                    $scope.disAmtInPer = val.discountAmount;
                                }
                                $scope.couponNameText = $scope.coupon;
                                $scope.updatePayable();
                                Notification.success({
                                    message: 'Coupon Applied Successfully',
                                    positionX: 'center',
                                    delay: 2000
                                });
                                $("#selectOffersPage").modal('hide');
                            } else {
                                $scope.coupon = "";
                                $scope.disAmtInPer = 0;
                                $scope.couponNameText = "";
                                $scope.updatePayable();
                                Notification.warning({
                                    message: 'Min Amount should not be less than coupon amount',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            }
                        }
                    });
                } else {
                    Notification.error({message: 'Please Enter Valid Coupon', positionX: 'center', delay: 2000})
                    $scope.coupon = "";
                    $scope.disAmtInPer = 0;
                    $scope.couponNameText = "";
                    $scope.updatePayable();
                }
            })
        };

        $scope.changeTable = function () {
            if ($scope.fromTableSearchText == null || $scope.toTableSearchText == null) {
                Notification.error({message: 'Please Select Table', positionX: 'center', delay: 2000});
            } else {
                var getRequest = {
                    url: '/hipos' + '/changeTableNames',
                    method: 'GET',
                    params: {
                        fromTable: $scope.fromTableSearchText.tableName,
                        toTable: $scope.toTableSearchText.tableName,
                        fromTableId: $scope.fromTableId,
                        toTableId: $scope.toTableId
                    }
                };
                $http(getRequest).then(function () {
                    $("#changeTablePopUp").modal('hide');
                    $scope.appendTableSelected($scope.toTableSearchText.tableName,'',$scope.toTableId);
                    Notification.info({message: 'Successfully Changed', positionX: 'center', delay: 2000});
                });
            }

        };

        $scope.getTablesListForReserved = function () {
            $http.post("/hipos/getTablesListForReserved?id="+$scope.tableReservationId).then(function (response) {
                var data = response.data;
                $scope.tableList = data;
            })
        };

        $scope.saveSelectedTableName = function () {
            // angular.forEach($scope.tableList,function (val,key) {
                // if(val.selectedObj==true){
                    $http.post("/hipos/saveSelectedTableName?tableId="+$scope.selectedObj+'&id='+$scope.tableReservationId).then(function (response) {
                        var data = response.data;
                        $("#add_new_tablereservation_modal").modal('hide');
                        Notification.success({message:'Table Reserved Successfully',positionX:'center',delay:2000})
                    })
                // }
            // })
        }

        $scope.saveReservationTable = function () {
            if ($scope.tableName==""||angular.isUndefined($scope.tableName)) {
                Notification.warning({message: 'Restaurant Name can not be empty', positionX: 'center', delay: 2000});
            }

            else {
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000)
                var saveItemDetails;
                saveItemDetails = {
                    noOfPersons: $scope.noOfPersons,
                    tableName: $scope.tableName,
                    date: $scope.date,
                    guestName: $scope.guestName,
                    time: new Date().getTime(),
                    status:'Reserve'


                };
                $http.post("/hipos/saveTableReservation", angular.toJson(saveItemDetails)).then(function (response) {
                    var data = response.data;
                    if(data==""){
                        Notification.error({
                            message: 'Restaurent Table Already Created',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                    else {
                        $scope.getTableRerservationList();
                        $("#add_Tablereservation_modal").modal('hide');
                        Notification.success({
                            message: 'Restaurent Table Created  successfully',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.getTableRerservationList();
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

        $scope.saveMergeTable = function () {
            if ($scope.fromTable == null || $scope.toTable == null) {
                Notification.error({message: 'Please Select Table', positionX: 'center', delay: 2000});
            } else {
                var getRequest = {
                    url: '/hipos' + '/saveMergeTable',
                    method: 'GET',
                    params: {
                        fromTableId: $scope.fromTable,
                        toTableId: $scope.toTable
                    }
                };
                $http(getRequest).then(function () {
                    $("#mergeTablePopUp").modal('hide');
                    Notification.info({message: 'Succesfully Merged', positionX: 'center', delay: 2000});
                });
            }

        };
        $scope.OnChangeCreditDiscount = function (oldValue) {
            if ($scope.totalCCPDiscount < 0) {
                $scope.totalCCPDiscount = oldValue;
                return;
            }
            if ($scope.totalCCPDiscount > $scope.totalCCPBeforDiscount) {
                Notification.info({
                    message: 'Discount cannot be greater than To be paid amount',
                    positionX: 'center',
                    delay: 2000
                });
                $scope.totalCCPDiscount = oldValue;
                return;
            }
            $scope.totalCCPAfterDiscount = $scope.totalCCPBeforDiscount - $scope.totalCCPDiscount;

        };
        $scope.addItemToMainTable = function (data) {
            if ($scope.currTableId != null) {
                $scope.addItem(data, $scope.selectedItemsList);
                $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                data.qty += 1;
                $('#iDitemSearchText').val("");
                $scope.itemSearchText = $("#iDitemSearchText").val("").trigger('change');
                $scope.itemSearchText = null;
            } else {
                $scope.addItem(data, $scope.selectedItemsList);
                $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                //Fresh order set qty to 1
                data.qty += 1;
                $('#iDitemSearchText').val("");
                $scope.itemSearchText = $("#iDitemSearchText").val("").trigger('change');
                $scope.itemSearchText = null;
            }

        };
        $scope.setClickedRow = function (index) {  //function that sets the value of selectedRow to current index
            $scope.selectedRow = index;
        };
        $scope.setSplitTable1ClickedRow = function (index) {  //function that sets the value of selectedRow to current index
            $scope.splitTable1SelRow = index;
        };
        $scope.setSplitTable2ClickedRow = function (index) {  //function that sets the value of selectedRow to current index
            $scope.splitTable2SelRow = index;
        };
        $scope.showSplitTablePopUp = function () {
            if (!$scope.isValidatedData()) {
                Notification.error({
                    message: 'Qty should not be Zero',
                    positionX: 'center',
                    delay: 2000
                });
            } else {
                $scope.disAmtInPer = 0;
                $scope.updatePayable();
                if ($scope.orderToKitchenBuffer.length > 0) {
                    $scope.placeOrdersTOKOT('payment');
                }
                if ($scope.selectedItemsList.length <= 0) {
                    Notification.error({
                        message: 'Atleast One Item has to be selected for Splitting the bill',
                        positionX: 'center',
                        delay: 2000
                    });
                    return;
                }
                $scope.splitTable1List = angular.copy($scope.selectedItemsList);
                $scope.splitTable2List = [];
                $("#splitTablePopUp").modal('show');
            }
        };
        $scope.resetSplitPopUp = function () {  //function that sets the value of selectedRow to current index
            $scope.splitTable2List = [];
            $scope.splitTable1SelRow = -1;
            $scope.splitTable2SelRow = -1;
            $scope.totalCPBeforDiscount = parseFloat('0.00');
            $scope.totalCPAmountRefunded = parseFloat('0.00');
            $scope.totalCPDiscount = parseFloat('0.00');
            $scope.totalCPAmountTendered = parseFloat('0.00');

            $scope.totalCCPBeforDiscount = parseFloat('0.00');
            $scope.totalCCPAmountRefunded = parseFloat('0.00');
            $scope.totalCCPDiscount = parseFloat('0.00');
            $scope.totalCCPAmountTendered = parseFloat('0.00');
            $scope.totalVPAmountTendered = parseFloat('0.00');
            $scope.totalVPAfterAllDeductions = parseFloat('0.00');
            $scope.totalVoucherAmt = parseFloat('0.00');
            $scope.totalVPAmountRefunded = parseFloat('0.00');
            $scope.totalVPDiscount = parseFloat('0.00');
            $scope.totalPaidAmt = parseFloat('0.00');
            $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];
            $scope.paymentList = [];
            $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
            if ($scope.tableSearchText != "")
                $scope.appendTableSelected($scope.tableSearchText,'',$scope.currTableId);

        };
        $scope.splitTable1List = [];
        $scope.splitTable2List = [];
        $scope.splitTable1SingleClickAction = function (item) {
            $scope.addItem(item, $scope.splitTable2List);
            if ($scope.checkIfQtyZero(item, $scope.splitTable1List)) {
                $scope.removeItemSplitTable1(item);
            }
        };
        $scope.splitTable2SingleClickAction = function (item) {
            $scope.addItem(item, $scope.splitTable1List);
            if ($scope.checkIfQtyZero(item, $scope.splitTable2List)) {
                $scope.removeItemSplitTable2(item);
            }
        };
        $scope.checkIfQtyZero = function (data, model) {
            $scope.iteamIndex = $scope.itemIndexOf(model, data.itemName);
            $scope.currentQty = model[$scope.iteamIndex].qty;
            model[$scope.iteamIndex].qty = $scope.currentQty - 1;
            $scope.currentQty = model[$scope.iteamIndex].qty;
            if ($scope.currentQty === 0) {
                return true;
            }
            return false;
        };

        $scope.splitTable2DoubleClickAction = function (item) {
            $scope.addSplitTableSelRow(item, $scope.splitTable1List);
            $scope.removeItemSplitTable2(item);
        };
        $scope.mergeTable2List = [];
        $scope.mergeTable1List = [];
        $scope.mergeTable2DoubleClickAction = function (item) {
            $scope.addSplitTableSelRow(item, $scope.mergeTable1List);
            $scope.removeItemMergeTable(item, $scope.mergeTable2List);
        };
        $scope.removeItemMergeTable = function (item, model) {
            $scope.iteamIndex = $scope.itemIndexOf(model, item.itemName);
            model.splice($scope.iteamIndex, 1);
        };

        $scope.removeItemSplitTable1 = function (item) {
            $scope.iteamIndex = $scope.itemIndexOf($scope.splitTable1List, item.itemName);
            $scope.splitTable1List.splice($scope.iteamIndex, 1);
        };
        $scope.removeItemSplitTable2 = function (item) {
            $scope.iteamIndex = $scope.itemIndexOf($scope.splitTable2List, item.itemName);
            $scope.splitTable2List.splice($scope.iteamIndex, 1);
        };

        $scope.validateCredentials = function (item) {
            var previousOrdersConfirmedRef = $filter('filter')($scope.previousOrdersConfirmed, {itemName: item.itemName}, true)[0];
            if(previousOrdersConfirmedRef!=undefined&&parseFloat(previousOrdersConfirmedRef.qty) > parseFloat(item.qty)){
                $scope.userNameText = "";
                $scope.pswdText = "";
               $("#openUserCredentialsForQty").modal('show');
               $scope.userNameText ="";
               $scope.pswdText ="";
               $scope.enteredQty= previousOrdersConfirmedRef.qty;
               $scope.itemQty = item;
            }else {
                $scope.onEditPriceRQty(item);
            }
        };

        $scope.savecredentialsForQty = function () {
            if ($scope.userNameText == $scope.userLoginId && $scope.password == $scope.pswdText) {
                $scope.onEditPriceRQty($scope.itemQty);
                    $scope.descriptionType = "kot";
                $scope.phone = $rootScope.phone;
                $scope.description ="";
                $("#description_popup_For_Cancel_Kot").modal('show');
            }else {
                $scope.itemQty.qty =$scope.enteredQty;
                Notification.error({message:'Invalid Credentials',positionx:'center',delay:2000})
            };
        };
        $scope.onEditPriceRQty = function (item) {
            $scope.editSelectedItemList(item, $scope.selectedItemsList);
            // $timeout(function(){
            //     document.getElementById("iDitemSearchText").focus();
            // },300);
        };
        $scope.onEditPrice = function (item) {
            $scope.editSelectedItemListPrice(item, $scope.selectedItemsList);
        };
        $scope.tableData = [];
        $scope.addSplitTableSelRow = function (data, model) {
            $scope.iteamIndex = $scope.itemIndexOf(model, data.itemName);
            if (!angular.isUndefined($scope.iteamIndex) && $scope.iteamIndex !== null && $scope.iteamIndex !== -1) {
                $scope.currentQty = model[$scope.iteamIndex].qty;
                model[$scope.iteamIndex].qty = $scope.currentQty + data.qty;
                $scope.editSelectedItemList(model[$scope.iteamIndex], model);
            } else {
                model.push(
                    data
                );
            }
        };

        $scope.addSelectedItemList = function (data, model) {
            $scope.taxString = "";
            var qty = 1;
            if ($scope.notificationFlag == true) {
                qty = data.qty;
            }
            // var unitPrice = angular.isUndefined(data.salesPrice) ? data.unitPrice : data.salesPrice;
            var unitPrice = angular.isUndefined(data.unitPrice) ? 0 :data.unitPrice;
            // $scope.taxIndex = $scope.taxIndexOf($scope.taxList, data.outputTaxId);
            var taxPercent = $scope.configurationData.taxId;
            var unitPrice1 = unitPrice;
            taxPercent = $scope.configurationData.taxId;
            if($scope.configurationData.taxcheckbox=='true'){
                taxPercent = $scope.configurationData.taxId;
                unitPrice1 = unitPrice;
                if ($scope.configurationData.taxcheckbox=='true') {
                    unitPrice = unitPrice1 / (1 + (Number(taxPercent[0]) * 0.01));
                }
            }
            var amtexclusivetax = (parseFloat(unitPrice) * parseFloat(qty));
            var totalTax = (parseFloat(taxPercent));
            var taxamt = (parseFloat(amtexclusivetax)) * ((parseFloat(totalTax) / 100));
            var amtinclusivetax = ((parseFloat(amtexclusivetax) + parseFloat(taxamt)));
            var i = $scope.selectedItemsList.length;
            model.push({
                id: i++,
                itemCode: data.itemCode,
                itemId: data.itemId,
                itemName: data.itemName,
                itemCategoryId: data.itemCategoryId,
                itemCategoryName: data.itemCategoryName,
                itemDescription: data.itemDesc,
                inputTaxId: data.inputTaxId,
                outputTaxId: data.outputTaxId,
                itemTypeId: data.itemTypeId,
                itemTypeName: data.itemTypeName,
                unitPrice: parseFloat(unitPrice.toFixed(2)),
                unitPriceIn: parseFloat(unitPrice1).toFixed(2),
                gstItemTax: parseFloat(taxamt).toFixed(2),
                taxamt: parseFloat(taxamt).toFixed(2),
                amtinclusivetax: parseFloat(amtinclusivetax).toFixed(2),
                qty: qty,
                discountAmt: data.discountAmt,
                discPercent: data.discountAmt,
                discountConfigAmt: 0,
                type: data.type,
                taxid: data.outputTaxId,
                amtexclusivetax: parseFloat(amtexclusivetax).toFixed(2),
                inclusiveJSON: data.inclusiveJSON,
                uom: data.unitOfMeasurementId,
                uomConvertorDTOList: data.uomConvertorDTOList,
                convertedQuantity: 0,
                uomValue: 0
            });
            var iteamIndex = $scope.itemIndexOf(model, data.itemName);
            var itm = $scope.selectedItemsList[iteamIndex];
            if (!$scope.discountType == 'itemWise' && $scope.notificationFlag == false) {
                angular.forEach($scope.selectedItemsList, function (item, index) {
                    $scope.configDiscount($scope.disAmtInPer + '%', item, index, "Config");
                });
            } else if ($scope.notificationFlag == true) {
                $scope.configDiscount(data.discountAmt, itm, 0, "Amt");
            } else {
                $scope.editSelectedItemList(itm, model);
            }
            $scope.updatePayable();
        };
        $scope.addSplitSelectedItemList = function (data, model) {
            var unitPrice = data.unitPrice;
            var qty = 1;
            var amtexclusivetax = (parseFloat(unitPrice) * parseFloat(qty));
            model.push({
                itemId: data.itemId,
                itemName: data.itemName,
                itemDescription: data.itemDescription,
                unitPrice: data.unitPrice,
                qty: 1,
                amtexclusivetax: parseFloat(amtexclusivetax).toFixed(2)
            });
        };
        $scope.editSelectedItemListPrice = function (data, model) {
            var qty = 0;
            var disQty = data.qty;
            $scope.taxString = "";
            if (data.type == "Percentage") {
                if (parseFloat(data.discPercent) > 100) {
                    data.discountAmt = 0;
                    data.discountConfigAmt = 0;
                    data.discPercent = 0;
                    Notification.error({
                        message: 'Discount Amount Should Be Less Than Amount(EX) ',
                        positionX: 'center',
                        delay: 2000
                    });
                } else {
                    data.discountAmt = parseFloat(((data.unitPrice * disQty) * data.discPercent) / 100).toFixed(2);
                    data.discountConfigAmt = parseFloat(data.discPercent).toFixed(2);
                }
            }
            else if (data.type == "Amount") {
                if (data.unitPrice >= data.discPercent) {
                    data.discountConfigAmt = parseFloat(data.discPercent).toFixed(2);
                    data.discountAmt = parseFloat(disQty * data.discPercent).toFixed(2);
                }
                else {
                    data.discountAmt = 0;
                    data.discountConfigAmt = 0;
                    data.discPercent = 0;
                    Notification.error({
                        message: 'Discount Amount Should Be Less Than Amount(EX) ',
                        positionX: 'center',
                        delay: 2000
                    });
                }
            }
            $scope.iteamIndex = $scope.itemIndexOf(model, data.itemName);
            if (data.unitPrice == undefined) {
                data.unitPrice = 0;
            }
            if (data.discountAmt == undefined) {
                data.discountAmt = 0;
            }
            var unitPrice = data.unitPrice;
            var unitPrice1 = unitPrice;
            // $scope.taxIndex = $scope.taxIndexOf($scope.taxList, data.taxid);
            var taxPercent = $scope.configurationData.taxId;
            taxPercent = $scope.configurationData.taxId;
            if($scope.configurationData.taxcheckbox=='true'){
                taxPercent = $scope.configurationData.taxId;
                if ($scope.configurationData.taxcheckbox=='true') {
                    unitPrice = unitPrice1 / (1 + (Number(taxPercent[0]) * 0.01));
                }
            }

            if (!data.convertedQuantity)
                qty = data.qty;
            else
                qty = data.convertedQuantity;
            unitPrice = parseFloat(unitPrice).toFixed(2);
            var amtexclusivetax = (parseFloat(unitPrice).toFixed(2) * parseFloat(qty) - parseFloat(data.discountAmt));
            var totalTax = (parseFloat(taxPercent));
            var taxamt = parseFloat(amtexclusivetax) * (parseFloat(totalTax) / 100);
            taxamt = parseFloat(taxamt).toFixed(2);
            var amtinclusivetax = parseFloat(amtexclusivetax) + parseFloat(taxamt);
            /**
             * for uom item, item is duplicated for every add
             * because we can serve difference sizes (30ml, 60ml,180ml)
             * @type {number}
             */

            data.unitPrice = parseFloat(unitPrice).toFixed(2);
            data.unitPriceIn = parseFloat(unitPrice1).toFixed(2);
            data.amtexclusivetax = parseFloat(amtexclusivetax).toFixed(2);
            data.qty = qty;
            data.gstItemTax = parseFloat(taxamt).toFixed(2);
            data.taxamt = parseFloat(taxamt).toFixed(2);
            data.amtinclusivetax = parseFloat(amtinclusivetax).toFixed(2);
            $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
        };
        $scope.editSelectedItemList = function (data, model) {
            var qty = 0;
            var disQty = data.qty;
            $scope.taxString = "";
            if (data.unitPrice == undefined) {
                data.unitPrice = 0;
            }
            if (data.type == "Percentage") {
                if (parseFloat(data.discPercent) > 100) {
                    data.discountAmt = 0;
                    data.discountConfigAmt = 0;
                    data.discPercent = 0;
                    Notification.error({
                        message: 'Discount Amount Should Be Less Than Amount(EX) ',
                        positionX: 'center',
                        delay: 2000
                    });
                } else {
                    data.discountAmt = parseFloat(((data.unitPrice * disQty) * data.discPercent) / 100).toFixed(2);
                    data.discountConfigAmt = parseFloat(data.discPercent).toFixed(2);
                }
            }
            else if (data.type == "Amount") {
                if (data.unitPrice >= data.discPercent) {
                    data.discountConfigAmt = parseFloat(data.discPercent).toFixed(2);
                    data.discountAmt = parseFloat(disQty * data.discPercent).toFixed(2);
                }
                else {
                    data.discountAmt = 0;
                    data.discountConfigAmt = 0;
                    data.discPercent = 0;
                    Notification.error({
                        message: 'Discount Amount Should Be Less Than Amount(EX) ',
                        positionX: 'center',
                        delay: 2000
                    });
                }
            }
            if (data.discountAmt == undefined) {
                data.discountAmt = 0;
            }
            $scope.iteamIndex = $scope.itemIndexOf(model, data.itemName);
            // $scope.taxIndex = $scope.taxIndexOf($scope.taxList, parseInt(data.taxid));
            var taxPercent = $scope.configurationData.taxId;
            var unitPrice = parseFloat(data.unitPrice).toFixed(2);
            var unitPrice1 = parseFloat(data.unitPriceIn).toFixed(2);
            taxPercent = $scope.configurationData.taxId;
            if($scope.configurationData.taxcheckbox=='true'){
                taxPercent = $scope.configurationData.taxId;
                if ($scope.configurationData.taxcheckbox=='true') {
                    unitPrice = unitPrice1 / (1 + (Number(taxPercent[0]) * 0.01));
                }
            }
            if (data.qty == undefined) {
                data.qty = 0;
            }

            if (!data.convertedQuantity)
                qty = data.qty;
            else
                qty = data.convertedQuantity;
            if ($scope.disAmtInPer === undefined) {
                $scope.disAmtInPer = 0;
            }
            var amtexclusivetax = (parseFloat(unitPrice).toFixed(2) * parseFloat(qty)) - parseFloat(data.discountAmt);
            var totalTax = (parseFloat(taxPercent));
            var taxamt = parseFloat(amtexclusivetax) * (parseFloat(totalTax) / 100);
            taxamt = parseFloat(taxamt).toFixed(2);
            var amtinclusivetax = parseFloat(amtexclusivetax) + parseFloat(taxamt);
            data.unitPrice = parseFloat(unitPrice).toFixed(2);
            data.amtexclusivetax = parseFloat(amtexclusivetax).toFixed(2);
            //show converted qty only while printing we can convert to ml
            data.qty = qty;
            data.gstItemTax = parseFloat(taxamt).toFixed(2);
            data.taxamt = parseFloat(taxamt).toFixed(2);
            data.amtinclusivetax = parseFloat(amtinclusivetax).toFixed(2);
            $scope.getTotalAmtForSelectedItems(model);
            $scope.updateKitchenOrder(data);
        };

        $scope.getTotalAmtToBeReturned = function (oldValue) {
            if ($scope.totalCPAmountTendered < 0) {
                $scope.totalCPAmountTendered = oldValue;
                return;
            }

            if ($scope.totalCPAmountTendered > $scope.totalCPAfterDiscount) {
                $scope.totalCPAmountRefunded = parseFloat($scope.totalCPAmountTendered) - parseFloat($scope.totalCPAfterDiscount);
                $scope.totalCPAmountRefunded = parseFloat($scope.totalCPAmountRefunded).toFixed(2);
            } else {
                $scope.totalCPAmountRefunded = 0.00;

            }
        };
        $scope.getTotalVoucherAmtToBeReturned = function (oldValue) {
            if ($scope.totalVPAmountTendered < 0) {
                $scope.totalVPAmountTendered = oldValue;
                return;
            }
            if ($scope.totalVPAmountTendered > $scope.totalVPAfterAllDeductions) {
                $scope.totalVPAmountRefunded = parseFloat($scope.totalVPAmountTendered) - parseFloat($scope.totalVPAfterAllDeductions);
            } else {
                $scope.totalVPAmountRefunded = 0.00;

            }
        };
        $scope.getTempData = function () {
            $http.get('/hipos/getTempData?currTableName=' + $scope.currTableName + "&currTableId=" + $scope.currTableId).then(function (response) {
                var data = response.data;
                $scope.selectedItemsList = [];
                if (!angular.isUndefined(data.selectedItemsList) && data.selectedItemsList !== null) {
                    $scope.previousOrdersConfirmed = angular.fromJson(data.selectedItemsList);
                    $scope.selectedItemsList = angular.fromJson(data.selectedItemsList);
                    angular.forEach($scope.selectedItemsList, function (value, key) {
                        value.savedOrder = true;
                        value.taxamt = value.amtinclusivetax - value.amtexclusivetax;
                        $scope.selectedItemsList[key].gstItemTax = value.taxamt;
                    });
                    $scope.tableSearchText = $scope.currTableName;
                    console.log($scope.selectedItemsList);
                    if(data.pax!=null) {
                        $scope.pax = data.pax;
                    }else {
                        $scope.pax = 1;
                    }
                    $scope.orderNo = data.orderNo;
                    $scope.customer = data.customerId;
                    if(data.customerName!=null) {
                        $scope.appendCustomerSelected(data.customerName, 'name');
                    }
                    $scope.tokenNumber = data.id;
                    if (data.useraccount_id === null || data.useraccount_id.trim() === '' || $scope.selectedItemsList.length == 0) {
                        $scope.makeEmployeeSearchTextReadonly = false;
                    } else {
                        $scope.makeEmployeeSearchTextReadonly = true;
                        $scope.waiterSearchText = data.useraccount_id;
                        $scope.agentSearchText = data.agentId;
                        $scope.waiterName = data.useraccount_id;
                    }
                } else {
                    $scope.makeEmployeeSearchTextReadonly = false;
                    $scope.tokenNumber = data.id;
                }
                $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);

            });
        };
        $scope.selectedItemListRemoval = {};
        $scope.removeSelectedItems = function () {
            if (angular.isUndefined($scope.selectedItemListRemoval)||$scope.selectedItemListRemoval == ""||$scope.selectedItemListRemoval==null|| $scope.selectedItemListRemoval.length <= 0) {
                Notification.error({message: 'Atleast One item has to be selected', positionX: 'center', delay: 2000});
            } else {
                $scope.removedItemList = [];
                $scope.removedItems = [];
                angular.forEach($scope.selectedItemListRemoval, function (val, key) {
                    if (val == true)
                        $scope.removedItems.push($scope.selectedItemsList[key])
                });
                angular.forEach($scope.removedItems,function (val,key) {
                    $scope.iteamIndex = $scope.itemIndexOf($scope.previousOrdersConfirmed, val.itemName);
                      if($scope.iteamIndex>-1)  {
                          $scope.userNameText = "";
                          $scope.pswdText = "";
                          $("#openUserCredentials").modal('show');
                    }
                    else {
                          $scope.removedItemList.push($scope.selectedItemsList.filter(function (data, index) {
                              return ($scope.selectedItemListRemoval[index] !== undefined && $scope.selectedItemListRemoval[index]
                                  && $scope.selectedItemsList[index].savedOrder !== undefined);
                          }));

                          //token number null mean fresh order
                          if ($scope.tokenNumber !== null) {
                              angular.forEach($scope.removedItems, function (value, key) {
                                  console.log("Removal of previously order items");
                                  console.log(value);
                                  //reset qty to zero
                                  value.qty = 0;
                                  //update kitchen order
                                  $scope.updateKitchenOrder(value)
                              });
                          } else {
                              //fresh order, so remove from kitchenOrders to..
                              var selectedItem = $scope.selectedItemsList.filter(function (data, index) {
                                  return ($scope.selectedItemListRemoval[index] !== undefined && $scope.selectedItemListRemoval[index])
                              });

                              angular.forEach(selectedItem, function (value, key) {
                                  var index = $scope.itemIndexOf($scope.orderToKitchenBuffer, value.itemName);
                                  $scope.orderToKitchenBuffer.splice(index, 1);
                              });
                              console.log($scope.orderToKitchenBuffer);
                          }

                          $scope.selectedItemsList = $scope.selectedItemsList.filter(function (data, index) {
                              return !($scope.selectedItemListRemoval[index] !== undefined && $scope.selectedItemListRemoval[index]);
                          });

                          angular.forEach($scope.selectedItemsList, function (value, key) {
                              $scope.selectedItemListRemoval[key] = false;
                          });
                          $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                          $scope.selectedItemListRemoval = {};
                      }
                });
                //get removed Items
                //Filter list for already placed orders

            }
        };
        $scope.totalVPDiscount = 0.00;
        $scope.totalvoucherAmt = 0.00;

        $scope.savelogin = function () {
            // $http.post($scope.restaurantServerURL + '/EncryptPassword?password=' + $scope.pswdText).then(function (response) {
            //     var data = response.data;
                if ($scope.userNameText == $scope.userLoginId && $scope.password == $scope.pswdText) {
                    // if($scope.descriptionType==undefined||$scope.descriptionType==null) {
                        $scope.descriptionType = "kot";
                        $scope.description = "";
                    // }
                    $("#description_popup_For_Cancel_Kot").modal('show');
                    $scope.phone=$rootScope.phone;
                    $scope.removedItemList.push($scope.selectedItemsList.filter(function (data, index) {
                        return ($scope.selectedItemListRemoval[index] !== undefined && $scope.selectedItemListRemoval[index]
                            && $scope.selectedItemsList[index].savedOrder !== undefined);
                    }));

                    //token number null mean fresh order
                    if ($scope.tokenNumber !== null) {
                        angular.forEach($scope.removedItems, function (value, key) {
                            console.log("Removal of previously order items");
                            console.log(value);
                            //reset qty to zero
                            value.qty = 0;
                            //update kitchen order
                            $scope.updateKitchenOrder(value)
                        });
                    } else {
                        //fresh order, so remove from kitchenOrders to..
                        var selectedItem = $scope.selectedItemsList.filter(function (data, index) {
                            return ($scope.selectedItemListRemoval[index] !== undefined && $scope.selectedItemListRemoval[index])
                        });

                        angular.forEach(selectedItem, function (value, key) {
                            var index = $scope.itemIndexOf($scope.orderToKitchenBuffer, value.itemName);
                            $scope.orderToKitchenBuffer.splice(index, 1);
                        });
                        console.log("( - )");
                        console.log($scope.orderToKitchenBuffer);
                    }

                    $scope.selectedItemsList = $scope.selectedItemsList.filter(function (data, index) {
                        return !($scope.selectedItemListRemoval[index] !== undefined && $scope.selectedItemListRemoval[index])
                    });

                    angular.forEach($scope.selectedItemsList, function (value, key) {
                        $scope.selectedItemListRemoval[key] = false;
                    });
                    $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                    $scope.selectedItemListRemoval = {};
                }else {
                    $scope.userNameText = "";
                    $scope.pswdText = "";
                    Notification.error({message:'Invalid Credentials',positionX:'center',delay:2000})
                    $("#openUserCredentials").modal('show');
                }
            // });
        };

        $scope.scissor = function () {
            var confirm = $window.confirm("Do you want to Clear all Items ?");
            if (confirm == false) {
                return false;
            }
            $scope.descriptionType="clearAllKot";
            angular.forEach($scope.selectedItemsList,function (val,key) {
                $scope.selectedItemListRemoval[val.id] = true;
            })
            $scope.removeSelectedItems();

            // //Iterate current ordered item reset qty to zero
            // angular.forEach($scope.selectedItemsList, function (value, key) {
            //     //Cancel Order
            //     value.qty = 0;
            //     //Prepare Cancel Order to KOT
            //     $scope.updateKitchenOrder(value);
            // });

        };

        $scope.cancelInvoice = function (data) {

            var confirm = $window.confirm("Do You Want To Cancel Invoice...?");
            $scope.params = {"id": data.id};
            if (confirm == true) {
                if($rootScope.phone!=null){
                    $scope.phone= $rootScope.phone;
                    $scope.openPopup();
                }
            } else {
                return false;
            }

        };

        $scope.openPopup = function () {
            $scope.descriptionType = "cancel";
            $scope.phone=$rootScope.phone;
            $("#PosInvoiceDuplicatePrint").modal('hide');
            $scope.description = "";
            $("#description_popup_For_Cancel_Kot").modal('show');
        };

        $scope.removeDescription= function(){
            $("#PosInvoiceDuplicatePrint").modal('show');
        }


        $scope.saveDescriptionForCancel = function () {
            if ($scope.description == "" || $scope.description == null || angular.isUndefined($scope.description)) {
                Notification.error({message: 'Please Mention Description', positionX: 'center', delay: 2000})
            }
            else {
                $http.post("/hipos" + '/cancelInvoice',
                    $scope.params).then(function (response) {
                    var data = response.data;
                    if (data == "Cancelled SuccessFully") {
                        $scope.getDuplicateInvoiceList();
                        $("#description_popup_For_Cancel_Kot").modal('hide');
                        Notification.info({message: 'Invoice Cancelled Successfully', positionX: 'center', delay: 2000})
                    }
                });
            };
        };

        $scope.populateSaveSplitSIData = function (paymentType) {
            $scope.BPDetails = [];
            $scope.CCPDetails = [];
            $scope.CPDetails = [];
            $scope.VPDetails = [];
            $scope.APDetails = [];
            $scope.BANK_PAYMENT_DETAILS = [];
            $scope.CARD_PAYMENT_DETAILS = [];
            $scope.CASH_PAYMENT_DETAILS = [];
            $scope.VOUCHER_PAYMENT_DETAILS = [];
            $scope.AIRPAY_PAYMENT_DETAILS = [];
            $scope.INVOICE_PAYMENT_DETAILS = [];
            angular.forEach($scope.paymentDropdown, function (value, key) {
                if (!angular.isUndefined($scope.paymentDropdown[key].DefaultPaymentmethodId)) {
                    var paymentTypeId = $scope.paymentDropdown[key].DefaultPaymentmethodId;
                }
                angular.forEach($scope.getPaymentTypes, function (value1, key1) {
                    if ($.trim(paymentTypeId) == value1.paymentmethodId) {
                        /****FOR PAYMENT-TYPE = "OTHERS" (BANK) ***************************************/
                        if (value1.paymentmethodType == 'Bank') {
                            var paymentType = value1.paymentmethodId;
                            $scope.BANK_PAYMENT_DETAILS.push({
                                'paymentType': paymentType,
                                'bankAccount': $scope.paymentDropdown[key].bankAccount,
                                'bankName': $scope.paymentDropdown[key].bankName,
                                'bankAccountId': $scope.paymentDropdown[key].bankAccountId,
                                'transactionNo': $scope.paymentDropdown[key].bankinvoiceNo,
                                'amount': $scope.paymentDropdown[key].bankAmount,
                                'date': $scope.paymentDropdown[key].bankDate
                            });
                        }
                        if (value1.paymentmethodType == 'Card') {
                            /****FOR PAYMENT-TYPE = "CARD" *********************************************/
                            var paymentType = value1.paymentmethodId;
                            $scope.CARD_PAYMENT_DETAILS.push({
                                'paymentType': paymentType,
                                'cardNo': $scope.paymentDropdown[key].cardTransactionNo,
                                'cardAmt': $scope.paymentDropdown[key].cardAmount,
                                'cardBankName': $scope.paymentDropdown[key].cardBankName,
                                'cardBankAccount': $scope.paymentDropdown[key].cardBankAccount,
                                'cardDate': $scope.paymentDropdown[key].cardDate
                            });
                        }
                        if (value1.paymentmethodType == 'Cash') {
                            /****FOR PAYMENT-TYPE = "CARD" *********************************************/
                            var paymentType = value1.paymentmethodId;
                            $scope.CASH_PAYMENT_DETAILS.push({
                                'paymentType': paymentType,
                                'cashAmt': $scope.paymentDropdown[key].totalCPAmountTendered
                            });
                        }
                        if (value1.paymentmethodType == 'Voucher') {
                            /****FOR PAYMENT-TYPE = "CARD" *********************************************/
                            var paymentType = value1.paymentmethodId;
                            $scope.VOUCHER_PAYMENT_DETAILS.push({
                                'paymentType': paymentType,
                                'voucherNo': $scope.paymentDropdown[key].voucherNo,
                                'voucherAmt': $scope.paymentDropdown[key].voucherAmt
                            });
                        }
                        if (value1.paymentmethodType == 'airPay') {
                            /****FOR PAYMENT-TYPE = "AIR PAY" *********************************************/
                            var paymentType = value1.paymentmethodId;
                            $scope.AIRPAY_PAYMENT_DETAILS.push({
                                'paymentType': paymentType,
                                'airPayAmt': $scope.paymentDropdown[key].totalAPAmountTendered,
                                'airPaymobile': $scope.paymentDropdown[key].airPaymobile
                            });
                        }
                        if (value1.paymentmethodType == 'invoicePay') {
                            /****FOR PAYMENT-TYPE = "INVOICE PAY" *********************************************/
                            var paymentType = value1.paymentmethodId;
                            $scope.INVOICE_PAYMENT_DETAILS.push({
                                'paymentType': paymentType,
                                'invoicePayAmt': $scope.paymentDropdown[key].totalINVAmountTendered,
                                'invmobile': $scope.paymentDropdown[key].invmobile,
                                'invmail': $scope.paymentDropdown[key].invmail
                            });
                        }


                    }
                });
            });
            $scope.BPDetails = {
                multiBankPaymentList: $scope.BANK_PAYMENT_DETAILS
            };
            $scope.CCPDetails = {
                cardPaymentList: $scope.CARD_PAYMENT_DETAILS
            };
            $scope.CPDetails = {
                multiCashPaymentList: $scope.CASH_PAYMENT_DETAILS
            };
            $scope.VPDetails = {
                multiVoucherPayments: $scope.VOUCHER_PAYMENT_DETAILS
            };
            $scope.APDetails = {
                airPayPaymentList: $scope.AIRPAY_PAYMENT_DETAILS
            };
            $scope.roundingOffValue = $scope.totalCheckOutamt - Math.round($scope.totalCheckOutamt);
            var totalCheckOutamt = Math.round($scope.totalCheckOutamt);
            if (angular.isUndefined($scope.roundingOffValue)) {
                $scope.roundingOffValue = 0.0;
            }
            var discPercentage=0;
            if($scope.disAmtInPer==undefined){
                $scope.disAmtInPer=0;
            }
            var disc= $scope.disAmtInPer.toString().slice(-1);
            if (disc == "%") {
                $scope.discType="Percentage";
                discPercentage = parseFloat($scope.disAmtInPer.toString().slice(0, -1));
            }else {
                $scope.discType="Amount";
                discPercentage = parseFloat(0);
            }
            var data = {
                selectedItemsList: $scope.splitTable2List,
                cashPayment: $scope.CPDetails,
                airPayments: $scope.APDetails,
                isAirPayPayment: $scope.AIRPAYPAYMENT,
                invoicePaymentList: $scope.INVOICE_PAYMENT_DETAILS,
                creditPayment: $scope.CCPDetails,
                bankPayment: $scope.BPDetails,
                orderType: $scope.invoiceType,
                voucherPayment: $scope.VPDetails,
                cmpyLocation: $scope.fromLocation,
                custLocation: $scope.toLocation,
                cutomerName: $scope.customerSearchText,
                totalRemaininBalance: $scope.totalVPAmountRefunded,
                hiPosServiceCharge: $scope.hiposServiceCharge,
                hiposServiceChargeAmt: $scope.hiposServiceChargeAmt,
                totalCheckOutamt: totalCheckOutamt,
                paymentType: paymentType,
                orderNo:$scope.orderNo,
                discountCode: $scope.couponNameText,
                totalTaxAmt: parseFloat($scope.totalTaxAmt).toFixed(2),
                totalTenderedAmount: parseFloat($scope.totalPaidAmt).toFixed(2),
                customerId: $scope.customerId,
                taxType: $scope.fullSimplTax,
                discountAmtInPercentage: discPercentage,
                roundingOffValue: parseFloat($scope.roundingOffValue).toFixed(2),
                employeeName: $scope.employeeSearchText,
                siNo: $scope.siNo,
                discType: $scope.discType,
                olInvPayId:$scope.onlineInvoicePay,
                airPayId:$scope.swipePay,
                returnReason: JSON.stringify($scope.removedItemList)
            };
            console.log(data);
            return data;
        };

        $scope.populatePrintData = function (data, type) {
            var style = "body {\n" +
                "    color: #000;\n" +
                "    background: #fff;\n" +
                "    font-family: Arial;\n" +
                "    font-size: 7px !importany;\n" +
                "    font-weight: 400;\n" +
                " }\n" +
                "\n" +
                ".resipt_wrap table td, .resipt_wrap table th {\n" +
                "    padding-bottom: 5px;\n" +
                "}\n" +
                "\n" +
                ".resipt_wrap table th {\n" +
                "    color: rgb(0, 0, 0);\n" +
                "}\n" +
                "\n" +
                ".res_head_info tr td {\n" +
                "    text-align: center;\n" +
                "    font-size: 7px;\n" +
                "}\n" +
                "\n" +
                ".res_head_info tr td h2 {\n" +
                "    margin: 0px;\n" +
                "    font-size: 13px;\n" +
                "}\n" +
                "\n" +
                "#printablearea_lessi1 tr {\n" +
                "    display: table-row;\n" +
                "}\n" +
                "\n" +
                "#printablearea_lessi1 td, #printablearea_lessi1 th {\n" +
                "    display: table-cell;\n" +
                "}\n" +
                "\n" +
                "#printablearea_lessi1 tbody, #printablearea_lessi1 thead {\n" +
                "    display: table-row-group;\n" +
                "}\n" +
                "\n" +
                ".res_item_table th {\n" +
                "    padding: 5px 0px;\n" +
                "    text-align: center;\n" +
                "}\n" +
                "\n" +
                ".res_item_table td {\n" +
                "    padding: 5px 0px;\n" +
                "    text-align: center;\n" +
                "}\n" +
                "\n" +
                "td, th {\n" +
                "    font-family:  \"Arial Narrow\", Arial, sans-serif;\n" +
                "}\n" +
                "\n";
            $scope.printData = data;
            var sandWichPrint = false;
            var loopItemsList = data.siData.selectedItemsList;
            angular.forEach(loopItemsList, function (value, key) {
                if (value.itemCategoryName.toLowerCase() === 'sandwich') {
                    sandWichPrint = true;
                }
            });
            if (type == 'savePrint' && $scope.printPreview) {
                $("#printSummaryBill").modal('show');
            }
            $scope.tableSearchText = $scope.tableSearchText;

            $timeout(function () {
                if ($scope.splitTable1List.length > 0) {
                    $scope.splitTable1List = $scope.splitTable1List;
                    $scope.getTotalAmtForSelectedItems($scope.splitTable1List);
                    $("#splitTablePopUp").modal('show');
                }

            }, 5000);
            $timeout(function () {
                if ($scope.mergeTable1List.length > 0) {
                    $scope.getTotalAmtForSelectedItems($scope.mergeTable1List);
                    $("#mergeTablePopUp").modal('show');
                }

            }, 3000);
        };
        $scope.printDiv = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var printer;
            printer = $scope.configurationData.reportPrinter;
            var postRequest = {
                url: '/hipos/1/printHTML',
                data: '<html><head></head><body>' + printContents + '</body></html>',
                method: 'POST',
                params: {printType: printer}
            };
            var popupWin = window.open('', '_blank', 'width=300,height=300');
            popupWin.document.open();
            popupWin.document.write('<html><head></head><body onload="window.print()">' + printContents + '</body></html>');
            popupWin.document.close();
        };
        $scope.printDiv4 = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            $("#print_dailyReport").modal('hide');
            //Logic for Browser Print
            var popupWin = window.open('', '_blank', 'width=300,height=300');
            popupWin.document.open();
            popupWin.document.write('<html><head></head><body onload="window.print()">' + printContents + '</body></html>');
            popupWin.document.close();
        };

        $scope.duplicateRSIPrint = function (formNo) {
            $scope.notHide = "posListClick";
            $http.get('/hipos' + '/getDuplicateSIPrint?invoiceNo=' + formNo).then(function (response, status, headers, config) {
                var data = response.data;
                $scope.TotalAmtAfterDiscount = 0;
                angular.forEach(data.siData.selectedItemsList, function (value, key) {
                    data.siData.selectedItemsList[key].cessTaxAmt = ((((value.unitPrice) * (value.qty)) - (value.discountAmt)) * (value.cess)) / 100;
                    if (data.siData.taxType == 'CGST:SGST/UGST') {
                        data.siData.selectedItemsList[key].cgstsgstsplitvalues = (value.taxamt) / 2;
                        data.siData.selectedItemsList[key].taxPercentageSplit = value.taxpercent / 2;
                        data.siData.selectedItemsList[key].rateTaxPercentage = value.taxpercent;
                        data.siData.selectedItemsList[key].taxpercent = 0;
                        data.siData.selectedItemsList[key].taxamt = 0;
                        data.siData.selectedItemsList[key].uomValue = 0;
                    }
                    else {
                        data.siData.selectedItemsList[key].rateTaxPercentage = value.taxpercent;
                    }
                    $scope.TotalAmtAfterDiscount = (value.unitPrice*value.qty) + $scope.TotalAmtAfterDiscount;
                });
                $scope.numberToWord = toWords(data.siData.totalCheckOutamt);
                $scope.printData = data;
                var $log = $("#logs");
                str = data.footer;
                html = $.parseHTML(str);
                // Append the parsed HTML
                $log.append(html);
                $("#PosInvoiceDuplicatePrint").modal('hide');
                if (data.printType == "normal") {
                    $("#PosInvoiceDuplicatePrint").modal('hide');
                } else {
                    $scope.voucherPayementList = $scope.printData.siData.voucherPayment.multiVoucherPayments;
                    $scope.cardPayementList = $scope.printData.siData.creditPayment.cardPaymentList;
                    $scope.cashPayment = $scope.printData.siData.cashPayment.totalCPAmountTendered;
                    $scope.taxSummaryList = $scope.printData.siData.taxSummaryList;
                }
                console.log(data.siData);
                $("#printSummaryBill").modal('show');
            }, function (error) {
                Notification.error({message: "Something went wrong in server", positionX: 'center', delay: 2000});
            })
        };
        $scope.itemIndexOf = function (array, searchVal) {
            var itemIndex = -1;
            if ($scope.isUndefinedOrNull(searchVal)) {
                itemIndex = -1;
            } else {
                var foundIndex = $filter('filter')(array, {
                    itemName: searchVal
                }, true)[0];
                itemIndex = array.indexOf(foundIndex);
            }
            return itemIndex;
        };
        $scope.taxIndexOf = function (array, searchVal) {
            var taxIndex = -1;
            if ($scope.isUndefinedOrNull(searchVal)) {
                taxIndex = -1;
            } else {
                var foundIndex = $filter('filter')(array, {
                    taxid: searchVal
                }, true)[0];
                taxIndex = array.indexOf(foundIndex);
            }
            return taxIndex;
        };
        $scope.negara = function (number) {
            var k = number;
            var rm = number.toString().split(".")[1];
            var a = rm % 10;
            var j = parseFloat(number);
            if (a == 1 || a == 6) {
                k = (j - parseFloat(0.01)).toFixed(2);
            }
            if (a == 2 || a == 7) {
                k = (j - parseFloat(0.02)).toFixed(2);
            }
            if (a == 3 || a == 8) {
                k = (j + parseFloat(0.02)).toFixed(2);
            }
            if (a == 4 || a == 9) {
                k = (j + parseFloat(0.01)).toFixed(2);
            }
            return parseFloat(k);
        };

        $scope.totalCCPDiscount = 0.0;
        $scope.isValidatedData = function () {
            $scope.isValide = true;
            angular.forEach($scope.selectedItemsList, function (item, index) {
                if (angular.isUndefined(item.unitPrice) || item.unitPrice === '' || parseFloat(item.unitPrice) < 0) {
                    $scope.isValide = false;
                } else if (angular.isUndefined(item.qty) || item.qty === '' || parseFloat(item.qty) <= 0) {
                    $scope.isValide = false;
                } else if (angular.isUndefined(item.itemName) || item.itemName === '') {
                    $scope.isValide = false;
                }
            });
            return $scope.isValide;
        };
        $scope.setDiscount = function () {
            if ($scope.freeMeal == true) {
                $scope.freeMealDisc = 100+"%";
                $scope.disAmtInPer = 0;
            } else {
                $scope.freeMealDisc = 0;
                $scope.disAmtInPer = 0;
            }
            $scope.coupon="";
            $scope.couponNameText="";
            $scope.updatePayable();
            angular.forEach($scope.selectedItemsList, function (item, index) {
                $scope.configDiscount($scope.freeMealDisc,item,index,"Config");
            });
        };
        $scope.toFixedTwo = function (number) {
            var k = number;
            if (angular.isUndefined(number) || !angular.isNumber(parseFloat(number))) {
                k = parseFloat(0.00).toFixed(2);
            } else {
                k = parseFloat(number).toFixed(2);
            }
            if (!angular.isUndefined($scope.onFocusElement)) {
                $scope.onFocusElement.value = k;
                angular.element($scope.onFocusElement).trigger('input');
            }
        };


        $scope.next_wizard = function () {
            $("#addCustomer  #cus_step1").removeClass("in active");
            $("#addCustomer  #cus_step2").addClass("tab-pane fade in active");
        };

        $scope.back_wizard = function () {
            $("#addCustomer #cus_step2").removeClass("in active");
            $("#addCustomer #cus_step1").addClass("tab-pane fade in active");
        };

        $scope.getDuplicateInvoiceList = function (val, type) {
            $(".loader").css("display", "block");
            if (angular.isUndefined(val)) {
                val = "";
            }
            if (angular.isUndefined($scope.inventoryLocation) || $scope.inventoryLocation == null) {
                $scope.inventoryLocation = "";
            }
            $http.get('/hipos' + '/getPosInvoices?itemSearchText=' + val + '&type=' + type + '&locationId=' + $scope.inventoryLocation).then(function (response) {
                var data = response.data;
                $scope.posSalesList = data;
                $("#PosInvoiceDuplicatePrint").modal('show');
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        };


        $scope.getplaceorder = function () {
            $("#place_order").modal('show');
        };



        $scope.assign = function (type, token) {
            $scope.type = "Delivery";
            $scope.token = token;
            $scope.orderid = token.orderNo;
            $("#add_new_picked_modal").modal('show');
        };
        $scope.pickup = function (type, token) {
            $scope.type = "pickup";
            $scope.token = token;
            $scope.orderid = token.orderNo;
            $("#add_new_picked_modal").modal('show');
        };
        $scope.saveasiandelivery = function (token) {
            var details;
            details = {
                custNotiId: token.customerId,
                orderId: token.orderNo,
                riderName: $scope.ridername,
                riderPhoneNo: $scope.phonenumber
            };
            $http.post("/restaurant/saveasiandelivery", angular.toJson(details)).then(function (response) {
                var data = response.data;
                if (data == "") {
                    Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                }
                else {
                    $("#add_new_picked_modal").modal('hide');
                    Notification.success({
                        message: 'Confirmed',
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

        $scope.savepickedup = function (token) {
            var details;
            details = {
                orderId: token.orderNo,
                riderName: $scope.ridername,
                riderPhoneNo: $scope.phonenumber
            };
            $http.post("/restaurant/savepickedup", angular.toJson(details)).then(function (response) {
                var data = response.data;
                if (data == "") {
                    Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                }
                else {
                    $("#add_new_picked_modal").modal('hide');
                    Notification.success({
                        message: 'Picked Up SuccessFully',
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

        $(".restaurant_body").css("min-height", $("#body_info").height() - 60);
        $scope.showcurrencySidePopup = false;

        $scope.showcurrencySidePopupModal = function (val, type, index) {
            $scope.showcurrencySidePopup = val;
            $scope.type = type;
            $scope.index = index;
        };
        $scope.closecurrencySidePopup = function () {
            $scope.showcurrencySidePopup = false;
        };

        $scope.currency_add = function (element, type) {

            if (type == "Cash") {
                var cur1 = $scope.paymentDropdown[$scope.index].totalCPAmountTendered || 0;
                $('#cashAmt1').prop('disabled', false);
                var cur = parseInt(element.currentTarget.innerHTML); // this will return the value of the button
                cur1 = cur1 + cur;
                $scope.paymentDropdown[$scope.index].totalCPAmountTendered = cur1;
                $('#cashAmt1').focus();
                $('#cashAmt1').keypress();
                $scope.OnChangeMultiPayament($scope.totalVPAfterDiscount, cur1, 'CASH', $scope.index);
                console.log(cur);
            }
            else if (type == "Card") {
                var cur1 = $scope.paymentDropdown[$scope.index].cardAmount || 0;
                $('#cardAmt1').prop('disabled', false);
                var cur = parseInt(element.currentTarget.innerHTML); // this will return the value of the button
                cur1 = cur1 + cur;
                $scope.paymentDropdown[$scope.index].cardAmount = cur1;
                $('#cardAmt1').focus();
                $('#cardAmt1').keypress();
                $scope.OnChangeMultiPayament($scope.totalVPAfterDiscount, cur1, 'CARD', $scope.index);
                console.log(cur);
            }
            else if (type == "Voucher") {
                var cur1 = $scope.paymentDropdown[$scope.index].voucherAmt || 0;
                $('#voucherAmt1').prop('disabled', false);
                var cur = parseInt(element.currentTarget.innerHTML); // this will return the value of the button
                cur1 = cur1 + cur;
                $scope.paymentDropdown[$scope.index].voucherAmt = cur1;
                $('#voucherAmt1').focus();
                $('#voucherAmt1').keypress();
                $scope.OnChangeMultiPayament($scope.totalVPAfterDiscount, cur1, 'VOUCHER', $scope.index);
                console.log(cur);
            }
            else if (type == 'Bank') {
                var cur1 = $scope.paymentDropdown[$scope.index].bankAmount || 0;
                $('#bankAmt1').prop('disabled', false);
                var cur = parseInt(element.currentTarget.innerHTML); // this will return the value of the button
                cur1 = cur1 + cur;
                $scope.paymentDropdown[$scope.index].bankAmount = cur1;
                $('#bankAmt1').focus();
                $('#bankAmt1').keypress();
                $scope.OnChangeMultiPayament($scope.totalVPAfterDiscount, cur1, 'BANK', $scope.index);
                console.log(cur);
            }
        };

        $scope.currency_clear = function (type) {

            var cur1 = 0;
            if (type == "Cash") {
                $scope.paymentDropdown[$scope.index].totalCPAmountTendered = cur1;
                $scope.OnChangeMultiPayament($scope.totalVPAfterDiscount, cur1, 'CASH', 0);
            }
            else if (type == "Card") {
                $scope.paymentDropdown[$scope.index].cardAmount = cur1;
                $scope.OnChangeMultiPayament($scope.totalVPAfterDiscount, cur1, 'CARD', 0);
            }
            else if (type == "Voucher") {
                $scope.paymentDropdown[$scope.index].voucherAmt = cur1;
                $scope.OnChangeMultiPayament($scope.totalVPAfterDiscount, cur1, 'VOUCHER', 0);
            }
            else if (type == "Bank") {
                $scope.paymentDropdown[$scope.index].bankAmount = cur1;
                $scope.OnChangeMultiPayament($scope.totalVPAfterDiscount, cur1, 'BANK', 0);
            }
        };
        $scope.getCompanyInfo = function () {
            var getRequest = {
                url: '/hipos/getCompanyList',
                method: 'POST',
                params: {}
            };
            $http(getRequest).then(function (successResponse) {
                $scope.printData4 = successResponse.data;
                $scope.printData4.date = new Date();
                console.log($scope.printData4);
            }, function (failureResponse) {
            });
        };

        $scope.getCompanyInfo();
        $scope.getDailyReport = function (val) {
            if (val === true) {
                var confirm = $window.confirm("Do you want to close accounts for the day");
                if (confirm == false) {
                    return false;
                }
            }
            var getRequest = {
                url: '/hipos/getReportforPeriod',
                method: 'GET',
                params: {'type': 'totalWise', 'dayEndStatus': val,'locationId':$rootScope.selectedLocation}
            };
            $http(getRequest).then(function (successResponse) {
                console.log(successResponse.data);
                $scope.dailyReportList = successResponse.data;
                console.log(successResponse.data);
                $scope.total = 0;
                $('#print_dailyReport').modal('show');
            }, function (failureResponse) {
            });
        };
        $scope.getDailyReportUserWise = function () {
            var getRequest = {
                url: '/hipos/getReportforPeriod',
                method: 'GET',
                params: {'type': 'userWise', 'dayEndStatus': false,'locationId':$rootScope.selectedLocation}
            };
            $http(getRequest).then(function (successResponse) {
                console.log(successResponse.data);
                $scope.dailyReportList = successResponse.data;
                console.log(successResponse.data);
                $scope.total = 0;
                $('#print_dailyReport').modal('show');
            }, function (failureResponse) {
            });
        };

        $scope.updateTotals = function (categoryTotal) {
            if (categoryTotal !== undefined) {
                $scope.total += parseFloat(categoryTotal);
            }
        };

        if(angular.isUndefined($scope.duplicatePrint)) {
            $scope.duplicatePrint = false;
        }
        $scope.placeOrdersTOKOT = function (payment, type) {
            if ($scope.customerId == "" || $scope.customerId == null) {
                var saveCustomerDetails;
                // $http.get("/hipos"  + '/addCustomer').then(function (response) {
                //     var data = response.data;
                //     $scope.stateDTOList = angular.copy(data.stateDTOList);
                //     $scope.countryDTOList = data.countryDTOList;
                //     $scope.currencyDTOList = data.currencyDTOList;
                //     $scope.selectedCountry = parseInt(data.cmpyCountry);
                //     $scope.selectedCurrency = parseInt(data.cmpyCurrency);
                //     $scope.selectedState = parseInt(data.cmpyState);
                //     $scope.customerAccountMasterDTOList = angular.copy(data);
                //     $scope.GSTINText = "";
                //     $scope.custStatusText = "Active";
                    saveCustomerDetails = {
                        customerName: $scope.customerSearchText,
                        customerEmail: $scope.customerEmailText,
                        customerNumber: $scope.customerContactText,
                        customerAddress: $scope.customerAddressText,
                        pincode: $scope.pincode,
                        companyRegNo: $scope.companyRegNo,
                        notificationFlag: $scope.notificationFlag,
                        from_Reg_Comp: $scope.fromRegNo,
                        to_Reg_Comp: $scope.toRegNo,
                        notificationId: $scope.notificationId,
                        gstIn: $scope.GSTINText,
                        state: $scope.selectedState,
                        personIncharge: $scope.personInchargeText,
                        country: $scope.selectedCountry,
                        currency: $scope.selectedCurrency,
                        custStatus: "Active",
                        bankName: $scope.bankNameText,
                        accountNo: $scope.accNoText,
                        branchName: $scope.bankBranchText,
                        iFSCCode: $scope.IFSCText,
                        uIN: $scope.UINText,
                        website: $scope.websiteText,
                        panNumber: $scope.panNumberText,
                        terms: $scope.creditTermText,
                        creditedLimit: $scope.creditLimitText
                    };
                    $http.post($scope.hiposServerURL +  '/saveCustomer', angular.toJson(saveCustomerDetails, "Create"))
                        .then(function (response) {
                            var data = response.data;
                            $scope.customerId = data.customerId;
                            $scope.placeOrders(payment, type);
                        });
                // });
            } else {
                $scope.placeOrders(payment, type);
            }
        };
        $scope.pax = 1;
        $scope.placeOrders = function (payment, type) {
            if ($scope.orderToKitchenBuffer !== undefined && $scope.orderToKitchenBuffer.length === 0) {
                if (payment == 'multiPayment') {
                    $scope.saveFullSI(payment, type);
                }
                return;
            }
            if ($scope.orderToKitchenBuffer.length > 0) {
                $scope.promptKOT = true;
                // if ($scope.configurationData.promptKOT && $scope.orderToKitchenBuffer !== undefined && $scope.orderToKitchenBuffer.length > 0) {
                //     $scope.promptKOT = $window.confirm("Do you want print KOT?");
                // }
                $scope.previousOrdersConfirmed = $scope.selectedItemsList;
                if ($scope.notificationFlag == true) {
                    $scope.waiterName = "";
                    $scope.pax = 0;
                }
                if($scope.notificationFlag==true){
                    $scope.placeOrdersTOKOTNotification($scope.selectedItemsList, $scope.currTableName, $scope.currTableId, false, $scope.customerId, $scope.orderNo);
                }else {
                    $scope.saveNGetNextTableDetails($scope.selectedItemsList, $scope.currTableName, $scope.currTableId, false, $scope.customerId, $scope.orderNo);
                }
                $scope.notificationFlag =false;
                if($scope.description==undefined){
                    $scope.description="";
                }
                if($scope.phone==undefined){
                    $scope.phone="";
                }
                var postRequest = {
                    url: '/hipos/placeOrderToKOT',
                    method: 'POST',
                    data: $scope.orderToKitchenBuffer,
                    params: {
                        tableName: $scope.currTableName,
                        waiterName: $scope.waiterName,
                        customerName: $scope.customerSearchText,
                        instructions: $scope.instructions,
                        kotRequired: $scope.promptKOT,
                        tableId: $scope.tableId,
                        pax: $scope.pax,
                        duplicatePrint: $scope.duplicatePrint,
                        description:$scope.description,
                        phone:$scope.phone
                    }
                };
                $scope.duplicatePrint = false;
                $timeout(function () {
                    $http(postRequest).then(function (successResponse) {
                        console.log('response....');
                        console.log('Sending Data to Cloud Printer..');
                        console.log(successResponse.data);
                        if (payment == 'multiPayment') {
                            $scope.saveFullSI(payment, type);
                        }
                        if ($scope.configurationData.buildTypeCloud) {
                            var postRequest = {
                                url: 'http://localhost:9001/hiAccounts/sendDatatoLocalPrinter',
                                data: successResponse.data,
                                method: 'POST',
                                params: {}
                            };
                            $http(postRequest);
                        }
                    }, function (failureResponse) {
                        console.log(failureResponse)
                    });
                }, 300);
                $timeout(function () {
                    Notification.warning({
                        message: 'Order(s) placed successfully',
                        positionX: 'center',
                        delay: 2000
                    });

                    $scope.orderToKitchenBuffer = [];
                    $scope.previousOrdersConfirmed = [];
                    if ($scope.currTableName == "TAKEAWAY" || $scope.currTableName == "Delivery") {
                        $scope.currTableName = $scope.tableSearchText;
                    } else {
                        $scope.currTableName = "";
                    }
                    $scope.getSelectedTableValues($scope.currTableName);
                    // $scope.totalCheckOutamt = 0;
                    if (payment != 'payment') {
                        $scope.orderType($scope.orderTypeFlag);
                    }
                }, 1000);
                if (payment == 'saveInvoice') {
                    $scope.orderNo = "";
                    $scope.notificationFlag = false;
                }
            }
        }

        $scope.updateKitchenOrder = function (data) {
            //holds the reference of previous confirmed orders
            var previousOrdersConfirmedRef = $filter('filter')($scope.previousOrdersConfirmed, {itemName: data.itemName}, true)[0];
            if(previousOrdersConfirmedRef!=undefined&&previousOrdersConfirmedRef.qty == data.qty){
                return;
            }
            //holds the reference of current order
            var orderToKitchenBufferRef = $filter('filter')($scope.orderToKitchenBuffer, {itemName: data.itemName}, true)[0];
            //item not in Kitchen order
            if (orderToKitchenBufferRef === undefined) {
                orderToKitchenBufferRef = {
                    tableName: $scope.tableSearchText,
                    itemName: data.itemName,
                    itemDescription: data.itemDescription,
                    itemQty: data.qty,
                    type: 'Order',
                    itemCategoryName: data.itemCategoryName
                };
                //previous order confirmed but Qty reduced(updated)
                if (previousOrdersConfirmedRef !== undefined && previousOrdersConfirmedRef.itemName === data.itemName && previousOrdersConfirmedRef.qty > parseFloat(data.qty)) {
                    //Cancel order
                    orderToKitchenBufferRef.type = 'Cancel';
                    orderToKitchenBufferRef.itemQty = previousOrdersConfirmedRef.qty - data.qty;
                    orderToKitchenBufferRef.itemDescription = data.itemDescription;
                }//previous order confirmed but Qty increased(updated)
                else if (previousOrdersConfirmedRef !== undefined && previousOrdersConfirmedRef.itemName === data.itemName && previousOrdersConfirmedRef.qty < parseFloat(data.qty)) {
                    //Update Order
                    orderToKitchenBufferRef.itemQty = data.qty - previousOrdersConfirmedRef.qty;
                    orderToKitchenBufferRef.type = 'Order';
                    orderToKitchenBufferRef.itemDescription = data.itemDescription;
                }
                $scope.orderToKitchenBuffer.push(orderToKitchenBufferRef);
                if (previousOrdersConfirmedRef !== undefined && previousOrdersConfirmedRef.itemName === data.itemName && previousOrdersConfirmedRef.qty == parseFloat(data.qty)) {
                    var removalIndex = $scope.orderToKitchenBuffer.map(function (d) {
                        return d['itemName'];
                    }).indexOf(data.itemName);
                    $scope.orderToKitchenBuffer.splice(removalIndex, 1);
                }

                //Finally update order to kitchen

            } else {//Already exists in kitchen order
                //Fresh Order
                if (previousOrdersConfirmedRef === undefined) {
                    orderToKitchenBufferRef.itemDescription = data.itemDescription;
                    //Update to Current Qty
                    if (data.qty == 0) {
                        var removalIndex = $scope.orderToKitchenBuffer.map(function (d) {
                            return d['itemName'];
                        }).indexOf(data.itemName);
                        $scope.orderToKitchenBuffer.splice(removalIndex, 1);
                    } else {
                        orderToKitchenBufferRef.itemQty = parseFloat(data.qty).toFixed(2);
                    }
                } else if (previousOrdersConfirmedRef.itemName === data.itemName && previousOrdersConfirmedRef.qty > parseFloat(data.qty)) {
                    //Cancel order
                    orderToKitchenBufferRef.type = 'Cancel';
                    orderToKitchenBufferRef.itemDescription = data.itemDescription;
                    orderToKitchenBufferRef.itemQty = previousOrdersConfirmedRef.qty - data.qty;
                } else if (previousOrdersConfirmedRef.itemName === data.itemName && previousOrdersConfirmedRef.qty < parseFloat(data.qty)) {
                    //Update Order
                    orderToKitchenBufferRef.itemQty = data.qty - previousOrdersConfirmedRef.qty;
                    orderToKitchenBufferRef.type = 'Order';
                    orderToKitchenBufferRef.itemDescription = data.itemDescription;
                } else {
                    //remove from current order list
                    orderToKitchenBufferRef.itemDescription = data.itemDescription;
                    var removalIndex = $scope.orderToKitchenBuffer.map(function (d) {
                        return d['itemName'];
                    }).indexOf(data.itemName);
                    $scope.orderToKitchenBuffer.splice(removalIndex, 1);
                }
            }
            if (orderToKitchenBufferRef.itemQty == 0 && orderToKitchenBufferRef.type == 'Order') {
                if (!$scope.isValidatedData()) {
                    Notification.error({
                        message: 'Qty should not be Zero',
                        positionX: 'center',
                        delay: 2000
                    });
                    data.qty = 1;
                    $scope.editSelectedItemList(data, $scope.selectedItemsList);
                }
            }
            if ($scope.notificationItemsLength == ($scope.orderToKitchenBuffer.length+$scope.previousOrdersConfirmed.length) && $scope.notificationFlag == true) {
                if($scope.disctAmt>0){
                    $scope.notificationItemsLength=0;
                    $scope.notiTotal=parseFloat(0);
                    angular.forEach($scope.selectedItemsList,function (val,key) {
                        $scope.notiTotal=parseFloat($scope.notiTotal)+parseFloat(val.amtexclusivetax);
                    })
                    $scope.disAmtInPer=($scope.disctAmt*100)/ $scope.notiTotal;
                    $scope.disAmtInPer=parseFloat( $scope.disAmtInPer).toFixed(2);
                    $scope.updatePayable();
                    $timeout(function () {
                        $scope.placeOrdersTOKOT();
                        $scope.getConfirmNotifications();
                        $rootScope.sharedNotificationData.flag=null;
                    }, 3000);
                }else {
                    $scope.placeOrdersTOKOT();
                    $scope.getConfirmNotifications();
                    $rootScope.sharedNotificationData.flag=null;
                    $scope.notificationItemsLength=0;
                }
            }
        };
        $scope.splitTable1DoubleClickAction = function (item, index) {
            var item = $scope.splitTable1List[index];
            $scope.index = index;
            if ($scope.splitQty == undefined || $scope.splitQty == 0) {
                $scope.numberList = [];
                for (var i = 1; i <= item.qty;) {
                    $scope.numberList.push(i++);
                }
                $("#selectQty").css("display","block");
                $("#selectQty").modal('show');
                $("#splitTablePopUp").modal('hide');
            } else {
                if (parseInt(item.qty) == parseInt($scope.splitQty)) {
                    $scope.addSplitTableSelRow(item, $scope.splitTable2List);
                    $scope.removeItemSplitTable1(item);
                } else {
                    $scope.qty = item.qty;
                    item.qty = $scope.splitQty;
                    $scope.addSplitTableSelRowSplit(item, $scope.splitTable1List, $scope.qty, $scope.splitTable2List);
                }
                $scope.splitQty = 0;
                $("#splitTablePopUp").modal('show');
                $("#selectQty").css("display","none");
                // var element = document.getElementById("body_info");
                // element.classList.add("modal-open");
            }
        };
        $scope.addSplitTableSelRowSplit = function (data, model1, qty, model) {
            $scope.iteamIndex = $scope.itemIndexOf(model, data.itemName);
            if (!angular.isUndefined($scope.iteamIndex) && $scope.iteamIndex !== null && $scope.iteamIndex !== -1) {
                $scope.currentQty = model[$scope.iteamIndex].qty;
                model[$scope.iteamIndex].qty = $scope.currentQty + data.qty;
                $scope.editSelectedItemListPrice(model[$scope.iteamIndex], model);
            } else {
                $scope.pushData(model, data);
            }
            $scope.iteamIndex = $scope.itemIndexOf(model1, data.itemName);
            $scope.qty = parseInt(qty) - parseInt($scope.splitQty);
            if (!angular.isUndefined($scope.iteamIndex) && $scope.iteamIndex !== null && $scope.iteamIndex !== -1) {
                model1[$scope.iteamIndex].qty = $scope.qty;
                $scope.editSelectedItemListPrice(model1[$scope.iteamIndex], model1);
            } else {
                $scope.pushData(model1, data);
            }
        };
        $scope.pushData = function (model, data) {
            model.push({
                itemCode: data.itemCode,
                itemId: data.itemId,
                itemName: data.itemName,
                itemCategoryId: data.itemCategoryId,
                itemCategoryName: data.itemCategoryName,
                itemDTOS: data.itemDTOS,
                gstItemTax: parseFloat(data.taxamt).toFixed(2),
                taxamt: parseFloat(data.taxamt).toFixed(2),
                unitPrice: parseFloat(data.unitPrice).toFixed(2),
                unitPriceIn: parseFloat(data.unitPriceIn).toFixed(2),
                amtinclusivetax: parseFloat(data.amtinclusivetax).toFixed(2),
                qty: data.qty,
                discountAmt: data.discountAmt,
                taxid: data.taxid,
                discountConfigAmt: data.discountConfigAmt,
                type: data.type,
                flag: data.flag,
                discountAmountRpercent: data.discountAmountRpercent,
                discPercent: data.discPercent,
                taxName: data.taxName,
                amtexclusivetax: parseFloat(data.amtexclusivetax).toFixed(2),
                inclusiveJSON: data.inclusiveJSON,
                uom: data.uom
            });
            $scope.editSelectedItemListPrice(model[model.length - 1], model);
        };
        $scope.openTableSelection = function () {
            if ($scope.orderToKitchenBuffer.length > 0) {
                var confirm = $window.confirm("Do you want to Clear Current Order ?");
                if (confirm === false) {
                    return false;
                }
            }
            //update table configuration dynamically
            if ($scope.tableConfigurationList.length > 0) {
                $scope.tableConfiguration = $scope.tableConfigurationList[0].configurationname;
            }
            $scope.orderToKitchenBuffer = [];
        };

        $scope.getCompleteItemList = function () {
            $http.get($scope.hiposServerURL + '/getSimplifiedItemList').then(function (response) {
                var data = response.data;
                $scope.itemList = angular.copy(data);
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        };
        $scope.getCompleteItemList();
        $scope.getTableList = function () {
            if($rootScope.selectedTableName!=undefined) {
                var getRequest = {
                    url: '/hipos/getTableList',
                    method: 'POST',
                    params: {
                        searchText: $rootScope.selectedTableName,
                        status: 'Active'
                    }
                };
                $http(getRequest).then(function (successResponse) {
                    $scope.completeTableList = successResponse.data;
                    if ($scope.selectedItemsList.length == 0 && $scope.tableSearchText == undefined) {
                        $scope.appendTableSelected($rootScope.selectedTableName, '', $rootScope.selectedTableId);
                    } else {
                        $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                    }
                });
            }
        };

        $scope.ChangeTables = function (val, type) {
            var getRequest = {
                url: '/hipos/getTableList',
                method: 'POST',
                params: {
                    searchText: val.tableName,
                    status: 'Active'
                }
            };
            $http(getRequest).then(function (successResponse) {
                $scope.completeTableList = successResponse.data;
                $scope.appendTableSelected(val.tableName, type,val.tableid);
            });
        }

        $scope.appendTableSelected = function (searchVal, type,tableId) {
            $scope.orderNo = "";
            $scope.agentSearchText = "";
            var tableObj = [];
            angular.forEach($scope.completeTableList, function (value, key) {
                if ((value.tableName.toLocaleLowerCase() == searchVal.toLocaleLowerCase())&&tableId==value.tableid) {
                    tableObj.push(value);
                }
            })
            if (tableObj.length === 0 && searchVal !== undefined) {
                $scope.tableSearchText = "";
                if (type == 'fromTable') {
                    $scope.fromTableSearchText = null;
                    $scope.fromTableId = null;
                } else if (type == 'toTable') {
                    $scope.toTableSearchText = null;
                    $scope.toTableId = null;
                }
                $scope.currTableName = "";
                $scope.currTableId = null;
                $scope.agentSearchText = "";
                $scope.orderNo = "";
                Notification.error({
                    message: 'Table not found',
                    positionX: 'center',
                    delay: 2000
                });
                $scope.selectedItemsList = [];
                $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
            } else if (searchVal === undefined) {
                document.activeElement.blur();
                $scope.selectedItemsList = [];
                $scope.orderToKitchenBuffer = [];
                $scope.previousOrdersConfirmed = [];

            } else {
                //Clear order buffer
                document.activeElement.blur();
                $scope.orderToKitchenBuffer = [];
                $scope.previousOrdersConfirmed = [];
                document.getElementById("iDemployeeSearchText").focus();
                $scope.onTableSelection(tableObj[0], type);
                $scope.disAmtInPer = 0;
                $scope.incServiceCharge = false;
            }
        };
        if($rootScope.guestName!=null&&$rootScope.guestName!=""){
            $scope.customerSearchText=$rootScope.guestName;
        }
        $scope.appendCustomerSelected = function (searchVal, type) {
            var customerObj = [];
            if (searchVal != "") {
                if (type == 'number') {
                    customerObj = $filter('filter')($scope.customerList, {
                        phoneNumber1: searchVal
                    }, true);
                } else {
                    customerObj = $filter('filter')($scope.customerList, {
                        customerName: searchVal
                    }, true);
                }
            }
            if (customerObj.length === 0 && searchVal !== undefined) {
                $scope.customerId = null;
            } else if (searchVal === undefined) {
                document.getElementById("iDcustomerSearchText").focus();
            } else {
                $scope.customerSearchText = customerObj[0].customerName;
                $scope.customerContactText = customerObj[0].phoneNumber1;
                $scope.customerMobileNo = customerObj[0].phoneNumber1;
                $scope.customerEmailText = customerObj[0].email;
                $scope.customerId = customerObj[0].customerId;
                document.getElementById("customerSearchText").focus();
                $scope.disAmtInPer=0;
                $scope.updatePayable();
                if($scope.discountType=='billWise'){
                    $scope.disAmtInPer = customerObj[0].discountPer;
                    if($scope.disAmtInPer!=null) {
                        $scope.disAmtInPer=$scope.disAmtInPer+"%";
                        $scope.updatePayable();
                    }
                }
            }
        };
        $scope.appendEmployeeSelected = function (searchVal) {
            var employeeObj = $filter('filter')($scope.employeeList, {
                employeeName: searchVal
            }, true);
            $scope.waiterSearchText = searchVal;
            if (employeeObj.length === 0 && searchVal !== undefined) {
                $('#iDemployeeSearchText').val("");
                $scope.itemSearchText = $("#iDemployeeSearchText").val("").trigger('change');
                $scope.itemSearchText = null;
                Notification.error({
                    message: 'Employee not found',
                    positionX: 'center',
                    delay: 2000
                });
            } else if (searchVal === undefined) {
                document.getElementById("iDemployeeSearchText").focus();
            } else if($rootScope.orderTypeName!='OnlineDelivery'){
                $scope.waiterName = $scope.waiterSearchText;
                //Clear order buffer
                document.getElementById("iDitemSearchText").focus();
            }
        };
        $scope.appendAgentSelected = function (searchVal) {
            var agentObj = $filter('filter')($scope.agentList, {
                agentName: searchVal
            }, true);
            $scope.agentSearchText = searchVal;
            if (agentObj.length === 0 && searchVal !== undefined) {
                $('#iDagentSearchText').val("");
                $scope.itemSearchText = $("#iDagentSearchText").val("").trigger('change');
                $scope.itemSearchText = null;
                Notification.error({
                    message: 'Agent not found',
                    positionX: 'center',
                    delay: 2000
                });
            } else if (searchVal === undefined) {
                document.getElementById("iDagentSearchText").focus();
            } else if($scope.notificationFlag!=true){
                document.getElementById("iDitemSearchText").focus();
            }
        };
        $scope.getCustomerBill = function () {
            $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
            $scope.placeOrdersTOKOT();
            if (angular.isUndefined($scope.selectedItemsList) || $scope.selectedItemsList.length <= 0) {
                Notification.error({
                    message: 'At least One item has to be selected',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            } else if (!$scope.isValidatedData()) {
                Notification.error({
                    message: 'Qty should not be Zero',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            } else {
                var discPercentage=0;
                if($scope.disAmtInPer==undefined){
                    $scope.disAmtInPer=0;
                }
                var disc= $scope.disAmtInPer.toString().slice(-1);
                if (disc == "%") {
                    $scope.discType="Percentage";
                    discPercentage = parseFloat($scope.disAmtInPer.toString().slice(0, -1));
                }
                else {
                    $scope.discType="Amount";
                    discPercentage = parseFloat(0);
                }
                if($scope.orderNo==null){
                    $scope.orderNo ="";
                }
                $scope.roundingOffValue = $scope.totalCheckOutamt - Math.round($scope.totalCheckOutamt);
                setTimeout(function () {
                    var jsonString = {
                        companyName: $scope.printData4.companyName,
                        companyAddress: $scope.printData4.address,
                        companyPhone: $scope.printData4.phone,
                        companyFax: $scope.printData4.fax,
                        companyGST: $scope.printData4.companyNo,
                        tableName: $scope.currTableName,
                        tableNo: $scope.currTableId,
                        waiter: $scope.waiterSearchText,
                        roundingOffValue: $scope.roundingOffValue,
                        selectedItemList: $scope.selectedItemsList,
                        customerName: $scope.customerSearchText,
                        customerId: $scope.customerId,
                        orderNo: $scope.orderNo,
                        agent: $scope.agentSearchText,
                        discount: discPercentage,
                        discountType: $scope.discType,
                        discountAmt: $scope.totalVPDiscount,
                        totalTax: $scope.totalTaxAmt,
                        subtotal: $scope.totalAmtExamt,
                        grandtotal: $scope.totalCheckOutamt,
                        serviceCharge: $scope.hiposServiceCharge,
                        hiposServiceChargeAmt: $scope.hiposServiceChargeAmt,
                        type: 'Desktop'
                    };
                    var postRequest = {
                        url: '/hipos/printCustomerBill',
                        method: 'POST',
                        data: jsonString
                    };
                    $http(postRequest).then(function (successResponse) {
                        console.log(successResponse);
                        $rootScope.getTableList();
                        if ($scope.configurationData.buildTypeCloud) {
                            var postRequest = {
                                url: 'http://localhost:9001/hiAccounts/sendDatatoLocalPrinter',
                                data: successResponse.data,
                                method: 'POST',
                                params: {}
                            };
                            $http(postRequest);
                        }
                    }, function (failureResponse) {
                        console.log(failureResponse);
                    })
                }, 3000);
            }
        };
        $scope.getItemInfoOnSearchText = function (searchVal) {
            var item = $filter('filter')($scope.itemList, {
                itemCode: searchVal.split(" ")[0]
            }, true);
            if (item.length > 0) {
                $scope.addItemToMainTable(item[0])
            } else {
                $('#iDitemSearchText').val("");
                $scope.itemSearchText = $("#iDitemSearchText").val("").trigger('change');
                $scope.itemSearchText = null;
                Notification.error({
                    message: 'Search string not found',
                    positionX: 'center',
                    delay: 2000
                });
            }
        };
        $scope.sendDataToPrinter = function (printContents, printer) {
            var postRequest = {
                url: '/hipos/printHTML',
                data: printContents,
                method: 'POST',
                params: {cssStyle: '', printType: printer}
            };

            $http(postRequest);
            //Logic for Browser Print
            var popupWin = window.open('', '_blank', 'width=300,height=300');
            popupWin.document.open();
            popupWin.document.write('<html><head></head><body onload="window.print()">' + printContents + '</body></html>');
            popupWin.document.close();
        };
        $scope.printBillById = function (divId, printer) {
            $timeout(function () {
                //fetch the html content
                var content = $('#' + divId).html();
                var modelToHide = divId.split("_")[0];
                $('#' + modelToHide).modal('hide');
                //console.log(content);
                $scope.sendDataToPrinter(content, printer);
                console.log("printBillById");
                //console.log(content);
                console.log("send to printer: " + printer);

            }, 100);
        };
        //Convert UOM-Qty eg: 60ml ml is uom name value is 750 so 60/750
        $scope.getConvertedQuantity = function (item, index, qty) {
            if (angular.isUndefined(qty)) {
                qty = 0;
            }
            angular.forEach(item.uomConvertorDTOList, function (value, key) {
                if (item.uom == value.unitOfMeasurementId) {
                    if (qty > 0) {
                        $scope.selectedItemsList[index].convertedQuantity = qty / value.uomValue;
                        $scope.selectedItemsList[index].uomValue = value.uomValue;
                        $scope.selectedItemsList[index].uomConvertedName = value.uomConvertedName;
                    }
                    else {
                        $scope.selectedItemsList[index].convertedQuantity = 0;
                        $scope.selectedItemsList[index].uomValue = 0;
                        $scope.selectedItemsList[index].uomConvertedName = "";
                    }
                }
            });
        };
        $scope.convertUOMQty = function (item, qty) {
            if (item.uomValue == null || item.uomValue == undefined) {
                item.uomValue = 0;
            }
            if (parseFloat(item.uomValue) === 0)
                return qty;
            else
                return ((item.uomValue * qty) + " " + item.uomConvertedName);
        };

        $scope.printKOTDateTime = function () {
            return new Date();
        };
        $scope.updateRemovedItemList = function () {
            //fetch updated removed item list
            $scope.removedItemList = [];

            var getRequest = {
                url: '/restaurant/getRestTempDataOnId',
                method: 'GET',
                params: {tableName: $scope.currTableName, tableId: $scope.currTableId}
            };

            $http(getRequest).then(
                function (successResponse) {
                    $scope.removedItemList = successResponse.data.removedItemsList;
                },
                function (failureResponse) {
                    console.log(failureResponse);
                }
            );
        };
        $scope.disAmtIn=0;
        $scope.updatePayable = function () {
            if ($scope.freeMeal == true) {
                $scope.disAmtInPer = 0;
            }
            if ($scope.disAmtInPer === undefined) {
                $scope.disAmtInPer = 0;
            }
            $scope.disAmtIn=0;
            $scope.discType = $scope.disAmtInPer.toString().slice(-1);
            if ($scope.discType == "%") {
                $scope.disAmtIn = ($scope.totalAmtExamt+$scope.disAmtIn)*(parseFloat($scope.disAmtInPer.toString().slice(0, -1))/100);
            }
            else {
                if(parseFloat($scope.disAmtInPer)<=$scope.totalAmtExamt) {
                    $scope.disAmtIn = parseFloat($scope.disAmtInPer);
                }else {
                    $scope.disAmtIn=0;
                    $scope.disAmtInPer=0;
                }
            }
            $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
        };
        $scope.getSelectedTableValues = function (type) {
            var getRequest = {
                url: '/hipos/getTableList?searchText=' + $scope.tableSearchText,
                method: 'POST',
                params: {status: 'Active'}
            };
            $http(getRequest).then(function (successResponse) {
                $scope.completeTablesList = successResponse.data;
                if ($scope.completeTablesList.length != 0) {
                    $scope.tableSearchText = $scope.completeTablesList[0].tableName;
                    $scope.currTableName = $scope.completeTablesList[0].tableName;
                    $scope.currTableId = $scope.completeTablesList[0].tableid;
                }
                if (angular.isUndefined(type)) {
                    $scope.appendTableSelected($scope.tableSearchText,'',$scope.currTableId);
                }

            });
        };
        $scope.printDuplicateBillRestaurant = function (printData) {
            console.log(printData);
            var postRequest = {
                url: '/hipos/printDuplicateBillSales',
                method: 'POST',
                data: printData,
                params: {
                    type: "Restaurant"
                }
            };
            $http(postRequest).then(function (successResponse) {
                if ($scope.configurationData.buildTypeCloud) {
                    console.log('Duplicate Bill -- Request type -- Cloud');
                    console.log(successResponse.data);
                    var postRequest = {
                        url: 'http://localhost:9001/hiAccounts/sendDatatoLocalPrinter',
                        data: successResponse.data,
                        method: 'POST',
                        params: {}
                    };
                    $http(postRequest);
                }
            }, function (failureResponse) {

            });
            $('#printSummaryBill').modal('hide');
        };
        $.getJSON('/resource/printerCategoryConf.json', function (data) {
            console.log(data);
            $scope.printerCategoryConfJSON = data;
        });
        $scope.clearPaymentValues = function () {
            $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];
            $scope.paymentList = [];
            $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
            if ($scope.splitTable1List.length > 0 || $scope.splitTable2List.length > 0) {
                $scope.resetSplitPopUp();
                $scope.getTempData();
            }
        };
        $scope.configDiscount = function (disConfig, data, index, type) {
            if (type == 'Config') {
                if (disConfig != null) {
                    $scope.discType = disConfig.toString().slice(-1);
                    if ($scope.discType == "%") {
                        data.type = "Percentage";
                        data.discPercent = parseFloat(disConfig.toString().slice(0, -1));
                    }
                    else {
                        data.type = "Amount";
                        data.discPercent = parseFloat(disConfig);
                    }
                }
                else {
                    data.type = "Amount";
                    data.discPercent = parseFloat(0);
                }
            }
            else if (type == 'Amt') {
                if (!angular.isUndefined(data.discountAmt)) {
                    data.type = "Amount";
                    data.discPercent = parseFloat(data.discountAmt / data.qty);
                }
                else {
                    data.type = "Amount";
                    data.discPercent = 0;
                }
            }
            $scope.editSelectedItemList(data, $scope.selectedItemsList);
        }
        $scope.updateitemDescription = function (item) {
            $scope.selectedItem = item;
            $scope.description = item.itemDescription;
            $("#description_popup").modal('show');
        }
        $scope.saveFormsetupDescription = function (desc) {
            $scope.selectedItem.itemDescription = $scope.description;
            $("#description_popup").modal('hide');
            $scope.updateKitchenOrder($scope.selectedItem);
            $scope.selectedItem = null;
            $scope.description = "";
        };

        $scope.saveDescriptionForKot= function () {
            if($scope.description==""||$scope.description==null||angular.isUndefined($scope.description)) {
                Notification.error({message: 'Please Mention Description', positionX: 'center', delay: 2000})
            }
            else {
                $scope.phone = $rootScope.phone;
                $("#description_popup_For_Cancel_Kot").modal('hide');
                if($scope.descriptionType=='clearAllKot'){
                    $scope.previousOrdersConfirmed = [];
                    // //Prepare to remove from data
                    var getRequest = {
                        method: 'GET',
                        url: '/hipos/deleteTableDataTemp',
                        data: '',
                        params: {"currTableName": $scope.currTableName, "currTableId": $scope.currTableId}
                    };

                    $http(getRequest).then(function (successResponse) {
                        console.log(successResponse)
                    }, function (failureResponse) {
                    });
                    // //Flush all angular data
                    $scope.splitTable1List = [];
                    $scope.removeAllItems();
                    $scope.placeOrdersTOKOT();
                }
            }
        };
        $scope.getPaginationCustomerList = function (page){
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
            if (angular.isUndefined($scope.customerSearchText)) {
                $scope.customerSearchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginationCustomerList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.customerSearchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                $scope.customerList = data.list;
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

        $rootScope.pageonload=true;

        $scope.changeStatusOfNotification=function (data,status) {
            var tabledata ="";
            var objectdata = JSON.parse(data.objectdata);
            var details;
            if(status == 'foodready') {
                details = {
                    "orderId": data.orderId,
                    "custNotiId": data.custNotiId,
                    "type": data.fromContact,
                    "deliveryTime": $scope.deliveryTime,
                    "prepareTime": $scope.preptime,
                    "status": "Food Ready",
                    "restaurantId": objectdata.order.restaurant_id,
                    "message": "Food Prepared"
                };
                $scope.allDetails = details;
                $scope.getConfirmNotifications();
            }else if(status=='Cancel'){
                details = {
                    "orderId": data.orderId,
                    "custNotiId": data.custNotiId,
                    "type": data.fromContact,
                    "deliveryTime": $scope.deliveryTime,
                    "prepareTime": $scope.preptime,
                    "status": "Cancelled",
                    "restaurantId": objectdata.order.restaurant_id,
                    "message": "Order Rejected"
                };
                $scope.allDetails =details;
                $scope.getConfirmNotifications();
            }else if(status=='Confirm'){
                $rootScope.sharedNotificationData = {
                    selectedNotification : data,
                    flag : "",
                    appendNotificationData : true
                };

                $scope.agentSearchText = "";
                $scope.orderNo = "";
                $scope.orderNo = $rootScope.sharedNotificationData.selectedNotification.notificationId;
                $scope.tableSearchText = "";
                $scope.notificationFlag = true;
                $scope.AgentNameText = $rootScope.sharedNotificationData.selectedNotification.fromCompname;
                $scope.appendAgentSelected($scope.AgentNameText);
                $scope.invoiceType = $scope.AgentNameText;
                if(data.tableName!=null){
                        $http.post("/hipos/getTable?tableName=" + data.tableName).then(function (response) {
                            tabledata = response.data;
                        });
                    $scope.tableSearchText = data.tableName;
                    $scope.currTableName = data.tableName;
                    $scope.currTableId = data.tableid;
                    $scope.tableId = data.tableid;
                    $scope.waiterSearchText = data.waiter;
                }else {
                    $scope.tableSearchText = $scope.orderNo;
                    $scope.currTableName = $scope.orderNo;
                    $scope.currTableId = "";
                    $scope.tableId = "";
                    $scope.waiterSearchText = "";
                }
                $scope.GSTINText = "";
                $scope.custStatusText = "Active";
                var objectdata = JSON.parse($rootScope.sharedNotificationData.selectedNotification.objectdata);
                $scope.customerAddressText = objectdata.order.customer_details.address;
                $scope.customerContactText = objectdata.order.customer_details.phone_number;
                $scope.customerSearchText = objectdata.order.customer_details.name;
                $scope.instructions = objectdata.order.instructions;
                if (objectdata.order.discount != null) {
                    $scope.disctAmt = objectdata.order.discount;
                } else {
                    $scope.disctAmt = 0;
                }
                $scope.orderToKitchenBuffer = [];
                $http.get('/hipos/getTempData?currTableName=' + $scope.currTableName + "&currTableId=" + $scope.currTableId).then(function (response) {
                    var data = response.data;
                    $scope.selectedItemsList = [];
                    $scope.previousOrdersConfirmed=[];
                    if (!angular.isUndefined(data.selectedItemsList) && data.selectedItemsList !== null) {
                        $scope.previousOrdersConfirmed = angular.fromJson(data.selectedItemsList);
                        $scope.selectedItemsList = angular.fromJson(data.selectedItemsList);
                    }
                    var objectdata = JSON.parse($rootScope.sharedNotificationData.selectedNotification.objectdata);
                    $scope.chargesList = [];
                    $scope.itemNotFound=false;
                    $scope.chargeNotFound=false;
                    angular.forEach(objectdata.order.order_items, function (data, key) {
                        $http.get($scope.hiposServerURL + '/getItemByName?itemName=' + data.item_name).then(function (response) {
                            if(response.data==""){
                                $scope.itemNotFound=true;
                                Notification.error({
                                    message: 'Item Not Found', positionX: 'center',
                                    delay: 2000
                                });
                                return;
                            }
                            var item = response.data;
                            if (item != null && item != undefined) {
                                item.qty = data.item_quantity;
                                item.salesPrice = data.item_unit_price;
                                item.unitPrice = data.item_unit_price;
                                item.discountAmt = 0;
                                angular.forEach(angular.fromJson(data.charges), function (val, key) {
                                    $scope.iteamIndex = $scope.chargesIndex($scope.chargesList, val.title);
                                    if (angular.isUndefined($scope.iteamIndex) && $scope.iteamIndex === null && $scope.iteamIndex == -1) {
                                        $scope.chargesList.push(val);
                                    }
                                    $scope.notificationItemsLength = parseInt(objectdata.order.order_items.length) + $scope.chargesList.length;
                                    $http.get($scope.hiposServerURL + '/getItemByName?itemName=' + val.title).then(function (response) {
                                        if(response.data==""){
                                            $scope.itemNotFound=true;
                                            Notification.error({
                                                message: 'Item Not Found', positionX: 'center',
                                                delay: 2000
                                            });
                                            return;
                                        }
                                        var item = response.data;
                                        if (item != null && item != undefined) {
                                            item.qty = 1;
                                            item.salesPrice = val.value;
                                            item.unitPrice = val.value;
                                            item.discountAmt = 0;
                                            $scope.iteamIndex = $scope.itemIndexOf($scope.selectedItemsList, item.itemName);
                                            if (!angular.isUndefined($scope.iteamIndex) && $scope.iteamIndex !== null && $scope.iteamIndex !== -1) {
                                                //fetch already added item qty
                                                $scope.currentQty = $scope.selectedItemsList[$scope.iteamIndex].qty;
                                                //update qty
                                                $scope.selectedItemsList[$scope.iteamIndex].qty = parseFloat($scope.currentQty) + 1;
                                                //update qty vs amount
                                                $scope.editSelectedItemList($scope.selectedItemsList[$scope.iteamIndex], $scope.selectedItemsList);

                                            } else {
                                                $scope.addSelectedItemList(item, $scope.selectedItemsList);
                                            }
                                        }else {
                                            $scope.chargeNotFound=true;
                                        }
                                    });
                                });
                                $scope.addSelectedItemList(item, $scope.selectedItemsList);
                            }else {
                                $scope.itemNotFound=true;
                            }
                        });
                    });
                    if($scope.itemNotFound==true){
                        Notification.error({
                            message: 'Item Not Found', positionX: 'center',
                            delay: 2000
                        });
                    }
                    if($scope.chargeNotFound==true){
                        Notification.error({
                            message: 'Charges Not Found', positionX: 'center',
                            delay: 2000
                        });
                    }
                    if (objectdata.order.charges != undefined) {
                        var charges = objectdata.order.charges;
                        $scope.chargesLength = charges.length;
                    } else {
                        $scope.chargesLength = parseInt(0);
                    }
                    $scope.notificationItemsLength = parseInt($scope.chargesLength) + objectdata.order.order_items.length;
                    angular.forEach(charges, function (data, key) {
                        $http.get($scope.hiposServerURL  + '/getItemByName?itemName=' + data.title).then(function (response) {
                            if(response.data==""){
                                $scope.itemNotFound=true;
                                Notification.error({
                                    message: 'Item Not Found', positionX: 'center',
                                    delay: 2000
                                });
                                return;
                            }
                            var item = response.data;
                            if (item != null && item != undefined) {
                                item.qty = 1;
                                item.salesPrice = data.value;
                                item.unitPrice = data.value;
                                item.discountAmt = 0;
                                $scope.addSelectedItemList(item, $scope.selectedItemsList);
                            }
                        });
                    });

                });
                details = {
                    "orderId": data.orderId,
                    "custNotiId": data.custNotiId,
                    "type": data.fromContact,
                    "deliveryTime": $scope.deliverytime,
                    "prepareTime": $scope.preptime,
                    "externalOrderId": $scope.externalid,
                    "status": 'Acknowledged',
                    "restaurantId": objectdata.order.restaurant_id,
                    "message": 'order accepted'
                };
                $scope.allDetails= details;
            }
            $scope.orderNo = data.notificationId;
        };
        $scope.getConfirmNotifications = function () {
            $http.post($scope.hiposServerURL + '/getRestaurantConfirmNotification', angular.toJson($scope.allDetails))
                .then(function (response) {
                    $scope.getOnlineDelivery('placed');
                    $scope.getOnlineDelivery('Acknowledged');
                    $scope.getOnlineDelivery('Completed');
                    $scope.getOnlineDelivery('Cancelled');
                    Notification.success({
                        message: 'Successfully Updated', positionX: 'center',
                        delay: 2000
                    });
                });
        };
        $scope.increaseQty = function (item) {
            if (angular.isUndefined(item.increaseQuantity)) {
                item.increaseQuantity = 0;
            }
            item.qty = item.qty + parseFloat(item.increaseQuantity);
            $scope.onEditPriceRQty(item);
            item.increaseQuantity = 0;
        };
        $scope.chargesIndex = function (array, searchVal) {
            var itemIndex = -1;
            if ($scope.isUndefinedOrNull(searchVal)) {
                itemIndex = -1;
            } else if (array.length > 0) {
                var foundIndex = $filter('filter')(array, {
                    title: searchVal
                }, true)[0];
                itemIndex = array.indexOf(foundIndex);
            }
            return itemIndex;
        };
        $scope.decreaseQty = function (item) {
            if (angular.isUndefined(item.decreaseQuantity)) {
                item.decreaseQuantity = 0;
            }
            if (item.decreaseQuantity > item.qty) {
                Notification.error({
                    message: 'You Cannot Decrease More Than Quantity',
                    positionX: 'center',
                    delay: 2000
                });
                item.qty = 0;
                item.decreaseQuantity = 0;
            }
            item.qty = item.qty - item.decreaseQuantity;
            $scope.onEditPriceRQty(item);
            item.decreaseQuantity = 0;
        };
        $scope.openOnlinePage = function (type) {
            $rootScope.orderTypeName=type;
            $rootScope.selectedTableId=null;
            $rootScope.selectedTableName=null;
            $window.location.href="#!/OnlineDelivery";
            $scope.getOnlineDelivery('placed');
            $scope.getOnlineDelivery('Acknowledged');
            $scope.getOnlineDelivery('Completed');
            $scope.getOnlineDelivery('Cancelled');
            $scope.getOnlineDelivery('');
        };
        $scope.openDigiOrders = function (type) {
            $rootScope.orderTypeName=type;
            $rootScope.selectedTableId=null;
            $rootScope.selectedTableName=null;
            $window.location.href="#!/digiOrders";
            $scope.getDigiOrders('placed');
            $scope.getDigiOrders('Acknowledged');
            $scope.getDigiOrders('Completed');
            $scope.getDigiOrders('Cancelled');
            $scope.getDigiOrders('');
        };
        // if (/\!\/OnlineDelivery/.test($window.location.href.split("#/")[0])) {
        //     $scope.orderType("OnlineDelivery");
        // }

        $scope.openOffersPage = function () {
            $scope.couponList();
            $("#selectOffersPage").modal('show');
        }
        $scope.viewSpecialRequests = function (data) {
            $http.post("/hipos/getTablesListSpecialRequests?id="+data.id).then(function (response) {
                var data = response.data;
                $scope.special = data.specialRequests;
                $("#view_special_requests").modal('show');
            });
        };
        $scope.couponList = function () {
            $http.post('/hipos/getCouponList' ).then(function (response) {
                var data = response.data;
                $scope.getCouponList = data;
            })
        }
        // $scope.couponList();

    }

]);
