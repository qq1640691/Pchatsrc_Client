package fileoperate;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static GUI.Stage.BYTELENGTH;

/**
 *  完成数据内容的插入
 */
public class writefile {
    /**
     * file//127.0.0.1//58589//方正//filename//file.length//file.allpart//part
     */
    public static void insertContent(byte[] result, ConcurrentHashMap<String,CopyOnWriteArrayList<String>> filehm,String title) throws IOException {
        String inf = new String(result,0,256);
        String[] infs = inf.split("//");
        String path = infs[4];
        File file = null;
        if(path.contains("fzimage"))
        {
            file = new File("image\\"+title+"\\"+path+"fzt");
        }
        if(path.contains("fzvoice"))
        {
            file = new File("voice\\"+title+"\\"+path+"fzt");
        }
        if(!path.contains("fzimage")&&!path.contains("fzvoice"))
        {
            file = new File("file\\"+title+"\\"+path+"fzt");
        }
        int index = Integer.parseInt(infs[7])*BYTELENGTH;
        assert file != null;
        writelostbyte(infs, file);
        File tmpfile = new File("temp\\"+file.getName()+"temp");
        try (FileOutputStream tmpout = new FileOutputStream(tmpfile); FileInputStream tmpinput = new FileInputStream(tmpfile); RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            //等下需要写入到新文件中
            //读取临时文件
            //打开目标文件
            //指定要插入文件位置
            randomAccessFile.seek(index + result.length - 256);
            //把该位置文件读取出来
            byte[] bytes = new byte[result.length - 256];//?
            int len;
            /**
             * 这句话吧while改for,然后for的次数是后面的字节数
             */
            while ((len = randomAccessFile.read(bytes)) != -1) {
                tmpout.write(bytes, 0, len);
            }
            tmpout.flush();
            //将指针移动到指定位置
            randomAccessFile.seek(index);
            byte[] output = new byte[result.length - 256];
            System.arraycopy(result, 256, output, 0, result.length - 256);
            randomAccessFile.write(output);
            //将临时文件内容重写写入原文件
            while ((len = tmpinput.read(bytes)) != -1) {
                randomAccessFile.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        CopyOnWriteArrayList<String> tempd = filehm.get(infs[4]);
        tempd.remove(infs[7]);
        filehm.put(infs[4],tempd);
    }

    public static void writelostbyte(String[] infs, File file) throws IOException {
        if(!file.exists()){
            RandomAccessFile r = null;
            try {
                r = new RandomAccessFile(file, "rw");
                r.setLength(Integer.parseInt(infs[5]));
            } finally{
                if (r != null) {
                    try {
                    r.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static void getnewfile(byte[] result, ConcurrentHashMap<String,CopyOnWriteArrayList<String>> filehm,String title) throws IOException {
        String inf = new String(result,0,256);
        String[] infs = inf.split("//");
        String path = infs[4];
        File file;
        CopyOnWriteArrayList<String> tempd = filehm.get(infs[4]);
        if (Integer.parseInt(infs[7])<Integer.parseInt(tempd.get(0)))
        {
            return;
        }
        if(!path.contains("fzimage")&&!path.contains("voice"))
        {
            file = new File("file\\"+title+"\\"+path+"fzt");
            writebyte(result, filehm, infs, file, tempd);
            return;
        }
        if(path.contains("fzimage"))
        {
            file = new File("image\\"+title+"\\"+path+"fzt");
            writebyte(result, filehm, infs, file, tempd);
            return;
        }
        if(path.contains("fzvoice"))
        {
            file = new File("voice\\"+title+"\\"+path+"fzt");
            writebyte(result, filehm, infs, file, tempd);
        }
    }

    public static void writebyte(byte[] result, ConcurrentHashMap<String, CopyOnWriteArrayList<String>> filehm, String[] infs, File file, CopyOnWriteArrayList<String> tempd) throws IOException {
        int index = Integer.parseInt(infs[7])*BYTELENGTH;
        writelostbyte(infs, file);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.seek(index);
            byte[] output = new byte[result.length - 256];
            System.arraycopy(result, 256, output, 0, result.length - 256);
            randomAccessFile.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempd.remove(infs[7]);
        filehm.put(infs[4],tempd);
    }
}
