/**
 * Created by chinmay on 7/1/2017.
 */

app.controller('createchartofaccountCtrl',
    function ($scope, $http,Notification,$timeout) {
        //adding var for pagination button on 19/08/2017
        $scope.bshimServerURL = "/hipos/";
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=1;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;

        // $scope.shortCutRestrict=keyPressFactory.shortCutRestrict();

        $scope.accountGroupList = [];
        $scope.addNewCreateChartOfAccount = function () {
            $scope.accountgroup = "";
            $scope.accountTypeId = "";
            $scope.firstLevelAccountId = "";
            $scope.secoundAccountId = "";
            $scope.AccountNameText = "";
            $scope.accountType = "";
            $scope.debit = "";
            $scope.credit = "";
            $scope.firstLevelStringAccCode = "";
            $scope.secoundLevelStringAccCode = "";
            $scope.operation = "";
            $scope.reportvalue="";
            $scope.getAccountGroupList();
            $("#submit").text("Save");
            $("#add_new_createchartofaccount_modal").modal('show');
        };
        $scope.getAccountGroupList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }

            $(".loader").css("display", "block");
            $http.post($scope.bshimServerURL  + '/getAccountGroupList?searchText=' + val).then(function (response) {
                var data = response.data;
                $scope.accountGroupList= data;
                $scope.searchText = val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })

        };

        $scope.getAccountGroupList();
        $scope.getAccountTypeList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }

            $(".loader").css("display", "block");
            $http.post($scope.bshimServerURL  + '/getAccountTypeList?searchText=' + val).then(function (response) {
                var data = response.data;
                $scope.accountTypeList= data;
                $scope.searchText = val;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            })

        };
        $scope.getAccountTypeList();
        $scope.accountFirstLeveMasterList = [];
        $scope.getFirstLevelChartOfAccount = function (accountGroupId) {
            $http.post($scope.bshimServerURL + "/" + 'getFirstLevelAccountMaster?accountType=' + accountGroupId).then(function (response) {
                $scope.accountFirstLeveMasterList = response.data;
            });
        };
        //
        $scope.accountMasterSecoundLevelList = [];
        $scope.getSecoundLevelChartOfAccount = function (firstLevelId) {
            $http.post($scope.bshimServerURL + "/" + 'getSecoundLevelAccount?firstLevelId=' + firstLevelId.accountid).then(function (response) {
                $scope.accountMasterSecoundLevelList = response.data;
            });
        };

        $scope.isValidate = function () {
            $scope.isValide = true;
            if (angular.isUndefined($scope.accountgroup) || $scope.accountgroup==null || $scope.accountgroup=="") {
                $scope.isValide = false;
                Notification.warning({
                    message: 'Please Select Account Group',
                    positionX: 'center',
                    delay: 2000
                });
            }
            else if ($scope.AccountNameText=="" || angular.isUndefined($scope.AccountNameText)) {
                $scope.isValide = false;
                Notification.warning({
                    message: 'Please Enter Account Name',
                    positionX: 'center',
                    delay: 2000
                });
            }
            else if (angular.isUndefined($scope.accountTypeId) || $scope.accountTypeId==null || $scope.accountTypeId=="") {
                $scope.isValide = false;
                Notification.warning({
                    message: 'Please Select Account',
                    positionX: 'center',
                    delay: 2000
                });
            }
            else if (angular.isUndefined($scope.accountType) || $scope.accountType==null || $scope.accountType=="") {
                $scope.isValide = false;
                Notification.warning({
                    message: 'Please Select Account Type',
                    positionX: 'center',
                    delay: 2000
                });
            }
            else if ((angular.isUndefined($scope.reportvalue) || $scope.reportvalue==null || $scope.reportvalue=="")&&(angular.isUndefined($scope.firstLevel) || $scope.firstLevel==null || $scope.firstLevel=="")) {
                $scope.isValide = false;
                Notification.warning({
                    message: 'Please Select P&L or Balance',
                    positionX: 'center',
                    delay: 2000
                });
            }
            // else if (angular.isUndefined($scope.firstLevel) || $scope.firstLevel==null ||  $scope.firstLevel=="") {
            //     $scope.isValide = false;
            //     Notification.warning({
            //         message: 'Please Select Level-1 Account',
            //         positionX: 'center',
            //         delay: 2000
            //     });
            // }
            return $scope.isValide;
        };

        $scope.chartOfAccountList = [];
        $scope.saveOrUpdateCreateChartOfAcc = function () {
            if (!$scope.isValidate()) {

                }
            else {
                if (!angular.isUndefined($scope.firstLevel)) {
                    $scope.firstLevelAccountId = $scope.firstLevel.accountid
                    $scope.firstLevelStringAccCode = $scope.firstLevel.stringAccountCode
                }
                if (!angular.isUndefined($scope.secoundLevel) && $scope.secoundLevel!=null && $scope.secoundLevel!= "") {
                    $scope.secoundAccountId = $scope.secoundLevel.accountid
                    $scope.secoundLevelStringAccCode = $scope.secoundLevel.stringAccountCode
                }
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000)
                var postValue;
                postValue = {
                    accountGroup: $scope.accountgroup,
                    firstLevelAccountId: $scope.firstLevelAccountId,
                    secoundLevelAccountId: $scope.secoundAccountId,
                    accountName: $scope.AccountNameText,
                    accountType: $scope.accountType,
                    accountTypeId: $scope.accountTypeId,
                    debitVal: $scope.debit,
                    creditVal: $scope.credit,
                    firstLevelStringAccCode: $scope.firstLevelStringAccCode,
                    secoundLevelStringAccCode: $scope.secoundLevelStringAccCode,
                    reportvalue:$scope.reportvalue
                };
                $http.post($scope.bshimServerURL + "/" + 'saveOrUpDateChartOfAcc', angular.toJson(postValue)).then(function (response) {
                    Notification.success({
                        message: 'Chart Of Account Created SuccessFully',
                        positionX: 'center',
                        delay: 2000
                    });
                    $("#add_new_createchartofaccount_modal").modal('hide');
                    $scope.getAccountMasterList();
                    $scope.removeChartAccountProperties();
                    $scope.clearChartAccountProperties();
                });
            }
        };

        $scope.editCreateChartOfAcc = function () {
            if (angular.isUndefined($scope.accountType) || $scope.accountType==null || $scope.accountType=="") {
                $scope.isValide = false;
                Notification.warning({
                    message: 'Please Select Account Type',
                    positionX: 'center',
                    delay: 2000
                });
            }
            else {
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000)
                var postValue;
                postValue = {
                    accountid:$scope.accountid,
                    accountName: $scope.AccountNameText,
                    accountType:$scope.accountType

                };
                $http.post($scope.bshimServerURL + "/" + 'editChartOfAcc', angular.toJson(postValue)).then(function (response) {
                    Notification.success({
                        message: 'Chart Of Account Created SuccessFully',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.getAccountMasterList();
                    $("#add_new_createchartofaccount_modal").modal('hide');
                    $scope.removeChartAccountProperties();
                    $scope.clearChartAccountProperties();
                });
            }
        };

        $scope.importPopup = function(){
            $("#import_modal").modal('show');
        }

        $scope.clearChartAccountProperties = function () {
            $scope.accountgroup = "";
            $scope.firstLevel = "";
            $scope.secoundLevel = "";
            $scope.AccountNameText = "";
            $scope.accountType = "";
            $scope.debit = 0;
            $scope.credit = 0;
        };
        $scope.removeChartAccountProperties = function () {
            $scope.accountgroup = null;
            $scope.firstLevelStringAccCode = "";
            $scope.secoundLevelStringAccCode = "";
            $scope.AccountNameText = "";
            $scope.accountType = "";
            $scope.accountTypeId = null;
            $scope.debit = 0;
            $scope.credit = 0;
        };


        $scope.listChartAccountAccount= function () {
            $http.post($scope.bshimServerURL + "/" + 'listChartOfAccount?accountName=' + $scope.accountName).then(function (response) {
                $scope.chartOfAccountList = response.data;
            });
        }

        //Edit Method For the ChartOFAccountList
        $scope.editChartOfAccounts = function (data) {
            $scope.accountid=data.accountid;
            $http.post($scope.bshimServerURL + "/" + 'editCreateChartOfAccount?accountid=' + $scope.accountid).then(function (response) {
                $scope.createchartofaccObj=response.data;
                $scope.AccountNameText = data.accountname;
                $scope.accountType=data.aparcode;
                $scope.operation = 'Edit';
                $("#submit").text("Update");
                $('#chartofaccounts-title').text("Edit ChatOfAccounts");
                $("#add_new_createchartofaccount_modal").modal('show');
            })
        };
        $scope.listChartAccountAccount();
        $scope.getAccountMasterList = function (val) {
            if (angular.isUndefined(val)) {
                val = "";
            }

            $(".loader").css("display", "block");
            $http.post($scope.bshimServerURL  + '/getAccountMasterList?searchText=' + val).then(function (response) {
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

        $scope.getAccountMasterList();
    });