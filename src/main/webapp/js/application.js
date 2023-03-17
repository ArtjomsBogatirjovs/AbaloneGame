const abalone = angular.module('abalone', ['ngRoute', 'gameModule']);

abalone.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/about', {
            templateUrl: 'about.html'
        }).
        when('/player/create', {
            templateUrl: 'create.html',
            controller: 'PlayerCreateController'
        }).
        when('/game/:id', {
            templateUrl: 'game-board.html',
            controller: 'gameController'
        }).
        when('/player/panel', {
            templateUrl: 'player-panel.html',
            controller: 'newGameController'
        }).
        otherwise({
            redirectTo: '/player/panel'
        });
}]);
