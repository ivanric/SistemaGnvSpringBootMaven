package app.models;

public class Greeting {
	private String name;

	public Greeting() {
		super();
	}

	public Greeting(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}