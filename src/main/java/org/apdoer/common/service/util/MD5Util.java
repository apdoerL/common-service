package org.apdoer.common.service.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类，提供字符串MD5加密（校验）、文件MD5值获取（校验）功能。
 *
 * @author newex-team
 * @date 2018/02/08
 * @since 0.0.1
 **/
@Slf4j
public class MD5Util {
    /**
     * 16进制字符集
     */
    private static final char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * 获取文件的MD5值
     *
     * @param file 目标文件
     * @return MD5字符串
     */
    public static String getFileMD5String(final File file) {
        String ret = "";
        FileInputStream in = null;
        FileChannel ch = null;

        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            ch = in.getChannel();
            final ByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messageDigest.update(byteBuffer);
            ret = bytesToHex(messageDigest.digest());
        } catch (final Exception e) {
            log.error("error:{}", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException e) {
                    log.error("error:{}", e);
                }
            }
            if (ch != null) {
                try {
                    ch.close();
                } catch (final IOException e) {
                    log.error("error:{}", e);
                }
            }
        }

        return ret;
    }

    /**
     * 获取文件的MD5值
     *
     * @param fileName 目标文件的完整名称
     * @return MD5字符串
     */
    public static String getFileMD5String(final String fileName) {
        return getFileMD5String(new File(fileName));
    }

    /**
     * MD5加密字符串
     *
     * @param str 目标字符串
     * @return MD5加密后的字符串
     */
    public static String getMD5String(final String str) {
        return getMD5String(str.getBytes());
    }

    /**
     * MD5加密以byte数组表示的字符串
     *
     * @param bytes 目标byte数组
     * @return MD5加密后的字符串
     */
    public static String getMD5String(final byte[] bytes) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            return bytesToHex(messageDigest.digest());
        } catch (final NoSuchAlgorithmException e) {
            log.error("error:{}", e);
        }
        return "";

    }

    /**
     * 校验密码与其MD5是否一致
     *
     * @param pwd 密码字符串
     * @param md5 基准MD5值
     * @return 检验结果
     */
    public static boolean checkPassword(final String pwd, final String md5) {
        return getMD5String(pwd).equalsIgnoreCase(md5);
    }

    /**
     * 校验密码与其MD5是否一致
     *
     * @param pwd 以字符数组表示的密码
     * @param md5 基准MD5值
     * @return 检验结果
     */
    public static boolean checkPassword(final char[] pwd, final String md5) {
        return checkPassword(new String(pwd), md5);
    }

    /**
     * 检验文件的MD5值
     *
     * @param file 目标文件
     * @param md5  基准MD5值
     * @return 检验结果
     */
    public static boolean checkFileMD5(final File file, final String md5) {
        return getFileMD5String(file).equalsIgnoreCase(md5);
    }

    /**
     * 检验文件的MD5值
     *
     * @param fileName 目标文件的完整名称
     * @param md5      基准MD5值
     * @return 检验结果
     */
    public static boolean checkFileMD5(final String fileName, final String md5) {
        return checkFileMD5(new File(fileName), md5);
    }

    /**
     * 将字节数组转换成16进制字符串
     *
     * @param bytes 目标字节数组
     * @return 转换结果
     */
    public static String bytesToHex(final byte[] bytes) {
        return bytesToHex(bytes, 0, bytes.length);
    }

    /**
     * 将字节数组中指定区间的子数组转换成16进制字符串
     *
     * @param bytes 目标字节数组
     * @param start 起始位置（包括该位置）
     * @param end   结束位置（不包括该位置）
     * @return 转换结果
     */
    public static String bytesToHex(final byte[] bytes, final int start, final int end) {
        final StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + end; i++) {
            sb.append(byteToHex(bytes[i]));
        }
        return sb.toString();
    }

    /**
     * 将单个字节码转换成16进制字符串
     *
     * @param bt 目标字节
     * @return 转换结果
     */
    public static String byteToHex(final byte bt) {
        return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];
    }

    /**
     * @param plainText
     * @return
     */
    public static long byteToInt(final String plainText) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        messageDigest.update(plainText.getBytes());
        final byte[] b = messageDigest.digest();
        int i;
        final StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toOctalString(i));
        }
        System.out.println(buf.toString());
        final String result = buf.toString().substring(8, 24);
        if (result == null || "".equals(result.trim())) {
            return 0L;
        }
        return Long.valueOf(result);
    }

    /**
     * encode string
     *
     * @param str
     * @return String
     */
    public static String encodeBySha1(final String str) {
        if (str == null) {
            return null;
        }

        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(final byte[] bytes) {
        final int len = bytes.length;
        final StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String byteArrayToString(final String text) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            final byte[] b = messageDigest.digest(text.getBytes());
            final StringBuffer resultSb = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                //使用本函数则返回加密结果的10进制数字字串，即全数字形式
                resultSb.append(byteToNumString(b[i]));
            }
            return resultSb.toString();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return "0";
        }
    }

    private static String byteToNumString(final byte b) {
        int _b = b;
        if (_b < 0) {
            _b = 256 + _b;
        }
        return String.valueOf(_b);
    }

    public static final int byteArrayToShort(final byte[] b) {
        return (b[0] << 8) + (b[1] & 0xFF);
    }

    public static String getMD5String16(final String plainText) {
        final String result = getMD5String(plainText);
        if (result != null && result.length() > 24) {
            return result.substring(8, 24);
        }
        return result;
    }

    public static String getMD5String64(final String plainText) {
        try {
            // 确定计算方法
            final MessageDigest md5 = MessageDigest.getInstance("SHA-256");
            md5.update(plainText.getBytes());
            return bytesToHex(md5.digest());
        } catch (final NoSuchAlgorithmException e) {
            log.error("error:{}", e);
        }
        return null;
    }

}  
