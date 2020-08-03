app.directive("addCustomerModal", function($http,Notification,$timeout){
    return {
        restrict : 'E',
        templateUrl : "partials/addCustomerModal.html",
        link : function ($scope, element, attrs) {
            $scope.addCustomer = function () {
                $scope.customerNameText="";
                $scope.GSTINText="";
                $scope.customerEmailText="";
                $scope.customerContactText="";
                $scope.customerAddressText="";
                $scope.pincode="";
                $scope.personInchargeText="";
                $scope.selectedCountry=null;
                $scope.selectedCurrency=null;
                $scope.custStatusText="Active";
                $scope.bankNameText="";
                $scope.accNoText="";
                $scope.discount=null;
                $scope.panNumberText="";
                $scope.bankBranchText="";
                $scope.IFSCText="";
                $scope.websiteText="";
                $scope.creditTermText="";
                $scope.creditLimitText="";
                $scope.customerId = "";
                $('#customer-title').text("Add Customer");
                $("#addCustomer").modal('show');
                $("#submit").text("Save");


                // $http.get("/hipos"+  '/addCustomer').then(function (response) {
                    //     var data = response.data;
                    //     $scope.stateDTOList = angular.copy(data.stateDTOList);
                    //     $scope.countryDTOList = data.countryDTOList;
                    //     $scope.currencyDTOList = data.currencyDTOList;
                    //     $scope.selectedCountry = parseInt(data.cmpyCountry);
                    //     $scope.selectedCurrency = parseInt(data.cmpyCurrency);
                    //     $scope.selectedState = parseInt(data.cmpyState);
                    //     $scope.customerAccountMasterDTOList = angular.copy(data);
                    //     $scope.GSTINText="";
                    //     $scope.custStatusText="Active";
                    //     $("#addCustomer").modal('show');
                    //     // $scope.searchText = $scope.itemSearchText;
                    // }, function (error) {
                    //     Notification.error({
                    //         message: 'Something went wrong, please try again',
                    //         positionX: 'center',
                    //         delay: 2000
                    //     });
                    // })
            };

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
            $scope.countryState();

            $scope.saveCustomer = function () {
                var cust=true;
                var indiaRegex = /^[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z0-9]{2}(z|Z)[a-zA-Z0-9]{1}$/;
                var ObjVal = $scope.GSTINText;
                var mail=$scope.customerEmailText;
                var mailRegex=/([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)\S+/;

                if (angular.isUndefined($scope.customerNameText) || $scope.customerNameText == ''||$scope.customerNameText == null) {
                    Notification.warning({message: 'Customer Name can not be empty', positionX: 'center', delay: 2000});
                    cust=false;

                }
                if($scope.GSTINText==null){
                    $scope.GSTINText="";
                }

               else if ($scope.GSTINText!=="" && $scope.selectedCountry == 1) {
                    if(angular.isUndefined($scope.GSTINText) || $scope.GSTINText !== $scope.GSTINText){
                        Notification.warning({message: 'Please Enter Valid GST No', positionX: 'center', delay: 2000});
                        cust=false;
                    }
                    if($scope.selectedCountry == 1) {
                        if (ObjVal.match(indiaRegex)) {
                            cust=true;
                        }
                        else {
                            Notification.error({message: 'Enter Valid GST Format', positionX: 'center', delay: 2000});
                            cust=true;
                        }
                    }
                }
              else  if ($scope.customerEmailText!=null && $scope.customerEmailText!="") {
                    if(angular.isUndefined($scope.customerEmailText) || $scope.customerEmailText !== $scope.customerEmailText){
                        Notification.warning({message: 'Please Enter Valid Email ID', positionX: 'center', delay: 2000});
                        cust=false;
                    }
                    if (mail.match(mailRegex)) {
                        console.log(mail);
                        cust=true;
                    }
                    else {
                        Notification.error({message: 'Please Enter Valid Email ID', positionX: 'center', delay: 2000});
                        cust=false;
                    }
                }

               if(cust==true) {
                    $scope.saveCustomerOBJ();
                }


            };
            $scope.saveCustomerOBJ = function () {
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000);

                 if (angular.isUndefined($scope.customerContactText)) {
                    Notification.warning({message: 'Enter Valid Contact Number', positionX: 'center', delay: 2000});
                }else {

                     var saveCustomerDetails;
                     saveCustomerDetails = {
                         customerId: $scope.customerId,
                         customerName: $scope.customerNameText,
                         email: $scope.customerEmailText,
                         customerNumber: $scope.customerContactText,
                         address: $scope.customerAddressText,
                         pincode: $scope.pincode,
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
                         status: $scope.custStatusText,
                         bankName: $scope.bankNameText,
                         uin: $scope.UINText,
                         discountType: $scope.discount,
                         accountNo: $scope.accNoText,
                         branchName: $scope.bankBranchText,
                         iFSCCode: $scope.IFSCText,
                         website: $scope.websiteText,
                         panNo : $scope.panNumberText,
                         creditedTerm: $scope.creditTermText,
                         creditedLimit: $scope.creditLimitText
                     };
                     $http.post("/hipos" +'/saveCustomer', angular.toJson(saveCustomerDetails, "Create"))
                         .then(function (response, status, headers, config) {
                             var data = response.data;
                             if (data == "") {
                                 Notification.error({
                                     message: 'NAME or GSTIN or Customer No Already exists',
                                     positionX: 'center',
                                     delay: 2000
                                 });
                             }
                             else {
                                 $scope.removeCustomerDetails();
                                 $scope.getPaginationCustomerList();
                                 $("#addCustomer").modal('hide');
                                 if (!angular.isUndefined(data) && data !== null) {
                                     $("#cus_step2").removeClass("in active");
                                     $("#cus_step1").addClass("tab-pane fade in active");
                                     document.getElementById("cform").reset();
                                     // $scope.customerSearchText = data.customerName;
                                     $scope.customerId = data.customerId;
                                     $scope.customerEmail = data.customerEmail;
                                     $scope.notificationFlag = "";
                                     $scope.fromRegNo = "";
                                     $scope.contactNo1 = /^[0-9]{10,15}$/;
                                     $scope.appendToLocation($scope.customerId);
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
                 }

            };
            $scope.next_wizard_cust = function(){
                $("#addCustomer  #cus_step1").addClass("tab-pane fade");
                $("#addCustomer  #cus_step2").addClass("tab-pane fade in active");
            }

            $scope.back_wizard_cust = function(){
                $("#addCustomer  #cus_step2").removeClass("in active");
                $("#addCustomer  #cus_step1").addClass("tab-pane fade in active");
            }
            $scope.getStateList = function () {
                $http.post($scope.hiposServerURL+"/getStateList").then(function (response) {
                    var data = response.data;
                    $scope.stateList = data;
                })
            };
            $scope.getStateList();
            $scope.getCountryList = function () {
                $http.post($scope.hiposServerURL+"/getCountryList").then(function (response) {
                    var data = response.data;
                    $scope.countryList = data;
                })
            };
            $scope.getCountryList();
            $scope.getCurrencyList = function () {
                $http.post($scope.hiposServerURL+"/getCurrencyList").then(function (response) {
                    var data = response.data;
                    $scope.currencyList = data;
                })
            };
            $scope.getCurrencyList();
        }
    }
});