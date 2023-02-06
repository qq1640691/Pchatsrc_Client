package natserver;

import Code.RSACoder;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static GUI.Stage.KEY;
import static GUI.Stage.RSApublickey;


public class natserver {
    /**
     * 这个函数会返回对方客户端的ip地址
     */
    public static void serverinf(DatagramSocket Client, String serverip, int serverport, String clientid) throws IOException {
        InetAddress ip = InetAddress.getByName(serverip);
        SocketAddress serveraddress = new InetSocketAddress(ip, serverport);//打洞服务器的ip和端口
        byte[] send;
        try {
            send = RSACoder.encryptByPublicKey((KEY+clientid).getBytes(StandardCharsets.UTF_8),RSApublickey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DatagramPacket pack = new DatagramPacket(Objects.requireNonNull(send), send.length, serveraddress);
        Client.send(pack);//把自己的ip地址端口和id号发过去
    }
    public static DatagramSocket socket()
    {
        try {
            return new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
