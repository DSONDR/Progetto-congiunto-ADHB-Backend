package it.unife.sample.backend.service;

import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QrCodeService {

    // Genera un'immagine PNG del codice QR a partire dal testo fornito.
    // Il backend non conserva l'immagine in DB: genera l'immagine al volo quando richiesta.
    public byte[] generateQrCodeImage(String text, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN, 1);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
                return outputStream.toByteArray();
            }
        } catch (WriterException | java.io.IOException e) {
            throw new IllegalStateException("Impossibile generare il QR code", e);
        }
    }
}
