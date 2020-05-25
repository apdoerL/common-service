package org.apdoer.common.service.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Slf4j
public class EncryptUtil {

    /***
     * 将url进行encode
     * @param url
     * @return
     */
    public static String urlEncode(String url) throws EncoderException {
        URLCodec codec = new URLCodec();
        return codec.encode(url);
    }

    /***
     * 将url进行decode
     * @param url
     * @return
     */
    public static String urlDecode(String url) throws DecoderException {
        URLCodec codec = new URLCodec();
        return codec.decode(url);
    }

    public static String utf8Enode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }

    /***
     * HmacSha256签名,进行base64编码
     * @param signStr
     * @param secretKey
     * @return
     */
    public static String getHmacHash(String signStr, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256HMAC.init(secretKeySpec);
        return Base64.encodeBase64String(sha256HMAC.doFinal(signStr.getBytes(StandardCharsets.UTF_8)));
    }


    public static String myMD5(String strSrc) {
        MessageDigest md = null;
        String encStr = null;
        byte[] bt = strSrc.getBytes(StandardCharsets.UTF_8);

        try {
            md = MessageDigest.getInstance("MD5");
            md.update(bt);
            encStr = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException var5) {
            log.error("error at md5",var5);
        }

        return encStr;
    }
    /**
     * md5->16进制
     *
     * @param strSrc
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String mD5And2Hex(String strSrc) throws NoSuchAlgorithmException {
        MessageDigest md = null;
        String encStr = null;
        byte[] bt = strSrc.getBytes(StandardCharsets.UTF_8);
        md = MessageDigest.getInstance("MD5");
        md.update(bt);
        encStr = bytes2Hex(md.digest());
        return encStr;
    }

    /**
     * 转换为16进制字符
     **/
    public static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * AES加密
     */
    public static String encrypt(String randomKey, String sSrc) throws Exception {
        byte[] raw = randomKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
        // 此处使用BAES64做转码功能，同时能起到2次加密的作用。
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * AES解密
     **/
    public static String decrypt(String randomKey, String sSrc) throws Exception {
        // 随机密锁
        byte[] raw = randomKey.getBytes(US_ASCII);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708"
                .getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        // 先用bAES64解密
        byte[] encrypted1 = Base64.decodeBase64(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, StandardCharsets.UTF_8);
    }

    //生成随机密锁
    private static String getKey(int length) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        // 参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                sb.append((char) (random.nextInt(26) + temp));
            } else {
                sb.append(random.nextInt(10));
            }
        }
        return new String(sb.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

    }

    /***
     * HmacSha256签名,转换成字符串
     *
     * @param message  "verb + path + expires + data"
     * @param apiKey 秘钥
     * @return String
     */
    public static String getHmacHashForSignature(String apiKey, String message) throws Exception {
        String hash;
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec apiKeySpec = new SecretKeySpec(apiKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256.init(apiKeySpec);
        hash = byteArrayToHexString(sha256.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        return hash;
    }

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * 16进制字符串转换成普通字符串<br>
     * e.g. hexString=
     * 0x687474703a2f2f3139322e3136382e31362e31373a383038352f232f73686172652f736861726552656769737465723f696e76697465436f64653d56445654
     * <br>
     * 转换后为:http://192.168.16.17:8085/#/share/shareRegister?inviteCode=VDVT<br>
     *
     * @param hexString 以0x打头的16进制字符串
     * @return
     */
    public static String hexString2String(String hexString) {
        byte[] baKeyword = new byte[hexString.length() / 2];
        int startIndex = hexString.startsWith("0x") ? 1 : 0;
        for (int i = startIndex; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            return new String(baKeyword, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
