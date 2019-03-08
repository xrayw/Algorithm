package org;

import java.util.concurrent.ThreadLocalRandom;
import org.RBTree.Node;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BRTreeTest {

  @Test
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
