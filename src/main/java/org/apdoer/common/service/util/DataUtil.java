package org.apdoer.common.service.util;

import java.util.regex.Pattern;

import static org.apdoer.common.service.constants.CommonConstants.EMAIL_FLAG;


public class DataUtil {

    private DataUtil() {
    }


    private static final String MAIL_REGEX = "(\\w{2})(\\w+)(@\\w+)";
    private static final String MAIL_REPLACE = "$1***$3";
    /**
     * $1 表示第1组的内容
      */
    private static final Pattern MAIL_PATTERN = Pattern.compile(MAIL_REGEX);

    /**
     *  手机号处理
     */
    private static final String PHONE_REGEX = "(\\d{3})(\\d*)(\\d{3})";
    private static final String PHONE_REPLACE = "$1****$3";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);


    public static String hideEmail(String email) {
        return MAIL_PATTERN.matcher(email).replaceAll(MAIL_REPLACE);
    }


    public static String hidePhone(String phone) {
        return PHONE_PATTERN.matcher(phone).replaceAll(PHONE_REPLACE);
    }

    public static String hideUserName(String username) {
        if (username.contains(EMAIL_FLAG)) {
            return hideEmail(username);
        } else {
            return hidePhone(username);
        }
    }
}
