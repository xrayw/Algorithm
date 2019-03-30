package org.sort;

/**
 * 原地归并排序算法
 */
public class InPlaceMergeSort {
  public void mergesort(int[] arr, int left, int right) {
    if (left < right) {
      int mid = left + (right - left) / 2;

      mergesort(arr, left, mid);
      mergesort(arr, mid + 1, right);
      inplaceMergearray(arr, left, mid, right);
    }
  }

  /**
   * 'abcdefghijkl', reverse 'abcdefg' and 'hijkl', then reverse all => 'hijklabcdefg'
   */
  private void inplaceMergearray(int[] arr, int left, int mid, int right) {
    int i, j;
    i = left;
    j = mid + 1;

    while (i < j && j <= right) {

      while (i < j && arr[i] <= arr[j]) {
        i++;
      }

      while (j <= right && arr[j] <= arr[i]) {
        j++;
      }

      msort(arr, i, mid, j);
      i += (j - mid - 1);
      mid = j-1;
    }
  }

  private void msort(int[] arr, int i, int mid, int j) {
    reverse(arr, i, mid);
    reverse(arr, mid+1, j-1);
    reverse(arr, i, j-1);
  }

  private void reverse(int[] arr, int i, int j) {
    while (i < j) {
      int t = arr[i];
      arr[i] = arr[j];
      arr[j] = t;

      i++;
      j--;
    }
  }
}
