'use strict';

// Common functions
function saveObjectLocal(key, object) {
    localStorage.setItem(key, JSON.stringify(object));
}

function loadObjectLocal(key) {
    return JSON.parse(localStorage.getItem(key));
}

// Declare app level module which depends on views, and controllers
var fitGraphModule = angular.module('fitGraph', [
    'ui.bootstrap',
    'angularCharts',
    'serverService',
    'ngRoute'
]);

fitGraphModule.run(function ($rootScope) {

});

fitGraphModule.config(function($routeProvider) {
    $routeProvider
        .when("greeting", {
            templateUrl: "templates/greeting.html",
            controller: "AppController"
        })
        .when('/graph', {
            templateUrl: "templates/graph.html",
            controller: "AppController"
        });

    $routeProvider.otherwise({redirectTo: '/greeting'});
});

fitGraphModule.controller('AppController', function($scope, $interval, $location, profileResource) {
    // Main app controller
    $scope.user = loadObjectLocal('user');

    $scope.auth = function () {
        var vkRequestUriResponse = profileResource.getVkRequestUri.get({}, function() {
            var authPopuap = window.open(vkRequestUriResponse['auth_uri'], '_blank',
                'menubar=no,location=no,resizable=no,scrollbars=no,status=no,height=422,width=700');
            var windowPender = $interval(function() {
                if(authPopuap.closed) {
                    $interval.cancel(windowPender);

                    $scope.user = profileResource.getUserProfile.get({}, function () {
                        saveObjectLocal('user', $scope.user);
                        $location.url('/graph');
                    }, function (data, status) {
                        if(status == 401) {
                            saveObjectLocal('user', undefined);
                            $scope.user = undefined;
                        }

                        console.log(data + " Status: " + status);
                    });
                }
            }, 500);
        });
    };
});

fitGraphModule.controller('PointController', function ($scope, $filter,  pointsResource) {
    $scope.dateFormat = 'dd.MM.yyyy HH:mm:ss';
    $scope.graphDateFormat = 'dd.MM HH:mm';

    $scope.chartData = {
        "series": ["Масса (Кг)"],
        "data": [{x: 0, y: [0]}]
    };

    $scope.datePickerFormat = 'dd.MM.yy';

    $scope.chartConfig = {
        title: '',
        tooltips: true,
        labels: false,
        mouseover: function() {},
        mouseout: function() {},
        click: function(d) {console.log(d)},
        legend: {
            display: true,
            //could be 'left, right'
            position: 'right'
        },
        innerRadius: 0, // applicable on pieCharts, can be a percentage like '50%'
        lineLegend: 'traditional' // can be also 'traditional'
    };

    $scope.endDate = new Date(); // Now, by default
    $scope.endDate.setDate($scope.endDate.getDate() + 1);

    $scope.startDate = new Date();
    $scope.startDate.setDate($scope.endDate.getDate() - 7);

    $scope.points = pointsResource.query({startDate: $filter('date')($scope.startDate, $scope.dateFormat),
        endDate: $filter('date')($scope.endDate, $scope.dateFormat)}, pointsReceived);

    //$scope.getPoints =
    $scope.$watch('startDate',periodChanged);

    $scope.$watch('endDate', periodChanged);

    $scope.startDateOpen = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.startDatePopup = true;
    };

    $scope.endDateOpen = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.endDatePopup = true;
    };

    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.pointEditDate = new Date();
    $scope.pointEditTime = new Date();
    $scope.pointEditWeight = 60;

    $scope.savePoint = function () {
        var pointToSave = {date: $filter('date')($scope.pointEditDate.setTime($scope.pointEditTime.getTime()),
            $scope.dateFormat), weight: $scope.pointEditWeight};
        pointsResource.save(pointToSave);
    };

    function pointsReceived(data, status) {
        $scope.chartData.data = [];

        for(var ind = 0; ind < $scope.points.length; ind++) {
            $scope.chartData.data.push({
                x: $filter('date')(Date.parse($scope.points[ind].date), $scope.graphDateFormat),
                y: [$scope.points[ind].weight]
            });
        }
    }

    function periodChanged(newValue, oldValue) {
        $scope.points = pointsResource.query({
            startDate: $filter('date')($scope.startDate, $scope.dateFormat),
            endDate: $filter('date')($scope.endDate, $scope.dateFormat)
        }, pointsReceived);
    }
});

fitGraphModule.run(function($rootScope, $location, $window) {
    if(loadObjectLocal('user') != null)
        $location.url('/graph');
});