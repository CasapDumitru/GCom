package ro.utcluj.gcom.view;

import ro.utcluj.gcom.model.ChatGroup;
import ro.utcluj.gcom.model.User;
import ro.utcluj.gcom.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;

public class GComMainView extends JFrame implements MouseListener {
    private Container container;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JPanel centerRightPanel;
    private JPanel centerLeftPanel;
    private JButton createGroupButton;
    private JTextField usernameField;
    private JButton joinButton;
    private JList groupList;
    private JLabel bottomLabel;
    private JTextField groupField;
    private JScrollPane listScroller;
    private DefaultListModel model;

    public GComMainView() {
        super("Group Communication");
        centerPanel.setPreferredSize(new Dimension(1200, 700));
        topPanel.setPreferredSize(new Dimension(1200, 50));
        bottomPanel.setPreferredSize(new Dimension(1200, 50));
        centerRightPanel.setPreferredSize(new Dimension(500, 700));
        centerLeftPanel.setPreferredSize(new Dimension(700, 700));
        this.model = new DefaultListModel();
        mainPanel.addMouseListener(this);

        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addJoinButtonListener(ActionListener listener) {
        joinButton.addActionListener(listener);
    }

    public void addCreateGroupButtonListener(ActionListener listener) {
        createGroupButton.addActionListener(listener);
    }

    public int getIndexSelectedItem() {
        return groupList.getSelectedIndex();
    }

    public void setBottomLabel(String message) {
        this.bottomLabel.setText(message);
    }

    public String getSelectedItem() {
        if (groupList.isSelectionEmpty()) {
            return null;
        }
        return (String) groupList.getSelectedValue();
    }

    public String getUsernameField() {
        return usernameField.getText();
    }

    public String getGroupField() {
        return groupField.getText();
    }

    public DefaultListModel getModel() {
        return model;
    }

    public JList getGroupList() {
        return groupList;
    }

    public void removeComponents() {
        this.remove(this.mainPanel);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        updateGroups();
    }

    private void updateGroups() {
        List<ChatGroup> groups;
        try {
            groups = ClientService.getGroupService().getAllGroups();
        } catch (RemoteException e1) {
            this.setBottomLabel("Could not get all the groups available!");
            return;
        }
        this.getModel().clear();
        groups.forEach(g -> this.getModel().addElement(g.getGroupName()));
        this.getGroupList().setModel(this.getModel());
        System.out.println("Updated!");
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
