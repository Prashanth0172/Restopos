app.controller('headerCtrl',
    function($rootScope, $scope,$location, $http,$interval,$window,Notification) {
        $scope.hiposServerURL = "/hipos";
        $scope.getUserObject = function () {
            $http.post("/hipos/getUserObject").then(function (response) {
                var data = response.data;
                $rootScope.userName = data.full_name;
                $rootScope.userAccessRights = data.userAccessRights;
            })
        };
        $scope.getUserObject();


        $scope.getTablesPosMessages = function () {
            $http.post("/hipos/getTablesPosMessages").then(function (response) {
                var data = response.data;
                if(data!="") {
                    $rootScope.tableMessage = data;
                }
            })
        };
        $scope.getTablesPosMessages();


        $scope.getCompanyList = function () {
            $http.post($scope.hiposServerURL + '/getCompanyList').then(function (response) {
                var data = response.data;
                if (data != null) {
                    $scope.companyname = data.companyName;
                    $scope.companyno = data.companyNo;
                    $rootScope.name = data.companyName;
                    $rootScope.phone = data.phone;
                    $rootScope.orderId = data.orderId;
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


        $scope.onlineplacedList=[];
        $scope.digiplacedList=[];

        $scope.getOnlineDeliveryList= function () {
            aaa();
            $scope.previousLength=$scope.onlineplacedList.length;
            $http.get("/hipos/getAllRestaurantNotifications?status="+"placed").then(function (response) {
                var data = response.data;
                $scope.onlineplacedList = data;
                angular.forEach($scope.onlineplacedList,function (val,key) {
                    var object = JSON.parse(val.objectdata);
                    val.customerName = object.order.customer_details.name;
                    val.location = object.order.customer_details.delivery_area;
                    val.amount = object.order.net_amount;
                    val.paymentStatus = object.order.payment_mode;
                    val.phone=object.order.customer_details.phone_number;
                    val.orderId=object.order.order_id;
                    val.email=object.order.customer_details.email;
                    val.pincode=object.order.customer_details.pincode;
                    val.address=object.order.customer_details.address;
                    val.city=object.order.customer_details.city;
                    val.country=object.order.customer_details.country;
                    val.itemdetails=object.order.order_items;
                    val.instructions = object.order.instructions;
                    val.total=object.order.net_amount;
                    $scope.total=0;
                    angular.forEach(val.itemdetails,function (value,key) {
                        $scope.total= (value.item_quantity * value.item_unit_price)+$scope.total;
                    });
                    val.date = object.order.order_date_time;
                    val.amountPaid=object.order.amount_paid;
                    val.balance=object.order.amount_balance;
                    val.cashcollect=object.order.cash_to_be_collected;
                    val.paymentmode=object.order.payment_mode;
                    val.restaurantName=object.order.restaurant_name;
                    val.charges=object.order.charges;
                    val.subTotal=$scope.total;
                    val.totalTaxes=object.order.total_taxes;
                    val.totalCharges=object.order.order_level_total_charges;
                    val.itemCharge=object.order.item_level_total_charges;
                    val.itemTaxes=object.order.item_level_total_taxes;
                    val.discount=object.order.discount;
                })
                if($scope.previousLength<$scope.onlineplacedList.length){
                    post();
                }
            })
        };
        $scope.getOnlineDeliveryList();
        $scope.getDigiOrders= function () {
            aaa();
            $scope.previousLengthdigi=$scope.digiplacedList.length;
            $http.get("/hipos/getAllDigiOrders?status="+"placed").then(function (response) {
                var data = response.data;
                $scope.digiplacedList = data;
                angular.forEach($scope.digiplacedList,function (val,key) {
                    var object = JSON.parse(val.objectdata);
                    val.customerName = object.order.customer_details.name;
                    val.location = object.order.customer_details.delivery_area;
                    val.amount = object.order.net_amount;
                    val.paymentStatus = object.order.payment_mode;
                    val.phone=object.order.customer_details.phone_number;
                    val.orderId=object.order.order_id;
                    val.email=object.order.customer_details.email;
                    val.pincode=object.order.customer_details.pincode;
                    val.address=object.order.customer_details.address;
                    val.city=object.order.customer_details.city;
                    val.country=object.order.customer_details.country;
                    val.itemdetails=object.order.order_items;
                    val.instructions = object.order.instructions;
                    val.total=object.order.net_amount;
                    $scope.total=0;
                    angular.forEach(val.itemdetails,function (value,key) {
                        $scope.total= (value.item_quantity * value.item_unit_price)+$scope.total;
                    });
                    val.date = object.order.order_date_time;
                    val.amountPaid=object.order.amount_paid;
                    val.balance=object.order.amount_balance;
                    val.cashcollect=object.order.cash_to_be_collected;
                    val.paymentmode=object.order.payment_mode;
                    val.restaurantName=object.order.restaurant_name;
                    val.charges=object.order.charges;
                    val.subTotal=$scope.total;
                    val.totalTaxes=object.order.total_taxes;
                    val.totalCharges=object.order.order_level_total_charges;
                    val.itemCharge=object.order.item_level_total_charges;
                    val.itemTaxes=object.order.item_level_total_taxes;
                    val.discount=object.order.discount;
                });
                if($scope.previousLengthdigi<$scope.digiplacedList.length){
                    post();
                }
            })
        };
        $scope.getDigiOrders();
        function post() {
            var audio = new Audio('resource/posimages/icons/NotificationSound.mp3');
            audio.play();
        }
        function aaa() {
            var audio = new Audio('resource/posimages/icons/NotificationSound.mp3');
            audio.pause();
        }
        $interval(function () {
            $scope.getDigiOrders();
            $scope.getOnlineDeliveryList();
            $scope.getUserObject();
            $scope.getTablesPosMessages();
        }, 3000);

        $scope.viewdetails = function (notification) {
            $scope.rejectionmessage="select";
            $scope.notification=notification;
            $("#viewalldetail").modal('show');
        };


        $scope.deactiveLicense = function () {
            $http.post("/hipos"+"/getDeactivatelicense?orderId="+$rootScope.orderId).then(function (response) {
                var data = response.data;
                if(data.message=="Success"){
                    Notification.success({message:'Deactivated Successfully',posittionX:'center',delay:2000});
                    $window.location.href = '/home#!/login';
                }else {
                    Notification.error({message:'Not Registered',positionX:'center',delay:2000})
                }
            })
        }

    });