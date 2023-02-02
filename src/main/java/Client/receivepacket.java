package Client;

import Code.AES;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static GUI.Login.*;

public class receivepacket extends Thread{
    DatagramSocket Client;
    public receivepacket(DatagramSocket client) {
        Client = client;
    }

    @Override
    public synchronized void run() {
        while(true)
        {
            byte[] get = new byte[1024];
            DatagramPacket packet = new DatagramPacket(get, get.length);
            try {
                Client.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] result = new byte[packet.getLength()];
            System.arraycopy(packet.getData(),0,result,0,result.length);
            try {
                byte[] bytes = AES.decrypt(result, KEY);
                String head = new String(bytes, 0, 2);
                switch (head)
                {
                    case "fi":
                        filebyte.add(bytes);
                        break;
                    case "de":
                        delaybyte.add(bytes);
                    default:
                        allbyte.add(bytes);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
