package messages;

public class ClientRequest extends Message {

	private String opponent;
	private String userName;
	private String request;
	
	public ClientRequest() {
		this.action = "serverRequest";
	}
	
	public ClientRequest(String ResponseType, String UserName) {
		this.action = "serverRequest";
		this.request = ResponseType;
		this.userName = UserName;
	}
	
	public ClientRequest (String ResponseType, String UserName, String opponent) {
		this.action = "serverRequest";
		this.request = ResponseType;
		this.userName = UserName;
		this.opponent = opponent;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getRequest(){
		return this.request;
	}
	
	public String getOpponent() {
		return this.opponent;
	}
}
