package ro.utcluj.gcom.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
public class Reply implements Serializable {

	private static final long serialVersionUID=1L;

	@Column
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY )
	private int id;
	@Column
	private String text;
	@Column
	private String sender;
	@Column
	private Date date;

	@Column
	private String groupName;
	
	public Reply(String text, String sender, Date date, String name) {
		this.text = text;
		this.sender = sender;
		this.date = date;
		this.groupName = name;
	}

	public Reply() {
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getgroupName() {
		return groupName;
	}

	public void setgroupName(String chatGroupName) {
		this.groupName = chatGroupName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
