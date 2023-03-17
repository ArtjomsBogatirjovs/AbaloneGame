const url = 'http://192.168.36.153:8080';
let isCreated = false;
let stompClient;
let pl_id;
let pl_name;
let pl_color;
let pl_direction;

var gameId = 552

function connectToGame() {
    let name = document.getElementById("name").value;
    if (name == null || name === '') {
        // alert("Пожалуйста, введите имя!");
        $("#alert-message").html("Please, enter the name");
    } else {
        $.ajax({
            url: url + "/gameplay/connect",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "id" : "2",
                "name": name,
                "symbol" : "O"
            }),
            success: function (data) {
                pl_id = data.Player[0].id;
                pl_name = data.Player[0].name;
                pl_symbol = data.Player[0].symbol;
                reset();
                connectToSocket(gameId);
                if (name === data.Player[1].name) {
                    // alert(name + ", congrats you're playing with: " + data.Player[0].name);
                    $("#message").html(name + ", вы играете с: " + data.Player[0].name);
                } else {
                    // alert(name + ", congrats you're playing with: " + data.Player[1].name);
                    $("#message").html(name + ", вы играете с: " + data.Player[1].name);
                }
                // alert("Игрок " + data.Player[0].name + " ходит первым!");
                $("#message2").html("Игрок " + data.Player[0].name + " (" + data.Player[0].symbol + ") ходит первым!");
            },
            error: function(request, status, error) {
                let statusCode = request.status; // код ответа
                console.log(error);
                // alert("Ошибка! Попытка присоединения третьего или игрока с существующим именем! Error=  " + statusCode);
                $("#alert-message").html("Ошибка! Попытка присоединения третьего или игрока с существующим именем! Error=  " + statusCode);
            }
        })
    }
}
function connectToSocket(gameId) {
    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay/game/");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            displayResponse(data);
        })
    })
}

