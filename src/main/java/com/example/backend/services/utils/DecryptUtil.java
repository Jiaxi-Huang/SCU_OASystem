package com.example.backend.services.utils;

import cn.hutool.json.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;
public class DecryptUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static JSONObject decrypt(String sessionKey, String encryptedData, String iv) throws Exception {
        try {
            // 将 Base64 编码的字符串转换为字节数组
            byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
            byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            // 确保 sessionKey 是 16 字节（128 位）
            // 创建 AES 密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(sessionKeyBytes, "AES");

            // 创建 IV 参数
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

            // 创建 Cipher 实例
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // 解密数据
            byte[] decryptedData = cipher.doFinal(encryptedDataBytes);
            JSONObject json = new JSONObject(new String(decryptedData));
            // 将解密后的字节数组转换为字符串
            return json;
        }
        catch (Exception e){
            throw e; // 如果需要进一步处理异常，可以选择抛出或返回特定值
        }
    }
}