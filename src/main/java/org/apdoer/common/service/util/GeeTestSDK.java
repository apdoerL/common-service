

package org.apdoer.common.service.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class GeeTestSDK {
    protected final String verName = "4.0";
    protected final String sdkLang = "java";
    protected final String apiUrl = "http://api.geetest.com";
    protected final String registerUrl = "/register.php";
    protected final String validateUrl = "/validate.php";
    protected final String json_format = "1";
    public static final String fn_geetest_challenge = "geetest_challenge";
    public static final String fn_geetest_validate = "geetest_validate";
    public static final String fn_geetest_seccode = "geetest_seccode";
    private String captchaId = "";
    private String privateKey = "";
    private boolean newFailback = false;
    private String responseStr = "";
    public boolean debugCode = true;
    public String gtServerStatusSessionKey = "gt_server_status";

    public GeeTestSDK(String captchaId, String privateKey, boolean newFailback) {
        this.captchaId = captchaId;
        this.privateKey = privateKey;
        this.newFailback = newFailback;
    }

    public String getResponseStr() {
        return this.responseStr;
    }

    public String getVersionInfo() {
        return "4.0";
    }

    private String getFailPreProcessRes() {
        Long rnd1 = Math.round(Math.random() * 100.0D);
        Long rnd2 = Math.round(Math.random() * 100.0D);
        String md5Str1 = this.md5Encode(rnd1 + "");
        String md5Str2 = this.md5Encode(rnd2 + "");
        String challenge = md5Str1 + md5Str2.substring(0, 2);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("success", 0);
            jsonObject.put("gt", this.captchaId);
            jsonObject.put("challenge", challenge);
            jsonObject.put("new_captcha", this.newFailback);
        } catch (JSONException var8) {
            this.gtlog("json dumps error");
        }

        return jsonObject.toString();
    }

    private String getSuccessPreProcessRes(String challenge) {
        this.gtlog("challenge:" + challenge);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("success", 1);
            jsonObject.put("gt", this.captchaId);
            jsonObject.put("challenge", challenge);
        } catch (JSONException var4) {
            this.gtlog("json dumps error");
        }

        return jsonObject.toString();
    }

    public int preProcess(HashMap<String, String> data) {
        if (this.registerChallenge(data) != 1) {
            this.responseStr = this.getFailPreProcessRes();
            return 0;
        } else {
            return 1;
        }
    }

    private int registerChallenge(HashMap<String, String> data) {
        try {
            String userId = (String)data.get("user_id");
            String clientType = (String)data.get("client_type");
            String ipAddress = (String)data.get("ip_address");
            String getUrl = "http://api.geetest.com/register.php?";
            StringBuilder var10000 = (new StringBuilder()).append("gt=").append(this.captchaId).append("&json_format=");
            this.getClass();
            String param = var10000.append("1").toString();
            if (userId != null) {
                param = param + "&user_id=" + userId;
            }

            if (clientType != null) {
                param = param + "&client_type=" + clientType;
            }

            if (ipAddress != null) {
                param = param + "&ip_address=" + ipAddress;
            }

            this.gtlog("GET_URL:" + getUrl + param);
            String result_str = this.readContentFromGet(getUrl + param);
            if (result_str == "fail") {
                this.gtlog("gtServer register challenge failed");
                return 0;
            } else {
                this.gtlog("result:" + result_str);
                JSONObject jsonObject = JSONObject.parseObject(result_str);
                String return_challenge = jsonObject.getString("challenge");
                this.gtlog("return_challenge:" + return_challenge);
                if (return_challenge.length() == 32) {
                    this.responseStr = this.getSuccessPreProcessRes(this.md5Encode(return_challenge + this.privateKey));
                    return 1;
                } else {
                    this.gtlog("gtServer register challenge error");
                    return 0;
                }
            }
        } catch (Exception var10) {
            this.gtlog(var10.toString());
            this.gtlog("exception:register api");
            return 0;
        }
    }

    protected boolean objIsEmpty(Object gtObj) {
        if (gtObj == null) {
            return true;
        } else {
            return gtObj.toString().trim().length() == 0;
        }
    }

    private boolean resquestIsLegal(String challenge, String validate, String seccode) {
        if (this.objIsEmpty(challenge)) {
            return false;
        } else if (this.objIsEmpty(validate)) {
            return false;
        } else {
            return !this.objIsEmpty(seccode);
        }
    }

    public int enhencedValidateRequest(String challenge, String validate, String seccode, HashMap<String, String> data) {
        if (!this.resquestIsLegal(challenge, validate, seccode)) {
            return 0;
        } else {
            this.gtlog("request legitimate");
            String userId = (String)data.get("user_id");
            String clientType = (String)data.get("client_type");
            String ipAddress = (String)data.get("ip_address");
            StringBuilder var10000 = new StringBuilder();
            this.getClass();
            var10000 = var10000.append("http://api.geetest.com");
            this.getClass();
            String postUrl = var10000.append("/validate.php").toString();
            Object[] var10001 = new Object[]{challenge, validate, seccode, null};
            this.getClass();
            var10001[3] = "1";
            String param = String.format("challenge=%s&validate=%s&seccode=%s&json_format=%s", var10001);
            if (userId != null) {
                param = param + "&user_id=" + userId;
            }

            if (clientType != null) {
                param = param + "&client_type=" + clientType;
            }

            if (ipAddress != null) {
                param = param + "&ip_address=" + ipAddress;
            }

            this.gtlog("param:" + param);
            String response = "";

            try {
                if (validate.length() <= 0) {
                    return 0;
                }

                if (!this.checkResultByPrivate(challenge, validate)) {
                    return 0;
                }

                this.gtlog("checkResultByPrivate");
                response = this.readContentFromPost(postUrl, param);
                this.gtlog("response: " + response);
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            String return_seccode = "";

            try {
                JSONObject return_map = JSONObject.parseObject(response);
                return_seccode = return_map.getString("seccode");
                this.gtlog("md5: " + this.md5Encode(return_seccode));
                return return_seccode.equals(this.md5Encode(seccode)) ? 1 : 0;
            } catch (JSONException var13) {
                this.gtlog("json load error");
                return 0;
            }
        }
    }

    public int failbackValidateRequest(String challenge, String validate, String seccode) {
        this.gtlog("in failback validate");
        if (!this.resquestIsLegal(challenge, validate, seccode)) {
            return 0;
        } else {
            this.gtlog("request legitimate");
            return 1;
        }
    }

    public void gtlog(String message) {
        if (this.debugCode) {
            System.out.println("gtlog: " + message);
        }

    }

    protected boolean checkResultByPrivate(String challenge, String validate) {
        String encodeStr = this.md5Encode(this.privateKey + "geetest" + challenge);
        return validate.equals(encodeStr);
    }

    private String readContentFromGet(String URL) throws IOException {
        URL getUrl = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection)getUrl.openConnection();
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(2000);
        connection.connect();
        if (connection.getResponseCode() != 200) {
            return "fail";
        } else {
            StringBuffer sBuffer = new StringBuffer();
            InputStream inStream = null;
            byte[] buf = new byte[1024];
            inStream = connection.getInputStream();

            int n;
            while((n = inStream.read(buf)) != -1) {
                sBuffer.append(new String(buf, 0, n, "UTF-8"));
            }

            inStream.close();
            connection.disconnect();
            return sBuffer.toString();
        }
    }

    private String readContentFromPost(String URL, String data) throws IOException {
        this.gtlog(data);
        URL postUrl = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection)postUrl.openConnection();
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(2000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.connect();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
        outputStreamWriter.write(data);
        outputStreamWriter.flush();
        outputStreamWriter.close();
        if (connection.getResponseCode() != 200) {
            return "fail";
        } else {
            StringBuffer sBuffer = new StringBuffer();
            InputStream inStream = null;
            byte[] buf = new byte[1024];
            inStream = connection.getInputStream();

            int n;
            while((n = inStream.read(buf)) != -1) {
                sBuffer.append(new String(buf, 0, n, "UTF-8"));
            }

            inStream.close();
            connection.disconnect();
            return sBuffer.toString();
        }
    }

    private String md5Encode(String plainText) {
        String re_md5 = new String();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");

            for(int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
        }

        return re_md5;
    }
}
