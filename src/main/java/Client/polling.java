package Client;

import Code.AES;
import GUI.Stage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static Client.Chat.*;
import static Client.Chat.myinf;
import static GUI.Stage.*;
import static GUI.Stage.filetopart;
import static natserver.natserver.serverinf;
import static natserver.ping.sendthedelay;

public class polling {

    DatagramSocket Client;
    TextArea area;

    String ip;
    int port;
    String id;
    ConcurrentHashMap<String, String> thefilepath;
    ObservableList<Object> infdata;


    public polling(DatagramSocket client, TextArea area, String ip, int port, String id, ConcurrentHashMap<String, String> thefilepath, ObservableList<Object> infdata) {
        Client = client;
        this.area = area;
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.thefilepath = thefilepath;
        this.infdata = infdata;
    }

    public void poll() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(9);

        service.scheduleWithFixedDelay(()->{
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
        },0,100, TimeUnit.MILLISECONDS);

        service.scheduleWithFixedDelay(() -> {
            for (byte[] result : filebyte) {
                try {
                    dealpacketfile(result, Client, area,thefilepath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                filebyte.remove(result);
            }
        }, 10000, 5, TimeUnit.MILLISECONDS);

        service.scheduleWithFixedDelay(() -> {
            for (byte[] result : delaybyte) {
                assert result != null;
                try {
                    receivedelay(result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                delaybyte.remove(result);
            }
//             sleep(10);
        }, 10, 20, TimeUnit.SECONDS);

        service.scheduleWithFixedDelay(() ->
        {
            if (fileinf.size() > 0) {
                Set<String> keyset = fileinf.keySet();
                for (String file : keyset) {
                    /**
                     * 已经过去的时间大于文件传输所需要的时间,就要求重传.
                     */
                    if (fileinf.containsKey(file)) {
                        String[] inf = fileinf.get(file).split("//");
                        double delays;
                        try {
                            delays = Double.parseDouble(sendthedelay(inf[1]));
                        } catch (Exception e) {
                            delays = 15;
                        }
                        long timed = (long) (4L * Double.parseDouble(inf[6]) / (double) 1000 * delays);
                        if (System.currentTimeMillis() - Long.parseLong(inf[9]) > timed * 1000 && filetopart.get(file).size() > 0) {
                            Platform.runLater(() -> {
                                infarea.appendText(file + "还剩下" + filetopart.get(file).size() + "块,需要重传\n");
                            });
                            try {
                                getlostfile(inf, Client);
                                Thread.sleep((long) (filetopart.get(file).size() / (double) 1000 * delays));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
//            sleep(20);
        }, 10, 30, TimeUnit.SECONDS);

        service.scheduleWithFixedDelay(() ->
        {
            if (userdelay.size() == 0) {
                area.appendText("正在与其他用户连接中,请耐心等待\n");
            }
            if (userdelay.size() > 0) {
                area.appendText("已链接用户:\n");
                area.appendText(userdelay + "\n");
            }
        }, 60, 30, TimeUnit.SECONDS);

        service.scheduleWithFixedDelay(() ->
        {
            DecimalFormat df = new DecimalFormat("0.00");
            if (filetopart.size() > 0) {
                Set<String> key = filetopart.keySet();
                for (String str : key) {
                    double dtime = (double) filetopart.get(str).size() / (double) 50;
                    if (dtime != 0) {
                        String time = "正在接收" + str + ",预计时间:" + df.format(dtime) + "秒";
                        Platform.runLater(() -> {
                            infarea.appendText(time + "\n");
                        });
                    }
                }
            }
//            sleep(15);
        }, 0, 20, TimeUnit.SECONDS);

        service.scheduleWithFixedDelay(() ->
        {
            if (userlist.size() > 1) {
                for (String str : userlist) {
                    if (!Objects.equals(myinf(), str)) {
                        String[] inf = str.split("//");
                        byte[] send = ("dely/" + myinf() + "//").getBytes(StandardCharsets.UTF_8);
                        byte[] sendbuf;
                        try {
                            sendbuf = AES.encrypt(send, Stage.KEY);
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
            }
//            sleep(30);
        }, 10, 30, TimeUnit.SECONDS);

        service.scheduleWithFixedDelay(() ->
        {
            for (String s : userlist) {
                long now = System.currentTimeMillis();
                if (now - userzx.get(s) > 180000L) {
                    userlist.remove(s);
                    userzx.remove(s);
                    userdelay.remove(s.split("//")[2]);
                    Platform.runLater(() -> {
                        infarea.appendText("用户" + s.split("//")[2] + "下线,移除用户\n");
                        listView.getItems().clear();
                        data.addAll(userlist);
                        listView.setItems(data);
                    });
                }
            }
//            sleep(50);
        }, 10, 60, TimeUnit.SECONDS);

        service.scheduleWithFixedDelay(()->{
            try {
                serverinf(Client, ip, port, id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        },0,60,TimeUnit.SECONDS);
    }
}