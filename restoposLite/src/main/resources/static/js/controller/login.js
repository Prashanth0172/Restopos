app.controller('loginController',
    function($scope, $http, $location, $filter, Notification, ngTableParams,  $timeout, $window, $rootScope){
        $('#side').hide();
        $('#header').hide();
        $('.main-footer').hide();
        $(".wrapper").css({"backgroundColor": "white"});
        $(".content-wrapper").css({"backgroundColor": "white"});
        $scope.word = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
        $scope.registrationFormData = {};
        $scope.loginFormData = {};
        $scope.forgetPwdFormData = {};
        // $scope.currentPage = PAGE_NAME.trim();
        $scope.loginId = $scope.currentPage == "license" ? "signup" : "signin";

        $scope.changeTab = function (tab) {
            $scope.loginId = tab;
        }
        $scope.getUserObject = function () {
            $http.post("/hipos/getUserObject").then(function (response) {
                var data = response.data;
                $rootScope.userName = data.full_name;
                $rootScope.userAccessRights = data.userAccessRights;
            })
        };
        $scope.getUserObject();

        $scope.changeLanguage = function (key) {

            $translate.use(key)
        };

        $(document).ready(function () {
            $(".btn-login").click(function () {
                $(".bigicon").addClass("fa-spin");
            });
        });

        $scope.register = function () {
            $rootScope.showFullPageLoading = true;

            $http.get("company/getCompanyRegistration?license_key=" + $scope.registrationFormData.license_key).then(
                function success() {
                    $rootScope.showFullPageLoading = false;
                    window.location.reload();
                }, function error(response) {
                    $rootScope.showFullPageLoading = false;
                    if (response.data) {
                        Notification.error({message: response.data['message'], positionX: 'center', delay: 2000});
                    }
                }
            );
        };

        $scope.forgetPwd = function () {
            $http.post("company/forgotPassword", JSON.stringify($scope.forgetPwdFormData)).then(
                function success() {
                    window.location.reload();
                }, function error(response) {
                    if (response.data) {
                        Notification.error({message: response.data['message'], positionX: 'center', delay: 2000});
                    }
                }
            );
        }

        $(".loginBody").css("background", "url('resource/posimages/hinext_new_op_bg.jpg') center center / cover no-repeat rgb(238, 238, 238)");
        $(".hinext_new, .bg_row_info, .bg_sub_row_info, .right_col").css("background", "transparent");

        $scope.removeuserDetails = function () {
            $scope.userEmailText = "";
            $scope.userNameText = "";
            $scope.pswdText = "";
        };
        $scope.getCompanyList = function () {
            $http.post("/hipos" + '/getCompanyList').then(function (response) {
                var data = response.data;
                if(data == "" ) {
                    $scope.hideOrderId = "success";
                }else {
                    $scope.hideOrderId = "fail";

                }
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })

        };
        $scope.getCompanyList();

        $scope.forgotPassword = function () {
            $scope.repassword="";
            $scope.password="";
            $("#add_forgot_modal").modal('show');
        };

        $scope.saveForgotPassword = function () {
            if (angular.isUndefined($scope.userNameText) || $scope.userNameText == '') {
                Notification.warning({message: 'UserName cannot be Empty', positionX: 'center', delay: 2000});
            }else if (angular.isUndefined($scope.password) || $scope.password == '') {
                Notification.warning({message: 'Password cannot be Empty', positionX: 'center', delay: 2000});
            }else if (angular.isUndefined($scope.repassword) || $scope.repassword == '') {
                Notification.warning({message: 'Retype-Password cannot be Empty', positionX: 'center', delay: 2000});
            }else if ($scope.repassword != $scope.password) {
                Notification.error({message: 'Password and Retype Password Should Match ', positionX: 'center', delay: 2000});
            }else {
                var details;
                details = {
                    reTypePassword: $scope.repassword,
                    full_name: $scope.userNameText
                };
                $http.post("/hipos/changeCredentails", angular.toJson(details)).then(function (response) {
                    var data = response.data;
                    if (data == "") {
                        Notification.error({
                            message: 'Invalid Authentication',
                            positionX: 'center',
                            delay: 2000
                        });
                    } else {
                        $("#add_forgot_modal").modal('hide');
                        Notification.error({
                            message: 'Password Changed Successfully',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                });
            };
        };

        $scope.disable=false;
        $scope.saveuservalidate = function () {
            if ($scope.userEmailText == '' || $scope.userEmailText == null || angular.isUndefined($scope.userEmailText)) {
                Notification.warning({message: 'Email cannot be empty', positionX: 'center', delay: 2000});
            }
            else if (angular.isUndefined($scope.userNameText) || $scope.userNameText == '') {
                Notification.warning({message: 'UserName cannot be Empty', positionX: 'center', delay: 2000});
            } else if (angular.isUndefined($scope.pswdText) || $scope.pswdText == '') {
                Notification.warning({message: 'Please Enter Password', positionX: 'center', delay: 2000});
            } else {
                $scope.disable = true;
                var saveUserDetails;
                saveUserDetails = {
                    orderId: $scope.orderId,
                    email: $scope.userEmailText,
                    full_name: $scope.userNameText,
                    passwordUser: $scope.pswdText
                };
                document.cookie = "fullName=" + $scope.userNameText;
                $http.post("/hipos/userValidate", angular.toJson(saveUserDetails)).then(function (response) {
                    var data = response.data;
                    $rootScope.userName = data.full_name;
                    if (data.message == "Not Registered") {
                        $('#side').hide();
                        $scope.removeuserDetails();
                        Notification.error({
                            message: 'Not Registered',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                    else if (data.message == "Invalid") {
                        $('#side').hide();
                        $scope.removeuserDetails();
                        Notification.error({
                            message: 'Invalid Authentication',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                    else {
                        $window.location.href = '/home#!/restdashboard';
                        $('#side').show();
                        $('#header').show();
                        $('.main-footer').show();
                        $scope.removeuserDetails();
                        Notification.success({
                            message: 'Success',
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
                $scope.disable = false;
            };
        };

        $scope.removeuserDetails = function () {
            $scope.userEmailText = "";
            $scope.userNameText = "";
            $scope.pswdText = "";
            $scope.userText="";
            $scope.AddressText="";
            $scope.FullNameText="";
            $scope.SecurityQuestionText="";
            $scope.AnswerText="";
            $scope.TelephoneNumberText="";
            $scope.EmailAddressText="";
            $scope.PasswordText="";
        };
        $scope.isDisabled=true;
    });