package Client;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static fileoperate.showfile.listshowimage;
import static fileoperate.showfile.listshowvoice;

public class pollone {
    ObservableList<Object> getdata;
    ListView<Object> getlist;
    String title;

    public pollone(ObservableList<Object> getdata, ListView<Object> getlist, String title) {
        this.getdata = getdata;
        this.getlist = getlist;
        this.title = title;
    }

    public void poll()
    {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.scheduleAtFixedRate(()->{
            listshowimage("image\\"+title.split("//")[2], getdata,getlist);
        },10,5, TimeUnit.SECONDS);

        service.scheduleAtFixedRate(()->{
            listshowvoice("voice\\"+title.split("//")[2],getdata,getlist);
        },10,5, TimeUnit.SECONDS);

    }
}
