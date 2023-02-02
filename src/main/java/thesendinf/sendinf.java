package thesendinf;

import Code.AES;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static GUI.Login.*;
import static fileoperate.fileinf.getfileinf;

public class sendinf {
    /**
     * 把一整段数据按照1024字节分段发送,文件地址同样可以改成字节数组,这样更方便
     */
    public static int HEADLENGTH = 256;
    public static int SENDBUFSIZE = 1000;
    public static void sendbigfiles(SocketAddress address, DatagramSocket client, File thefile, String useinf, ConcurrentHashMap<String,String> path,int delay,String flag) throws Exception
    //需要考虑如何定义消息类型,用前256个字节定义,第一位数据包类型,第二位文件名,第三位文件分块,第四位当前块数.第五位,填充0
    {
        //有点小问题,优化一下
        String filename=thefile.getName();
        path.put(filename, String.valueOf(thefile));
        byte[] file;
        if(filetemp.containsKey(filename))
        {
            file = filetemp.get(filename);
        }
        else {
            file = fileConvertToByteArray(thefile);
            filetemp.put(filename,file);
        }
        long times = (thefile.length()/(SENDBUFSIZE-HEADLENGTH))+1;
        byte[] sendbuf = new byte[SENDBUFSIZE];
        byte[] finalbuf = new byte[file.length%(SENDBUFSIZE-HEADLENGTH)+HEADLENGTH];
        for(int i=0;i<times;i++)
        {
            if(i<times-1)//前面的文件块
            {
                StringBuilder packetinf = new StringBuilder(getfileinf(String.valueOf(thefile), useinf,flag) + times + "//" + i + "//");
//                System.out.println(packetinf);
                sendtheinf(delay, sendbuf, packetinf);
                System.arraycopy(file,i*(SENDBUFSIZE-HEADLENGTH),sendbuf,HEADLENGTH,SENDBUFSIZE-HEADLENGTH);
                byte[] send = AES.encrypt(sendbuf,KEY);
                DatagramPacket packet = new DatagramPacket(Objects.requireNonNull(send),send.length,address);
                client.send(packet);
            }
            if(i>=times-1)//最后一块内容
            {
                StringBuilder packetinf = new StringBuilder(getfileinf(String.valueOf(thefile), useinf,flag) + times + "//" + (times - 1) + "//");
                sendtheinf(delay, finalbuf, packetinf);
                System.arraycopy(file,i*(SENDBUFSIZE-HEADLENGTH),finalbuf,HEADLENGTH,finalbuf.length-HEADLENGTH);
                byte[] send = AES.encrypt(finalbuf,KEY);
                DatagramPacket packet2 = new DatagramPacket(Objects.requireNonNull(send),send.length,address);
                client.send(packet2);
            }
        }
        if(file.length<=SENDBUFSIZE-HEADLENGTH)
        {
            StringBuilder packetinf = new StringBuilder(getfileinf(String.valueOf(thefile), useinf,flag) + times + "//" + (times - 1) + "//");
            sendtheinf(delay, finalbuf, packetinf);
            System.arraycopy(file, Math.toIntExact((times - 1) * (SENDBUFSIZE - HEADLENGTH)),finalbuf,HEADLENGTH,finalbuf.length-HEADLENGTH);
            byte[] send = AES.encrypt(finalbuf,KEY);
            DatagramPacket packet = new DatagramPacket(Objects.requireNonNull(send),send.length,address);
            client.send(packet);
        }
    }

    public static void sendtheinf(int delay, byte[] sendbuf, StringBuilder packetinf) throws InterruptedException {
        if (packetinf.toString().getBytes(StandardCharsets.UTF_8).length<HEADLENGTH)
        {
            int templength = HEADLENGTH- packetinf.toString().getBytes(StandardCharsets.UTF_8).length;
            for(int j = 0; j<templength; j++)
            {
                packetinf.append("*");
            }
        }
        Thread.sleep(delay);
        System.arraycopy(packetinf.toString().getBytes(StandardCharsets.UTF_8),0,sendbuf,0,HEADLENGTH);//把头部复制到sendbuf数组里
    }




    public static void sendstr(SocketAddress address,DatagramSocket client,String str,String myinf)
    {
        StringBuilder head = new StringBuilder("mesl/" + myinf + "//");
        sendthestr(address, client, str, head);
    }

    public static void sendfileover(SocketAddress address,DatagramSocket client,String str,String myinf,String flag,String[] infs)
    {
        StringBuilder head = new StringBuilder("fiov/" + myinf + "//");
        str=str.replace(infs[3],"").replace(flag,"");
        sendthestr(address, client, str, head);
    }

    public static void sendthestr(SocketAddress address, DatagramSocket client, String str, StringBuilder head) {
        if(head.toString().getBytes(StandardCharsets.UTF_8).length<HEADLENGTH)
        {
            int templength = HEADLENGTH- head.toString().getBytes(StandardCharsets.UTF_8).length;
            for(int j = 0; j<templength; j++)
            {
                head.append("*");
            }
        }
        byte[] result = (head+str).getBytes(StandardCharsets.UTF_8);
        byte[] send;
        try {
            send = AES.encrypt(result,KEY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DatagramPacket packet = new DatagramPacket(Objects.requireNonNull(send),send.length,address);
        try {
            client.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送消息给单人
     */
    public static void sendstrone(SocketAddress address,DatagramSocket client,String str,String myinf)
    {
        StringBuilder head = new StringBuilder("meso/" + myinf + "//"+str.hashCode()+"//");
        sendthestr(address, client, str, head);
    }

    public static void sendmget(SocketAddress address, DatagramSocket client, int str, String myinf)
    {
        StringBuilder head = new StringBuilder("megt/" + myinf + "//");
        sendthestr(address, client, String.valueOf(str), head);
    }

    public static void sendFF(SocketAddress address,DatagramSocket client,String str,String myinf,int delay) throws InterruptedException {
        byte[] result = ("fifh/"+myinf+"//"+myinf.split("//")[2]+str).getBytes(StandardCharsets.UTF_8);
        byte[] send;
        try {
            send = AES.encrypt(result,KEY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(delay==0)
        {
            Thread.sleep(50);
        }
        else {
            Thread.sleep(delay);
        }
        DatagramPacket packet = new DatagramPacket(Objects.requireNonNull(send),send.length,address);
        try {
            client.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        /**
     * 把一个文件转化为byte字节数组。
     *
     * @return
     */
    public static byte[] fileConvertToByteArray(File file) {
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            data = baos.toByteArray();
            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}

