const gameModule = angular.module('gameModule', []);

gameModule.controller('newGameController', ['$rootScope', '$scope', '$http', '$location',

    function (rootScope, scope, http, location) {
        rootScope.gameId = null;
        scope.newGameData = null;
        scope.newGameOptions = {
            availableColors: [
                {name: 'WHITE'},
                {name: 'BLACK'}
            ],
            selectedColor: {name: 'BLACK'},
            availableGameTypes: [
                {name: 'PvP'},
                {name: 'PvE'}
            ],
            selectedBoardDimension: {name: 'PvE'}
        };
        scope.createNewGame = function () {
            const data = scope.newGameData;
            const params = JSON.stringify(data);
            http.post("/game/create", params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (data) {
                rootScope.gameId = data.data.gameId;
                location.path('/game/' + rootScope.gameId);
            }).catch(function () {
                location.path('/player/panel');
            });
        }
    }
]);


gameModule.controller('gamesToJoinController', ['$scope', '$http', '$location',
    function (scope, http, location) {
        scope.gamesToJoin = [];
        http.get('/game/list').then(function (data) {
            scope.gamesToJoin = data.data;
        }).catch(function () {
            location.path('/player/panel');
        });
        scope.joinGame = function (id) {
            const params = {"id": id};
            http.post('/game/connect', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (data) {
                location.path('/game/' + data.data.id);
            }).catch(function () {
                location.path('/player/panel');
            });
        }
    }]);


gameModule.controller('playerGamesController', ['$scope', '$http', '$location', '$routeParams',
    function (scope, http, location) {
        scope.playerGames = [];

        http.get('/game/player/list').then(function (data) {
            scope.playerGames = data.data;
        }).catch(function () {
            location.path('/player/panel');
        });

        scope.loadGame = function (id) {
            location.path('/game/' + id);
        }
    }]);


gameModule.controller('gameController', ['$rootScope', '$routeParams', '$scope', '$http',
    function (rootScope, routeParams, scope, http) {
        subscribe("/topic/movement/" + routeParams.id, initGameParams)
        canvas = document.getElementById("canvas");
        ctx = canvas.getContext("2d");
        initGameParams();

        function makePlayerMove() {
            const parameters = {'fieldCords': selectedField, 'direction': direction}
            http.get('/move/turn').then(function () {
                http.post("/move/create", parameters, {
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).then(function () {
                    initGameParams();
                    //getNextMove();
                    stompClient.send('/topic/movement/' + routeParams.id, {}, "lol")
                }).catch(function (data) {
                    raiseError(data.data.message);
                });
            }).catch(function (data) {
                raiseError(data.data.message);
            })
        }

        function getNextMove() {
            scope.nextMoveData = []
            // COMPUTER IS A SECOND PLAYER
            if (!scope.gameProperties.secondPlayer) {
                http.get("/move/autocreate").then(function (data) {
                    scope.nextMoveData = data;
                    getMoveHistory().then(function () {
                        var gameStatus = scope.movesInGame[scope.movesInGame.length - 1].status;
                        if (gameStatus !== 'IN_PROGRESS') {
                            alert(gameStatus)
                        }
                    });
                }).catch(function () {
                    scope.errorMessage = "Can't send the move"
                });

                // SECOND PLAYER IS A REAL USER
            } else {
                console.log(' another player move');
            }
        }


        scope.setDirection = function (dirX = 0, dirY = 0) {
            direction.x = dirX;
            direction.y = dirY;
            makePlayerMove();
        };

        scope.getFieldsCords = function (fields) {
            let result = "";
            for (let field of fields) {
                result += "(" + field.cordX + ", " + field.cordY + ") ";
            }
            return result;
        }

        scope.getDirectionArrow = function (x, y) {
            if (x === -1 && y === -1) {
                return "\u2196";
            } else if (x === 1 && y === 1) {
                return "\u2198";
            } else if (x === 0 && y === -1) {
                return "\u2199";
            } else if (x === 0 && y === 1) {
                return "\u2197";
            } else if (x === -1 && y === 0) {
                return "\u2191";
            } else if (x === 1 && y === 0) {
                return "\u2193";
            }
        }

        function initGameParams() {
            http.get('/game/' + routeParams.id).then(function (data) {
                initGameBoard(data.data.ballsCords);
            }).catch(function (data) {
                raiseError(data.data.message);
            });
        }

        function initGameBoard(newGameState) {
            selectedField = [];
            gameState = newGameState;
            ctx.clearRect(0, 0, canvas.width, canvas.height)
            board.drawBoard();
            board.drawGameState();
            getMoveHistory();
        }

        function getMoveHistory() {
            scope.movesInGame = [];
            return http.get('/move/list').then(function (data) {
                scope.movesInGame = data.data;
            }).catch(function (data) {
                raiseError(data.data.message);
            });
        }

        function updateSelectedFields(key) {
            if (selectedField.includes(key)) {
                let selectedHexagon = hexagons[key];
                board.drawHexagon(selectedHexagon.x, selectedHexagon.y, boardColor);
                selectedField.splice(selectedField.indexOf(key), 1)
                return;
            }
            if (selectedField.length >= 3) {
                let selectedHexagon = hexagons[selectedField[0]];
                board.drawHexagon(selectedHexagon.x, selectedHexagon.y, boardColor);
                selectedField.shift()
            }
            selectedField.push(key)
        }

        const drawSelectedField = () => {
            for (const selectedFieldElement of selectedField) {
                let selectedHexagon = hexagons[selectedFieldElement];
                board.drawHexagon(selectedHexagon.x, selectedHexagon.y, "#296600");
            }
            board.drawGameState();
        }

        function isInsideHexagon(clickX, clickY, centerX, centerY) {
            const distanceX = Math.abs(clickX - centerX);
            const distanceY = Math.abs(clickY - centerY);
            if (distanceX > scale || distanceY > scale) {
                return false;
            }
            if (distanceX + distanceY <= scale) {
                return true;
            }
            const distanceToCorner = Math.sqrt(
                Math.pow(distanceY - scale / 2, 2) +
                Math.pow(distanceX - scale * Math.sqrt(3) / 2, 2)
            );
            return distanceToCorner <= scale / 2;
        }
        function raiseError(message){
            Swal.fire(message)
            subscribe("/topic/movement/" + routeParams.id, initGameParams)
        }

        canvas.addEventListener("click", (event) => {
            let clickedField;
            for (const [key, value] of Object.entries(hexagons)) {
                if (isInsideHexagon(event.offsetX, event.offsetY, value.x + 160, value.y + 91)) {
                    clickedField = key;
                    updateSelectedFields(key);
                    drawSelectedField();
                    break;
                }
            }
        });
    }
]);


