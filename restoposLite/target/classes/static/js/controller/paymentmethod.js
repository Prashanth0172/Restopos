
app.controller('paymentmethodCtrl',
    function ($scope, $timeout,$http, $location, $filter, Notification) {
        $scope.hiposServerURL = "/hipos/";
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
        $scope.listStatus="";
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
        $scope.receiptPaymentList=[];
        $scope.disableButtons=false;
        $scope.customerDetails=[];
        $scope.customerNameText = "";
        $scope.hiConnectNotificationList=[];
        $scope.fullUserName="";
        $scope.PaymentMethodText="";
        $scope.DescriptionText="";
        $scope.TypeText="";
        $scope.AccountTypeText="";
        $scope.inactiveStatus="Active";
        //added for pagination purpose @rahul
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=0;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;

        $scope.taxTypes = [
            {name: 'FullTax', value: 'FullTax'},
            {name: 'SimplifiedTax', value: 'SimplifiedTax'},
        ];
        $scope.updateCustomerId = function (newCustVal) {
            $scope.customer = newCustVal.customerId;
            $scope.removeAllItems();
        }
        $scope.removePaymentMethodDetails = function () {
            $scope.paymentmethodId = "0";
            $scope.PaymentMethodText="0";
            $scope.DescriptionText="0";
            $scope.TypeText="0";
            $scope.account_name="";
            $scope.defaultType=false;
            $scope.validateVoucher=false;
            $scope.merchantId="";
            $scope.uniqueId="";
            $scope.secretKey="";
        };
        $scope.addNewPaymentMethodPopulate = function () {
            $scope.paymentmethodId = "";
            $scope.paymentmethodNameText = "";
            $scope.paymentmethodDescriptionText = "";
            $scope.paymentmethodTypeText = "";
            $scope.account = "";
            $scope.defaultType=false;
            $scope.validateVoucher=false;
            $scope.account_name="";
            $scope.paymentMethodText="Active";
            $("#submit").text("Save");
            $('#payment-title').text("Add Payment Method");
            $("#add_new_PaymentMethod_modal").modal('show');

        };

        $scope.getPaginatedPaymentMethodList = function (page){
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
            if (angular.isUndefined($scope.paymentmethodSearchText)) {
                $scope.paymentmethodSearchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginatedPaymentMethodList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.paymentmethodSearchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.paymentMethodList = data.list;
                angular.forEach($scope.paymentMethodList,function (value,key) {
                    if (value.paymentmethodType == 'airPay') {
                        $scope.airpayDisabled = true;

                    }
                    if (value.paymentmethodType == 'invoicePay') {
                        $scope.invDisable = true;

                    }
                })
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
        $scope.getPaginatedPaymentMethodList();
        $scope.inactivePaymentMethod = function (){
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
            $scope.getPaginatedPaymentMethodList();

        };

        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";



        $scope.importPopup = function(){
            $("#import_payMthd").modal('show');
        }

        $scope.savePayMthdImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("payMthdDetails");
            var payMthdDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL +  '/payMethodImportSave',payMthdDetails,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#import_payMthd").modal('hide');
                    $scope.getPaginatedPaymentMethodList();
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

        $scope.getAccountList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.get("/hipos" + '/getAccountMasterList?accountSearchText=' + val).then(function (response) {
                var data = response.data;
                $scope.accountList = angular.copy(data);
                $("#selectAccount").modal('show');
                $scope.accountSearchText = val;
                $scope.searchText = val;
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            })
            //     // .error(function (data, status, header, config) {
            //     // Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            // });
        };
        $scope.addAccount = function (accountCode, keyEvent) {
            var localAccountCode;
            localAccountCode = accountCode.accountid;
            $scope.getAccount(localAccountCode);
            //timebeen committed
            // $scope.accountIndex = $scope.itemIndexOfAccountCode($scope.selectedAccountList, localAccountCode);
            // if (!angular.isUndefined($scope.accountIndex) && $scope.accountIndex !== null && $scope.accountIndex !== -1) {
            //     $scope.currentQty = $scope.selectedAccountList[$scope.accountIndex].qty;
            //     $scope.selectedAccountList[$scope.accountIndex].qty = parseFloat($scope.currentQty) + 1;
            //     $scope.editSelectedAccountList($scope.selectedAccountList[$scope.accountIndex], $scope.selectedAccountList[$scope.accountIndex].taxid,$scope.accountIndex);
            // } else {
            //     $scope.getAccount(localAccountCode);
            // }

        };
        $scope.getAccount = function (accountCode) {
            console.log(accountCode),
                $http.get("/hipos" + '/getAccount?accountCode=' + accountCode).then(function (response) {
                    var data = response.data;
                    $scope.addSelectedAccountList(data[0]);
                    $("#selectAccount").modal('hide');

                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })

        };
        $scope.selectedAccountList=[];
        $scope.addSelectedAccountList = function (data) {
            $scope.account_name = data.account_name;
            $scope.account=data;
            $scope.CashAccountIDText =  data.accountid,
                $scope.selectedAccountList.push({
                    account_name: data.account_name,
                    CashAccountIDText: data.accountid,
                });

        };
        $scope.saveOrUpdatePayment= function () {
            if (angular.isUndefined($scope.paymentmethodNameText) || $scope.paymentmethodNameText == '' ) {
                Notification.warning({message: 'payment Name can not be empty', positionX: 'center', delay: 2000});
            }
            else if (angular.isUndefined($scope.paymentmethodTypeText) || $scope.paymentmethodTypeText == '' ) {
                Notification.warning({message: 'payment Method Type can not be empty', positionX: 'center', delay: 2000});
            }
            // else if(angular.isUndefined($scope.account) || $scope.account == '' ) {
            //     Notification.warning({message: 'Account can not be empty', positionX: 'center', delay: 2000})
            //
            // }
            else if($scope.paymentmethodTypeText === "airPay"){
                if(angular.isUndefined(merchantId) || $scope.merchantId == '' || $scope.merchantId == null){
                    Notification.warning({message: 'MerchantId Can not be empty', positionX: 'center', delay: 2000});
                }
                else if(angular.isUndefined(uniqueId) || $scope.uniqueId == '' || $scope.uniqueId == null){
                    Notification.warning({message: 'UniqueId Can not be empty', positionX: 'center', delay: 2000});

                }
                else{
                    $scope.savePayMethod();
                }

            }
            else if($scope.paymentmethodTypeText === "invoicePay"){
                if(angular.isUndefined(merchantId) || $scope.merchantId == '' || $scope.merchantId == null){
                    Notification.warning({message: 'MerchantId Can not be empty', positionX: 'center', delay: 2000});
                }
                else if(angular.isUndefined(uniqueId) || $scope.uniqueId == '' || $scope.uniqueId == null){
                    Notification.warning({message: 'UniqueId Can not be empty', positionX: 'center', delay: 2000});

                }
                else if(angular.isUndefined(secretKey) || $scope.secretKey == '' || $scope.secretKey == null){
                    Notification.warning({message: 'Seceret Key Can not be empty', positionX: 'center', delay: 2000});

                }
                else{
                    $scope.savePayMethod();
                }

            }
            else {
                $scope.savePayMethod();

            };
        };

        $scope.savePayMethod = function(){
            $scope.isDisabled= true;
            $timeout(function(){
                $scope.isDisabled= false;
            }, 3000)
            var savePaymentMethodDetails;
            savePaymentMethodDetails = {
                paymentmethodId: $scope.paymentmethodId,
                paymentmethodName: $scope.paymentmethodNameText,
                paymentmethodDescription: $scope.paymentmethodDescriptionText,
                paymentmethodType: $scope.paymentmethodTypeText,
                accountMaster: $scope.account,
                validateVoucher:$scope.validateVoucher,
                defaultType:$scope.defaultType,
                status:$scope.paymentMethodText,
                merchantId:$scope.merchantId,
                uniqueId:$scope.uniqueId,
                secretKey:$scope.secretKey,
            };
            $http.post($scope.hiposServerURL + '/savePaymentmethod', angular.toJson(savePaymentMethodDetails, "Create")).then(function (response, status, headers, config) {
                var data = response.data;
                if(data==""){
                    Notification.success({message: 'Already exists', positionX: 'center', delay: 2000});
                }
                else {
                    $scope.removePaymentMethodDetails();
                    $scope.getPaginatedPaymentMethodList();
                    $("#add_new_PaymentMethod_modal").modal('hide');
                    if (!angular.isUndefined(data) && data !== null) {
                        $scope.paymentMethodSearchText = "";
                    }
                    Notification.success({
                        message: 'Payment Method Created  successfully',
                        positionX: 'center',
                        delay: 2000
                    });
                }
            },function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            });
        }
        $scope.editPayment = function(data) {
            $scope.paymentmethodId = data.paymentmethodId;
            $scope.paymentmethodNameText = data.paymentmethodName;
            $scope.paymentmethodDescriptionText=data.paymentmethodDescription;
            $scope.paymentmethodTypeText=data.paymentmethodType;
            $scope.paymentMethodText=data.status;
            $scope.merchantId=data.merchantId;
            $scope.uniqueId=data.uniqueId;
            $scope.secretKey=data.secretKey;
            if(data.accountMasterId!=null)
            $scope.getAccount(data.accountMasterId);
            if(data.accountMaster!=null) {
                $scope.account_name = data.accountMaster.account_name;
                $scope.account=data.accountMaster;
            }
            else {
                $scope.account_name="";
            }
            if(data.defaultType=='true'){
                $scope.defaultType=true;
            }
            else {
                $scope.defaultType=false;
            }
            if(data.validateVoucher=='true'){
                $scope.validateVoucher=true;
            }
            else {
                $scope.validateVoucher=false;
            }
            $("#submit").text("Update");
            $('#payment-title').text("Edit Payment Method");
            $("#add_new_PaymentMethod_modal").modal('show');
        },function (error) {
            Notification.error({message: 'Something went wrong, please try again',positionX: 'center',delay: 2000});

        };
        $scope.deletePayment = function (data) {
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
                    //  alert("delete");
                    if(result == true){
                        $scope.paymentmethodId = data.paymentmethodId;
                        $scope.paymentmethodNameText=data.paymentmethodName;
                        $scope.paymentmethodDescriptionText=data.paymentmethodDescription;
                        $scope.CashAccountIDText=data.accountMaster;
                        $scope.paymentMethodText=data.status;
                        var deleteDetails = {
                            paymentmethodId : $scope.paymentmethodId,
                            paymentmethodName: $scope.paymentmethodNameText,
                            paymentmethodDescription : $scope.paymentmethodDescriptionText,
                            accountMaster: $scope.account
                        };
                        $http.post("/hipos"  + '/deletePayment', angular.toJson(deleteDetails, "Create")).then(function (response, status, headers, config) {
                            var data = response.data;
                            if(data=="") {
                                Notification.success({
                                    message: 'It Is Already In Use Cant be Deleted',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            }else {
                                $scope.getPaginatedPaymentMethodList();
                                if($scope.paymentMethodText=="InActive") {
                                    $scope.getPaginatedPaymentMethodList("","InActive");
                                    Notification.success({
                                        message: 'Status has been changed to Active',
                                        positionX: 'center',
                                        delay: 2000
                                    });
                                }
                                else{
                                    $scope.getPaginatedPaymentMethodList("","");
                                    Notification.success({
                                        message: 'Status has been changed to InActive',
                                        positionX: 'center',
                                        delay: 2000
                                    });
                                }
                            }
                        }, function (error) {
                            Notification.error({
                                message: 'Something went wrong, please try again',
                                positionX: 'center',
                                delay: 2000
                            });
                        });
                    }
                    //  console.log('This was logged in the callback: ' + result);
                }
            });
        };

    });
