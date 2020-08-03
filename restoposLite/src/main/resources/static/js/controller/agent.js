

app.controller('agentCtrl',
    function ($scope, $http, $location, $filter, Notification, ngTableParams, $timeout, $window, $cookies, $httpParamSerializerJQLike) {
        // $scope.shortCutRestrict=keyPressFactory.shortCutRestrict();
        // body...
        $scope.hiposServerURL =  "/hipos/";
        $scope.retailServerURL = "/retail/";
        $scope.posPurchaseServerURL = "hiposrestapi/purchase";
        $scope.returnQty = parseFloat('0.00');
        $scope.totalBeforDiscount = parseFloat('0.00');
        $scope.totalDiscount = parseFloat('0.00');
        $scope.totalAfterDiscount = parseFloat('0.00');
        $scope.totalTaxAmt = parseFloat('0.00');
        $scope.customerId = 1;
        $scope.countVal = 0;
        $scope.customerEmail = "";
        $scope.userRights = [];
        $scope.operation = 'Create';
        $scope.cursorPosVal = 0;
        $scope.customer = 1;
        $scope.SIId = 0;
        $scope.taxList = [];
        $scope.serializableItemsList = [];
        $scope.itemList = [];
        $scope.posSalesList = [];
        $scope.posAdvanceSalesList = [];
        $scope.SIList = [];
        $scope.invokeOrderId = '';
        $scope.InvokeOrderList = [];
        $scope.selectedItemsList = [];
        $scope.retailPostData = [];
        $scope.customerSearchText = "";
        $scope.cardPayementList = [];
        $scope.voucherPayementList = [];
        $scope.removePayments = "";
        $scope.checkbox = "";
        $scope.totaldupltax = parseFloat(0);
        $scope.totaltaxdupltax = parseFloat(0);
        $scope.serialNo = "";
        $scope.today = new Date();
        $scope.companyName = "";
        $scope.cashPayment = parseFloat(0);
        $scope.taxSummaryList = [];
        $scope.companyAddress = "";
        $scope.companyPhoneNo = "";
        $scope.companyFax = "";
        $scope.companyGstNo = "";
        $scope.cutomerName = "";
        $scope.balanceAmt = parseFloat(0);
        $scope.inventoryadress = "";
        $scope.inventoryphone = "";
        $scope.inventoryfax = "";
        $scope.formNo="";
        $scope.returnType="";
        $scope.salesOrderList=[];
        $scope.receiptPaymentList=[];
        $scope.disableButtons=false;
        $scope.customerDetails=[];
        $scope.customerNameText = "";
        $scope.hiConnectNotificationList=[];
        $scope.fullUserName="";
        $scope.word = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
        $scope.number =/^[0-9]/;
        //added for pagination purpose @rahul
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=0;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;
        $scope.inactiveStatus="Active";
        $scope.taxTypes = [
            {name: 'FullTax', value: 'FullTax'},
            {name: 'SimplifiedTax', value: 'SimplifiedTax'},
        ];
        $scope.updatesimplifiedTax = function (newCustVal) {
            $scope.simplifiedTax = newCustVal.fullSimplTax;
        }
        $scope.updateCustomerId = function (newCustVal) {
            $scope.customer = newCustVal.customerId;
            $scope.removeAllItems();
        }
        $scope.companyLogoPath = "";
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
        $scope.addNewAgentPopulate = function () {
            $scope.dt = new Date();
            $scope.agentStatusText="Active"
            $('#agent-title').text("Add Agent");
            $("#submit").text("Save");
            $("#add_new_agent_modal").modal('show');
        }
        $scope.removeAgentDetails = function () {
            $scope.agentId ="";
            $scope.AgentNameText = "";
            $scope.effectiveDateText = "";
            $scope.EmailText="";
            $scope.MobileText="";
            $scope.AddressText="";
            $scope.CommissionText="";
            $scope.GSTINText="";
            $scope.EcommerceText="";
            $scope.GSTINText="";
        };

        $scope.inactiveAgent = function (){
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
            $scope.getPaginatedAgentList();

        };

        $scope.getPaginatedAgentList = function (page){
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
            if (angular.isUndefined($scope.agentSearchText)) {
                $scope.agentSearchText = "";
            }
            $http.post($scope.hiposServerURL + "/getPaginatedAgentList?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.agentSearchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.agentList = data.list;
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
        $scope.getPaginatedAgentList();

        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";
        $scope.saveNewAgent = function () {
            if ($scope.AgentNameText==""||angular.isUndefined($scope.AgentNameText)) {
                Notification.warning({message: 'Agent Name can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
            if ($scope.CommissionText==""||angular.isUndefined($scope.CommissionText)) {
                Notification.warning({message: 'Commission can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
             if ($scope.dt==""||angular.isUndefined($scope.dt)) {
                Notification.warning({message: 'Effective Date can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
             if ($scope.EcommerceText==""||angular.isUndefined($scope.EcommerceText)) {
                Notification.warning({message: 'E-Commerce can not be empty', positionX: 'center', delay: 2000});
                return false;
            }
            else {
                 $scope.isDisabled= true;
                 $timeout(function(){
                     $scope.isDisabled= false;
                 }, 3000)
                var saveItemDetails;
                saveItemDetails = {
                    agentId: $scope.agentId,
                    agentName: $scope.AgentNameText,
                    effectiveDate: $scope.dt,
                    email: $scope.EmailText,
                    mobile: $scope.MobileText,
                    address: $scope.AddressText,
                    commission: $scope.CommissionText,
                    gstinNo: $scope.GSTINText,
                    ecommerce: $scope.EcommerceText,
                    status:$scope.agentStatusText

                };
                $http.post($scope.hiposServerURL +  '/saveNewAgent', angular.toJson(saveItemDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if(data==""){
                        Notification.error({
                            message: 'Agent or Gstin Already Created',
                            positionX: 'center',
                            delay: 2000
                        });
                    }
                    else {
                        $scope.removeAgentDetails();
                        $scope.getPaginatedAgentList();
                        $("#add_new_agent_modal").modal('hide');
                        Notification.success({
                            message: 'Agent Created  successfully',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.getPaginatedAgentList();
                    }
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                });

        };
        }
        $scope.editAgentPopulate = function (data) {
            $scope.agentObj = data;
            $scope.agentId = data.agentId;
            $scope.AgentNameText =data.agentName;
            $scope.EmailText=data.email;
            $scope.MobileText=data.mobile;
            $scope.AddressText=data.address;
            $scope.CommissionText=data.commission;
            $scope.GSTINText=data.gstinNo;
            $scope.dt=new Date(data.effectiveDate);
            $scope.EcommerceText=data.ecommerce;
            $scope.agentStatusText=data.status;
            $("#submit").text("update");
            $('#agent-title').text("Edit Agent");
            $scope.getPaginatedAgentList();
            $("#add_new_agent_modal").modal('show');
        },function (error) {
            Notification.error({message: 'Something went wrong, please try again',positionX: 'center',delay: 2000});
        };
        $scope.deleteAgent = function (data) {
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
                        $http.post($scope.hiposServerURL + '/deleteAgent?agentId='+ data).then(function (response) {
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
                            $scope.getPaginatedAgentList();
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


        /*Import POPUP*/
        $scope.importPopup = function(){
            $("#import_agent").modal('show');
        }
        /*****Method for AgentImport********/
        $scope.saveAgentImport = function(){
            $scope.isDisabled= true;
            var formElement = document.getElementById("agentDetails");
            var agentDetails = new FormData(formElement);
            $http.post($scope.hiposServerURL  + '/saveAgentImport',agentDetails,
                { headers: {'Content-Type': undefined},
                    transformRequest: angular.identity,
                }).then(function (response) {
                    $("#import_agent").modal('hide');
                    $scope.getPaginatedAgentList();
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
/*
*Added on 13/09/2017 for preventing user to enter more than max val by @rahul
 */
app.directive("preventTypingGreater", function() {
    return {
        link: function(scope, element, attributes) {
            var oldVal = null;
            element.on("keydown keyup", function(e) {
                if (Number(element.val()) > Number(attributes.max) &&
                    e.keyCode != 46 // delete
                    &&
                    e.keyCode != 8 // backspace
                ) {
                    e.preventDefault();
                    element.val(oldVal);
                } else {
                    oldVal = Number(element.val());
                }
            });
        }
    };
});