
app.controller('kitchenCtrl',
    function ($scope, $timeout,$http, $location, $filter, Notification) {
        // body...\
        $scope.hiposServerURL =  "/hipos/";
        $scope.KitchenNameText = "";
        $scope.description = "";
        $scope.StatusText = "";
        $scope.operation = 'Create';
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo = 0;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;
        $scope.customer=1;
        $scope.inactiveStatus="Active";
        $scope.removeKitchenDetails = function () {
            $scope.KitchenNameText = "";
            $scope.StatusText =null;
            $scope.description="";
            $scope.operation = "";
        };

        $scope.clicked = false;
        $scope.ButtonStatus = "InActive";

        $scope.editKitchen = function(data) {
            $scope.setupobj = data;
            $scope.kitchenId = data.kitchenId;
            $scope.KitchenNameText = data.kitchenName;
            $scope.description = data.description;
            $scope.StatusText =data.status;
            $scope.operation='Edit';
            $('#kitchen-title').text("Edit kitchen Details");
            $("#submit").text("Update");
            $("#add_new_kitchen_modal").modal('show');
        },function (error) {
            Notification.error({message: 'Something went wrong, please try again',positionX: 'center',delay: 2000});

        };

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
            $scope.getPaginationList();

        };

        $scope.addNewKitchen = function () {
            $scope.StatusText="Active";
            $('#kitchen-title').text("Add Kitchen Details");
            $("#submit").text("Save");
            $("#add_new_kitchen_modal").modal('show');
        };


        $scope.saveKitchen = function () {
            if ($scope.KitchenNameText == ''||$scope.KitchenNameText==null||angular.isUndefined($scope.KitchenNameText)) {
                Notification.warning({message: 'Kitchen Name can not be empty', positionX: 'center', delay: 2000});
            }
            else {
                $scope.isDisabled= true;
                $timeout(function(){
                    $scope.isDisabled= false;
                }, 3000)
                var saveKitchenDetails;
                saveKitchenDetails = {
                    kitchenId : $scope.kitchenId,
                    kitchenName: $scope.KitchenNameText,
                    description: $scope.description,
                    status:$scope.StatusText
                };
                $http.post($scope.hiposServerURL + '/saveKitchen', angular.toJson(saveKitchenDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if(data==""){
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $scope.removeKitchenDetails();
                        $scope.getPaginationList();
                        $("#add_new_kitchen_modal").modal('hide');
                        if (!angular.isUndefined(data) && data !== null) {
                            $scope.kitchenSearchText = "";
                        }
                        Notification.success({
                            message: 'Kitchen Created  successfully',
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
            ;
        };
        $scope.getPaginationList = function (page){
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
            if (angular.isUndefined($scope.kitchenSearchText)) {
                $scope.kitchenSearchText = "";
            }
            $http.post($scope.hiposServerURL + "/getpaginatedKitchen?&type=" + $scope.inactiveStatus+ '&searchText=' + $scope.kitchenSearchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                $scope.kitchenList = data.list;
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
        $scope.getPaginationList();


    });