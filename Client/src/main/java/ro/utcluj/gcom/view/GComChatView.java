package ro.utcluj.gcom.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GComChatView extends JFrame {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel centerPanel;
    private JPanel centerRightPanel;
    private JPanel centerLeftPanel;
    private JButton leaveButton;
    private JButton deleteGroup;
    private JPanel viewUsersPanel;
    private JLabel adminLabel;
    private JLabel adminNameLabel;
    private JLabel bottomLabel;
    private JLabel usersLabel;
    private JLabel topLabel;
    private JLabel groupNameLabel;
    private JList groupList;
    private JTextField messageBox;
    private JButton sendMessage;
    private JTextArea chatBox;

    public GComChatView() {
        super("Group Communication");
        centerPanel.setPreferredSize(new Dimension(1200, 700));
        topPanel.setPreferredSize(new Dimension(1200, 50));
        bottomPanel.setPreferredSize(new Dimension(1200, 50));
        centerRightPanel.setPreferredSize(new Dimension(500, 700));
        centerLeftPanel.setPreferredSize(new Dimension(700, 700));

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 20));
        chatBox.setLineWrap(true);

        centerLeftPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        centerLeftPanel.add(BorderLayout.SOUTH, southPanel);

        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setVisible(true);

        this.getRootPane().setDefaultButton(sendMessage);
    }

    public void addSendMsgButtonListener(ActionListener listener) {
        sendMessage.addActionListener(listener);
    }

    public void addLeaveGroupButtonListener(ActionListener listener) {
        leaveButton.addActionListener(listener);
    }

    public void addDeleteGroupButtonListener(ActionListener listener) {
        deleteGroup.addActionListener(listener);
    }

    public JTextArea getChatBox() {
        return chatBox;
    }

    public void setChatBox(JTextArea chatBox) {
        this.chatBox = chatBox;
    }

    public String getMessageBox() {
        return messageBox.getText();
    }

    public void setMessageBox(String messageBox) {
        this.messageBox.setText(messageBox);
    }

    public void setBottomLabel(String bottomLabel) {
        this.bottomLabel.setText(bottomLabel);
    }

    public void setAdminNameLabel(String adminNameLabel) {
        this.adminNameLabel.setText(adminNameLabel);
    }

    public void setUsersLabel(String usersLabel) {
        this.usersLabel.setText(usersLabel);
    }

    public JButton getDeleteGroup() {
        return deleteGroup;
    }

    public void setGroupNameLabel(String groupNameLabel) {
        this.groupNameLabel.setText(groupNameLabel);
    }

    public String getGroupNameLabel() {
        return groupNameLabel.getText();
    }

    public void addKeyPressedListener(KeyListener listener) {
        messageBox.addKeyListener(listener);
    }

    public void addMouseClickedListener(MouseListener listener) {
        messageBox.addMouseListener(listener);
        sendMessage.addMouseListener(listener);
        mainPanel.addMouseListener(listener);
        centerPanel.addMouseListener(listener);
        centerLeftPanel.addMouseListener(listener);
        centerRightPanel.addMouseListener(listener);
        chatBox.addMouseListener(listener);
    }

}
