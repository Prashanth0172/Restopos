app.controller('smsServerCtrl',
    function ($scope, $http, $location, $filter, Notification, ngTableParams, $timeout) {

        $scope.hiposServerURL =  "/hipos/";
        $scope.removeSMSDetails = function () {
            $scope.smsUrlText = "";
            $scope.apiKeyText = "";
            $scope.senderIdText = "";
            $scope.operation = "";
        };
        $scope.getSmsList = function () {
            if (angular.isUndefined($scope.SearchText)) {
                $scope.SearchText = "";
            }
            $http.post( $scope.hiposServerURL + '/getSMSList?SearchText=' + $scope.SearchText).then(function (data) {
                $scope.smsList = data.data;
            });
        }
        $scope.getSmsList();
        $scope.addNewSmsServer = function () {
            $(".loader").css("display", "block");
            $scope.id = "";
            $scope.serverUrl = "";
            $scope.apiKeyText = "";
            $scope.senderIdText = "";
            $('#sms-title').text("Add SMS");
            $("#add_new_sms_modal").modal('show');
        }, function (error) {
            Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
        };
        $scope.editSMS = function (data) {
            // $http.post('/sms' + '/editSmsDetails').then(function (response) {
            //     var data = response.data[0];
            $scope.id = data.id;
            $scope.serverUrl = data.serverUrl;
            $scope.apiKeyText = data.apiKey;
            $scope.senderIdText = data.senderId;
            $scope.operation = 'Edit';
            $('#sms-title').text("Edit SMS");
            $("#add_new_sms_modal").modal('show');
        }, function (error) {
            Notification.error({
                message: 'Something went wrong, please try again',
                positionX: 'center',
                delay: 2000
            });

        }

        $scope.BackServices = function () {
            window.location.href = '#!/configurator';
        }


        $scope.deleteSMS = function (data) {
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
                        $http.post($scope.hiposServerURL + '/deleteSMSDetails?id=' + data.id).then(function (response) {
                            var data = response.data;
                            $scope.removeSMSDetails();
                            $scope.getSmsList();
                            $("#add_new_sms_modal").modal('hide');
                            Notification.success({
                                message: 'Successfully Deleted',
                                positionX: 'center',
                                delay: 2000
                            });

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

        $scope.saveSMS = function () {
            if ($scope.serverUrl === "" || angular.isUndefined($scope.serverUrl)) {
                Notification.warning({message: 'URL can not be empty', positionX: 'center', delay: 2000});
            }
            else if ($scope.apiKeyText === "" || angular.isUndefined($scope.apiKeyText)) {
                Notification.warning({message: 'API Key can not be empty ', positionX: 'center', delay: 2000});
            }
            else if ($scope.senderIdText === "" || angular.isUndefined($scope.senderIdText)) {
                Notification.warning({message: 'Sender Id can not be empty', positionX: 'center', delay: 2000});
            }
            else {
                $scope.isDisabled = true;
                $timeout(function () {
                    $scope.isDisabled = false;
                }, 3000)
                var saveSmsDetails;
                saveSmsDetails = {
                    id: $scope.id,
                    serverUrl: $scope.serverUrl,
                    apiKey: $scope.apiKeyText,
                    senderId: $scope.senderIdText,

                };
                $http.post($scope.hiposServerURL + '/saveSMSDetails', angular.toJson(saveSmsDetails, "Create")).then(function (response) {
                    var data = response.data;
                    $scope.removeSMSDetails();
                    $scope.getSmsList();
                    $("#add_new_sms_modal").modal('hide');
                    Notification.success({
                        message: 'SMS Configurator Created  successfully',
                        positionX: 'center',
                        delay: 2000
                    });

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
