package Client;

import Code.AES;
import GUI.Login;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Client.Chat.*;
import static GUI.Login.*;

public class senddelay extends Thread {
    DatagramSocket Client;

    public senddelay(DatagramSocket client) {
        Client = client;
    }

    @Override
    public void run() {
        while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (userlist.size() > 1) {
                    ExecutorService service = Executors.newFixedThreadPool(userlist.size());
                    service.submit(() -> {
                        for (String str : userlist) {
                            if (!Objects.equals(myinf(), str)) {
                                String[] inf = str.split("//");
                                /**
                                 * 这是我的发送数据包的时间
                                 */
                                byte[] send = ("dely/" + myinf() + "//").getBytes(StandardCharsets.UTF_8);
                                byte[] sendbuf;
                                try {
                                    sendbuf = AES.encrypt(send, Login.KEY);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                String ip = inf[0].replace("/", "");
                                SocketAddress address = new InetSocketAddress(ip, Integer.parseInt(inf[1]));
                                DatagramPacket packet = new DatagramPacket(Objects.requireNonNull(sendbuf), sendbuf.length, address);
                                try {
                                    Client.send(packet);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }
    }
