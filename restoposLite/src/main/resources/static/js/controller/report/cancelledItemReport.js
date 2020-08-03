app.controller('cancelledItemReportCtrl',
    function ($scope, $http, $location, $filter, Notification) {
        // body...\
        $scope.hiposServerURL = "/hipos";
        $scope.customer = 1;
        $scope.cancelledItemReportList = [];
        // $scope.today = function() {
            $scope.fromDate = new Date();
            $scope.toDate = new Date();
        // };
        $scope.firstPage=true;
        $scope.lastPage=false;
        $scope.pageNo=1;
        $scope.prevPage=false;
        $scope.isPrev=false;
        $scope.isNext=false;
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

        $scope.isFilterApplied = false;
        $scope.popup2 = {
            opened: false
        };
        angular.isUndefinedOrNull = function(val) {
            return angular.isUndefined(val) || val === null
        }
        $scope.getFinStartDate = function () {
            var url = "reports/getCompanyList";
            $http.get(url)
                .then(function mySuccess(response) {
                    $scope.fromDate = new Date(response.data.startyear);
                    $scope.fromDate.setHours(10);
                    $scope.toDate = new Date();
                    $scope.dateOptions = {
                        minDate : response.data.startyear,
                        maxDate : response.data.endyear
                    };
                    $scope.getPaginatedCancelledItem('firstPage');
                });
        }
        // $scope.getFinStartDate();
        $http.get("/reports/invoice/onLoadPageData").then(function (data) {
            $scope.locationList = data.data.locationList;
        })
        $scope.getTokenList = function () {
            var getRequest = {
                url: '/reports/getTokenList',
                method: 'GET',
                params: '{}'
            }
            $http(getRequest).then(function (successResponse) {
                    $scope.tokenList = successResponse.data;
                },
                function (failureResponse) {
                    console.log(failureResponse)
                });
        }

        $scope.getTokenList();

        $scope.isUndefinedOrNull = function(val) {
            return !angular.isDefined(val) || val === null;
        }

        $scope.getPaginatedCancelledItem = function (page) {
            switch (page) {
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
            }
            if($scope.fromDate > $scope.toDate){
                Notification.info({
                    message: 'FromDate should be less than ToDate ',
                    positionX: 'center',
                    delay: 2000
                });
                return false;
            }
            $http.get("/reports/getCancelledItemList", {
                params: {
                    "fromDate": new Date($scope.fromDate).getTime(),
                    "toDate": new Date($scope.toDate).getTime(),
                    "tokenNo": $scope.tokenNo,
                    "firstPage": $scope.firstPage,
                    "lastPage": $scope.lastPage,
                    "prevPage": $scope.isPrev,
                    "nextPage": $scope.isNext
                }
            }).then(function (data) {
                $scope.cancelledItemReportList = data.data;
                $scope.first = data.data.first;
                $scope.last = data.data.last;
                $scope.prev = data.data.prev;
                $scope.next = data.data.next;
                $scope.pageNo = data.data.pageNo;
            })
        }
        $scope.getPaginatedCancelledItem('firstPage');

    });