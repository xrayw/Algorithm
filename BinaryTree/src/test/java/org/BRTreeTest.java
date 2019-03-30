package org;

import org.RBTree.Node;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class BRTreeTest {

  @Test
  @Ignore
  public void testRbtree() {
    RBTree<Integer, Integer> rbtree = new RBTree<>();
    ThreadLocalRandom current = ThreadLocalRandom.current();
    for (int i = 0; i < 1000000; i++) {
      rbtree.add(current.nextInt(0, Integer.MAX_VALUE), current.nextInt(0, Integer.MAX_VALUE));
    }

    levelTrace(rbtree).forEach(System.out::println);

    // levelTrace(rbtree).forEach(System.out::println);
    // for (int i = 0; i < 15; i++) {
    //   System.out.println(rbtree.removeV2(14 - i));
    // }
    // System.out.println(rbtree.remove(3));
    //
    // levelTrace(rbtree).forEach(System.out::println);

    for (int i = 0; i < 1000000000; i++) {
      System.out.println(rbtree.removeV2(current.nextInt(0, Integer.MAX_VALUE)));
    }
  }

  @Test
  @Ignore
  public void testR() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(2);

    BRTreeTest lock = this;

    new Thread() {
      @Override
      public void run() {
        for (int i = 1; i < 100; i += 2) {
          synchronized (lock) {
            System.out.println(i);

            try {
              lock.notify();
              if (i != 99) {
                lock.wait();
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }

        latch.countDown();
      }
    }.start();

    new Thread() {
      @Override
      public void run() {
        for (int i = 2; i < 100; i += 2) {
          synchronized (lock) {
            System.out.println(i);
            try {
              lock.notify();
              if (i != 98) {
                lock.wait();
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }

        latch.countDown();
      }
    }.start();


    latch.await();
  }


  private static <K extends Comparable<K>, V> List<List<V>> levelTrace(RBTree<K, V> tree) {
    Node<K, V> root = tree.getRoot();
    List<List<V>> res = new ArrayList<>();
    Deque<Node<K, V>> dq = new ArrayDeque<>();
    dq.offerLast(root);
    while (!dq.isEmpty()) {
      int len = dq.size();
      List<V> level = new ArrayList<>(len);
      for (int i = 0; i < len; i++) {
        Node<K, V> n = dq.pollFirst();
        level.add(n.value);

        if (n.left != null) {
          dq.offerLast(n.left);
        }
        if (n.right != null) {
          dq.offerLast(n.right);
        }
      }
      res.add(level);
    }
    return res;
  }
}
