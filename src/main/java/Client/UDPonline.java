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

import static GUI.Login.all;
import static GUI.Login.userlist;
public class UDPonline extends Thread{

    @Override
    public synchronized void run() {
        int i=0;
        while(true)
        {
            try {
                Thread.sleep(5000+i* 100000L);
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
                    err.setTextAlignment(TextAlignment.CENTER);
                    err.setFill(Color.RED);
                    err.setFont(Font.font(null, FontWeight.BOLD, 18));
                    HBox theerr = new HBox();
                    theerr.setAlignment(Pos.CENTER);
                    theerr.getChildren().add(err);
                    theerr.setPadding(new Insets(-30, 0, 0, 0));
                    Scene scene = new Scene(theerr, 300, 160);
                    aleat.setScene(scene);
                    aleat.setResizable(false);
                    File ico = new File("ico\\alert.png");
                    try {
                        aleat.getIcons().add(new Image(new FileInputStream(ico)));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    aleat.show();
                    all.close();
                    aleat.setOnCloseRequest(event -> {
                        System.exit(0);
                    });
                });
            }
            i++;

        }
    }
}
