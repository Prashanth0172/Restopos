app.controller('companyController',
    function($scope, $http, $location, $filter, Notification) {

        $scope.hiposServerURL = "/hipos";
        $scope.word = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
        $scope.operation = 'Create';

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
        $scope.countryState = function(country){
            if(angular.isUndefined(country)){
                country ="";
            }
            var url = "/hipos/getCountryState?countryId=" + country;
            $http.post(url).then(function (response) {
                var data = response.data;
                $scope.stateList = angular.copy(data);
                $scope.state=[];
                angular.forEach($scope.stateList,function (val,key) {
                    if(val.status=="Active"){
                        $scope.state.push(val);
                    }

                })

            })
        }
        $scope.disable=false;
        $scope.syncOrders = function(){
            $scope.disable=true;
            var url = "/hipos/syncOrders";
            $http.post(url).then(function (response) {
                var data = response.data;
                Notification.warning({message: 'Sync Completed', positionX: 'center', delay: 2000});
                $scope.disable=false;
            });
        }
        $scope.countryState();
        $scope.imageUpload = function (event) {
            var files = event.target.files;
            var file = files[0];
            var srcString;
            var imageCompressor = new ImageCompressor;
            var compressorSettings = {
                toWidth: 200,
                toHeight: 200,
                mimeType: 'image/png',
                mode: 'strict',
                quality: 1,
                grayScale: false,
                sepia: false,
                threshold: false,
                speed: 'low'
            };
            if (files && file) {
                var reader = new FileReader();
                reader.onload = function (readerEvt) {
                    binaryString = readerEvt.target.result;
                    $('.image-preview').attr('src', binaryString);
                };
                reader.readAsDataURL(file);
                reader.onloadend = function () {
                    srcString = $('.image-preview').attr("src");
                    imageCompressor.run(srcString, compressorSettings, proceedCompressedImage);
                };
            }
        };
        function proceedCompressedImage(compressedSrc) {
            $('#image-preview').attr('src', compressedSrc);
            $scope.fileToUpload = compressedSrc;
        }
        $scope.SaveDetails = function () {
            if (angular.isUndefined($scope.companyname) || $scope.companyname == ''|| $scope.companyname == null) {
                Notification.warning({message: 'Company Name can not be Empty', positionX: 'center', delay: 2000});

            }
           else if (angular.isUndefined($scope.countryId) || $scope.countryId == ''|| $scope.countryId == null) {
                Notification.warning({message: 'Country Name can not be Empty', positionX: 'center', delay: 2000});

            }
          else  if (angular.isUndefined($scope.currencyId) || $scope.currencyId == ''|| $scope.currencyId == null) {
                Notification.warning({message: 'Currency Name can not be Empty', positionX: 'center', delay: 2000});

            }
           else if (angular.isUndefined($scope.stateId) || $scope.stateId == ''|| $scope.stateId == null) {
                Notification.warning({message: 'State Name can not be Empty', positionX: 'center', delay: 2000});

            }
            else {
                var SaveComDetails;
                SaveComDetails = {
                    companyId: $scope.companyId,
                    companyName: $scope.companyname,
                    companyNo: $scope.companyno,
                    panNumber: $scope.panNo,
                    incdate: $scope.incdate,
                    address: $scope.address,
                    logo: $scope.fileToUpload,
                    pincode:$scope.pincode,
                    phone:$scope.phone,
                    fax:$scope.fax,
                    lang:$scope.lang,
                    email:$scope.email,
                    web:$scope.web,
                    countryId:$scope.countryId,
                    currencyId:$scope.currencyId,
                    stateId:$scope.stateId,
                    gstRegister:$scope.gstRegText,
                    gstNo:$scope.gstRegisterNo,
                    gstRegisteredDate:$scope.gstregisteredDate,
                    locationName:$scope.invLoc,
                    connectNo:$scope.hiconnectNo
                };
                $http.post($scope.hiposServerURL + "/SaveDetails", angular.toJson(SaveComDetails)).then(function (response) {
                    var data = response.data;
                    console.log(data);
                    $scope.getCompanyList();
                    if (data == "") {
                        Notification.error({message: ' Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        Notification.success({
                            message: 'Company Details is Created  successfully',
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
        };
        $scope.getCompanyList = function () {
            $http.post($scope.hiposServerURL + '/getCompanyList').then(function (response) {
                var data = response.data;
                if(data != null ) {
                    $scope.companyname = data.companyName;
                    $scope.companyno = data.companyNo;
                    $scope.panNo = data.panNumber;
                    if (data.incdate!=null) {
                        $scope.incdate = new Date(data.incdate);
                    }
                    else{
                        $scope.incdate=new Date();
                    }
                    $scope.address = data.address;
                    $scope.pincode = data.pincode;
                    $scope.phone = data.phone;
                    $scope.fax = data.fax;
                    $scope.fileName = data.logo;
                    $scope.lang = data.lang;
                    $scope.email = data.email;
                    $scope.web = data.web;
                     $scope.countryState(data.countryId);
                    $scope.countryId = data.countryId;
                    $scope.currencyId = data.currencyId;
                    $scope.stateId = data.stateId;
                    $scope.gstRegText = data.gstRegister;
                    $scope.gstRegisterNo = data.gstNo;
                    if (data.gstRegisteredDate!=null) {
                        $scope.gstregisteredDate = new Date(data.gstRegisteredDate);
                    }
                    else{
                        $scope.gstregisteredDate=new Date();
                    }
                    $scope.invLoc = data.locationName;
                    $scope.hiconnectNo = data.connectNo;

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

        // $scope.getStateList = function () {
        //     $http.post($scope.hiposServerURL+"/getStateList").then(function (response) {
        //         var data = response.data;
        //         $scope.stateList = data;
        //     })
        // };
        // $scope.getStateList();
        $scope.getCountryList = function () {
            $http.post($scope.hiposServerURL+"/getCountryList").then(function (response) {
                var data = response.data;
                $scope.countryList = data;
            })
        };
        $scope.getCountryList();
        $scope.getCurrencyList = function () {
            $http.post($scope.hiposServerURL+"/getCurrencyList").then(function (response) {
                var data = response.data;
                $scope.currencyList = data;
            })
        };
        $scope.getCurrencyList();
    });
