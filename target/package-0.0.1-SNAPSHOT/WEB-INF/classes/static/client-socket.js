/****
 *  http://jmesnil.net/stomp-websocket/doc/
 */
var Client;
Client=function () {

	function loadClient() {
		jQuery(function ($) {
			var stompClient;

			var headers = {
				//login: 'mylogin',
				//passcode: 'mypasscode',
				// additional header
				'client-id': 'my-client-id',
				'user':'Dimitri'
			};

			$("#js-connect").click(function () {
				if (!stompClient) {
					console.log('Try to connect ');

					//const socket = new SockJS("http://localhost:8080/pmanager/notifications");
					var socket = new SockJS("http://localhost:8080/pmanager/notifications");
					stompClient = Stomp.over(socket);
					stompClient.connect(headers, connect_success_callback,connect_error_callback);
				}
			});

			$('#js-disconnect').click(function () {
				if (stompClient) {
					stompClient.disconnect(headers);
					stompClient = null;
					console.info("disconnected :-/");
				}
			});

			$("#js-start").click(function () {
				if (stompClient) {
					stompClient.send("/pm-swns/start", {});
				}
			});


			//SUBSCRIPTION
			$("#js-subscriber").click(function () {
				if (stompClient) {

					//var subscription = client.subscribe(destination, callback, { id: mysubid });
					var headers = {ack: 'client', 'selector': "location = 'Europe'"};
					//   client.subscribe("/queue/test", message_callback, headers);
					stompClient.subscribe('/user/notification/item', subscribe_callback);
					stompClient.subscribe('/user/notification/announce', subscribe_callback);
				}
			});

			$("#js-announce-start").click(function () {
				if (stompClient) {
					stompClient.send("/pm-swns/notification/announce", send_success_callback);
				}
			});
			$("#js-comment-start").click(function () {
				if (stompClient) {
					//client.send(destination, {}, body);   client.send("/queue/test", {priority: 9}, "Hello, STOMP");
					stompClient.send("/pm-swns/comment/start", send_success_callback);
				}
			});

			$("#js-stop").click(function () {
				if (stompClient) {
					stompClient.send("/pm-swns/stop", send_success_callback);
				}
			});
		});

	}


	var subscribe_callback = function(response) {
		// called when the client receives a STOMP message from the server

		console.log('Got ' + response.body);

		if (response.body) {
			console.log("got message with body " + response.body)
			$("#js-notifications").append(JSON.parse(response.body).message + ' ')

		} else {
			console.log("got empty message");
		}
	}

	var connect_success_callback = function() {
		// called back after the client is connected and authenticated to the STOMP server
		console.info('connected!')
	};

	var send_success_callback = function() {
		// called back after the client is connected and authenticated to the STOMP server
		console.info('send!')
	};

	var connect_error_callback = function() {
		// called back after the client is connected and authenticated to the STOMP server
		console.info('connection error!')
	};

	return {
		loadClient:function(){
			loadClient();
		}
	}
}();