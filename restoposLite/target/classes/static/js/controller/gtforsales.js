app.controller('gtforsalesCtrl',
    function ($scope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window) {
        $("#generaltransaction").addClass('active');
        $('#generaltransaction').siblings().removeClass('active');
        $("#receipt").addClass('active');
        $('#receipt').siblings().removeClass('active');
        // body...\
        $scope.hiposServerURL =  "/hipos/";
        $scope.generalTransactionServerURL = "/gt";
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
        // $scope.today = new Date();
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
        $scope.supplier=1;
        $scope.displayAccountLength=0;
        $scope.regEX =/^[0-9]{1,8}(\.[0-9]+)?$/;
        // $scope.tax = "SimplifiedTax";
        // $scope.fullSimplTax ='Full Tax';
        // $scope.simplifiedTax ='Full Tax';

        var location = window.location.origin;

        //setting date restriction in calender/datepicker
        (function(){
            var dateRange, splitDate, interval = 300; //msec
            var setDateRestriction = function(){
                dateRange = document.getElementById("topHeaderFinancialDate").innerHTML;
                splitDate = dateRange.split("-");
                if (splitDate[0].trim().length > 0 && splitDate[1].trim().length > 0) {
                    var range = dateManagerServ.getDateRange();
                    if(range.min<new Date() && new Date()<range.max){
                        $scope.dt=new Date();
                        $scope.dt1 = new Date();
                    }else {
                        $scope.dt=range.min;
                        $scope.dt1 = range.min;
                    }
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
        $scope.reloadPage = function () {
            $window.location.reload();
        };

        $scope.taxTypes = [
            {name: 'IGST', value: 'IGST'},
            {name: 'CGST:SGST/UGST', value: 'CGST:SGST/UGST'},
        ];
        $scope.taxTypesMYR = [
            {name: 'Full Tax', value: 'Full Tax'},
            {name: 'Simplified Tax', value: 'Simplified Tax'},
        ];
        $scope.notHide = "";
        $scope.updatesimplifiedTax = function () {
            var taxType = $scope.fullSimplTax;
            if(taxType==='CGST:SGST/UGST') {
                angular.forEach($scope.selectedAccountList, function (value, key) {
                    var splitValue = value.taxamt / 2;
                    $scope.selectedAccountList[key].taxAmountSplit = splitValue + "+" + splitValue
                });
            }else{
                if(taxType==='IGST') {
                    angular.forEach($scope.selectedAccountList, function (value, key) {
                        var splitValue = value.taxamt;
                        $scope.selectedAccountList[key].taxAmountSplit = splitValue
                    });
                }
            }
            // $scope.simplifiedTax = newCustVal.fullSimplTax;
        }

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



        $scope.updateCustomerId = function (newCustVal) {
            $scope.customer = newCustVal.customerId;
            $scope.removeAllItems();
        }
        $scope.companyLogoPath = "";
        $scope.getPageLoadData = function () {
            $("#multiprintreceipt").modal('hide');
            $http.get($scope.generalTransactionServerURL  + '/getPageLoadData').then(function (response) {
                var data = response.data;
                if (!angular.isUndefined(data) && data !== null && data != 'invalid') {
                    $scope.taxList = data.taxList;
                    $scope.userRights = data.userRights;
                    $scope.customerList = data.customers;
                    $scope.companyLogoPath = location + "/" + data.companyLogoPath;
                    $scope.customerId = "";
                    $scope.termsAndConditionList = data.termsAndConditionList;
                    $scope.exchangeRateList = data.exchangeRateList;
                    $scope.agentList = data.agentList;
                    $scope.shippingMethod=data.shippingMethodList;
                    $scope.currencyList = data.currencyList;
                    $scope.projectList = data.projectList;
                    $scope.fullSimplTax = $scope.taxTypes[0].value;
                    $scope.MYRSimplTax = $scope.taxTypesMYR[0].value;
                    // $scope.customerSearchText = "Enter Receive From";
                    $scope.companyName = data.companyName;
                    $scope.fullUserName = data.fullUserName;
                    $scope.taxListInDict = {};
                    $scope.countryId = data.countryId;
                    $scope.currencyText = data.countryId;
                    angular.forEach($scope.exchangeRateList,function (value,key) {
                        if (value.currencyId === $scope.currencyText) {
                            $scope.exchangeRateId=value.exchangeRateId;
                            $scope.exchangeRateText=value.exchangeRateValue;
                        }
                    });
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
            //     // .error(function (data, status, header, config) {
            //     // Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            // });
        };
        var div = $('.textdiv'),
            height = div.height();

        $scope.addAccount = function (accountCode, keyEvent) {
            div.animate({scrollTop: height}, 500);
            height += div.height();
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
        $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];

        $scope.getAccount = function (accountCode) {
            console.log(accountCode),
            $http.get($scope.hiposServerURL  + '/getAccount?accountCode=' + accountCode).then(function (response) {
                //$scope.addSelectedAccountList(response);
                angular.forEach($scope.taxList,function (value,key) {
                    if(value.taxString=="0.0 | N/A | NOT APPLICABLE"){
                        response.data[0].taxid = value.taxid;
                    }
                })
                $scope.selectedAccountList.push(response.data[0]);
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            })
            //     .error(function (data, status, header, config) {
            //     Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            // });
        };
        $scope.selectedAccountList=[];
        // $scope.openpayment = function () {
        //     $scope.getTotalAmtForSelectedItems();
        //     $scope.totalVPBeforDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
        //     if (angular.isUndefined($scope.totalVPBeforDiscount) || $scope.totalVPBeforDiscount <= 0) {
        //         Notification.warning({message: 'Check out amount can not be zero', positionX: 'center', delay: 2000});
        //         return false;
        //     }
        //     $scope.amountWithoutDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
        //     $scope.totalVPDiscount = parseFloat($scope.totalDiscount.toFixed(2));
        //     $scope.voucherNo = 0.00;
        //     $scope.cardAmount = 0.00;
        //     $scope.transactionNo = 0.00;
        //     $scope.totalVPAfterDiscount = parseFloat($scope.totalAfterDiscount.toFixed(2));
        //     $scope.totalVPAmountTendered = 0.00;
        //     $scope.totalVPAmountRefunded = 0.00;
        //     $scope.voucherAmt = 0.00;
        //     $scope.totalVoucherAmt = 0.00;
        //     $scope.totalPaidAmt = 0.00;
        //     $("#paymentNew").modal('show');
        // }

        $scope.amtexclusivetax = 0;

        // $scope.$watch('taxValue', function(newValue, oldValue) {
        //     console.log(newValue);
        // });

        $scope.amtexclusivetax = new Array(100);
        $scope.selctedTaxId = new Array(100);

        $scope.taxPercent = new Array(100);
        $scope.taxamt = 0;
        $scope.func = function(value, taxId, index){
            $scope.totalAmt=value-0;
            $scope.amtexclusivetax[index] = $scope.totalAmt;
            // var taxIndex;
            // for(var i=0; i<$scope.taxList.length; i++){
            //     if($scope.taxList[i]["taxid"] == taxId){
            //         taxIndex = i;
            //         break;
            //     }
            // }
            // var taxDropDown = $scope.taxList[taxIndex].taxString.split('|');
            // console.log(taxDropDown)
            // // $scope.selctedTaxId[index] = value;
            // $scope.taxPercent = taxDropDown[0]/100;

            // $scope.taxPercent =
            // $scope.taxamt = $scope.totalAmt * $scope.taxPercent;
            $scope.amtinclusivetax =  ($scope.totalAmt-0);
            // $scope.selectedAccountList[index].taxid =taxId ;
            // $scope.selectedAccountList[index].taxamt = parseFloat($scope.taxamt).toFixed(2);
            $scope.selectedAccountList[index].amtinclusivetax = parseFloat($scope.amtinclusivetax).toFixed(2);
        }
        $scope.getCancelGtforSalesDraft=function (formNo) {
            $http.get($scope.generalTransactionServerURL  +'/cancelGtforSales/' + formNo).then(function (response, status, headers, config) {
                var data = response.data;
                $("#receiptDuplicatePrint").modal('hide');
                Notification.info({message: 'Canceled Successfully', positionX: 'center', delay: 2000})
            });
        }

        $scope.changeTaxId = function(value, index){
            value = value-1;
            console.log(value);
            console.log($scope.taxList[value].taxString)
            // $scope.selctedTaxId[index] = value;
            // $scope.taxIndex = $scope.taxIndexOf($scope.taxList, value);
            // console.log($scope.taxList[$scope.taxIndex]);
            var taxDropDown = $scope.taxList[value].taxString.split('|');
            console.log(taxDropDown)
            // $scope.selctedTaxId[index] = value;
            $scope.taxPercent = taxDropDown[0]/100;
            // console.log($scope.taxPercent[index]);
            $scope.taxamt = $scope.amtexclusivetax[index] * $scope.taxPercent;
            $scope.amtinclusivetax = $scope.amtexclusivetax[index] + $scope.taxamt;
            //////////////
            console.log($scope.selectedAccountList[index]);
            $scope.selectedAccountList[index].taxid = $scope.taxid;
            $scope.selectedAccountList[index].taxamt = $scope.taxamt;
            $scope.selectedAccountList[index].amtexclusivetax = $scope.amtexclusivetax;
            $scope.selectedAccountList[index].amtinclusivetax = $scope.amtinclusivetax;
        }

        $scope.selectedAccountList= [];
        $scope.addSelectedAccountList = function (data) {
            // var count=0;
            console.log("hi",data)
            // $scope.selectedAccountList=data.data;
            var taxxid=1;
            //kaleem
            var taxString = "simple";
            var count = $scope.countVal;
            $scope.taxIndex = $scope.taxIndexOf($scope.taxList, taxxid);
            // $scope.serializableItemsIndex=$scope.serializableItemsIndexOf(data.serializableItemsDTOList, data.serializableItemsDTOList[0].serializableItemCode);
            //    $scope.serializableItemsIndex = $scope.serializableItemsIndexOf($scope.serializableItemsList, data.serializableitemId);
            var taxDropDown = $scope.taxList[$scope.taxIndex].taxString.split('|');
            $scope.taxPercent = taxDropDown[0];
            var unitPrice = data.salesPrice;
            var qty = 1;
            var amtexclusivetax = (parseFloat(unitPrice) * parseFloat(qty));
            var discountAmt = 0;
            $scope.taxamt = (parseFloat(amtexclusivetax) - discountAmt) * ((parseFloat($scope.taxPercent) / 100));
            var serializableItems = $scope.serializableItemsIndex;
            // var amtinclusivetax = ((parseFloat(amtexclusivetax) + parseFloat(taxamt)) - parseFloat(discountAmt));

            var tax1 = $scope.taxList[0].tax;
            console.log("$scope.taxList",$scope.taxList);
            console.log("tax",tax1);
            var test1 = {
                    // $scope.selectedAccountList.push({
                    //   accountCode: data.data[0].data.account_code,
                    accountid: data.data[0].accountid,
                    account_name: data.data[0].account_name,
                    stringAccountCode: data.data[0].stringAccountCode,
                    tax: $scope.taxid[$scope.taxIndex],//$scope.taxList,
                    // taxValue: 0,
                    // unitPrice: data.salesPrice,
                    // discountAmt: data.discountAmountRpercent,
                    // isDiscountInPercent: data.isDiscountInPercent,
                    // qty: 1,
                    // returnQty: 0,
                    // remainingQty: 1,
                    //amtexclusivetax: parseFloat(amtexclusivetax.toFixed(2)),
                    // taxid:2,
                    // //  serializableitemId: data.serializableitemId,
                    // taxpercent: taxPercent[0],
                    // taxName: taxPercent[1],
                    // taxamt: parseFloat(taxamt),
                    // amtinclusivetax: parseFloat(amtinclusivetax.toFixed(2)),
                    // serialItems:data.serializableItemsDTOList
                };
            $scope.selectedAccountList.push(test1);
            console.log($scope.selectedAccountList);
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
            // if(parseInt($scope.selectedItemsList[index]["qty"]) > parseInt(item["stock"]) && item["itemTypeName"] != "Service" ){
            //    alert("Requested quantity is greater than available quantity. \n Creating order with available stock.");
            //    $scope.selectedItemsList[index]["qty"] = item["stock"];
            // }
            $("#selectItem .modal-footer button").click();
        }
        $scope.editSelectedAccountList = function (data, tax, index) {
            // if(data.serializableStatus=='Serialize') {
            //     $scope.getItemBarcode("1242");
            // }
            // else {
            $scope.accountIndex = $scope.itemIndexOf($scope.selectedItemsList, data.itemName);
            // $scope.accountIndex = $scope.itemIndexOf($scope.selectedItemsList, data.ItemDesc);
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


        $scope.removeAllItems = function () {
            $scope.totalCheckOutamt = parseFloat('0.00');
            $scope.totalBeforDiscount = parseFloat('0.00');
            $scope.totalDiscount = parseFloat('0.00');
            $scope.totalAfterDiscount = parseFloat('0.00');
            $scope.totalTaxAmt = parseFloat('0.00');
            $scope.invokeOrderName = "";
            $scope.invokeOrderId = "";
            $scope.invokeorder = "";
            $scope.dt=new Date();
            $scope.selectedAccountList = [];
            $scope.accountSearchText = "";
            $scope.searchContactText = "";
            $scope.operation = 'Create';
            $scope.customerEmail = "";
            $scope.customerSearchText = "";
            $scope.selectedSerialNumberFilter = "";
            $scope.agentText = null;
            $scope.currencyText = null;
            $scope.exchangeRateText = null;
            $scope.termsAndConditionText = null;
            $scope.projectText = null;
            $scope.shipingmethod = null;
            $scope.dt1 = "";
            $scope.shippingmethodreferenceno = "";
            $scope.referenceNo = "";
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



        $scope.getContactList = function (val) {
            $(".loader").css("display", "block");
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.post($scope.hiposServerURL  + '/getContactsList?searchContactText=' + val).then(function (response) {
                    var data = response.data;
                    $scope.contactList = angular.copy(data);
                   $("#selectContact").modal('show');
                    $scope.searchContactText = val;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                }
            )
            $scope.isCustomerSearch = false;
            $scope.isOtherContactSearch = true;
        };

        $scope.appendContact = function (otherContact) {
            $scope.searchContactText = otherContact.fullName;
            $scope.customerId = otherContact.otherContactId;
            // $scope.showEmailBox = false;
            $("#selectContact").modal('hide');
        }



        $scope.itemsNoStockList = [];
        $scope.openStockValidateMultipaymnet = function () {
            if (!$scope.isValidatedData()) {
                Notification.warning({
                    message: 'Please Enter Proper Data',
                    positionX: 'center',
                    delay: 2000
                });
            } else {
                $scope.getTotalAmtForSelectedItems();
                        $scope.totalVPBeforDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
                        // sowmya
                        // if (angular.isUndefined($scope.totalVPBeforDiscount) || $scope.totalVPBeforDiscount <= 0) {
                        //     Notification.error({
                        //         message: 'At lest One Account has to be selected',
                        //         positionX: 'center',
                        //         delay: 2000
                        //     });
                        //     return false;
                        // }
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
                        $scope.operation = "gtSales";
                        $("#paymentNew1").modal('show');
                   // }
                // }, function (error) {
                //     Notification.error({message: data.message, positionX: 'center', delay: 2000});
                // })
                //     .error(function (data, status, header, config) {
                //     Notification.error({message: data.message, positionX: 'center', delay: 2000});
                // });
            }
        };
        $scope.getClearPaymentList = function (checkVal, paymentType) {
            if (checkVal == false && paymentType == "amount") {
                var tempTotalVPAfterDiscount = $scope.totalVPAfterDiscount;
                var tempTotalVPAmountTendered = $scope.totalVPAmountTendered;
                var tempTotalVPAmountRefunded = parseFloat(0.00);
                if (parseFloat(tempTotalVPAmountTendered) > tempTotalVPAfterDiscount) {
                    tempTotalVPAmountRefunded = parseFloat(0.00);
                }
                else {
                    tempTotalVPAmountRefunded = parseFloat(0.00);
                }
                $scope.totalVPAmountTendered = parseFloat(0.00);
                $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
            }
            else if (checkVal == false && paymentType == "card") {
                angular.forEach($scope.cardPayementList, function (value, key) {
                    if (value.cardAmount > 0) {
                        value.cardAmount = parseFloat(0.00);
                        value.transactionNo = "";
                        $scope.cardPayementList.key = value.cardAmount;
                        $scope.cardPayementList.key = value.transactionNo;
                        $scope.checkbox = "unchecked";
                        $scope.OnChangeMultiPayament();
                    }
                });
            }
            else if (checkVal == false && paymentType == "voucher") {
                angular.forEach($scope.voucherPayementList, function (value, key) {
                    if (value.amt > 0) {
                        value.amt = parseFloat(0.00);
                        value.voucherNo = "";
                        $scope.voucherPayementList.key = value.amt;
                        $scope.voucherPayementList.key = value.voucherNo;
                        $scope.checkbox = "unchecked";
                        $scope.OnChangeMultiPayament();
                    }
                });
            }
            else if (checkVal == false && paymentType == "cash") {
                if ($scope.totalCPAmountTendered > parseFloat(0)) {
                    $scope.totalCPAmountTendered = parseFloat(0);
                    $scope.checkbox = "unchecked";
                    $scope.OnChangeMultiPayament();
                }
            }
        };
            $scope.CardEntity = function ($event) {
            if (!$event) {
                $scope.cards.forEach(function (card, index) {
                    if (index != 0) {
                        $scope.cards.splice(index, 1);
                        $scope.CardEntity($event);
                    }
                });
            }
        }

        $scope.vouchers = [{id: 'voucher_info1'}];
        $scope.addNewVoucher = function () {
            var newVoucherNo = $scope.vouchers.length + 1;
            $scope.vouchers.push({'id': 'voucher' + newVoucherNo});
        };
        $scope.voucherPayementList = $scope.vouchers;
        $scope.removeVoucher = function (index) {
            $scope.vouchers.splice(index, 1);
            $scope.voucherPayementList = $scope.vouchers;
            $scope.removePayments = "removeField";
            $scope.OnChangeMultiPayament();
        };
        $scope.cards = [{id: 'card_info1'}];
        $scope.addNewCard = function () {
            var newCard = $scope.cards.length + 1;
            $scope.cards.push({'id': 'card' + newCard});
        };
        $scope.cardPayementList = $scope.cards;
        $scope.removeCard = function (index) {
            $scope.cards.splice(index, 1);
            $scope.cardPayementList = $scope.cards;
            $scope.removePayments = "removeField";
            $scope.OnChangeMultiPayament();
        };
        $scope.onFocusElement;
        $scope.getSelectedTexElement = function ($event) {
            $scope.onFocusElement = $event.target;
            $scope.doGetCaretPosition($scope.onFocusElement);
        };
        $scope.OnChangeMultiPayament = function (oldValue, currentValue) {
            var onFocusedElement = $scope.onFocusElement.name;
            var voucherTotalAmt = parseFloat(0.00);
            var cardTotalAmt = parseFloat(0.00);
            var cashTotalAmt = parseFloat(0.00);
            var paymentAmount = parseFloat(0.00);
            cashTotalAmt = $scope.totalCPAmountTendered;
            if (angular.isUndefined(cashTotalAmt) || cashTotalAmt === "" ||
                !(angular.isNumber(parseFloat(cashTotalAmt)))) {
                cashTotalAmt = parseFloat(0);
            }
            if ($scope.removePayments == "removeField") {
                currentValue = parseFloat(1);
                $scope.removePayments = "";
            }
            angular.forEach($scope.voucherPayementList, function (value, key) {
                if (value.amt > 0) {
                    voucherTotalAmt = parseFloat(voucherTotalAmt) + parseFloat(value.amt);
                    $scope.checkbox = "";
                    currentValue = parseFloat(1);
                }
            });
            angular.forEach($scope.cardPayementList, function (value, key) {
                if (value.cardAmount > 0 || $scope.checkbox == "unchecked") {
                    cardTotalAmt = parseFloat(cardTotalAmt) + parseFloat(value.cardAmount);
                    $scope.checkbox = "";
                    currentValue = parseFloat(1);
                }
            });
            if (cardTotalAmt.toString() == "NaN") {
                cardTotalAmt = parseFloat(0);
            }
            paymentAmount = parseFloat(voucherTotalAmt) + parseFloat(cardTotalAmt) + parseFloat(cashTotalAmt);

            switch (onFocusedElement) {
                case "totalVoucherAmt":
                    var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
                    var tempTotalVPAfterDiscount = parseFloat(0.00);
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(currentValue) || currentValue === "" ||
                        !(angular.isNumber(parseFloat(currentValue)))) {
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    } else if (parseFloat($scope.totalVoucherAmt) > parseFloat($scope.totalVPBeforDiscount)) {
                        Notification.info({
                            message: 'Voucher Amount can not be greater than to be paid amount',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.totalVoucherAmt = parseFloat(oldValue);
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    }
                    if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
                        tempTotalVPAfterDiscount = parseFloat(0);
                        var reFoundAmt = parseFloat(0);
                        reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }
                    else {
                        $scope.totalVPAmountRefunded = (parseFloat(paymentAmount.toFixed(2)) - parseFloat(tempTotalVPBeforDiscount.toFixed(2)) - parseFloat($scope.cardAmount.toFixed(2))).toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                        // $scope.totalVPAmountRefunded=parseFloat(0);
                    }
                    // if (parseFloat(currentValue) > 0) {
                    //     tempTotalVPAfterDiscount = parseFloat(paymentAmount)-parseFloat(tempTotalVPBeforDiscount) - parseFloat($scope.cardAmount);
                    // }
                    // if (parseFloat($scope.totalVPAmountTendered) > 0) {
                    //     tempTotalVPAmountRefunded = parseFloat($scope.totalVPAmountTendered) - parseFloat(tempTotalVPAfterDiscount);
                    // }
                    // $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    // $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                    break;
                case "cardAmt":
                    var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
                    var tempTotalVPAfterDiscount = parseFloat(0.00);
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(currentValue) || currentValue === "" ||
                        !(angular.isNumber(parseFloat(currentValue)))) {
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    } else if (parseFloat($scope.cardAmount) > parseFloat($scope.totalVPBeforDiscount)) {
                        Notification.info({
                            message: 'Card  Amount can not be greater than to be paid amount',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.totalVoucherAmt = parseFloat(oldValue);
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    }
                    if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
                        tempTotalVPAfterDiscount = parseFloat(0);
                        var reFoundAmt = parseFloat(0);
                        reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }
                    else {
                        $scope.totalVPAmountRefunded = (parseFloat(paymentAmount.toFixed(2)) - parseFloat(tempTotalVPBeforDiscount.toFixed(2)) - parseFloat($scope.totalVoucherAmt.toFixed(2))).toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                        // $scope.totalVPAmountRefunded=parseFloat(0);
                    }
                    // if (parseFloat(currentValue) > 0) {
                    //     tempTotalVPAfterDiscount = parseFloat(paymentAmount)-parseFloat(tempTotalVPBeforDiscount) -parseFloat($scope.totalVoucherAmt);
                    // }
                    // if (parseFloat($scope.totalVPAmountTendered) > 0) {
                    //     tempTotalVPAmountRefunded = parseFloat($scope.totalVPAmountTendered) - parseFloat(tempTotalVPAfterDiscount);
                    // }
                    // $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    // $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                    break;
                case "totalVPAmountTendered":
                    var tempTotalVPAfterDiscount = $scope.totalVPAfterDiscount;
                    var tempTotalVPAmountTendered = $scope.totalVPAmountTendered;
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined($scope.totalCPAmountTendered) || $scope.totalCPAmountTendered === ""
                        || !(angular.isNumber(parseFloat($scope.totalCPAmountTendered)))) {
                        tempTotalVPAmountTendered = parseFloat(0.00);
                    }
                    if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
                        tempTotalVPAfterDiscount = parseFloat(0);
                        $scope.totalVPAmountRefunded = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }
                    else {
                        $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                        // $scope.totalVPAmountRefunded=parseFloat(0);
                    }
                    // if (parseFloat($scope.totalVPAfterDiscount) > parseFloat($scope.totalCPAmountTendered)  ) {

                    // }
                    // if (parseFloat($scope.totalCPAmountTendered) > parseFloat($scope.totalVPAfterDiscount)) {
                    //     tempTotalVPAfterDiscount=parseFloat(0.00);
                    //     tempTotalVPAmountRefunded = parseFloat(paymentAmount) - parseFloat($scope.totalVPBeforDiscount);
                    // }
                    // else{
                    //     tempTotalVPAmountRefunded = parseFloat(0.00);
                    // }
                    // $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                    $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    break;
            }
        };
        $scope.isValidatedData = function () {
            $scope.isValide = true;
            // sowmya
            // angular.forEach($scope.selectedAccountList, function (item, index) {
            //     if (angular.isUndefined(item.amtexclusivetax) || item.amtexclusivetax === '' || parseFloat(item.amtexclusivetax) <= 0) {
            //         $scope.isValide = false;
            //     }else if (angular.isUndefined(item.amtinclusivetax) || item.amtinclusivetax === '' || parseFloat(item.amtinclusivetax) <= 0) {
            //         $scope.isValide = false;
            //     }
            // });
            return $scope.isValide;
        };

        $scope.OnChangeCreditDiscount = function (oldValue) {
            if ($scope.totalCCPDiscount > $scope.totalCCPBeforDiscount) {
                Notification.info({
                    message: 'Discount cannot be greater than to be paid amount',
                    positionX: 'center',
                    delay: 2000
                });
                $scope.totalCCPDiscount = oldValue;
                return;
            }
            var totalCCPAfterDiscount = parseFloat($scope.totalCCPBeforDiscount - $scope.totalCCPDiscount)
            $scope.totalCCPAfterDiscount = parseFloat(totalCCPAfterDiscount.toFixed(2));
        };
        $scope.populateSaveSIMultiPayData = function (paymentType, operation) {
            $scope.BPDetails = [];
            $scope.CCPDetails = [];
            $scope.CPDetails = [];
            $scope.VPDetails = [];
            var CASHPAYMENT = $scope.CASHPAYMENT;
            var CARDPAYMENT = $scope.CARDPAYMENT;
            var OTHERSPAYMENT = $scope.OTHERSPAYMENT;
            var VOUCHERPAYMENT = $scope.VOUCHERPAYMENT;

            $scope.BANK_PAYMENT_DETAILS = [];
            $scope.CARD_PAYMENT_DETAILS = [];
            $scope.CASH_PAYMENT_DETAILS = [];
            $scope.VOUCHER_PAYMENT_DETAILS = [];
            var fromReg = "";
            var toReg = "";
            var userId = $("#userObj").val();
            var typeDoc = "PO";
            //console.log($scope.paymentDropdown);console.log($scope.getPaymentTypes);

            angular.forEach($scope.paymentDropdown, function (value, key) {
                if (!angular.isUndefined($scope.paymentDropdown[key].DefaultPaymentmethodId)) {
                    var paymentTypeId = $scope.paymentDropdown[key].DefaultPaymentmethodId;
                }
                angular.forEach($scope.getPaymentTypes, function (value1, key1) {
                    if ($.trim(paymentTypeId) == value1.paymentmethodId) {
                        //alert(value1.paymentmethodType);
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


                    }
                });
            });
            // console.log($scope.BANK_PAYMENT_DETAILS);
            // console.log($scope.CARD_PAYMENT_DETAILS);

            if ((CASHPAYMENT != true) && (CARDPAYMENT != true) && (OTHERSPAYMENT != true) && (VOUCHERPAYMENT != true)) {
                var confirm = $window.confirm("No Payment Type Is Selected Do You Want To Continue?");
                if (confirm == false) {
                    return false;
                }
                console.log("should not execute...........");
            }

            $scope.BPDetails = {
                // totalBPBeforDiscount: $scope.totalVPBeforDiscount,
                // totalBPDiscount: $scope.totalVPDiscount,
                // totalBPAmountTendered: $scope.totalPaidAmt,
                // totalBPAmountRefunded: $scope.totalVPAmountRefunded,
                multiBankPaymentList: $scope.BANK_PAYMENT_DETAILS
            };
            $scope.CCPDetails = {
                // totalBPBeforDiscount: $scope.totalVPBeforDiscount,
                // totalBPDiscount: $scope.totalVPDiscount,
                // totalBPAmountTendered: $scope.totalPaidAmt,
                // totalBPAmountRefunded: $scope.totalVPAmountRefunded,
                cardPaymentList: $scope.CARD_PAYMENT_DETAILS
            };
            $scope.CPDetails = {
                // totalBPBeforDiscount: $scope.totalVPBeforDiscount,
                // totalBPDiscount: $scope.totalVPDiscount,
                // totalBPAmountTendered: $scope.totalPaidAmt,
                // totalBPAmountRefunded: $scope.totalVPAmountRefunded,
                multiCashPaymentList: $scope.CASH_PAYMENT_DETAILS
            };
            $scope.VPDetails = {
                multiVoucherPayments: $scope.VOUCHER_PAYMENT_DETAILS
            };
            var data = {
                operation: operation,
                selectedAccountList: $scope.selectedAccountList,
                cashPayment: $scope.CPDetails,
                creditPayment: $scope.CCPDetails,
                voucherPayment: $scope.VPDetails,
                bankPayment: $scope.BPDetails,
                totalCheckOutamt: $scope.totalVPBeforDiscount,
                paymentType: paymentType,
                totalTaxAmt: $scope.totalTaxAmt,
                taxType: $scope.fullSimplTax,
                customerId: $scope.customerId,
                customerEmail: $scope.customerEmail,
                cutomerName: $scope.searchContactText,
                exchangerateId: $scope.exchangeRateId,
                exchangerateValue: $scope.exchangeRateText,
                currencyId: $scope.currencyText,
                termsandConditionsId: $scope.termsAndConditionText,
                agentId: $scope.agentText,
                shippingmethodId: $scope.shipingmethod,
                projectId: $scope.projectText,
                referenceNo: $scope.referenceNo,
                shippingReferenceNo: $scope.shippingmethodreferenceno,
                shippingDate: $scope.dt1,
                amountReturned: $scope.totalVPAmountRefunded,
                discountAmount: $scope.totalVPDiscount,
                totalTenderedAmount: $scope.totalPaidAmt,
                userName: $('#userName').val(),
                dateOfInvoice: $scope.dt,
                orNo: $scope.orNo,
                otherCustId:$scope.otherCustId,
                otherCustName: $scope.customerSearchText,
                advancepayment: $scope.advancepayment
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

        $scope.getprintlist = function () {
            $("#purchase_print_list_modal").modal('show');

        };

        $scope.getDuplicateReceipt = function (val) {
            $(".loader").css("display", "block");
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.get($scope.generalTransactionServerURL   + '/getDuplicateReceipt?searchText=' + val).then(function (response) {
                var data = response.data;
                $scope.receiptList = angular.copy(data);
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
        $scope.remove=function () {
            $scope.text = "";
            $scope.accountSearchText="";
        }
        $scope.saveContact = function () {
            if (angular.isUndefined($scope.contactNameText) || $scope.contactNameText == '') {
                Notification.warning({message: 'contact Name can not be empty', positionX: 'center', delay: 2000});
            }
            else {
                var saveCustomerDetails;
                saveCustomerDetails = {
                    fullName: $scope.contactNameText,
                    email:$scope.contactEmailText,
                    contactNumber: $scope.contactContactText,
                    address: $scope.contactAddressText,
                    companyRegNo: $scope.companyRegNo,
                    notificationFlag: $scope.notificationFlag,
                    from_Reg_Comp: $scope.fromRegNo,
                    to_Reg_Comp: $scope.toRegNo,
                    notificationId: $scope.notificationId,
                    gstCode: $scope.GSTINText,
                    state: $scope.selectedState,
                    personIncharge: $scope.personInchargeText,
                    country: $scope.selectedCountry,
                    currency: $scope.selectedCurrency,
                    bankName: $scope.bankNameText,
                    accountNo: $scope.accNoText,
                    branchName: $scope.bankBranchText,
                    iFSCCode: $scope.IFSCText,
                    website: $scope.websiteText,
                    panNO : $scope.panNumberText
                };
                $http.post($scope.hiposServerURL+ '/saveContact', angular.toJson(saveCustomerDetails, "Create"))
                    .then(function (response, status, headers, config) {
                        var data = response.data;
                        $scope.removeOtherContactsData();
                        $("#addContact").modal('hide');
                        if (!angular.isUndefined(data) && data !== null) {
                            $scope.customerSearchText = data.customerName;
                            $scope.customerId = data.customerId;
                            $scope.customerEmail = data.customerEmail;
                            $scope.notificationFlag = "";
                            $scope.fromRegNo = "";
                            $scope.contactNo1="/^[0-9]{10,10}$/;"
                        }
                        // $scope.getContactList();
                        Notification.success({message: 'Customer Created  successfully', positionX: 'center', delay: 2000});
                    }, function (error) {
                        Notification.error({
                            message: 'Something went wrong, please try again',
                            positionX: 'center',
                            delay: 2000
                        });
                    });
                };
        };
        $scope.gtprintDivA5 = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=300,height=300');
            popupWin.document.open();
            $("#closebtn").hide;
            $("#printbuttonbtn").hide;
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" media="print" href="poscss/recept_print.css"><link href="css/bootstrap.css" rel="stylesheet"></head><body style="width:595px;" onload="window.print()">' + printContents + '</body></html>');
            popupWin.document.close();
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
            $scope.customerSearchText = "";
            $scope.selectedSerialNumberFilter = "";


        }

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
            $scope.currencyText= $scope.countryId;
            $scope.truefalse=true;
                Notification.error({
                    message: 'Please Add Exchange rate value',
                    positionX: 'center',
                    delay: 2000
                });
            }
        }
        $scope.saveSIandDraft = function (paymentType) {
            if (!$scope.validatePayment(paymentType)) {
                return;
            }

            if (parseInt($scope.totalVPBeforDiscount) != parseInt($scope.totalPaidAmt)) {
                Notification.error({
                    message: 'Total Amount is not equal to Amount Tendered',
                    positionX: 'center',
                    delay: 2000
                });
            }
            else if (parseInt($scope.totalVPAmountRefunded) != 0) {
                Notification.error({
                    message: 'Amount Returned should be zero',
                    positionX: 'center',
                    delay: 2000
                });
            }else {
                $scope.isDisabled=true;
                $http.post($scope.generalTransactionServerURL  + '/saveDraftGtSales',
                    angular.toJson($scope.populateSaveSIMultiPayData(paymentType), "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    $("#cashpayment").modal('hide');
                    $("#creditcardpayment").modal('hide');
                    $("#voucherPayment").modal('hide')
                    $("#multiPayment1").modal('hide');
                    $("#paymentNew1").modal('hide');
                    $scope.removeAllItems();
                    $scope.clearPaymentValues();
                    $scope.numberToWord = toWords(data.amountPaid);
                    $scope.gtReceipt = data;
                    var $log = $("#log");
                    str = data.footer;
                    html = $.parseHTML(str);
                    $scope.orNo=null;
                    // Append the parsed HTML
                    $log.append(html);
                    if ($scope.gtReceipt.taxType == 'CGST:SGST/UGST') {
                        $scope.gtReceipt.splitTaxAmt = ($scope.gtReceipt.totalTaxAmt) / 2;
                    } else {
                        $scope.gtReceipt.splitTaxAmt = ($scope.gtReceipt.totalTaxAmt)
                    }
                    $("#gtOtherRecieptPrint").modal('show');
                    // $scope.populateSIResponceData(data, paymentType);
                    Notification.success({
                        message: 'Voucher has been saved successfully',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.isDisabled = false;
                    $scope.isCustomerSearch = true;
                    $scope.isOtherContactSearch = true;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.isDisabled = false;
                    $scope.isCustomerSearch = true;
                    $scope.isOtherContactSearch = true;
                });
            }
        };
        $scope.getGTSalesForEdit= function (formNo) {
            if (formNo === "") {
                Notification.info({message: "Please Enter Invoice No", positionX: 'center', delay: 2000});
                return;
            }
            $http.get($scope.generalTransactionServerURL  +'/getOtherGTSalesEdit/' + formNo).then(function (response, status, headers, config) {
                var data = response.data;
                $scope.operation='Edit';
                $scope.op='Edit';
                $scope.orNo=data.orNo;
                $scope.selectedAccountList = data.selectedAccountList;
                $scope.searchContactText=data.supplierName;
                $scope.customerSearchText=data.cutomerName;
                $scope.customerId=data.customerId;
                $scope.otherCustId=data.otherCustId;
                $scope.dt=new Date(data.orDate);
                // angular.forEach($scope.selectedAccountList,function (value,key) {
                //     $scope.func(value.gtAmountExcTax,value.taxid,key);
                // });
                $scope.updatesimplifiedTax();
                $("#receiptDuplicatePrint").modal('hide');
            },function (error) {
                if (error.status == 500) {
                    Notification.error({message: "Something went wrong in server", positionX: 'center', delay: 2000});
                } else {
                    Notification.error({message: data.message, positionX: 'center', delay: 2000});
                }
            })
        };

        $scope.cancelGtSAles=function (no) {
            $http.post($scope.generalTransactionServerURL +'/cancelGtReceipt?invoiceNo=' + no).then(function (response) {
                var data = response.data;
                Notification.info({message: 'Canceled Successfully', positionX: 'center', delay: 2000})
                $("#receiptDuplicatePrint").modal('hide');
            });
        };


        $scope.postGtSales = function (formNo,index) {
            if (formNo === "") {
                Notification.info({message: "Please Enter Invoice No", positionX: 'center', delay: 2000});
                return;
            }
            $http.get($scope.generalTransactionServerURL   +'/postGtforSales/' + formNo, "Create").then(function (response, status, headers, config) {
                $scope.receiptList[index].piStatus="Prepared";
                Notification.info({message: "Posted", positionX: 'center', delay: 2000});
                $scope.removeAllItems();
                $scope.clearPaymentValues();
            },function (error) {
                if (error.status == 500) {
                    Notification.error({message: "Something went wrong in server", positionX: 'center', delay: 2000});
                } else {
                    Notification.error({message: data.message, positionX: 'center', delay: 2000});
                }
            })
        };
        $scope.clearPaymentValues =function(){
            $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];
            $scope.paymentList = [];
            $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
        }
        $scope.next_wizard = function(){
            $("#cus_step1").removeClass("in active");
            $("#cus_step2").addClass("tab-pane fade in active");
        }
        $scope.back_wizard = function(){
            $("#cus_step2").removeClass("in active");
            $("#cus_step1").addClass("tab-pane fade in active");
        }
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
        $scope.isCustomerSearch = true;
        $scope.isOtherContactSearch = true;
        $scope.removeCustomerDetails = function () {
            $scope.customerNameText = "";
            $scope.customerEmailText = "";
            $scope.customerContactText = "";
            $scope.customerContactText = "";
            $scope.customerAddressText = "";
            $scope.fromRegNo = "";
            $scope.companyRegNo = "";
            $scope.Hi_Conn_company_Name = false;
        };
        $scope.notificationFlag = "";
        $scope.updateCustomerId = function (newCustVal) {
            $scope.customer = newCustVal.customerId;
            $scope.custId = newCustVal.customerId;
            $scope.removeAllItems();
        }
        $scope.getCustomerListSearch = function (val) {
            $(".loader").css("display", "block");
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.post($scope.hiposServerURL  + '/getCustomerListSearch?searchCustomerText=' + val).then(function (response) {
                    var data = response.data;
                    $scope.customerList = angular.copy(data);
                    $("#selectCustomer").modal('show');
                    $scope.searchCustomerText = val;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                }
            )
            $scope.isCustomerSearch = true;
            $scope.isOtherContactSearch = false;
        };
        $scope.next_wizard = function(){
            $("#addCustomer  #cus_step1").removeClass("in active");
            $("#addCustomer  #cus_step2").addClass("tab-pane fade in active");
        }

        $scope.back_wizard = function(){
            $("#addCustomer #cus_step2").removeClass("in active");
            $("#addCustomer #cus_step1").addClass("tab-pane fade in active");
        }
        $scope.appendCustomer = function (customerId) {
            $scope.customerSearchText = customerId.customerName;
            $scope.otherCustId = customerId.customerId;
            $scope.showEmailBox = false;
            $scope.removeAllItemsWithoutCustomer();
            $("#selectCustomer").modal('hide');
            $scope.op = "Create";

        }
        $scope.saveMultiPaySI = function (paymentType) {
            if (!$scope.validatePayment(paymentType)) {
                return;
            }
            // $scope.isDisabled=true;
            if (parseInt($scope.totalVPBeforDiscount) != parseInt($scope.totalPaidAmt)) {
                Notification.error({
                    message: 'Total Amount is not equal to Amount Tendered',
                    positionX: 'center',
                    delay: 2000
                });
            }
            else if (parseInt($scope.totalVPAmountRefunded) != 0) {
                Notification.error({
                    message: 'Amount Returned should be zero',
                    positionX: 'center',
                    delay: 2000
                });
            }else {
                $scope.isDisabled= true;
                $http.post($scope.generalTransactionServerURL  + '/saveOtherRecieptGt',
                    angular.toJson($scope.populateSaveSIMultiPayData(paymentType), "Create")).then(function (response, status, headers, config) {
                    $scope.isDisabled= false;
                    var data = response.data;
                    $scope.numberToWord = toWords(data.amountPaid);
                    $("#cashpayment").modal('hide');
                    $("#creditcardpayment").modal('hide');
                    $("#voucherPayment").modal('hide')
                    $("#multiPayment1").modal('hide');
                    $("#paymentNew1").modal('hide');
                    $scope.removeAllItems();
                    $scope.gtReceipt = data;
                    var $log = $("#log");
                    str = data.footer;
                    html = $.parseHTML(str);
                    // Append the parsed HTML
                    $log.append(html);
                    if ($scope.gtReceipt.taxType == 'CGST:SGST/UGST') {
                        $scope.gtReceipt.splitTaxAmt = ($scope.gtReceipt.totalTaxAmt) / 2;
                    } else {
                        $scope.gtReceipt.splitTaxAmt = ($scope.gtReceipt.totalTaxAmt)
                    }
                    $("#gtOtherRecieptPrint").modal('show');
                    $scope.populateSIResponceData(data, paymentType);
                    Notification.success({
                        message: 'Voucher has been saved successfully',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.orNo=null;
                    $scope.isDisabled = false;
                    $scope.isCustomerSearch = true;
                    $scope.isOtherContactSearch = true;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.isDisabled = false;
                    $scope.isCustomerSearch = true;
                    $scope.isOtherContactSearch = true;
                });
            }
        };
        // $scope.getBankAccountList = function (ind) {
        //     if (angular.isUndefined($scope.searchText)) {
        //         $scope.searchText = "";
        //     }
        //     if(ind!=undefined){
        //         $scope.ind = ind;
        //         $scope.searchText = "";
        //     }
        //     $http.post($scope.hiposServerURL  + '/getBankList?accountSearchText=' + $scope.searchText).then(function (response) {
        //         var data = response.data;
        //         $scope.accountList = angular.copy(data);
        //         $("#selectAccount1").modal('show');
        //         $scope.accountSearchText =$scope.searchText;
        //         $scope.searchText =$scope.searchText;
        //     }, function (error) {
        //         Notification.error({
        //             message: 'Something went wrong, please try again',
        //             positionX: 'center',
        //             delay: 2000
        //         });
        //     })
        // };
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
                //$scope.paymentDropdown[$scope.paymentDropdown.length-1].DefaultPaymentmethodId = filteredData[0].paymentmethodId;
                $scope.getPaymentTypesList1(newddpIndex, $scope.paymentDropdown.length, 'ADDNEWSECTION');
                /***remove all CASH TYPE payments from drop down array***/
                if ($scope.CASHPAYMENT == 'true' || $scope.CASHPAYMENT == true) {
                    function filterPaymentmethodType(pay) {
                        return pay.paymentmethodType != 'Cash';
                    }

                    var filteredData = $scope.paymentList[0].filter(filterPaymentmethodType);
                    $scope.paymentList[payInd] = filteredData;
                } else {
                    $scope.paymentList[payInd] = $scope.paymentList[payInd - 1];
                }
            }
        };
        $scope.gtsalesopenpayment = function () {
            if (angular.isUndefined($scope.selectedAccountList) || $scope.selectedAccountList.length <= 0) {
                Notification.error({
                    message: 'At lest One Account has to be selected',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            else if (angular.isUndefined($scope.searchContactText) || $scope.searchContactText == '') {
                Notification.warning({message: 'contact Name can not be empty', positionX: 'center', delay: 2000});
            }
            else {
                $scope.openStockValidateMultipaymnet();
                $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
            }

        };
        $scope.selectPaymentType = function (selectedOption, ddpStr, payInd) {
            $('.bankInfoHide').css('display', 'block');
            var data = $scope.paymentList[payInd - 1];
            //$scope.selectedPaymentType = [];
            angular.forEach(data, function (value, key) {
                if (value.paymentmethodId == selectedOption) {
                    var selectedPaymentType = value.paymentmethodType;
                    if (selectedPaymentType == 'Cash') {
                        $scope.CASHPAYMENT = true;
                        $('#PaymentTypeCash' + ddpStr).show();
                        $('#PaymentTypeCash' + ddpStr).siblings().hide();

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
                    if (selectedPaymentType == 'Card') {
                        $scope.CARDPAYMENT = true;
                        $('#PaymentTypeCard' + ddpStr).show();
                        $('#PaymentTypeCard' + ddpStr).siblings().hide();
                    } else {
                        $scope.CARDPAYMENT = false;
                        $('#PaymentTypeCard' + ddpStr).hide();
                    }
                    if (selectedPaymentType == 'Bank') {
                        $scope.OTHERSPAYMENT = true;
                        $('#PaymentTypeBank' + ddpStr).show();
                        $('#PaymentTypeBank' + ddpStr).siblings().hide();
                    } else {
                        $scope.OTHERSPAYMENT = false;
                        $('#PaymentTypeBank' + ddpStr).hide();
                    }
                    if (selectedPaymentType == 'Voucher') {
                        $scope.VOUCHERPAYMENT = true;
                        $('#PaymentTypeVoucher' + ddpStr).show();
                        $('#PaymentTypeVoucher' + ddpStr).siblings().hide();
                    } else {
                        $scope.VOUCHERPAYMENT = false;
                        $('#PaymentTypeVoucher' + ddpStr).hide();
                    }


                }
            });
        };
        $scope.paymentList=[];
        $scope.getPaymentTypesList1 = function (paymentTypeIndex, payIndex, flag) {
            //alert('onload');
            //alert(paymentTypeIndex+' ** '+payIndex);
            if (angular.isUndefined(paymentTypeIndex)) {
                paymentTypeIndex = 1;
            }
            $http.get($scope.hiposServerURL + '/getPaymentTypes').then(function (response) {
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
                        $('.bankInfoHide').css('display','block');
                    }else if(value.defaultType == '' && value.defaultType == null && value.defaultType == undefined){
                        //**if default value is not present in dropdown
                        $('.bankInfoHide').css('display','none');
                    }
                });
                var paymentmethodType=$scope.DEFAULT_PAYMENT_TYPE;
                if (flag == 'ONLOAD') {
                    //alert(paymentmethodType);
                    if (paymentmethodType == 'Cash') {
                        $('.bankInfoHide').css('display','block');
                        $scope.CASHPAYMENT = true;
                        $('#PaymentTypeCashpaymentDropdown' + paymentTypeIndex).show();
                        $('#PaymentTypeCashpaymentDropdown' + paymentTypeIndex).siblings().hide();
                    } else {
                        $scope.CASHPAYMENT = false;
                        $('#PaymentTypeCashpaymentDropdown' + paymentTypeIndex).hide();
                    }
                    if (paymentmethodType == 'Card') {
                        $('.bankInfoHide').css('display','block');
                        $scope.CARDPAYMENT = true;
                        $('#PaymentTypeCardpaymentDropdown' + paymentTypeIndex).show();
                        $('#PaymentTypeCardpaymentDropdown' + paymentTypeIndex).siblings().hide();
                    } else {
                        $scope.CARDPAYMENT = false;
                        $('#PaymentTypeCardpaymentDropdown' + paymentTypeIndex).hide();
                    }
                    if (paymentmethodType == 'Bank') {
                        $('.bankInfoHide').css('display','block');
                        $scope.OTHERSPAYMENT = true;
                        $('#PaymentTypeBankpaymentDropdown' + paymentTypeIndex).show();
                        $('#PaymentTypeBankpaymentDropdown' + paymentTypeIndex).siblings().hide();
                    } else {
                        $scope.OTHERSPAYMENT = false;
                        $('#PaymentTypeBankpaymentDropdown' + paymentTypeIndex).hide();
                    }
                    if (paymentmethodType == 'Voucher') {
                        $('.bankInfoHide').css('display','block');
                        $scope.VOUCHERPAYMENT = true;
                        $('#PaymentTypeVoucherpaymentDropdown' + paymentTypeIndex).show();
                        $('#PaymentTypeVoucherpaymentDropdown' + paymentTypeIndex).siblings().hide();
                    } else {
                        $scope.VOUCHERPAYMENT = false;
                        $('#PaymentTypeVoucherpaymentDropdown' + paymentTypeIndex).hide();
                    }
                }else if(flag == 'ADDNEWSECTION'){
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
        $scope.removeSection = function (index) {
            $scope.paymentDropdown.splice(index, 1);
            $scope.paymentDropdown = $scope.paymentDropdown;
            $scope.removePayments = "removeField";
            $scope.OnChangeMultiPayament('',currentValue=undefined,'REMOVE',index);
        };
        $scope.OnChangeMultiPayament = function (oldValue, currentValue, onFocusInputName, paymentDropdownIndex) {
            if(parseFloat($scope.totalTCSAmountTendered) > 0)
            {
                $scope.totalTDSAmountTendered = parseFloat(0.00);
            }
            if(parseFloat($scope.totalTDSAmountTendered) > 0)
            {
                $scope.totalTCSAmountTendered = parseFloat(0.00);
            }
            //alert(oldValue+' ## '+currentValue+' ## '+onFocusInputName +' ## '+ paymentDropdownIndex);
            var totalAmount = parseFloat(0.00);
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
                }else if('voucherAmt' in payment){
                    totalAmount = totalAmount + parseFloat(payment.voucherAmt);
                    currentValue = parseFloat(1);
                }
            });
            //console.log(totalAmount);
            if ($scope.removePayments == "removeField") {
                currentValue = undefined;
                $scope.removePayments = "";
            }
            if (totalAmount.toString() == "NaN") {
                totalAmount = parseFloat(0);
            }
            if($scope.totalTCSAmountTendered == undefined){
                $scope.totalTCSAmountTendered = parseFloat(0.00);
            }
            if($scope.totalTDSAmountTendered == undefined){
                $scope.totalTDSAmountTendered = parseFloat(0.00);
            }
            var paymentAmount = totalAmount + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
            var onFocusedElement = onFocusInputName;
            //alert(onFocusedElement +'&&&'+ currentValue);
            switch (onFocusedElement) {
                case "TCS":
                    var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
                    var tempTotalVPAfterDiscount = parseFloat(0.00);
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(currentValue) || currentValue === "" ||
                        !(angular.isNumber(parseFloat(currentValue)))) {
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    } else if (parseFloat($scope.totalTCSAmountTendered) > parseFloat($scope.totalVPBeforDiscount)) {
                        Notification.info({
                            message: 'TCS Amount can not be greater than to be paid amount',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.totalTCSAmountTendered = parseFloat(oldValue);
                        var paymentAmount = totalAmount + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    }

                    if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
                        tempTotalVPAfterDiscount = parseFloat(0);
                        var reFoundAmt = parseFloat(0);
                        reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }else {
                        $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }
                    $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    break;
                case "TDS":
                    var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
                    var tempTotalVPAfterDiscount = parseFloat(0.00);
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(currentValue) || currentValue === "" ||
                        !(angular.isNumber(parseFloat(currentValue)))) {
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    } else if (parseFloat($scope.totalTDSAmountTendered) > parseFloat($scope.totalVPBeforDiscount)) {
                        Notification.info({
                            message: 'TDS Amount can not be greater than to be paid amount',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.totalTDSAmountTendered = parseFloat(oldValue);
                        var paymentAmount = totalAmount + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    }
                    if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
                        tempTotalVPAfterDiscount = parseFloat(0);
                        var reFoundAmt = parseFloat(0);
                        reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }else {
                        $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2) );
                    }
                    $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    break;
                case "BANK":
                    var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
                    // alert(tempTotalVPBeforDiscount);
                    var tempTotalVPAfterDiscount = parseFloat(0.00);
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(currentValue) || currentValue === "" ||
                        !(angular.isNumber(parseFloat(currentValue)))) {
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    } else if (parseFloat($scope.paymentDropdown[paymentDropdownIndex].bankAmount) > parseFloat($scope.totalVPBeforDiscount)) {
                        Notification.info({
                            message: 'Bank Amount can not be greater than to be paid amount',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.paymentDropdown[paymentDropdownIndex].bankAmount = parseFloat(oldValue);
                        var paymentAmount = totalAmount + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    }
                    if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
                        tempTotalVPAfterDiscount = parseFloat(0);
                        var reFoundAmt = parseFloat(0);
                        reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    } else {
                        $scope.totalVPAmountRefunded = parseFloat(paymentAmount.toFixed(2)) - parseFloat(tempTotalVPBeforDiscount.toFixed(2));
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }
                    $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    break;
                case "VOUCHER":
                    var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
                    var tempTotalVPAfterDiscount = parseFloat(0.00);
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(currentValue) || currentValue === "" ||
                        !(angular.isNumber(parseFloat(currentValue)))) {
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    } else if (parseFloat($scope.paymentDropdown[paymentDropdownIndex].voucherAmt) > parseFloat($scope.totalVPBeforDiscount)) {
                        Notification.info({
                            message: 'Voucher Amount can not be greater than to be paid amount',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.paymentDropdown[paymentDropdownIndex].voucherAmt = parseFloat(oldValue);
                        var paymentAmount = totalAmount + parseFloat($scope.totalTCSAmountTendered) + parseFloat($scope.totalTDSAmountTendered);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    }
                    if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
                        tempTotalVPAfterDiscount = parseFloat(0);
                        var reFoundAmt = parseFloat(0);
                        reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    } else {
                        $scope.totalVPAmountRefunded = (parseFloat(paymentAmount.toFixed(2)) - parseFloat(tempTotalVPBeforDiscount.toFixed(2)));
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }
                    $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    break;
                case "CARD":
                    var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
                    var tempTotalVPAfterDiscount = parseFloat(0.00);
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(currentValue) || currentValue === "" ||
                        !(angular.isNumber(parseFloat(currentValue)))) {
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    } else if (parseFloat($scope.paymentDropdown[paymentDropdownIndex].cardAmount) > parseFloat($scope.totalVPBeforDiscount)) {
                        Notification.info({
                            message: 'Card  Amount can not be greater than to be paid amount',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.paymentDropdown[paymentDropdownIndex].cardAmount = parseFloat(oldValue);
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    }
                    if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
                        tempTotalVPAfterDiscount = parseFloat(0);
                        var reFoundAmt = parseFloat(0);
                        reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    } else {
                        $scope.totalVPAmountRefunded = parseFloat(paymentAmount.toFixed(2)) - parseFloat(tempTotalVPBeforDiscount.toFixed(2));
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }
                    $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    break;
                case "CASH":
                    var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
                    var tempTotalVPAfterDiscount = parseFloat(0.00);
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(currentValue) || currentValue === "" ||
                        !(angular.isNumber(parseFloat(currentValue)))) {
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    } else if (parseFloat($scope.paymentDropdown[paymentDropdownIndex].totalCPAmountTendered) > parseFloat($scope.totalVPBeforDiscount)) {
                        Notification.info({
                            message: 'Cash  Amount can not be greater than to be paid amount',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.paymentDropdown[paymentDropdownIndex].totalCPAmountTendered = parseFloat(oldValue);
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                    }
                    if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
                        tempTotalVPAfterDiscount = parseFloat(0);
                        var reFoundAmt = parseFloat(0);
                        reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }else {
                        $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
                        $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    }
                    $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    break;
                case "REMOVE":
                    var totalAmount = parseFloat(0.00);
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
                        }
                    });
                    if (totalAmount.toString() == "NaN") {
                        totalAmount = parseFloat(0);
                    }
                    var paymentAmount = totalAmount;
                    $scope.totalVPAmountRefunded = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
                    $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
                    break;


            }
        };
        $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
        $scope.duplicateReceiptPrint = function (paymentID) {
            $http.post($scope.generalTransactionServerURL  + '/duplicateReceiptPrint/' + paymentID).then(function (response) {
                var data = response.data;
                $scope.gtReceipt = data;
                $scope.numberToWord = toWords(data.amountPaid);
                // $scope.currencyCode = data.companyData. currencyId.currencyCode;
                // $scope.currencyName = data.companyData. currencyId.currencyName;
                // if($scope.gtReceipt.taxType == 'CGST:SGST/UGST'){
                //     $scope.gtReceipt.splitTaxAmt = ($scope.gtReceipt.totalTaxAmt)  / 2;
                // }else {
                //     $scope.gtReceipt.splitTaxAmt = ($scope.gtReceipt.totalTaxAmt)
                // }
                $("#receiptDuplicatePrint").modal('hide');
                $("#gtOtherRecieptPrint").modal('show');
            });
        };
    });
