package Client;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static GUI.Stage.*;
import static fileoperate.showfile.delecttemp;

public class showvideo extends Thread{
    @Override
    public void run() {
        Platform.runLater(()->{
            ImageView live1 = new ImageView();
            Stage stage = new Stage();
            HBox box = new HBox();
            box.getChildren().add(live1);
            Scene scene = new Scene(box, 400, 300);
            stage.setScene(scene);
            videostrem.clear();
                showedtime.clear();
                longtime.clear();
            Thread getimage=new Thread(()->{
                while (true) {
                    for(String time:longtime)
                    {
                        if(videostrem.containsKey(time))
                        {
                            long dt = videostrem.get(time).length/1000;
                            try {
                                Thread.sleep(dt);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            byte[] showimage = videostrem.get(time);
                            Image image = getvideo(showimage);
                            live1.setImage(image);
                            videostrem.remove(time);
                            longtime.remove(time);
                            showedtime.add(time);
                        }
                    }
                }
            });
            getimage.start();
            File ico = new File("ico\\video.png");
        try {
            stage.getIcons().add(new Image(new FileInputStream(ico)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
            stage.show();
            stage.setX(100);
            stage.setY(100);
            stage.setResizable(false);
            Thread showvc = new showvc();
            showvc.start();
            stage.setOnCloseRequest(event -> {
                sendvideo="allow";
                videostrem.clear();
                showedtime.clear();
                showvc.stop();
                longtime.clear();
                getimage.stop();
                delecttemp();
            }
            );
        });
    }
    public static Image getvideo(byte[] imageInByte)
    {
        if(imageInByte.length>0)
        {
            InputStream in = new ByteArrayInputStream(imageInByte);
            try {
                BufferedImage bImageFromConvert = ImageIO.read(in);
                if(bImageFromConvert!=null)
                {
                  return SwingFXUtils.toFXImage(bImageFromConvert, null);
                }
                else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }
}
