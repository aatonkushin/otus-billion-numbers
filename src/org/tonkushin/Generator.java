package org.tonkushin;

import java.io.*;

/**
 * Вспомогательный класс для генерации файла с числами, а также для его чтения
 */
public class Generator {
    private final String filename;
    private final int max;
    private final int length;

    public Generator(String filename, int max, int length) {
        this.filename = filename;
        this.max = max;
        this.length = length;
    }

    private void generate() {
        int[] array = new int[length];

        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * max);
        }

        save(array);
    }

    /**
     * Возвращает массив чисел из сгенерированного файла
     * @return массив чисел из сгенерированного файла
     */
    public int[] getArray() {
        File file = new File(filename);

        if (!file.exists()) {
            generate();
        }

        return load();
    }

    private void save(int[] array) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(array);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] load() {
        try (FileInputStream fis = new FileInputStream(filename); ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (int[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Error while reading file");
    }
}
