package messages;

import messages.Message;

public class TextMessage extends Message{

	private String sender;
	private String recipient;
	private String text;
	private int id;
	
	public TextMessage(String s, String r, String t){
		this.action = "textMessage";
		this.sender = s;
		this.recipient = r;
		this.text = t;
		this.id = 1;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getRecipient() {
		return recipient;
	}
	
	public String getText() {
		return text;
	}
	
	public void idInc() {
		this.id++;
	}
	
	public int getMsgId() {
		return id;
	}
}
