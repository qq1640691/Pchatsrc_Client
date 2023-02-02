package Client;

import javafx.scene.control.TextArea;

import static GUI.Login.userdelay;

public class Linkuser extends Thread{
    TextArea area;

    public Linkuser(TextArea area) {
        this.area = area;
    }

    @Override
    public void run() {
        while(true)
        {
            if(userdelay.size()==0)
            {
                area.appendText("正在与其他用户连接中,请耐心等待\n");
            }
            if(userdelay.size()>0){
                area.appendText("已链接用户:\n");
                area.appendText(userdelay+"\n");
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
