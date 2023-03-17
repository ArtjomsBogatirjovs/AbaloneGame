
var turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
var num = 0;
var gameOn = false;

onload;

function playerTurn(id) {
    if (gameOn) {
        let spotTaken = $("#" + id).text();
        if (spotTaken === "#") {
            makeAMove(id);
        }
    }
}
function makeAMove(text) {
    if (num === 0) {
        num = 1;
    } else {
        num += 1;
        // $("#message3").html(num);
    }
    $.ajax({
        url: url + "/gameplay/game",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "num": num,
            "playerId": pl_id,
            "text": text
        }),
        success: function (data) {
            gameOn = false;
            pl_id = data.currPlayer.id;
            pl_name = data.currPlayer.name;
            pl_color = data.currPlayer.color;
            displayResponse(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}
function displayResponse(data) {
    let board = data.board;
    for (let i = 0; i < board.length; i++) {
        for (let j = 0; j < board[i].length; j++) {
            if (board[i][j] === "X") {
                turns[i][j] = 'X'
            } else if (board[i][j] === "O") {
                turns[i][j] = 'O';
            }
            let id = coordTranfer(i, j);
            $("#" + id).text(turns[i][j]);
        }
    }

    $("#message").html(" ");

    if ( data.status === "FINISHED" && num === 9) {
        $("#message").html("Игра завершилась вничью!");
        // alert("Игра завершилась вничью!");
    }

    if (data.GameResult != null) {
        // alert("Победил игрок " + data.GameResult.Player.name + " ("+ data.GameResult.Player.symbol + ")");
        $("#message2").html("Победил игрок " + data.GameResult.Player.name + " ("+ data.GameResult.Player.symbol + ")!");
    }

    if (data.curPlayer.name === data.Player[0].name) {
        $("#message2").html("Следующий ход игрока " + data.Player[0].name + " ("+ data.Player[0].symbol + ")");
    } else  {
        $("#message2").html("Следующий ход игрока " + data.Player[1].name + " ("+ data.Player[1].symbol + ")");
    }

    gameOn = true;
}


