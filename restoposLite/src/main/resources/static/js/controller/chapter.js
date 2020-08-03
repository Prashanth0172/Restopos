app.controller('chapterController',
    function ($scope, $timeout, $http, $location, $filter, Notification) {
        $scope.bshimServerURL = "/bs";
        $scope.operation = 'Create';
        $scope.inactiveStatus = "Active";
        $scope.ButtonStatus = "InActive";
        $scope.firstPage = true;
        $scope.lastPage = false;
        $scope.pageNo = 0;
        $scope.prevPage = false;
        $scope.isPrev = false;
        $scope.isNext = false;
        $scope.clicked = false;

        $scope.inactiveStatus = "Active";
        $scope.removeChapter = function () {
            $scope.chapterId="";
            $scope.chapterText = "";
            $scope.operation = "";
            $scope.LevelName=null;
            $scope.classId=null;
            $scope.subjectId=null;
        };
        $scope.inactiveButton = function () {
            if ($scope.clicked == false) {
                $scope.inactiveStatus = "InActive";
                $scope.ButtonStatus = "Active";
            }
            else {
                $scope.inactiveStatus = "Active";
                $scope.ButtonStatus = "InActive";
            }
            $scope.clicked = !$scope.clicked;
            $scope.getPaginatedChapterList();

        };
        $scope.addChapter = function () {
            $scope.chapterId="";
            $scope.removeChapter();
            $('#chapter-title').text("Add Chapter");
            $scope.StatusText = "Active";
            $("#submit").text("Save");
            $("#add_new_chapter_modal").modal('show');
        };

        $scope.getPaginatedChapterList = function (page) {
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
            $http.post($scope.bshimServerURL + "/getPaginatedChapterList?&type=" +  $scope.inactiveStatus+ '&searchText=' + $scope.searchText, angular.toJson(paginationDetails)).then(function (response) {
                var data = response.data;
                console.log(data);
                var i = 0;
                $scope.chapterList = data.list;
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
        $scope.getPaginatedChapterList();

        $scope.editChapter  = function(data) {
            $scope.chapterId = data.chapterId;
            $scope.chapterText = data.chapterName;
            $scope.subjectclass(data.subjectId);
            $scope.subjectId = data.subjectId;
            $scope.LevelName =data.levelId;
            $scope.classes(data.levelId);
            $scope.classId = data.classId;
            $scope.StatusText = data.status;
            $scope.operation = 'Edit';
            $('#chapter-title').text("Edit Chapter");
            $("#add_new_chapter_modal").modal('show');
        }, function (error) {
            Notification.error({
                message: 'Something went wrong, please try again',
                positionX: 'center',
                delay: 2000
            });
        };
        $scope.saveChapter = function () {
         if ($scope.subjectId === ''||$scope.subjectId==null||angular.isUndefined($scope.subjectId)) {
                Notification.warning({message: 'Select Subject', positionX: 'center', delay: 2000});
            }
           else if ($scope.chapterText === ''||$scope.chapterText==null||angular.isUndefined($scope.chapterText)) {
                Notification.warning({message: 'Enter Chapter Name', positionX: 'center', delay: 2000});
            }
            else {
                var saveDetails;
                saveDetails = {
                    chapterId: $scope.chapterId,
                    chapterName: $scope.chapterText,
                    subjectId: $scope.subjectId,
                    levelId: $scope.LevelName,
                    classId: $scope.classId,
                    status: $scope.StatusText
                };
                $http.post($scope.bshimServerURL + '/saveChapter', angular.toJson(saveDetails, "Create")).then(function (response, status, headers, config) {
                    var data = response.data;
                    if (data == "") {
                        Notification.error({message: 'Already exists', positionX: 'center', delay: 2000});
                    }
                    else {
                        $scope.removeChapter();
                        $scope.getPaginatedChapterList();
                        $("#add_new_chapter_modal").modal('hide');
                        if (!angular.isUndefined(data) && data !== null) {
                            $scope.searchText = "";
                        }
                        Notification.success({
                            message: ' Created  successfully',
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
        $scope.getGradeList = function () {
            $http.post($scope.bshimServerURL + '/getGradeListNormal').then(function (response) {
                var data = response.data.object;
                $scope.gradeList = data;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            });
        };
        $scope.getGradeList();
        $scope.getSubjectList = function () {
            $http.post($scope.bshimServerURL + '/getSubjectList?searchText='+"").then(function (response) {
                var data = response.data;
                $scope.subjectList = data;
            }, function (error) {
                Notification.error({
                    message: 'Something went wrong, please try again',
                    positionX: 'center',
                    delay: 2000
                });
            });
        };
        $scope.getSubjectList();
        $scope.classes = function(level){
            var url = "/bs/getClassLevel?levelId=" + level;
            $http.post(url).then(function (response) {
                var data = response.data;
                $scope.classesList = angular.copy(data);

            })
        }

        $scope.subjectclass = function(classId){
            if(classId!=null){
                var url = "/bs/getclassSubject?classId=" + classId;
                $http.post(url).then(function (response) {
                    var data = response.data;
                    $scope.subjectList = angular.copy(data);
                })
            }
        }

        $scope.deleteChapter = function (data) {
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
                    if(result == true){
                        var deleteDetails = {
                            chapterId:data.chapterId,
                            chapterText:data.chapterName,
                            subjectId:data.subject,
                            LevelName:data.level,
                            classId:data.classes,
                            StatusText:data.status

                        };
                        $http.post($scope.bshimServerURL +"/deleteChapter", angular.toJson(deleteDetails, "Create")).then(function (response, status, headers, config) {
                            var data = response.data;
                            $scope.getPaginatedChapterList();
                            if(data==true){
                                Notification.success({
                                    message: 'Successfully Deleted',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            }else {
                                Notification.warning({
                                    message: 'Cannot delete Already in Use',
                                    positionX: 'center',
                                    delay: 2000
                                });
                            }
                        }, function (error) {
                            Notification.warning({
                                message: 'Cannot be delete,already it is using',
                                positionX: 'center',
                                delay: 2000
                            });
                        });
                    }
                }
            });
        };

    });