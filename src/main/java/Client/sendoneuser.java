package Client;

import GUI.Login;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static Client.Chat.myinf;
import static GUI.Login.infarea;
import static Regular.reguler.method3;
import static thesendinf.sendinf.sendstrone;

public class sendoneuser extends Thread {
    DatagramSocket Client;
    TextArea area;
    String inf;
    ObservableList<Object> getdata;
    ListView<Object> getlist;

    CopyOnWriteArrayList<String> mess;

    public sendoneuser(DatagramSocket client, TextArea area, String inf, ObservableList<Object> getdata, ListView<Object> getlist, CopyOnWriteArrayList<String> mess) {
        Client = client;
        this.area = area;
        this.inf = inf;
        this.getdata = getdata;
        this.getlist = getlist;
        this.mess = mess;
    }

    @Override
    public void run() {
        String[] infs = inf.split("//");
        String input = area.getText();
        if(!Objects.equals(input, ""))
        {
            String theip = infs[0].replace("/","");
            SocketAddress address = new InetSocketAddress(theip, Integer.parseInt(infs[1]));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = new Date(); // 获取当前时间
            String format = sdf.format(time);// 格式化时间
            Platform.runLater(()->{
                if(input.getBytes(StandardCharsets.UTF_8).length>=800)
                {
                    infarea.appendText("消息过长,请分段发送");
                }
                //更新JavaFX的主线程的代码放在此处
                else if(!input.equals("")){
                    if(mess.size()==0) {
                        mess.add(input);
                        sendstrone(address, Client, input, myinf());
                        getdata.add(Login.ID + ":" + format);
                        TextArea print = new TextArea();
                        print.setText(input);
                        print.setWrapText(true);
                        print.setEditable(false);
                        print.setPrefSize(500, 100);
                        print.setStyle("-fx-font-size: 18 ;-fx-font-weight:bold");
                        getdata.add(print);
                        getlist.setItems(getdata);
                        area.setText("");
                        method3("message\\" + inf.split("//")[2] + "\\" + "allmessage.txt", Login.ID + ":" + format + "\n" + input);
                    }
                    else {
                        for (String s:mess)
                        {
                            sendstrone(address,Client,s,myinf());
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        mess.add(input);
                        sendstrone(address, Client, input, myinf());
                        getdata.add(Login.ID + ":" + format);
                        TextArea print = new TextArea();
                        print.setText(input);
                        print.setWrapText(true);
                        print.setEditable(false);
                        print.setPrefSize(500, 100);
                        print.setStyle("-fx-font-size: 18 ;-fx-font-weight:bold");
                        getdata.add(print);
                        getlist.setItems(getdata);
                        area.setText("");
                        method3("message\\" + inf.split("//")[2] + "\\" + "allmessage.txt", Login.ID + ":" + format + "\n" + input);
                    }
                }
            });

        }
//             System.gc();

    }
}
