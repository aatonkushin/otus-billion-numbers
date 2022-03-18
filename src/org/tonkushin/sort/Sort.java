package org.tonkushin.sort;

public interface Sort {
    void sort(int[] arr);

    /**
     * Проверяет отсортированный массив
     * @return CHECK OK - если массив отсортирован по возрастанию, иначе - CHECK FAILED
     */
    default String checkSortedArray(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return "CHECK FAILED";
            }
        }

        return "CHECK OK";
    }
}
