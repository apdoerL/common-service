package org.apdoer.common.service.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apdoer.common.service.model.bo.QrCodeGenWrapper;
import org.apdoer.common.service.model.bo.QrCodeConfig;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenUtil {
	public static BufferedImage genQrCodeWithNoPadding(String content, Integer size) throws WriterException, IOException {
		// 简单的生成
        QrCodeConfig qrCodeConfig = QrCodeGenWrapper.createQrCodeConfig()
                .setMsg(content)
                .setH(size)
                .setW(size)
                .setPadding(0)
                .build();
        return QrCodeGenWrapper.asBufferedImage(qrCodeConfig);
	}
	
	public static BufferedImage genQrCodeWithPadding(String content, Integer size) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, Object> hints = new HashMap<>( 3 );
		hints.put( EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H );
		hints.put( EncodeHintType.CHARACTER_SET, "UTF-8" );
		hints.put( EncodeHintType.MARGIN, 0 );
		BitMatrix bitMatrix = qrCodeWriter.encode( content, BarcodeFormat.QR_CODE, size, size, hints );
		return MatrixToImageWriter.toBufferedImage( bitMatrix );
	}

}
