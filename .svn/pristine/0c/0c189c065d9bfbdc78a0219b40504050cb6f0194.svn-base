
app.controller('configurationCtrl',
    function ($rootScope, $scope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window, $cookies) {

        // body...\
        $scope.hiposServerURL = "/hipos";
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
        $scope.Restaurant='';
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
        $scope.account_name = "";
        $scope.BankAccountText = "";
        $scope.UnitPriceText = "Editable";
        $scope.DiscountText = "Editable";
        $scope.PrintTypeText = "pos";
        $scope.Restaurant ="";
        $scope.discountType = "itemWise";
        $scope.kotPrinterList = [];
        $scope.categoryList = [];
        $scope.itemCategoryList = [];
        $scope.kotPrinterModelList = [];
        $scope.kotPrinterModelListMobile = [];
        $scope.printModelList = [];
        $scope.printModelListMobile = [];

        $scope.taxTypes = [
            {name: 'FullTax', value: 'FullTax'},
            {name: 'SimplifiedTax', value: 'SimplifiedTax'},
        ];
        $scope.updatesimplifiedTax = function (newCustVal) {
            $scope.simplifiedTax = newCustVal.fullSimplTax;
        }
        $scope.updateCustomerId = function (newCustVal){
            $scope.customer = newCustVal.customerId;
            $scope.removeAllItems();
        }
        $scope.removeAccountSetupDetails = function () {
            $scope.account_name = "";
            $scope.BankAccountText = "";
            $scope.PrintTypeText = "";

        };
        $scope.companyLogoPath = "";

        $scope.remove = function () {
            $scope.text = "";
            $scope.accountSearchText = "";

        }

        $scope.getConfigurationList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.post($scope.hiposServerURL +  '/getConfiguration').then(function (response) {
                var data = response.data;
                $scope.configurationList = angular.copy(data);
                // $scope.configurationSearchText = val;
                // $scope.searchText = val;
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            })
        };
        $scope.getConfigurationList();
        $scope.selectModule = function (x) {
            console.log(x.id);
            console.log(x.status);
        }


        $scope.addAccount = function (accountCode, keyEvent) {
            var localAccountCode;
            localAccountCode = accountCode.accountid;
            $scope.getAccount(localAccountCode);
        };
        $scope.addAccount1 = function (accountCode, keyEvent) {
            var localAccountCode;
            localAccountCode = accountCode.accountid;
            $scope.getAccount1(localAccountCode);
        };

        $scope.getConfiguredInformation = function () {
            $http.post($scope.hiposServerURL +  '/getConfigurationList').then(function (response, status, headers, config) {
                console.log('Configuration Loaded..');
                console.log(response.data);
                if (response.data) {
                    $scope.id=response.data.id;
                    $scope.CashAccountIDText =response.data.cashAcct;
                    $scope.BankAccountText =response.data.bankAcct;
                    // $scope.account_name = response.data.cashAccountName;
                    // $scope.CashAccountIDText = response.data.cashAccountId;
                    // $scope.BankAccountIDText = response.data.bankAccountId;
                    // $scope.BankAccountText = response.data.bankAccountName;
                    $scope.UnitPriceText = response.data.unitPrice;
                    $scope.DiscountText = response.data.discount;
                    $scope.PrintTypeText = response.data.printType;
                    $scope.discountType = response.data.discountType;
                    $scope.menuTypeGold = response.data.menuTypeGold;
                    $scope.menuTypePharma = response.data.menuTypePharma;
                    $scope.pompOrderTimeOut = response.data.pompOrderMaxTime;
                    $scope.sortOrder = response.data.tokenOrder;
                    if(response.data.taxcheckbox=="true"){
                        $scope.taxcheckbox = true;
                    }else {
                        $scope.taxcheckbox=false ;
                    }
                    $scope.taxid = parseInt(response.data.taxId);
                    $scope.menuTypeMobile = response.data.menuTypeMobile;
                    $scope.menuTypeResturant = response.data.menuTypeResturant;
                    if(response.data.reportPrinter!=""){
                        $scope.posPrinterModalList = JSON.parse(response.data.reportPrinter);
                        if($scope.posPrinterModalList==null){
                            $scope.posPrinterModalList=[];
                            $scope.posPrinterModalList.push({
                                posPrinter:'',
                                user:'',
                                tableConfig:'',
                            })
                        }
                    }else { $scope.posPrinterModalList=[];
                        $scope.posPrinterModalList.push({
                            posPrinter:'',
                            user:'',
                            tableConfig:'',
                        })
                    }

                    $scope.copytokot = response.data.copytokot;
                    $scope.cnfkotprinter = response.data.kotPrinter;
                    $scope.cnfa4printer = response.data.a4Printer;
                    $scope.mobilebillprinter = response.data.mobileBillPrinter;
                    $scope.printerModelList = response.data.printerModel;
                    $scope.kotPrinterModel = response.data.printerModelMobile;
                    $scope.buildTypeCloud = response.data.buildTypeCloud;
                    $scope.printPreview = (response.data.printPreview)?"Yes":"No";
                    $scope.Restaurant = response.data.restaurant;
                    $scope.notification = (response.data.notification)?"Yes":"No";
                    if(response.data.billModel!=""){
                        $scope.reportToReceipt = JSON.parse(response.data.billModel).reportToReceipt;
                        $scope.reportToCategory = JSON.parse(response.data.billModel).reportToCategory;
                    }
                    $scope.promptKOT = response.data.promptKOT;
                    if(response.data.printDetails!=null){
                        $scope.printer = JSON.parse(response.data.printDetails);
                        if($scope.printer.footervalue!=null){
                            $scope.checkbox=true;
                        }
                    }
                    $scope.printerSelection = response.data.printDetails;
                    $scope.getPrinterDevices();
                    $scope.initiateKOTDeviceList();
                    $scope.populateKOTDevices();
                    $scope.populatePrinterModalSelections();
                }
            });
        };
        $scope.getConfiguredInformation();
        $scope.initiateKOTDeviceList = function(){
            $scope.kotPrinterList.push('');
            $scope.kotPrinterModelList.push('');
            $scope.kotPrinterModelListMobile.push('');
        }
        $scope.populateKOTDevices = function(){
            angular.forEach(JSON.parse($scope.cnfkotprinter),function(values,keys){
                angular.forEach(values,function(value,key){
                    $scope.kotPrinterList[keys] = value;
                    $scope.categoryList[keys] = key;
                });
            });
        };

        $scope.populatePrinterModalSelections = function(){
            angular.forEach(JSON.parse($scope.printerModelList),function(values,keys){
                angular.forEach(values,function(value,key){
                    $scope.kotPrinterModelList[keys] = key;
                    $scope.printModelList[keys] = value;
                });
            });
            angular.forEach(JSON.parse($scope.kotPrinterModel),function(values,keys){
                angular.forEach(values,function(value,key){
                    $scope.kotPrinterModelListMobile[keys] = key;
                    $scope.printModelListMobile[keys] = value;
                });
            });
        };

        $scope.posPrinterModalList=[];
        $scope.posPrinterModalList.push({
            posPrinter:'',
            user:'',
            tableConfig:'',
        });
        $scope.saveAccountSetup = function () {

            // if ($scope.account_name === ''||$scope.account_name ===null) {
            //     Notification.warning({message: 'Cash account can not be empty', positionX: 'center', delay: 2000});
            // } else if ($scope.BankAccountText === ''||$scope.BankAccountText===null) {
            //     Notification.warning({message: 'Bank account can not be empty', positionX: 'center', delay: 2000});
            // }
            if(angular.isUndefined($scope.taxid)|| $scope.taxid === ''||$scope.taxid === null){
                Notification.warning({message: 'Tax Cannot be Empty', positionX: 'center', delay: 2000});
            }
            else if(angular.isUndefined($scope.pompOrderTimeOut)||$scope.pompOrderTimeOut === ''||$scope.pompOrderTimeOut === null){
                Notification.warning({message: 'prompt Order Cannot be Empty', positionX: 'center', delay: 2000});
            }
            else if(angular.isUndefined($scope.Restaurant) || $scope.Restaurant === ''||$scope.Restaurant === null){
                Notification.warning({message: 'Please Select Restaurant screen', positionX: 'center', delay: 2000});
            }
            else {
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000)
                var saveAccountSetupDetails;
                {
                    $scope.cnfkotprinter = [];
                    angular.forEach($scope.kotPrinterList,function(value,key){
                        $scope.cnfMap={};
                        $scope.cnfMap[$scope.categoryList[key]]=$scope.kotPrinterList[key];
                        $scope.cnfkotprinter.push($scope.cnfMap);
                    })

                    $scope.cnfPrinterModel = [];
                    angular.forEach($scope.kotPrinterModelList,function(value,key){
                        $scope.cnfMap={};
                        $scope.cnfMap[$scope.kotPrinterModelList[key]]=$scope.printModelList[key];
                        $scope.cnfPrinterModel.push($scope.cnfMap);
                    })
                    $scope.cnfPrinterModelMobile = [];
                    angular.forEach($scope.kotPrinterModelListMobile,function(value,key){
                        $scope.cnfMap={};
                        $scope.cnfMap[$scope.kotPrinterModelListMobile[key]]=$scope.printModelListMobile[key];
                        $scope.cnfPrinterModelMobile.push($scope.cnfMap);
                    })
                }
                saveAccountSetupDetails = {
                    id:$scope.id,
                    posBankAccountForSaving: $scope.BankAccountIDText,
                    bankAcct: $scope.BankAccountText,
                    posCashAccountForSaving: $scope.CashAccountIDText,
                    cashAcct: $scope.account_name,
                    unitPrice: $scope.UnitPriceText,
                    discount: $scope.DiscountText,
                    printType: $scope.PrintTypeText,
                    discountType: $scope.discountType,
                    tokenOrder: $scope.sortOrder,
                    pompOrderMaxTime: $scope.pompOrderTimeOut,
                    projectModuleDTOList: $scope.projectModuleList,
                    reportPrinter: JSON.stringify($scope.posPrinterModalList),
                    kotPrinter: JSON.stringify($scope.cnfkotprinter),
                    a4Printer: $scope.cnfa4printer,
                    mobileBillPrinter :$scope.mobilebillprinter,
                    copytokot: $scope.copytokot,
                    printDetails:$scope.printerSelection,
                    printPreview: ($scope.printPreview == "Yes"),
                    notification: ($scope.notification == "Yes"),
                    restaurant: $scope.Restaurant,
                    billModel: '{\"reportToReceipt\":' + $scope.reportToReceipt + ',\"reportToCategory\":' + $scope.reportToCategory + '}',
                    printerModel: JSON.stringify($scope.cnfPrinterModel),
                    printerModelMobile: JSON.stringify($scope.cnfPrinterModelMobile),
                    promptKOT: $scope.promptKOT,
                    taxId:$scope.taxid,
                    taxcheckbox:$scope.taxcheckbox
                };

                $rootScope.showFullPageLoading = true;
                $http.post($scope.hiposServerURL + '/saveNewPosConfig', angular.toJson(saveAccountSetupDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    $("#save_new_AccountSetup_modal").modal('hide');
                    if (!angular.isUndefined(data) && data !== null) {
                        $scope.accountSearchText = "";
                    }
                    // $scope.getConfiguredInformation();
                    $scope.getConfigurationList();
                    Notification.success({
                        message: 'Configuration Created  successfully',
                        positionX: 'center',
                        delay: 2000
                    });
                    $rootScope.showFullPageLoading = false;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                    $rootScope.showFullPageLoading = false;
                });

            }
            ;
            // $scope.today = function () {
            //     $scope.dt = new Date();
            //     $scope.dt1 = new Date();
            // };
            // $scope.today();
            //
            // $scope.open1 = function () {
            //     $scope.popup1.opened = true;
            // };
            //
            // $scope.popup1 = {
            //     opened: false
            // };
            //
            // $scope.open2 = function () {
            //     $scope.popup2.opened = true;
            // };
            //
            // $scope.popup2 = {
            //     opened: false
            // };
        }

        $scope.addNewbankPopulate = function () {
            $scope.configStatusText="Active";
            $('#bank-title').text("Add Bank");
            $("#add_new_Bank_modal").modal('show');
        };

        $scope.saveNewBank = function () {
            if ( angular.isUndefined($scope.bankNameText)|| $scope.bankNameText == '') {
                Notification.warning({message: 'Bank Name can not be empty', positionX: 'center', delay: 2000});
            }
            else {
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000)
                var saveBankDetails;
                saveBankDetails = {
                    bankId:$scope.bankId,
                    bankName: $scope.bankNameText,
                    address: $scope.AddressText,
                    iFSCCode: $scope.IFSCCodeText,
                    bankEmail: $scope.EmailText,
                    branchName: $scope.BranchNameText,
                    bankPhoneNo: $scope.PhoneNoText,
                    bankAccountNo: $scope.accountNoText,
                    status:$scope.configStatusText
                };

                $http.post($scope.hiposServerURL + "/" + $scope.customer + '/saveNewBank', angular.toJson(saveBankDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if (data == "") {
                        Notification.error({
                            message: 'Bank Account Already Created',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                    else {
                        $scope.removeBankDetails();
                        $("#add_new_Bank_modal").modal('hide');
                        if (!angular.isUndefined(data) && data !== null) {
                            $scope.bankSearchText = "";
                        }
                        Notification.success({message: 'Bank Created  successfully', positionX: 'center', delay: 2000});
                        $scope.BankAccountText = data.bankName;
                        $scope.BankAccountIDText = data.accountNo;
                    }
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                });
                //     .error(function (data, status, header, config) {
                //     Notification.error({
                //         message: 'Something went wrong, please try again',
                //         positionX: 'center',
                //         delay: 2000
            } //     });
            // });
        };

        $scope.removeBankDetails = function () {
            $scope.bankId = "";
            $scope.bankNameText = "";
            $scope.AddressText = "";
            $scope.IFSCCodeText = "";
            $scope.EmailText = "";
            $scope.BranchNameText = "";
            $scope.PhoneNoText = "";
            $scope.accountNoText = "";
        };

        $scope.getPrinterDevices = function(){
            var getRequest = {};
            console.log($scope.buildTypeCloud);
            if(!$scope.buildTypeCloud){
                getRequest = {
                    url: '/hipos/getPrinterDeviceList',
                    method: 'GET',
                    params: {}
                }

            }else {
                getRequest = {
                    url: 'http://localhost:9001/hiAccounts/getLocalPrinterList',
                    method: 'GET',
                    params: {}
                }
            }
            $http(getRequest).then(function (successResponse) {
                if (successResponse.data.body === undefined)
                    $scope.printerDeviceList = successResponse.data;
                else
                    $scope.printerDeviceList = successResponse.data.body
            }, function (failureResponse) {
            });
        };

        $scope.addKOT = function(){
            if($scope.kotPrinterList.length < 12) {
                $scope.kotPrinterList.push('');
            }else{
              Notification.error({
                message: 'Max KOT can be 12',
                positionX: 'center',
                delay: 2000
             });
            }
        }

        $scope.removeKOT = function(index){
            if(index === 0) {
                Notification.error({
                    message: 'Alteast one KOT printer should be defined',
                    positionX: 'center',
                    delay: 2000
                });
            }else{
                $scope.kotPrinterList.splice(index, 1);
                $scope.categoryList.splice(index, 1);
            }
        };
        $scope.removePosPrinter = function(index){
            if(index === 0) {
                Notification.error({
                    message: 'Alteast one Pos printer should be defined',
                    positionX: 'center',
                    delay: 2000
                });
            }else{
                $scope.posPrinterModalList.splice(index, 1);
            }
        }


        $scope.addPrinterModel = function(){
            if($scope.kotPrinterModelList.length < 12) {
                $scope.kotPrinterModelList.push('');
            }else{
                Notification.error({
                    message: 'Max KOT can be 12',
                    positionX: 'center',
                    delay: 2000
                });
            }
        }

        $scope.openPrinterSelectionPage = function () {

            $("#add_new_PrinterSelection_modal").modal('show');
        };


        $scope.savePrinterSelection = function(){
            $scope.printer.companyName ="Yes";
            $scope.printerSelection = JSON.stringify($scope.printer);
            $("#add_new_PrinterSelection_modal").modal('hide');
        }

        $scope.addPosPrinterModel = function(){
            if($scope.posPrinterModalList.length<12){
                $scope.posPrinterModalList.push({
                    posPrinter:'',
                    user:'',
                    tableConfig:'',
                })
            }else {
                Notification.error({
                    message: 'Max KOT can be 12',
                    positionX:'center',
                    delay:2000
                });
            }
        }

        $scope.removePrinterModel = function(index){
            if(index === 0) {
                Notification.error({
                    message: 'Alteast one KOT printer should be defined',
                    positionX: 'center',
                    delay: 2000
                });
            }else{
                $scope.kotPrinterModelList.splice(index, 1);
                $scope.printerModelList.splice(index, 1);
            }
        }


        $scope.getItemCategoryList = function() {
            $http.post($scope.hiposServerURL + "/getItemCategoryList").then(function (response) {
                var data = response.data;
                $scope.itemCategoryList = data;
            })
        }
        $scope.getItemCategoryList();

        $(".loginBody").css("background-color", "dimgray");
        $(".bg_sub_row_info").css("background-color", "lightdimgray");
        $(".pos-header-new").css({"border-top": "30px solid #d9534f", "color": "white", "margin": "0 0 0 -7px"});

        $scope.getTableConfigurationList = function () {
            $http.post("/hipos/getTableConfigList").then(function (successResponse) {
                    $scope.tableConfigurationList = angular.copy(successResponse.data);
                }, function () {
                    Notification.error('Something went wrong');
                }
            )
        };
        $scope.getTableConfigurationList();
        $scope.userList = function () {
            $http.post($scope.hiposServerURL + '/getUserAccountSetupList').then(function (response) {
                var data = response.data;
                $scope.userlist = data;
            });
        };
        $scope.userList();





    });