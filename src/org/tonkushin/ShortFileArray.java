package org.tonkushin;

import java.io.*;

public class ShortFileArray implements FileArray {

    private final DataInputStream dis;
    private DataOutputStream dos;


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public ShortFileArray(String filename, boolean overwrite) throws FileNotFoundException {
        File file = new File(filename);

        if (file.exists() && overwrite) {
            file.delete();
        }

        if (!file.exists()) {
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
        }

        dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)));
    }

    @Override
    public void set(int value) throws IOException {
        dos.writeShort(value & 0xFFFF);
    }

    @Override
    public void set(int[] values) throws IOException {
        for (int value : values) {
            set(value);
        }
    }

    @Override
    public int get() throws IOException {
        return dis.readUnsignedShort();
    }

    @Override
    public boolean hasNext() throws IOException {
        return dis.available() > 0;
    }

    @Override
    public int[] getArray() throws IOException {
        int[] retVal = new int[dis.available() / 2];

        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = get();
        }

        return retVal;
    }

    @Override
    public void close() throws Exception {
        if (dis != null) {
            dis.close();
        }

        if (dos != null) {
            dos.close();
        }
    }
}
