package fileoperate;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static GUI.Stage.infarea;

public class showfile {

	    public static void listshowimage(String path, ObservableList<Object> getdata, ListView<Object> getlist) {
	        File f = new File(path);//获取路径
	        if (!f.exists()) {
	            return;
	        }
	        File[] fa = f.listFiles();//用数组接收
        if(fa!=null) {
            for (File fs : fa) {//循环遍历
                if (!fs.isDirectory()) {
                        if (!fs.getName().endsWith("fzt")) {
                            Platform.runLater(()->{
                                FileInputStream input;
                                try {
                                    input = new FileInputStream(fs);
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                Image image = new Image(input);
                                ImageView imageView = new ImageView(image);
                                double bl = image.getHeight()/image.getWidth();
                                imageView.setFitHeight(450*bl);
                                imageView.setFitWidth(450);
                                getdata.add(imageView);
                                getlist.setItems(getdata);
                                try {
                                    input.close();
                                    File file = new File("done\\"+ System.currentTimeMillis()+fs.getName());
                                    if (!file.getParentFile().exists()) {
                                      file.getParentFile().mkdirs();
                                    }
                                    fs.renameTo(file);
                                    infarea.appendText("图片已显示\n");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }

                }
            }
        }

    public static void listshowvoice(String path,ObservableList<Object> getdata, ListView<Object> getlist) {
	        File f = new File(path);//获取路径
	        if (!f.exists()) {
	            return;
	        }
	        File[] fa = f.listFiles();//用数组接收
        if(fa!=null) {
            for (File fs : fa) {//循环遍历
                if (!fs.isDirectory()) {
                    if (!fs.getName().endsWith("fzt")) {
                        Platform.runLater(()->{
                            File file = new File("done\\"+ System.currentTimeMillis()+fs.getName());
                            if (!file.getParentFile().exists()) {
                              file.getParentFile().mkdirs();
                            }
                            fs.renameTo(file);
                            getdata.add(String.valueOf(file));
                            getlist.setItems(getdata);
                        });
                    }
                }
            }
        }
    }

    public static void delecttemp()
    {
        File f = new File("temp\\");//获取路径
	        if (!f.exists()) {
	            return;
	        }
            File[] fa = f.listFiles();//用数组接收
        if (fa != null) {
            for (File fs : fa) {//循环遍历
                    if (!fs.isDirectory()) {
                        fs.delete();
                    }
                }
        }
    }
}
