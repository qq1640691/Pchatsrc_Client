package Client;

import GUI.Stage;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Client.Chat.myinf;
import static GUI.Stage.*;
import static Regular.reguler.method3;
import static thesendinf.sendinf.sendstr;

/**
 * 发送消息的线程,重写一下线程池,变成广播消息.
 */
public class sendmessageall extends Thread {
    DatagramSocket Client;
    CopyOnWriteArrayList<String> userlist;
    TextArea area;

    public sendmessageall(DatagramSocket client, CopyOnWriteArrayList<String> userlist, TextArea area) {
        Client = client;
        this.userlist = userlist;
        this.area = area;
    }

    @Override
    public void run() {
        String input = area.getText();
        if (!Objects.equals(input, "")) {
            ExecutorService pool = Executors.newFixedThreadPool(100);
            if(input.getBytes(StandardCharsets.UTF_8).length>=800)
                {
                    infarea.appendText("消息过长,请分段发送");
                }
            else {
                for (String use : userlist) {
                    pool.submit(() -> {
                        if (!Objects.equals(use, myinf())) {
                            String[] oneuser = use.split("//");
                            SocketAddress address = new InetSocketAddress(oneuser[0].replace("/", ""), Integer.parseInt(oneuser[1]));
                            sendstr(address, Client, input, myinf());
                        }
                    });
                }
            }
            SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date timed = new Date(); // 获取当前时间
            String formattime = sdfd.format(timed);// 格式化时间
            Platform.runLater(() -> {
                //更新JavaFX的主线程的代码放在此处
                if (!input.equals("")&&input.getBytes(StandardCharsets.UTF_8).length<800) {
                    getdata.add(Stage.ID + ":" + formattime);
                    Chat.printmessage(getdata, input, getlist);
                    area.setText("");
                    method3("allmessage\\allmessage.txt", Stage.ID + ":" + formattime+"\n"+input);
                }
            });
        }
    }
}
