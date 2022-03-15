package org.tonkushin.sort;

/**
 * Блочная сортировка
 */
public class BucketSort implements Sort {

    public void sort(int[] arr) {
        if (arr.length > (int) Math.pow(10, 8)) {
            System.out.println("Skip");
            return;
        }

        // Ищем элемент с максимальным значением
        int max = 0;
        for (int val : arr) {
            if (max < val) {
                max = val;
            }
        }

        // Создаём массив с корзинами
        SortedVector[] buckets = new SortedVector[arr.length];

        // Распределяем элементы в корзинах
        for (int value : arr) {
            // Ai*N / (max + 1)
            int pos = (int) (((long) value * arr.length) / (max + 1));

            if (buckets[pos] == null) {
                buckets[pos] = new SortedVector();
            }

            buckets[pos].add(value);
        }

        // Записываем значения в отсортированный массив
        int pos = 0;

        for (SortedVector bucket : buckets) {
            if (bucket != null) {
                for (int j = 0; j < bucket.size(); j++) {
                    arr[pos] = bucket.get(j);
                    pos++;
                }
            }
        }
    }

    /**
     * Сортированный список.
     * Добавляемый элемент всегда вставляется в нужное место, таким образом, список всегда отсортирован.
     */
    private static class SortedVector {
        int[] arr;
        private final int vector = 10;
        private int size = 0;

        public SortedVector() {
            this.arr = new int[vector];
        }

        public int get(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }

            return this.arr[index];
        }

        public void add(int item) {
            if (size == this.arr.length) {
                resize();
            }

            for (int i = 0; i < size; i++) {
                if (item <= this.arr[i]) {
                    this.add(item, i);
                    return;
                }
            }

            this.arr[size] = item;
            size++;
        }

        public int size() {
            return size;
        }

        private void add(int item, int index) {
            if (size == this.arr.length) {
                //  Если массив нужно расширить
                int[] newArray = new int[size + vector];

                if (size != 0) {
                    System.arraycopy(this.arr, 0, newArray, 0, index);
                }

                // Устанавливаем элемент в указанном индексе
                newArray[index] = item;

                // Копируем от индекса
                System.arraycopy(this.arr, index, newArray, index + 1, size - index);

                arr = newArray;
            } else {
                // в рамках существующего массива
                if (size - index >= 0) {
                    System.arraycopy(this.arr, index, this.arr, index + 1, size - index);
                }

                arr[index] = item;
            }

            size++;
        }

        private void resize() {
            int[] newArray = new int[size + vector];
            if (size != 0) {
                System.arraycopy(this.arr, 0, newArray, 0, size);
            }

            this.arr = newArray;
        }
    }
}
