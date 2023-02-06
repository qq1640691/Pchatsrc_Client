package Client;

import javafx.scene.control.TextArea;

import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentHashMap;

import static Client.Chat.dealpacketfile;
import static GUI.Stage.filebyte;

/**
 * 接收数据的线程
 */
public class receivefile extends Thread {
    DatagramSocket Client;//绑定的ip和端口
//    String title;

    TextArea area;
    ConcurrentHashMap<String, String> thefilepath;

    public receivefile(DatagramSocket client,TextArea area, ConcurrentHashMap<String, String> thefilepath) {
        Client = client;
        this.area = area;
        this.thefilepath = thefilepath;
    }

    @Override
    public synchronized void run() {
        while (true)
        {
            for (byte[] result : filebyte) {
                try {
                    dealpacketfile(result, Client, area,thefilepath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                filebyte.remove(result);
            }
        }
    }
}