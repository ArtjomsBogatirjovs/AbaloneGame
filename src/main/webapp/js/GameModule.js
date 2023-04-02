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
                {name: 'PvE'},
                {name: 'LOCAL'},
                {name: 'BOT_TRAINING'}
            ],
            selectedBoardDimension: {name: 'LOCAL'}
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
            }).catch(function (data) {
                Swal.fire({
                    icon: 'error',
                    title: data.data.message,
                })
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

gameModule.controller('playerGameHistory', ['$scope', '$http', '$location', '$routeParams',
    function (scope, http, location) {
        scope.playerGameHistory = [];

        http.get('/game/history').then(function (data) {
            scope.playerGameHistory = data.data;
        }).catch(function () {
            location.path('/player/panel');
        });

        scope.loadGame = function (id) {
            location.path('/game/' + id);
        }
    }]);

gameModule.controller('gameController', ['$rootScope', '$routeParams', '$scope', '$http',
    function (rootScope, routeParams, scope, http) {
        var gameType;
        canvas = document.getElementById("canvas");
        ctx = canvas.getContext("2d");
        subscribe("/topic/movement/" + routeParams.id, initGameParams)
        initGameParams();

        function makePlayerMove() {
            const parameters = {'fieldCords': selectedField, 'x': direction.x, 'y': direction.y}
            http.get('/move/turn').then(function () {
                http.post("/move/create", parameters, {
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).then(function (data) {
                    initGameParams(data.data);
                    if (gameType === "PvE") {
                        getNextMove(botType);
                    }
                    stompClient.send('/topic/movement/' + routeParams.id, {}, "Field update")
                }).catch(function (data) {
                    stompClient.send('/topic/movement/' + routeParams.id, {}, "Field update")
                    raiseError(data.data.message);
                });
            }).catch(function (data) {
                raiseError(data.data.message);
            })
        }

        function getNextMove(playerType) {
            http.post("/move/automove", playerType).then(function () {
                stompClient.send('/topic/movement/' + routeParams.id, {}, "Field update")
            }).catch(function (data) {
                raiseError(data.data.message);
            });
        }

        scope.setDirection = function (dirX = 0, dirY = 0) {
            direction.x = dirX;
            direction.y = dirY;
            makePlayerMove();
        };

        scope.startAutoplay = function () {
            setInterval(stompClient.send('/topic/movement/' + routeParams.id, {}, "Field update"), 60 * 1000);
            document.getElementById('autoplay').style.display = 'none';
            http.get("/move/autoplay").then(function () {
            }).catch(function () {
                raiseError(data.data.message);
            });
        };

        scope.makeAutoMove = function () {
            http.get('/move/turn').then(function () {
                getNextMove(playerType);
            }).catch(function (data) {
                raiseError(data.data.message);
            })
        };

        scope.getFieldsCords = function (fields) {
            let result = "";
            for (let field of fields) {
                result += "(" + field.x + ", " + field.y + ") ";
            }
            return result;
        }

        scope.getDirectionArrow = function (direction) {
            if (direction === 'UP_LEFT') {
                return "\u2196";
            } else if (direction === 'DOWN_RIGHT') {
                return "\u2198";
            } else if (direction === 'DOWN_LEFT') {
                return "\u2191";
            } else if (direction === 'UP_RIGHT') {
                return "\u2193";
            } else if (direction === 'UP') {
                return "\u2199";
            } else if (direction === 'DOWN') {
                return "\u2197";
            }
        }

        function initGameParams(newGameStatus) {
            gameStatus = newGameStatus;
            http.get('/game/' + routeParams.id).then(function (data) {
                color = data.data.playerColor;
                opColor = calculateOpColor(color);
                scope.playerColor = color;
                gameType = data.data.type;
                showOrHide(gameType);
                if (data.data.status === "SECOND_PLAYER_WON" && gameType !== 'BOT_TRAINING') {
                    Swal.fire(data.data.playerTwo.name + " WON");
                }
                if (data.data.status === "FIRST_PLAYER_WON" && gameType !== 'BOT_TRAINING') {
                    Swal.fire(data.data.playerOne.name + " WON");
                }
                initGameBoard(data.data.ballsCords);
            }).catch(function (data) {
                raiseError(data.data.message);
            });
        }

        function initGameBoard(newGameState) {
            selectedField = [];
            gameState = newGameState;
            scope.ballToLose = gameState[color].length - 8;
            scope.ballToWin = gameState[opColor].length - 8;
            ctx.clearRect(0, 0, canvas.width, canvas.height)
            board.drawBoard();
            board.drawGameState();
            getMoveHistory();
        }

        function getMoveHistory() {
            scope.movesInGame = [];
            return http.get('/move/list').then(function (data) {
                if(gameType === 'PvE' && !data.data.length && color === 'WHITE'){
                    getNextMove(botType);
                }
                if (data.data.length > 0) {
                    document.getElementById('autoplay').style.display = 'none';
                }
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

        function raiseError(message) {
            Swal.fire(message)
            subscribe("/topic/movement/" + routeParams.id, initGameParams)
        }

        function showOrHide(gameType) {
            if (gameType === 'PvE') {
                document.getElementById('autoMove').style.display = 'none';
            }
            if(gameType === 'LOCAL'){
                document.getElementById('playerColor').style.display = 'none';
            }
            if (gameType === 'BOT_TRAINING') {
                document.getElementById('buttons').style.display = 'none';
                document.getElementById('autoMove').style.display = 'none';
                document.getElementById('playerColor').style.display = 'none';
            } else {
                document.getElementById('buttons').style.display = 'block';
                document.getElementById('autoplay').style.display = 'none';
            }
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


