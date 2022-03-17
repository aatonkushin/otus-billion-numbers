package org.tonkushin;

import java.io.FileNotFoundException;

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
            fileArray = new FileArray(filename, true);
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
        return load();
    }

    private int[] load() {
        int[] retVal = new int[length];

        try (FileArray fileArray = new FileArray(filename, false)) {
            for (int i = 0; i < length; i++) {
                retVal[i] = fileArray.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }
}
