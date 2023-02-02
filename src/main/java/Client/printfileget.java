package Client;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.text.DecimalFormat;
import java.util.Set;

import static GUI.Login.*;
import static natserver.ping.sendthedelay;

public class printfileget extends Thread{
    TextArea area;

    public printfileget(TextArea area) {
        this.area = area;
    }

    @Override
    public void run() {
        DecimalFormat df = new DecimalFormat("0.00");
        while(true) {
            if (filetopart.size() > 0) {
                Set<String> key = filetopart.keySet();
                for (String str : key) {
                    double dtime = (double) filetopart.get(str).size() / (double) 50;
                    if(dtime!=0)
                    {
                       String time = "正在接收"+str+",预计时间:" + df.format(dtime) + "秒";
                        Platform.runLater(() -> {
                            infarea.appendText(time + "\n");
                        });
                    }
                }
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
