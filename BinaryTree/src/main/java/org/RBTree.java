package org;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.Objects;

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
    Node<K, V> x = getNode(key);
    if (x == null) {
      return null;
    }

    V res = x.value;
    deleteNode(x);
    return res;
  }

  private Node<K, V> getNode(K key) {
    Objects.requireNonNull(key);

    Node<K, V> cur = getRoot();
    while (cur != null) {
      int cmp = key.compareTo(cur.key);
      if (cmp < 0) {
        cur = cur.left;
      } else if (cmp > 0) {
        cur = cur.right;
      } else {
        break;
      }
    }
    return cur;
  }

  private void deleteNode(Node<K, V> cur) {
    Node<K, V> p = cur;
    if (cur.left != null && cur.right != null) {
      Node<K, V> s = successor(cur);
      p.key = s.key;
      p.value = s.value;
      p = s;
    }

    Node<K, V> replacement = p.left != null ? p.left : p.right;

    if (replacement != null) {
      replacement.parent = p.parent;
      if (p.parent == null)
        root = replacement;
      else if (p == p.parent.left)
        p.parent.left = replacement;
      else
        p.parent.right = replacement;

      p.parent = p.left = p.right = null;

      if (isBlack(p)) {
        fixAfterRemove(replacement);
      }
    }
    else if (p.parent == null) {
      root = null;
    }
    else {
      if (isBlack(p)) {
        fixAfterRemove(p);
      }

      if (p.parent != null) {
        if (p == p.parent.left) {
          p.parent.left = null;
        }
        else {
          p.parent.right = null;
        }
        p.parent = null;
      }
    }
  }

  /**
   * 4种情况， 其他的是镜像操作
   * <ul>
   * <li>1.兄弟节点是红色</li>
   * <li>兄弟节点是黑色, 且兄弟节点的两个子节点都是黑色</li>
   * <li>兄弟节点是黑色, 兄弟节点子节点靠近删除节点一侧是红色, 另一侧是黑色</li>
   * <li>兄弟节点是黑色, 兄弟节点子节点远离删除节点一侧是红色</li>
   * </ul>
   */
  private void fixAfterRemove(Node<K, V> x) {
    while (x != root && isBlack(x)) {
      Node<K, V> parent = x.parent;
      Node<K, V> sibling = getSibling(x);
      boolean isLeft = (x == parent.left);

      if (sibling.isRed()) {
        sibling.setRed(false);
        parent.setRed(true);

        if (isLeft) {
          leftRotate(parent);
        }
        else {
          rightRotate(parent);
        }
      }

      // sibling is black
      // 需要以父节点为当前节点继续调整, 如果父节点是红色, 直接将父节点设为黑色即可
      else if (isBlack(sibling.left) && isBlack(sibling.right)) {
        sibling.setRed(true);
        x = parent;
      }

      // case3:  sibling节点左子节点靠近删除节点一侧是红，另一侧是黑
      // 这种情况转换成了case4, 需要继续平衡
      else if (isLeft && !isBlack(sibling.left) && isBlack(sibling.right)) {
        sibling.setRed(true);
        sibling.left.setRed(false);
        rightRotate(sibling);
      }
      else if (!isLeft && !isBlack(sibling.right) && isBlack(sibling.left)) {
        sibling.setRed(true);
        sibling.right.setRed(false);
        leftRotate(sibling);
      }

      // case 4: sibling节点远离删除节点的一侧是红
      // 该情况删除后已经平衡, 直接跳过调整
      else if (isLeft && !isBlack(sibling.right)) {
        sibling.setRed(parent.isRed());
        parent.setRed(false);
        sibling.right.setRed(false);
        leftRotate(parent);
        break;
      }
      else if (!isLeft && !isBlack(sibling.left)) {
        sibling.setRed(parent.isRed());
        parent.setRed(false);
        sibling.left.setRed(false);
        rightRotate(parent);
        break;
      }
    }

    // 如果x为root或者红色, 直接染黑即可
    x.setRed(false);
  }

  private Node<K, V> getSibling(Node<K, V> node) {
    Node<K, V> parent = node.parent;
    return parent.left == node ? parent.right : parent.left;
  }

  private static boolean isBlack(Node node) {
    return node == null || !node.isRed();
  }

  private Node<K, V> successor(Node<K, V> node) {
    Node<K, V> cur = node.right;
    if (cur == null) {
      return null;
    }

    while (cur.left != null) {
      cur = cur.left;
    }
    return cur;
  }

  /**
   * 当父节点是黑色的时候不用处理
   *
   * <ul>修复操作
   * <li>1.叔叔节点也为红色。</li>
   * <li>2.叔叔节点为空，且祖父节点、父节点和新节点处于一条斜线上。</li>
   * <li>3.叔叔节点为空，且祖父节点、父节点和新节点不处于一条斜线上。</li>
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
      } else {
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
          } else {
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
        } else {
          if (isRight) {
            //   a
            //    \
            //     p
            //      \
            //      node
            leftRotate(ancestor);

            parent.setRed(false);
          } else {
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
