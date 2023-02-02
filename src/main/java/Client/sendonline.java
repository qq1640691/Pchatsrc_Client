package Client;

import java.io.IOException;
import java.net.DatagramSocket;

import static natserver.natserver.serverinf;

/**
 * 向服务器发送在线的线程
 */
public class sendonline extends Thread {
    String ip;
    int port;
    String id;
    DatagramSocket Client;

    public sendonline(String ip, int port, String id, DatagramSocket client) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.Client = client;
    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                serverinf(Client, ip, port, id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//             System.gc();

        }
    }
}
