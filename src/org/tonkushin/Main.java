package org.tonkushin;

import org.tonkushin.sort.*;

public class Main {
    private static final Stopwatch sw = new Stopwatch();

    private static final String filename = "d:\\temp\\otus\\9\\%d.bin";
    private static final int max = 65535;

    public static void main(String[] args) {

        for (int i = 2; i < 9; i++) {
            int length = (int) Math.pow(10, i);
            Generator generator = new Generator(String.format(filename, length), max, length);

            process("QuickSort ", new QuickSort(), generator.getArray());
            process("MergeSort ", new MergeSort(), generator.getArray());
            process("BucketSort ", new BucketSort(), generator.getArray());
            process("CountingSort ", new CountingSort(), generator.getArray());
            process("RadixSort ", new RadixSort(), generator.getArray());
        }
    }

    private static void process(String name, Sort sort, int[] arr) {
        System.out.println(name + " (" + arr.length + "): ");
        sw.start();
        sort.sort(arr);
        sw.stop();
        System.out.println(sw + ", " + sort.checkSortedArray(arr));
    }
}
