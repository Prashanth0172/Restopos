
app.controller('accountSetupController',
    function ($rootScope, $scope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window, $cookies, $httpParamSerializerJQLike, keyPressFactory) {
        $scope.shortCutRestrict = keyPressFactory.shortCutRestrict();
        // body...\
        $scope.hiposServerURL = "/hipos/";
        $scope.retailServerURL = "/retail/";
        $scope.posPurchaseServerURL = "/purchase/";
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
        $scope.Restaurant='dineIn';
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
        $scope.Restaurant ="takeAway";
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
        $scope.notHide = "";
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
        $scope.getCashAccountList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.get($scope.hiposServerURL + "/" + $scope.customer + '/getCashAccountList?accountSearchText=' + val).then(function (response) {
                var data = response.data;
                $scope.accountList = angular.copy(data);
                $("#selectAccount").modal('show');
                $scope.accountSearchText = val;
                $scope.searchText = val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        };

        $scope.taxTypeList = function () {
            $http.get($scope.hiposServerURL + "/" + $scope.customer + '/getoutputTaxList').then(function (response) {
                var data = response.data;
                $scope.taxList = data;
            });
        };
        $scope.taxTypeList();
        $scope.getConfigurationList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.get($scope.hiposServerURL + "/" + $scope.customer + '/getConfigurationList?configurationSearchText=' + val).then(function (response) {
                var data = response.data;
                $scope.configurationList = angular.copy(data);
                $scope.configurationSearchText = val;
                $scope.searchText = val;
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            })
        };

        $scope.getVersionControlList = function () {
            $http.get($scope.hiposServerURL + "/" + $scope.customer + '/getVersionControlList').then(function (response) {
                var data = response.data;
                $scope.projectModuleList = angular.copy(data);

                angular.forEach($scope.projectModuleList, function (value, key) {
                    $scope.projectModuleList[key].status = value.status == "true";
                });
                $scope.projectModuleSearchText = val;
                $scope.searchText = val;
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            })
        };
        $scope.getVersionControlList();
        $scope.selectModule = function (x) {
            console.log(x.id);
            console.log(x.status);
        }

        $scope.getBankAccountList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.get($scope.hiposServerURL + "/" + $scope.customer + '/getBankAccountList?accountSearchText=' + val).then(function (response) {
                var data = response.data;
                $scope.accountList = angular.copy(data);
                $("#selectAccount1").modal('show');
                $scope.accountSearchText = val;
                $scope.searchText = val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        };


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

        $scope.getAccount = function (accountCode) {

            $http.get($scope.hiposServerURL + "/" + $scope.customer + '/getAccount?accountCode=' + accountCode).then(function (response) {
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
        $scope.getAccount1 = function (accountCode) {
            console.log(accountCode),
                $http.get($scope.hiposServerURL + "/" + $scope.customer + '/getAccount?accountCode=' + accountCode).then(function (response) {
                    var data = response.data;
                    $scope.addSelectedAccountList1(data[0]);
                    $("#selectAccount1").modal('hide');
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })

        };
        $scope.selectedAccountList = [];
        $scope.addSelectedAccountList = function (data) {
            $scope.account_name = data.account_name;
            $scope.CashAccountIDText = data.accountid;
            $scope.selectedAccountList.push({
                account_name: data.account_name,
                CashAccountIDText: data.accountid
            });

        };
        $scope.selectedAccountList1 = [];
        $scope.addSelectedAccountList1 = function (data) {
            $scope.BankAccountText = data.account_name;
            $scope.BankAccountIDText = data.accountid;
            $scope.selectedAccountList1.push({
                BankAccountText: data.account_name,
                BankAccountIDText: data.accountid
            });

        };
        $scope.getConfigurationList();
        $scope.getConfiguredInformation = function () {
            $http.post($scope.hiposServerURL + "/" + $scope.customer + '/getConfigureData').then(function (response, status, headers, config) {
                console.log('Configuration Loaded..');
                console.log(response.data);
                if (response.data) {
                    $scope.account_name = response.data.cashAccountName;
                    $scope.CashAccountIDText = response.data.cashAccountId;
                    $scope.BankAccountIDText = response.data.bankAccountId;
                    $scope.BankAccountText = response.data.bankAccountName;
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
                    $scope.printerModelList = response.data.printerModelCnf;
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
        })
        $scope.saveAccountSetup = function () {

            if ($scope.account_name === ''||$scope.account_name ===null) {
                Notification.warning({message: 'Cash account can not be empty', positionX: 'center', delay: 2000});
            } else if ($scope.BankAccountText === ''||$scope.BankAccountText===null) {
                Notification.warning({message: 'Bank account can not be empty', positionX: 'center', delay: 2000});
            } else {
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
                    posBankAccountForSaving: $scope.BankAccountIDText,
                    posCashAccountForSaving: $scope.CashAccountIDText,
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
                    printerModelCnf: JSON.stringify($scope.cnfPrinterModel),
                    printerModelMobile: JSON.stringify($scope.cnfPrinterModelMobile),
                    promptKOT: $scope.promptKOT,
                    taxId:$scope.taxid,
                    taxcheckbox:$scope.taxcheckbox
                };

                $rootScope.showFullPageLoading = true;
                $http.post($scope.hiposServerURL +'/saveAccountSetup', angular.toJson(saveAccountSetupDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    $("#save_new_AccountSetup_modal").modal('hide');
                    if (!angular.isUndefined(data) && data !== null) {
                        $scope.accountSearchText = "";
                    }
                    $scope.getConfigurationList();
                    Notification.success({
                        message: 'Configuration Created  successfully',
                        positionX: 'center',
                        delay: 2000
                    });

                    // got to dashboard page if its first configuration save request
                    if(PAGE_NAME.trim() === 'configuration'){
                        window.location.href = "/";
                    }

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
            $scope.today = function () {
                $scope.dt = new Date();
                $scope.dt1 = new Date();
            };
            $scope.today();

            $scope.open1 = function () {
                $scope.popup1.opened = true;
            };

            $scope.popup1 = {
                opened: false
            };

            $scope.open2 = function () {
                $scope.popup2.opened = true;
            };

            $scope.popup2 = {
                opened: false
            };
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
                    url: '/hipos/1/getPrinterDeviceList',
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
        }

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
        }
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
            var getRequest = {
                url: '/hipos/1/getItemCategoryList',
                method: 'GET',
                params: {itemCategorySearchText:''}
            }

            $http(getRequest).then(function(successResponse){
                //Push all 'defaultType'
                $scope.itemCategoryList = $filter('filter')(successResponse.data,{defaultType:"true"},true);
            })
        }

        $scope.getItemCategoryList();

        $(".loginBody").css("background-color", "dimgray");
        $(".bg_sub_row_info").css("background-color", "lightdimgray");
        $(".pos-header-new").css({"border-top": "30px solid #d9534f", "color": "white", "margin": "0 0 0 -7px"});

        $scope.getTableConfigurationList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.get("/hipos/1/getTableConfigurationList?tableConfigSearchText=" + val + '&status=' + "Active").then(function (successResponse) {
                    $scope.tableConfigurationList = angular.copy(successResponse.data);
                    $http.get("/hipos/1/getTableConfigurationList?tableConfigSearchText=" + val + '&status=' + "OrderType").then(function (successResponse) {
                            angular.forEach(successResponse.data,function (val,key) {
                                $scope.tableConfigurationList.push(val);

                            })
                        }, function () {
                            Notification.error('Something went wrong');
                        }
                    )
                }, function () {
                    Notification.error('Something went wrong');
                }
            )
        };
        $scope.getTableConfigurationList();
        $scope.userList = function () {
            $http.post($scope.hiposServerURL + "/" + $scope.customer + '/getUserList?status='+"Active").then(function (response) {
                var data = response.data;
                $scope.userlist = data;
            });
        };
        $scope.userList();





    });