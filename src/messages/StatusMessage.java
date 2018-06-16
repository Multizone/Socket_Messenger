package messages;

public class StatusMessage extends Message {

	private String UserName;
	private String operation;
	
	public StatusMessage(String name, String oper) {
		this.UserName = name;
		this.operation = oper;
		this.action = "satusMsg";
	}
	
	public String getUserName() {
		return UserName;
	}
	
	public String getOperation() {
		return operation;
	}
}