package ganji;

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
	
	// the list of contacts that are belonging to this user
    // the parent variable of the Contact class will map the
    // contact to the ContactUser.
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

}
