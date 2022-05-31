package bg.sofia.uni.fmi.mjt.foodanalyzer.server.decoder;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class QRDecoder {

    private static final String NO_QR_CODE_FOUND_MESSAGE = "There is no QR code in the image";
    private static final String NOT_EXISTING_FILE_MESSAGE = "File %s does not exist";
    private static final String IMAGE_READING_ERROR_MESSAGE = "Problem occurred while reading file %s";
    private static final String PATH_CANNOT_BE_NULL_MESSAGE = "Path cannot be null or empty";

    private static String decodeQRCode(File image) {

        try {
            BufferedImage bufferedImage = ImageIO.read(image);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return NO_QR_CODE_FOUND_MESSAGE;
        } catch (IOException e) {
            throw new IllegalStateException(String.format(IMAGE_READING_ERROR_MESSAGE, image), e);
        }
    }

    public static String getQRCode(String path) throws FileNotFoundException {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException(PATH_CANNOT_BE_NULL_MESSAGE);
        }

        File file = new File(Path.of(path).toAbsolutePath().toString());

        if (!file.exists()) {
            throw new FileNotFoundException(String.format(NOT_EXISTING_FILE_MESSAGE, path));
        }

        return decodeQRCode(file);
    }

}
