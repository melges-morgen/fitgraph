/**
 * Created by melges on 27.01.2015.
 */
var fitgraphRemoteService = angular.module('serverService', ['ngResource']);

fitgraphRemoteService.factory('profileResource', function ($resource) {
    var profileResource = {};
    profileResource.getVkRequestUri = $resource('/rest/profile/getVkRequestUri');

    profileResource.getUserProfile = $resource('/rest/profile');

    return profileResource;
});

fitgraphRemoteService.factory('pointsResource', function ($resource) {
    return $resource('/rest/weight/points', {}, {save: {method: 'PUT'}});
});