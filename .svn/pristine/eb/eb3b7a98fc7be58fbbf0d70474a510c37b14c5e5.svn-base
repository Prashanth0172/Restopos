app.directive("addContactModal", function($http,Notification,$timeout){
    return {
        restrict : 'E',
        templateUrl : "partials/addContactModal.html",
        link : function ($scope, element, attrs) {

            $scope.getCountryList = function () {
                $http.post($scope.hiposServerURL+"/getCountryList").then(function (response) {
                    var data = response.data;
                    $scope.countryList = data;
                })
            };
            $scope.getCountryList();
            $scope.countryState = function(country){
                if(angular.isUndefined(country)){
                    country ="";
                }
                var url = "/hipos/getCountryState?countryId=" + country;
                $http.post(url).then(function (response) {
                    var data = response.data;
                    $scope.stateList = angular.copy(data);
                    $scope.state=[];
                    angular.forEach($scope.stateList,function (val,key) {
                        if(val.status=="Active"){
                            $scope.state.push(val);
                        }

                    })

                })
            }
            $scope.getCurrencyList = function () {
                $http.post($scope.hiposServerURL+"/getCurrencyList").then(function (response) {
                    var data = response.data;
                    $scope.currencyList = data;
                })
            };
            $scope.getCurrencyList();
            $scope.addContact = function () {
                $scope.status="Active"
                $(".loader").css("display", "block");
                // $scope.status = "Active";
                $('#contact-title').text("Add Contact");
                $("#submit").text("Save");
                $("#add_new_contact_modal").modal('show');
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
                    $http.post("/hipos"  + '/saveContact', angular.toJson(saveCustomerDetails, "Create")).then(function (response, status, headers, config) {
                        var data = response.data;
                        if (data == "") {
                            Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                        } else {
                            $("#add_new_contact_modal").modal('hide');
                            $scope.removeOtherContactsData();
                            $scope.getContactList();
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



        }
    }
});