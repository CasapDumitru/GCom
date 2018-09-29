package ro.utcluj.gcom.service;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import org.jgroups.Message;

import ro.utcluj.gcom.model.ChatGroup;
import ro.utcluj.gcom.model.Reply;
import ro.utcluj.gcom.serviceInterfaces.IGroupService;

public class ClientService {

    public static IGroupService getGroupService() {
        try {
            //Registry registry = LocateRegistry.getRegistry("192.168.43.33", 1099);
//            Registry registry = LocateRegistry.getRegistry("192.168.0.102", 1099);
            Registry registry = LocateRegistry.getRegistry(1099);
            IGroupService groupService = (IGroupService) registry.lookup("IGroupService");
            return groupService;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public void createNewGroup(String groupName, String creator) throws Exception {
        try {
            ChatGroup group = ClientService.getGroupService().getGroupByName(groupName);
            if (group != null) {
                ClientService.getGroupService().addGroup(new ChatGroup(groupName, creator));
            } else {
                throw new Exception("Group name already taken");
            }

        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addMessage(Reply message) {
        try {
            ClientService.getGroupService().addMessage(message);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deleteGroup(ChatGroup group) {
        try {
            ClientService.getGroupService().deleteGroup(group);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
