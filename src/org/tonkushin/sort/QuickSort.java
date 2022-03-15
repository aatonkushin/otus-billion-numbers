package org.tonkushin.sort;

/**
 * Быстрая сортировка
 */
public class QuickSort implements Sort {
    int[] arr;

    public void sort(int[] arr) {

        if (arr.length > (int) Math.pow(10, 8)) {
            System.out.println("Skip");
            return;
        }

        this.arr = arr;

        sort(0, arr.length - 1);
    }

    private void sort(int l, int r) {
        if (l >= r) {
            return;
        }

        int x = split(l, r);
        sort(l, x - 1);
        sort(x + 1, r);
    }

    private int split(int l, int r) {
        int p = this.arr[r];
        int a = l - 1;

        for (int m = l; m <= r; m++) {
            if (arr[m] <= p) {
                swap(++a, m);
            }
        }

        return a;
    }

    private void swap(int x, int y) {
        int z = arr[x];
        arr[x] = arr[y];
        arr[y] = z;
    }
}
