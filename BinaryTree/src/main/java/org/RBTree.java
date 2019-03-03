package org;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

/**
 * 参考美团技术团队关于红黑树的描述和实现
 */
public class RBTree<K extends Comparable<K>, V> {

  @Getter
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
    this.fixAfterInsert(node);
    return true;
  }

  public V remove(K key) {
    Objects.requireNonNull(key);

    Node<K, V> cur = getRoot();
    while (cur != null) {
      int cmp = key.compareTo(cur.key);
      if (cmp < 0) {
        cur = cur.left;
      }
      else if (cmp > 0) {
        cur = cur.right;
      }
      else {
        if (cur.right == null) {
          Node<K, V> parent = cur.parent;

          if (cur.left != null) {
            cur.left.parent = parent;
          }

          if (parent.left == cur) {
            parent.left = cur.left;
          }
          else {
            parent.right = cur.left;
          }

          if (!cur.isRed()) {

          }

        }
        else {
          // 找到右子树最小的数替换当前节点
          Node<K, V> min = removeMin(cur.right);

        }
      }
    }
  }

  /**
   * 移除node子树的最小节点
   * @param node
   * @return
   */
  private Node<K, V> removeMin(Node<K, V> node) {
    if (node.left == null) {
      return node;
    }

    while (node.left != null) {
      node = node.left;
    }

    Node<K, V> parent = node.parent;
    parent.left = node.right;
    node.right.parent = parent;

    return node;
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
  private void fixAfterInsert(Node<K, V> node) {
    Node<K, V> parent = node.getParent();
    while (parent != null && parent.isRed()) {
      Node<K, V> uncle = getUncle(node);

      // uncle节点存在且和parent节点都是红色的情况下只需要变色，不需要旋转
      // uncle节点不存在或者是黑色（parent节点是红色）， 需要旋转操作
      if (uncle != null && uncle.isRed()) {
        // uncle is red
        parent.setRed(false);
        uncle.setRed(false);
        parent.parent.setRed(true);

        node = parent.parent;
        parent = node.parent;
      }
      else {
        Node<K, V> ancestor = parent.getParent();
        boolean isRight = (node == parent.right);
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

            node.setRed(false);
          }
          ancestor.setRed(true);
        }
        break;
      }
    }

    root.setRed(false);
    root.setParent(null);
  }

  /**
   * 旋转后还得更新对应的parent
   */
  private void leftRotate(Node<K, V> p) {
    //   p
    //    \
    //     r
    //      \
    //      node
    Node<K, V> parent = p.parent;

    Node<K, V> r = p.right;

    p.right = r.left;
    if (r.left != null) {
      p.right.parent = p;
    }

    r.left = p;
    p.parent = r;

    if (parent == null) {
      this.root = r;
      r.parent = null;
    } else {
      if (parent.left == p) {
        parent.left = r;
      } else {
        parent.right = r;
      }
      r.parent = parent;
    }
  }

  private void rightRotate(Node<K, V> p) {
    //         p
    //       /
    //      l
    //    /
    // node
    Node<K, V> parent = p.parent;

    Node<K, V> l = p.left;

    p.left = l.right;
    if (l.right != null) {
      p.left.parent = p;
    }

    l.right = p;
    p.parent = l;

    if (parent == null) {
      this.root = l;
      l.parent = null;
    } else {
      if (parent.left == p) {
        parent.left = l;
      } else {
        parent.right = l;
      }
      l.parent = parent;
    }
  }


  @Nullable
  private Node<K, V> getUncle(Node<K, V> node) {
    Node<K, V> parent = node.getParent();
    Node<K, V> ancestor = parent.getParent();
    if (ancestor == null) {
      return null;
    }

    return ancestor.left == parent ? ancestor.right : ancestor.left;
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
  static class Node<K extends Comparable<K>, V> {
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

    @Override
    public String toString() {
      return "Node{" +
        "key=" + key +
        ", value=" + value +
        ", red=" + red +
        '}';
    }
  }
}
