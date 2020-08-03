app.directive("paymentModalRestaurant", function ($http, $filter, Notification, $timeout, $window,$rootScope,$interval) {
    return {
        restrict: 'E',
        templateUrl: "partials/paymentModal.html",
        link: function ($scope) {

            $scope.today = function () {
                $scope.dt = new Date();
                $scope.format = 'dd/MM/yyyy';
            };
            $scope.today();
            $scope.opened = [];
            $scope.openDatePicker = function ($event, index) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened[index] = true;
            };
            $scope.opened1 = [];
            $scope.openDatePicker1 = function ($event, index) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.opened1[index] = true;
            };
            $scope.getBankAccountList = function (ind) {
                $scope.val = "";
                $scope.ind = ind;
                $http.get($scope.hiposServerURL + "/" + 1 + '/getBankAccountList?accountSearchText=' + $scope.val).then(function (response) {
                    var data = response.data;
                    $scope.accountList = angular.copy(data);
                    $("#selectAccount1").modal('show');
                    $("#paymentNew1").modal('hide');
                    $scope.accountSearchText = $scope.val;
                    $scope.searchText = $scope.val;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })
            };

            $scope.getAccount = function (accountCode) {
                $http.get($scope.hiposServerURL + "/" + 1 + '/getAccount?accountCode=' + accountCode).then(function (response) {
                    $scope.bankAccount=response.data[0];
                    $scope.addAccount($scope.bankAccount);
                },function (error) {
                    Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
                })
            };
            $scope.addAccount = function (account) {
                $scope.paymentDropdown[$scope.ind].bankAccount = account.account_name;
                $scope.paymentDropdown[$scope.ind].bankAccountId = account.accountid;
                if(parseFloat($scope.totalPaidAmt)>0){
                    $scope.paymentDropdown[$scope.ind].bankAmount = $scope.totalVPBeforDiscount-parseFloat($scope.totalPaidAmt);
                }else {
                    $scope.paymentDropdown[$scope.ind].bankAmount = $scope.totalVPBeforDiscount;
                }
                $scope.OnChangeMultiPayament($scope.paymentDropdown[$scope.ind].bankAmount,Math.round($scope.totalCheckOutamt),"Bank",0);
                $("#selectAccount1").modal('hide');
                $("#paymentNew1").modal('show');
            };
            $scope.OnlinePayType = function (value) {
                $scope.typepayOl = value;
            }

            $scope.settlePayment = function (order) {
                $http.get('/hipos/getOnlineOrders?orderNo='+order.notificationId).then(function (response) {
                    var data = response.data;
                    if(data.message=="Invoice Done"){
                        Notification.error({message:'Invoice Done Already',positionX:'center',delay:2000})
                    }
                    if(data!=undefined) {
                        angular.forEach(data, function (value, key) {
                            if (value.selectedItemsList !== null) {
                                value.selectedItemsList = JSON.parse(value.selectedItemsList);
                            }
                        });
                        var token = data[0];
                        $scope.selectedItemsList = token.selectedItemsList;
                        $scope.currTableName = token.tableName;
                        $scope.tableSearchText = token.tableName;
                        $scope.currTableId = token.tableId;
                        $scope.orderNo = token.orderNo;
                        $scope.waiterSearchText = token.useraccount_id;
                        $scope.employeeSearchText = token.useraccount_id;
                        $scope.waiterName = token.useraccount_id;
                        $scope.customerId = token.customerId;
                        $scope.agentSearchText = token.agentId;
                        $scope.invoiceType = "OnlineDelivery";
                        $scope.orderToKitchenBuffer = [];
                        $scope.notificationFlag = true;
                        $scope.paymentMethodType = "other";
                        $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
                        $scope.openSettlePayment(false);
                    }else {
                        Notification.error({
                            message: 'Invoice is already done',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                }, function (failureResponse) {
                    console.log(failureResponse)
                });
            }
            if($scope.customerMobileNo==undefined){
                $scope.customerMobileNo="";
            }
            $scope.saveSI = function (paymentType, type,smsType) {
                if(smsType=='sms'||$scope.smsType==true){
                    $scope.smsType=true;
                    if($scope.customerMobileNo==null||$scope.customerMobileNo==""){
                        Notification.error({
                            message: 'Mobile No is not configured for customer please enter Mobile No',
                            positionX: 'center',
                            delay: 2000
                        })
                        $scope.showEmailBox = true;
                        return;
                    }
                }else {
                    $scope.smsType=false;
                }
                if (($scope.totalCPAfterDiscount > $scope.totalCPAmountTendered && paymentType === 'cashPayment')
                    || ($scope.totalVPAfterAllDeductions > $scope.totalVPAmountTendered && paymentType === 'voucherPayment')
                    || ($scope.totalCCPAfterDiscount > $scope.totalCCPAmountTendered && paymentType === 'creditPayment')
                    || ($scope.totalAPAfterDiscount > $scope.totalAPAmountTendered && paymentType === 'airPay')
                    || ($scope.totalIPAfterDiscount > $scope.totalINVAmountTendered && paymentType === 'invoicePay')
                ) {
                    Notification.error({
                        message: 'Total Paid amout is lesser than to be paid amount',
                        positionX: 'center',
                        delay: 2000
                    });
                    return false;
                } else if ($scope.splitTable2List !== null && $scope.splitTable2List.length <= 0) {
                    /************      Place Order to Printer Instructed             ********/
                    if($scope.orderToKitchenBuffer.length>0||($scope.customerId == "" || $scope.customerId == null)) {
                        $scope.placeOrdersTOKOT();
                    }
                    /************      Place Order to HTML Coding Style              ********/
                    $scope.saveFullSI(paymentType, type);
                } else {
                    $scope.saveSplitSI(paymentType, type);
                }

            };
            $scope.saveFullSI = function (paymentType, type) {
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
                }
                else if ((angular.isUndefined($scope.waiterSearchText) || $scope.waiterSearchText == null || $scope.waiterSearchText == "") && $scope.orderTypeFlag != 'TakeAway' && ($scope.orderTypeFlag != 'OnlineDelivery'&&$scope.notificationFlag!=true)) {
                    Notification.error({
                        message: 'Select Waiter ',
                        positionX: 'center',
                        delay: 2000
                    });
                }
                else {
                    angular.forEach($scope.paymentDropdown, function (payment, key) {
                        if (payment.DEFAULT_PAYMENT_TYPE=='airPay') {
                            $scope.AIRPAYPAYMENT = true;
                        }
                        if (payment.DEFAULT_PAYMENT_TYPE=='invoicePay') {
                            $scope.INVPAYMENT = true;
                        }
                    });

                    if($scope.AIRPAYPAYMENT === true){
                        $scope.swipeInvMethod(paymentType,type,'');
                    }
                    else if($scope.INVPAYMENT === true){
                    $scope.onlineInvMethod(paymentType,type,status);
                    }else{
                        $scope.saveInvoice(paymentType,type);
                    }
                }
            };
            $scope.swipeInvMethod = function(paymentType,type,status){
                $scope.payStatus = "initiated";
                $('#paymentsOnline').modal('show');
                if(status === 'Re-Initiate'){
                    $('#warningpopup').modal('hide');
                    $('#paymentsOnline').modal('show');
                }
                $http.post('/hipos'+ '/swipeMachinePayment?payStatus='+ status,
                    angular.toJson($scope.populateSaveSIData(paymentType))).then(function (response) {
                    var datas = response;
                    if(datas!=null) {
                        var data = datas.data;
                        if (data.message == "Successful") {
                            $('#SuccessOnline').modal('show');
                            $scope.swipePay = data.airPayId;
                            $scope.payStatus = "success";
                            $scope.saveInvoice(paymentType, type);
                            $('#paymentsOnline').modal('hide');
                            $('#SuccessOnline').modal('hide');

                        }
                        else if (data.message == "FAIL") {
                            Notification.error({
                                message: 'please Check the MerchantId/OrderId',
                                positionX: 'center',
                                delay: 2000
                            });
                            $('#paymentsOnline').modal('hide');

                        }
                        else if (data.message == "Payment Processing Failed"|| data.message == "REVERSED") {
                            $scope.payStatus = "failure";
                            $('#warningpopup').modal('show');

                        }
                        else {
                            Notification.error({
                                message: data.message,
                                positionX: 'center',
                                delay: 2000
                            });
                            $scope.payStatus = "";
                            $('#paymentsOnline').modal('hide');

                        }
                    }else{
                        Notification.error({
                            message: 'please try the Transaction again',
                            positionX: 'center',
                            delay: 2000
                        });
                    }

                })
            }
            $scope.onlineInvMethod = function(paymentType,type,status){
                $scope.payStatus = "initiated";
                $('#paymentsOnline').modal('show');
                if(status === 'Re-Initiate'){
                    $('#warningpopup').modal('hide');
                    $('#paymentsOnline').modal('show');
                }
                $http.post('/hipos'+ '/onlinePay?payStatus='+ status,
                    angular.toJson($scope.populateSaveSIData(paymentType))).then(function (response) {
                    var datas = response;
                    if(datas!=null) {
                        var data = datas.data;
                        if (data.message == "Success") {
                            $('#SuccessOnline').modal('show');
                            $scope.onlineInvoicePay = data.invoicePayId;
                            $scope.payStatus = "success";
                            $scope.saveInvoice(paymentType, type);
                            $('#paymentsOnline').modal('hide');
                            $('#SuccessOnline').modal('hide');

                        }

                        else if (data.message == "Fail" || data.message == "TRANSACTION CANCELLED") {
                            $scope.payStatus = "failure";
                            $('#warningpopup').modal('show');

                        }
                        else {
                            Notification.error({
                                message: data.message,
                                positionX: 'center',
                                delay: 2000
                            });
                            $scope.payStatus = "";
                            $('#paymentsOnline').modal('hide');

                        }
                    }else{
                        Notification.error({
                            message: 'please try the Transaction again',
                            positionX: 'center',
                            delay: 2000
                        });
                    }

                })
            }
            $scope.closeAirPayment = function(){
                $scope.payStatus ="";
                $('#warningpopup').modal('hide');
                $('#paymentsOnline').modal('hide');
            }

            $scope.saveInvoice = function (paymentType,type){
                $scope.isDisabled = true;
                if ($scope.currTableName == 'TAKEAWAY') {
                    $scope.waiterName = "";
                }
                $http.post('/hipos' + '/save?tableNo=' + $scope.currTableId + "&tableName=" + $scope.currTableName + "&waiterName=" + $scope.waiterName + "&printVal=" + type +"&locationId="+$rootScope.selectedLocation,
                    angular.toJson($scope.populateSaveSIData(paymentType))).then(function (response, status, headers, config) {
                    $scope.isDisabled = false;
                    $scope.notificationFlag=false;
                    var data = response.data.Desktop;
                    $scope.TotalAmtAfterDiscount = 0;
                    angular.forEach(data.siData.selectedItemsList, function (value, key) {
                        $scope.TotalAmtAfterDiscount = value.amtexclusivetax + $scope.TotalAmtAfterDiscount;
                    });
                    $scope.printSavedData(data,response,type);
                    $scope.payStatus = "";
                    $scope.AIRPAYPAYMENT=false;
                    $scope.INVPAYMENT=false;
                }), function (error) {
                    Notification.error({
                        message: 'Somthing went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                };
            };
            $scope.printSavedData = function(data,response,type){
                $scope.printData = data;

                if ($scope.printPreview) {
                    if (type == 'savePrint' && $scope.orderTypeFlag != "DineIn") {
                        $('#printSummaryBill').modal('show');
                    }
                }
                else {
                    if ($scope.configurationData.buildTypeCloud) {
                        console.log('Payment Method Call -- Request type: Cloud');
                        console.log(response.data.Cloud);
                        var postRequest = {
                            url: 'http://localhost:9001/hiAccounts/sendDatatoLocalPrinter',
                            data: response.data.Cloud,
                            method: 'POST',
                            params: {}
                        };
                        $http(postRequest);
                        if(response.data.Desktop.siData.airPayId!=null||response.data.Desktop.siData.olInvPayId!=null) {
                            var postRequest = {
                                url: 'http://localhost:9001/hiAccounts/sendDatatoLocalPrinter',
                                data: response.data.CloudPaybill,
                                method: 'POST',
                                params: {}
                            };
                            $http(postRequest);
                        }
                    }
                }
                $("#paymentNew1").modal('hide');
                $(".modal-backdrop").attr('class', 'modal');
                /************      Place Order to Printer Instructed             ********/
                Notification.success({
                    message: 'Order has been saved successfully',
                    positionX: 'center',
                    delay: 2000
                });
                $rootScope.getTableList();
                $scope.removeAllItems();
                $scope.orderType($scope.orderTypeFlag);
            }
            $scope.orderType = function (type) {
                $scope.instructions = "";
                var getRequest = {
                    url: '/hipos/getTableList',
                    method: 'POST',
                    params: {
                        searchText: '',
                        status: 'OrderType'
                    }
                };
                $http(getRequest).then(function (successResponse) {
                    console.log(successResponse.data);
                    $scope.orderTypeTableList = successResponse.data;
                    $scope.noOfPersonsList = [];
                    if ((type == undefined || type == null)) {
                        type = $scope.configurationData.restaurant;
                    }
                    if ($rootScope.orderTypeName == 'DineIn') {
                        $scope.orderTypeFlag = $rootScope.orderTypeName;
                        type = null;
                        $scope.waiter = false;
                        $scope.table = false;
                        $rootScope.orderTypeName = null;
                        $scope.waiterPersonsList = [];
                        angular.forEach($scope.employeeList, function (val, key) {
                            if (val.waiterFlag == true) {
                                $scope.waiterPersonsList.push(val);
                            }
                        })
                        $scope.employeeList = $scope.waiterPersonsList;
                    } else if (type != null) {
                        $scope.orderTypeFlag = type;
                        $rootScope.orderTypeName = type;
                    }
                    $scope.style = {'background-color': 'pink', 'color': 'black'};
                    if (type == 'TakeAway') {
                        $scope.orderNo = "";
                        $scope.waiterName = "";
                        $scope.agentSearchText = "";
                        var tableObj = [];
                        angular.forEach($scope.orderTypeTableList, function (value, key) {
                            if (value.tableName.toLocaleLowerCase() == "TAKEAWAY".toLocaleLowerCase()) {
                                tableObj.push(value);
                            }
                        })
                        $scope.orderToKitchenBuffer = [];
                        $scope.previousOrdersConfirmed = [];
                        $scope.waiter = true;
                        $scope.table = true;
                        $scope.waiterSearchText = null;
                        $scope.style1 = $scope.style;
                        $scope.style2 = null;
                        $scope.tableSearchText = null;
                        $scope.currTableId = null;
                        $scope.onTableSelection(tableObj[0], "");
                        $scope.style3 = null;
                        $scope.waiterHeading = 'Waiter';
                        $window.location.href = '#!/restaurant';
                    } else if (type == 'Delivery') {
                        $scope.orderNo = "";
                        $scope.agentSearchText = "";
                        var tableObj = [];
                        angular.forEach($scope.orderTypeTableList, function (value, key) {
                            if (value.tableName.toLocaleLowerCase() == "Delivery".toLocaleLowerCase()) {
                                tableObj.push(value);
                            }
                        })
                        $scope.orderToKitchenBuffer = [];
                        $scope.previousOrdersConfirmed = [];
                        $scope.style2 = $scope.style;
                        $scope.style1 = null;
                        $scope.style3 = null;
                        $scope.waiter = false;
                        $scope.table = true;
                        $scope.tableSearchText = null;
                        $scope.currTableId = null;
                        $scope.onTableSelection(tableObj[0], "");
                        $scope.waiterSearchText = null;
                        $scope.waiterHeading = 'Delivery Person';
                        $scope.deliveryPersonList = [];
                        angular.forEach($scope.employeeList, function (val, key) {
                            if (val.deliveryFlag == true) {
                                $scope.deliveryPersonList.push(val);
                            }
                        })
                        $window.location.href = '#!/restaurant';
                        $scope.employeeList = $scope.deliveryPersonList;
                    } else if (type == 'DineIn') {
                        $scope.style3 = $scope.style;
                        $scope.style2 = null;
                        $scope.style1 = null;
                        $scope.waiter = false;
                        $scope.table = false;
                        $scope.waiterHeading = 'Waiter';
                        $scope.waiterPersonsList = [];
                        $rootScope.pageonload=false;
                        angular.forEach($scope.employeeList, function (val, key) {
                            if (val.waiterFlag == true) {
                                $scope.waiterPersonsList.push(val);
                            }
                        })
                        $scope.employeeList = $scope.waiterPersonsList;
                        $scope.openTableSelection();
                        $window.location.href = '#!/restaurantZone';
                    }
                    else if (type == 'OnlineDelivery') {
                        $scope.openOnlinePage(type);
                    }
                    else if (type == 'digiorders') {
                        $scope.openDigiOrders(type);
                    }
                    $scope.invoiceType = $scope.orderTypeFlag;
                });
            }
            $scope.showEmailBox = false;
            $scope.removeAllItems = function () {
                $scope.airpayDisable = false;
                $scope.onlineInvoicePay ="";
                $scope.swipePay ="";
                $scope.transaction = false;
                $scope.totalCheckOutamt = parseFloat('0.00');
                $scope.totalCCPBeforDiscount = parseFloat('0.00');
                $scope.totalCCPAfterDiscount = parseFloat('0.00');
                $scope.totalCPBeforDiscount = parseFloat('0.00');
                $scope.totalAPBeforDiscount = parseFloat('0.00');
                $scope.totalCPAmountRefunded = parseFloat('0.00');
                $scope.totalCPDiscount = parseFloat('0.00');
                $scope.totalAPDiscount = parseFloat('0.00');
                $scope.totalIPDiscount = parseFloat('0.00');
                $scope.totalCCPDiscount = parseFloat('0.00');
                $scope.totalCPAmountTendered = parseFloat('0.00');
                $scope.totalINVAmountTendered = parseFloat('0.00');
                $scope.totalAPAmountTendered = parseFloat('0.00');
                $scope.totalAirPayAmountTendered = parseFloat('0.00');
                $scope.totalCCPAfterDiscount = parseFloat('0.00');
                $scope.totalVPAmountTendered = parseFloat('0.00');
                $scope.totalVPAfterAllDeductions = parseFloat('0.00');
                $scope.totalVoucherAmt = parseFloat('0.00');
                $scope.totalVPAmountRefunded = parseFloat('0.00');
                $scope.totalVPDiscount = parseFloat('0.00');
                $scope.totalAmtExamt = parseFloat('0.00');
                $scope.totalAmtEx = parseFloat('0.00');
                $scope.totalTaxAmt = parseFloat('0.00');
                $scope.totalCheckOutamt = parseFloat('0.00');
                $scope.voucherCode = "";
                $scope.orderNo = "";
                $scope.voucherDate = "";
                $scope.notificationFlag = false;
                $scope.freeMeal = false;
                $scope.agentIdOfInvoice = "";
                $scope.agentSearchText = "";
                $scope.instructions = "";
                $scope.makeAgentReadonly = false;
                $scope.agentId = "";
                $scope.itemCount = 0;
                $scope.totalAfterDiscount = parseFloat('0.00');
                $scope.totalBeforDiscount = parseFloat('0.00');
                $scope.tenderedAmount = parseFloat('0.00');
                $scope.discountAmt = parseFloat('0.00');
                $scope.totalAmtExamt = parseFloat('0.00');
                $scope.totalAmtEx = parseFloat('0.00');
                $scope.totalTaxAmt = parseFloat('0.00');
                $(".resetClass").val("");
                $scope.selectedItemsList = [];
                $scope.previousOrdersConfirmed = [];
                $scope.itemSearchText = "";
                $scope.customerSearchText = "Cash Customer";
                $scope.customerId = 2;
                $scope.selectedItemListRemoval = {};
                if ($scope.splitTable1List.length <= 0 || angular.isUndefined($scope.splitTable1List)) {
                    $scope.tableSearchText = "";
                    $scope.agentSearchText = "";
                    $scope.incServiceCharge = false;
                    $("#iDtableSearchText").val("");
                    $scope.makeEmployeeSearchTextReadonly = false;
                }

                $("#iDitemSearchText").val("");

                $scope.disAmtInPer = 0;
                $scope.resetSplitPopUp();
            };

            $scope.openSettlePayment = function (isDirectCash,printType,type) {
                if($scope.paymentMethodType=="cash"){
                    isDirectCash=true;
                }else {
                    isDirectCash=false;
                }
                if(type=='sms'){
                    $scope.customerMobileNo=$rootScope.phone;
                    $scope.smsType=true;
                    if($scope.customerMobileNo==null||$scope.customerMobileNo==""){
                        Notification.error({
                            message: 'Mobile No is not configured for customer please enter Mobile No',
                            positionX: 'center',
                            delay: 2000
                        })
                        $scope.showEmailBox = true;
                    }
                }else {
                    $scope.smsType=false;
                }
                console.log($scope.totalCheckOutamt);
                $scope.totalVPAmountRefunded=parseFloat(0);
                if ((angular.isUndefined($scope.totalCheckOutamt) || parseFloat($scope.totalCheckOutamt).toFixed(2) <= 0)&&$scope.selectedItemsList.length==0) {
                    Notification.error({
                        message: 'At least One item has to be selected',
                        positionX: 'center',
                        delay: 2000
                    });
                }else if (!$scope.isValidatedData()) {
                    Notification.warning({
                        message: 'Qty should not be blank or zero',
                        positionX: 'center',
                        delay: 2000
                    });
                } else {
                    $scope.invokeOrderId = 1;
                    // $http.post('/hipos' + '/validateCheckout?invokeOrderId=' + $scope.invokeOrderId,
                    //     angular.toJson($scope.populateSaveSIData("", ""))).then(function (response, status, headers, config) {
                    //     var data = response.data;
                    //     $scope.itemsNoStockList = data;
                    //     if ($scope.itemsNoStockList !== null && $scope.itemsNoStockList.length > 0) {
                    //         $("#ItemsNoStockListModel").modal('show');
                    //     }
                    //     else {
                            $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
                            $scope.paymentButton = "Restaurant";
                            $scope.totalPaidAmt = 0;

                            if (isDirectCash) {
                                var confirm = $window.confirm("Do you want to continue with payment?");
                                if (confirm == false) {
                                    return false;
                                }
                                $scope.totalPaidAmt=Math.round($scope.totalCheckOutamt);
                                $scope.paymentDropdown=[];
                                $scope.paymentDropdown.push({
                                    totalCPAmountTendered: Math.round($scope.totalCheckOutamt),
                                    id:"paymentDropdown1",
                                    DefaultPaymentmethodId:1,
                                    DEFAULT_PAYMENT_TYPE:"Cash"
                                })
                                $scope.placeOrdersTOKOT("multiPayment",printType);
                            } else if($scope.invoiceType=="OnlineDelivery"){
                                $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];
                                angular.forEach($scope.paymentList[0],function (val,key) {
                                    if(val.paymentmethodName==$scope.agentSearchText) {
                                        $scope.paymentDropdown=[];
                                        $scope.paymentDropdown.push({
                                            bankAmount: Math.round($scope.totalCheckOutamt),
                                            id: "paymentDropdown1",
                                            DefaultPaymentmethodId: val.paymentmethodId,
                                            bankAccount: val.accountMaster,
                                            bankAccountId: val.accountMasterId,
                                            DEFAULT_PAYMENT_TYPE: val.paymentmethodType
                                        });
                                        $scope.OnChangeMultiPayament($scope.totalVPAfterDiscount,Math.round($scope.totalCheckOutamt),'BANK',0);
                                        $http.get($scope.hiposServerURL + "/" + 1 + '/getBankAccountList?accountSearchText=' + "").then(function (response) {
                                            var data = response.data;
                                            $scope.accountList = angular.copy(data);
                                            angular.forEach($scope.accountList,function (value,key) {
                                                if(value.accountid==val.accountMasterId){
                                                    $scope.paymentDropdown[0].bankAccount = value.account_name;
                                                    $scope.paymentDropdown[0].bankAccountId = value.accountid;
                                                }
                                            })
                                            $scope.placeOrdersTOKOT("payment");
                                            $("#paymentNew1").modal('show');
                                            return;
                                        });
                                    }
                                    else{
                                        $scope.placeOrdersTOKOT("payment");
                                        $("#paymentNew1").modal('show');
                                    }
                                })
                            }
                            else {
                                if($scope.orderToKitchenBuffer.length>0||($scope.customerId == "" || $scope.customerId == null)){
                                    $scope.placeOrdersTOKOT("payment");
                                    $("#paymentNew1").modal('show');
                                }else {
                                    $("#paymentNew1").modal('show');
                                }
                            }

                        // }
                    // }, function (error) {
                    //     Notification.error({message: data.message, positionX: 'center', delay: 2000});
                    // })
                }
            };
            $scope.populateSaveSIData = function (paymentType, operation) {
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
                $scope.getTotalAmtForSelectedItems($scope.selectedItemsList);
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
                $scope.APDetails = {
                    airPayPaymentList: $scope.AIRPAY_PAYMENT_DETAILS
                };
                $scope.VPDetails = {
                    multiVoucherPayments: $scope.VOUCHER_PAYMENT_DETAILS
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
                }
                else {
                    $scope.discType="Amount";
                    discPercentage = parseFloat(0);
                }
                var data = {
                    operation: operation,
                    selectedItemsList: $scope.selectedItemsList,
                    cashPayment: $scope.CPDetails,
                    airPayments: $scope.APDetails,
                    isAirPayPayment: $scope.AIRPAYPAYMENT,
                    creditPayment: $scope.CCPDetails,
                    bankPayment: $scope.BPDetails,
                    voucherPayment: $scope.VPDetails,
                    invoicePaymentList: $scope.INVOICE_PAYMENT_DETAILS,
                    cmpyLocation: $rootScope.selectedLocation,
                    custLocation: $scope.toLocation,
                    totalRemaininBalance: $scope.totalVPAmountRefunded,
                    cutomerName: $scope.customerSearchText,
                    orderType: $scope.invoiceType,
                    memo: $scope.memo,
                    pax:$scope.pax,
                    hiPosServiceCharge: $scope.hiposServiceCharge,
                    hiposServiceChargeAmt: $scope.hiposServiceChargeAmt,
                    totalCheckOutamt: totalCheckOutamt,
                    paymentType: paymentType,
                    totalTaxAmt: parseFloat($scope.totalTaxAmt).toFixed(2),
                    totalTenderedAmount: parseFloat($scope.totalPaidAmt).toFixed(2),
                    customerId: $scope.customerId,
                    taxType: $scope.fullSimplTax,
                    roundingOffValue: parseFloat($scope.roundingOffValue).toFixed(2),
                    employeeName: $scope.employeeSearchText,
                    agentIdOfInvoice: $scope.agentSearchText,
                    orderNo: $scope.orderNo,
                    siNo: $scope.siNo,
                    discountCode: $scope.couponNameText,
                    invoiceType: 'restaurant',
                    discountType: $scope.discountType,
                    discountAmount: $scope.totalVPDiscount,
                    billWiseDiscount: $scope.disAmtIn,
                    discountAmtInPercentage: discPercentage,
                    mobileNo: $scope.customerMobileNo,
                    discType: $scope.discType,
                    smsType: $scope.smsType,
                    olInvPayId:$scope.onlineInvoicePay,
                    airPayId:$scope.swipePay,
                    returnReason: JSON.stringify($scope.removedItemList)
                };
                console.log(data);
                return data;
            };

            $scope.openSplitSelectpayment = function () {
                $scope.getTotalAmtForSelectedItems($scope.splitTable2List);
               if (angular.isUndefined($scope.totalCheckOutamt) || $scope.totalCheckOutamt <= 0 || $scope.splitTable2List.length <= 0) {
                    Notification.error({
                        message: 'At least One item has to be selected',
                        positionX: 'center',
                        delay: 2000
                    });
                } else if (!$scope.isValidatedData()) {
                    Notification.warning({
                        message: 'Qty should not be blank or zero',
                        positionX: 'center',
                        delay: 2000
                    });
                } else {
                    $scope.invokeOrderId = 1;
                    // $http.post($scope.retailServerURL + '/validateCheckout?invokeOrderId=' + $scope.invokeOrderId,
                    //     angular.toJson($scope.populateSaveSplitSIData("", ""))).then(function (response) {
                    //     var data = response.data;
                    //     $scope.itemsNoStockList = data;
                    //     if ($scope.itemsNoStockList !== null && $scope.itemsNoStockList.length > 0) {
                    //         $("#ItemsNoStockListModel").modal('show');
                    //     }
                    //     else {
                            if ($scope.orderToKitchenBuffer.length > 0) {
                                var confirm = $window.confirm("Do you want to clear KOT Orders?");
                                if (confirm == false) {
                                    return false;
                                } else {
                                    //reset kitchen order buffer
                                    $scope.orderToKitchenBuffer = []
                                }
                            }
                            else {
                                if (angular.isUndefined($scope.totalCheckOutamt) || $scope.totalCheckOutamt === 0) {
                                    Notification.error({
                                        message: 'Check out amount can not be zero',
                                        positionX: 'center',
                                        delay: 2000
                                    });
                                } else if (!$scope.isValidatedData()) {
                                    Notification.error({
                                        message: 'Qty should not be blank',
                                        positionX: 'center',
                                        delay: 2000
                                    });
                                } else {
                                    $scope.getTotalAmtForSelectedItems($scope.splitTable2List);
                                    $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
                                    $scope.paymentButton = "Restaurant";
                                    $scope.selectedItemsList=$scope.splitTable2List;
                                    $("#paymentNew1").modal('show');
                                }
                                ;
                            }
                        // }
                    // }, function (error) {
                    //     Notification.error({message: data.message, positionX: 'center', delay: 2000});
                    // })
                }

            }
            $scope.totalVPAfterDiscount = 0;
            $scope.isValidatedData = function () {
                $scope.isValide = true;
                angular.forEach($scope.selectedItemsList, function (item, index) {
                    if (angular.isUndefined(item.unitPrice) || item.unitPrice === '' || parseFloat(item.unitPrice) <0) {
                        $scope.isValide = false;
                    } else if (angular.isUndefined(item.qty) || item.qty === '' || !parseFloat(item.qty) > 0 || parseFloat(item.qty) <= 0) {
                        $scope.isValide = false;
                    }
                    else if (angular.isUndefined(item.amtinclusivetax) || item.amtinclusivetax === ''  || parseFloat(item.amtinclusivetax) < 0) {
                        $scope.isValide = false;
                    } else if (angular.isUndefined(item.itemName) || item.itemName === '') {
                        $scope.isValide = false;
                    }
                });
                return $scope.isValide;
            };
            $scope.getTotalAmtForSelectedItems = function (list) {
                var totalAmt = 0.00;
                var totalTaxAmt = 0.00;
                var totalDiscountAmt = 0.00;
                var cessTotalTaxAmt = 0.00;
                var totalQty = 0.00;
                var totalAmtExamt = 0.00;
                var itemCount = 0.00;
                angular.forEach(list, function (item, index) {
                    item.id=index;
                    totalAmt += parseFloat(item.amtinclusivetax);
                    totalAmtExamt += parseFloat(item.amtexclusivetax);
                    if (!angular.isUndefined(item.taxamt)) {
                        totalTaxAmt += parseFloat(item.taxamt);
                    }
                    if (!angular.isUndefined(item.cessTaxAmt)) {
                        cessTotalTaxAmt += parseFloat(item.cessTaxAmt);
                    }
                    if (!angular.isUndefined(item.discountAmt)) {
                        totalDiscountAmt += parseFloat(item.discountAmt);
                    }
                    if (!angular.isUndefined(item.returnQty)) {
                        totalQty += parseFloat(item.returnQty);
                    }
                    if (!angular.isUndefined(item.qty)) {
                        itemCount += parseInt(item.qty);
                    }
                });
                $scope.totalAmtExamt = parseFloat(totalAmtExamt).toFixed(2);
                $scope.totalAmtEx = parseFloat(totalAmtExamt).toFixed(2);
                if($scope.configurationData.discountType=='billWise'){
                    $scope.totalAmtExamt = parseFloat(totalAmtExamt).toFixed(2);
                    if($scope.disAmtIn==undefined){
                        $scope.disAmtIn=0;
                    }
                    if($scope.disAmtIn>0) {
                        $scope.totalAmtEx = parseFloat(totalAmtExamt - $scope.disAmtIn).toFixed(2);
                        var taxPercent = parseInt($scope.configurationData.taxId);
                        $scope.totalAmtEx = parseFloat($scope.totalAmtEx).toFixed(2);
                        totalDiscountAmt = totalDiscountAmt + parseFloat($scope.disAmtIn);
                        $scope.billWiseTax = parseFloat($scope.totalAmtEx).toFixed(2) * (parseFloat(taxPercent) / 100);
                        totalTaxAmt = $scope.billWiseTax;
                        $scope.totalAmtExamt = parseFloat($scope.totalAmtEx).toFixed(2);
                        totalTaxAmt = parseFloat(totalTaxAmt).toFixed(2);
                        $scope.totalAmtExamt = parseFloat($scope.totalAmtExamt).toFixed(2);
                        totalAmt = parseFloat(totalTaxAmt) + parseFloat($scope.totalAmtEx);
                    }
                }
                $scope.totalCheckOutamt = parseFloat(totalAmt).toFixed(2);
                if($scope.incServiceCharge==true){
                    var serviceCharge=$scope.totalAmtEx * $scope.hiposServiceCharge;
                    serviceCharge=serviceCharge/100;
                    $scope.hiposServiceChargeAmt=serviceCharge.toFixed(2);
                    $scope.totalCheckOutamt = parseFloat($scope.totalCheckOutamt) + parseFloat(serviceCharge);
                }else {
                    $scope.hiposServiceChargeAmt=0;
                }
                $scope.totalBeforDiscount = parseFloat(totalAmt).toFixed(2);
                $scope.tenderedAmount = parseFloat(totalAmt).toFixed(2);
                $scope.totalDiscount = parseFloat(totalDiscountAmt).toFixed(2);
                $scope.totalVPDiscount = parseFloat(totalDiscountAmt).toFixed(2);
                $scope.discountAmt = parseFloat(totalDiscountAmt).toFixed(2);
                $scope.totalTaxAmt = parseFloat(totalTaxAmt).toFixed(2);
                $scope.cessTotalTaxAmt = parseFloat(cessTotalTaxAmt).toFixed(2);
                $scope.returnQty = parseFloat(totalQty).toFixed(2);
                var totalAfterDiscount = parseFloat(totalAmt);
                $scope.totalAfterDiscount = parseFloat(totalAfterDiscount).toFixed(2);
                $scope.totalCheckOutamt = parseFloat($scope.totalCheckOutamt).toFixed(2);
                $scope.itemCount = parseFloat(itemCount).toFixed(2);
                $scope.totalCPBeforDiscount = $scope.totalCheckOutamt;
                $scope.totalAPBeforDiscount = $scope.totalCheckOutamt;
                $scope.totalIPBeforDiscount = $scope.totalCheckOutamt;
                $scope.totalCCPBeforDiscount = $scope.totalCheckOutamt;
                $scope.totalVPBeforDiscount = Math.round($scope.totalCheckOutamt);
                $scope.totalVPAfterAllDeductions =$scope.totalCheckOutamt;
                // if($scope.discountType=='billWise'){
                //     $scope.disAmtInPer=list[0].discountConfigAmt;
                // }
                $scope.totalCPAfterDiscount = $scope.totalCPBeforDiscount - $scope.totalCPDiscount;
                $scope.totalAPAfterDiscount = $scope.totalAPBeforDiscount - $scope.totalAPDiscount;
                $scope.totalIPAfterDiscount = $scope.totalIPBeforDiscount - $scope.totalIPDiscount;
                $scope.totalCCPAfterDiscount = $scope.totalCCPBeforDiscount - $scope.totalCCPDiscount;
            };
            $http.post('/hipos/getConfigureData').then(function (successResponse) {
                $scope.configurationData = successResponse.data;
                if($scope.configurationData.taxId==undefined||$scope.configurationData.taxId==null||$scope.configurationData.taxId==""){
                    $scope.configurationData.taxId=0;
                }
                $scope.restuarantType = ($scope.configurationData.copytokot === true) ? 'qos' : 'nonqos';
                $scope.printPreview = $scope.configurationData.printPreview;
                $scope.discountType = $scope.configurationData.discountType;
                console.log('configuration data..');
                console.log($scope.configurationData);
                if($rootScope.pageonload!=false) {
                    if ($scope.configurationData.restaurant == 'Delivery' || $scope.configurationData.restaurant == 'TakeAway') {
                        $scope.setOrderType($scope.configurationData.restaurant);
                    }
                    if ($scope.configurationData.restaurant == 'onlineDelivery') {
                        $scope.openOnlinePage($scope.configurationData.restaurant);
                    }
                    if ($scope.configurationData.restaurant == 'digiOrders') {
                        $scope.openDigiOrders($scope.configurationData.restaurant);
                    }
                }
            }, function (failureResponse) {

            });
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
            $scope.isUndefinedOrNull = function (data) {
                return (angular.isUndefined(data) || data === null || data === '' || data === 'null');
            };
            /*********** ** ** PAYMENT-NAME && PAYMENT-TYPE ** ** *********************/
            $scope.paymentList = [];
            $scope.getPaymentTypesList1 = function (paymentTypeIndex, payIndex, flag) {
                if (angular.isUndefined(paymentTypeIndex)) {
                    paymentTypeIndex = 1;
                }
                $http.get($scope.hiposServerURL+ '/getPaymentTypes').then(function (response) {
                    var data = response.data;
                    $scope.getPaymentTypes = response.data;
                    $scope.paymentList[payIndex] = angular.copy(data);
                    console.log($scope.paymentList[payIndex]);
                    $scope.paymentTypeIndex = paymentTypeIndex;
                    /************ ** ** DEFAULT PAYMENT TYPE ** ** *****************************/
                    angular.forEach(data, function (value, key) {
                        if (value.defaultType == 'true') {
                            //**if default value is present in dropdown
                            $scope.paymentDropdown[payIndex].DefaultPaymentmethodId = value.paymentmethodId;
                            $scope.paymentDropdown[payIndex].bankAccount = value.accountMaster;
                            $scope.paymentDropdown[payIndex].bankAccountId = value.accountMasterId;
                            $scope.DEFAULT_PAYMENT_TYPE = value.paymentmethodType;
                            $('.bankInfoHide').css('display', 'block');
                        } else if (value.defaultType == '' && value.defaultType == null && value.defaultType == undefined) {
                            //**if default value is not present in dropdown
                            $('.bankInfoHide').css('display', 'none');
                        }
                    });
                    var paymentmethodType = $scope.DEFAULT_PAYMENT_TYPE;
                    if (flag == 'ONLOAD') {
                        //alert(paymentmethodType);
                        if (paymentmethodType == 'Cash') {
                            $('.bankInfoHide').css('display', 'block');
                            $scope.CASHPAYMENT = true;
                            $('#PaymentTypeCashpaymentDropdown' + paymentTypeIndex).show();
                            $('#PaymentTypeCashpaymentDropdown' + paymentTypeIndex).siblings().hide();
                        } else {
                            $scope.CASHPAYMENT = false;
                            $('#PaymentTypeCashpaymentDropdown' + paymentTypeIndex).hide();
                        }
                        if (paymentmethodType == 'airPay') {
                            $('.bankInfoHide').css('display', 'block');
                            $scope.AIRPAYPAYMENT = true;
                            $('#PaymentTypeAirPaypaymentDropdown' + paymentTypeIndex).show();
                            $('#PaymentTypeAirPaypaymentDropdown' + paymentTypeIndex).siblings().hide();
                        } else {
                            $scope.AIRPAYPAYMENT = false;
                            $('#PaymentTypeAirPaypaymentDropdown' + paymentTypeIndex).hide();
                        }
                        if (paymentmethodType == 'invoicePay') {

                            $('.bankInfoHide').css('display', 'block');
                            $scope.INVPAYMENT = true;
                            $('#PaymentTypeInvpaymentDropdown' + paymentTypeIndex).show();
                            $('#PaymentTypeInvpaymentDropdown' + paymentTypeIndex).siblings().hide();
                        } else {
                            $scope.INVPAYMENT = false;
                            $('#PaymentTypeInvpaymentDropdown' + paymentTypeIndex).hide();
                        }
                        if (paymentmethodType == 'Card') {
                            $('.bankInfoHide').css('display', 'block');
                            $scope.CARDPAYMENT = true;
                            $('#PaymentTypeCardpaymentDropdown' + paymentTypeIndex).show();
                            $('#PaymentTypeCardpaymentDropdown' + paymentTypeIndex).siblings().hide();
                        } else {
                            $scope.CARDPAYMENT = false;
                            $('#PaymentTypeCardpaymentDropdown' + paymentTypeIndex).hide();
                        }
                        if (paymentmethodType == 'Bank') {
                            $('.bankInfoHide').css('display', 'block');
                            $scope.OTHERSPAYMENT = true;
                            $('#PaymentTypeBankpaymentDropdown' + paymentTypeIndex).show();
                            $('#PaymentTypeBankpaymentDropdown' + paymentTypeIndex).siblings().hide();
                        } else {
                            $scope.OTHERSPAYMENT = false;
                            $('#PaymentTypeBankpaymentDropdown' + paymentTypeIndex).hide();
                        }
                        if (paymentmethodType == 'Voucher') {
                            $('.bankInfoHide').css('display', 'block');
                            $scope.VOUCHERPAYMENT = true;
                            $('#PaymentTypeVoucherpaymentDropdown' + paymentTypeIndex).show();
                            $('#PaymentTypeVoucherpaymentDropdown' + paymentTypeIndex).siblings().hide();
                        } else {
                            $scope.VOUCHERPAYMENT = false;
                            $('#PaymentTypeVoucherpaymentDropdown' + paymentTypeIndex).hide();
                        }
                    } else if (flag == 'ADDNEWSECTION') {
                        $('.bankInfoHide').css('display', 'block');
                    }

                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })
            };
            $scope.getInventoryLocationList = function () {
                $http.get($scope.hiposServerURL + "/" + $scope.customer + '/addUserAccountSetup').then(function (response) {
                    var data = response.data;
                    $scope.inventoryLocationList = data;
                    $scope.fromLocation = parseInt($scope.userLocationId);
                    $scope.dailyReportlocation = parseInt($scope.userLocationId);
                    if($rootScope.selectedLocation!=undefined) {
                        $scope.fromLocation = $rootScope.selectedLocation;
                        $scope.dailyReportlocation = $rootScope.selectedLocation;
                    }
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })
            };
            $scope.getPageLoadDataRest = function () {
                $http.get('/hipos/getPageOnloadData').then(function (response) {
                    var data = response.data;
                    console.log(data);
                    if (!angular.isUndefined(data) && data !== null) {
                        $scope.userRights = data.userRights;
                        $scope.getTableList();
                        $scope.customerList = data.customers;
                        $scope.companyName = data.companyName;
                        $scope.fullUserName = data.fullUserName;
                        $scope.userLoginId = data.userLoginId;
                        $scope.password = data.password;
                        $scope.user_access_rights = data.userAccessRights;
                        // $scope.fullSimplTax = $scope.taxTypes[0].value;
                        if(data.locationRights =="true") {
                            $scope.restrictLoc = "false";
                        }
                        $scope.taxList = data.taxList;
                        $scope.userLocationId = data.userLocationId;
                        $scope.itemCategoryNames = data.itemCategorys;
                        // if (data.hiPosServiceCharge !== null) {
                        //     $scope.hiposServiceCharge = data.hiPosServiceCharge.servicePercentage;
                        //     $scope.serviceChargeName = data.hiPosServiceCharge.serviceChargeName;
                        // }
                        $scope.agentList = data.agentList;
                        // $scope.getInventoryLocationList();
                        $scope.employeeList = data.employeeList;
                        $scope.orderType($rootScope.orderTypeName);
                        $scope.companyLogoPath = location + "/" + data.companyLogoPath;
                        if (data.defaultEmployee != null && angular.isUndefined($scope.waiterSearchText)) {
                            $scope.appendEmployeeSelected(data.defaultEmployee);
                        }
                    }
                }), function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                }
            };
            $scope.getPageLoadDataRest();
            $scope.getPaymentTypesList1(1, 0, 'ONLOAD'); //called ONLOAD
            //ON CHANGE DROPDOWN FUNC TO SELECT PAYMENT TYPE
            $scope.selectPaymentType = function (selectedOption, ddpStr, payInd, list) {
                if(payInd==$scope.paymentDropdown.length&&payInd==1){
                    $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];
                }else if(payInd==$scope.paymentDropdown.length){
                    $scope.paymentDropdown.splice(payInd, 1);
                }
                $scope.getPaymentTypesAmount();
                angular.forEach(list, function (value, key) {
                    if (value.paymentmethodId == selectedOption) {
                        $scope.paymentDropdown[payInd - 1].DEFAULT_PAYMENT_TYPE = value.paymentmethodType;
                        $scope.paymentDropdown[payInd - 1].DefaultPaymentmethodId = value.paymentmethodId;
                    }
                })
                $scope.totalPaidAmt=$scope.totalPaymentAmount;
                $('.bankInfoHide').css('display', 'block');
                var data = $scope.paymentList[payInd - 1];
                //$scope.selectedPaymentType = [];
                angular.forEach(data, function (value, key) {
                    if (value.paymentmethodId == selectedOption) {
                        var selectedPaymentType = value.paymentmethodType;
                        if (selectedPaymentType == 'Cash') {
                            $scope.CASHPAYMENT = true;
                            if(parseFloat($scope.totalPaidAmt)>0){
                                $scope.paymentDropdown[payInd-1].totalCPAmountTendered = $scope.totalVPBeforDiscount-parseFloat($scope.totalPaidAmt);
                            }else {
                                $scope.paymentDropdown[payInd-1].totalCPAmountTendered = $scope.totalVPBeforDiscount;
                            }
                            $('#PaymentTypeCash' + ddpStr).show();
                            $('#PaymentTypeCash' + ddpStr).siblings().hide();
                            $scope.OnChangeMultiPayament($scope.paymentDropdown[payInd-1].totalCPAmountTendered,Math.round($scope.totalCheckOutamt),selectedPaymentType,0);

                            /***remove all CASH TYPE payments from drop down array***/
                            function filterPaymentmethodType(pay) {
                                return pay.paymentmethodType != 'Cash';
                            }

                            var filteredData = $scope.paymentList[payInd - 1].filter(filterPaymentmethodType);
                            $scope.paymentList[payInd] = filteredData;
                        } else {
                            $scope.CASHPAYMENT = false;
                            $('#PaymentTypeCash' + ddpStr).hide();
                        }
                        if (selectedPaymentType == 'airPay') {
                            $scope.AIRPAYPAYMENT = true;
                            if(parseFloat($scope.totalPaidAmt)>0){
                                $scope.paymentDropdown[payInd-1].totalAPAmountTendered = $scope.totalVPBeforDiscount-parseFloat($scope.totalPaidAmt);
                                $scope.paymentDropdown[payInd-1].airPaymobile = $scope.customerContactText;
                            }else {
                                $scope.paymentDropdown[payInd-1].totalAPAmountTendered = $scope.totalVPBeforDiscount;
                                $scope.paymentDropdown[payInd-1].airPaymobile = $scope.customerContactText;
                            }
                            $('#PaymentTypeAirPay' + ddpStr).show();
                            $('#PaymentTypeAirPay' + ddpStr).siblings().hide();
                            $scope.OnChangeMultiPayament($scope.paymentDropdown[payInd-1].totalAPAmountTendered,Math.round($scope.totalCheckOutamt),selectedPaymentType,0);

                            /***remove all AIR-PAY TYPE payments from drop down array***/
                            function filterPaymentmethodType(pay) {
                                return pay.paymentmethodType != 'airPay';
                            }

                            var filteredData = $scope.paymentList[payInd - 1].filter(filterPaymentmethodType);
                            $scope.paymentList[payInd] = filteredData;
                        } else {
                            $scope.AIRPAYPAYMENT = false;
                            $('#PaymentTypeAirPay' + ddpStr).hide();
                        }
                        if (selectedPaymentType == 'invoicePay') {
                            $scope.INVPAYMENT = true;
                            if(parseFloat($scope.totalPaidAmt)>0){
                                $scope.paymentDropdown[payInd-1].totalINVAmountTendered = $scope.totalVPBeforDiscount-parseFloat($scope.totalPaidAmt);
                                $scope.paymentDropdown[payInd-1].invmobile = $scope.customerContactText;
                                $scope.paymentDropdown[payInd-1].invmail = $scope.customerEmailText;
                            }else {
                                $scope.paymentDropdown[payInd-1].totalINVAmountTendered = $scope.totalVPBeforDiscount;
                                $scope.paymentDropdown[payInd-1].invmobile = $scope.customerContactText;
                                $scope.paymentDropdown[payInd-1].invmail = $scope.customerEmailText;
                            }
                            $('#PaymentTypeINV' + ddpStr).show();
                            $('#PaymentTypeINV' + ddpStr).siblings().hide();
                            $scope.OnChangeMultiPayament($scope.paymentDropdown[payInd-1].totalINVAmountTendered,Math.round($scope.totalCheckOutamt),selectedPaymentType,0);

                            /***remove all INVOICE TYPE payments from drop down array***/
                            function filterPaymentmethodType(pay) {
                                return pay.paymentmethodType != 'invoicePay';
                            }

                            var filteredData = $scope.paymentList[payInd - 1].filter(filterPaymentmethodType);
                            $scope.paymentList[payInd] = filteredData;
                        } else {
                            $scope.INVPAYMENT = false;
                            $('#PaymentTypeINV' + ddpStr).hide();
                        }
                        if (selectedPaymentType == 'Card') {
                            if($scope.totalPaidAmt>0){
                                $scope.paymentDropdown[payInd-1].cardAmount = $scope.totalVPBeforDiscount-parseFloat($scope.totalPaidAmt);
                            }else {
                                $scope.paymentDropdown[payInd-1].cardAmount = $scope.totalVPBeforDiscount;
                            }
                            $scope.CARDPAYMENT = true;
                            $('#PaymentTypeCard' + ddpStr).show();
                            $('#PaymentTypeCard' + ddpStr).siblings().hide();
                            $scope.OnChangeMultiPayament($scope.paymentDropdown[payInd-1].cardAmount,Math.round($scope.totalCheckOutamt),selectedPaymentType,0);
                        } else {
                            $scope.CARDPAYMENT = false;
                            $('#PaymentTypeCard' + ddpStr).hide();
                        }
                        if (selectedPaymentType == 'Bank') {
                            $scope.OTHERSPAYMENT = true;
                            $scope.ind=payInd-1;
                            if($scope.totalPaidAmt>0){
                                $scope.paymentDropdown[payInd-1].bankAmount = $scope.totalVPBeforDiscount-parseFloat($scope.totalPaidAmt);
                            }else {
                                $scope.paymentDropdown[payInd-1].bankAmount = $scope.totalVPBeforDiscount;
                            }
                            $scope.OnChangeMultiPayament($scope.paymentDropdown[payInd-1].bankAmount,Math.round($scope.totalCheckOutamt),selectedPaymentType,0);
                            $('#PaymentTypeBank' + ddpStr).show();
                            $('#PaymentTypeBank' + ddpStr).siblings().hide();
                        } else {
                            $scope.OTHERSPAYMENT = false;
                            $('#PaymentTypeBank' + ddpStr).hide();
                        }
                        if (selectedPaymentType == 'Voucher') {
                            if($scope.totalPaidAmt>0){
                                $scope.paymentDropdown[payInd-1].voucherAmt = $scope.totalVPBeforDiscount-parseFloat($scope.totalPaidAmt);
                            }else {
                                $scope.paymentDropdown[payInd-1].voucherAmt = $scope.totalVPBeforDiscount;
                            }
                            $scope.OnChangeMultiPayament($scope.paymentDropdown[payInd-1].voucherAmt,Math.round($scope.totalCheckOutamt),selectedPaymentType,0);
                            $scope.VOUCHERPAYMENT = true;
                            $('#PaymentTypeVoucher' + ddpStr).show();
                            $('#PaymentTypeVoucher' + ddpStr).siblings().hide();
                        } else {
                            $scope.VOUCHERPAYMENT = false;
                            $('#PaymentTypeVoucher' + ddpStr).hide();
                        }
                        if (selectedPaymentType == 'Redeem') {
                            $scope.REDEEMPAYMENT = true;
                            $('#PaymentTypeRedeem' + ddpStr).show();
                            $('#PaymentTypeRedeem' + ddpStr).siblings().hide();
                        } else {
                            $scope.REDEEMPAYMENT = false;
                            $('#PaymentTypeRedeem' + ddpStr).hide();
                        }
                    }
                });
            };
            $scope.addNewSection = function (payInd) {
                //add more button will not work until you choose something in previous dropdown
                if (angular.isUndefined($scope.paymentDropdown[payInd - 1].DefaultPaymentmethodId)) {
                    Notification.error({
                        message: 'Please select something from the previous dropdown',
                        positionX: 'center',
                        delay: 2000
                    });
                } else {
                    var newddpIndex = $scope.paymentDropdown.length + 1;
                    $scope.paymentDropdown.push({'id': 'paymentDropdown' + newddpIndex});
                    $scope.getPaymentTypesList1(newddpIndex, $scope.paymentDropdown.length, 'ADDNEWSECTION');
                    /***remove all CASH TYPE payments from drop down array***/
                    if ($scope.CASHPAYMENT == 'true' || $scope.CASHPAYMENT == true) {
                        function filterPaymentmethodType(pay) {
                            return pay.paymentmethodType != 'Cash';
                        }

                        var filteredData = $scope.paymentList[0].filter(filterPaymentmethodType);
                        $scope.paymentList[payInd] = filteredData;
                    }
                    else if($scope.AIRPAYPAYMENT == true || $scope.INVPAYMENT == true){
                        function filterPaymentmethodType(pay) {
                            return pay.paymentmethodType != 'airPay' && pay.paymentmethodType !='invoicePay';
                        }

                        var filteredData = $scope.paymentList[0].filter(filterPaymentmethodType);
                        $scope.paymentList[payInd] = filteredData;
                    }
                    else {
                        $scope.paymentList[payInd] = $scope.paymentList[payInd - 1];
                    }
                }
            };
            $scope.removePayments = "";
            $scope.ddpIndex = 0;
            $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];
            //$scope.paymentDropdown = $scope.paymentDropdown;
            $scope.removeSection = function (index) {
                if(index>0){
                    $scope.paymentDropdown.splice(index, 1);
                    $scope.paymentDropdown = $scope.paymentDropdown;
                    $scope.removePayments = "removeField";
                    $scope.OnChangeMultiPayament('', currentValue = undefined, 'REMOVE', index);
                }
            };
            $scope.getPaymentTypesAmount=function(){
                var totalAmount = parseFloat(0.00);
                angular.forEach($scope.paymentDropdown, function (payment, key) {
                    if (payment.DEFAULT_PAYMENT_TYPE=='Cash') {
                        totalAmount = totalAmount + parseFloat(payment.totalCPAmountTendered);
                    } else if (payment.DEFAULT_PAYMENT_TYPE=='Card') {
                        totalAmount = totalAmount + parseFloat(payment.cardAmount);
                    } else if (payment.DEFAULT_PAYMENT_TYPE=='Bank') {
                        totalAmount = totalAmount + parseFloat(payment.bankAmount);
                    } else if (payment.DEFAULT_PAYMENT_TYPE=='Voucher') {
                        totalAmount = totalAmount + parseFloat(payment.voucherAmt);
                    } else if(payment.DEFAULT_PAYMENT_TYPE=='airPay'){
                        totalAmount = totalAmount + parseFloat(payment.totalAPAmountTendered);
                    }
                    else if(payment.DEFAULT_PAYMENT_TYPE=='invoicePay'){
                        totalAmount = totalAmount + parseFloat(payment.totalINVAmountTendered);
                    }
                });
                $scope.totalPaymentAmount=totalAmount;
            }
            $scope.OnChangeMultiPayament = function (oldValue, currentValue, onFocusInputName, paymentDropdownIndex) {
                if (parseFloat($scope.totalTCSAmountTendered) > 0) {
                    $scope.totalTDSAmountTendered = parseFloat(0.00);
                }
                if (parseFloat($scope.totalTDSAmountTendered) > 0) {
                    $scope.totalTCSAmountTendered = parseFloat(0.00);
                }
                currentValue = parseFloat(1);
                $scope.getPaymentTypesAmount();
                var totalAmount = parseFloat($scope.totalPaymentAmount).toFixed(2);
                if ($scope.removePayments == "removeField") {
                    currentValue = undefined;
                    $scope.removePayments = "";
                }
                if (totalAmount.toString() == "NaN") {
                    totalAmount = parseFloat(0);
                }
                if ($scope.totalTCSAmountTendered == undefined) {
                    $scope.totalTCSAmountTendered = parseFloat(0.00);
                }
                if ($scope.totalTDSAmountTendered == undefined) {
                    $scope.totalTDSAmountTendered = parseFloat(0.00);
                }
                var totalPaymentAmount = totalAmount + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                var onFocusedElement = onFocusInputName;
                var tempTotalVPAfterDiscount = parseFloat(0.00);
                var tempTotalVPAmountRefunded = parseFloat(0.00);
                switch (onFocusedElement) {
                    case "TCS":
                        if (angular.isUndefined(currentValue) || currentValue === "" || !(angular.isNumber(parseFloat(currentValue)))) {
                            $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                            return;
                        } else if (parseFloat(totalPaymentAmount) > parseFloat($scope.totalVPBeforDiscount)) {
                            Notification.info({
                                message: 'TCS Amount can not be greater than to be paid amount',
                                positionX: 'center',
                                delay: 2000
                            });
                            $scope.totalTCSAmountTendered = parseFloat(0);
                            $scope.getPaymentTypesAmount();
                            var paymentAmount = $scope.totalPaymentAmount + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                            $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                            return;
                        }
                        break;
                    case "TDS":
                        if (angular.isUndefined(currentValue) || currentValue === "" ||
                            !(angular.isNumber(parseFloat(currentValue)))) {
                            $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                            return;
                        } else if (parseFloat(totalPaymentAmount) > parseFloat($scope.totalVPBeforDiscount)) {
                            Notification.info({
                                message: 'TDS Amount can not be greater than to be paid amount',
                                positionX: 'center',
                                delay: 2000
                            });
                            $scope.totalTDSAmountTendered = parseFloat(0);
                            $scope.getPaymentTypesAmount();
                            var paymentAmount = $scope.totalPaymentAmount + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                            $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                            return;
                        }
                        break;
                    case "BANK":
                        if (angular.isUndefined(currentValue) || currentValue === "" ||
                            !(angular.isNumber(parseFloat(currentValue)))) {
                            $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                            return;
                        }
                        else if (parseFloat(totalPaymentAmount) > parseFloat($scope.totalVPBeforDiscount)) {
                            Notification.info({
                                message: 'Bank Amount can not be greater than to be paid amount',
                                positionX: 'center',
                                delay: 2000
                            });
                            var paymentAmount = $scope.totalPaymentAmount -$scope.paymentDropdown[paymentDropdownIndex].bankAmount+ parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                            $scope.paymentDropdown[paymentDropdownIndex].bankAmount = parseFloat(0);
                            $scope.totalPaidAmt = parseFloat(paymentAmount).toFixed(2);
                            $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount).toFixed(2) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                            return;
                        }
                        break;
                    case "VOUCHER":
                        if (angular.isUndefined(currentValue) || currentValue === "" ||
                            !(angular.isNumber(parseFloat(currentValue)))) {
                            $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                            return;
                        }
                        else if (parseFloat(totalPaymentAmount) > parseFloat($scope.totalVPBeforDiscount)) {
                            Notification.info({
                                message: 'Voucher Amount can not be greater than to be paid amount',
                                positionX: 'center',
                                delay: 2000
                            });
                            angular.forEach($scope.paymentDropdown, function (payment, key) {
                                if ('totalCPAmountTendered' in payment) {
                                    totalAmount = totalAmount + parseFloat(payment.totalCPAmountTendered);
                                    currentValue = parseFloat(1);
                                } else if ('cardAmount' in payment) {
                                    totalAmount = totalAmount + parseFloat(payment.cardAmount);
                                    currentValue = parseFloat(1);
                                } else if ('bankAmount' in payment) {
                                    totalAmount = totalAmount + parseFloat(payment.bankAmount);
                                    currentValue = parseFloat(1);
                                } else if('totalAPAmountTendered' in payment){
                                    totalAmount = totalAmount + parseFloat(payment.totalAPAmountTendered);
                                    currentValue = parseFloat(1);
                                }
                                else if('totalINVAmountTendered' in payment){
                                    totalAmount = totalAmount + parseFloat(payment.totalINVAmountTendered);
                                    currentValue = parseFloat(1);
                                }
                            });
                            var paymentAmount = $scope.totalPaymentAmount-$scope.paymentDropdown[paymentDropdownIndex].voucherAmt + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                            $scope.paymentDropdown[paymentDropdownIndex].voucherAmt = parseFloat(0);
                            $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                            return;
                        }
                        break;
                    case "CARD":
                        if (angular.isUndefined(currentValue) || currentValue === "" ||
                            !(angular.isNumber(parseFloat(currentValue)))) {
                            $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                            return;
                        }
                        else if (parseFloat(totalPaymentAmount) > parseFloat($scope.totalVPBeforDiscount)) {
                            Notification.info({
                                message: 'Card  Amount can not be greater than to be paid amount',
                                positionX: 'center',
                                delay: 2000
                            });
                            var paymentAmount = $scope.totalPaymentAmount-$scope.paymentDropdown[paymentDropdownIndex].cardAmount + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                            $scope.paymentDropdown[paymentDropdownIndex].cardAmount = parseFloat(0);
                            $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                            return;
                        }
                        break;
                    case "CASH":
                        if (angular.isUndefined(currentValue) || currentValue === "" ||
                            !(angular.isNumber(parseFloat(currentValue)))) {
                            $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                            return;
                        }
                        break;
                    case "INVAMT":
                        if (angular.isUndefined(currentValue) || currentValue === "" ||
                            !(angular.isNumber(parseFloat(currentValue)))) {
                            $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                            $scope.paymentDropdown[paymentDropdownIndex].invmobile = $scope.customerContactText;
                            $scope.paymentDropdown[paymentDropdownIndex].invmail = $scope.customerEmailText;
                            return;
                        }
                        break;
                    case "AirPay":
                        if (angular.isUndefined(currentValue) || currentValue === "" ||
                            !(angular.isNumber(parseFloat(currentValue)))) {
                            $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                            $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                            $scope.paymentDropdown[paymentDropdownIndex].airPaymobile = $scope.customerContactText;
                            return;
                        }
                        break;
                    case "REMOVE":
                        var totalAmount = parseFloat(0.00);
                        angular.forEach($scope.paymentDropdown, function (payment, key) {
                            if (payment.DEFAULT_PAYMENT_TYPE=='Cash') {
                                totalAmount = totalAmount + parseFloat(payment.totalCPAmountTendered);
                            } else if (payment.DEFAULT_PAYMENT_TYPE=='Card') {
                                totalAmount = totalAmount + parseFloat(payment.cardAmount);
                            } else if (payment.DEFAULT_PAYMENT_TYPE=='Bank') {
                                totalAmount = totalAmount + parseFloat(payment.bankAmount);
                            } else if (payment.DEFAULT_PAYMENT_TYPE=='Voucher') {
                                totalAmount = totalAmount + parseFloat(payment.voucherAmt);
                            } else if(payment.DEFAULT_PAYMENT_TYPE=='airPay'){
                                totalAmount = totalAmount + parseFloat(payment.totalAPAmountTendered);
                            }
                            else if(payment.DEFAULT_PAYMENT_TYPE=='invoicePay'){
                                totalAmount = totalAmount + parseFloat(payment.totalINVAmountTendered);
                            }
                            currentValue = parseFloat(1);
                        });
                        break;
                }
                if (totalAmount.toString() == "NaN") {
                    totalAmount = parseFloat(0);
                }
                var paymentAmount = totalAmount;
                $scope.totalVPAmountRefunded = parseFloat(paymentAmount).toFixed(2) - parseFloat($scope.totalVPBeforDiscount).toFixed(2);
                $scope.totalPaidAmt = parseFloat(paymentAmount).toFixed(2);
            };
            $scope.validateVoucher=function (voucherNo,amt,voucherType) {
                amt=$scope.totalVPBeforDiscount;
                angular.forEach($scope.getPaymentTypes,function (val,key) {
                    if(val.paymentmethodName=='Discount Voucher'){
                        $scope.valVoucher=val.validateVoucher;
                    }
                })
                if($scope.valVoucher=='true'){
                    $http.post($scope.hiposServerURL + "/" + $scope.customer + '/checkVoucherNo?voucherNo=' + voucherNo+"&voucherType="+voucherType+"&amt="+amt+"&customer="+$scope.customerSearchText).then(function (response) {
                        var data = response.data;
                        $scope.voucherAmt=data;
                        if(data==0){
                            Notification.error({
                                message: 'Voucher No does not exist',
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
                    })
                }
            }

            $scope.getOnlineDelivery = function (status) {
                $scope.stats=status;
                $scope.getOnlineDeliveryList(status);
            };
            $scope.getDigiOrders = function (status) {
                $scope.stats=status;
                $scope.getDigiOrdersList(status);
            };

            $interval(function () {
                $scope.getOnlineDelivery("");
            }, 30000);
            $interval(function () {
                $scope.getDigiOrders("");
            }, 30000);

            $scope.getOnlineDeliveryList = function (status) {
                if ($scope.searchText == undefined) {
                    $scope.searchText = "";
                }
                $http.get('/hipos/getAllRestaurantNotifications?searchText=' + $scope.searchText +"&status="+status).then(function (response) {
                    var data = response.data;
                    if(status==''){
                        $scope.onlineorderlist=data;
                    }
                    if(status=='placed'){
                        $scope.placedList=data;
                    }
                    if(status=='Acknowledged'){
                        $scope.ConfirmList=data;
                    }
                    if(status=='Completed'){
                        $scope.CompletedList=data;
                    }
                    if(status=='Cancelled'){
                        $scope.CancelledList=data;
                    }
                    $scope.onlineorderlist = data;
                    angular.forEach($scope.onlineorderlist, function (val, key) {
                        var object = JSON.parse(val.objectdata);
                        val.customerName = object.order.customer_details.name;
                        val.location = object.order.customer_details.delivery_area;
                        val.amount = object.order.net_amount;
                        val.paymentStatus = object.order.payment_status;
                        val.phone = object.order.customer_details.phone_number;
                        val.orderId = object.order.order_id;
                        val.email = object.order.customer_details.email;
                        val.pincode = object.order.customer_details.pincode;
                        val.address = object.order.customer_details.address;
                        val.city = object.order.customer_details.city;
                        val.country = object.order.customer_details.country;
                        val.itemdetails = object.order.order_items;
                        val.total = object.order.net_amount;
                        val.amountPaid = object.order.amount_paid;
                        val.balance = object.order.amount_balance;
                        val.cashcollect = object.order.cash_to_be_collected;
                        val.paymentmode = object.order.payment_mode;
                        val.restaurantName = object.order.restaurant_name;
                        val.instructions = object.order.instructions;
                        val.charges = object.order.charges;
                        $scope.total=0;
                        angular.forEach(val.itemdetails,function (val,key) {
                            $scope.total= (val.item_quantity * val.item_unit_price)+$scope.total;
                        });
                        val.date = object.order.order_date_time;
                        val.subTotal = $scope.total;
                        val.totalTaxes = object.order.total_taxes;
                        val.totalCharges = object.order.order_level_total_charges;
                        val.itemCharge = object.order.item_level_total_charges;
                        val.itemTaxes = object.order.item_level_total_taxes;
                        val.discount = object.order.discount;
                        if (object.order.createdTime != null) {
                            val.createdTime = new Date(object.order.createdTime);
                        }
                        if (object.order.deliveryTime != null) {
                            val.deliveryTime = new Date(object.order.deliveryTime);
                        }
                    })
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })
            }

            $scope.getDigiOrdersList = function (status) {
                $http.get('/hipos/getAllDigiOrders?status='+status ).then(function (response) {
                    var data = response.data;
                    if (status == 'placed') {
                        $scope.placedList = data;
                    }
                    if (status == 'Acknowledged') {
                        $scope.ConfirmList = data;
                    }
                    if (status == 'Completed') {
                        $scope.CompletedList = data;
                    }
                    if (status == 'Cancelled') {
                        $scope.CancelledList = data;
                    }
                    $scope.digiOrdersList = data;
                    angular.forEach($scope.digiOrdersList, function (val, key) {
                        var object = JSON.parse(val.objectdata);
                        val.customerName = object.order.customer_details.name;
                        val.location = object.order.customer_details.delivery_area;
                        val.amount = object.order.net_amount;
                        val.paymentStatus = object.order.payment_status;
                        val.phone = object.order.customer_details.phone_number;
                        val.orderId = object.order.order_id;
                        val.email = object.order.customer_details.email;
                        val.pincode = object.order.customer_details.pincode;
                        val.address = object.order.customer_details.address;
                        val.city = object.order.customer_details.city;
                        val.country = object.order.customer_details.country;
                        val.itemdetails = object.order.order_items;
                        val.total = object.order.net_amount;
                        val.amountPaid = object.order.amount_paid;
                        val.balance = object.order.amount_balance;
                        val.cashcollect = object.order.cash_to_be_collected;
                        val.paymentmode = object.order.payment_mode;
                        val.restaurantName = object.order.restaurant_name;
                        val.instructions = object.order.instructions;
                        val.charges = object.order.charges;
                        $scope.total = 0;
                        angular.forEach(val.itemdetails, function (val, key) {
                            $scope.total = (val.item_quantity * val.item_unit_price) + $scope.total;
                        });
                        val.date = object.order.order_date_time;
                        val.subTotal = $scope.total;
                        val.totalTaxes = object.order.total_taxes;
                        val.totalCharges = object.order.order_level_total_charges;
                        val.itemCharge = object.order.item_level_total_charges;
                        val.itemTaxes = object.order.item_level_total_taxes;
                        val.discount = object.order.discount;
                        if (object.order.createdTime != null) {
                            val.createdTime = new Date(object.order.createdTime);
                        }
                        if (object.order.deliveryTime != null) {
                            val.deliveryTime = new Date(object.order.deliveryTime);
                        }
                    })
                    },
                    function (error) {
                        Notification.error({
                            message: 'Something went wrong, please try again',
                            positionX: 'center',
                            delay: 2000
                        });
                })
            };

            $scope.openTableReservation = function (type) {
                $rootScope.orderTypeName=type;
                $rootScope.selectedTableId=null;
                $rootScope.selectedTableName=null;
                $window.location.href="#!/tableReserve";
            };

            $scope.setEmail=function (val) {
                $scope.customerMobileNo=val;
            };
            $scope.clearValues = function () {
                $scope.selfBuildInvoice = false;
                // $scope.disableForReturn=true;
                $scope.today();
                $scope.getPageLoadDataRest();
                $(".resetClass").val("");
                $("#paymentNew1").modal('hide');
            };
        }
    }
});
app.directive("restaurantPrint", function () {
    return {
        restrict: 'E',
        templateUrl: "partials/restaurantPrint.html",
        link: function ($scope, element, attrs) {

        }
    }
});
