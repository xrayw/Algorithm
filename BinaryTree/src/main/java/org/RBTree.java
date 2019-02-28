package org;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class RBTree<K, V extends Comparable> {

  private Node<K, V> root;

  public boolean add(K key, V value) {
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);

    // 新插入节点为红色
    Node<K, V> node = new Node<>(key, value, true);
    if (root == null) {
      root = node;
      node.setRed(false);    // 根节点为黑色
      return true;
    }

    Node<K, V> parent = this.findInsertParentNode(value);
    int x = value.compareTo(parent.value);
    if (x == 0) {
      return false;   // 已经存在

    }

    node.parent = parent;
    if (x < 0) {
      // left
      parent.left = node;
    } else {
      parent.right = node;
    }

    // 修复由于插入导致的不平衡
    this.fixInsert(node);
    return true;
  }

  /**
   * 当父节点是黑色的时候不用处理
   */
  private void fixInsert(Node<K, V> node) {
    Node<K, V> parent = node.getParent();
    if (parent != null && parent.isRed()) {

    }
  }

  private Node<K, V> getUncle(Node<K, V> node) {
    Node<K, V> parent = node.getParent();
    Node<K, V> ancestor = parent.getParent();
    if (ancestor == null) {
      return null;
    }

    if (ancestor.left == parent) {
      return ancestor.right;
    }
    return ancestor.left;
  }

  private Node<K, V> findInsertParentNode(V v) {
    Node<K, V> parent = null;
    Node<K, V> cur = this.root;
    while (cur != null) {
      int x = v.compareTo(cur.value);
      parent = cur;
      if (x == 0) {
        return cur;
      } else if (x > 0) {
        cur = cur.right;
      } else {
        cur = cur.left;
      }
    }

    return parent;
  }

  @Getter
  @Setter
  private static class Node<K, V extends Comparable> {
    K key;
    V value;
    Node<K, V> left;
    Node<K, V> right;
    Node<K, V> parent;

    private boolean red;    // red or not

    public Node(K key, V value) {
      this.key = key;
      this.value = value;
    }

    public Node(K key, V value, boolean red) {
      this.key = key;
      this.value = value;
      this.red = red;
    }
  }
}
