var stompClient = null;
var roomId = null;

function setConnected(connected) {
    document.getElementById('connectForm').style.display = connected ? 'none' : 'block';
    document.getElementById('messageForm').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('enterForm').style.visibility = connected ? 'visible' : 'hidden';
}

function connect() {
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);
    // 인증 토큰 - 실제 애플리케이션에서는 사용자 인증 후 받은 토큰을 사용해야 합니다.
    var token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtcjYyMDhAbmF2ZXIuY29tIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwOTgzMDU0Nn0.jLe1ek6d-VZXyo0MtMQ0qc0masp0W1lGYGsxRo_qtR3SOpdqYdLMTsWjKcbfvPMnDN_jegWf3F4ubhYqswVODQ"; // 예: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'

    stompClient.connect({"Authorization": "Bearer " + token}, function(frame) {
        // 연결 성공 시의 처리...
        stompClient.subscribe('/sub/chat/room/' + roomId, function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    }, function(error) {
        // 연결 실패 시의 처리...
        alert('Connection failed: ' + error);
    });

    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/sub/chat/room/' + roomId, function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
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

function enterRoom() {
    roomId = document.getElementById('room').value;
    if (roomId) {
        document.getElementById('chat').innerHTML = '';
        connect(); // Connect when entering a room
    }
}

function sendMessage() {
    var messageContent = document.getElementById('message').value;
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: "Username", // Change this to the actual sender's name
            content: messageContent,
            type: 'CHAT',
            chattingroom_id: roomId
        };
        stompClient.send("/pub/chat/message", {}, JSON.stringify(chatMessage));
        document.getElementById('message').value = '';
    }
}

function showMessageOutput(messageOutput) {
    var response = document.getElementById('chat');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(messageOutput.sender + ": " + messageOutput.content));
    response.appendChild(p);
}
