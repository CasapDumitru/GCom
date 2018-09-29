package ro.utcluj.gcom.controller;

import org.apache.commons.lang3.StringUtils;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import ro.utcluj.gcom.model.ChatGroup;
import ro.utcluj.gcom.model.Reply;
import ro.utcluj.gcom.service.ClientService;
import ro.utcluj.gcom.view.GComChatView;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GComChatController extends ReceiverAdapter {
    private JChannel channel;
    private GComChatView gComChatView;
    private ChatGroup chatGroup;
    private String adminGroup;
    private String username;

    public GComChatController(GComChatView gComChatView, ChatGroup chatGroup, String usernameField) {
        this.gComChatView = gComChatView;
        this.chatGroup = chatGroup;
        this.username = usernameField;
        this.adminGroup = chatGroup.getCreator();
        this.gComChatView.addSendMsgButtonListener(new SendMsgButtonListener());
        this.gComChatView.addDeleteGroupButtonListener(new DeleteGroupButtonListener());
        this.gComChatView.addLeaveGroupButtonListener(new LeaveGroupButtonListener());
        this.gComChatView.addKeyPressedListener(new KeyPreesedListener());
        this.gComChatView.addMouseClickedListener(new MouseClickedListener());
        gComChatView.setAdminNameLabel(adminGroup);
        gComChatView.setGroupNameLabel(" \"" + chatGroup.getGroupName() + "\"");
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");
        channel = new JChannel();
        channel.setName(username);
        channel.setReceiver(this);
        channel.connect(chatGroup.getGroupName());
        updateOnlineUsers();
        showMessagesHistory();

        if (username.equals(adminGroup)) {
            gComChatView.getDeleteGroup().setVisible(true);
        } else {
            gComChatView.getDeleteGroup().setVisible(false);
        }
    }

    @Override
    public void viewAccepted(View newView) {
        updateOnlineUsers();
    }

    public void receive(Message msg) {
        if (msg.getSrc().toString().equals(username)) {
            return;
        }

        Timestamp time = new Timestamp(System.currentTimeMillis());
        gComChatView.getChatBox().append("* " + msg.getSrc() + " *: ~" + time.toString().substring(0, 19) + "~ " + msg.getObject() + "\n");
    }

    private void updateOnlineUsers() {
        gComChatView.setUsersLabel(channel.getView().getMembers().stream().map(m -> m.toString()).collect(Collectors.joining(", ")));
    }

    private void checkIfDeletedGroup() throws Exception {
        List<ChatGroup> groups = ClientService.getGroupService().getAllGroups();
        boolean isDeleted = !groups.stream().anyMatch(g -> g.getGroupName().equals(chatGroup.getGroupName()));

        if (isDeleted) {
            new Inner();
        }
    }

    private void showMessagesHistory() {
        List<Reply> messages;
        try {
            messages = ClientService.getGroupService().getAllMessages(chatGroup.getGroupName());
        } catch (RemoteException e) {
            gComChatView.setBottomLabel("Not able to get message history!!");
            return;
        }

        String messagesStr = messages.stream().map(m -> {
            String message = "";
            if (m.getSender().equals(username)) {
                message += "* ME *: ~";
            } else {
                message += "* " + m.getSender() + " *: ~";
            }

            message += m.getDate().toString();
            message += "~ ";
            message += m.getText();
            return message;
        }).collect(Collectors.joining("\n"));
        if (StringUtils.isNotEmpty(messagesStr)) {
            gComChatView.getChatBox().append(messagesStr + "\n");
        }
    }

    private class SendMsgButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = gComChatView.getMessageBox();
            Message msg = new Message(null, null, message);

            try {
                channel.send(msg);
            } catch (Exception e1) {
                e1.printStackTrace();
                gComChatView.setBottomLabel("The message was not sent successfully!");
            }

            Reply messageDB = new Reply(message, username, null, chatGroup.getGroupName());
            try {
                ClientService.getGroupService().addMessage(messageDB);
            } catch (RemoteException e1) {
                System.out.println("The message was not saved in DB!");
            }

            Timestamp time = new Timestamp(System.currentTimeMillis());
            gComChatView.getChatBox().append("* ME" + " *: ~" + time.toString().substring(0, 19) + "~ " + message + "\n");
            gComChatView.setMessageBox("");
        }
    }

    private class DeleteGroupButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ClientService.getGroupService().deleteGroup(chatGroup);
                channel.close();
            } catch (RemoteException e1) {
                gComChatView.setBottomLabel("Error while deleting the group!");
                return;
            }
            gComChatView.dispose();
        }
    }

    private class LeaveGroupButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            channel.close();
            gComChatView.dispose();
        }
    }

    private class Inner extends Thread {
        Inner() {
            start();
        }

        public void run() {
            for (int i = 5; i > 0; i--) {
                gComChatView.setBottomLabel("The group chat was deleted!" + "\n" + " You'll be disconnected in " + i);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gComChatView.dispose();
        }
    }

    private class KeyPreesedListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            try {
                checkIfDeletedGroup();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private class MouseClickedListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                checkIfDeletedGroup();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
