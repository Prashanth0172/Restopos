app.controller('useraccountsetupCtrl',
    function ($scope, $http, $location, $filter, Notification, $timeout) {
        window.$scope = $scope;
        $scope.hiposServerURL = "/hipos";
        $scope.customerId = 1;
        $scope.operation = 'Create';
        $scope.customerID = 1;
        $scope.checkbox = "";
        $scope.serialNo = "";
        $scope.today = new Date();
        $scope.inventoryLocationId = null;
        $scope.AddressText = "";
        $scope.PasswordText = "";
        $scope.FullNameText = "";
        $scope.currentPassword = "";
        $scope.SecurityQuestionText = "";
        $scope.AnswerText = ""
        $scope.TelephoneNumberText = "";
        $scope.EmailAddressText = "";
        $scope.location = [];
        $scope.userAccountSetupID = "1";
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=0;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;
        $scope.inactiveStatus="Active";
        $scope.word = /(([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)\S+)|(^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$)/;

        $scope.removeUserAccountSetupDetails = function () {
            $scope.AddressText = "0";
            $scope.PasswordText = "0";
            $scope.FullNameText = "0";
            $scope.SecurityQuestionText = "0";
            $scope.AnswerText = "0";
            $scope.TelephoneNumberText = "0";
            $scope.EmailAddressText = "0";
        };

        $scope.selectedUserAccountSetupListRemoval = {};
        $scope.removeSelectedItems = function () {
            if (angular.isUndefined($scope.selectedUserAccountSetupList) || $scope.selectedUserAccountSetupList.length <= 0) {
                Notification.error({message: 'At lest One item has to be selected', positionX: 'center', delay: 2000});
            } else {
                $scope.selectedUserAccountSetupList = $scope.selectedUserAccountSetupList.filter(function (data, index) {
                    return !($scope.selectedUserAccountSetupListRemoval[index] !== undefined && $scope.selectedUserAccountSetupListRemoval[index]);
                });
                $scope.selectedUserAccountSetupListRemoval = {};
            }
        };

        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";
        $scope.inactiveUserAccountSetUp = function (val) {

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
            $scope.getPaginatedUserAccountSetupList();

        };

        $scope.getPaginatedUserAccountSetupList = function (page){
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
            if (angular.isUndefined($scope.searchText)) {
                $scope.searchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginatedUserAccountSetupList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.searchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.userAccountSetupList = data.list;
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
        $scope.getPaginatedUserAccountSetupList();
        $scope.addNewuseraccountsetupwithpermissions = function () {
            $(".loader").css("display", "block");
            $scope.useraccount_id = "";
            $scope.user_loginId = "";
            $scope.AddressText = "";
            $scope.employee = "";
            $scope.kitchen = "";
            $scope.kitchenId = "";
            $scope.waiterFlag = "";
            $scope.deliveryFlag = "";
            $scope.FullNameText = "";
            $scope.SecurityQuestionText = "";
            $scope.AnswerText = "";
            $scope.TelephoneNumberText = "";
            $scope.EmailAddressText = "";
            $scope.PasswordText = "";
            $scope.status = "Active";
            $scope.addOReditUserTitle = "Add User";
            $scope.getPaginatedUserAccountSetupList();
            $scope.operation = 'Create';
            $scope.isUserAccountInEditMode = false;
            $("#submit").text("Save");
            $("#add_useraccountsetupwithpermissions_modal").modal('show');
        };

        $scope.selecetedValue = "";
        $scope.newValue = function ($event, selectedValue) {
            console.log("Method Called successfully");
        }
        $scope.permissionUserAccountSetup11 = function (data) {
            $scope.useraccount_id = data.useraccount_id;
            $http.post( '/hipos/getEditUserAccessRights?userIdSearchText=' + data.useraccount_id).then(function (response) {
                var data = response.data;
                $scope.userObj = data;
                console.log(data);
                $("#add_permissions_modal_1check").modal('show');
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        };

        var postValue;
        $scope.addPermisstion = function () {
            $scope.isDisabled = true;
            $timeout(function () {
                $scope.isDisabled = false;
            }, 4000)
            console.log(postValue);
            $http.post($scope.hiposServerURL + '/saveUserAccessRights', angular.toJson($scope.userObj, "Create")).then(function (response) {
                var data = response.data;

                $("#add_permissions_modal_1check").modal('hide');

                $scope.getPaginatedUserAccountSetupList();
                Notification.success({
                    message: 'User Permission Updated   successfully',
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


        $scope.editUserAccountSetupWithPermissions = function (data) {
            $scope.setupobj = data;
            $scope.useraccount_id = data.useraccount_id;
            $scope.AddressText = data.user_loginId;
            $scope.currentPassword = '';
            $scope.PasswordText = '';
            $scope.FullNameText = data.full_name;
            $scope.employee = data.employeeflag;
            $scope.kitchenId = data.kitchenId;
            $scope.kitchen = data.kitchenflag;
            $scope.SecurityQuestionText = data.securityQuestion;
            $scope.AnswerText = data.securityAnswer;
            $scope.TelephoneNumberText = data.phone;
            $scope.EmailAddressText = data.email;
            if (data.waiterFlag != null) {
                $scope.waiterFlag = data.waiterFlag;
            }
            if (data.deliveryFlag != null) {
                $scope.deliveryFlag = data.deliveryFlag;
            }
            if (data.accessLocations != null) {
                var boolValue = data.accessLocations.toLowerCase() == 'true' ? true : false;
                $scope.locationAccess = (boolValue) ? "Yes" : "No";
            }
            $scope.status = data.status;
            $scope.addOReditUserTitle = "Edit User";
            $scope.operation = 'Edit';
            $scope.isUserAccountInEditMode = true;
            $("#submit").text("Update");
            $("#add_useraccountsetupwithpermissions_modal").modal('show');
        }, function (error) {
            Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});

        };
        $scope.deleteUserAccountSetup = function (data) {
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
                        $scope.useraccount_id = data.useraccount_id;
                        $scope.AddressText = data.user_loginId;
                        $scope.PasswordText = data.passwordUser;
                        $scope.FullNameText = data.full_name;
                        $scope.SecurityQuestionText = data.securityQuestion;
                        $scope.AnswerText = data.securityAnswer;
                        $scope.TelephoneNumberText = data.phone;
                        $scope.EmailAddressText = data.email;
                        var deleteDetails = {
                            useraccount_id: $scope.useraccount_id,
                            user_loginId: $scope.user_loginId,
                            passwordUser: $scope.passwordUser,
                            full_name: $scope.full_name,
                            securityQuestion: $scope.securityQuestion,
                            securityAnswer: $scope.securityAnswer,
                            phone: $scope.phone,
                            email: $scope.email
                        };
                        $http.post($scope.hiposServerURL + "/" + $scope.customerID + '/deleteUserAccountSetup', angular.toJson(deleteDetails, "Create")).then(function (response, status, headers, config) {
                            var data = response.data;
                            $scope.status = data.status;
                            if (data == "") {
                                $scope.getPaginatedUserAccountSetupList();
                                Notification.success({
                                    message: 'It Is Already In Use Cant be Deleted',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            } else {
                                $scope.getPaginatedUserAccountSetupList();
                                Notification.success({
                                    message: 'Successfully Deleted',
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
                }
            });
        };

        $scope.saveUserAccountSetup = function () {
            if (angular.isUndefined($scope.AddressText) || $scope.itemCodeText == '') {
                Notification.warning({message: 'User Name can not be empty', positionX: 'center', delay: 2000});
                return false;
            }


            if (angular.isUndefined($scope.setupobj) || angular.isUndefined($scope.setupobj.useraccount_id) || $scope.setupobj.useraccount_id == null || $scope.setupobj.useraccount_id == '') {
                if (angular.isUndefined($scope.PasswordText) || $scope.PasswordText == '') {
                    Notification.warning({message: 'Password  can not be empty', positionX: 'center', delay: 2000});
                    return false;
                }
            }
            if (angular.isUndefined($scope.FullNameText) || $scope.FullNameText == '') {
                Notification.warning({message: 'Full Name can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
            if (angular.isUndefined($scope.SecurityQuestionText) || $scope.SecurityQuestionText == '') {
                Notification.warning({message: 'Security Question can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
            if (angular.isUndefined($scope.AnswerText) || $scope.AnswerText == '') {
                Notification.warning({message: 'Answer can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
            if (angular.isUndefined($scope.TelephoneNumberText) || $scope.TelephoneNumberText == '') {
                Notification.warning({message: 'Telephone Number can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
            if (angular.isUndefined($scope.EmailAddressText) || $scope.EmailAddressText == '') {
                Notification.warning({message: 'Email can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
            if (angular.isUndefined($scope.inventoryLocationId) || $scope.inventoryLocationId == '') {
                Notification.warning({
                    message: 'Inventory Location can not be empty',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            if (angular.isUndefined($scope.status) || $scope.status == '') {
                Notification.warning({message: 'status can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
            if ($scope.employee == true && (angular.isUndefined($scope.waiterFlag) || $scope.waiterFlag == '') && (angular.isUndefined($scope.deliveryFlag) || $scope.deliveryFlag == '')) {
                Notification.warning({message: 'Please Select Any Person', positionX: 'center', delay: 2000});
            }
            if ($scope.kitchen == true && (angular.isUndefined($scope.kitchen) || $scope.kitchen == '') && (angular.isUndefined($scope.kitchen) || $scope.deliveryFlag == '')) {
                Notification.warning({message: 'Please Select Any Person', positionX: 'center', delay: 2000});
            }
            else {
                $scope.isDisabled = true;
                $timeout(function () {
                    $scope.isDisabled = false;
                }, 3000)
                var saveUserAccountSetupDetails;
                saveUserAccountSetupDetails = {
                    useraccount_id: $scope.useraccount_id,
                    user_loginId: $scope.AddressText,
                    passwordUser: $scope.PasswordText,
                    currentPassword: $scope.currentPassword,
                    full_name: $scope.FullNameText,
                    securityQuestion: $scope.SecurityQuestionText,
                    securityAnswer: $scope.AnswerText,
                    deliveryFlag: $scope.deliveryFlag,
                    waiterFlag: $scope.waiterFlag,
                    employeeflag: $scope.employee,
                    kitchenflag: $scope.kitchen,
                    kitchenId: $scope.kitchenId,
                    accessLocations: $scope.locationAccess,
                    phone: $scope.TelephoneNumberText,
                    email: $scope.EmailAddressText,
                    location: {inventoryLocationId: $scope.inventoryLocationId},
                    status: $scope.status
                };
                console.log($scope.currentPassword);
                $http.post($scope.hiposServerURL + '/saveUserAccountSetup', angular.toJson(saveUserAccountSetupDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    console.log(data);
                    if (data == "") {
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        if (data.apimessage == '' || data.apimessage == undefined) {
                            Notification.success({
                                message: 'UserAccount Created  successfully ',
                                positionX: 'center',
                                delay: 2000
                            });
                            $("#add_useraccountsetupwithpermissions_modal").modal('hide');
                            $scope.removeUserAccountSetupDetails();
                            $scope.getPaginatedUserAccountSetupList();
                        } else {
                            Notification.success({
                                message: data.apimessage,
                                positionX: 'center',
                                delay: 2000
                            });
                        }
                        if (data.apimessage == "Email ID/Username is already registered.") {
                            Notification.success({
                                message: 'UserAccount Created  successfully ',
                                positionX: 'center',
                                delay: 2000
                            });
                            $("#add_useraccountsetupwithpermissions_modal").modal('hide');
                            $scope.removeUserAccountSetupDetails();
                            $scope.getPaginatedUserAccountSetupList();
                        }
                    }
                    if (data.apimessage == 'User count is exceeded.') {

                        $("#add_useraccountsetupwithpermissions_modal").modal('hide');
                        $scope.removeUserAccountSetupDetails();
                        $scope.getPaginatedUserAccountSetupList();
                    }
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });

                });
            };
        };
        $scope.getBankAccountList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.get($scope.hiposServerURL + "/" + $scope.customerID + '/getBankAccountList?accountSearchText=' + val).then(function (response) {
                var data = response.data;
                $scope.accountList = angular.copy(data);
                $("#edit_configurator").modal('hide');
                $("#selectAccount1").modal('show');
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
        $scope.getCashAccountList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }
            $http.get($scope.hiposServerURL + "/" + $scope.customerID + '/getCashAccountList?accountSearchText=' + val).then(function (response) {
                var data = response.data;
                $scope.accountList = angular.copy(data);
                $("#edit_configurator").modal('hide');
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
        $scope.getConfigurator = function (user) {
            $scope.userId = user.useraccount_id;
            $scope.accessRightsId = user.userAccessRightsId;
            $http.get($scope.hiposServerURL + "/" + $scope.customerID + '/getConfigurator?userAccesssRightsId=' + $scope.accessRightsId).then(function (response, status, headers, config) {
                var data = response.data;
                $scope.UnitPriceText = data.unitPriceAccess;
                $scope.QuantityText = data.quantityAccess;
                $scope.DescriptionText = data.descriptionAccess;
                $scope.CessText = data.cessAccess;
                $scope.UomText = data.uomAccess;
                $scope.TaxText = data.taxAccess;
                $scope.DateText = data.dateAccess;
                $scope.DiscountText = data.discountAccess;
                $scope.PaymentType = data.paymentTypeAccess;
                $scope.BankAccountIDText = data.posBankAccount;
                $scope.CashAccountIDText = data.posCashAccount;
                $scope.PrintTypeText = data.printType;
                $("#edit_configurator").modal('show');
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })
        };
        $scope.saveConfigurator = function () {
            $scope.isDisabled = true;
            $timeout(function () {
                $scope.isDisabled = false;
            }, 3000)
            var userConfigurator;
            userConfigurator = {
                unitPriceAccess: $scope.UnitPriceText,
                quantityAccess: $scope.QuantityText,
                descriptionAccess: $scope.DescriptionText,
                cessAccess: $scope.CessText,
                uomAccess: $scope.UomText,
                taxAccess: $scope.TaxText,
                dateAccess: $scope.DateText,
                discountAccess: $scope.DiscountText,
                paymentTypeAccess: $scope.PaymentType,
                userAccountSetupID: $scope.userId,
                posBankAccount: $scope.BankAccountIDText,
                posCashAccount: $scope.CashAccountIDText,
                printType: $scope.PrintTypeText
            };
            $http.post($scope.hiposServerURL + "/" + $scope.customerID + '/updateUserConfigurator', angular.toJson(userConfigurator, "Create")).then(function (response, status, headers, config) {
                var data = response.data;
                $("#edit_configurator").modal('hide');
                Notification.success({
                    message: 'updated Successfully',
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
        $scope.cashAccount = function (account) {
            $scope.account_name = account.account_name;
            $scope.CashAccountIDText = account.accountid;
            $("#selectAccount").modal('hide');
            $("#edit_configurator").modal('show');
        };
        $scope.bankAccount = function (account) {
            $scope.BankAccountText = account.account_name;
            $scope.BankAccountIDText = account.accountid,
                $("#selectAccount1").modal('hide');
            $("#edit_configurator").modal('show');
        };

        $scope.next_wizard = function () {
            $("#add_permissions_modal_1").modal('hide');
            $("#add_permissions_modal_2").modal('show');
        };

        $scope.back_wizard = function () {
            $("#add_permissions_modal_2").modal('hide');
            $("#add_permissions_modal_1").modal('show');
        };

        $scope.changeUserAccountSetupWithPermissions = function (user) {
            $scope.user = user;
            $("#modal-title").modal('show');
            $("#change_password_popup").modal('show');
        }, function (error) {
            Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
        };
        $scope.getKitchenList = function () {
            $http.post($scope.hiposServerURL+"/getKitchenList").then(function (response) {
                var data = response.data;
                $scope.kitchenList = data;
            })
        };
        $scope.getKitchenList();
        $scope.savepassword = function () {
            if ($scope.user.password != $scope.user.cunformpassword) {
                Notification.warning({message: 'Password  not matching', positionX: 'center', delay: 2000});
                return false;
            }
            var changepassword;
            changepassword = {
                useraccount_id: $scope.user.useraccount_id,
                passwordUser: $scope.user.password
            };
            $http.post($scope.hiposServerURL + "/" + $scope.customerID + '/savepassword', angular.toJson(changepassword, "Create")).then(function (response, status, headers, config) {
                var data = response.data;
                $("#change_password_popup").modal('hide');
                Notification.success({
                    message: 'User Password Updated   successfully',
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
        };
    });