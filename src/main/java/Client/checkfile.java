package Client;

import javafx.application.Platform;

import java.net.DatagramSocket;
import java.util.Set;

import static Client.Chat.getlostfile;
import static GUI.Login.*;
import static natserver.ping.sendthedelay;

public class checkfile extends Thread {
    DatagramSocket Client;

    public checkfile(DatagramSocket client) {
        Client = client;
    }

    @Override
    public void run() {
        Set<String> keyset;
        while (true) {
            if(fileinf.size()>0)
            {
               keyset = fileinf.keySet();
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
                            delays =15;
                        }
                        long timed = (long) (4L * Double.parseDouble(inf[6]) / (double) 1000*delays);
                        if (System.currentTimeMillis() - Long.parseLong(inf[9]) > timed*1000 && filetopart.get(file).size() > 0) {
                            Platform.runLater(()->{
                                infarea.appendText(file+"还剩下"+filetopart.get(file).size()+"块,需要重传\n");
                            });
                            try {
                                getlostfile(inf, Client);
                                Thread.sleep((long) (filetopart.get(file).size()/(double) 1000*delays));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
