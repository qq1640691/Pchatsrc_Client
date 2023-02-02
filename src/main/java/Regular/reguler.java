package Regular;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class reguler {
    public static boolean isIPAdress(String str) {
        Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }


    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return !m.find();
    }

    public static boolean isport(String str) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(str);
        boolean result = matcher.matches();
        if (result)
        {
            int port = Integer.parseInt(str);
            return port >= 1 && port <= 65535;
        }
        else {
            return false;
        }
    }

    public static boolean isfigure(String str)
    {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static void method3(String fileName, String content) {
    try {
        // 打开一个随机访问文件流，按读写方式
        RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
        // 文件长度，字节数
        long fileLength = randomFile.length();
        // 将写文件指针移到文件尾。
        randomFile.seek(fileLength);
        randomFile.writeBytes(new String(content.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        randomFile.writeBytes("\r\n");
        randomFile.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}



}
