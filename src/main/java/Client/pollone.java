package Client;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.net.DatagramSocket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static fileoperate.showfile.listshowimage;
import static fileoperate.showfile.listshowvoice;
import static Client.Chat.dealpacketone;
import static GUI.Stage.allbyte;

public class pollone {
    ObservableList<Object> getdata;
    ListView<Object> getlist;
    String title;
    CopyOnWriteArrayList<String> mess;

    DatagramSocket Client;//绑定的ip和端口


    public pollone(ObservableList<Object> getdata, ListView<Object> getlist, String title, CopyOnWriteArrayList<String> mess, DatagramSocket client) {
        this.getdata = getdata;
        this.getlist = getlist;
        this.title = title;
        this.mess = mess;
        Client = client;
    }

    public void poll()
    {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        service.scheduleWithFixedDelay(()->{
            listshowimage("image\\"+title.split("//")[2], getdata,getlist);
        },10,5, TimeUnit.SECONDS);

        service.scheduleWithFixedDelay(()->{
            listshowvoice("voice\\"+title.split("//")[2],getdata,getlist);
        },10,5, TimeUnit.SECONDS);


        service.scheduleWithFixedDelay(()->{
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
        },0,100, TimeUnit.MILLISECONDS);

    }
}
