package org.tonkushin.sort;

public class RadixSort implements Sort {
    @Override
    public void sort(int[] arr) {
        // Ищем элемент с максимальным значением
        int max = 0;
        for (int val : arr) {
            if (max < val) {
                max = val;
            }
        }

        for (int place = 1; max / place > 0; place *= 10)
            sort(arr, place);
    }

    void sort(int[] arr, int place) {
        int max = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max)
                max = arr[i];
        }

        // Создаём вспомогательный массив, длиной max+1
        int[] countArray = new int[max + 1];

        for (int i = 0; i < max; ++i)
            countArray[i] = 0;

        // Подсчёт кол-ва элементов
        for (int j : arr) countArray[(j / place) % 10]++;

        // Накопительный подсчёт
        for (int i = 1; i < 10; i++)
            countArray[i] += countArray[i - 1];

        // если массив умещается в памяти
        int[] output = new int[arr.length + 1];
        for (int i = arr.length - 1; i >= 0; i--) {
            output[countArray[(arr[i] / place) % 10] - 1] = arr[i];
            countArray[(arr[i] / place) % 10]--;
        }

        System.arraycopy(output, 0, arr, 0, arr.length);
    }
}
