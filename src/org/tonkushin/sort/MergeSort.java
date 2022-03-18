package org.tonkushin.sort;

/**
 * Сортировка слиянием
 */
public class MergeSort implements Sort {
    int[] arr;

    public void sort(int[] arr) {
        this.arr = arr;
        sort(0, this.arr.length - 1);
    }

    private void sort(int l, int r) {
        if (l >= r) {
            return;
        }

        int x = (l + r) / 2;

        sort(l, x);
        sort(x + 1, r);
        merge(l, x, r);
    }

    private void merge(int l, int x, int r) {
        int[] M = new int[r - l + 1];
        int a = l;
        int b = x + 1;
        int m = 0;

        while (a <= x && b <= r) {
            if (arr[a] <= arr[b]) {
                M[m++] = arr[a++];
            } else {
                M[m++] = arr[b++];
            }
        }

        while (a <= x) {
            M[m++] = arr[a++];
        }

        while (b <= r) {
            M[m++] = arr[b++];
        }

        for (int i = l; i <= r; i++) {
            arr[i] = M[i - l];
        }
    }


}
