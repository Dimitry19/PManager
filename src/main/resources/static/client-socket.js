var Client;
Client=function () {

	function loadClient() {
		jQuery(function ($) {
			let stompClient;

			$("#js-connect").click(function () {
				if (!stompClient) {
					console.log('Try to connect ');

					//const socket = new SockJS("http://localhost:8080/pmanager/notifications");
					const socket = new SockJS("http://localhost:8080/pmanager/notifications");
					stompClient = Stomp.over(socket);
					stompClient.connect({}, function () {

						stompClient.subscribe('/user/notification/item', function (response) {
							//console.log('Got ' + response);
							console.log('Got ' + response.body);
							$("#js-notifications").append(JSON.parse(response.body).content + ' ')
						});

						console.info('connected!')
					});
				}
			});

			$('#js-disconnect').click(function () {
				if (stompClient) {
					stompClient.disconnect();
					stompClient = null;
					console.info("disconnected :-/");
				}
			});

			$("#js-start").click(function () {
				if (stompClient) {
					stompClient.send("/swns/start", {});
				}
			});

			$("#js-stop").click(function () {
				if (stompClient) {
					stompClient.send("/swns/stop", {});
				}
			});
		});

	}

	return {
		loadClient:function(){
			loadClient();
		}
	}
}();