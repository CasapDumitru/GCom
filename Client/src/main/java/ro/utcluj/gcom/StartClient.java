package ro.utcluj.gcom;

import org.apache.commons.logging.LogFactory;
import ro.utcluj.gcom.controller.GComChatController;
import ro.utcluj.gcom.controller.GComController;
import ro.utcluj.gcom.view.GComChatView;
import ro.utcluj.gcom.view.GComMainView;

public class StartClient {
    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(StartClient.class);

    public static void main(String[] args) {
        LOG.info("Client started!");
//        new GComChatController(new GComChatView());
        new GComController(new GComMainView());
    }
}
