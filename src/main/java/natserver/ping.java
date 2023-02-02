package natserver;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ping {
    public static String sendthedelay(String ip) throws Exception {
        BufferedReader br = null;
        try{
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("ping " + ip);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "GB2312");
            br = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            if(!sb.toString().contains("平均")){
                return "无网络";
            }
            else{
                return sb.substring(sb.toString().lastIndexOf("平均")+5,sb.length()).replace("ms","");
            }
        }catch (Exception e){
            throw new Exception();
        }finally {
            if (br != null){
                br.close();
            }
        }
    }
}