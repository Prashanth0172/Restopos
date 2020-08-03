
app.controller('journalentryCtrl',
    function ($scope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window) {
        $("#generaltransaction").addClass('active');
        $('#generaltransaction').siblings().removeClass('active');
        $("#journalentry").addClass('active');
        $('#journalentry').siblings().removeClass('active');
        // $scope.shortCutRestrict=keyPressFactory.shortCutRestrict();
        // body...\
        $scope.hiposServerURL =  "/hipos/";
        $scope.retailServerURL = "/hipos/";
        $scope.posPurchaseServerURL = "/hipos/";
        $scope.generalTransactionServerURL = "/gt";
        $scope.selectedAccountList = [];
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
        $scope.accountList = [];
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
        $scope.formNo="";
        $scope.returnType="";
        $scope.salesOrderList=[];
        $scope.receiptPaymentList=[];
        $scope.disableButtons=false;
        $scope.customerDetails=[];
        $scope.customerNameText = "";
        $scope.hiConnectNotificationList=[];
        $scope.fullUserName="";
        $scope.displayAccountLength=0;
        $scope.regEX =/^[0-9]{1,8}(\.[0-9]+)?$/;

        var location = window.location.origin;

        //setting date restriction in calender/datepicker
        (function(){
            var dateRange, splitDate, interval = 300; //msec
            var setDateRestriction = function(){
                dateRange = document.getElementById("topHeaderFinancialDate").innerHTML;
                splitDate = dateRange.split("-");
                if (splitDate[0].trim().length > 0 && splitDate[1].trim().length > 0) {
                    var range = dateManagerServ.getDateRange();
                    $scope.dateOptions = {
                        minDate : range.min,
                        maxDate : range.max
                    };
                }else{
                    $timeout(setDateRestriction, interval);
                }
            };
            $timeout(setDateRestriction, 0);
        })();

        $scope.taxTypes = [
            {name: 'FullTax', value: 'FullTax'},
            {name: 'SimplifiedTax', value: 'SimplifiedTax'},
        ];

        var div = $('.textdiv'),
            height = div.height();
       
        $scope.updatesimplifiedTax = function (newCustVal) {
            $scope.simplifiedTax = newCustVal.fullSimplTax;
        }
        $scope.updateCustomerId = function (newCustVal) {
            $scope.customer = newCustVal.customerId;
            $scope.removeAllItems();
        }

        $scope.companyLogoPath = "";
        $scope.getPageLoadData = function () {

            $http.get($scope.generalTransactionServerURL + "/" + $scope.customer + '/getPageLoadData').then(function (response) {
                var data = response.data;
                if (!angular.isUndefined(data) && data !== null && data != 'invalid') {
                    $scope.taxList = data.taxList;
                    $scope.userRights = data.userRights;
                    $scope.customerList = data.customers;
                    $scope.companyLogoPath = location + "/" + data.companyLogoPath;
                    $scope.customerId = $scope.customerList[0].customerId;
                    $scope.fullSimplTax = $scope.taxTypes[0].value;
                    $scope.customerSearchText = "Cash Customer|01";
                    $scope.companyName = data.companyName;
                    $scope.termsAndConditionList = data.termsAndConditionList;
                    $scope.exchangeRateList = data.exchangeRateList;
                    $scope.agentList = data.agentList;
                    $scope.shippingMethod=data.shippingMethodList;
                    $scope.currencyList = data.currencyList;
                    $scope.projectList = data.projectList;
                    $scope.fullUserName = data.fullUserName;
                    $scope.countryId = data.countryId;
                    $scope.currencyText = data.countryId;
                    $scope.exchangeRateText = 1;
                    $scope.taxListInDict = {};
                    angular.forEach($scope.taxList, function(value){
                        $scope.taxListInDict[value.taxString.split("|")[1].trim()] = value.taxid;
                    });
                }
                else{
                    Notification.error({message: 'Your session expired.Please login again', positionX: 'center', delay: 2000});
                    $window.location.href="hinextlogin.html";
                }
            })

        };
        $scope.getPageLoadData();

        app.directive('validNumber', function() {
            return {
                require: '?ngModel',
                link: function(scope, element, attrs, ngModelCtrl) {
                    if(!ngModelCtrl) {
                        return;
                    }

                    ngModelCtrl.$parsers.push(function(val) {
                        if (angular.isUndefined(val)) {
                            var val = '';
                        }

                        var clean = val.replace(/[^-0-9\.]/g, '');
                        var negativeCheck = clean.split('-');
                        var decimalCheck = clean.split('.');
                        if(!angular.isUndefined(negativeCheck[1])) {
                            negativeCheck[1] = negativeCheck[1].slice(0, negativeCheck[1].length);
                            clean =negativeCheck[0] + '-' + negativeCheck[1];
                            if(negativeCheck[0].length > 0) {
                                clean =negativeCheck[0];
                            }

                        }

                        if(!angular.isUndefined(decimalCheck[1])) {
                            decimalCheck[1] = decimalCheck[1].slice(0,4);
                            clean =decimalCheck[0] + '.' + decimalCheck[1];
                        }

                        if (val !== clean) {
                            ngModelCtrl.$setViewValue(clean);
                            ngModelCtrl.$render();
                        }
                        return clean;
                    });

                    element.bind('keypress', function(event) {
                        if(event.keyCode === 32) {
                            event.preventDefault();
                        }
                    });
                }
            };
        });
        $scope.selectedAccountListRemoval = {};
        $scope.removeSelectedAccount = function () {
            $scope.remove_account= true;
            $timeout(function(){
                $scope.remove_account= false;
            }, 2000)
            var length=$scope.selectedAccountList.length;
            if (angular.isUndefined($scope.selectedAccountList) || $scope.selectedAccountList.length <= 0) {
                Notification.error({message: 'At lest One Account has to be selected', positionX: 'center', delay: 2000});
            } else {
                $scope.selectedAccountList = $scope.selectedAccountList.filter(function (data, index) {
                    return !($scope.selectedAccountListRemoval[index] !== undefined && $scope.selectedAccountListRemoval[index]);
                });
                $scope.displayAccountLength=$scope.selectedAccountList.length;
                $scope.selectedAccountListRemoval = {};
                if ($scope.selectedAccountList.length==length) {
                    Notification.error({message: 'At lest One Account has to be selected', positionX: 'center', delay: 2000});
                }
            }
        };

        $scope.getAccountList = function (val,showPopUp) {
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.post($scope.hiposServerURL  + '/getAccountMasterList?accountSearchText=' + val).then(function (response) {
                var data = response.data;
                $scope.accountList = angular.copy(data);
                if(showPopUp) {
                    $("#selectAccount").modal('show');
                }else {
                    $scope.showSelectAccountSidePopUp = true;
                }
                // $("#selectAccount").modal('show');
                $scope.accountSearchText = val;
                $scope.searchText = val;
                // $timeout(function(){$("#selectAccountSearchBox").focus();},500);
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            })
        };

        $scope.addAccount = function (accountCode) {
            for(var i =0, acc; i<$scope.selectedAccountList.length; ++i){
                acc = $scope.selectedAccountList[i];

            }

            div.animate({scrollTop: height}, 500);
            height += div.height();
            // alert(height);
            var localAccountCode;
            localAccountCode = accountCode.accountid;
            $scope.getAccount(localAccountCode);
        };
        $scope.postJournalEntry = function (formNo,index) {
            if (formNo === "") {
                Notification.info({message: "Please Enter Invoice No", positionX: 'center', delay: 2000});
                return;
            }
            $http.get($scope.generalTransactionServerURL + '/postJournalEntryGt/'  + formNo, "Create").then(function (response) {
                var data = response.data;
                $scope.journalEntryData = data;
                Notification.success({message: 'Posting has been saved successfully', positionX: 'center', delay: 2000});
                $("#receiptDuplicatePrint").modal('hide');

            })
        };
        $scope.getJREForEdit= function (formNo) {
            if (formNo === "") {
                Notification.info({message: "Please Enter Invoice No", positionX: 'center', delay: 2000});
                return;
            }
            $http.get($scope.generalTransactionServerURL + '/getJREEdit/' + formNo).then(function (response, status, headers, config) {
                var data = response.data;
                $scope.operation = 'Edit';
                $scope.op = 'Edit';
                $scope.opNo = data.opNo;
                $scope.selectedAccountList = data.selectedAccountList;
                $scope.dt = new Date(data.jeDate);
                $scope.dt1=new Date(data.shippingDate);
                $scope.shippingmethodreferenceno=data.shippingReferenceNo;
                $scope.agentText=parseInt(data.agentId);
                $scope.currencyText=parseInt(data.currencyId);
                $scope.projectText=parseInt(data.projectId);
                $scope.exchangeRateText=parseInt(data.exchangeRateText);
                $scope.shipingmethod=parseInt(data.shippingmethodId);
                $scope.referenceNo=data.referenceNo;
                $scope.jeNo=data.jeNo;
                $scope.jeId=data.jeId;
                $scope.termsAndConditionText=parseInt(data.termsandConditionsId);
                $("#receiptDuplicatePrint").modal('hide');
                angular.forEach($scope.selectedAccountList, function (account,index) {
                    if(account.debitAmount>0){
                        $scope.updateTotalDebit_Credit(account,index,'debit');
                    }else if(account.creditAmount>0){
                        $scope.updateTotalDebit_Credit(account,index,'credit');
                    }
                });
            }, function (error) {
                if (error.status == 500) {
                    Notification.error({message: "Something went wrong in server", positionX: 'center', delay: 2000});
                } else {
                    Notification.error({message: data.message, positionX: 'center', delay: 2000});
                }
            })
        }
        $scope.saveJournalEntry = function () {
            if ($scope.selectedAccountList.length < 1) {
                Notification.error({
                    message: 'Check Out Amount Canot Be Zero',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            else if ($scope.outofBalanceText === '' || $scope.outofBalanceText !== 0) {
                Notification.error({message: 'Out Of Balance Should Be Zero', positionX: 'center', delay: 2000});
            }
            else{
                $http.post($scope.generalTransactionServerURL  + '/saveJournalEntryGt', angular.toJson($scope.populateSaveJEData())).then(function (response) {
                    var data = response.data;
                    $scope.jeNo="";
                    $scope.removeAllItems();
                    $scope.journalEntryData = data;
                    var $log = $( "#log" );
                    str = data.footer;
                    html = $.parseHTML( str );
                    // Append the parsed HTML
                    $log.append( html );
                    $("#gtJournalEntryPrint").modal('show');
                    Notification.info({
                        message: 'Journal Entry  Created Successfully',
                        positionX: 'center',
                        delay: 2000
                    })
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })
            }   };
        $scope.getAccount = function (accountCode) {
                $http.get($scope.hiposServerURL + '/getAccount?accountCode=' + accountCode).then(function (response) {
                    $scope.selectedAccountList.push(response.data[0]);
                },function (error) {
                    Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
                })
        };
        $scope.selectedAccountList=[];
        $scope.amtexclusivetax = 0;
        $scope.amtexclusivetax = new Array(100);
        $scope.selctedTaxId = new Array(100);
        $scope.taxPercent = new Array(100);
        $scope.taxamt = 0;
        // $scope.func = function(value, taxId, index){
        //     $scope.amtexclusivetax[index] = value;9741127977
        //     var taxIndex;
        //     for(var i=0; i<$scope.taxList.length; i++){
        //         if($scope.taxList[i]["taxid"] == taxId){
        //             taxIndex = i;
        //             break;
        //         }
        //     }
        //
        //     var taxDropDown = $scope.taxList[taxIndex].taxString.split('|');
        //     $scope.taxPercent = taxDropDown[0]/100;
        //     $scope.taxamt = value * $scope.taxPercent;
        //     $scope.amtinclusivetax = $scope.taxamt + value;
        //     $scope.selectedAccountList[index].taxamt = $scope.taxamt;
        //     $scope.selectedAccountList[index].amtinclusivetax = $scope.amtinclusivetax;
        //
        // }
        $scope.changeTaxId = function(value, index){
            value = value-1;
            var taxDropDown = $scope.taxList[value].taxString.split('|');
            $scope.taxPercent = taxDropDown[0]/100;
            $scope.taxamt = $scope.amtexclusivetax[index] * $scope.taxPercent;
            $scope.amtinclusivetax = $scope.amtexclusivetax[index] + $scope.taxamt;
            $scope.selectedAccountList[index].taxid = $scope.taxid;
            $scope.selectedAccountList[index].taxamt = $scope.taxamt;
            $scope.selectedAccountList[index].amtexclusivetax = $scope.amtexclusivetax;
            $scope.selectedAccountList[index].amtinclusivetax = $scope.amtinclusivetax;
        }
        $scope.selectedAccountList= [];
        $scope.addSelectedAccountList = function (data) {
            var taxxid=1;
            var count = $scope.countVal;
            $scope.taxIndex = $scope.taxIndexOf($scope.taxList, taxxid);
            var taxDropDown = $scope.taxList[$scope.taxIndex].taxString.split('|');
            $scope.taxPercent = taxDropDown[0];
            var unitPrice = data.salesPrice;
            var qty = 1;
            var amtexclusivetax = (parseFloat(unitPrice) * parseFloat(qty));
            var discountAmt = 0;
            $scope.taxamt = (parseFloat(amtexclusivetax) - discountAmt) * ((parseFloat($scope.taxPercent) / 100));
            var serializableItems = $scope.serializableItemsIndex;
            var tax1 = $scope.taxList[0].tax;
            var test1 = {
                accountid: data.data[0].accountid,
                account_name: data.data[0].account_name,
                stringAccountCode: data.data[0].stringAccountCode,
                tax: $scope.taxid[$scope.taxIndex],//$scope.taxList,

            };
            $scope.selectedAccountList.push(test1);
            $scope.countVal = count + 1;
            $scope.getTotalAmtForSelectedItems();
        };
        $scope.getTotalAmtForSelectedItems = function () {
            var totalAmt = 0.00;
            var totalTaxAmt = 0.00;
            var totalDiscountAmt = 0.00;
            var totalQty = 0.00;
            angular.forEach($scope.selectedAccountList, function (account, index) {
                totalAmt += parseFloat(account.amtinclusivetax);
                totalTaxAmt += parseFloat(account.taxamt);
                totalDiscountAmt += parseFloat(account.discountAmt);
                totalQty += parseFloat(account.returnQty);
            });

            var totalAfterDiscount = parseFloat(totalAmt);
            $scope.totalCheckOutamt = totalAmt.toFixed(2);
            $scope.totalBeforDiscount = parseFloat(totalAmt.toFixed(2));
            $scope.totalDiscount = parseFloat(totalDiscountAmt.toFixed(2));
            $scope.totalAfterDiscount = parseFloat(totalAfterDiscount.toFixed(2));
            $scope.totalTaxAmt = parseFloat(totalTaxAmt.toFixed(2));
            $scope.returnQty = parseFloat(totalQty.toFixed(2));
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
        $scope.itemIndexOfAccountCode = function (array, searchVal) {
            var accountIndex = -1;
            if ($scope.isUndefinedOrNull(searchVal)) {
                accountIndex = -1;
            } else {
                var foundIndex = $filter('filter')(array, {
                    accountCode: searchVal
                }, true)[0];
                var foundIndex1 = $filter('filter')(array, {
                    accountCode: searchVal
                }, true)[0];
                accountIndex = array.indexOf(foundIndex);
                if (!$scope.isUndefinedOrNull(foundIndex1)) {
                    if (foundIndex1.serializableStatus == 'Serialize')
                        accountIndex = -1
                }
            }
            return accountIndex;
        };
        $scope.updateNotificationItemInList = function(item){
            var index = $scope.selectedNotificationIndex;
            $scope.selectedItemsList[index]["itemCode"] = item["itemCode"];
            $scope.selectedItemsList[index]["itemName"] = item["itemName"];
            $scope.selectedItemsList[index]["itemId"] = item["itemId"];
            $("#selectItem .modal-footer button").click();
        }
        $scope.editSelectedAccountList = function (data, tax, index) {
            $scope.accountIndex = $scope.itemIndexOf($scope.selectedItemsList, data.itemName);
            if (tax != -1) {
                $scope.taxIndex = $scope.taxIndexOf($scope.taxList, tax);
                $scope.selectedItemsList[$scope.accountIndex].taxid = tax;
            } else {
                $scope.taxIndex = $scope.taxIndexOf($scope.taxList, data.taxid);
            }
            var taxPercent = $scope.taxList[$scope.taxIndex].taxString.split('|');
            var taxName = taxPercent[1];
            var unitPrice = data.unitPrice;
            var amntEX = data.amtexclusivetax;
            var qty = data.qty;
            var disAmt = $scope.getDiscountAmt(data.discountAmt, data.isDiscountInPercent, amtexclusivetax);
            if (parseFloat(amntEX) > 0) {
                if (parseFloat(disAmt) >= parseFloat(amntEX)) {
                    Notification.error({
                        message: 'Discount Amount Should Be Less Than Amount(EX) ',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.selectedItemsList[$scope.accountIndex].discountAmt = 0;
                    var amtexclusivetax = (parseFloat(unitPrice) * parseFloat(qty));
                    var discountAmt = $scope.getDiscountAmt(data.discountAmt, data.isDiscountInPercent, amtexclusivetax);
                    var taxamt = (parseFloat(amtexclusivetax) - parseFloat(discountAmt)) * ((parseFloat(taxPercent[0]) / 100));
                    var amtinclusivetax = ((parseFloat(amtexclusivetax) + parseFloat(taxamt)) - parseFloat(discountAmt));
                    $scope.selectedItemsList[index].taxamt = parseFloat(taxamt.toFixed(2));
                    $scope.selectedItemsList[index].amtinclusivetax = parseFloat(amtinclusivetax.toFixed(2));
                    return;
                }
            }
            if ($scope.operation === 'Return') {
                if (parseFloat(data.returnQty) > parseFloat(qty)) {
                    Notification.error({
                        message: 'Return quantity should be less or equal than quantity',
                        positionX: 'center',
                        delay: 2000
                    });
                    return;
                } else {
                }
            }
            if ($scope.operation !== 'Return') {
                $scope.selectedItemsList[index].qty = qty;
                $scope.selectedItemsList[index].remainingQty = qty;
                var amtexclusivetax = (parseFloat(unitPrice) * parseFloat(qty));
                var discountAmt = $scope.getDiscountAmt(data.discountAmt, data.isDiscountInPercent, amtexclusivetax);
                var taxamt = (parseFloat(amtexclusivetax) - parseFloat(discountAmt)) * ((parseFloat(taxPercent[0]) / 100));
                //to do
                var amtinclusivetax = ((parseFloat(amtexclusivetax) + parseFloat(taxamt)) - parseFloat(discountAmt));
                $scope.selectedItemsList[index].taxpercent = taxPercent[0];
                if (angular.isUndefined(qty) || angular.isUndefined(unitPrice) || angular.isUndefined(discountAmt)) {
                    $scope.selectedItemsList[index].taxamt = parseFloat(0).toFixed(2);
                    $scope.selectedItemsList[index].amtexclusivetax = parseFloat(0).toFixed(2);
                    $scope.selectedItemsList[index].amtinclusivetax = parseFloat(0).toFixed(2);
                }
                else {
                    $scope.selectedItemsList[index].amtexclusivetax = parseFloat(amtexclusivetax.toFixed(2)).toFixed(2);
                    $scope.selectedItemsList[index].taxamt = parseFloat(taxamt.toFixed(2)).toFixed(2);
                    $scope.selectedItemsList[index].amtinclusivetax = parseFloat(amtinclusivetax.toFixed(2)).toFixed(2);
                }
                $scope.selectedItemsList[index].discountAmt = parseFloat(discountAmt.toFixed(2));
                $scope.selectedItemsList[index].taxName = taxName;
                $scope.getTotalAmtForSelectedItems();
            } else {
                qty = parseFloat(qty) - parseFloat(data.returnQty);
//              $scope.selectedItemsList[$scope.accountIndex].qty = parseFloat(data.returnQty);
                $scope.selectedItemsList[index].remainingQty = parseFloat(qty);
                $scope.selectedItemsList[index].returnQty = parseFloat(data.returnQty);
                var amtexclusivetax = (parseFloat(unitPrice) * parseFloat(data.returnQty));
                var taxamt = (parseFloat(amtexclusivetax) * (parseFloat(taxPercent[0]) / 100));
                //to do
                var discountAmt = $scope.getDiscountAmt(data.discountAmt, data.isDiscountInPercent, amtexclusivetax);
                var amtinclusivetax = ((parseFloat(amtexclusivetax) + parseFloat(taxamt)) - parseFloat(discountAmt));
                $scope.selectedItemsList[index].taxpercent = taxPercent[0];
                $scope.selectedItemsList[index].amtexclusivetax = parseFloat(amtexclusivetax.toFixed(2));
                $scope.selectedItemsList[index].taxamt = parseFloat(taxamt.toFixed(2));
                $scope.selectedItemsList[index].amtinclusivetax = parseFloat(amtinclusivetax.toFixed(2));
                $scope.selectedItemsList[index].discountAmt = parseFloat(discountAmt.toFixed(2));
                $scope.selectedItemsList[index].taxName = taxName;
                $scope.getTotalAmtForSelectedItemsForReturn();
            }

            // }
        };
        $scope.isUndefinedOrNull = function (data) {
            return (angular.isUndefined(data) || data === null || data === '' || data === 'null');
        };
        // $scope.selectedAccountListRemoval = {};
        // $scope.removeSelectedAccount = function () {
        //     if (angular.isUndefined($scope.selectedAccountList) || $scope.selectedAccountList.length <= 0) {
        //         Notification.error({message: 'At lest One item has to be selected', positionX: 'center', delay: 2000});
        //     } else {
        //         $scope.selectedAccountList = $scope.selectedAccountList.filter(function (data, index) {
        //             return !($scope.selectedAccountListRemoval[index] !== undefined && $scope.selectedAccountListRemoval[index]);
        //         });
        //         $scope.selectedAccountListRemoval = {};
        //         $scope.getTotalAmtForSelectedItems();
        //     }
        // };
        $scope.removeAllItems = function () {
            $scope.totalCreditText = parseFloat('0.00');
            $scope.totalDebitText = parseFloat('0.00');
            $scope.totalCheckOutamt = parseFloat('0.00');
            $scope.totalBeforDiscount = parseFloat('0.00');
            $scope.totalDiscount = parseFloat('0.00');
            $scope.totalAfterDiscount = parseFloat('0.00');
            $scope.totalTaxAmt = parseFloat('0.00');
            $scope.invokeOrderName = "";
            $scope.memoText = "";
            $scope.outofBalanceText="";
            $scope.invokeOrderId = "";
            $scope.invokeorder = "";
            $scope.selectedAccountList = [];
            $scope.accountSearchText = "";
            $scope.operation = 'Create';
            $scope.customerEmail = "";
            $scope.customerSearchText = "Cash Customer|01";
            $scope.selectedSerialNumberFilter = "";
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
            $scope.selectedAccountList = [];
            $scope.accountSearchText = "";
            $scope.operation = 'Create';
            $scope.customerEmail = "";
            $scope.serialItems = "";

        };
        $scope.getCustomerListSearch = function (val) {
            $(".loader").css("display", "block");
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.get($scope.hiposServerURL + "/" + $scope.customer + '/getCustomerListSearch?searchCustomerText=' + val).then(function (response) {
                    var data = response.data;
                    $scope.customerList = angular.copy(data);
                    $("#selectCustomer").modal('show');
                    $scope.searchCustomerText = val;
                },function (error) {
                    Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
                }
            )

        };
        $scope.appendCustomer = function (customerId) {
            $scope.customerSearchText = customerId.customerName;
            $scope.customerId = customerId.customerId;
            $scope.customer = $scope.customerId;
            $scope.showEmailBox = false;
            $scope.removeAllItemsWithoutCustomer();
            $("#selectCustomer").modal('hide');

        }
        $scope.addCustomer = function () {
            $("#addCustomer").modal('show');
        }
        $scope.saveCustomer = function () {
            if (angular.isUndefined($scope.customerNameText) || $scope.customerNameText == '') {
                Notification.warning({message: 'customer Name can not be empty', positionX: 'center', delay: 2000});
            }
            else {
                $scope.customerName = "";
                $scope.customerEmail = "";
                $scope.customerContact = "";
                $scope.customerAddress = "";
                var saveCustomerDetails;
                saveCustomerDetails = {
                    customerName: $scope.customerNameText,
                    customerEmail: $scope.customerEmailText,
                    customerContact: $scope.customerContactText,
                    customerAddress: $scope.customerAddressText,
                    companyRegNo:$scope.companyRegNo,
                    notificationFlag:$scope.notificationFlag,
                    from_Reg_Comp:$scope.fromRegNo,
                    pincode:$scope.pincode,
                    to_Reg_Comp:$scope.toRegNo,
                    notificationId:$scope.notificationId,
                    gstIn:$scope.GSTINText,
                    state:$scope.state
                };
                $http.post($scope.retailServerURL + '/saveCustomer', angular.toJson(saveCustomerDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    $scope.removeCustomerDetails();
                    $("#addCustomer").modal('hide');
                    if (!angular.isUndefined(data) && data !== null) {
                        $scope.customerSearchText = data.customerName;
                        $scope.customerId = data.customerId;
                        $scope.customerEmail = data.customerEmail;
                        $scope.notificationFlag="";
                        $scope.fromRegNo="";
                    }
                    Notification.success({message: 'Customer Created  successfully', positionX: 'center', delay: 2000});
                },function (error) {
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
                //     });
                // });
            }
            ;
        };
        $scope.openpayment = function () {
            $scope.openStockValidateMultipaymnet();
        };
        $scope.openStockValidateMultipaymnet = function () {
            // if (!$scope.isValidatedData()) {
            //     Notification.warning({
            //         message: 'Data Entered May Not Be Proper',
            //         positionX: 'center',
            //         delay: 2000
            //     });
            // } else {
            $scope.getTotalAmtForSelectedItems();
            $scope.totalVPBeforDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
            if (angular.isUndefined($scope.totalVPBeforDiscount) || $scope.totalVPBeforDiscount <= 0) {
                Notification.warning({message: 'Check out amount can not be zero', positionX: 'center', delay: 2000});
                return false;
            }
            $scope.amountWithoutDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
            $scope.totalVPDiscount = parseFloat($scope.totalDiscount.toFixed(2));
            $scope.voucherNo = 0.00;
            $scope.cardAmount = 0.00;
            $scope.transactionNo = 0.00;
            $scope.totalVPAfterDiscount = parseFloat($scope.totalAfterDiscount.toFixed(2));
            $scope.totalVPAmountTendered = 0.00;
            $scope.totalVPAmountRefunded = 0.00;
            $scope.voucherAmt = 0.00;
            $scope.totalVoucherAmt = 0.00;
            $scope.totalPaidAmt = 0.00;
            $("#paymentNew").modal('show');
            // }
        };
        $scope.isValidatedData = function () {
            $scope.isValide = true;
            angular.forEach($scope.selectedAccountList, function (item, index) {
                if (angular.isUndefined(item.amtinclusivetax) || item.amtinclusivetax === '' || parseFloat(item.amtinclusivetax) <= 0) {
                    $scope.isValide = false;
                }else if (angular.isUndefined(item.qty) || item.qty === '' || parseFloat(item.qty) <= 0) {
                    $scope.isValide = false;
                }else if (angular.isUndefined(item.itemName) || item.itemName === '') {
                    $scope.isValide = false;
                }
            });
            return $scope.isValide;
        };
        $scope.saveMultiPaySI = function (paymentType) {
            if (!$scope.validatePayment(paymentType)) {
                return;
            }
            $scope.isDisabled=true;
            $http.post($scope.generalTransactionServerURL + '/saveJournalEntryGt',
                angular.toJson($scope.populateSaveSIMultiPayData(paymentType), "Create")).then(function (response, status, headers, config) {
                var data = response.data;
                $("#cashpayment").modal('hide');
                $("#creditcardpayment").modal('hide');
                $("#voucherPayment").modal('hide')
                $("#multiPayment").modal('hide');
                $scope.removeAllItems();
                $scope.populateSIResponceData(data, paymentType);
                Notification.success({message: 'Order has been saved successfully', positionX: 'center', delay: 2000});
                $("#paymentNew").modal('hide');
                $scope.isDisabled=false;
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
                $scope.isDisabled=false;
            });
            //     .error(function (data, status, header, config) {
            //     Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            //     $scope.isDisabled=false;
            // });
        };
        $scope.populateSaveSIMultiPayData = function (paymentType, operation) {
            var CPDetails;
            var CCPDetails;
            var VPDetails;
            var cashPayment = $scope.cashcheck;
            var cardPayment = $scope.card_checked;
            var voucherPayment = $scope.voucher_checked;
            if(angular.isUndefined(cashPayment) && angular.isUndefined(cardPayment) && angular.isUndefined(voucherPayment)){
                var confirm = $window.confirm("No Payment Type Is Selected Do You Want To Continue?");
                if (confirm == false) {
                    return false;
                }
            }
            if(cashPayment==false || cardPayment==false || voucherPayment==false){
                var confirm = $window.confirm("No Payment Type Is Selected Do You Want To Continue?");
                if (confirm == false) {
                    return false;
                }
            }
            if (cashPayment == "cashPayment") {
                CPDetails = {
                    totalCPAmountRefunded: $scope.totalCPAmountRefunded,
                    totalCPDiscount: $scope.totalCPDiscount,
                    totalCPAmountTendered: $scope.totalCPAmountTendered,
                };
            }
            if (cardPayment == "creditPayment") {
                CCPDetails = {
                    totalCCPDiscount: $scope.totalCCPDiscount,
                    totalCCPAmountTendered: $scope.totalCCPAmountTendered,
                    transactionNo: $scope.totalCCPTransactionNo,
                    totalCCPAfterDiscount: $scope.totalCCPAfterDiscount,
                    cardPaymentList: $scope.cards
                };
            }
            if (voucherPayment == "voucherPayment") {
                VPDetails = {
                    totalVPBeforDiscount: $scope.totalVPBeforDiscount,
                    totalVPDiscount: $scope.totalVPDiscount,
                    voucherNo: $scope.voucherNo,
                    voucherDate: $scope.voucherDate,
                    totalVoucherAmt: $scope.totalVoucherAmt,
                    totalVPAfterAllDeductions: $scope.totalVPAfterAllDeductions,
                    totalVPAmountTendered: $scope.totalVPAmountTendered,
                    totalVPAmountRefunded: $scope.totalVPAmountRefunded,
                    multiVoucherPayments: $scope.vouchers
                };
            }
            var data = {
                operation: operation,
                selectedAccountList: $scope.selectedAccountList,
                cashPayment: CPDetails,
                creditPayment: CCPDetails,
                voucherPayment: VPDetails,
                totalCheckOutamt: $scope.totalVPBeforDiscount,
                paymentType: paymentType,
                totalTaxAmt: $scope.totalTaxAmt,
                taxType: $scope.fullSimplTax,
                customerId: $scope.customerId,
                customerEmail: $scope.customerEmail,
                cutomerName: $scope.customerSearchText,
                exchangerateId: $scope.exchangeRateId,
                currencyId: $scope.currencyText,
                termsandConditionsId: $scope.termsAndConditionText,
                agentId: $scope.agentText,
                shippingmethodId: $scope.shipingmethod,
                projectId:$scope.projectText,
                referenceNo:$scope.referenceNo,
                shippingReferenceNo:$scope.shippingmethodreferenceno,
                shippingDate:$scope.dt1,
                amountReturned: $scope.totalVPAmountRefunded,
                discountAmount: $scope.totalVPDiscount,
                totalTenderedAmount: $scope.totalPaidAmt,
                userName: $('#userName').val(),
                advancepayment:$scope.advancepayment
            };
            return data;
        }
        $scope.populateSIResponceData = function (data, paymentType) {
            if (data.result === "SUCCESS") {
                angular.forEach(data.siData.selectedItemsList, function (value, key) {
                    if (data.siData.taxType == 'CGST:SGST/UGST') {
                        data.siData.selectedItemsList[key].cgstsgstsplitvalues = value.taxamt / 2;
                        data.siData.selectedItemsList[key].taxPercentageSplit = value.taxpercent / 2;
                        data.siData.selectedItemsList[key].rateTaxPercentage = value.taxpercent;
                        data.siData.selectedItemsList[key].taxpercent = 0;
                        data.siData.selectedItemsList[key].taxamt = 0;
                    }
                    else {
                        data.siData.selectedItemsList[key].rateTaxPercentage = value.taxpercent;
                    }
                });
                $scope.numberToWord = toWords(data.siData.totalCheckOutamt);
                $scope.printData = data;
                if (paymentType === 'multiPayment' && data.siData.printType === 'normal') {
                    // $("#multiprintreceiptA4").modal('show');//Division Is Used To Print Malayasian A4 Print
                    $("#indianA4Print").modal('show');//Division Is Used To Print Indian A4 Print
                }
                else if (paymentType === 'multiPayment' && data.siData.printType === 'pos') {
                    $("#multiprintreceipt").modal('show');
                    $timeout(function () {
                        $("#multiprintreceipt").modal('hide');
                        $scope.printDiv('printableMultiarea');
                    }, 1000);
                }
            }

            // else {
            //     $scope.itemsNoStockList = data.itemDetils;
            //     $("#ItemsNoStockListModel").modal('show');
            // }


            $scope.clearMultiPayment();
        };
        $scope.open2 = function() {
            $scope.popup2.opened = true;
        };

        $scope.popup2 = {
            opened: false
        };
        $scope.getExchangeRate=function (no,list) {
            var defValue = 1;
            angular.forEach(list,function (value,key) {
                if (value.currencyId === no) {
                    defValue = value.exchangeRateValue;
                    $scope.exchangeRateId=value.exchangeRateId;
                    $scope.truefalse=false;
                }
            });
            $scope.exchangeRateText = defValue;
            if (defValue === 1) {
            $scope.currencyText=$scope.countryId;
            $scope.truefalse=true;
                Notification.error({
                    message: 'Please Add Exchange rate value',
                    positionX: 'center',
                    delay: 2000
                });
            }
        }
        $scope.validatePayment = function (paymentType) {
            if ("cashPayment" === paymentType) {
                if (angular.isUndefined($scope.totalCPAmountTendered) || $scope.totalCPAmountTendered === ""
                    || !(angular.isNumber(parseFloat($scope.totalCPAmountTendered))) || parseFloat($scope.totalCPAmountTendered) <= 0) {
                    Notification.error({message: 'Amount tendered should not be zero', positionX: 'center', delay: 2000});
                    return false;
                } else if (parseFloat($scope.totalCPAmountTendered) < parseFloat($scope.totalCPAfterDiscount)) {
                    Notification.error({
                        message: 'Amount tendered should not be less than payment amount',
                        positionX: 'center',
                        delay: 2000
                    });
                    return false;
                }
            } else if ("creditPayment" === paymentType) {
                if (angular.isUndefined($scope.totalCCPAfterDiscount) || $scope.totalCCPAfterDiscount === ""
                    || !(angular.isNumber(parseFloat($scope.totalCCPAfterDiscount))) || parseFloat($scope.totalCCPAfterDiscount) <= 0) {
                    Notification.error({message: 'Payment Amount should not be zero', positionX: 'center', delay: 2000});
                    return false;
                } else if (angular.isUndefined($scope.totalCCPTransactionNo) ||
                    $scope.totalCCPTransactionNo === "") {
                    Notification.error({message: 'Transaction No should not be black', positionX: 'center', delay: 2000});
                    return false;
                }
            } else if ("voucherPayment" === paymentType) {
                if (angular.isUndefined($scope.totalVPAmountTendered) || $scope.totalVPAmountTendered === ""
                    || !(angular.isNumber(parseFloat($scope.totalVPAmountTendered)))
                    || parseFloat($scope.totalVPAmountTendered) < 0) {
                    Notification.error({message: 'Amount tendered  should not be zero', positionX: 'center', delay: 2000});
                    return false;
                }


                else if (parseFloat($scope.totalVPAmountTendered) < parseFloat($scope.totalCPAfterDiscount)) {
                    Notification.error({
                        message: 'Amount tendered should not be less than payment amount',
                        positionX: 'center',
                        delay: 2000
                    });
                    return false;
                } else if (angular.isUndefined($scope.voucherNo) || $scope.voucherNo === "") {
                    Notification.error({message: 'Please enter voucherNo.', positionX: 'center', delay: 2000});
                    return false;
                } else if (angular.isUndefined($scope.totalVoucherAmt) || $scope.totalVoucherAmt === "") {
                    Notification.error({message: 'Please enter voucher amount', positionX: 'center', delay: 2000});
                    return false;
                }
            }
            return true;
        };
        $scope.clearMultiPayment = function () {
            $scope.selectedItemsList = [];
            $scope.totalCPAmountTendered = parseFloat(0);
            $scope.card_checked = false;
            $scope.voucher_checked = false;
            angular.forEach($scope.voucherPayementList, function (value, key) {
                if (value.amt > 0) {
                    value.amt = parseFloat(0.00);
                    value.voucherNo = "";
                    $scope.voucherPayementList.key = value.amt;
                    $scope.voucherPayementList.key = value.voucherNo;
                }
            });
            angular.forEach($scope.vouchers, function (value, key) {
                if (key != 0) {
                    $scope.vouchers.splice(key, 1);
                }
            });
            angular.forEach($scope.cardPayementList, function (value, key) {
                if (value.cardAmount > 0) {
                    value.cardAmount = parseFloat(0.00);
                    value.transactionNo = "";
                    $scope.cardPayementList.key = value.cardAmount;
                    $scope.cardPayementList.key = value.transactionNo;
                }
            });
            angular.forEach($scope.cards, function (value, key) {
                if (key != 0) {
                    $scope.cards.splice(key, 1);
                }
            });
            //----above newly added---
            $scope.totalBeforDiscount = parseFloat('0.00');
            $scope.totalDiscount = parseFloat('0.00');
            $scope.totalAfterDiscount = parseFloat('0.00');
            $scope.totalTaxAmt = parseFloat('0.00');
            $scope.invokeOrderName = "";
            $scope.invokeOrderId = "";
            $scope.invokeorder = "";
            $scope.itemSearchText = "";
            $scope.operation = 'Create';
            $scope.customerEmail = "";
            $scope.customerSearchText = "Cash Customer|01";
            $scope.selectedSerialNumberFilter = "";


        };

        $scope.updateTotalDebit_Credit = function(account, index, field) {
            $scope.totalDebitText = 0;
            $scope.totalCreditText = 0;
            if(field == "credit"){
                $scope.selectedAccountList[index]["debitAmount"] = 0;
                $scope.selectedAccountList[index]["creditAmount"] = account.creditAmount;
            }else{
                $scope.selectedAccountList[index]["creditAmount"] = 0;
                $scope.selectedAccountList[index]["debitAmount"] = account.debitAmount;
            }

            angular.forEach($scope.selectedAccountList, function(acc){
                $scope.totalDebitText += parseFloat(acc.debitAmount);
                $scope.totalCreditText += parseFloat(acc.creditAmount);
            });

            $scope.outofBalanceText = $scope.totalDebitText - $scope.totalCreditText;
        };

        $scope.getprintlist = function () {
            $("#purchase_print_list_modal").modal('show');

        };
        $scope.saveDraftJournalEntry = function () {
            if ($scope.selectedAccountList.length < 1) {
                Notification.error({
                    message: 'Check Out Amount Canot Be Zero',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            else if ($scope.outofBalanceText === '' || $scope.outofBalanceText !== 0) {
                Notification.error({message: 'Out Of Balance Should Be Zero', positionX: 'center', delay: 2000});
            }
            else{
                $http.post($scope.generalTransactionServerURL + '/saveDraftJournalEntryGt', angular.toJson($scope.populateSaveJEData())).then(function (response) {
                    var data = response.data;
                    $scope.jeNo="";
                    $scope.removeAllItems();
                    $scope.journalEntryData = data;
                    var $log = $( "#log" );
                    str = data.footer;
                    html = $.parseHTML( str );
                    // Append the parsed HTML
                    $log.append( html );
                    $("#gtJournalEntryPrint").modal('show');
                    Notification.info({
                        message: 'Journal Entry  Created Successfully',
                        positionX: 'center',
                        delay: 2000
                    })
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })
            }   };

        $scope.getDuplicateJE = function (val) {
            $(".loader").css("display", "block");
            if (angular.isUndefined(val)) {
                val = "";
            }
            if(angular.isUndefined($scope.fromLocation)){
                $scope.fromLocation =null;
            }
            $http.get($scope.generalTransactionServerURL + '/getDuplicateJE?searchText=' + val).then(function (response) {
                var data = response.data;
                $scope.JeList = angular.copy(data);
                $("#print_list_modal").modal('hide');
                $("#receiptDuplicatePrint").modal('show');
                $scope.searchText = val;
                $scope.itemSearchText=val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        };
        $scope.duplicateJEPrint = function (paymentID) {
            $http.post($scope.generalTransactionServerURL + '/duplicateJEPrint/' + paymentID).then(function (response) {
                var data = response.data;
                $scope.journalEntryData = data;
                $("#paymentVoucherDuplicatePrint").modal('hide');
                $("#gtJournalEntryPrint").modal('show');
            });
        };
        $scope.remove = function () {
            $scope.text = "";
        }
        $scope.populateSaveJEData = function () {
            var data = {
                journalEntryDetailsList: $scope.selectedAccountList,
                totalDebit: $scope.totalDebitText,
                totalCredit: $scope.totalCreditText,
                memo: $scope.memoText,
                exchangerateId: $scope.exchangeRateText,
                currencyId: $scope.currencyText,
                termsandConditionsId: $scope.termsAndConditionText,
                agentId: $scope.agentText,
                shippingmethodId: $scope.shipingmethod,
                projectId:$scope.projectText,
                referenceNo:$scope.referenceNo,
                shippingReferenceNo:$scope.shippingmethodreferenceno,
                shippingDate:$scope.dt1,
                jeDate :$scope.dt,
                jeNo :$scope.jeNo,
                jeId: $scope.jeId
            };
            return data;
        };

        $scope.remove=function () {
            $scope.text = "";
            $scope.accountSearchText="";
        }
        $scope.printDivA5 = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=400,height=400');
            popupWin.document.open();
            $("#close").hide;
            $("#printbutton").hide;
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" media="print" href="poscss/recept_print.css"><link href="css/bootstrap.css" rel="stylesheet"></head><body style="width:595px;" onload="window.print()">' + printContents + '</body></html>');
            popupWin.document.close();
        };


        $scope.today = function() {
            $scope.dt = new Date();
            $scope.dt1 = new Date();
        };
        $scope.today();
        $scope.format = 'dd/MM/yyyy';

        $scope.open1 = function() {
            $scope.popup1.opened = true;
        };

        $scope.popup1 = {
            opened: false
        };
        $scope.hideSection = function(){
            $('#openSection').hide();
            $('#hideSection').css("display","none");
            $('#showSection').css("display","block");
        };
        $scope.showSection = function(){
            $('#openSection').show();
            $('#hideSection').css("display","block");
            $('#showSection').css("display","none");
        };
    });
