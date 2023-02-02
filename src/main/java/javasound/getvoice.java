package javasound;

import Client.sendfile;
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

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentHashMap;

public class getvoice {
    //空参也可以吧,直接在里面新建
    public static void record(DatagramSocket client,String title, ConcurrentHashMap<String, String> thefilepath)
            throws LineUnavailableException, InterruptedException {
                File outputFile = new File("record\\"+System.currentTimeMillis()+".wav");
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 2, 4, 8000.0F, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                audioFormat);
        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem
                .getLine(info);
        targetDataLine.open(audioFormat);
        targetDataLine.start();
        new Thread(() -> {
            AudioInputStream cin = new AudioInputStream(targetDataLine);
            try {
                AudioSystem.write(cin, AudioFileFormat.Type.WAVE,
                        outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        Platform.runLater(()->{
            Stage alert = new Stage();
            Text err = new Text();
            err.setText("正在录音,关闭停止录音");
            err.setTextAlignment(TextAlignment.CENTER);
            err.setFill(Color.RED);
            err.setFont(Font.font(null, FontWeight.BOLD, 18));
            HBox theerr = new HBox();
            theerr.setAlignment(Pos.CENTER);
            theerr.getChildren().add(err);
            theerr.setPadding(new Insets(-30, 0, 0, 0));
            Scene scene = new Scene(theerr, 300, 160);
            alert.setScene(scene);
            alert.setResizable(false);
            File ico = new File("ico\\alert.png");
            try {
                alert.getIcons().add(new Image(new FileInputStream(ico)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            alert.show();
            alert.setOnCloseRequest(e->{
                targetDataLine.close();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                Thread sendvoice = new sendfile(client,outputFile,title,thefilepath,"fzvoice");
                sendvoice.start();
            });
        });
    }
}
