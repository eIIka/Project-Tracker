package ua.ellka.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Base64Convertor {

    public String encodeToString(InputStream inputStream)throws IOException {
        byte[] buffer = readBytesFromInputStream(inputStream);
        return Base64.getEncoder().encodeToString(buffer);
    }

    public byte[] encodeToBytes(InputStream inputStream)throws IOException {
        byte[] buffer = readBytesFromInputStream(inputStream);
        return Base64.getEncoder().encode(buffer);
    }

    private byte[] readBytesFromInputStream(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toByteArray();

        }
    }
}
