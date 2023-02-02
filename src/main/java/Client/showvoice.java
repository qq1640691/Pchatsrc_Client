package Client;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import static fileoperate.showfile.listshowvoice;

public class showvoice extends Thread{
    ObservableList<Object> getdata;
    ListView<Object> getlist;
    String title;

    public showvoice(ObservableList<Object> getdata, ListView<Object> getlist, String title) {
        this.getdata = getdata;
        this.getlist = getlist;
        this.title = title;
    }

    @Override
    public void run() {
        while (true)
        {
          listshowvoice("voice\\"+title.split("//")[2],getdata,getlist);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
