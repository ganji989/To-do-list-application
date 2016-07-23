package ganji;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class User {
	@Persistent
	private String email;
	
	@PrimaryKey
	@Persistent
	private Key id;
	
	@Persistent(mappedBy="parent")
	//@Persistent(defaultFetchGroup = "true")
	private ArrayList<Task> tasks;

	public User(Key id ,String n){
		email=n;
		this.id=id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Key getId() {
		return id;
	}
	public void setId(Key id) {
		this.id = id;
	}
	
	public void addTask(Task t){
		if (tasks==null)	
			tasks=new ArrayList<Task>();
    	tasks.add(t);
    }
	
	public ArrayList<Task> getTasks(){ return tasks;}

}
