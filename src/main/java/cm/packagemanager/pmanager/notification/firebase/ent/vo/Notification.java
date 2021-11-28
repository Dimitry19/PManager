package cm.packagemanager.pmanager.notification.firebase.ent.vo;

public class Notification {

	private String title;
	private String message;
	private String topic;
	private String token;
	private String url;

	public Notification() {
	}

	public Notification(String title, String messageBody, String topicName, String url) {
		this.title = title;
		this.message = messageBody;
		this.topic = topicName;
		this.url=url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {return url;	}

	public void setUrl(String url) {this.url = url;	}
}
