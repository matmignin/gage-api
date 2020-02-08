var stompClient = null;
var stompClientUrl = null;

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function() { connect(); });
});

function setConnected(connected) {
    $("#connect").hide();
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
		showCommand({'messages' : [gameCode], 'options' : []});
	});
}	

function openSocket(subscribeUrl) {
    var socket = new SockJS('/handler');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe(subscribeUrl, function(command) {
        	console.log(JSON.stringify(command));
        	showCommand(JSON.parse(command.body));
        });
    });
}

function sendCommand(type, value) {
    stompClient.send(stompClientUrl, {}, JSON.stringify({'type': type, 'value' : value}));
}

function showCommand(command) {
	clearCommands();
	command.messages.forEach(function(message) {
		appendCommand("<tr><td>" + message + "</td></tr>");
	});
	command.options.forEach(function(option) {
	    appendCommand("<tr><td><button onClick=\"sendCommand('" + command.type + "', '" + escape(option) + "')\">" + option + "</button></td></tr>");
	});
}

function appendCommand(command) {
	$("#commands").append(command);
}

function clearCommands() {
	$("#commands").html("");
}
