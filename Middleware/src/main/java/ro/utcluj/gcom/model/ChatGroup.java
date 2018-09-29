package ro.utcluj.gcom.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
public class ChatGroup implements Serializable {

	private static final long serialVersionUID=1L;

	@Id
	@Column
	private String groupName;

	@Column
	private String creator;
	
	public ChatGroup(String groupName, String creator) {
		this.groupName = groupName;
		this.creator = creator;
	}

	public ChatGroup() {
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String name) {
		this.groupName = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return "ChatGroup{" +
				"groupName='" + groupName + '\'' +
				'}';
	}
}
