package app.models;
//https://learningprogramming.net/java/spring-mvc/build-chat-room-with-websocket-in-spring-mvc-framework/
public class HelloMessage {

	private String name;

	public HelloMessage(String name) {
		super();
		this.name = name;
	}

	public HelloMessage() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
