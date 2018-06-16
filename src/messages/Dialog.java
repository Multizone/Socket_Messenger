package messages;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Dialog {

	private ArrayList<String> dHistory = new ArrayList<String>();
	private static Set<String> dName = new TreeSet(); 
	
	public Dialog(String User1, String User2) {
		dName.add(User1);
		dName.add(User2);
	}
	
	public static Set<String> getDialogName() {
		return dName;
	}
	
	public ArrayList<String> getDialogHistory(){
		return dHistory;
	}
	
	public String getLastMsg() {
		return dHistory.get(dHistory.size() - 1);
	}
	
	public void addMsg(String msg) {
		dHistory.add(msg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dName == null) ? 0 : dName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dialog other = (Dialog) obj;
		if (dName == null) {
			if (other.dName != null)
				return false;
		} else if (!dName.equals(other.dName))
			return false;
		return true;
	}
}