
app.controller('employeeCtrl',
    function ($scope, $timeout,$http, $location, $filter, Notification) {
        $scope.hiposServerURL =  "/hipos/";
        $scope.customerId = 1;
        $scope.customer = 1;
        $scope.countVal = 0;
        $scope.customerEmail = "";
        $scope.userRights = [];
        $scope.operation = 'Create';
        $scope.inactiveStatus="Active";
        $scope.number =/^[0-9]/;
        //added for pagination purpose @rahul
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=0;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;

        (function(){
            var setDateRestriction = function(){
                $scope.options = {
                    maxDate : new Date()
                };
            };
            $timeout(setDateRestriction, 0);
        })();

        $scope.getPaginatedEmployeeList = function (page){
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
            if (angular.isUndefined($scope.employeeSearchText)) {
                $scope.employeeSearchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginatedEmployeeList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.employeeSearchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.employeeList = data.list;
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
        $scope.getPaginatedEmployeeList();

        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";
        $scope.inactiveEmployee = function (){
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
            $scope.getPaginatedEmployeeList();

        };
        $scope.removeEmployeeDetails = function () {
            $scope.employeeId = "";
            $scope.EmployeeCodeText = "";
            $scope.EmployeeNameText="";
            $scope.EmployeeAddressText = "";
            $scope.EmployeePhoneText = "";
            $scope.GenderText = "";
            $scope.incentives = "";
            $scope.operation = "";
            $scope.deliveryFlag="";
            $scope.waiterFlag="";
            $scope.dt = new Date();
            $scope.dt1 = new Date();

        };


        $scope.saveNewEmployee = function () {
            if (angular.isUndefined($scope.EmployeeNameText) || $scope.EmployeeNameText == '') {
                Notification.warning({message: 'Employee Name can not be empty', positionX: 'center', delay: 2000});
            }
            else if (angular.isUndefined($scope.dt) || $scope.dt == '' ) {
                Notification.warning({message: 'DOB can not be Empty', positionX: 'center', delay: 2000});
            }
            else if (angular.isUndefined($scope.dt1) || $scope.dt1 == '' ) {
                Notification.warning({message: 'DOJ can not be Empty', positionX: 'center', delay: 2000});
            }
            else if($scope.dt > $scope.dt1){
                Notification.warning({message: 'DOJ can not be less than DOB', positionX: 'center', delay: 2000});
            }
            else if (angular.isUndefined($scope.EmployeeCodeText) || $scope.EmployeeCodeText == '' ) {
                Notification.warning({message: 'Employee code can not be Empty', positionX: 'center', delay: 2000});
            }
            else {
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000)
                var saveEmployeeDetails;
                saveEmployeeDetails = {
                    employeeId: $scope.employeeId,
                    employeeCode: $scope.EmployeeCodeText,
                    employeeName: $scope.EmployeeNameText,
                    employeeAddr: $scope.EmployeeAddressText,
                    employeePhone: $scope.EmployeePhoneText,
                    gender: $scope.GenderText,
                    employeeDOJ: $scope.dt1,
                    employeeDOB: $scope.dt,
                    status:$scope.empStatusText,
                    incentives:$scope.incentives,
                    waiterFlag: $scope.waiterFlag,
                    deliveryFlag: $scope.deliveryFlag,
                };
                $http.post($scope.hiposServerURL + '/saveEmployee', angular.toJson(saveEmployeeDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if(data==""){
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $scope.removeEmployeeDetails();
                        $scope.getPaginatedEmployeeList();
                       $("#add_new_Employee_modal").modal('hide');
                        Notification.success({message: 'Employee Created  successfully', positionX: 'center', delay: 2000});
                    }

                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                });

            }
            ;
        };
        $scope.importEmployee = function(){
            $("#import_Employee").modal('show');
        }

        $scope.downloadSheet = function(){
            var employee = "Employee";
            $http({
                url: $scope.hiposServerURL + "/" + $scope.customer + '/employeesSheet' + "/" + employee,
                method: "POST",
                responseType: 'arraybuffer'
            }).success(function (data) {
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                var objectUrl = url.createObjectURL(blob);
                window.open(objectUrl);
            }).error(function (data, status, headers, config) {
                //upload failed
            });
        }

        // $scope.saveEmployeeImport = function(){
        //     $scope.isDisabled= true;
        //     var formElement = document.getElementById("employeeDetails");
        //     var itemDetails = new FormData(formElement);
        //     $http.post($scope.hiposServerURL + "/" + $scope.customer+ '/employeeImportSave',itemDetails,
        //         { headers: {'Content-Type': undefined},
        //             transformRequest: angular.identity,
        //         }).then(function (response) {
        //             $("#import_Employee").modal('hide');
        //             $scope.getPaginatedEmployeeList();
        //         $scope.isDisabled= false;
        //         }, function (error) {
        //             Notification.error({
        //                 message: 'Something went wrong, please try again',
        //                 positionX: 'center',
        //                 delay: 2000
        //             });
        //         $scope.isDisabled= false;
        //         }
        //     )
        // }

        $scope.addNewEmployeeee = function () {
            $scope.empStatusText="Active";
            $('#employee-title').text("Add Employee");
            $("#submit").text("Save");
            $("#add_new_Employee_modal").modal('show');
        };

        $scope.today = function() {
            $scope.dt = new Date();
            $scope.dt1 = new Date();
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

        $scope.editEmployeePopulate = function (data) {
            $scope.empObj = data;
            $scope.employeeId = data.employeeId;
            $scope.EmployeeCodeText=data.employeeCode;
            $scope.EmployeeNameText=data.employeeName;
            $scope.EmployeeAddressText=data.employeeAddr;
            $scope.EmployeePhoneText=data.employeePhone;
            $scope.GenderText=data.gender;
            $scope.dt1= new Date(data.employeeDOJ);
            $scope.incentives = data.incentives;
            $scope.dt=new Date(data.employeeDOB);
            $scope.empStatusText=data.status;
            $scope.deliveryFlag=data.deliveryFlag;
            $scope.waiterFlag=data.waiterFlag;
            $scope.operation='Edit';
            $('#employee-title').text("Edit Employee");
            $("#submit").text("update");
            $scope.getPaginatedEmployeeList();
            $("#add_new_Employee_modal").modal('show');
        },function (error) {
            Notification.error({message: 'Something went wrong, please try again',positionX: 'center',delay: 2000});

        };
        $scope.deleteEmployee = function (data) {
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
                    //  alert("delete");
                    if(result == true){
                        $scope.employeeId=data.employeeId;
                        $scope.EmployeeCodeText=data.employeeCode;
                        $scope.EmployeeNameText=data.employeeName;
                        $scope.EmployeeAddressText=data.employeeAddr;
                        $scope.EmployeePhoneText=data.employeePhone;
                        $scope.GenderText=data.gender;
                        $scope.dt1=data.employeeDOJ;
                        $scope.dt=data.employeeDOB;
                        $scope.empStatusText=data.status1;
                        var deleteDetails = {
                            employeeId: $scope.employeeId,
                            employeeCode: $scope.employeeCode,
                            employeeName: $scope.employeeName,
                            employeeAddr: $scope.employeeAddr,
                            employeePhone: $scope.employeePhone,
                            gender: $scope.gender,
                            employeeDOJ: $scope.employeeDOJ,
                            employeeDOB: $scope.employeeDOB

                        };
                        $http.post($scope.hiposServerURL +  '/deleteEmployee', angular.toJson(deleteDetails, "Create")).then(function (response, status, headers, config) {
                            var data = response.data;
                            $scope.status=data.status;
                               if($scope.status=="InActive") {
                                $scope.getPaginatedEmployeeList();
                                Notification.success({
                                    message: 'It is Already in use So Status Changes to Inactive',
                                    message: 'It is Already in use So Status Changes to Inactive',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            }else {
                                   $scope.removeEmployeeDetails();
                                   if($scope.empStatusText=="InActive") {
                                       $scope.getPaginatedEmployeeList("","InActive");
                                       Notification.success({
                                           message: 'Status has been changed to Active',
                                           positionX: 'center',
                                           delay: 2000
                                       });
                                   }
                                   else{
                                       $scope.getPaginatedEmployeeList("","");
                                       Notification.success({
                                           message: 'Status has been changed to InActive',
                                           positionX: 'center',
                                           delay: 2000
                                       });
                                   }
                            }
                        }, function (error) {
                            Notification.error({
                                message: 'Something went wrong, please try again',
                                positionX: 'center',
                                delay: 2000
                            });
                        });
                    }
                    //  console.log('This was logged in the callback: ' + result);
                }
            });
        };
        $scope.importPopup = function(){
            $("#import_Employee").modal('show');
        }
        /*****Method for Import********/
        $scope.saveEmployeeImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("employeeDetails");
            var empDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL  + '/saveEmployeeImport',empDetails,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#import_Employee").modal('hide');
                    $scope.getPaginatedEmployeeList();
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

    });
