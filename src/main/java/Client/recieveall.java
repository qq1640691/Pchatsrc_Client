package Client;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.net.DatagramSocket;
import java.util.concurrent.CopyOnWriteArrayList;

import static Client.Chat.*;
import static GUI.Login.*;

/**
 * 接收数据的线程
 */
public class recieveall extends Thread {
    DatagramSocket Client;//绑定的ip和端口
    CopyOnWriteArrayList<String> userlist;
//    TextArea area;

    ListView<String> listview;
    ObservableList<String> data;
    ObservableList<Object> infdata;

    public recieveall(DatagramSocket client, CopyOnWriteArrayList<String> userlist, ListView<String> listview, ObservableList<String> data, ObservableList<Object> infdata) {
        Client = client;
        this.userlist = userlist;
        this.listview = listview;
        this.data = data;
        this.infdata = infdata;
    }

    @Override
    public synchronized void run() {
        while (true)
        {
            if(allbyte.size()>0) {
                for (byte[] result : allbyte) {
                    String object;
                    if (result.length >= 256) {
                        object = new String(result, 0, 256);
                    } else {
                        object = new String(result, 0, result.length);
                    }
                    if (object.contains("meso") || object.contains("list") || object.contains("mesl") || object.contains("errr")) {
                        try {
                            dealpacketall(result, userlist, Client, infdata);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        allbyte.remove(result);
                    }
                }
            }
        }
    }
}
