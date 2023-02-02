package Client;

import com.sun.javafx.scene.control.skin.IntegerFieldSkin;

import java.io.File;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static Client.Chat.myinf;
import static GUI.Login.infarea;
import static GUI.Login.userdelay;
import static fileoperate.fileinf.filetheinf;
import static natserver.ping.sendthedelay;
import static thesendinf.sendinf.*;

/**
 * 发送文件的线程
 */
public class sendfile extends Thread {
    DatagramSocket Client;
    File file;
    String title;
    ConcurrentHashMap<String, String> thefilepath;
    String flag;

    public sendfile(DatagramSocket client, File file, String title, ConcurrentHashMap<String, String> thefilepath,String flag) {
        Client = client;
        this.file = file;
        this.title = title;
        this.thefilepath = thefilepath;
        this.flag = flag;
    }

    @Override
    public synchronized void run() {
        String[] infs = title.split("//");
        SocketAddress address = new InetSocketAddress(infs[0].replace("/",""), Integer.parseInt(infs[1]));
        try {
            int delay;
            try {
                delay = Integer.parseInt(sendthedelay(infs[0].replace("/","")));
            } catch (Exception e) {
                delay=15;
            }
//            System.out.println(delay);
            if(flag.contains("fzvoice"))
            {
                Thread.sleep(10);
                sendstrone(address, Client, "正在向您发送语音:", myinf());
            }
            if(flag.contains("fzimage")){
                Thread.sleep(10);
                sendstrone(address, Client, "正在向您发送图片:", myinf());
            }
            if(!flag.contains("fzimage")&&!flag.contains("fzvoice"))
            {
                Thread.sleep(10);
                sendstrone(address, Client, "正在向您发送文件"+file.getName(), myinf());
                infarea.appendText("正在发送文件"+file.getName()+"预计时间"+(double)file.length()/1000/1000*delay+"\n"+"请等待此文件发送完成再发送下一个文件\n");
            }
            sendbigfiles(address, Client, file, myinf(), thefilepath,delay,flag);
            Thread.sleep(delay);
//            System.out.println("发送完成大文件");
            sendFF(address, Client, filetheinf(String.valueOf(file),flag), Objects.requireNonNull(myinf()),delay);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
