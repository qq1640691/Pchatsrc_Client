package GUI;

import Client.receivepacket;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.DatagramSocket;

import static GUI.Stage.login;
import static natserver.natserver.socket;

public class Main extends Application {

    public static void main(String[] args) {
        Main.launch(Main.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        DatagramSocket Client = socket();
        Thread receivepacket = new receivepacket(Client);
        receivepacket.start();
        login(Client);
    }
}
