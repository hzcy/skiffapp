package com.yellowriver.skiff.Help;




import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptAES(String data, String key, String iv) throws Exception {

        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");   //参数分别代表 算法名称/加密模式/数据填充方式
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decryptAES(String data, String key, String iv,String isGBK) throws Exception {
        LogUtil.info("轻舟执行aes解密","获取的内容为"+data+"key:"+key+"iv:"+iv+"isgbk:"+isGBK);
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec;
            IvParameterSpec ivspec;
            if(isGBK.equals("1")) {
                 keyspec = new SecretKeySpec(key.getBytes("GBK"), "AES");
                 ivspec = new IvParameterSpec(iv.getBytes("GBK"));

            }else{
                 keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
                 ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));
            }
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            LogUtil.info("轻舟执行aes解密","解密的内容为"+originalString);
            if(originalString!=null){
              originalString = originalString.replaceAll(" ","");
            }
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
