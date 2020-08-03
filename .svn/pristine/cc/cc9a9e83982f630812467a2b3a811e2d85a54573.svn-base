
app.directive("addNewItemModal", function($http,$timeout,$window,Notification){
    return {
        restrict : 'E',
        templateUrl : "partials/addNewItemModal.html",
        link : function ($scope, element, attrs){
            $scope.captalize = function(str){
                var words = str.toLowerCase().split(' ');
                for (var i = 0; i < words.length; i++) {
                    var letters = words[i].split('');
                    letters[0] = letters[0].toUpperCase();
                    words[i] = letters.join('');
                }
                return words.join(' ');
            }
            $scope.importItem = function(){
                $("#import_item").modal('show');
            };

            $scope.addNewItemPopulate = function () {
                $http.post($scope.hiposServerURL+"/getItemCategoryList").then(function (response) {
                    var data = response.data;
                    $scope.itemCategoryDTOList = data;
                })
                $scope.salesPricingText = 0.0;
                $scope.purchasePricingText = 0.0;
                $scope.CESSText = 0.0;
                $scope.itemCodeText = "";
                $scope.itemNameText = "";
                $scope.itemType = "";
                $scope.itemStatusText = "Active";
                $scope.foodtype = null;
                $scope.itemDiscText = "";
                $scope.itemId = "";
                $scope.itemTypeId = "";
                $scope.hsnCode = "";
                $scope.productionItem = "";
                $scope.reOrderLevelText = "";
                $scope.showFirst = true;
                $scope.showSecond = false;
                $scope.isDisabled = false;
                $scope.certNoText = "";
                $("#submit").text("Save");
                $('#modal-title').text("Add Item");
                $("#add_new_item_modal").modal('show');
            };

            $scope.imageUpload = function(event){
                var files = event.target.files;
                var file = files[0];
                var srcString;
                var imageCompressor = new ImageCompressor;
                var compressorSettings = {
                    toWidth : 200,
                    toHeight : 200,
                    mimeType : 'image/png',
                    mode : 'strict',
                    quality : 1,
                    grayScale : false,
                    sepia : false,
                    threshold : false,
                    speed : 'low'
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
            function proceedCompressedImage (compressedSrc){
                $('#image-preview').attr('src', compressedSrc);
                $scope.fileToUpload = compressedSrc;
            }
            $scope.importPopup = function(){
                $("#import_modal").modal('show');
            }
            $scope.customer=1;
            $scope.saveNewItem = function () {
                if (angular.isUndefined($scope.itemCodeText) || $scope.itemCodeText == '') {
                    Notification.warning({message: 'Item Code can not be empty', positionX: 'center', delay: 2000});
                }
                else if (angular.isUndefined($scope.itemNameText) || $scope.itemNameText == '') {
                    Notification.warning({message: 'Item Name can not be empty', positionX: 'center', delay: 2000});
                }
                else if (angular.isUndefined($scope.itemCategoryId) || $scope.itemCategoryId == '' ||  $scope.itemCategoryId==null) {
                    Notification.warning({message: 'Item Category can not be empty', positionX: 'center', delay: 2000});
                }
                else if (angular.isUndefined($scope.hsnCode) || $scope.hsnCode == '') {
                    Notification.warning({message: 'HSN Code can not be empty', positionX: 'center', delay: 2000});
                }
                else {
                    $scope.isDisabled= true;
                    var saveItemDetails;
                    saveItemDetails = {
                        itemId: $scope.itemId,
                        itemCode: $scope.itemCodeText,
                        itemName: $scope.itemNameText,
                        itemDesc: $scope.itemDiscText,
                        salesPrice: $scope.salesPricingText,
                        reOrderLevel:$scope.reOrderLevelText,
                        purchasePrice: $scope.purchasePricingText,
                        itemStatus: $scope.itemStatusText,
                        foodtype: $scope.foodtype,
                        itemType: $scope.itemTypeId,
                        hsnCode: $scope.hsnCode,
                        itemCategory: $scope.itemCategoryId,
                        productionItem:$scope.productionItem,
                        inclusiveJSON:"{\"purchases\":"+$scope.inTax+",\"sales\":"+$scope.outTax+"}",
                        itemImage: $scope.fileToUpload
                    };
                }
                $http.post($scope.hiposServerURL  + '/saveNewItem', angular.toJson(saveItemDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if (data.message == "Duplicate") {
                        $scope.isDisabled= false;
                        Notification.error({
                            message: 'Item Already Exists',
                            positionX: 'center',
                            delay: 2000
                        });
                        $scope.getPaginatedItemList();
                    }

                    else {
                        $("#add_new_item_modal").modal('hide');
                        $scope.getPaginatedItemList();
                        $scope.isDisabled= false;
                        Notification.success({message: 'Item Created  successfully', positionX: 'center', delay: 2000});
                    }
                }, function (error) {
                    $scope.isDisabled= false;
                });

            };
            $scope.isEmpty = function(card){
                return card == '';
            }
            $scope.additemcategory = function () {
                $(".loader").css("display", "block");
                $scope.itemCategoryId=null;
                $scope.CategoryNameText="";
                $scope.CategoryDescriptionText="";
                $scope.defaultType=true;
                $scope.Kitchen=null;
                $scope.ctgryStatusText="Active";
                $('#project-title').text("Add ItemCategory");
                $("#submit").text("Save");
                $("#add_new_ItemCategory_modal").modal('show');
            },function (error) {
                Notification.error({message: 'Something went wrong, please try again', positionX: 'center', delay: 2000});
            };
            $scope.saveNewItemCategory = function () {
                if ($scope.isEmpty($scope.CategoryNameText)) {
                    Notification.warning({message: 'Category Name can not be empty', positionX: 'center', delay: 2000});
                }
                else {
                    $scope.isDisabled= true;
                    var saveItemCategoryDetails;
                    saveItemCategoryDetails = {
                        itemCategoryId:$scope.itemCategoryId,
                        itemCategoryName: $scope.CategoryNameText,
                        itemCategoryDesc: $scope.CategoryDescriptionText,
                        defaultType:$scope.defaultType,
                        status:$scope.ctgryStatusText,
                        kitchen:$scope.Kitchen
                    };

                    $http.post(($scope.hiposServerURL || "/hipos/") + "/" + '/saveNewItemCategory', angular.toJson(saveItemCategoryDetails, "Create")).then(function (response) {
                        var data = response.data;
                        if(data ===""){
                            $scope.isDisabled= false;
                            Notification.error({
                                message: 'Item Category Already Created',
                                positionX: 'center',
                                delay: 2000
                            });
                        }
                        else {
                            $scope.getItemCategoryList();
                            $scope.isDisabled= false;
                            $("#add_new_ItemCategory_modal").modal('hide');
                            Notification.success({
                                message: 'Item Category Created  successfully',
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
                };
            }
            $scope.getKitchenList = function () {
                $http.post($scope.hiposServerURL+"/getKitchenList").then(function (response) {
                    var data = response.data;
                    $scope.kitchenMasterList = data;
                })
            };
            $scope.getKitchenList();

            $scope.getItemCategoryList = function (val,type) {
                if (angular.isUndefined(val)) {
                    val = "";
                }
                if (angular.isUndefined(type)) {
                    type = "";
                }
                $http.post($scope.hiposServerURL +  '/getItemCategoryList?itemCategorySearchText=' + val).then(function (response){
                    var data = response.data;
                    console.log(data);
                    $scope.itemCategoryDTOList = data;
                    $scope.itemCategoryNames=data;
                }, function (error) {
                    Notification.error({
                        message: 'Something went wrong, please try again',
                        positionX: 'center',
                        delay: 2000
                    });
                })
            };
            $scope.getItemCategoryList();

            $scope.selection = [];
            $scope.toggleSelection = function toggleSelection(hsnCode, hsnDesc) {
                var currentHsnCodeObject = {
                    "code": hsnCode,
                    "description": hsnDesc,
                    "status":"Active"
                };
                var status = true;
                for (var i = 0; i < $scope.selection.length; i++) {
                    if (parseFloat($scope.selection[i].code) == parseFloat(hsnCode)) {
                        $scope.selection.splice(i, 1);
                        status = false;
                        break;
                    }
                }
                if (status) {
                    $scope.selection.push(currentHsnCodeObject);
                }
                console.log($scope.selection);
            };
            $scope.next_wizard = function(){
                $("#add_new_item_modal  #sub_step1").removeClass("in active");
                $("#add_new_item_modal  #sub_step2").addClass("tab-pane fade in active");
            }
            $scope.back_wizard = function(){
                $("#add_new_item_modal  #sub_step2").removeClass("in active");
                $("#add_new_item_modal  #sub_step1").addClass("tab-pane fade in active");
            }

            $scope.clicked = false;
            $scope.cartStatus = false;
            $scope.addRemoveCart = "Add To Cart";
            $scope.changeColor = function() {
                var color = $scope.clicked ? '#fff' : 'green';
                $("#colButn").css('background-color', color);
                if($scope.clicked == false){
                    Notification.info({message: 'Item Added To Cart ', positionX: 'center', delay: 2000});
                    $scope.cartStatus = true;
                    $scope.addRemoveCart = "Remove From Cart";
                }
                else{
                    Notification.info({message: 'Item Removed From Cart', positionX: 'center', delay: 2000});
                    $scope.cartStatus = false;
                    $scope.addRemoveCart = "Add To Cart";
                }
                $scope.clicked = !$scope.clicked;
            };

            /*
             *  Code for split the add new item model as show more or less using Angularjs instead of Jquery.
             */
            $scope.showFirst = true;
            $scope.showSecond = false;
            $scope.moreOrLessText = "More";
            $scope.showFirstSecond = function(val){
                if(val == 'second'){
                    $scope.showSecond = true;
                    $scope.showFirst = false;
                    $scope.moreOrLessText = "Less";
                } else {
                    $scope.showSecond = false;
                    $scope.showFirst = true;
                    $scope.moreOrLessText = "More";
                }
            };

        }


    }
});