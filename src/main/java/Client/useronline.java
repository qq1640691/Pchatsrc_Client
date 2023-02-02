package Client;

import javafx.application.Platform;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static GUI.Login.*;

public class useronline extends Thread{
    ConcurrentHashMap<String,Long> userzx;
    CopyOnWriteArrayList<String> userlist;

    public useronline(ConcurrentHashMap<String, Long> userzx, CopyOnWriteArrayList<String> userlist) {
        this.userzx = userzx;
        this.userlist = userlist;
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for(String s:userlist)
            {
                long now = System.currentTimeMillis();
                if(now-userzx.get(s)>180000L)
                {
                    userlist.remove(s);
                    userzx.remove(s);
                    userdelay.remove(s.split("//")[2]);
                    Platform.runLater(()->{
                        infarea.appendText("用户"+s.split("//")[2]+"下线,移除用户\n");
                        listView.getItems().clear();
                        data.addAll(userlist);
                        listView.setItems(data);
                    });
                }
            }
            try {
                Thread.sleep(54000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
