package ro.utcluj.gcom.serviceInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import ro.utcluj.gcom.model.ChatGroup;
import ro.utcluj.gcom.model.Reply;

public interface IGroupService extends Remote {
	
	List<Reply> getAllMessages(String groupName) throws RemoteException;
	void addMessage(Reply message) throws RemoteException;
	List<ChatGroup> getAllGroups() throws RemoteException;
	void addGroup(ChatGroup chatGroup) throws RemoteException;
	void deleteGroup(ChatGroup chatGroup) throws RemoteException;
	ChatGroup getGroupByName(String groupName) throws RemoteException;
	
}
