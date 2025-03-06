package ua.ellka.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class Base64ConvertorTest {
    private final Base64Convertor base64Convertor = new Base64Convertor();

    @Test
    void encodeToStringTest_returnString() throws IOException {
        String string = "testPassword\n";
        String expected = "dGVzdFBhc3N3b3JkCg==";
        InputStream inputStream = new ByteArrayInputStream(string.getBytes());

        String actual = base64Convertor.encodeToString(inputStream);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    void encodeToStringTest_returnException() throws IOException {
        String string = "testPassword\n";
        InputStream inputStream = new ByteArrayInputStream(string.getBytes()) {
            @SneakyThrows
            @Override
            public int read(byte[] b, int off, int len) {
                throw new IOException();
            }
        };

        assertThrows(IOException.class, () -> base64Convertor.encodeToString(inputStream));
    }

    @Test
    void encodeToBytesTest_returnArray() throws IOException {
        String string = "testPassword\n";
        String expected = "dGVzdFBhc3N3b3JkCg==";
        InputStream inputStream = new ByteArrayInputStream(string.getBytes());

        byte[] actual = base64Convertor.encodeToBytes(inputStream);

        assertNotNull(actual);
        assertNotEquals(0, actual.length);
        assertArrayEquals(expected.getBytes(), actual);
    }

    @Test
    void encodeToBytesTest_returnException() throws IOException {
        String string = "testPassword\n";
        InputStream inputStream = new ByteArrayInputStream(string.getBytes()) {
            @SneakyThrows
            @Override
            public int read(byte[] b, int off, int len) {
                throw new IOException();
            }
        };

        assertThrows(IOException.class, () -> base64Convertor.encodeToBytes(inputStream));
    }

}