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
            selectedPiece: {name: 'BLACK'},
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
            }).success(function (data, status, headers, config) {
                rootScope.gameId = data.id;
                location.path('/game/' + rootScope.gameId);
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });
        }
    }
]);


gameModule.controller('gamesToJoinController', ['$scope', '$http', '$location',
    function (scope, http, location) {

        scope.gamesToJoin = [];

        http.get('/game/list').success(function (data) {
            scope.gamesToJoin = data;
        }).error(function (data, status, headers, config) {
            location.path('/player/panel');
        });


        scope.joinGame = function (id) {

            var params = {"id": id}

            http.post('/game/connect', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data) {
                location.path('/game/' + data.id);
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });
        }

    }]);


gameModule.controller('playerGamesController', ['$scope', '$http', '$location', '$routeParams',
    function (scope, http, location, routeParams) {

        scope.playerGames = [];

        http.get('/game/player/list').success(function (data) {
            scope.playerGames = data;
        }).error(function (data, status, headers, config) {
            location.path('/player/panel');
        });

        scope.loadGame = function (id) {
            console.log(id);
            location.path('/game/' + id);
        }

    }]);


gameModule.controller('gameController', ['$rootScope', '$routeParams', '$scope', '$http',
    function (rootScope, routeParams, scope, http) {
        canvas = document.getElementById("canvas");
        ctx = canvas.getContext("2d");
        const boardIdsMap = {
            white: ["i5", "i6", "i7", "i8", "i9", "h4", "h5", "h6", "h7", "h8", "h9", "g5", "g6", "g7"],
            black: ["a1", "a2","a3", "a4", "a5", "b1","b2", "b3", "b4", "b5", "b6", "c3", "c4", "c5"],
        };
        createGame(boardIdsMap);
        var gameStatus;
        getInitialData()
        canvas.addEventListener("click", (event) => {
            const clickedField = [];
            for (const [key, value] of Object.entries(hexagons)) {

                if (isInsideHexagon(event.offsetX,event.offsetY,value.x + 160,value.y + 94)) {
                    clickedField.push(key);
                }
            }
            //if (clickedField.length === 1) {
                findClickedPiece(null, clickedField[0]);
            console.log(clickedField)
            //}
        });
        function isInsideHexagon(clickX, clickY,centerX, centerY) {
            const distanceX = Math.abs(clickX - centerX);
            const distanceY = Math.abs(clickY - centerY);
            // console.log("Click on X - " + clickX)
            // console.log("Click on Y - " + clickY)
            // console.log("Click on CenterX - " + centerX)
            // console.log("Click on CenterY - " + centerY)
            // console.log("Click on distanceX - " + distanceX)
            // console.log("Click on distanceY - " + distanceY)
            if (distanceX > scale || distanceY > scale) {
                return false;
            }

            if (distanceX + distanceY <= scale) {
                return true;
            }

            const distanceToCorner = Math.sqrt(
                Math.pow(distanceY- scale / 2, 2) +
                Math.pow(distanceX - scale * Math.sqrt(3) / 2, 2)
            );

            return distanceToCorner <= scale / 2;
        }
        const findClickedPiece = (player, clickedField) => {
            console.log(clickedField);
            if (!selectedField) {
                for (const [key, value] of Object.entries(gameState)) {
                    value.forEach((field) => {
                        if (field === clickedField) {
                            console.log(clickedField);
                            getPossibleMoves(key, clickedField);
                        }
                    });
                }
            } else {
                for (const [key, value] of Object.entries(gameState)) {
                    value.forEach((field) => {
                        if (field === selectedField) {
                            console.log(clickedField);
                            handleMove(clickedField, key, !localPlay);
                        }
                    });
                }
            }
        };

        function createGame(boardIdsMap) {
            board.drawBoard();
            board.drawGameState(boardIdsMap);
        }

        function getInitialData() {

            http.get('/game/' + routeParams.id).success(function (data) {
                scope.gameProperties = data;
                gameStatus = scope.gameProperties.gameStatus;
                getMoveHistory();
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
        }

        function getMoveHistory() {
            scope.movesInGame = [];

            return http.get('/move/list').success(function (data) {
                scope.movesInGame = data;
                scope.playerMoves = [];

                //paint the board with positions from the retrieved moves
                angular.forEach(scope.movesInGame, function (move) {
                    scope.rows[move.boardRow - 1][move.boardColumn - 1].letter = move.playerPieceCode;
                });
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed to load moves in game"
            });
        }

        function checkPlayerTurn() {
            return http.get('/move/turn').success(function (data) {
                scope.playerTurn = data;
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed to get the player turn"
            });
        }

        function getNextMove() {

            scope.nextMoveData = []

            // COMPUTER IS A SECOND PLAYER
            if (!scope.gameProperties.secondPlayer) {
                http.get("/move/autocreate").success(function (data, status, headers, config) {
                    scope.nextMoveData = data;
                    getMoveHistory().success(function () {
                        var gameStatus = scope.movesInGame[scope.movesInGame.length - 1].gameStatus;
                        if (gameStatus !== 'IN_PROGRESS') {
                            alert(gameStatus)
                        }
                    });
                }).error(function (data, status, headers, config) {
                    scope.errorMessage = "Can't send the move"
                });

                // SECOND PLAYER IS A REAL USER
            } else {
                console.log(' another player move');
            }
        }

        function checkIfBoardCellAvailable(boardRow, boardColumn) {

            for (var i = 0; i < scope.movesInGame.length; i++) {
                var move = scope.movesInGame[i];
                if (move.boardColumn === boardColumn && move.boardRow === boardRow) {
                    return false;
                }
            }
            return true;
        }

        scope.rows = [
            [
                {'id': '11', 'letter': '', 'class': 'box'},
                {'id': '12', 'letter': '', 'class': 'box'},
                {'id': '13', 'letter': '', 'class': 'box'}
            ],
            [
                {'id': '21', 'letter': '', 'class': 'box'},
                {'id': '22', 'letter': '', 'class': 'box'},
                {'id': '23', 'letter': '', 'class': 'box'}
            ],
            [
                {'id': '31', 'letter': '', 'class': 'box'},
                {'id': '32', 'letter': '', 'class': 'box'},
                {'id': '33', 'letter': '', 'class': 'box'}
            ]
        ];

        angular.forEach(scope.rows, function (row) {
            row[0].letter = row[1].letter = row[2].letter = '';
            row[0].class = row[1].class = row[2].class = 'box';
        });

        scope.markPlayerMove = function (column) {
            checkPlayerTurn().success(function () {

                var boardRow = parseInt(column.id.charAt(0));
                var boardColumn = parseInt(column.id.charAt(1));
                var params = {'boardRow': boardRow, 'boardColumn': boardColumn}

                if (checkIfBoardCellAvailable(boardRow, boardColumn) === true) {
                    // if player has a turn
                    if (scope.playerTurn === true) {

                        http.post("/move/create", params, {
                            headers: {
                                'Content-Type': 'application/json; charset=UTF-8'
                            }
                        }).success(function () {
                            getMoveHistory().success(function () {

                                var gameStatus = scope.movesInGame[scope.movesInGame.length - 1].gameStatus;
                                if (gameStatus === 'IN_PROGRESS') {
                                    getNextMove();
                                } else {
                                    alert(gameStatus)
                                }
                            });

                        }).error(function (data, status, headers, config) {
                            scope.errorMessage = "Can't send the move"
                        });

                    }
                }
            });
        };
    }
]);




