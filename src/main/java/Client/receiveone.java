package Client;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.DatagramSocket;
import java.util.concurrent.CopyOnWriteArrayList;

import static Client.Chat.dealpacketone;
import static GUI.Login.allbyte;
import static GUI.Login.chat;

/**
 * 接收数据的线程
 */
public class receiveone extends Thread {
    DatagramSocket Client;//绑定的ip和端口
    String title;
    ObservableList<Object> getdata;
    ListView<Object> getlist;

    CopyOnWriteArrayList<String> mess;

    public receiveone(DatagramSocket client, String title, ObservableList<Object> getdata, ListView<Object> getlist, CopyOnWriteArrayList<String> mess) {
        Client = client;
        this.title = title;
        this.getdata = getdata;
        this.getlist = getlist;
        this.mess = mess;
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
                    if (object.contains(title) && !object.contains("list/") && !object.contains("mesl/")) {
                        try {
                            dealpacketone(Client,result,title, getdata, getlist,mess);
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