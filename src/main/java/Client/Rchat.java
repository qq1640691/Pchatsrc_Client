package Client;

import Code.AES;
import thesendinf.sendinf;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

import static Client.Vchat.sendhead;
import static GUI.Stage.Close;
import static GUI.Stage.KEY;

public class Rchat extends Thread{

    String ip;
    int port;
    DatagramSocket Client;

    public Rchat(String ip, int port, DatagramSocket client) {
        this.ip = ip;
        this.port = port;
        Client = client;
    }

    @Override
    public void run() {
        while (true) {
            SocketAddress address = new InetSocketAddress(ip, port);
            long time = System.currentTimeMillis();
            File file = new File("temp\\"+time+"send.wav");
            AudioFormat audioFormat = new AudioFormat(8000.0F, 8, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            TargetDataLine targetDataLine;
            try {
                targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
            try {
                targetDataLine.open(audioFormat);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
            targetDataLine.start();
            TargetDataLine finalTargetDataLine = targetDataLine;
            new Thread(() -> {
                AudioInputStream cin = new AudioInputStream(finalTargetDataLine);
                try {
                    AudioSystem.write(cin, AudioFileFormat.Type.WAVE, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            targetDataLine.close();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sendbyte(fileConvertToByteArray(file), time, Client, address);
            if(Close==0)
            {
                System.gc();
                targetDataLine.close();
                return;
            }
        }
    }

        /**
     * ????????????????????????byte???????????????
     */
    private static  byte[] fileConvertToByteArray(File file) {
        if(file.length()>0) {
            byte[] data=sendinf.filetobyte(file);
            file.delete();
            return data;
        }
        return null;
    }

    public static void sendbyte(byte[] frame, long time, DatagramSocket Client, SocketAddress address)
    {
        if(frame!=null) {
            byte[] send = new byte[frame.length + 50];
            /**
             * ?????????????????????,0???????????????,i????????????,time???????????????????????? frame.length???????????????
             */
            StringBuilder head = new StringBuilder("00" + "//" + time + "//" + frame.length + "//");
            sendhead(head, send);
            System.arraycopy(head.toString().getBytes(StandardCharsets.UTF_8), 0, send, 0, 50);
            System.arraycopy(frame, 0, send, 50, frame.length);
            byte[] sendbuf;
            try {
                sendbuf = AES.encrypt(send, KEY);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            DatagramPacket packet = new DatagramPacket(sendbuf, sendbuf.length, address);
            try {
                Client.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
