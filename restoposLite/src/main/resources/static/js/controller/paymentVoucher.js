app.controller('VoucherController',
    function($scope, $http, $location, $filter, Notification, ngTableParams,  $timeout, $window, $rootScope) {
        $scope.hiposServerURL =  "/hipos/";
        $scope.word = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
        $scope.customerId = 1;
        $scope.userRights = [];
        $scope.operation = 'Create';
        $scope.customer = 1;
        $scope.firstPage = true;
        $scope.lastPage = false;
        $scope.pageNo = 0;
        $scope.prevPage = false;
        $scope.isPrev = false;
        $scope.isNext = false;
        $scope.ButtonStatus = "InActive";
        $scope.inactiveStatus = "Active";
        $scope.clicked = false;

        $scope.today = function() {
            $scope.fromDate= new Date();
            $scope.todate = new Date();
        };
        $scope.today();
        $scope.format = 'dd/MM/yyyy';

        $scope.open1 = function() {
            $scope.popup1.opened = true;
        };

        $scope.popup1 = {
            opened: false
        };

        $scope.open2 = function() {
            $scope.popup2.opened = true;
        };
        $scope.popup2 = {
            opened: false
        };
        $scope.inactiveStatus="Active";
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
            $scope.getPaginatedVoucherList();

        };
        $scope.removeVocher = function () {
            $scope.vouId="";
            $scope.vocherCode = "";
            $scope.acddesc = "";
            $scope.statusText = "Active";
            $scope.type="";
            $scope.disType="";
            $scope.discountValue="";
            $scope.minBill="";
            $scope.maxDis="";
            $scope.NoOf="";
        };

        $scope.EditVoucher = function(data) {
            $scope.vouId=data.vouId;
            $scope.vocherCode=data.vocherCode;
            $scope.fromDate= new Date(data.fromDate);
            $scope.toDate=new Date(data.toDate);
            $scope.statusText=data.status;
            $scope.type=data.defaultVoucher;
            $scope.disType=data.discountType;
            $scope.discountValue=data.discountAmount;
            $scope.minBill=data.minBill;
            $scope.maxDis=data.maxDiscount;
            $scope.NoOf=data.noOfTimesValid;
            $scope.operation='Edit';
            $('#stu-title').text("Edit Voucher");
            $("#add_Payment_master").modal('show');
        },function (error) {
            Notification.error({message: 'Something went wrong, please try again',positionX: 'center',delay: 2000});

        };
        $scope.addPayment = function () {
            $scope.vouId=null;
            $scope.fromDate= new Date();
            $scope.toDate = new Date();
            $scope.statusText = "Active";
            $('#student-title').text("Add Voucher");
            $("#add_Payment_master").modal('show');
        };
        $scope.saveVoucher = function () {
                var saveVoucherDetails;
            saveVoucherDetails = {

                vouId: $scope.vouId,
                vocherCode: $scope.vocherCode.toUpperCase(),
                fromDate: $scope.fromDate,
                toDate:$scope.toDate,
                status:$scope.statusText,
                defaultVoucher:$scope.type,
                discountType:$scope.disType,
                discountAmount:$scope.discountValue,
                minBill:$scope.minBill,
                maxDiscount:$scope.maxDis,
                noOfTimesValid:$scope.NoOf
                };
                $http.post($scope.hiposServerURL + '/saveVoucher', angular.toJson(saveVoucherDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if(data==""){
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $scope.removeVocher();
                        $scope.getPaginatedVoucherList();
                        $("#add_Payment_master").modal('hide');
                        Notification.success({message: 'Payment Voucher Created  successfully', positionX: 'center', delay: 2000});
                    }

                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                });

        };
        $scope.getPaginatedVoucherList = function (page){
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
            $http.post($scope.hiposServerURL + "/getPaginatedVoucherList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.searchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.voucherList = data.list;
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
        $scope.getPaginatedVoucherList();
        $scope.DeleteVocher = function (data) {
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
                        $http.post($scope.hiposServerURL + '/DeleteVocher?vouId='+ data).then(function (response) {
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
                            $scope.getPaginatedVoucherList();
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