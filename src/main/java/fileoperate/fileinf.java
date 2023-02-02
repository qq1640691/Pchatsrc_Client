package fileoperate;

import java.io.File;

public class fileinf {
    public static String getfileinf(String filepath,String userinf,String flag)
    {
        File file = new File(filepath);
        String filename = file.getName();
        return "file/"+userinf+"//"+userinf.split("//")[2]+flag+filename+"//"+file.length()+"//";
    }
    public static String filetheinf(String filepath,String flag)
    {
        File file = new File(filepath);
        String filename = file.getName();
        return flag+filename+"//"+file.length()+"//";
    }

}
