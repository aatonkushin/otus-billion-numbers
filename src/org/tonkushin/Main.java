package org.tonkushin;

import org.tonkushin.sort.MergeSort;
import org.tonkushin.sort.Sort;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    private static final Stopwatch sw = new Stopwatch();

    private static final String dir = "d:\\temp\\otus\\9\\";
    private static final String filename = dir + "%d.bin";
    private static final String tmpFilename = dir + "tmp%d.bin";
    private static final int max = 65535;
    private static final int parts = 100;

    public static void main(String[] args) {
        for (File t : getTempFiles()) {
            t.delete();
        }
        int length = (int) Math.pow(10, 8);
//        int length = (int) Math.pow(10, 2);
        String f = String.format(filename, length);

        File file = new File(f);

        if (!file.exists()) {
            Generator generator = new Generator(String.format(filename, length), max, length);
            generator.generate();
        }

//        try (FileArray fa = new FileArray(String.format(filename, length), false)){
//            System.out.println(Arrays.toString(fa.getArray()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        int partLength = length / parts;
        int rest = length - parts * partLength;

        // Чтение исходного файла и запись частей во временные файлы
        try (FileArray initialArray = new FileArray(String.format(filename, length), false)) {
            for (int i = 0; i < parts; i++) {
                try (FileArray tempFileArray = new FileArray(String.format(tmpFilename, i), true)) {
                    int tempPos = 0;
                    // i - номер части

                    int[] buf = new int[partLength];
                    for (int j = i * partLength; j < i * partLength + partLength; j++) {
                        // j - указатель внутри части
                        int value = initialArray.get();
                        buf[tempPos++] = value;
                    }

                    Sort sort = new MergeSort();
                    sw.start();
                    sort.sort(buf);
                    sw.stop();
                    System.out.println("Sort " + buf.length + "  took: " + sw);

                    tempFileArray.set(buf);       // запись во временный файл
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (rest > 0) {
                try (FileArray tempFileArray = new FileArray(String.format(tmpFilename, parts + 1), true)) {
                    int tempPos = 0;
                    // i - номер части

                    int[] buf = new int[rest];
                    for (int j = parts * partLength; j < parts * partLength + rest; j++) {
                        // j - указатель внутри части
                        int value = initialArray.get();
                        buf[tempPos++] = value;
                    }

                    Sort sort = new MergeSort();
                    sw.start();
                    sort.sort(buf);
                    sw.stop();
                    System.out.println("Sort " + buf.length + "  took: " + sw);

                    tempFileArray.set(buf);       // запись во временный файл
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Слияние файлов
        String resultFileName = null;
        File[] tmpFiles = getTempFiles();
        while (tmpFiles.length > 1) {
            for (int i = 1; i < tmpFiles.length; i += 2) {
                sw.start();
                resultFileName = mergeFiles(tmpFiles[i - 1], tmpFiles[i]);
                sw.stop();
                System.out.println("Merge " + i + " took " + sw);
            }
            tmpFiles = getTempFiles();
        }

        try {
            FileArray result = new FileArray(resultFileName, false);
            int prev = result.get();

            for (int i = 1; i < length; i++) {
                int current = result.get();

                if (prev > current) {
                    System.out.println("CHECK FAILED");
                    return;
                }
            }

            System.out.println("CHECK OK");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        for (int i = 2; i < 9; i++) {
//            int length = (int) Math.pow(10, i);
//            Generator generator = new Generator(String.format(filename, length), max, length);
//
//            process("QuickSort ", new QuickSort(), generator.getArray());
//            process("MergeSort ", new MergeSort(), generator.getArray());
//            process("BucketSort ", new BucketSort(), generator.getArray());
//            process("CountingSort ", new CountingSort(), generator.getArray());
//            process("RadixSort ", new RadixSort(), generator.getArray());
//        }
    }

    private static void process(String name, Sort sort, int[] arr) {
        System.out.println(name + " (" + arr.length + "): ");
        sw.start();
        sort.sort(arr);
        sw.stop();
        System.out.println(sw + ", " + sort.checkSortedArray(arr));
    }

    private static File[] getTempFiles() {
        File directory = new File(dir);
        if (!directory.isDirectory()) throw new IllegalStateException("Not a directory");

        return directory.listFiles((file, s) -> s.startsWith("tmp"));
    }

    private static String mergeFiles(File f1, File f2) {
        String p1 = f1.getName().substring(f1.getName().indexOf("tmp") + 3, f1.getName().indexOf(".bin"));
        String resultFileName = dir + String.format("tmp_0_%s.bin", p1);

        try (FileArray array1 = new FileArray(f1.getAbsolutePath(), false);
             FileArray array2 = new FileArray(f2.getAbsolutePath(), false);
             FileArray result = new FileArray(resultFileName, true)) {

            int v1 = -1;
            int v2 = -1;
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

            if (v1 != -1) {
                result.set(v1);
            }

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
}
