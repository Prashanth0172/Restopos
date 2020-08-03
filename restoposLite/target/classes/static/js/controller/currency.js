
app.controller('currencyController',
    function ($scope, $timeout,$http, $location, $filter, Notification){
        // body...\
        $scope.hiposServerURL =  "/hipos/";
        $scope.CurrrencyNameText = "";
        $scope.CurrrencyCodeText ="";
        $scope.CurrrencySymbolText ="";
        $scope.CurrrencyDescriptionText ="";
        $scope.StatusText = "";
        $scope.currencyList="";
        $scope.operation = 'Create';
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=0;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;
        $scope.customer=1;
        $scope.inactiveStatus="Active";
        $scope.removeCurrencyDetails = function (){
            $scope.CurrrencyNameText = "";
            $scope.CurrrencyCodeText = "";
            $scope.CurrrencySymbolText = "";
            $scope.CurrrencyDescriptionText = "";
            $scope.StatusText =null;
            $scope.listStatus="";
            $scope.operation = "";
        };
        $scope.companyLogoPath = "";

        $scope.importPopup = function(){
            $("#import_currency").modal('show');
        }


        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";
        $scope.inactiveButton = function (){
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
            $scope.getPaginatedCurrencyList();

        };
        $scope.saveCurrencyImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("currencyDetails");
            var currencyDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL +  '/CurrencyImportSave',currencyDetails,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#import_currency").modal('hide');
                    $scope.getPaginatedCurrencyList();
                    $scope.isDisabled= false;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                    $scope.isDisabled= false;
                }
            )
        }

        $scope.getPaginatedCurrencyList = function (page){
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
            $http.post($scope.hiposServerURL + "/getPaginatedCurrencyList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.searchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                $scope.currencyList = data.list;
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
        $scope.getPaginatedCurrencyList();


        $scope.editCurrency = function(data) {
            $scope.currencyId = data.currencyId;
            $scope.CurrrencyNameText = data.currencyName;
            $scope.CurrrencyCodeText = data.currencyCode;
            $scope.CurrrencySymbolText = data.currencySymbol;
            $scope.CurrrencyDescriptionText = data.currencyDescription;
            $scope.StatusText =data.status;
            $scope.operation='Edit';
            $('#currency-title').text("Edit Currency");
            $("#submit").text("update");
            $("#add_new_Currency_modal").modal('show');
        },function (error) {
            Notification.error({message: 'Something went wrong, please try again',positionX: 'center',delay: 2000});

        };

        $scope.addNewCurrency = function (){
            $scope.StatusText="Active";
            $('#currency-title').text("Add Currency");
            $("#submit").text("Save");
            $("#add_new_Currency_modal").modal('show');
        };

        $scope.importCurrency = function(){
            $("#import_Currency").modal('show');
        }

        $scope.saveCurrency = function (){
            if ($scope.CurrrencyNameText == ''||$scope.CurrrencyNameText == null||angular.isUndefined($scope.CurrrencyNameText)){
                Notification.warning({message: 'Currency Name cannot be empty', positionX: 'center', delay: 2000});
            }else if ($scope.CurrrencyCodeText == ''||$scope.CurrrencyCodeText == null||angular.isUndefined($scope.CurrrencyCodeText)){
                Notification.warning({message: 'Currency Code cannot be empty', positionX: 'center', delay: 2000});
            }
            else {
                var saveCurrencyDetails;
                saveCurrencyDetails = {
                    currencyId : $scope.currencyId,
                    currencyName: $scope.CurrrencyNameText,
                    currencyCode:$scope.CurrrencyCodeText,
                    currencySymbol:$scope.CurrrencySymbolText,
                    currencyDescription:$scope.CurrrencyDescriptionText,
                    status:$scope.StatusText
                };
                $http.post($scope.hiposServerURL + '/saveCurrency', angular.toJson(saveCurrencyDetails, "Create")).then(function (response) {
                    var data = response.data;
                    if(data==""){
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $scope.removeCurrencyDetails();
                        $scope.getPaginatedCurrencyList();
                        $("#add_new_Currency_modal").modal('hide');
                        if (!angular.isUndefined(data) && data !== null) {
                            $scope.searchText = "";
                        }
                        Notification.success({
                            message: 'Currency Created  successfully',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                }, function (error){
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                });
            };
        };
        $scope.deleteCurrency = function (data) {
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
                        $http.post($scope.hiposServerURL + '/deleteCurrency?currencyId='+ data).then(function (response) {
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
                            $scope.getPaginatedCurrencyList();
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
    });
