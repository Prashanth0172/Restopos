app.controller('customerCtrl',
    function ($scope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window, $cookies, $httpParamSerializerJQLike, $rootScope) {
        $("#contact").addClass('active');
        $('#contact').siblings().removeClass('active');
        $("#customer").addClass('active');
        $('#customer').siblings().removeClass('active');
        // body...\
        $scope.hiposServerURL = "/hipos";
        $scope.retailServerURL = "/retail/";
        $scope.posPurchaseServerURL = "/purchase/";
        $scope.countVal = 0;
        $scope.customerEmail = "";
        $scope.userRights = [];
        $scope.operation = 'Create';
        $scope.cursorPosVal = 0;
        $scope.customer = 1;
        $scope.customerSearchText = "";
        $scope.today = new Date();
        $scope.companyName = "";
        $scope.companyAddress = "";
        $scope.companyPhoneNo = "";
        $scope.companyFax = "";
        $scope.companyGstNo = "";
        $scope.cutomerName = "";
        $scope.customerDetails = [];
        $scope.customerNameText = "";
        $scope.hiConnectNotificationList = [];
        $scope.fullUserName = "";
        $scope.bankNameText = "";
        $scope.accountNoText = "";
        $scope.word = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
        $scope.regEx = "/^[0-9]{10,10}$/";
        //added for pagination purpose @rahul
        $scope.firstPage = true;
        $scope.lastPage = false;
        $scope.pageNo = 0;
        $scope.prevPage = false;
        $scope.isPrev = false;
        $scope.isNext = false;
        $scope.inactiveStatus = "Active";
        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";
        $scope.notHide = "";
        $scope.notificationFlag = "";
        $scope.fromRegNo = "";
        $scope.toRegNo = "";
        $scope.transaction_id = "";
        $scope.showAcceptedNotificationList = false;
        $scope.inactiveStatus = "Active";
        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";

        $scope.inactiveButton = function () {
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
            $scope.getPaginationCustomerList();

        };
        $scope.rejectNotification = function (notification) {
            // write your code here
        }

        $scope.Button = "SelectAll";
        $scope.selectAll = function () {

            if ($scope.clicked == false) {
                $scope.select = "SelectAll";
                $scope.Button = "UnSelectAll";
                angular.forEach($scope.customerList, function (val, key) {
                    val.checkbox = true;
                })
            }
            else {
                $scope.select = "UnSelectAll";
                $scope.Button = "SelectAll";
                angular.forEach($scope.customerList, function (val, key) {
                    val.checkbox = false;
                })
            }
            $scope.clicked = !$scope.clicked;
        };

        $scope.appendCustomerDetails = function (customerDetails, keyEvent) {

            // console.log(customerDetails);

            $scope.showAutoComplete = false;
            $scope.customerNameText = customerDetails.companyname;
            $scope.GSTINText = customerDetails.gstCode;
            $scope.customerContactText = customerDetails.phoneNo;
            $scope.customerEmailText = customerDetails.email;
            $scope.customerAddressText = customerDetails.address;
            $scope.personInchargeText = customerDetails.personIncharge;
            $scope.companyRegNo = customerDetails.cmp_REG_NO;
            // $scope.companyRegNo = customerDetails.hiConnectCompnyRegNo;
            // $scope.selectedState = parseInt(customerDetails.stateId.id);
            // $scope.selectedCountry = parseInt(customerDetails.countryId.countryId);
            // $scope.selectedCurrency = parseInt(customerDetails.currencyId.currencyId);
            $scope.bankNameText = customerDetails.bankName;
            $scope.accNoText = customerDetails.accountNo;
            $scope.panNumberText = customerDetails.panNo;
            $scope.bankBranchText = customerDetails.branchName;
            $scope.IFSCText = customerDetails.iFSCCode;
            $scope.websiteText = customerDetails.website;
            $scope.creditTermText = customerDetails.creditedLimitAlert;
            $scope.creditLimitText = customerDetails.creditedLimit;
            $scope.custStatusText = customerDetails.status;
            $("#companySearch").modal('hide');
        };

        $scope.getPaginationCustomerList = function (page) {
            switch (page) {
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
            $http.post($scope.hiposServerURL + "/getPaginationCustomerList?&type=" + $scope.inactiveStatus + '&searchText=' + $scope.customerSearchText, angular.toJson(paginationDetails)).then(function (response) {
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
        $scope.getPaginationCustomerList();
        $scope.removeCustomerDetails = function () {
            $scope.customerNameText = "";
            $scope.customerEmailText = "";
            $scope.customerContactText = "";
            $scope.customerContactText = "";
            $scope.customerAddressText = "";
            $scope.creditLimitText = "";
            $scope.creditTermText = "";
            $scope.pincode = "";
            $scope.fromRegNo = "";
            $scope.companyRegNo = "";
            $scope.UINText = "";
            $scope.Hi_Conn_company_Name = false;
        };
        $scope.Button = "SelectAll";
        $scope.selectAll = function () {

            if ($scope.clicked == false) {
                $scope.select = "SelectAll";
                $scope.Button = "UnSelectAll";
                angular.forEach($scope.customerList, function (val, key) {
                    val.checkbox = true;
                })
            }
            else {
                $scope.select = "UnSelectAll";
                $scope.Button = "SelectAll";
                angular.forEach($scope.customerList, function (val, key) {
                    val.checkbox = false;
                })
            }
            $scope.clicked = !$scope.clicked;
        };
        $scope.synchronize = function () {
            $scope.checkboxlist = [];
            angular.forEach($scope.customerList, function (val, key) {
                if (val.checkbox == true) {
                    $scope.checkboxlist.push(val);
                }
            });
            $http.post($scope.hiposServerURL + '/synchronizeCustomer', angular.toJson($scope.checkboxlist)).then(function (response) {
                var data = response.data;
                Notification.success({
                    message: 'Successfully Syncronized',
                    positionX: 'center',
                    delay: 2000
                });
                angular.forEach($scope.customerList, function (val, key) {
                    if (val.checkbox == true) {
                        val.checkbox = false;
                    }
                })
            })
        };
        $scope.updateCustomer = function () {
            var saveCustomerDetails;
            saveCustomerDetails = {
                customerId: $scope.customerId,
                customerName: $scope.customerNameText,
                email: $scope.customerEmailText,
                customerNumber: $scope.customerContactText,
                address: $scope.customerAddressText,
                companyRegNo: $scope.companyRegNo,
                notificationFlag: $scope.notificationFlag,
                from_Reg_Comp: $scope.fromRegNo,
                to_Reg_Comp: $scope.toRegNo,
                notificationId: $scope.notificationId,
                gstCode: $scope.GSTINText,
                stateId: $scope.selectedState,
                personIncharge: $scope.personInchargeText,
                country: $scope.selectedCountry,
                currencyId: $scope.selectedCurrency,
                pincode: $scope.pincode,
                bankName: $scope.bankNameText,
                discountType: $scope.discount,
                accountNo: $scope.accNoText,
                branchName: $scope.bankBranchText,
                iFSCCode: $scope.IFSCText,
                website: $scope.websiteText,
                panNo: $scope.panNumberText,
                uin: $scope.UINText,
                creditedTerm: $scope.creditTermText,
                creditedLimit: $scope.creditLimitText,
                status: $scope.custStatusText
            };
            $http.post($scope.hiposServerURL + '/saveCustomer', angular.toJson(saveCustomerDetails, "Create")).then(function (response, status, headers, config) {
                var data = response.data;
                if (data == "") {
                    Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                }
                else {
                    $scope.getPaginationCustomerList();
                    $("#addCustomer").modal('hide');
                    if (!angular.isUndefined(data) && data !== null) {
                        $scope.searchText = "";
                    }
                    Notification.success({
                        message: 'Customer Created  successfully',
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


            ;
        };


        $scope.importPopup = function () {
            $("#import_Customer").modal('show');
        }

        $scope.downloadSheet = function () {
            var customer = "Customer";
            $http({
                url: $scope.hiposServerURL + "/" + $scope.customer + '/customerSheet' + "/" + customer,
                method: "POST",
                responseType: 'arraybuffer'
            }).success(function (data) {
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                var objectUrl = url.createObjectURL(blob);
                window.open(objectUrl);
            }).error(function (data, status, headers, config) {
            });

        }

        $scope.saveCustomerImport = function () {
            $scope.isDisabled = true;
            var formElement = document.getElementById("customerDetails");
            var custDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL + '/customerImportSave', custDetails,
                {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#import_Customer").modal('hide');
                    $scope.getPaginationCustomerList();
                    $scope.isDisabled = false;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.isDisabled = false;
                }
            )
        }
        $scope.editCustomer = function (data) {
            $scope.customerId = data.customerId;
            $scope.selectedState = parseInt(data.stateId);
            $scope.selectedCountry = parseInt(data.country);
            $scope.selectedCurrency = parseInt(data.currencyId);
            $scope.customerNameText = data.customerName;
            $scope.GSTINText = data.gstCode;
            $scope.pincode = data.pincode;
            $scope.customerContactText = data.customerNumber;
            $scope.customerEmailText = data.email;
            $scope.customerAddressText = data.address;
            $scope.personInchargeText = data.personIncharge;
            $scope.bankNameText = data.bankName;
            $scope.accNoText = data.accountNo;
            $scope.panNumberText = data.panNo;
            $scope.bankBranchText = data.branchName;
            $scope.IFSCText = data.iFSCCode;
            $scope.websiteText = data.website;
            $scope.discount = parseInt(data.discountType);
            $scope.creditTermText = data.creditedTerm;
            $scope.creditLimitText = data.creditedLimit;
            $scope.UINText = data.uin;
            $scope.custStatusText = data.status;
            $("#submit").text("update");
            $('#customer-title').text("Edit Customer");
            $("#addCustomer").modal('show');
        };
        // $scope.getCustomerList();
        $scope.next_wizard = function () {
            $("#cus_step1").removeClass("in active");
            $("#cus_step2").addClass("tab-pane fade in active");
        }
        $scope.back_wizard = function () {
            $("#cus_step2").removeClass("in active");
            $("#cus_step1").addClass("tab-pane fade in active");
        }
        $scope.next_wizardEdit = function () {
            $("#cus_step12").removeClass("in active");
            $("#cus_step22").addClass("tab-pane fade in active");
        }
        $scope.back_wizardEdit = function () {
            $("#cus_step22").removeClass("in active");
            $("#cus_step12").addClass("tab-pane fade in active");
        }
        $scope.exportFullData = function () {
            $rootScope.exportAction($scope.exportType);
        }
        $scope.deletecustomer = function (data) {
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
                        $http.post("/hipos" + '/deletecustomer?customerId=' + data).then(function (response) {
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
                            $scope.getCustomerList();
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


        $scope.getStateList = function () {
            $http.post($scope.hiposServerURL + "/getStateList").then(function (response) {
                var data = response.data;
                $scope.stateList = data;
            })
        };
        $scope.getStateList();
        $scope.getCountryList = function () {
            $http.post($scope.hiposServerURL + "/getCountryList").then(function (response) {
                var data = response.data;
                $scope.countryList = data;
            })
        };
        $scope.getCountryList();
        $scope.getCurrencyList = function () {
            $http.post($scope.hiposServerURL + "/getCurrencyList").then(function (response) {
                var data = response.data;
                $scope.currencyList = data;
            })
        };
        $scope.getCurrencyList();

    });