package org.tonkushin.sort;

public class CountingSort implements Sort {
    @Override
    public void sort(int[] arr) {
        // Ищем элемент с максимальным значением
        int max = 0;
        for (int val : arr) {
            if (max < val) {
                max = val;
            }
        }

        // Создаём вспомогательный массив, длиной max+1
        int[] countArray = new int[max + 1];

        // Подсчитываем кол-во вхождений каждого числа и записываем в соответсвующий индекс
        for (int j : arr) {
            countArray[j]++;
        }

        // Накопительный подсчёт
        for (int i = 1; i < countArray.length; i++) {
            countArray[i] += countArray[i - 1];
        }

        // Вспомогательный отсортированный массив
        int[] out = new int[arr.length];

        for (int i = arr.length - 1; i >= 0; i--) {
            int pos = --countArray[arr[i]];
            out[pos] = arr[i];
        }

        // Перенос в исходный массив
        System.arraycopy(out, 0, arr, 0, arr.length);
    }
}
