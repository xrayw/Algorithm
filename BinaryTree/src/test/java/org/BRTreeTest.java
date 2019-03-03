package org;

import org.RBTree.Node;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BRTreeTest {
  @Test
  public void testRbtree() {
    RBTree<Integer, Integer> rbtree = new RBTree<>();
    for (int i = 0; i < 15; i++) {
      rbtree.add(i, i);
    }

    levelTrace(rbtree).forEach(System.out::println);
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
