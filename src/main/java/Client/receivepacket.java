package Client;

import Code.AES;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static GUI.Login.*;

public class receivepacket extends Thread{
    DatagramSocket Client;
    public receivepacket(DatagramSocket client) {
        Client = client;
    }

    @Override
    public synchronized void run() {
        while(true)
        {
            byte[] get = new byte[20480];
            DatagramPacket packet = new DatagramPacket(get, get.length);
            try {
                Client.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] result = new byte[packet.getLength()];
            System.arraycopy(packet.getData(),0,result,0,result.length);
            packetbytes.add(result);
        }
    }

    public static void getfrema(byte[] get,String time,String length,String part)
    {
        if(!longtime.contains(time))
        {
            longtime.add(time);
        }
        if (!videostrem.containsKey(time))
        {
            videostrem.put(time,new byte[Integer.parseInt(length)]);
            byte[] temp = videostrem.get(time);
            System.arraycopy(get,50,temp,Integer.parseInt(part)*950,get.length-50);
            videostrem.put(time,temp);
        }
        if(videostrem.containsKey(time))
        {
            byte[] temp = videostrem.get(time);
            System.arraycopy(get,50,temp,Integer.parseInt(part)*950,get.length-50);
            videostrem.put(time,temp);
        }
    }

//    public static void getvoice(byte[] get,String time,String length,String part)
//    {
//        if(!longtimev.contains(time))
//        {
//            longtimev.add(time);
//        }
//        if (!videostremv.containsKey(time))
//        {
//            videostremv.put(time,new byte[Integer.parseInt(length)]);
//            byte[] temp = videostremv.get(time);
//            System.arraycopy(get,50,temp,Integer.parseInt(part)*950,get.length-50);
//            videostremv.put(time,temp);
//        }
//        if(videostrem.containsKey(time))
//        {
//            byte[] temp = videostremv.get(time);
//            System.arraycopy(get,50,temp,Integer.parseInt(part)*950,get.length-50);
//            videostremv.put(time,temp);
//        }
//    }
}
