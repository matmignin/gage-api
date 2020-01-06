var stompClient = null;
var stompClientUrl = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#start").prop("disabled", !connected);
    $("#clear").prop("disabled", !connected);
}

function connect() {
	var args = {
		gameName: $('#gameName').val()
	};
	$.post('/api/v1/game/add', args, function(data) {
		var errorMessage = data.error;
		if (errorMessage !== null) {
			showCommand(errorMessage);
			return;
		}
		var gameCode = data.result.gameCode;
		var subscribeUrl = '/topic/game/' + gameCode;
		stompClientUrl = '/app/command/game/' + gameCode;
		openSocket(subscribeUrl);
		showCommand(gameCode);
	});
}	

function openSocket(subscribeUrl) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe(subscribeUrl, function (message) {
        	console.log(message);
        	showCommand(message.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function startGame() {
    stompClient.send(stompClientUrl, 
    		{}, JSON.stringify({'type': 'START'}));
}

function showCommand(message) {
    $("#commands").append("<tr><td>" + message + "</td></tr>");
}

function clearCommands() {
	$("#commands").html("");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function() { connect(); });
    $("#start").click(function() { startGame(); });
    $("#clear").click(function() { clearCommands(); });
});