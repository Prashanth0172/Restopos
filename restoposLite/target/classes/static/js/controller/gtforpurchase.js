
app.controller('gtforpurchaseCtrl',
    function($scope, $http, $location, $filter, Notification, ngTableParams,  $timeout, $window) {
        $("#generaltransaction").addClass('active');
        $('#generaltransaction').siblings().removeClass('active');
        $("#expense").addClass('active');
        $('#expense').siblings().removeClass('active');
        // body...
        $scope.hiposServerURL =  "/hipos/";
        $scope.generalTransactionServerURL = "/gt";
        $scope.returnQty = parseFloat('0.00');
        $scope.totalBeforDiscount = parseFloat('0.00');
        $scope.totalDiscount = parseFloat('0.00');
        $scope.totalAfterDiscount = parseFloat('0.00');
        $scope.totalTaxAmt = parseFloat('0.00');
        $scope.supplierId = 1;
        $scope.countVal = 0;
        $scope.supplierInvNo= "";
        $scope.supplierEmail = "";
        $scope.userRights = [];
        $scope.operation = 'Create';
        $scope.cursorPosVal = 0;
        $scope.supplier = 1;
        $scope.customer = 1;
        $scope.SIId = 0;
        $scope.taxList = [];
        $scope.serializableItemsList = [];
        $scope.itemList = [];
        $scope.itemCategoryDTOList=[];
        $scope.itemTypeDTOList=[];
        $scope.itemUOMTypeDTOList=[];
        $scope.itemMSICDTOList=[];
        $scope.itemBrandDTOList=[];
        $scope.itemCountTypeDTOList=[];
        $scope.itemIPTaxDTOList=[];
        $scope.itemOPTaxDTOList=[];
        $scope.posPurchaseList = [];
        $scope.posAdvancePurchaseList = [];
        $scope.SIList = [];
        $scope.invokeOrderId = '';
        $scope.InvokeOrderList = [];
        $scope.selectedItemsList = [];
        $scope.purchasePostData = [];
        $scope.supplierSearchText = "";
        $scope.cardPayementList = [];
        $scope.voucherPayementList = [];
        $scope.removePayments = "";
        $scope.checkbox = "";
        $scope.totaldupltax = parseFloat(0);
        $scope.totaltaxdupltax = parseFloat(0);
        $scope.serialNo = "";
        $scope.supplierNameText = "";
        $scope.supplierDetails=[];
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
        $scope.number =/^[0-9]/;
        $scope.fullUserName="";
        $scope.regEX =/^[0-9]{1,8}(\.[0-9]+)?$/;
        $scope.displayAccountLength=0;
        // $scope.tax = "SimplifiedTax";
        // $scope.fullSimplTax ='Full Tax';
        // $scope.simplifiedTax ='Full Tax';

        var location = window.location.origin;

        //setting date restriction in calender/datepicker
        // (function(){
        //     var dateRange, splitDate, interval = 300; //msec
        //     var setDateRestriction = function(){
        //         dateRange = document.getElementById("topHeaderFinancialDate").innerHTML;
        //         splitDate = dateRange.split("-");
        //         if (splitDate[0].trim().length > 0 && splitDate[1].trim().length > 0) {
        //             var range = dateManagerServ.getDateRange();
        //             if(range.min<new Date() && new Date()<range.max){
        //                 $scope.dt=new Date();
        //                 $scope.dt1 = new Date();
        //             }else {
        //                 $scope.dt=range.min;
        //                 $scope.dt1 = range.min;
        //             }
        //             $scope.dateOptions = {
        //                 minDate : range.min,
        //                 maxDate : range.max
        //             };
        //         }else{
        //             $timeout(setDateRestriction, interval);
        //         }
        //     };
        //     $timeout(setDateRestriction, 0);
        // })();
        $scope.today = function() {
            $scope.dt = new Date();
            $scope.dt1 = new Date();
        };
        $scope.today();
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
            //$scope.simplifiedTax = newCustVal.fullSimplTax;
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

        $scope.updateSupplierId = function (newCustVal) {
            $scope.supplier = newCustVal.supplierId;
            $scope.removeAllItems();
        }
        $scope.barCodeInputfocus = function () {
            $("#barcodeInput").focus();
        };
        $scope.barCodeInputfocus();
        $scope.serialBarCodeInputfocus = function () {
            $("#serialBarcodeInput").focus();
        };
        $scope.serialBarCodeInputfocus();
        $scope.companyLogoPath = "";
        // $scope.getPageLoadData = function () {
        //     $("#multiprintreceipt").modal('hide');
        //     $http.get($scope.generalTransactionServerURL +  '/getPageLoadData').then(function (response) {
        //         var data = response.data;
        //         if (!angular.isUndefined(data) && data !== null && data != 'invalid') {
        //             $scope.taxList = data.taxList;
        //             $scope.userRights = data.userRights;
        //             $scope.supplierList = data.suppliers;
        //             $scope.companyLogoPath = location + "/" + data.companyLogoPath;
        //             $scope.supplierId ="";
        //             $scope.fullSimplTax = $scope.taxTypes[0].value;
        //             $scope.MYRSimplTax = $scope.taxTypesMYR[0].value;
        //             $scope.supplierSearchText = "Enter Pay To";
        //             $scope.companyName = data.companyName;
        //             $scope.fullUserName = data.fullUserName;
        //             $scope.taxListInDict = {};
        //             $scope.termsAndConditionList = data.termsAndConditionList;
        //             $scope.exchangeRateList = data.exchangeRateList;
        //             $scope.agentList = data.agentList;
        //             $scope.shippingMethod=data.shippingMethodList;
        //             $scope.currencyList = data.currencyList;
        //             $scope.countryId = data.countryId;
        //             $scope.currencyText = data.countryId;
        //             $scope.exchangeRateText =1;
        //             $scope.projectList = data.projectList;
        //             $scope.budgetList = data.budgetList;
        //             $scope.countryId = data.countryId;
        //             angular.forEach($scope.exchangeRateList,function (value,key) {
        //                 if (value.currencyId === $scope.currencyText) {
        //                     $scope.exchangeRateId=value.exchangeRateId;
        //                     $scope.exchangeRateText=value.exchangeRateValue;
        //                 }
        //             });
        //             angular.forEach($scope.taxList, function(value){
        //                 $scope.taxListInDict[value.taxString.split("|")[1].trim()] = value.taxid;
        //             });
        //         } else {
        //             $cookies.remove('accessToken');
        //             Notification.error({
        //                 message: 'Your session expired.Please login again',
        //                 positionX: 'center',
        //                 delay: 2000
        //             });
        //             $window.location.href = "hinextlogin.html";
        //         }
        //     })
        // };

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

        $scope.addContact = function () {

            $scope.status="Active"
            $(".loader").css("display", "block");
            // $scope.status = "Active";
            $('#modelName').text("Add Contact");
            $("#submit").text("Save");
            $("#add_new_contact_modal").modal('show');
        };

        $scope.saveContact = function () {
            if (angular.isUndefined($scope.contactEmailText) || $scope.contactEmailText == ''||$scope.contactEmailText == null) {
                Notification.warning({message: 'Please Enter EmailId', positionX: 'center', delay: 2000});
            } else {
                var saveCustomerDetails;
                saveCustomerDetails = {
                    fullName: $scope.contactNameText,
                    email: $scope.contactEmailText,
                    contactNumber: $scope.contactContactText,
                    address: $scope.contactAddressText,
                    companyRegNo: $scope.companyRegNo,
                    notificationFlag: $scope.notificationFlag,
                    from_Reg_Comp: $scope.fromRegNo,
                    to_Reg_Comp: $scope.toRegNo,
                    notificationId: $scope.notificationId,
                    gstCode: $scope.GSTINText,
                    state: $scope.stateId,
                    personIncharge: $scope.personInchargeText,
                    country: $scope.countryId,
                    currency: $scope.selectedCurrency,
                    bankName: $scope.bankNameText,
                    accountNo: $scope.accNoText,
                    contStatus: $scope.contStatusText,
                    branchName: $scope.bankBranchText,
                    iFSCCode: $scope.IFSCText,
                    website: $scope.websiteText,
                    panNO: $scope.panNumberText,
                    otherContactId: $scope.otherContactId,
                    status: $scope.status
                };
                $http.post($scope.bshimServerURL + '/saveContact', angular.toJson(saveCustomerDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if (data == "") {
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    } else {
                        $scope.removeOtherContactsData();
                        $scope.getContactList();
                        $("#add_new_contact_modal").modal('hide');
                        if (!angular.isUndefined(data) && data !== null) {
                            $scope.searchText = "";
                        }
                        Notification.success({
                            message: 'Contact Created  successfully',
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
            }

        };

        $scope.removeOtherContactsData = function () {
            $scope.customerId="";
            $scope.contactNameText="";
            $scope.GSTINText="";
            $scope.stateId="";
            $scope.contactContactText="";
            $scope.contactEmailText="";
            $scope.contactAddressText="";
            $scope.personInchargeText="";
            $scope.countryId="";
            $scope.selectedCurrency="";
            $scope.bankNameText="";
            $scope.accNoText="";
            $scope.panNumberText="";
            $scope.bankBranchText="";
            $scope.IFSCText="";
            $scope.websiteText="";
        }


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
        $scope.addSelectedAccountList = function (data) {
            var taxxid=2;
            var count = $scope.countVal;
            $scope.taxIndex = $scope.taxIndexOf($scope.taxList, taxxid);
            var taxPercent = $scope.taxList[$scope.taxIndex].taxString.split('|');
            var unitPrice = data.salesPrice;
            var qty = 1;
            var amtexclusivetax = (parseFloat(unitPrice) * parseFloat(qty));
            var discountAmt = 0;
            var taxamt = (parseFloat(amtexclusivetax) - discountAmt) * ((parseFloat(taxPercent[0]) / 100));
            var serializableItems = $scope.serializableItemsIndex;
            var amtinclusivetax = ((parseFloat(amtexclusivetax) + parseFloat(taxamt)) - parseFloat(discountAmt));
            console.log("-------4444444444-----------"),
                console.log(data),
                $scope.selectedAccountList.push({
                    //   accountCode: data.accountCode,
                    //      accountid: data.accountid,
                    account_name: data.account_name

                });

            $scope.countVal = count + 1;
            $scope.serializableItemsList[$scope.countVal] = data.serializableItemsDTOList;



            $scope.getTotalAmtForSelectedItems();
        };

        // $scope.getPageLoadData();
        $scope.supplier = 0;


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
        };

        $scope.appendContact = function (otherContact) {
            $scope.searchContactText = otherContact.fullName;
            $scope.supplierId = otherContact.otherContactId;
            $("#selectContact").modal('hide');
        }


        $scope.supplier = 0;


        $scope.printDivA5 = function (divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var popupWin = window.open('', '_blank', 'width=300,height=300');
            popupWin.document.open();
            $("#close").hide;
            $("#printbutton").hide;
            popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" media="print" href="poscss/recept_print.css"><link href="css/bootstrap.css" rel="stylesheet"></head><body style="width:595px;" onload="window.print()">' + printContents + '</body></html>');
            popupWin.document.close();
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
            $scope.appendSupplier = function (supplierId) {
            $scope.supplierSearchText = supplierId.supplierName;
            $scope.supplierId = supplierId.supplierId;
            $scope.showEmailBox = false;
            $scope.removeAllItemsWithoutSupplier();
            $("#selectSupplier").modal('hide');

        }

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
            $scope.totalCheckOutamt = parseFloat(totalAmt.toFixed(2));
            $scope.totalBeforDiscount = parseFloat(totalAmt.toFixed(2));
            $scope.totalDiscount = parseFloat(totalDiscountAmt.toFixed(2));
            $scope.totalAfterDiscount = parseFloat(totalAfterDiscount.toFixed(2));
            $scope.totalTaxAmt = parseFloat(totalTaxAmt.toFixed(2));
            $scope.returnQty = parseFloat(totalQty.toFixed(2));
        };
        $scope.getTotalAmtToBeReturned = function (oldValue) {
            if (parseFloat($scope.totalCPAmountTendered) > 0) {
                $scope.totalCPAmountRefunded = parseFloat($scope.totalCPAmountTendered) - parseFloat($scope.totalCPAfterDiscount);
            }
        };
        $scope.getTotalVoucherAmtToBeReturned = function () {
            if ($scope.totalVPAmountTendered > $scope.totalVPAfterAllDeductions) {
                $scope.totalVPAmountRefunded = parseFloat($scope.totalVPAmountTendered) - parseFloat($scope.totalVPAfterAllDeductions);
            } else {
                $scope.totalVPAmountRefunded = 0.00;
            }
        };
        $scope.OnChangeCashDiscount = function (oldValue, currentValue) {
            var onFocusedElement = $scope.onFocusElement.id;
            switch (onFocusedElement) {
                case "totalCPDiscount":
                    var tempTotalCPBeforDiscount = $scope.totalCPBeforDiscount;
                    var tempTotalCPDiscount = $scope.totalCPDiscount;
                    var tempTotalCPAfterDiscount = parseFloat(0.00);
                    var tempTotalCPAmountTendered = parseFloat(0.00);
                    var tempTotalCPAmountRefunded = parseFloat(0.00);
                    if (!angular.isUndefined($scope.totalCPAmountTendered) || $scope.totalCPAmountTendered !== "" ||
                        (angular.isNumber(parseFloat($scope.totalCPAmountTendered)))) {
                        tempTotalCPAmountTendered = parseFloat($scope.totalCPAmountTendered);
                    }
                    if (angular.isUndefined(currentValue) || currentValue === "" || !(angular.isNumber(parseFloat(currentValue)))) {
                        tempTotalCPDiscount = parseFloat(0.00);
                    } else if (parseFloat(tempTotalCPDiscount) > parseFloat(tempTotalCPBeforDiscount)) {
                        Notification.info({
                            message: 'Discount can not be greater than to be paid amount',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.totalCPDiscount = parseFloat(oldValue)
                        tempTotalCPDiscount = oldValue;
                    }
                    if (parseFloat(tempTotalCPDiscount) > 0) {
                        tempTotalCPAfterDiscount = parseFloat(tempTotalCPBeforDiscount) - parseFloat(tempTotalCPDiscount);
                    } else {
                        tempTotalCPAfterDiscount = parseFloat(tempTotalCPBeforDiscount);
                    }
                    if (parseFloat(tempTotalCPAmountTendered) > 0) {
                        tempTotalCPAmountRefunded = parseFloat(tempTotalCPAmountTendered) - parseFloat(tempTotalCPAfterDiscount);
                    }
                    $scope.totalCPAfterDiscount = parseFloat(tempTotalCPAfterDiscount.toFixed(2));
                    $scope.totalCPAmountRefunded = parseFloat(tempTotalCPAmountRefunded.toFixed(2));
                    break;
                case "totalCPAmountTendered":
                    var tempTotalCPAfterDiscount = $scope.totalCPAfterDiscount;
                    var tempTotalCPAmountTendered = $scope.totalCPAmountTendered;
                    var tempTotalCPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(tempTotalCPAmountTendered) || tempTotalCPAmountTendered === ""
                        || !(angular.isNumber(parseFloat(tempTotalCPAmountTendered)))) {
                        tempTotalCPAmountTendered = parseFloat(0.00);
                    }
                    if (parseFloat(tempTotalCPAmountTendered) > 0) {
                        tempTotalCPAmountRefunded = parseFloat(tempTotalCPAmountTendered) - parseFloat(tempTotalCPAfterDiscount);
                    }
                    $scope.totalCPAmountRefunded = parseFloat(tempTotalCPAmountRefunded).toFixed(2);
                    break;
            }
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

        $scope.OnChangeMultiDiscount = function (oldValue) {
            if (parseFloat($scope.totalVPDiscount) > parseFloat($scope.totalVPAfterDiscount)) {
                Notification.info({
                    message: 'Discount cannot be greater than to be paid amount',
                    positionX: 'center',
                    delay: 2000
                });
                $scope.totalVPDiscount = oldValue;
                return;
            }
            var totalCCPAfterDiscount = parseFloat($scope.amountWithoutDiscount - $scope.totalVPDiscount);
            $scope.totalVPAfterDiscount = parseFloat(totalCCPAfterDiscount.toFixed(2));
            $scope.totalVPBeforDiscount = parseFloat(totalCCPAfterDiscount.toFixed(2));

            if (Math.abs($scope.totalVPAmountRefunded) > parseFloat(0) && $scope.totalVPDiscount != "") {
                // var discountplusdiscount=parseFloat($scope.amountWithoutDiscount)+parseFloat($scope.totalVPDiscount);
                var totalrefundamt = parseFloat($scope.totalPaidAmt) - parseFloat($scope.totalVPBeforDiscount);
                $scope.totalVPAmountRefunded = totalrefundamt.toFixed(2);
            }
        };

        $scope.OnChangeVoucherDiscount = function (oldValue, currentValue) {
            var onFocusedElement = $scope.onFocusElement.id;
            switch (onFocusedElement) {
                case "totalVoucherAmt":
                    var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
                    var tempTotalVPAfterDiscount = parseFloat(0.00);
                    var tempTotalVPAmountTendered = parseFloat(0.00);
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(currentValue) || currentValue === "" ||
                        !(angular.isNumber(parseFloat(currentValue)))) {
                        $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                        $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                        return;
                        //$scope.totalVoucherAmt = parseFloat(0.00);
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
                    if (parseFloat(currentValue) > 0) {
                        tempTotalVPAfterDiscount = parseFloat(tempTotalVPBeforDiscount) - parseFloat(currentValue);
                    }
                    if (parseFloat($scope.totalVPAmountTendered) > 0) {
                        tempTotalVPAmountRefunded = parseFloat($scope.totalVPAmountTendered) - parseFloat(tempTotalVPAfterDiscount);
                    }
                    $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
                    $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                    break;
                case "totalVPAmountTendered":
                    var tempTotalVPAfterDiscount = $scope.totalVPAfterDiscount;
                    var tempTotalVPAmountTendered = $scope.totalVPAmountTendered;
                    var tempTotalVPAmountRefunded = parseFloat(0.00);
                    if (angular.isUndefined(tempTotalVPAmountTendered) || tempTotalVPAmountTendered === ""
                        || !(angular.isNumber(parseFloat(tempTotalVPAmountTendered)))) {
                        tempTotalVPAmountTendered = parseFloat(0.00);
                    }
                    if (parseFloat(tempTotalVPAmountTendered) > tempTotalVPAfterDiscount) {
                        tempTotalVPAmountRefunded = parseFloat(tempTotalVPAmountTendered) - parseFloat(tempTotalVPAfterDiscount);
                    }
                    else {
                        tempTotalVPAmountRefunded = parseFloat(0.00);
                    }
                    $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
                    break;
            }
        }
        // $scope.OnChangeMultiPayament = function (oldValue, currentValue) {
        //     var onFocusedElement = $scope.onFocusElement.name;
        //     var voucherTotalAmt = parseFloat(0.00);
        //     var cardTotalAmt = parseFloat(0.00);
        //     var cashTotalAmt = parseFloat(0.00);
        //     var paymentAmount = parseFloat(0.00);
        //     cashTotalAmt = $scope.totalCPAmountTendered;
        //     if (angular.isUndefined(cashTotalAmt) || cashTotalAmt === "" ||
        //         !(angular.isNumber(parseFloat(cashTotalAmt)))) {
        //         cashTotalAmt = parseFloat(0);
        //     }
        //     if ($scope.removePayments == "removeField") {
        //         currentValue = parseFloat(1);
        //         $scope.removePayments = "";
        //     }
        //     angular.forEach($scope.voucherPayementList, function (value, key) {
        //         if (value.amt > 0) {
        //             voucherTotalAmt = parseFloat(voucherTotalAmt) + parseFloat(value.amt);
        //             $scope.checkbox = "";
        //             currentValue = parseFloat(1);
        //         }
        //     });
        //     angular.forEach($scope.cardPayementList, function (value, key) {
        //         if (value.cardAmount > 0 || $scope.checkbox == "unchecked") {
        //             cardTotalAmt = parseFloat(cardTotalAmt) + parseFloat(value.cardAmount);
        //             $scope.checkbox = "";
        //             currentValue = parseFloat(1);
        //         }
        //     });
        //     if (cardTotalAmt.toString() == "NaN") {
        //         cardTotalAmt = parseFloat(0);
        //     }
        //     paymentAmount = parseFloat(voucherTotalAmt) + parseFloat(cardTotalAmt) + parseFloat(cashTotalAmt);
        //
        //     switch (onFocusedElement) {
        //         case "totalVoucherAmt":
        //             var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
        //             var tempTotalVPAfterDiscount = parseFloat(0.00);
        //             var tempTotalVPAmountRefunded = parseFloat(0.00);
        //             if (angular.isUndefined(currentValue) || currentValue === "" ||
        //                 !(angular.isNumber(parseFloat(currentValue)))) {
        //                 $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
        //                 $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
        //                 return;
        //             } else if (parseFloat($scope.totalVoucherAmt) > parseFloat($scope.totalVPBeforDiscount)) {
        //                 Notification.info({
        //                     message: 'Voucher Amount can not be greater than to be paid amount',
        //                     positionX: 'center',
        //                     delay: 2000
        //                 });
        //                 $scope.totalVoucherAmt = parseFloat(oldValue);
        //                 $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
        //                 $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
        //                 return;
        //             }
        //             if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
        //                 tempTotalVPAfterDiscount = parseFloat(0);
        //                 var reFoundAmt = parseFloat(0);
        //                 reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
        //                 $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
        //                 $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
        //             }
        //             else {
        //                 $scope.totalVPAmountRefunded = (parseFloat(paymentAmount.toFixed(2)) - parseFloat(tempTotalVPBeforDiscount.toFixed(2)) - parseFloat($scope.cardAmount.toFixed(2))).toFixed(2);
        //                 $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
        //                 // $scope.totalVPAmountRefunded=parseFloat(0);
        //             }
        //             // if (parseFloat(currentValue) > 0) {
        //             //     tempTotalVPAfterDiscount = parseFloat(paymentAmount)-parseFloat(tempTotalVPBeforDiscount) - parseFloat($scope.cardAmount);
        //             // }
        //             // if (parseFloat($scope.totalVPAmountTendered) > 0) {
        //             //     tempTotalVPAmountRefunded = parseFloat($scope.totalVPAmountTendered) - parseFloat(tempTotalVPAfterDiscount);
        //             // }
        //             // $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
        //             // $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
        //             break;
        //         case "cardAmt":
        //             var tempTotalVPBeforDiscount = $scope.totalVPBeforDiscount;
        //             var tempTotalVPAfterDiscount = parseFloat(0.00);
        //             var tempTotalVPAmountRefunded = parseFloat(0.00);
        //             if (angular.isUndefined(currentValue) || currentValue === "" ||
        //                 !(angular.isNumber(parseFloat(currentValue)))) {
        //                 $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
        //                 $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
        //                 return;
        //             } else if (parseFloat($scope.cardAmount) > parseFloat($scope.totalVPBeforDiscount)) {
        //                 Notification.info({
        //                     message: 'Card  Amount can not be greater than to be paid amount',
        //                     positionX: 'center',
        //                     delay: 2000
        //                 });
        //                 $scope.totalVoucherAmt = parseFloat(oldValue);
        //                 $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
        //                 $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
        //                 return;
        //             }
        //             if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
        //                 tempTotalVPAfterDiscount = parseFloat(0);
        //                 var reFoundAmt = parseFloat(0);
        //                 reFoundAmt = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
        //                 $scope.totalVPAmountRefunded = reFoundAmt.toFixed(2);
        //                 $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
        //             }
        //             else {
        //                 $scope.totalVPAmountRefunded = (parseFloat(paymentAmount.toFixed(2)) - parseFloat(tempTotalVPBeforDiscount.toFixed(2)) - parseFloat($scope.totalVoucherAmt.toFixed(2))).toFixed(2);
        //                 $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
        //                 // $scope.totalVPAmountRefunded=parseFloat(0);
        //             }
        //             // if (parseFloat(currentValue) > 0) {
        //             //     tempTotalVPAfterDiscount = parseFloat(paymentAmount)-parseFloat(tempTotalVPBeforDiscount) -parseFloat($scope.totalVoucherAmt);
        //             // }
        //             // if (parseFloat($scope.totalVPAmountTendered) > 0) {
        //             //     tempTotalVPAmountRefunded = parseFloat($scope.totalVPAmountTendered) - parseFloat(tempTotalVPAfterDiscount);
        //             // }
        //             // $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
        //             // $scope.totalVPAmountRefunded = parseFloat(tempTotalVPAmountRefunded.toFixed(2));
        //             break;
        //         case "totalVPAmountTendered":
        //             var tempTotalVPAfterDiscount = $scope.totalVPAfterDiscount;
        //             var tempTotalVPAmountTendered = $scope.totalVPAmountTendered;
        //             var tempTotalVPAmountRefunded = parseFloat(0.00);
        //             if (angular.isUndefined($scope.totalCPAmountTendered) || $scope.totalCPAmountTendered === ""
        //                 || !(angular.isNumber(parseFloat($scope.totalCPAmountTendered)))) {
        //                 tempTotalVPAmountTendered = parseFloat(0.00);
        //             }
        //             if (parseFloat(paymentAmount) > $scope.totalVPBeforDiscount) {
        //                 tempTotalVPAfterDiscount = parseFloat(0);
        //                 $scope.totalVPAmountRefunded = parseFloat(paymentAmount.toFixed(2)) - parseFloat($scope.totalVPBeforDiscount.toFixed(2));
        //                 $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
        //             }
        //             else {
        //                 $scope.totalVPAmountRefunded = parseFloat(parseFloat(paymentAmount.toFixed(2)) - $scope.totalVPBeforDiscount.toFixed(2)).toFixed(2);
        //                 $scope.totalPaidAmt = parseFloat(paymentAmount.toFixed(2));
        //             }
        //             $scope.totalVPAfterDiscount = parseFloat(tempTotalVPAfterDiscount.toFixed(2));
        //             break;
        //     }
        // };
        $scope.onFocusElement;
        $scope.getSelectedTexElement = function ($event) {
            $scope.onFocusElement = $event.target;
            $scope.doGetCaretPosition($scope.onFocusElement);
        };
        $scope.makeSerialItemRead = function (id, val) {
            if (val == 'Serialize') {
                $('#qty_' + id).prop('readonly', true);
                $('#qty_' + id).attr('title', 'Serialized Item Quantity Can Not Be Edited');
            }
        };
        $scope.doGetCaretPosition = function (oField) {
            var iCaretPos = 0;
            var iCaretEndPos = 0;
            if (document.selection) {
                oField.focus();
                var oSel = document.selection.createRange();
                oSel.moveStart('character', -oField.value.length);
                iCaretPos = oSel.text.length;
            } else if (oField.selectionStart || oField.selectionStart === 0) {
                iCaretPos = oField.selectionStart;
                iCaretEndPos = oField.selectionEnd;
            }
            $scope.cursorCurPosVal = iCaretPos;
            $scope.cursorEndPosVal = iCaretEndPos;
        };
        $scope.getCalculatorValue = function (value) {
            if (value === 'BackSpace') {
                var val = ($scope.onFocusElement.value).substring(0, $scope.onFocusElement.value.length - 1);
                if (val === "" || val === undefined) {
                    $scope.onFocusElement.value = 0.00;
                } else {
                    $scope.onFocusElement.value = val;
                }
                angular.element($scope.onFocusElement).trigger('input');
            } else if (value === 'CE') {
                $scope.onFocusElement.value = 0;
                angular.element($scope.onFocusElement).trigger('input');
            } else {
                if ($scope.cursorEndPosVal > $scope.cursorCurPosVal) {
                    var front = ($scope.onFocusElement.value).substring(0, $scope.cursorCurPosVal);
                    var back = ($scope.onFocusElement.value).substring($scope.cursorEndPosVal, $scope.onFocusElement.value.length);
                    if (front == parseFloat(0)) {
                        $scope.onFocusElement.value = value + back;
                    } else {
                        $scope.onFocusElement.value = front + value + back;
                    }
                    angular.element($scope.onFocusElement).trigger('input');
                    $scope.cursorCurPosVal = $scope.cursorCurPosVal + 1;
                    $scope.cursorEndPosVal = 0;
                } else {
                    var front = ($scope.onFocusElement.value).substring(0, $scope.cursorCurPosVal);
                    var back = ($scope.onFocusElement.value).substring($scope.cursorCurPosVal, $scope.onFocusElement.value.length);
                    if (front == parseFloat(0)) {
                        $scope.onFocusElement.value = value + back;
                    } else {
                        $scope.onFocusElement.value = front + value + back;
                    }
                    angular.element($scope.onFocusElement).trigger('input');
                    $scope.cursorCurPosVal = $scope.cursorCurPosVal + 1;
                    $scope.cursorEndPosVal = 0;
                }
            }
        };
        $scope.selectedItemListRemoval = {};
        $scope.removeSelectedItems = function () {
            if (angular.isUndefined($scope.selectedItemsList) || $scope.selectedItemsList.length <= 0) {
                Notification.error({message: 'At lest One item has to be selected', positionX: 'center', delay: 2000});
            } else {
                $scope.selectedItemsList = $scope.selectedItemsList.filter(function (data, index) {
                    return !($scope.selectedItemListRemoval[index] !== undefined && $scope.selectedItemListRemoval[index]);
                });
                $scope.selectedItemListRemoval = {};
                $scope.getTotalAmtForSelectedItems();
            }
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
            // $scope.dt=new Date();
            $scope.selectedAccountList = [];
            $scope.searchContactText = "";
            $scope.accountSearchText = "";
            $scope.operation = 'Create';
            $scope.supplierEmail = "";
            $scope.supplierInvNo="";
            $scope.supplierSearchText = "Cash Supplier|01";
            $scope.selectedSerialNumberFilter = "";
            $scope.agentText = "";
            $scope.currencyText = "";
            $scope.exchangeRateText = "";
            $scope.termsAndConditionText = "";
            $scope.projectText = "";
            $scope.shipingmethod = "";
            $scope.dt1 = "";
            $scope.shippingmethodreferenceno = "";
            $scope.referenceNo = "";
        };
        $scope.removeAllItemsWithoutSupplier = function () {
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
            $scope.supplierEmail = "";
            $scope.supplierInvNo="";
            $scope.serialItems = "";

        };
        $scope.removeSupplierDetails = function () {

            $scope.supplierNameText = "";
            $scope.supplierEmailText = "";
            $scope.supplierContactText = "";
            $scope.supplierContactText = "";
            $scope.supplierAddressText = "";
            $scope.companyRegNo="";
            $scope.fromRegNo="";

        };
        $scope.populateSaveGTData = function (paymentType, operation) {

            $scope.BPDetails=[];
            $scope.CCPDetails=[];
            $scope.CPDetails=[];
            $scope.VPDetails=[];
            var CASHPAYMENT = $scope.CASHPAYMENT;
            var CARDPAYMENT = $scope.CARDPAYMENT;
            var OTHERSPAYMENT = $scope.OTHERSPAYMENT;
            var VOUCHERPAYMENT = $scope.VOUCHERPAYMENT;

            $scope.BANK_PAYMENT_DETAILS = [];
            $scope.CARD_PAYMENT_DETAILS = [];
            $scope.CASH_PAYMENT_DETAILS = [];
            $scope.VOUCHER_PAYMENT_DETAILS=[];
            var fromReg="";
            var toReg="";
            var userId=$("#userObj").val();
            var typeDoc="PO";
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
                                'transactionNo': $scope.paymentDropdown[key].cardBankAccount,
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

            var data = {
                operation: operation,
                selectedAccountList: $scope.selectedAccountList,
                cashPayment: $scope.CPDetails,
                creditPayment: $scope.CCPDetails,
                voucherPayment: $scope.VPDetails,
                creditPayment: $scope.CCPDetails,
                bankPayment: $scope.BPDetails,
                totalCheckOutamt: $scope.totalCheckOutamt,
                totalTenderedAmount: $scope.totalPaidAmt,
                paymentType: paymentType,
                totalTaxAmt: $scope.totalTaxAmt,
                taxType: $scope.fullSimplTax,
                supplierId: $scope.supplierId,
                gstApprovalNumber: $scope.searchISDNText,
                supplierEmail: $scope.supplierEmail,
                supplierInvNo: $scope.supplierInvNo,
                supplierName: $scope.searchContactText,
                userName: $('#userName').val(),
                from_reg: fromReg,
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
                opNo: $scope.opNo,
                to_reg: toReg,
                user_id: userId,
                type_doc: typeDoc,
                dateOfInvoice: $scope.dt
            };
            console.log(data);
            return data;
        };

        $scope.getDuplicatePaymentVoucher = function (val) {
            $(".loader").css("display", "block");
            if (angular.isUndefined(val)) {
                val = "";
            }
            if(angular.isUndefined($scope.fromLocation)){
                $scope.fromLocation =null;
            }
            $http.get($scope.generalTransactionServerURL   + '/getDuplicatePaymentVoucher/?searchText=' + val).then(function (response) {
                var data = response.data;
                $scope.paymentVoucherList = angular.copy(data);
                $("#print_list_modal").modal('hide');

                $("#paymentVoucherDuplicatePrint").modal('show');
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
        $scope.duplicateVoucherPrint = function (paymentID) {
            $http.post($scope.generalTransactionServerURL  + '/printDuplicateVoucher/' + paymentID).then(function (response) {
                var data = response.data;
                $scope.gtPurchase = data;
                $scope.numberToWord = toWords(data.amountPaid);
                $scope.numberToWordGstTax=toWords(data.totalTaxAmt);
                // $scope.currencyCode = data.companyData. currencyId.currencyCode;
                // $scope.currencyName = data.companyData. currencyId.currencyName;
                // if($scope.gtPurchase.taxType == 'CGST:SGST/UGST'){
                //     $scope.gtPurchase.splitTaxAmt = ($scope.gtPurchase.totalTaxAmt)  / 2;
                // }else {
                //     $scope.gtPurchase.splitTaxAmt = ($scope.gtPurchase.totalTaxAmt)
                // }
                $("#paymentVoucherDuplicatePrint").modal('hide');
                $("#gtOtherPurchasePrint").modal('show');
            });
        };

        $scope.remove = function () {
            $scope.text = "";
        }
        $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];
        $scope.clearPaymentValues =function(){
            $scope.paymentDropdown = [{'id': 'paymentDropdown1'}];
            $scope.paymentList = [];
            $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
        }
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

                    //     {
                    //     $('.bankInfoHide').hide();
                    // }
                }
            });
            //console.log($scope.paymentDropdown)
        };
        $scope.removeOtherContactsData = function () {
            $scope.customerId="";
            $scope.contactNameText="";
            $scope.GSTINText="";
            $scope.selectedState="";
            $scope.contactContactText="";
            $scope.contactEmailText="";
            $scope.contactAddressText="";
            $scope.personInchargeText="";
            $scope.selectedCountry="";
            $scope.selectedCurrency="";
            $scope.bankNameText="";
            $scope.accNoText="";
            $scope.panNumberText="";
            $scope.bankBranchText="";
            $scope.IFSCText="";
            $scope.websiteText="";
        }
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
                selectedItemsList: $scope.selectedItemsList,
                cashPayment: CPDetails,
                creditPayment: CCPDetails,
                voucherPayment: VPDetails,
                totalCheckOutamt: $scope.totalVPBeforDiscount,
                paymentType: paymentType,
                totalTaxAmt: $scope.totalTaxAmt,
                taxType: $scope.fullSimplTax,
                supplierId: $scope.supplierId,
                supplierEmail: $scope.supplierEmail,
                cutomerName: $scope.supplierSearchText,
                amountReturned: $scope.totalVPAmountRefunded,
                discountAmount: $scope.totalVPDiscount,
                totalTenderedAmount: $scope.totalPaidAmt,
                userName: $('#userName').val(),
                dateOfInvoice :$scope.dt,
                advancepayment:$scope.advancepayment
            };
            return data;
        }

        $scope.populateReturnPIData = function (piId, operation, paymentType) {
            angular.forEach($scope.selectedItemsList, function (item, index) {
                if (parseFloat(item.returnQty) === parseFloat(item.qty)) {
                    $scope.selectedItemsList.splice(index, 1);
                }
            });
            var CPDetails;
            var data = {
                piid: piId,
                operation: operation,
                selectedItemsList: $scope.selectedItemsList,
                cashPayment: CPDetails,
                totalCheckOutamt: $scope.totalCheckOutamt,
                paymentType: paymentType,
                totalTaxAmt: $scope.totalTaxAmt,
                taxType: $scope.tax,
                supplierId: $scope.supplierId,
                dateOfInvoice :$scope.dt
            };
            return data;
        };
        $scope.itemsNoStockList = [];
        $scope.openSelectpayment = function () {
            if (angular.isUndefined($scope.totalCheckOutamt) || $scope.totalCheckOutamt <= 0) {
                Notification.warning({message: 'Check out amount can not be zero', positionX: 'center', delay: 2000});
            } else if (!$scope.isValidatedData()) {
                Notification.warning({
                    message: 'Unit price and Qty should not be blank or zero',
                    positionX: 'center',
                    delay: 2000
                });
            } else {
                $http.post($scope.posPurchaseServerURL + '/validateCheckout?invokeOrderId=' + $scope.invokeOrderId,
                    angular.toJson($scope.populateSavePIData("", ""))).then(function (response, status, headers, config) {
                    var data = response.data;
                    $scope.itemsNoStockList = data;
                    if ($scope.itemsNoStockList !== null && $scope.itemsNoStockList.length > 0) {
                        $("#ItemsNoStockListModel").modal('show');
                    }
                    else {
                        $("#selectpayment").modal('show');
                    }
                },function (error) {
                    Notification.error({message: data.message, positionX: 'center', delay: 2000});
                })
                //     .error(function (data, status, header, config) {
                //     Notification.error({message: data.message, positionX: 'center', delay: 2000});
                // });
            }
        };
        $scope.itemsNoStockList = [];
        $scope.openStockValidateMultipayment = function () {
            if (!$scope.isValidatedData()) {
                Notification.warning({
                    message: 'Amount EX  should not be blank or zero',
                    positionX: 'center',
                    delay: 2000
                });
            } else {
                $scope.getTotalAmtForSelectedItems();
                $scope.totalVPBeforDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
                if (angular.isUndefined($scope.totalVPBeforDiscount)) {
                    Notification.error({
                        message: 'At lest One Account has to be selected',
                        positionX: 'center',
                        delay: 2000
                    });
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
                $scope.operation = "gtPurchase";
                $("#paymentNew1").modal('show');
            }
        };
        $scope.openSelectpaymentForReturn = function () {
            $("#selectPaymentForReturn").modal('show');

        };
        $scope.printData;

        $scope.populateSIResponceData = function (data, paymentType) {
            if (data.result === "SUCCESS") {
                $scope.printData = data;
                if (paymentType === 'multiPayment' && data.siData.printType === 'normal') {
                    $("#multiprintreceiptA4").modal('show');
                }
                else if (paymentType === 'multiPayment' && data.siData.printType === 'pos') {
                    $("#multiprintreceipt").modal('show');
                }

            } else {
                $scope.itemsNoStockList = data.itemDetils;
                $("#ItemsNoStockListModel").modal('show');
            }
            $scope.clearMultiPayment();
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
            $scope.supplierEmail = "";
            $scope.supplierSearchText = "Cash Supplier|01";
            $scope.selectedSerialNumberFilter = "";
        }


        $scope.populateSRResponseData = function (data) {
            $scope.printReturnData = data;
            $("#printReturnReceipt").modal('show');

        };

        $scope.openCashpaymentForReturn = function () {
            $scope.totalCashPymtAmtReturned = parseFloat($scope.totalBeforDiscount.toFixed(2));
            ;
            $("#returnCashpayment").modal('show');
        };
        $scope.openCashpayment = function () {
            $scope.totalCPBeforDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
            $scope.totalCPDiscount = parseFloat($scope.totalDiscount.toFixed(2));
            $scope.totalTaxAmt = parseFloat($scope.totalTaxAmt.toFixed(2));
            $scope.totalCPAfterDiscount = parseFloat($scope.totalAfterDiscount.toFixed(2));
            $scope.totalCPAmountTendered = 0.00;
            $scope.totalCPAmountRefunded = 0.00;
            $("#cashpayment").modal('show');
        };
        $scope.openCreditCardPayment = function () {
            $scope.totalCCPBeforDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
            $scope.totalCCPDiscount = parseFloat($scope.totalDiscount.toFixed(2));
            //$scope.totalCCPAfterDiscount = $scope.negara($scope.totalAfterDiscount);
            $scope.totalTaxAmt = parseFloat($scope.totalTaxAmt.toFixed(2));
            $scope.totalCCPTransactionNo = ""
            $scope.totalCCPAfterDiscount = parseFloat($scope.totalAfterDiscount.toFixed(2));
            $("#creditcardpayment").modal('show');
        };
        $scope.openVoucherpayment = function () {
            $scope.totalVPBeforDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
            //$scope.totalVPAfterAllDeductions = $scope.negara($scope.totalBeforDiscount);
            $scope.totalVPDiscount = parseFloat($scope.totalDiscount.toFixed(2));
            $scope.voucherNo = 0.00;
            $scope.totalVoucherAmt = 0.00;
            $scope.totalVPAfterDiscount = parseFloat($scope.totalAfterDiscount.toFixed(2));
            $scope.totalVPAmountTendered = 0.00;
            $scope.totalVPAmountRefunded = 0.00;
            $("#voucherPayment").modal('show');
        };
        $scope.openMultipayment = function () {
            $scope.totalVPBeforDiscount = parseFloat($scope.totalBeforDiscount.toFixed(2));
            $scope.totalVPDiscount = parseFloat($scope.totalDiscount.toFixed(2));
            $scope.voucherNo = 0.00;
            $scope.cardAmount = 0.00;
            $scope.transactionNo = 0.00;
            $scope.totalVPAfterDiscount = parseFloat($scope.totalAfterDiscount.toFixed(2));
            $scope.totalVPAmountTendered = 0.00;
            $scope.totalVPAmountRefunded = 0.00;
            $scope.voucherAmt = 0.00;
            $scope.totalVoucherAmt = 0.00;
            $("#multiPayment").modal('show');
        };
        $scope.openpayment = function () {
            if (angular.isUndefined($scope.supplierId) || $scope.supplierId=="") {
                Notification.error({
                    message: 'Please Select Pay To',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            else if(angular.isUndefined($scope.amtexclusivetax) || $scope.amtexclusivetax == '' ) {
                Notification.warning({message: 'Please enter proper data', positionX: 'center', delay: 2000});

            }
            $scope.openStockValidateMultipayment();
        };

        $scope.itemIndexOfItemCode = function (array, searchVal) {
            var itemIndex = -1;
            if ($scope.isUndefinedOrNull(searchVal)) {
                itemIndex = -1;
            } else {
                var foundIndex = $filter('filter')(array, {
                    itemCode: searchVal
                }, true)[0];
                var foundIndex1 = $filter('filter')(array, {
                    itemCode: searchVal
                }, true)[0];
                itemIndex = array.indexOf(foundIndex);
                if (!$scope.isUndefinedOrNull(foundIndex1)) {
                    if (foundIndex1.serializableStatus == 'Serialize')
                        itemIndex = -1
                }
            }
            return itemIndex;
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
        $scope.serializableItemsIndexOf = function (array, searchVal) {
            var serializableItemsIndex = -1;
            if ($scope.isUndefinedOrNull(searchVal)) {
                serializableItemsIndex = -1;
            } else {
                var foundIndex = $filter('filter')(array, {
                    serializableItemCode: searchVal
                }, true)[0];
                serializableItemsIndex = array.indexOf(foundIndex);
            }
            return serializableItemsIndex;
        };
        $scope.InvokeOrderIndexOf = function (array, searchVal) {
            var invokeOrderIndex = -1;
            if ($scope.isUndefinedOrNull(searchVal)) {
                invokeOrderIndex = -1;
            } else {
                var foundIndex = $filter('filter')(array, {
                    id: searchVal
                }, true)[0];
                invokeOrderIndex = array.indexOf(foundIndex);
            }
            return invokeOrderIndex;
        };
        $scope.negara = function (number) {
            var k = number;
            if (!angular.isUndefined(number) && number !== null && number !== '') {
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

            } else {
                return k;
            }
        };
        $scope.toFixedTwo = function (number) {
            var k = number;
            if (angular.isUndefined(number) || number === "" || !angular.isNumber(parseFloat(number))) {
                k = parseFloat(0.00).toFixed(2);
            } else {
                k = parseFloat(number).toFixed(2);
            }
            $scope.onFocusElement.value = k;
            angular.element($scope.onFocusElement).trigger('input');
        };
        $scope.isUndefinedOrNull = function (data) {
            return (angular.isUndefined(data) || data === null || data === '' || data === 'null');
        };
        $scope.isValidatedData = function () {
            $scope.isValide = true;
            angular.forEach($scope.selectedAccountList, function (account, index) {
                if (angular.isUndefined(account.amtexclusivetax) || account.amtexclusivetax === '') {
                    $scope.isValide = false;
                }
            });
            return $scope.isValide;
        };
        $scope.hideButton = function () {
            $scope.showrtntype = false;
        };

        $('#PosInvoiceList').on('hidden.bs.modal', function () {
            if ($scope.notHide != 'posListClick') {
                $timeout(function () {
                    $scope.hideButton();
                }, 0);
            }
        });
        check = 0;
        $('#camera_icon').click(function () {
            $scope.barcodeInput = "";
            check = 0;
            $('#barcodeModal').modal('show');
            $('.custom_input label').click();
        });

        check = 0;
        $('#serial_icon').click(function () {
            $scope.serialBarcodeInput = "";
            check = 0;
            $('#barcodeModal').modal('show');
            $('.custom_input label').click();
        });

        $scope.populateSaveSIAdvanceMultiPayData = function () {
            var CPDetails;
            var CCPDetails;
            var VPDetails;
            var cashPayment = $scope.cashcheck;
            var cardPayment = $scope.card_checked;
            var voucherPayment = $scope.voucher_checked;

            if (cashPayment == "cashPayment") {
                CPDetails = {
                    totalCPAmountRefunded: $scope.totalCPAmountRefunded,
                    totalCPDiscount: $scope.totalCPDiscount,
                    totalCPAmountTendered: $scope.totalCPAmountTendered
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
                cashPayment: CPDetails,
                creditPayment: CCPDetails,
                voucherPayment: VPDetails,
                totalCheckOutamt: $scope.totalVPBeforDiscount,
                totalTaxAmt: $scope.totalTaxAmt,
                taxType: $scope.fullSimplTax,
                supplierId: $scope.supplierId,
                supplierName: $scope.supplierSearchText,
                amountReturned: $scope.totalVPAmountRefunded,
                discountAmount: $scope.totalVPDiscount,
                totalTenderedAmount: $scope.totalPaidAmt,
                dateOfInvoice :$scope.dt,
                userName: $('#userName').val()
            };
            return data;
        };

        $scope.vouchersEntity = function ($event) {
            if (!$event) {
                $scope.vouchers.forEach(function (voucher, index) {
                    if (index != 0) {
                        $scope.vouchers.splice(index, 1);
                        $scope.vouchersEntity($event);
                    }
                });
            }
        }

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

        $scope.reloadPage = function () {
            $window.location.reload();
        };

        $scope.searchSupplierName = function (companyName, checkBoxVal) {
            var parameter = JSON.stringify({company_name: companyName});
            if (checkBoxVal == true) {
                $scope.showInputLoader = true;
                $http.post($scope.posPurchaseServerURL + "/" + $scope.supplier + '/searchhiconnectcompanyName/' + parameter).then(function (response) {
                    var data = response.data;
                    $scope.supplierDetails = data.list;
                    $scope.showInputLoader = false;
                    $scope.showAutoComplete = true;
                });
            }
        };

        $scope.searchCompanyFromHiConnect = function (companyName, checkBoxVal) {
            var parameter = JSON.stringify({company_name: companyName});
            if (checkBoxVal == true) {
                $scope.showInputLoader = true;
                //$timeout(function () {
                $http.post($scope.posPurchaseServerURL + "/" + $scope.supplier + '/searchhiconnectcompanyName/'+parameter).then(function (response) {
                    var data = response.data;
                    $scope.customerDetails = data.list;
                    $scope.showInputLoader = false;
                    $scope.showAutoComplete = true;
                    //$("#companySearch").modal('show');
                });
                //}, 2000);
            }
        };

        $scope.typeDocReference = {"PO": "Purchase Order Request", "SO": "Sales Order", "CR": "Customer Request", "PI": "Purchase Invoice", "SI": "Sales Invoice", "SP": "Supplier Payment", "RP": "Receiver Payment", "SR": "Supplier Request"};

        $scope.hideAutoComplete = function(){
            $timeout(function () {
                $scope.showAutoComplete = false;
            }, 250)
        }

        $scope.notificationFlag = "";
        $scope.fromRegNo = "";
        $scope.toRegNo = "";
        $scope.showAcceptedNotificationList = false;
        $scope.openSupplierPopUp=function (notification, flag) {
            if (notification.type_doc == "CR") {
                $("#addSupplier").modal('show');
                $scope.supplierNameText = notification.from_compname;
                $scope.supplierEmailText=notification.from_email;
                $scope.supplierContactText=notification.from_contact;
                $scope.supplierAddressText=notification.from_address;
                $scope.Hi_Conn_company_Name = true;
                $scope.notificationFlag = flag;
                $scope.fromRegNo = notification.from_regno;
                $scope.toRegNo = notification.to_regno;
                $scope.notificationId = notification.notification_id;
            }
            else {
                var acceptNotification;
                acceptNotification = {
                    fromRegNo: notification.from_regno,
                    toRegNo: notification.to_regno,
                    typeDoc: notification.type_doc,
                    transactionId:notification.transaction_id
                }
                $http.post($scope.posPurchaseServerURL + "/" + $scope.supplier + '/getNotificationTransactionData', angular.toJson(acceptNotification)).then(function (response) {
                    var data = response.data;
                    if(data && data.selectedItemsList){
                        $scope.selectedItemsList = data.selectedItemsList;
                        angular.forEach($scope.selectedItemsList, function(value){
                            value["ItemDesc"] = value["itemName"];
                            value["itemDescription"] = value["itemName"];
                            value["itemCode"] = "";
                            value["itemName"] = "";
                            if(value["taxName"] && value["taxName"].trim() == "SR"){
                                value["taxid"] = $scope.taxListInDict["TX"];
                            }else{
                                value["taxid"] = $scope.taxListInDict[value["taxName"].trim()] || "";
                            }
                        });
                        $scope.supplierSearchText = data.supplierName || "";
                        $scope.supplierId = data.supplierId || "";
                        $scope.showAcceptedNotificationList = true;
                    }
                });
            }
            $("#notificationList").modal('hide');
        }

        $scope.hideNotificationUI = function(){
            $timeout(function () {
                $scope.showNotificationList=false;
            }, 250)
        }

        $scope.rejectNotification = function(notification){
            $http.post($scope.posPurchaseServerURL + "/" + $scope.supplier + '/rejectSpecificNotification').then(function (response) {
                var data = response.data;
                $scope.hiConnectNotificationList=data.list;
                $scope.showNotificationList=showNoti;
            });
        };

        $scope.savePI = function (paymentType) {
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
                $scope.isDisabled= true;
                $http.post($scope.generalTransactionServerURL  + '/saveOtherPurchaseGt',
                    angular.toJson($scope.populateSaveGTData(paymentType), "Create")).then(function (response, status, headers, config) {
                    $scope.isDisabled= false;
                    var data = response.data;
                    $scope.numberToWord = toWords(data.amountPaid);
                    $scope.removeAllItems();
                    $scope.gtPurchase = data;
                    var $log = $("#log");
                    str = data.footer;
                    html = $.parseHTML(str);
                    // Append the parsed HTML
                    $log.append(html);
                    if ($scope.gtPurchase.taxType == 'CGST:SGST/UGST') {
                        $scope.gtPurchase.splitTaxAmt = ($scope.gtPurchase.totalTaxAmt) / 2;
                    } else {
                        $scope.gtPurchase.splitTaxAmt = ($scope.gtPurchase.totalTaxAmt)
                    }
                    $("#paymentNew1").modal('hide');
                    $("#gtOtherPurchasePrint").modal('show');
                    // $scope.populateSIResponceData(data, paymentType);

                    $("#cashpayment").modal('hide');
                    Notification.success({
                        message: 'Order has been saved successfully',
                        positionX: 'center',
                        delay: 2000
                    });

                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })
            }
        };

        $scope.updateNotificationItemInList = function(item){
            var index = $scope.selectedNotificationIndex;

            $scope.selectedItemsList[index]["itemCode"] = item["itemCode"];
            $scope.selectedItemsList[index]["itemName"] = item["itemName"];
            $scope.selectedItemsList[index]["itemId"] = item["itemId"];

            $("#selectItem .modal-footer button").click();
        }

        $scope.openSelectItemModalForNotification = function(index, itemSearchText){
            $scope.selectedNotificationIndex = index;
            $scope.getItemList(itemSearchText);
        }

            var div = $('.textdiv'),
            height = div.height();
        $scope.addAccount = function (accountCode, keyEvent) {
            div.animate({scrollTop: height}, 500);
            height += div.height();
            var localAccountCode;
            localAccountCode = accountCode.accountid;
            $scope.getAccount(localAccountCode);

        };
        $scope.getAccount = function (accountCode) {
            console.log(accountCode),
                $http.get($scope.hiposServerURL + '/getAccount?accountCode=' + accountCode).then(function (response) {
                    var account = response.data[0];
                    angular.forEach($scope.budgetList, function (budget) {
                        if (budget.accountId.accountid === account.accountid) {
                            account.budget = budget['period'+(new Date($scope.dt).getMonth()+1)];
                        }
                    });
                    $scope.selectedAccountList.push(account);
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
        $scope.func = function(value, taxId, index,account){
            $scope.totalAmt=value-0;
            $scope.amtexclusivetax[index] = parseFloat( $scope.totalAmt).toFixed(2);
            $scope.amtinclusivetax = $scope.totalAmt;
            $scope.selectedAccountList[index].amtinclusivetax = parseFloat($scope.amtinclusivetax).toFixed(2);
            //verify budget
            if (!angular.isUndefined(account.budget) && $scope.amtinclusivetax > account.budget) {
                Notification.error({
                    message: 'Amount Allocated Exceeded Budget',
                    positionX: 'center',
                    delay: 2000
                });
            }
        };
        $scope.getCancelGtforExpenseDraft=function (formNo) {
            $http.get($scope.generalTransactionServerURL  +'/cancelGtforExpense/' + formNo).then(function (response, status, headers, config) {
                var data = response.data;
                $("#paymentVoucherDuplicatePrint").modal('hide');
                Notification.info({message: 'Canceled Successfully', positionX: 'center', delay: 2000})
            });
        }
        $scope.changeTaxId = function(value, index){
            value = value-1;
            console.log(value);
            console.log($scope.taxList[value].taxString)

            var taxDropDown = $scope.taxList[value].taxString.split('|');
            console.log(taxDropDown)
            $scope.taxPercent = taxDropDown[0]/100;

            $scope.taxamt = $scope.amtexclusivetax[index] * $scope.taxPercent;
            $scope.amtinclusivetax = $scope.amtexclusivetax[index] + $scope.taxamt;


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
                accountid: data.data[0].accountid,
                account_name: data.data[0].account_name,
                stringAccountCode: data.data[0].stringAccountCode,
                tax: $scope.taxid[$scope.taxIndex],//$scope.taxList,

            };
            $scope.selectedAccountList.push(test1);
            console.log($scope.selectedAccountList);
            $scope.countVal = count + 1;
            $scope.getTotalAmtForSelectedItems();
        };
        $scope.companyRegNo="";
        $scope.appendSupplierDetails=function(supplierDetails, keyEvent){
            $scope.supplierNameText=supplierDetails.companyname;
            $scope.supplierEmailText=supplierDetails.email;
            $scope.supplierContactText=supplierDetails.phoneNo;
            $scope.supplierAddressText=supplierDetails.address;
            $scope.companyRegNo=supplierDetails.cmp_REG_NO;
            $scope.companyRegNo.notification_id=supplierDetails.notification_id;
            $("#companySearch").modal('hide');
        };
        $scope.remove=function () {
            $scope.text = "";
            $scope.accountSearchText="";
        }

        $scope.format = 'dd/MM/yyyy';

        $scope.open1 = function() {
            $scope.popup1.opened = true;
        };
        $scope.open2 = function() {
            $scope.popup2.opened = true;
        };

        $scope.popup2 = {
            opened: false
        };
        $scope.popup1 = {
            opened: false
        };
        $scope.getExchangeRate=function (no,list) {
            var defValue = 1;
            angular.forEach(list,function (value,key) {
                if (value.currencyId === no) {
                    defValue = value.exchangeRateValue;
                    $scope.exchangeRateId=value.exchangeRateId;
                    $scope.truefalse= false;

                }
            });
            $scope.exchangeRateText = defValue;
            if (defValue === 1) {
                $scope.currencyText = $scope.countryId;
                $scope.truefalse= true;
                Notification.error({
                    message: 'Please Add Exchange rate value',
                    positionX: 'center',
                    delay: 2000
                });
            }
        }

        $scope.savePIandDraft = function (paymentType) {
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
                $scope.isDisabled= true;
                $http.post($scope.generalTransactionServerURL + '/saveDraftGtPurchase',
                    angular.toJson($scope.populateSaveGTData(paymentType), "Create")).then(function (response, status, headers, config) {
                    $scope.isDisabled= false;
                    var data = response.data;
                    $scope.numberToWord = toWords(data.amountPaid);
                    $("#paymentNew1").modal('hide');
                    $("#cashpayment").modal('hide');
                    $("#creditcardpayment").modal('hide');
                    $("#voucherPayment").modal('hide')
                    $("#multiPayment").modal('hide');
                    $scope.removeAllItems();
                    $scope.gtPurchase = data;
                    var $log = $("#log");
                    str = data.footer;
                    html = $.parseHTML(str);
                    // Append the parsed HTML
                    $log.append(html);
                    if ($scope.gtPurchase.taxType == 'CGST:SGST/UGST') {
                        $scope.gtPurchase.splitTaxAmt = ($scope.gtPurchase.totalTaxAmt) / 2;
                    } else {
                        $scope.gtPurchase.splitTaxAmt = ($scope.gtPurchase.totalTaxAmt)
                    }
                    $("#gtOtherPurchasePrint").modal('show');
                    Notification.success({
                        message: 'Order has been saved successfully',
                        positionX: 'center',
                        delay: 2000
                    });

                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })
            }
        };
        $scope.getGTPurchaseForEdit= function (formNo) {
            if (formNo === "") {
                Notification.info({message: "Please Enter Invoice No", positionX: 'center', delay: 2000});
                return;
            }
            $http.get($scope.generalTransactionServerURL   +'/getOtherGTPurchaseEdit/' + formNo).then(function (response, status, headers, config) {
                var data = response.data;
                $scope.operation='Edit';
                $scope.op='Edit';
                $scope.opNo=data.opNo;
                $scope.selectedAccountList = data.selectedAccountList;
                $scope.searchContactText=data.supplierName;
                $scope.searchISDNText=data.gstApprovalNumber;
                $scope.supplierId=data.supplierId;
                $scope.dt=new Date(data.date);
                $("#paymentVoucherDuplicatePrint").modal('hide');
                // angular.forEach($scope.selectedAccountList,function(index,value){
                //     $scope.selectedAccountList.taxid =
                // })
                // angular.forEach($scope.selectedAccountList,function (value,key) {
                //     $scope.func(value.gtAmountExcTax,value.taxid,key);
                // });
                $scope.updatesimplifiedTax();
            },function (error) {
                if (error.status == 500) {
                    Notification.error({message: "Something went wrong in server", positionX: 'center', delay: 2000});
                } else {
                    Notification.error({message: data.message, positionX: 'center', delay: 2000});
                }
            })
        };

        $scope.postGtPurchase = function (formNo,index) {
            if (formNo === "") {
                Notification.info({message: "Please Enter Invoice No", positionX: 'center', delay: 2000});
                return;
            }
            $http.get($scope.generalTransactionServerURL  +'/postGtforExpense/' + formNo, "Create").then(function (response, status, headers, config) {
                $scope.paymentVoucherList[index].piStatus="Prepared";
                Notification.info({message: "Posted", positionX: 'center', delay: 2000});
                $scope.removeAllItems();
                $scope.getDuplicatePaymentVoucher();
            },function (error) {
                if (error.status == 500) {
                    Notification.error({message: "Something went wrong in server", positionX: 'center', delay: 2000});
                } else {
                    Notification.error({message: data.message, positionX: 'center', delay: 2000});
                }
            })
        };

        $scope.cancelGtExpense=function (no) {
            $http.post($scope.generalTransactionServerURL  +'/cancelGtExpense?invoiceNo=' + no).then(function (response) {
                var data = response.data;
                Notification.info({message: 'Canceled Successfully', positionX: 'center', delay: 2000})
                $("#paymentVoucherDuplicatePrint").modal('hide');
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
        $scope.getTotalAmtForSelectedAcccountGtPurchase = function () {
            var totalAmt = 0.00;
            var totalTaxAmt = 0.00;
            var totalDiscountAmt = 0.00;
            var cessTotalTaxAmt =0.00;
            var totalQty = 0.00;
            angular.forEach($scope.selectedItemsList, function (item, index) {
                totalAmt += parseFloat(item.amtinclusivetax);
                totalTaxAmt += parseFloat(item.taxamt);
                if(!angular.isUndefined(item.cessTaxAmt))  {
                    cessTotalTaxAmt += parseFloat(item.cessTaxAmt);
                }
                totalDiscountAmt += parseFloat(item.discountAmt);
                //  totalQty += parseFloat(item.returnQty);
            });
            var totalAfterDiscount = parseFloat(totalAmt);
            $scope.totalCheckOutamt = parseFloat(totalAmt.toFixed(2));
            $scope.totalBeforDiscount = parseFloat(totalAmt.toFixed(2));
            $scope.totalDiscount = parseFloat(totalDiscountAmt.toFixed(2));
            $scope.totalAfterDiscount = parseFloat(totalAfterDiscount.toFixed(2));
            $scope.totalTaxAmt = parseFloat(totalTaxAmt.toFixed(2));
            $scope.cessTotalTaxAmt = parseFloat(cessTotalTaxAmt).toFixed(2);
            // $scope.returnQty = parseFloat(totalQty.toFixed(2));
        };
        $scope.gtopenpayment = function () {
            if (angular.isUndefined($scope.supplierId) || $scope.supplierId=="") {
                Notification.error({
                    message: 'Please Select Pay To',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            else if(angular.isUndefined($scope.amtexclusivetax) || $scope.amtexclusivetax == '' ) {
                Notification.warning({message: 'Please enter proper data', positionX: 'center', delay: 2000});

            }
            $scope.openStockValidateMultipayment();
            // $scope.OnLoadMultiPayament();
            $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
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
            $scope.totalCheckOutamt = parseFloat(totalAmt.toFixed(2));
            $scope.totalBeforDiscount = parseFloat(totalAmt.toFixed(2));
            $scope.totalDiscount = parseFloat(totalDiscountAmt.toFixed(2));
            $scope.totalAfterDiscount = parseFloat(totalAfterDiscount.toFixed(2));
            $scope.totalTaxAmt = parseFloat(totalTaxAmt.toFixed(2));
            $scope.returnQty = parseFloat(totalQty.toFixed(2));
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
        $scope.getPaymentTypesList1(1, 0, 'ONLOAD');
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
        $scope.openSelectpaymentForReturn = function () {
            $("#selectPaymentForReturn").modal('show');

        };
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
        $('#hideSection').css("display","none");
    });
