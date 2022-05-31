package bg.sofia.uni.fmi.mjt.foodanalyzer.server.decoder;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QRDecoderTest {

    private static final String BARCODE = "009800146130";

    @Test
    public void testGetQRCodeWithNullPathArgument() {
        String message = "IllegalArgumentException expected when path argument is null in method getQRCode";
        assertThrows(IllegalArgumentException.class, () -> QRDecoder.getQRCode(null), message);
    }

    @Test
    public void testGetQRCodeWhenFileDoesNotExist() {
        String message = "FileNotFoundException expected when the file provided in the path does not exist";
        String path = "noSuchFile";
        assertThrows(FileNotFoundException.class, () -> QRDecoder.getQRCode(path), message);
    }

    @Test
    public void testGetQRCodeSuccess() throws FileNotFoundException {
        String message = "Method getQRCode does not return the correct barcode when image is valid";
        String[] tokens = new String[]{"resources", "images", "barcode.gif"};
        String path = String.join(File.separator, tokens);

        assertEquals(BARCODE, QRDecoder.getQRCode(path), message);
    }

}