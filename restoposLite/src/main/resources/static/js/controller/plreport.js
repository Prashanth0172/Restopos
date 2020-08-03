app.controller('plreportCtrl',
    function ($scope, $http,Notification) {
        $scope.bshimServerURL = "/hipos";
        $scope.accountGroupList = [];
        $scope.getAccountMasterListBasedOnReport = function () {
            $scope.val = "pl";
            $(".loader").css("display", "block");
            $http.post($scope.bshimServerURL  + '/getAccountMasterListBasedOnReport?searchText=' + $scope.val).then(function (response) {
                var data = response.data;
                $scope.accountMasterList= data;
                $scope.searchText = val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })

        };

        $scope.getAccountMasterListBasedOnReport();
    });