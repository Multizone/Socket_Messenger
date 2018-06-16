package sources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import messages.ClientRequest;
import messages.Dialog;
import messages.Message;
import messages.ServerAnswer;
import messages.StatusMessage;
import messages.TextMessage;

public class ServerLogic extends Thread {
	
	private static ServerForm form;
	private static final int PORT = 6789;
	private static ServerSocket serverSocket;
	private static Socket socket;
	private static BufferedReader in;
	private static PrintWriter out;
	private static ArrayList<String> Users;
	private static Map <Set<String>, Dialog> dialogs;
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static void main(String... args) throws IOException {
		form = new ServerForm();
		form.setVisible(true);
		
		new ServerLogic();
	}
	
	public ServerLogic() throws IOException{
		Users = new ArrayList<String>();
		dialogs = new HashMap<Set<String>, Dialog>();
		serverSocket = new ServerSocket(PORT);
		Listening();
	}
	
	public void Listening() throws IOException {
		
		while(true) {
			socket = serverSocket.accept();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			
			String str = readFromJson(in);
			if (str.startsWith("{")) {
				System.out.println(str);
				
				if(getMsgAction(str).equals("textMessage")) {
					handleTextMsg(str);
				}else if(getMsgAction(str).equals("satusMsg")) {
					handleStatusMsg(str);
				}else if(getMsgAction(str).equals("serverRequest")) {
					out.write(GSON.toJson(handleClientRequest(str)));
					System.out.println(handleClientRequest(str));
					out.flush();
				}
				out.close();
				socket.close();
			}
		}
	}
	
	public String readFromJson(BufferedReader in) throws IOException{
		String buf = null;
		StringBuilder rawJson = new StringBuilder();
		while((buf = in.readLine()) != null) {
			rawJson.append(buf);
		}
		buf = rawJson.toString();
		return buf;
	}
	
	public String getMsgAction(String str) {
		Message m = GSON.fromJson(str, Message.class);
		return m.getAction();
	}
	
	public void handleTextMsg(String msg) {
		TextMessage tm = GSON.fromJson(msg, TextMessage.class);
		Set<String> bufKey = new TreeSet<String>();
		bufKey.add(tm.getSender());
		bufKey.add(tm.getRecipient());
		
		if(!(dialogs.containsKey(bufKey))) {
			dialogs.put(bufKey, new Dialog(tm.getSender(), tm.getRecipient()));
			dialogs.get(bufKey).addMsg(tm.getSender() + ": " + tm.getText());
		}else {
			dialogs.get(bufKey).addMsg(tm.getSender() + ": " + tm.getText());
		}
		System.out.println(dialogs.get(bufKey).getLastMsg());
		form.displayMessage(tm.getSender() + ": " + tm.getText());
	}
	
	public String handleClientRequest(String msg) {
		ClientRequest cr = GSON.fromJson(msg, ClientRequest.class);
		ServerAnswer sa;
		String json = null;
		
		if(cr.getRequest().equals("getUserList")) {
			sa = new ServerAnswer(Users);
			json = GSON.toJson(sa);
		}else if(cr.getRequest().equals("getDialog")) {
			if (cr.getOpponent().equals(null))
				return "";
			
			Set<String> bufKey = new TreeSet<String>();
			bufKey.add(cr.getUserName());
			bufKey.add(cr.getOpponent());
			if(!(dialogs.containsKey(bufKey))) {
				dialogs.put(bufKey, new Dialog(cr.getUserName(), cr.getOpponent()));
			}else {
				sa = new ServerAnswer(dialogs.get(bufKey));
				json = GSON.toJson(sa);
			}
		}
		return json;
	}
	
	public void handleStatusMsg(String msg) {
		StatusMessage sm = GSON.fromJson(msg, StatusMessage.class);

		if(sm.getOperation().equals("addition")) {
			Users.add(sm.getUserName());
		}else if(sm.getOperation().equals("removing")) {
			Users.remove(sm.getUserName());
		}
		form.updateUsers(Users);
	}
}
