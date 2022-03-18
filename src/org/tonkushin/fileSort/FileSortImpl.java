package org.tonkushin.fileSort;

import org.tonkushin.FileArray;
import org.tonkushin.ShortFileArray;
import org.tonkushin.sort.Sort;

import java.io.File;
import java.util.Arrays;

public class FileSortImpl implements FileSort {
    private final String dir;
    private final String filename;
    private final String tmpFilename;
    private final int length;
    private static final int parts = 4;
    private String resultFileName;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FileSortImpl(String dir, int length) {
        this.dir = dir;
        this.filename = dir + "%d.bin";
        this.tmpFilename = dir + "tmp%d.bin";
        this.length = length;

        for (File t : getTempFiles()) {
            t.delete();
        }
    }

    @Override
    public void sort(Sort sort) {
//        printArray(String.format(filename, length), length);

        int partLength = length / parts;
        int rest = length - parts * partLength;

        // Чтение исходного файла и запись частей во временные файлы
        try (FileArray initialArray = new ShortFileArray(String.format(filename, length), false)) {
            for (int i = 0; i < parts; i++) {
                try (FileArray tempFileArray = new ShortFileArray(String.format(tmpFilename, i), true)) {
                    int tempPos = 0;
                    // i - номер части

                    int[] buf = new int[partLength];
                    int len = i * partLength + partLength;
                    for (int j = i * partLength; j < len; j++) {
                        // j - указатель внутри части
                        int value = initialArray.get();
                        buf[tempPos++] = value;
                    }

                    sort.sort(buf);

                    tempFileArray.set(buf);       // запись во временный файл
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (rest > 0) {
                try (FileArray tempFileArray = new ShortFileArray(String.format(tmpFilename, parts + 1), true)) {
                    int tempPos = 0;
                    // i - номер части

                    int[] buf = new int[rest];
                    int len = parts * partLength + rest;
                    for (int j = parts * partLength; j < len; j++) {
                        // j - указатель внутри части
                        int value = initialArray.get();
                        buf[tempPos++] = value;
                    }

                    sort.sort(buf);

                    tempFileArray.set(buf);       // запись во временный файл
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Слияние файлов
        File[] tmpFiles = getTempFiles();
        while (tmpFiles.length > 1) {
            for (int i = 1; i < tmpFiles.length; i += 2) {
                resultFileName = mergeFiles(tmpFiles[i - 1], tmpFiles[i]);
            }
            tmpFiles = getTempFiles();
        }

//        printArray(resultFileName, length);
    }

    @Override
    public String checkSortedArray() {
        try (FileArray result = new ShortFileArray(resultFileName, false)) {
            int prev = result.get();

            for (int i = 1; i < length; i++) {
                int current = result.get();

                if (prev > current) {
                    return "CHECK FAILED";
                }
            }

            return "CHECK OK";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "CHECK FAILED";
    }

    private File[] getTempFiles() {
        File directory = new File(dir);
        if (!directory.isDirectory()) throw new IllegalStateException("Not a directory");

        return directory.listFiles((file, s) -> s.startsWith("tmp"));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String mergeFiles(File f1, File f2) {
        String p1 = f1.getName().substring(f1.getName().indexOf("tmp") + 3, f1.getName().indexOf(".bin"));
        String resultFileName = dir + String.format("tmp_0_%s.bin", p1);

        try (FileArray array1 = new ShortFileArray(f1.getAbsolutePath(), false);
             FileArray array2 = new ShortFileArray(f2.getAbsolutePath(), false);
             FileArray result = new ShortFileArray(resultFileName, true)) {

            int v1 = -1;
            int v2 = -1;
            // Общий случай
            while (array1.hasNext() && array2.hasNext()) {
                if (v1 == -1) {
                    v1 = array1.get();
                }

                if (v2 == -1) {
                    v2 = array2.get();
                }

                if (v1 < v2) {
                    result.set(v1);
                    v1 = -1;
                } else if (v1 > v2) {
                    result.set(v2);
                    v2 = -1;
                } else {
                    result.set(v1);
                    v1 = -1;
                    result.set(v2);
                    v2 = -1;
                }
            }

            // Когда в первом массиве есть значения, но может остаться 1 значение от второго массива
            while (array1.hasNext()) {
                if (v1 == -1) {
                    v1 = array1.get();
                }

                if (v2 != -1 && v2 < v1) {
                    result.set(v2);
                    v2 = -1;
                }

                result.set(v1);
                v1 = -1;
            }

            // Когда во втором массиве есть значения, но может остаться одно значение от первого массива
            while (array2.hasNext()) {
                if (v2 == -1) {
                    v2 = array2.get();
                }

                if (v1 != -1 && v1 <= v2) {
                    result.set(v1);
                    v1 = -1;
                }

                result.set(v2);
                v2 = -1;
            }

            // Остаток от первого массива
            if (v1 != -1) {
                result.set(v1);
            }

            // Остаток от второго массива
            if (v2 != -1) {
                result.set(v2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        f1.delete();
        f2.delete();

        return resultFileName;
    }

    private void printArray(String fn, int len) {
        try (FileArray fa = new ShortFileArray(String.format(fn, len), false)) {
            System.out.println(Arrays.toString(fa.getArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
