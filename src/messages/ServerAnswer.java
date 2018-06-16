package messages;

import java.util.ArrayList;

public class ServerAnswer extends Message {
	
	private ArrayList<String> answerList;
	private Dialog dialog;

	
	public ServerAnswer(Dialog d){
		this.action = "serverResponse";
		this.dialog = d;
	}

	public ServerAnswer(ArrayList<String> list) {
		this.action = "serverResponse";
		this.answerList = list;
	}

	public ArrayList<String> getList(){
		return this.answerList;
	}
	
	public Dialog getDialog() {
		return this.dialog;
	}
}
