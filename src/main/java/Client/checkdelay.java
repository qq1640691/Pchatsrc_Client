package Client;

import java.util.Set;

import static Client.Chat.receivedelay;
import static GUI.Login.delaybyte;

/**
 * 接收数据的线程
 */
public class checkdelay extends Thread {
    @Override
    public synchronized void run() {
        while (true) {
            for(byte[] result:delaybyte)
            {
                assert result != null;
                    try {
                        receivedelay(result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    delaybyte.remove(result);
                }
            }
        }
    }
