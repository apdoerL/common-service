package org.apdoer.common.service.model.bo;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import org.apdoer.common.service.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yihui on 2017/4/7.
 */
public class QrCodeReaderWrapper {

	/**
	 * 读取二维码中的内容, 并返回
	 *
	 * @param qrcodeImg
	 *            二维码图片的地址
	 * @return 返回二维码的内容
	 * @throws IOException
	 *             读取二维码失败
	 * @throws FormatException
	 *             二维码解析失败
	 * @throws ChecksumException
	 * @throws NotFoundException
	 */
	public static String decode(String qrcodeImg) throws IOException, FormatException, ChecksumException, NotFoundException {
		BufferedImage image = ImageUtil.getImageByPath( qrcodeImg );
		if (image == null) {
			throw new IllegalStateException( "can not load qrCode!" );
		}

		LuminanceSource source = new BufferedImageLuminanceSource( image );
		BinaryBitmap bitmap = new BinaryBitmap( new HybridBinarizer( source ) );
		QRCodeReader qrCodeReader = new QRCodeReader();
		Result result = qrCodeReader.decode( bitmap );
		return result.getText();
	}
}