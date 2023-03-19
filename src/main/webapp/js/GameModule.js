var gameModule = angular.module('gameModule', []);

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
            var data = scope.newGameData;
            var params = JSON.stringify(data);
            http.post("/game/create", params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (data, status, headers, config) {
                rootScope.gameId = data.data.id;
                location.path('/game/' + rootScope.gameId);
            }).catch(function (data, status, headers, config) {
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
        }).catch(function (data, status, headers, config) {
            location.path('/player/panel');
        });


        scope.joinGame = function (id) {

            var params = {"id": id}

            http.post('/game/connect', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function (data) {
                location.path('/game/' + data.data.id);
            }).catch(function (data, status, headers, config) {
                location.path('/player/panel');
            });
        }
    }]);


gameModule.controller('playerGamesController', ['$scope', '$http', '$location', '$routeParams',
    function (scope, http, location, routeParams) {
        scope.playerGames = [];

        http.get('/game/player/list').then(function (data) {
            scope.playerGames = data.data;
        }).catch(function (data, status, headers, config) {
            location.path('/player/panel');
        });

        scope.loadGame = function (id) {
            location.path('/game/' + id);
        }
    }]);


gameModule.controller('gameController', ['$rootScope', '$routeParams', '$scope', '$http',
    function (rootScope, routeParams, scope, http) {
        canvas = document.getElementById("canvas");
        ctx = canvas.getContext("2d");

        createGame();
        var gameStatus;
        getInitialData()

        function createGame(boardIdsMap) {
            board.drawBoard();
            board.drawGameState(boardIdsMap);
        }

        function getInitialData() {
            http.get('/game/' + routeParams.id).then(function (data) {
                scope.gameProperties = data.data;
                gameStatus = scope.gameProperties.status;
                getMoveHistory();
            }).catch(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
                console.log(scope.errorMessage);
            });
        }

        function getMoveHistory() {
            scope.movesInGame = [];

            return http.get('/move/list').then(function (data) {
                scope.movesInGame = data.data;
            }).catch(function (data, status, headers, config) {
                scope.errorMessage = "Failed to load moves in game"
                console.log(scope.errorMessage);
            });
        }

        function getNextMove() {

            scope.nextMoveData = []

            // COMPUTER IS A SECOND PLAYER
            if (!scope.gameProperties.secondPlayer) {
                http.get("/move/autocreate").then(function (data, status, headers, config) {
                    scope.nextMoveData = data;
                    getMoveHistory().then(function () {
                        var gameStatus = scope.movesInGame[scope.movesInGame.length - 1].status;
                        if (gameStatus !== 'IN_PROGRESS') {
                            alert(gameStatus)
                        }
                    });
                }).catch(function (data, status, headers, config) {
                    scope.errorMessage = "Can't send the move"
                });

                // SECOND PLAYER IS A REAL USER
            } else {
                console.log(' another player move');
            }
        }

        canvas.addEventListener("click", (event) => {
            let clickedField;
            for (const [key, value] of Object.entries(hexagons)) {
                if (isInsideHexagon(event.offsetX, event.offsetY, value.x + 160, value.y + 91)) {
                    clickedField = key;
                    updateSelectedFields(key);
                    drawSelectedField()
                    break;
                }
            }
        });

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

        function makePlayerMove() {
            var parameters = {'fieldCords' : selectedField, 'direction': direction}
            http.get('/move/turn').then(function () {
                http.post("/move/create", parameters, {
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).then(function () {
                    getMoveHistory().then(function () {
                        var gameStatus = scope.movesInGame[scope.movesInGame.length - 1].status;
                        if (gameStatus === 'IN_PROGRESS') {
                            getNextMove();
                        } else {
                            //alert(gameStatus)
                        }
                    });

                }).catch(function (data, status, headers, config) {
                    scope.errorMessage = "Can't send the move"
                    alert(scope.errorMessage)
                });
            }).catch(function (data) {
                Swal.fire(data.data.message)
            })
        }
        const drawSelectedField = () => {
            for (const selectedFieldElement of selectedField) {
                let selectedHexagon = hexagons[selectedFieldElement];
                board.drawHexagon(selectedHexagon.x, selectedHexagon.y, "#296600");
            }
            board.drawGameState();
        };
        function updateSelectedFields(key){
            if(selectedField.includes(key)){
                let selectedHexagon = hexagons[key];
                board.drawHexagon(selectedHexagon.x, selectedHexagon.y, boardColor);
                selectedField.splice(selectedField.indexOf(key),1)
                return;
            }
            if(selectedField.length >= 3){
                let selectedHexagon = hexagons[selectedField[0]];
                board.drawHexagon(selectedHexagon.x, selectedHexagon.y, boardColor);
                selectedField.shift()
            }
            selectedField.push(key)
        }

        scope.setDirection = function (dirX = 0, dirY = 0) {
            direction.x = dirX;
            direction.y = dirY;
            makePlayerMove();
        };

        scope.getFieldsCords = function (fields){
            let result = "";
            for (let field of fields)  {
                result+="(" + field.cordX + ", " + field.cordY + ") ";
            }
            return result;
        }

        scope.getDirectionArrow = function (x,y){
            if(x === -1 && y === -1){
                return "\u2199";
            } else if (x === 0 && y === -1){
                return "\u2193";
            }else if (x === 1 && y === -1){
                return "\u2198";
            }else if (x === -1 && y === 1){
                return "\u2196";
            }else if (x === 0 && y === 1){
                return "\u2191";
            }else if (x === 1 && y === 1){
                return "\u2197";
            }
        }
    }
]);


