package org.tonkushin;

import org.tonkushin.fileSort.FileSort;
import org.tonkushin.fileSort.FileSortImpl;
import org.tonkushin.sort.*;

public class Main {
    private static final Stopwatch sw = new Stopwatch();

    private static final String dir = "d:\\temp\\otus\\9\\";
    private static final String filename = dir + "%d.bin";
    private static final int max = 65535;

    public static void main(String[] args) {
        // Генерируем файлы
        for (int i = 2; i <= 9; i++) {
            int length = (int) Math.pow(10, i);
            Generator generator = new Generator(String.format(filename, length), max, length);
            generator.generate();
        }

        // Сортировка в памяти
        for (int i = 2; i < 9; i++) {
            int length = (int) Math.pow(10, i);
            Generator generator = new Generator(String.format(filename, length), max, length);

            process("QuickSort ", new QuickSort(), generator.getArray());
            process("MergeSort ", new MergeSort(), generator.getArray());
            process("BucketSort ", new BucketSort(), generator.getArray());
            process("CountingSort ", new CountingSort(), generator.getArray());
            process("RadixSort ", new RadixSort(), generator.getArray());
        }

        // Сортировка через файлы
        for (int i = 2; i <= 9; i++) {
            int length = (int) Math.pow(10, i);
            processFileArray("File array QuickSort", new QuickSort(), length);
            processFileArray("File array MergeSort", new MergeSort(), length);
            processFileArray("File array BucketSort", new BucketSort(), length);
            processFileArray("File array CountingSort", new CountingSort(), length);
            processFileArray("File array RadixSort", new RadixSort(), length);
            System.out.println();
        }
    }

    private static void process(String name, Sort sort, int[] arr) {
        System.out.println(name + " (" + arr.length + "): ");
        sw.start();
        sort.sort(arr);
        sw.stop();
        System.out.println(sw + ", " + sort.checkSortedArray(arr));
    }

    private static void processFileArray(String name, Sort sort, int length) {
        System.out.println(name + " (" + length + "): ");
        FileSort fs = new FileSortImpl(dir, length);
        sw.start();
        fs.sort(sort);
        sw.stop();
        System.out.println(sw + ", " + fs.checkSortedArray());
    }
}
