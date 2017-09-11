package org.dbrouter;

/**
 * 根据数据库秒级扩容的思路实现的重新计算扩容后的数据库路由算法
 * <a href="http://www.cnblogs.com/firstdream/p/7090524.html">数据库秒级扩容</a>
 */
public class ReRouter {
  public int[] reRouter(int n) {
    if (n < 2 && (n & (n - 1)) != 0) {
      throw new IllegalArgumentException();
    }

    int[] result = {0, 1};
    if (n == 2) {
      return result;
    }

    int[] temp;
    int lastCapacity = 2;
    int capacity = 4;

    while (capacity <= n) {
      temp = new int[capacity];
      for (int i = 0; i < lastCapacity; i++) {
        int index = 2 * i;
        int v = result[i];
        temp[index] = v;
        temp[index + 1] = v + lastCapacity;
      }

      result = temp;
      lastCapacity = capacity;
      capacity <<= 1;
    }

    return result;
  }
}
