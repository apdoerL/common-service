package org.apdoer.common.service.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by William on 2018/4/2.
 */
@Slf4j
public class InviteImgUtil {

    /**
     * 根据邀请链接生成海报图片
     *
     * @param inviteUrl
     * @param fileTempUrl
     * @param locale
     * @return
     * @throws IOException
     */
    public static BufferedImage genQRImg4InviteUrl(String inviteUrl, URL fileTempUrl, Locale locale)
            throws IOException {
        Image qrimg = null;
        try {
            qrimg = QRCodeGenUtil.genQrCodeWithNoPadding(inviteUrl, 160 );
        } catch (Exception e) {
            log.error("邀请码二维码生成失败", e);
            return null;
        }
        Image imageTemp = null;
        try {
            imageTemp = ImageIO.read(fileTempUrl);
        } catch (Exception e) {
            log.error("图片模版读取失败", e);
            return null;
        }
        BufferedImage bufferedImage = new BufferedImage(imageTemp.getWidth(null), imageTemp.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(
                imageTemp.getScaledInstance(imageTemp.getWidth(null), imageTemp.getHeight(null), Image.SCALE_SMOOTH), 0,
                0, null);
        graphics.setColor(Color.BLACK);
        if (Locale.CHINA.getLanguage().equalsIgnoreCase(locale.getLanguage())) {
            graphics.drawImage(qrimg, (int) (bufferedImage.getWidth() / 2.54), (int) (bufferedImage.getHeight() / 1.34),
                    null);
        } else {
            graphics.drawImage(qrimg, (int) (bufferedImage.getWidth() / 2.54), (int) (bufferedImage.getHeight() / 1.34),
                    null);
        }
        graphics.dispose();
        return bufferedImage;
    }

//    public static void main(String[] args) throws IOException {
//        final String FILE_TEMP = "file:d:\\act_zh_CN.png";
//        ClassLoader classLoader = InviteImgUtil.class.getClassLoader();
//        URL fileTempUrl = new URL(FILE_TEMP);
//        // 中文
//        //fileTempUrl = classLoader.getResource(FILE_TEMP);
//        String url = "http://192.168.16.17:8085/#/share/shareRegister?inviteCode=VDVT";
//        BufferedImage bi = InviteImgUtil.genQRImg4InviteUrl(url, fileTempUrl, Locale.CHINA);
//        File f = new File("d:\\1.jpg");
//        f.createNewFile();
//        ImageIO.write(bi, "jpg", f);
//    }
}
