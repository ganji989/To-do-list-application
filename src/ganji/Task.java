package ganji;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable
public class Task {
	
	// the id of the contact as this is not going to be directly
	// retrieved we will allow the keys for this to be auto generated 
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	@Persistent
	String name;
	@Persistent
	String date;
	@Persistent
	boolean checked=false;
	@Persistent
	private User parent;
	
	public Key getId() {
		return id;
	}
	public void setId(Key id) {
		this.id = id;
	}
	public User getParent() {
		return parent;
	}
	public void setParent(User parent) {
		this.parent = parent;
	}
	public Task(String name, String date) {
		this.name = name;
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}	
	public void check(){
		checked=true;
	}
	public void uncheck(){
		checked=false;
	}
	public boolean isChecked(){return checked;}
	public void setStatus(boolean b){checked=b;};
}
