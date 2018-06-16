package sources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import messages.ClientRequest;
import messages.Dialog;
import messages.Message;
import messages.ServerAnswer;
import messages.StatusMessage;
import messages.TextMessage;

public class ClientLogic extends Thread {
	
	public static ReentrantLock lock;
	public static String UserName;
	
	private static ClientForm form;
	private static String host = null;
	private static int PORT = 6789;
	private static BufferedReader in;
	private static PrintWriter out;
	private static Socket socket;
	private static Authorization Auth;
	private static ArrayList <String> ActiveUsers;
	private static Dialog dialog;
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	

	public static void main(String[] args) throws IOException{
		Auth = new Authorization();
		Auth.setVisible(true);
		getInfo();
		ActiveUsers = new ArrayList <String>();
		
		new ClientLogic();
	}
	
	public ClientLogic() throws IOException{
		sendClientStatus("addition");
		form = new ClientForm();
		form.setVisible(true);
		form.displayUserName(UserName);
		form.updateUsers(ActiveUsers);
		start();
	}
	
	public void run() {
		while(true) {
			if(socket.isClosed()) {
				try {
					getUserList();
					getDialogHistory();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					try {
						disconnect();
						Thread.sleep(1000);
					} catch (InterruptedException e) { form.displayMessage("Smth is messed up! \n"); }
				}
			}
		}
	}
	
	public static void getInfo() {
		while(UserName == null || host == null) {
			host = Auth.getServerName();
			UserName = Auth.getUserName();
		}
		Auth.end();
	}
	
	public static void getUserList() throws IOException {
		if(socket.isClosed())
			connect();
		ClientRequest cr = new ClientRequest("getUserList", UserName);
		out.write(GSON.toJson(cr));
		out.flush();
		out.close();
		
		if(socket.isConnected()) {
			String str = readFromJson(in);
			if (str.startsWith("{") && getMsgAction(str).equals("serverResponse")) {
				ServerAnswer sa = GSON.fromJson(str, ServerAnswer.class);
				ActiveUsers.clear();
				ActiveUsers = (ArrayList<String>) sa.getList().clone();
				form.updateUsers(ActiveUsers);
				
			}
		}
	}
	
	public static void getDialogHistory() throws IOException {
		if(socket.isClosed())
			connect();
		ClientRequest cr = new ClientRequest("getDialog", UserName, form.getRecipient());
		out.write(GSON.toJson(cr));
		out.flush();
		out.close();
		
		if(socket.isConnected()) {
			String str = readFromJson(in);
			ServerAnswer sa = GSON.fromJson(str, ServerAnswer.class);
			dialog = sa.getDialog();
			form.textArea.setText("");
			for(int i = 0; i < dialog.getDialogHistory().size(); i++) {
				form.textArea.append(dialog.getDialogHistory().get(i) + "\n");
			}
		}
	}
	
	public static void sendClientStatus(String statusOperation) throws IOException {
		StatusMessage sm = new StatusMessage(UserName, statusOperation);
		connect();
		out.write(GSON.toJson(sm));
		out.flush();
		disconnect();
	}
	
	public static void connect() throws IOException {
		socket = new Socket(host, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));  
	    System.out.println("Connected to the server! " + host + "PORT: " + PORT);
	}
	
	public static void disconnect() {
		try { socket.close(); } 
		catch (IOException e) {}
	}
	
	public static void sendMessage(String msg) throws IOException {
		if (msg == null)
			return;
		if (socket == null || socket.isClosed())
			connect();
		form.displayMessage(msg);
		TextMessage tm = new TextMessage(UserName, form.getRecipient(), msg);
		String json = GSON.toJson(tm);
		System.out.println(json);
		out.write(json);
		out.flush();
		disconnect();
	}
	
	public static String readFromJson(BufferedReader in) throws IOException{
		String buf;
		StringBuilder rawJson = new StringBuilder();
		while((buf = in.readLine()) != null) {
			rawJson.append(buf).append("\n");
			if (buf.isEmpty())
				break;
		}
		return rawJson.toString();
	}
	
	public static String getMsgAction(String str) {
		Message m = GSON.fromJson(str, Message.class);
		return m.getAction();
	}
}
