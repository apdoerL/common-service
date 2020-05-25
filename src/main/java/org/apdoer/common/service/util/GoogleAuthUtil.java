package org.apdoer.common.service.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.ui.ModelMap;

public class GoogleAuthUtil {

    /***
     * 生成secret
     * @return
     */
    public static ModelMap getKey(String hostName, String username) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secret = key.getKey();
        String qrcode = "otpauth://totp/" + hostName + ":" + username + "?secret=" + secret;
        ModelMap mm = new ModelMap();
        mm.put("secret", secret);
        mm.put("qrcode", qrcode);
        return mm;
    }

    /***
     * 校验key和password
     * @param secretKey
     * @param password
     * @return
     */
    public static boolean isCodeValid(String secretKey, Integer password) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secretKey, password);
    }

//	public static void main(String[] args) {
//		GoogleAuthenticator gAuth = new GoogleAuthenticator();
//		final GoogleAuthenticatorKey key = gAuth.createCredentials();
//		String secretKey = key.getKey();
//		System.out.println(secretKey);
//		int org.apdoer.code = gAuth.getTotpPassword(secretKey);
//		System.out.println(org.apdoer.code);
//		String secret = "TCH45STTBNVXU6QM";
//		int password = 246668;
//		GoogleAuthenticator gAuth = new GoogleAuthenticator();
//		boolean isCodeValid = gAuth.authorize(secret, password);
//		System.out.println(isCodeValid);
//	}
}
