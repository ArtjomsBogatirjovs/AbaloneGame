const abalone = angular.module('abalone', ['ngRoute', 'gameModule']);
const socket = new SockJS('/abalone');
const stompClient = Stomp.over(socket);

abalone.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/about', {
        templateUrl: 'templates/about.html'
    }).when('/player/create', {
        templateUrl: 'templates/create.html',
        controller: 'PlayerCreateController'
    }).when('/login?logout', {
        redirectTo: '/player/panel'
    }).when('/game/:id', {
        templateUrl: 'templates/game-board.html',
        controller: 'gameController'
    }).when('/player/panel', {
        templateUrl: 'templates/player-panel.html',
        controller: 'newGameController'
    }).otherwise({
        redirectTo: '/player/panel'
    });
}]);

function subscribe(topic, callback) {
    const connected = stompClient.connected;
    if (connected) {
        subscribeToTopic(topic, callback);
        return;
    }
    stompClient.connect({}, function () {
        subscribeToTopic(topic, callback);
    });
}

function subscribeToTopic(topic, callback) {
    stompClient.subscribe(topic, function () {
        callback();
    })
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function showInfo(event) {
    board.drawGameState();
    await sleep(8);
    var fields = parseCoordinates(event.target.innerText);
    for (const field of fields) {
        var x = fieldEnum[field.x];
        var y = field.y;
        historyInfo.push(x + y);
    }

    for (const cord of historyInfo) {
        let selectedHexagon = hexagons[cord];
        board.drawHexagon(selectedHexagon.x, selectedHexagon.y, "#198db7");
        var arrow = event.target.children[3].innerText;

        ctx.font = "bold 50px Arial";
        ctx.fillStyle = "black";
        ctx.fillText(
            arrow,
            hexagons[cord].x + hexagonWidth * 2,
            hexagons[cord].y + hexagonHeight * 1.2,
            hexagonWidth * 0.75
        );
    }
}

function hideInfo() {
    for (const cord of historyInfo) {
        let selectedHexagon = hexagons[cord];
        board.drawHexagon(selectedHexagon.x, selectedHexagon.y, boardColor);
    }
    historyInfo = [];
}

function parseCoordinates(str) {
    const regex = /\((-?\d+), (-?\d+)\)/g;
    const matches = str.matchAll(regex);
    const coordinates = [];
    for (const match of matches) {
        const x = parseInt(match[1]);
        const y = parseInt(match[2]);
        coordinates.push({x, y});
    }
    return coordinates;
}

function calculateOpColor(color) {
    if (color === 'BLACK') {
        return 'WHITE';
    } else {
        return 'BLACK';
    }
}

function hideColorChoose(gameType) {
    if (gameType.value === 'LOCAL' || gameType.value === 'BOT_TRAINING') {
        document.getElementById('colorChoose').style.display = "none";
    } else {
        document.getElementById('colorChoose').style.display = 'block';
    }
}