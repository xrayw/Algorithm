package org;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * 参考美团技术团队关于红黑树的描述和实现
 */
public class RBTree<K extends Comparable<K>, V> {

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

    Node<K, V> parent = this.findInsertParentNode(key);
    int x = key.compareTo(parent.key);
    if (x == 0) {
      parent.value = value;
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
   *
   * <ul>修复操作
   *    <li>1.叔叔节点也为红色。</li>
   *    <li>2.叔叔节点为空，且祖父节点、父节点和新节点处于一条斜线上。</li>
   *    <li>3.叔叔节点为空，且祖父节点、父节点和新节点不处于一条斜线上。</li>
   * </ul>
   */
  private void fixInsert(Node<K, V> node) {
    Node<K, V> parent = node.getParent();
    if (parent != null && parent.isRed()) {

      Node<K, V> uncle = getUncle(node);
      if (uncle != null) {
        // case 1: uncle is red
        parent.setRed(false);
        uncle.setRed(false);
        parent.parent.setRed(true);

        // 当前节点node = parent.parent
        // parent = node.parent
        node = parent.parent;
        parent = node.parent;
      }
      else {
        // uncle is null
        Node<K, V> ancestor = parent.getParent();
        boolean isRight = node == parent.left;
        if (parent == ancestor.left) {
          if (isRight) {
            //       a
            //      /
            //     p
            //      \
            //      node
            leftRotate(parent);
            rightRotate(ancestor);

            node.setRed(false);

            // 已经平衡, 停止修复
            // parent = null;
          }
          else {
            //       a
            //      /
            //     p
            //    /
            // node
            rightRotate(ancestor);

            parent.setRed(false);
          }

          // 旋转后都需要将ancestor设为红色
          ancestor.setRed(true);
        }
        else {
          if (isRight) {
            //   a
            //    \
            //     p
            //      \
            //      node
            leftRotate(ancestor);

            ancestor.setRed(true);
            parent.setRed(false);
          }
          else {
            //   a
            //    \
            //     p
            //    /
            //  node
            rightRotate(parent);
            leftRotate(ancestor);

            ancestor.setRed(true);
            node.setRed(false);
          }
        }

        return;
      }
    }

    root.setRed(false);
    root.setParent(null);
  }

  /**
   * 旋转后还得更新对应的parent
   */
  private static void leftRotate(Node node) {

  }

  private static void rightRotate(Node node) {

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

  private Node<K, V> findInsertParentNode(K key) {
    Node<K, V> parent = null;
    Node<K, V> cur = this.root;
    while (cur != null) {
      int x = key.compareTo(cur.key);
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
  private static class Node<K extends Comparable<K>, V> {
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
