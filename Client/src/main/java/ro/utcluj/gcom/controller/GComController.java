package ro.utcluj.gcom.controller;

import org.apache.commons.lang3.StringUtils;
import ro.utcluj.gcom.model.ChatGroup;
import ro.utcluj.gcom.service.ClientService;
import ro.utcluj.gcom.view.GComChatView;
import ro.utcluj.gcom.view.GComMainView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

public class GComController {
    private GComMainView gComMainView;

    public GComController(GComMainView gComMainView) {
        this.gComMainView = gComMainView;
        this.gComMainView.addJoinButtonListener(new JoinButtonListener());
        this.gComMainView.addCreateGroupButtonListener(new CreateGroupButtonListener());
        getAndShowAllGroups();
    }

    private void getAndShowAllGroups() {
        List<ChatGroup> groups;
        try {
             groups = ClientService.getGroupService().getAllGroups();
        } catch (RemoteException e) {
            gComMainView.setBottomLabel("Could not get all the groups available!");
            return;
        }
        gComMainView.getModel().clear();
        groups.forEach(g -> gComMainView.getModel().addElement(g.getGroupName()));
        gComMainView.getGroupList().setModel(gComMainView.getModel());
    }

    private class JoinButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = gComMainView.getIndexSelectedItem();

            String usernameField = gComMainView.getUsernameField();
            String selectedItem = gComMainView.getSelectedItem();

            if (index == -1) {
                gComMainView.setBottomLabel("Please select a group!");
                return;
            } else if (StringUtils.isEmpty(usernameField)) {
                gComMainView.setBottomLabel("Please enter a username!");
                return;
            }

            gComMainView.removeComponents();

            try {
                new GComChatController(new GComChatView(), ClientService.getGroupService().getGroupByName(selectedItem), usernameField);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            gComMainView.setBottomLabel("");
            getAndShowAllGroups();
        }
    }

    private class CreateGroupButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String usernameField = gComMainView.getUsernameField();
            String groupField = gComMainView.getGroupField();

            if (StringUtils.isEmpty(usernameField) || StringUtils.isEmpty(groupField)) {
                gComMainView.setBottomLabel("The username and group name must not be empty!");
                return;
            }

            try {
                ClientService.getGroupService().addGroup(new ChatGroup(groupField, usernameField));
                System.out.println("Group created successfully!");
            } catch (RemoteException e1) {
                gComMainView.setBottomLabel("Error adding the group. Try later again please!");
                return;
            }

            gComMainView.removeComponents();
            try {
                new GComChatController(new GComChatView(), ClientService.getGroupService().getGroupByName(groupField), usernameField);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            gComMainView.setBottomLabel("");
            getAndShowAllGroups();
        }
    }
}
