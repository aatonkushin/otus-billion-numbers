package org.tonkushin;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Массив в файле
 */
public class FileArray implements AutoCloseable {
    private static final int bufferSize = 8192;

    private final FileInputStream fis;
    private FileOutputStream fos;

    private final BufferedInputStream bis;
    private BufferedOutputStream bos;

    public FileArray(String filename, boolean overwrite) throws FileNotFoundException {
        File file = new File(filename);

        if (file.exists() && overwrite) {
            file.delete();
        }

        if (!file.exists()) {
            fos = new FileOutputStream(filename);
            bos = new BufferedOutputStream(fos, bufferSize);
        }

        fis = new FileInputStream(filename);
        bis = new BufferedInputStream(fis, bufferSize);
    }

    public void set(int value) throws IOException {
        bos.write(toBytes(value), 0, 4);
    }

    public void set(int[] values) throws IOException {
        for (int value : values) {
            set(value);
        }
    }

    public int get() throws IOException {
        return toInt(bis.readNBytes(4));
    }

    private byte[] toBytes(int value) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
    }

    private int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    @Override
    public void close() throws Exception {
        bis.close();
        fis.close();

        if (bos != null && fos != null) {
            bos.close();
            fos.close();
        }
    }

    public boolean hasNext() throws IOException {
        return bis.available() >= 4;
    }

    /**
     * Возвращает массив чисел из сгенерированного файла
     *
     * @return массив чисел из сгенерированного файла
     */
    public int[] getArray() throws IOException {
        byte[] bytes = bis.readNBytes(bis.available());
        int[] retVal = new int[bytes.length / 4];

        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = ByteBuffer.wrap(bytes, 4 * i, 4).getInt();
        }

        return retVal;
    }
}
