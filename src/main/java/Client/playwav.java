package Client;


import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;
import java.io.File;
import java.io.IOException;

public class playwav {

    public static void play(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file));
            AudioFormat audioFormat = audioInputStream.getFormat();
            Info dataLineInfo = new Info(SourceDataLine.class, audioFormat);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            byte[] b = new byte[1024];
            int len;
            sourceDataLine.open(audioFormat, 1024);
            sourceDataLine.start();
            while ((len = audioInputStream.read(b)) != -1) {
                sourceDataLine.write(b, 0, len);
            }
            audioInputStream.close();
            sourceDataLine.drain();
            sourceDataLine.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("error");
        }
    }
}