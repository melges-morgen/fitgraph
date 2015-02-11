/**
 * Created by melges on 27.01.2015.
 */
var fitgraphRemoteService = angular.module('serverService', ['ngResource']);

fitgraphRemoteService.config(["$httpProvider", function ($httpProvider) {
    $httpProvider.defaults.transformResponse.push(function(responseData){
        convertDateStringsToDates(responseData);
        return responseData;
    });
}]);

fitgraphRemoteService.factory('profileResource', function ($resource) {
    var profileResource = {};
    profileResource.getVkRequestUri = $resource('/rest/profile/getVkRequestUri');

    profileResource.getUserProfile = $resource('/rest/profile');

    return profileResource;
});

fitgraphRemoteService.factory('pointsResource', function ($resource) {
    return $resource('/rest/weight/points', {}, {
        save: {method: 'PUT'},
        change: {method: 'POST'}
    });
});

function convertDateStringsToDates(input) {
    var dateRegex = /^(\d\d)\.(\d\d)\.(\d\d\d\d) (\d\d):(\d\d):(\d\d)/;
    // Ignore things that aren't objects.
    if (typeof input !== "object") return input;

    for (var key in input) {
        if (!input.hasOwnProperty(key)) continue;

        var value = input[key];
        var match;
        // Check for string properties which look like dates.
        if (typeof value === "string" && (match = value.match(dateRegex))) {
                input[key] = new Date(match[3], match[2] - 1, match[1], match[4], match[5], match[6]);
        } else if (typeof value === "object") {
            // Recurse into object
            convertDateStringsToDates(value);
        }
    }
}