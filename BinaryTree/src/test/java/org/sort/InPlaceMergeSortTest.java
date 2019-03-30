package org.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class InPlaceMergeSortTest {
  @Test
  public void testInplaceMergesort() {
    InPlaceMergeSort sort = new InPlaceMergeSort();

    int max = 10000;
    int[] arr = new int[max];
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < max; i++) {
      arr[i] = random.nextInt(0, max);
    }

    // arr = new int[]{10, 30, 50, 60, 80, 20, 25, 55, 65, 73};

    int[] clone = arr.clone();
    Arrays.sort(clone);
    String cloneOutput = Arrays.toString(clone);
    System.out.println(cloneOutput);

    sort.mergesort(arr, 0, arr.length - 1);
    String arrOutput = Arrays.toString(arr);
    System.out.println(arrOutput);

    assertThat(cloneOutput, is(arrOutput));
  }
}
