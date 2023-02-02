package Code;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
/**
 * AES算法进行加密
 *
 * @author xxx
 * @create 2022年2月22日 下午1:52:52
 **/
public class AES {

    /**
     * 密钥
     */
//    private static final String KEY = "abcdefghhgfedcba";// AES加密要求key必须要256个比特位（这里需要长度为16，否则会报错）

    /**
     * 算法
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";



    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     */
    private static byte[] aesEncryptToBytes(byte[] content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher.doFinal(content);
    }


    /**
     * AES加密为base 64 code
     *
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     */
    public static byte[] encrypt(byte[] content, String encryptKey) throws Exception {
        return aesEncryptToBytes(content, encryptKey);
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     */
    private static byte[] aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return decryptBytes;
    }


    /**
     * 将base 64 code AES解密
     *
     * @param decryptKey 解密密钥
     * @return 解密后的string
     */
    public static byte[] decrypt(byte[] content, String decryptKey) throws Exception {
        return aesDecryptByBytes(content, decryptKey);
    }
}

