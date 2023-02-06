package Client;


import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static GUI.Stage.all;
import static GUI.Stage.userlist;
public class UDPonline extends Thread{

    @Override
    public synchronized void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(userlist.size()==0)
        {
            Platform.runLater(()->
            {
                Stage aleat = new Stage();
                Text err = new Text();
                err.setText("抱歉,服务器暂未运行");
                GUI.Stage.newalert(aleat, err);
                all.close();
                aleat.setOnCloseRequest(event -> {
                    System.exit(0);
                });
            });
        }
    }
}
