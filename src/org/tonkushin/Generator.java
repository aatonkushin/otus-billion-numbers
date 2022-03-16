package org.tonkushin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Вспомогательный класс для генерации файла с числами, а также для его чтения
 */
public class Generator {
    private final String filename;
    private final int max;
    private final int length;
    private FileArray fileArray;

    public Generator(String filename, int max, int length) {
        this.filename = filename;
        this.max = max;
        this.length = length;

        try {
            fileArray = new FileArray(filename, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void generate() {
        try {
            for (int i = 0; i < length; i++) {
                fileArray.set((int) (Math.random() * max));
            }
            fileArray.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает массив чисел из сгенерированного файла
     *
     * @return массив чисел из сгенерированного файла
     */
    public int[] getArray() {
        File file = new File(filename);

        if (!file.exists()) {
            generate();
        }

        return load();
    }

    private int[] load() {
        int[] retVal = new int[length];

        try {
            for (int i = 0; i < length; i++) {
                retVal[i] = fileArray.get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }
}
